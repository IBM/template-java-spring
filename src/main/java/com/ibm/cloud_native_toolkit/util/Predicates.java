package com.ibm.cloud_native_toolkit.util;

import java.util.function.Predicate;

public class Predicates {
  public static <T> Predicate<T> any() {
    return (T value) -> true;
  }
}
