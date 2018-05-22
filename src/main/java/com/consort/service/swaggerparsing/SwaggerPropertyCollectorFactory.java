package com.consort.service.swaggerparsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.consort.entities.PropertyInfo;

import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

public class SwaggerPropertyCollectorFactory {
    public SwaggerPropertyCollector createPropertyCollector(Model swaggerModel) {
        if (swaggerModel instanceof ComposedModel) {
            return new ComposedModelPropertyCollector();
        } else if (swaggerModel instanceof RefModel) {
            return new RefModelPropertyCollector();
        }
        return new StandardPropertyCollector();
    }

    private class ComposedModelPropertyCollector extends PropertyCollectorBase {

        @Override
        public Map<String, Property> getProperties(Model swaggerModel, Map<String, Model> cache) {
            Map<String, Property> result = new HashMap<>();
            ComposedModel composedModel = (ComposedModel) swaggerModel;

            List<Model> subModels = composedModel.getAllOf();
            subModels.forEach(model -> {
                Map<String, Property> subModelResults = new SwaggerPropertyCollectorFactory()
                        .createPropertyCollector(model).getProperties(model, cache);
                result.putAll(subModelResults);
            });
            return result;
        }

    }

    private class RefModelPropertyCollector extends PropertyCollectorBase {

        @Override
        public Map<String, Property> getProperties(Model swaggerModel, Map<String, Model> cache) {
            RefModel refModel = (RefModel) swaggerModel;
            String key = refModel.getSimpleRef();
            Map<String, Property> result = cache.get(key).getProperties();
            return result != null ? result : new HashMap<>();
        }
    }

    private class StandardPropertyCollector extends PropertyCollectorBase {

        @Override
        public Map<String, Property> getProperties(Model swaggerModel, Map<String, Model> cache) {
            return swaggerModel.getProperties();
        }
    }

    private abstract class PropertyCollectorBase implements SwaggerPropertyCollector {
        public abstract Map<String, Property> getProperties(Model swaggerModel, Map<String, Model> cache);

        @Override
        public List<PropertyInfo> getPropertiesFromModel(Model swaggerModel, Map<String, Model> cache) {
            Map<String, Property> props = getProperties(swaggerModel, cache);
            ArrayList<PropertyInfo> results = new ArrayList<>();
            if (props != null) {
                props.forEach((String key, Property prop) -> {
                    PropertyInfo pi = getPropertyInfoFromSwagger(key, prop);
                    results.add(pi);
                });
            }
            return results;
        }

        protected PropertyInfo getPropertyInfoFromSwagger(String key, Property prop) {
            PropertyInfo pi = new PropertyInfo();
            pi.setName(key);
            pi.setIsRequired(prop.getRequired());
            pi.setIsArray(prop instanceof ArrayProperty);
            String typeName = getTypeName(prop);
            pi.setType(typeName);
            return pi;
        }

        protected String getTypeName(Property prop) {
            String typeName = prop.getType();
            if (prop instanceof ArrayProperty) {
                Property items = ((ArrayProperty) prop).getItems();
                if (items instanceof RefProperty) {
                    typeName = ((RefProperty) items).getSimpleRef();
                }
            }
            return typeName;
        }
    }
}
