package com.oneassist.serviceplatform.webservice;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class App extends ResourceConfig {

    public App() {
        // which is a Spring filter that provides a bridge
        // between JAX-RS and Spring request attributes
        register(RequestContextFilter.class);

        register(JacksonFeature.class);

        register(LoggingFilter.class);
    }
}