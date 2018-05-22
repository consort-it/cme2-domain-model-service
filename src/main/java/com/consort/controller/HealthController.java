package com.consort.controller;

import com.consort.util.StatusMessage;
import spark.Service;

import static spark.Service.ignite;

public class HealthController implements RouteController {

    private static final String HEALTH_PATH = "/api/v1/domain-model-service/health";
    private static final String METRICS_PATH = "/api/v1/domain-model-service/metrics";
    private static final String METRICS_NAME_PATH = "/api/v1/domain-model-service/metrics/:name";

    public void initRoutes() {

        final Service http = ignite().port(8081);

        http.get(HEALTH_PATH, (req, res) -> StatusMessage.CHECK_HEALTH.getValue());

        http.get(METRICS_PATH, (req, res) -> StatusMessage.CHECK_HEALTH.getValue());

        http.get(METRICS_NAME_PATH, (req, res) -> StatusMessage.CHECK_HEALTH.getValue());
    }
}
