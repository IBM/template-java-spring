package com.ibm.hello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.ibm.hello.model.GreetingResponse;

public class HolaServiceTest {
    GreetingService service;

    @BeforeEach
    void setup() {
        service = new HolaService();
    }
    
    @Nested
    @DisplayName("Given getGreeting()")
    class GivenGetGreeting {
        @Nested
        @DisplayName("When name is null")
        class WhenNameIsNull {
            @Test
            @DisplayName("Then return 'Hola a todos.'")
            void thenReturnHolaATodos() {

                assertEquals(
                        "Hola a todos.",
                        service.getGreeting(null).getGreeting());
            }
        }

        @Nested
        @DisplayName("When name is not null")
        class WhenNameIsNotNull {
            @Test
            @DisplayName("Then return 'Hola, {name}'.")
            void thenReturnHolaName() {

                final String name = "Martha";
                GreetingResponse greeting = service.getGreeting(name);
                assertEquals(name, greeting.getName());
                assertEquals("Hola, " + name + ".", greeting.getGreeting());
            }
        }
    }
}
