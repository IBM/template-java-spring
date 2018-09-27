package com.ibm.hello.service;

import java.util.HashMap;
import java.util.Map;

public enum ServiceName implements ServiceNameConstants {
    HELLO(HELLO_NAME), HOLA(HOLA_NAME);

    private static final Map<String, ServiceName> INSTANCES = new HashMap<>();

    static {
        for (ServiceName name : values()) {
            INSTANCES.put(name.simpleName.toLowerCase(), name);
        }
    }

    private final String simpleName;

    ServiceName(String simpleName) {
        this.simpleName = simpleName;
    }

    public static ServiceName safeValueOf(String name, ServiceName defaultServiceName) {
        if (name == null || !INSTANCES.containsKey(name.toLowerCase())) {
            return defaultServiceName;
        }

        return INSTANCES.get(name.toLowerCase());
    }

    public String simpleName() {
        return simpleName;
    }
}
