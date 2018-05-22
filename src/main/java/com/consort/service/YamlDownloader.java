package com.consort.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class YamlDownloader implements Runnable {
    final static Logger logger = LoggerFactory.getLogger(YamlDownloader.class);

    private String serviceName;
    private Map<String, String> service2YamlMap;
    private String apiToken;
    private GitlabConnector gitlabConnector;

    public YamlDownloader(String serviceName, Map<String, String> service2YamlMap, String apiToken,
            GitlabConnector gitlabConnector) {
        this.serviceName = serviceName;
        this.service2YamlMap = service2YamlMap;
        this.apiToken = apiToken;
        this.gitlabConnector = gitlabConnector;
    }

    @Override
    public void run() {
        MDC.put("serviceName", this.serviceName);
        logger.info("getting yaml from gitlab for service '{}'", this.serviceName);
        try {
            loadYamlIntoMap();
        } catch (Exception ex) {
            logger.error("Error while processing yaml for service '{}', skipping.", this.serviceName, ex);
        }
        MDC.remove("serviceName");
    }

    private void loadYamlIntoMap() throws Exception {
        String yaml = this.gitlabConnector.getYAMLStringFromGitlabAdapter(this.serviceName, this.apiToken);
        if (yaml == null) {
            logger.warn("Yaml for service '{}' is NULL", this.serviceName);
        } else {
            this.service2YamlMap.put(this.serviceName, yaml);
        }
    }
}