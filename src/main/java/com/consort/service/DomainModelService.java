package com.consort.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.consort.entities.DomainModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DomainModelService {
    final private static Logger logger = LoggerFactory.getLogger(DomainModelService.class);
    private S3Connector s3Service;

    public DomainModelService(S3Connector s3Service) {
        this.s3Service = s3Service;
    }

    public List<DomainModel> parseModels(Map<String, String> serviceYamls, String projectName) throws Exception {
        ArrayList<DomainModel> modelsFromAllServices = new ArrayList<>();
        serviceYamls.forEach((String serviceName, String yaml) -> {
            if (yaml != null) {
                MDC.put("parseYaml", serviceName);
                try {
                    List<DomainModel> models = YamlToModelParser.parse(serviceName, yaml);
                    modelsFromAllServices.addAll(models);
                } catch (Exception ex) {
                    logger.error("Error while parsing yaml for {} in project {}, ignoring this service.", serviceName,
                            projectName, ex);
                    logger.error("yaml:\n{}", yaml);
                }
                MDC.remove("parseYaml");
            } else {
                logger.warn("yaml for service '{}' is NULL, skipping.", serviceName);
            }
        });
        enrichModelsWithSavedProps(modelsFromAllServices, projectName + ".json");
        return modelsFromAllServices;
    }

    public void saveModels(List<DomainModel> models, String projectName) {
        String objectName = projectName + ".json";
        try {
            logger.trace("Saving models to S3 object '{}'", objectName);
            this.s3Service.updateObject(objectName, models);
            logger.debug("Saved '{}' successfully", objectName);
        } catch (Exception ex) {
            logger.error("Could not save model props for '{}' in projectName '{}' to S3", objectName, projectName, ex);
            throw ex;
        }
    }

    private void enrichModelsWithSavedProps(List<DomainModel> models, String projectName) {
        logger.trace("Reading data from S3 file: " + projectName);
        List<DomainModel> s3Data;
        try {
            s3Data = this.s3Service.getObject(projectName);
            logger.trace("Got data for project '{}'\n{}", projectName, s3Data);
            models.forEach(model -> {
                Optional<DomainModel> fromS3 = s3Data.stream().filter(m -> m.getId().equals(model.getId())).findFirst();

                if (fromS3.isPresent()) {
                    DomainModel data = fromS3.get();
                    model.setHeaderColor(data.getHeaderColor());
                    model.setIconName(data.getIconName());
                    model.setPositionX(data.getPositionX());
                    model.setPositionY(data.getPositionY());
                    model.setZIndex(data.getZIndex());
                }
            });
            logger.debug("Enriched data of models with info from S3: {}", models);
        } catch (AmazonS3Exception ex) {
            if (!"NoSuchKey".equals(ex.getErrorCode())) {
                logger.error("Error while loading domain model data from S3 (" + projectName + ")", ex);
                throw ex;
            }
            logger.warn("Did not find data for '{}' in S3", projectName);
        }
    }
}
