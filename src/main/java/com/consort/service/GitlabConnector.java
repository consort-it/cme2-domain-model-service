package com.consort.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.consort.util.EnvironmentContext;
import com.consort.util.EnvironmentProps;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

public class GitlabConnector {
    final static Logger logger = LoggerFactory.getLogger(GitlabConnector.class);

    public GitlabConnector() {
    }

    public String getYAMLStringFromGitlabAdapter(final String serviceName, final String apiToken) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String hostName = EnvironmentContext.getInstance().getenv(EnvironmentProps.GitLabAdapterUrl);
            String fullUrl = hostName + validatedServiceName(serviceName) + "/content-as-string/swagger.yaml";
            logger.debug("fetching url '{}'", fullUrl);

            HttpGet httpGet = new HttpGet(fullUrl);
            httpGet.setHeader("Authorization", apiToken);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                validateSuccessfulResponse(fullUrl, response);
                final HttpEntity responseEntity = response.getEntity();
                return IOUtils.toString(responseEntity.getContent(), "UTF-8");
            }
        }
    }

    private String validatedServiceName(String serviceName) {
        try {
            return URLEncoder.encode(serviceName, "UTF-8");
        } catch (UnsupportedEncodingException uex) {
            throw new RuntimeException(uex);
        }
    }

    private void validateSuccessfulResponse(String fullUrl, CloseableHttpResponse response) throws Exception {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new Exception(
                    "Could not read content of url " + fullUrl + ": " + response.getStatusLine().getReasonPhrase());
        }
    }
}
