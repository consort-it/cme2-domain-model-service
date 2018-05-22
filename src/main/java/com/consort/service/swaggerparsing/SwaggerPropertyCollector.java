package com.consort.service.swaggerparsing;

import java.util.List;
import java.util.Map;

import com.consort.entities.PropertyInfo;

import io.swagger.models.Model;
import io.swagger.models.properties.Property;

public interface SwaggerPropertyCollector {
    List<PropertyInfo> getPropertiesFromModel(Model swaggerModel, Map<String, Model> cache);

    public abstract Map<String, Property> getProperties(Model swaggerModel, Map<String, Model> cache);
}