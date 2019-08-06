package com.ibm.cloud_garage.logging.inbound;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public interface SimpleFilter extends Filter {
    @Override
    default void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    default void destroy() {
    }
}
