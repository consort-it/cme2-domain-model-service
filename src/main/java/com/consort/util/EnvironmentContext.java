package com.consort.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentContext {

    private static EnvironmentContext context = null;
    private Dotenv dotenv = null;

    private EnvironmentContext() {
        initEnvironment();
    }

    public static EnvironmentContext getInstance() {
        if (context == null) {
            context = new EnvironmentContext();
        }
        return context;
    }

    private void initEnvironment() {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    public String getenv(final EnvironmentProps propertyName) {
        return this.dotenv.get(propertyName.toString());
    }
}
