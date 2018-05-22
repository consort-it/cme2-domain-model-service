package com.consort.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import com.consort.entities.DomainModel;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class YamlToModelParserTest {

        @Test
        public void parseModels_should_return_correct_properties_for_combined_models() throws Exception {
                // GIVEN
                String testYaml = IOUtils.toString(
                                this.getClass().getClassLoader().getResourceAsStream("testdata_with_allOf.yaml"));

                List<DomainModel> result = YamlToModelParser.parse("test-service", testYaml);

                DomainModel projectModel = result.stream().filter(x -> x.getName().equals("Project")).findFirst().get();
                assertEquals(6, projectModel.getProperties().size());
                List<String> propertyNames = projectModel.getProperties().stream().map(x -> x.getName())
                                .collect(Collectors.toList());
                assertTrue(propertyNames.contains("id"));
                assertTrue(propertyNames.contains("description"));
                assertTrue(propertyNames.contains("name"));
                assertTrue(propertyNames.contains("phases"));
                assertTrue(propertyNames.contains("team"));
                assertTrue(propertyNames.contains("context"));
        }

        @Test
        public void parseModels_should_return_correct_properties_for_standard_models() throws Exception {
                // GIVEN
                String testYaml = IOUtils.toString(
                                this.getClass().getClassLoader().getResourceAsStream("testdata_with_allOf.yaml"));

                List<DomainModel> result = YamlToModelParser.parse("test-service", testYaml);

                DomainModel projectModel = result.stream().filter(x -> x.getName().equals("Phase")).findFirst().get();
                assertEquals(2, projectModel.getProperties().size());
                List<String> propertyNames = projectModel.getProperties().stream().map(x -> x.getName())
                                .collect(Collectors.toList());
                assertTrue(propertyNames.contains("name"));
                assertTrue(propertyNames.contains("services"));
        }
}
