package com.consort.controller;

import static spark.Service.ignite;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.consort.entities.DomainModel;
import com.consort.entities.IdUtils;
import com.consort.security.AuthorizationFilter;
import com.consort.service.DomainModelService;
import com.consort.service.GitlabConnector;
import com.consort.service.S3Connector;
import com.consort.service.YamlDownloader;
import com.consort.util.ErrorMessage;
import com.consort.util.JsonTransformer;
import com.consort.util.ServiceNameNormalizer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Service;

public class DomainModelRouteController implements RouteController {
    private static ThreadLocal<String> apiToken = new ThreadLocal<String>() {
        @Override
        public String initialValue() {
            return null;
        }
    };

    private static final String METADATA_GET_SERVICE_PATH = "/api/v1/domain-model-service/models/:solutionName";
    private static final String AUTHORIZER_NAME = "scope";
    private static final String ROLE_DEVELOPER = "aws.cognito.signin.user.admin";

    final static Logger logger = LoggerFactory.getLogger(DomainModelRouteController.class);

    public void initRoutes() {

        final Service http = ignite().port(8080);
        enableCORS(http, "*", "GET, POST", "Content-Type, Authorization");

        http.before(METADATA_GET_SERVICE_PATH, new AuthorizationFilter(AUTHORIZER_NAME, ROLE_DEVELOPER));

        http.notFound((req, res) -> {
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage("DMS-404", "404 URL not found"));
        });

        http.exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.type("application/json");
            response.body(new Gson().toJson(new ErrorMessage("DMS-500", e.getMessage())));
        });

        http.before((request, response) -> {
            DomainModelRouteController.apiToken.remove();
        });

        http.get(METADATA_GET_SERVICE_PATH, "application/json", (Request req, Response res) -> {
            S3Connector s3Connector = new S3Connector();
            GitlabConnector gitlabConnector = new GitlabConnector();
            DomainModelService domainModelService = new DomainModelService(s3Connector);

            String apiToken = req.headers("Authorization");
            DomainModelRouteController.apiToken.set(apiToken);

            String solutionName;
            String[] serviceNames;

            try {
                solutionName = req.params("solutionName");
                serviceNames = req.queryParamOrDefault("serviceNames", "").split(",");
                normalizeServiceNames(serviceNames);
                logger.info(
                        "Data objects from services " + StringUtils.join(serviceNames, " ") + " have been requested");
            } catch (Exception e) {
                logger.error("Could not parse input parameters", e);
                res.status(400);
                return new ErrorMessage("DMS-0001", "Could not parse input parameters: " + e.getMessage());
            }

            Map<String, String> service2Yaml = new HashMap<String, String>();

            List<Thread> threadList = new ArrayList<>();
            for (String serviceName : serviceNames) {
                YamlDownloader yamlDownloader = new YamlDownloader(serviceName, service2Yaml,
                        DomainModelRouteController.apiToken.get(), gitlabConnector);
                Thread downloaderThread = new Thread(yamlDownloader);
                downloaderThread.setName("DL-Thread:" + serviceName);
                downloaderThread.start();
                threadList.add(downloaderThread);
            }
            for (Thread thread : threadList) {
                thread.join(60000);
            }

            logger.debug("Finished fetching the interface specification documents, got {}", service2Yaml.size());

            List<DomainModel> models = domainModelService.parseModels(service2Yaml, solutionName);
            res.type("application/json");
            res.status(200);
            return models;
        }, new JsonTransformer());

        http.put(METADATA_GET_SERVICE_PATH, "application/json", (Request req, Response res) -> {
            S3Connector s3Connector = new S3Connector();
            DomainModelService domainModelService = new DomainModelService(s3Connector);

            String apiToken = req.headers("Authorization");
            DomainModelRouteController.apiToken.set(apiToken);
            String solutionName;
            String[] serviceNames;

            try {
                solutionName = req.params("solutionName");
                serviceNames = req.queryParamOrDefault("serviceNames", "").split(",");
                normalizeServiceNames(serviceNames);
                logger.debug("Data objects from services {} have been requested", StringUtils.join(serviceNames, " "));
            } catch (Exception e) {
                logger.error("Could not parse input parameters", e);
                res.status(400);
                return new ErrorMessage("DMS-0001", "Could not parse input parameters: " + e.getMessage());
            }

            Type listType = new TypeToken<List<DomainModel>>() {
            }.getType();
            List<DomainModel> models = new Gson().fromJson(req.body(), listType);
            ErrorMessage em = null;
            for (DomainModel model : models) {
                try {
                    IdUtils.validateDomainModelObject(model);
                } catch (Exception ex) {
                    em = new ErrorMessage("DMS-0002", ex.getMessage());
                    break;
                }
            }

            if (em != null) {
                res.status(400);
                return em;
            }

            domainModelService.saveModels(models, solutionName);
            res.type("application/json");
            res.status(200);
            return models;
        }, new JsonTransformer());

        logger.info("Routes have been initialized");
    }

    private void normalizeServiceNames(String[] serviceNames) {
        Arrays.asList(serviceNames).stream().map(ServiceNameNormalizer::normalizeServiceName)
                .collect(Collectors.toList()).toArray(serviceNames);
    }

    private static void enableCORS(final Service http, final String origin, final String methods,
            final String headers) {

        http.options("/*", (req, res) -> {

            final String acRequestHeaders = req.headers("Access-Control-Request-Headers");

            if (acRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", acRequestHeaders);
            }

            final String accessControlRequestMethod = req.headers("Access-Control-Request-Method");

            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        http.before((req, res) -> {
            res.header("Access-Control-Allow-Origin", origin);
            res.header("Access-Control-Request-Method", methods);
            res.header("Access-Control-Allow-Headers", headers);
            res.type("application/json");
            res.header("Server", "Spark");
        });
    }

}
