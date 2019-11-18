package com.ibm.inventory_management.services;

public class CloudServicesException extends Exception {
    public CloudServicesException() {
    }

    public CloudServicesException(String message) {
        super(message);
    }

    public CloudServicesException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudServicesException(Throwable cause) {
        super(cause);
    }

    public CloudServicesException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
