package com.consort;

import com.consort.controller.HealthController;
import com.consort.controller.DomainModelRouteController;
import com.consort.controller.RouteController;
import com.consort.service.S3Connector;

import java.util.HashSet;
import java.util.Set;

public class DomainModelMain {

    private static Set<RouteController> routeControllers = new HashSet<>();

    public static void main(String[] args) {
        new S3Connector().initialize();
        registerRouteControllers();
        initRoutes();
    }

    private static void registerRouteControllers() {
        routeControllers.add(new HealthController());
        routeControllers.add(new DomainModelRouteController());
    }

    private static void initRoutes() {
        for (final RouteController routeController : routeControllers) {
            routeController.initRoutes();
        }
    }
}
