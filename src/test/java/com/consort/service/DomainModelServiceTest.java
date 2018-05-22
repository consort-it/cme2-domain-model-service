package com.consort.service;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

import com.consort.entities.DomainModel;

public class DomainModelServiceTest {

    @Test
    public void parseModels_should_return_empty_list_when_no_yamls_are_present() throws Exception {

        // GIVEN
        Map<String, String> yamls = new HashMap<>();
        yamls.put("test-service1", null);

        S3Connector mockS3 = mock(S3Connector.class);
        // WHEN / THEN
        List<DomainModel> result = new DomainModelService(mockS3).parseModels(yamls, "testProject");

        assertEquals(0, result.size());
    }

    @Test
    public void parseModels_should_return_standard_domain_models_when_yamls_are_present() throws Exception {
        String testYaml = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("testdata.yaml"));
        // GIVEN
        Map<String, String> yamls = new HashMap<>();
        yamls.put("test-service1", testYaml);

        S3Connector mockS3 = mock(S3Connector.class);
        when(mockS3.getObject(anyString())).thenReturn(new ArrayList<DomainModel>());

        // WHEN / THEN
        List<DomainModel> result = new DomainModelService(mockS3).parseModels(yamls, "testProject");
        assertEquals("Environment", result.get(0).getName());
        assertEquals("FeatureToggle", result.get(1).getName());
        assertEquals(null, result.get(1).getPositionX());
    }

    @Test
    public void parseModels_should_return_standard_domain_models_with_enriched_data_when_yamls_and_enrichment_data_are_present()
            throws Exception {
        String testYaml = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("testdata.yaml"));
        // GIVEN
        Map<String, String> yamls = new HashMap<>();
        yamls.put("test-service1", testYaml);

        DomainModel model1 = new DomainModel().id("test-service1_Environment").positionX(42).positionY(100).zIndex(17)
                .iconName("test").headerColor("goldenrod");
        DomainModel model2 = new DomainModel().id("test-service1_FeatureToggle").positionX(12).positionY(5).zIndex(1)
                .iconName("foo").headerColor("salmon");

        List<DomainModel> enrichmentData = new ArrayList<DomainModel>();
        enrichmentData.add(model1);
        enrichmentData.add(model2);

        S3Connector mockS3 = mock(S3Connector.class);
        when(mockS3.getObject(anyString())).thenReturn(enrichmentData);

        // WHEN / THEN
        List<DomainModel> result = new DomainModelService(mockS3).parseModels(yamls, "testProject");
        assertEquals("Environment", result.get(0).getName());
        assertTrue(result.get(0).getPositionX() == 42);
        assertTrue(result.get(0).getPositionY() == 100);
        assertTrue(result.get(0).getZIndex() == 17);
        assertEquals("test", result.get(0).getIconName());
        assertEquals("goldenrod", result.get(0).getHeaderColor());

        assertEquals("FeatureToggle", result.get(1).getName());
        assertTrue(result.get(1).getPositionX() == 12);
        assertTrue(result.get(1).getPositionY() == 5);
        assertTrue(result.get(1).getZIndex() == 1);
        assertEquals("foo", result.get(1).getIconName());
        assertEquals("salmon", result.get(1).getHeaderColor());
    }
}
