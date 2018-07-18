package com.ibm.hello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HelloService")
public class HelloServiceTest {
    @Nested
    @DisplayName("Given createReply()")
    public class GivenCreateReply {
        @Test
        @DisplayName("When name is `test` then return `Hello, test!`")
        public void when_name_is_test_return_hellotest() {
            final String name = "test";

            HelloService service = new HelloService();

            assertEquals(String.format("Hello, %s!", name), service.createReply(name));
        }

        @Test
        @DisplayName("When name is null then return `Hello, world!`")
        public void when_name_is_null_return_helloworld() {
            HelloService service = new HelloService();

            assertEquals("Hello, world!", service.createReply(null));
        }
    }
}
