package com.consort.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;

@RunWith(Parameterized.class)
public class ServiceNameNormalizerTest {

    private String input;
    private String expected;

    public ServiceNameNormalizerTest(String expected, String input) {
        this.expected = expected;
        this.input = input;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "cme-ui", "cme-ui" }, { "cme-ui", "cme-ui-v1" },
                { "test-service", "test-service-v1" }, { "test-service", "test-service-v142" } });
    }

    @Test
    public void should_return_correct_result() {
        // WHEN / THEN
        String result = ServiceNameNormalizer.normalizeServiceName(input);

        Assert.assertEquals(expected, result);
    }
}
