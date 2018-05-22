package com.consort.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.consort.controller.DomainModelRouteController;
import com.consort.entities.DomainModel;
import com.consort.entities.IdUtils;
import com.consort.entities.PropertyInfo;
import com.consort.service.swaggerparsing.SwaggerPropertyCollector;
import com.consort.service.swaggerparsing.SwaggerPropertyCollectorFactory;
import com.consort.util.MapSort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

public class YamlToModelParser {

    final private static Logger logger = LoggerFactory.getLogger(DomainModelRouteController.class);

    public static ArrayList<DomainModel> parse(String serviceName, String ymlString) {
        logger.trace("parsing yaml for service '{}'", serviceName);
        ArrayList<DomainModel> entities = new ArrayList<>();
        Swagger swaggerFromString = new SwaggerParser().parse(ymlString);

        if (swaggerFromString != null) {
            Map<String, Model> map = swaggerFromString.getDefinitions();
            if (map != null) {
                map = MapSort.sortByValue(map, new Comparator<Map.Entry<String, Model>>() {
                    public int compare(Map.Entry<String, Model> o1, Map.Entry<String, Model> o2) {
                        boolean o1IsComposed = o1.getValue() instanceof ComposedModel;
                        boolean o2IsComposed = o2.getValue() instanceof ComposedModel;
                        if (o1IsComposed && !o2IsComposed) {
                            return 1;
                        } else if (!o1IsComposed && o2IsComposed) {
                            return -1;
                        }
                        return 0;
                    }
                });

                final Map<String, Model> cacheMap = Collections.unmodifiableMap(map);

                map.forEach((String key, Model swaggerModel) -> {
                    DomainModel domainModel = getDomainModelFromSwagger(serviceName, key, swaggerModel, cacheMap);
                    entities.add(domainModel);
                });
            }
        } else {
            logger.error("There was some error during parsing of yaml for service '{}', skipping. Yaml:\n{}",
                    serviceName, ymlString);
        }
        return entities;
    }

    private static DomainModel getDomainModelFromSwagger(String serviceName, String key, Model swaggerModel,
            Map<String, Model> cache) {
        DomainModel domainModel = new DomainModel();
        domainModel.setName(key);
        domainModel.setServiceName(serviceName);
        domainModel.setId(IdUtils.generateId(serviceName, key));
        domainModel.setHeaderColor("salmon");
        SwaggerPropertyCollector spCollector = new SwaggerPropertyCollectorFactory()
                .createPropertyCollector(swaggerModel);
        List<PropertyInfo> properties = spCollector.getPropertiesFromModel(swaggerModel, cache);
        domainModel.setProperties(properties);
        return domainModel;
    }
}
