package com.ibm.hello.util;

import java.util.Collection;

public class HelloUtil {

    static boolean isEmptyString(Object o) {
        return o == null || o == "";
    }

    static boolean isEmpty(Object o) {
        if (isEmptyString(o)) {
            return true;
        }

        if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        }

        return false;
    }
}