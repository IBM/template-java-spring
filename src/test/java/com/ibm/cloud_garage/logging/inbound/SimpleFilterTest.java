package com.ibm.cloud_garage.logging.inbound;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SimpleFilter")
public class SimpleFilterTest {
    private SimpleFilter classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new SimpleFilterImpl();
    }

    @Nested
    @DisplayName("Given init()")
    public class GivenInit {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then do nothing")
            public void thenDoNothing() throws ServletException {
                classUnderTest.init(null);
            }
        }
    }

    @Nested
    @DisplayName("Given destroy()")
    public class GivenDestroy {
        @Nested
        @DisplayName("When called")
        public class WhenCalled {
            @Test
            @DisplayName("Then do nothing")
            public void thenDoNothing() {
                classUnderTest.destroy();
            }
        }
    }

    private static class SimpleFilterImpl implements SimpleFilter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
        }
    }
}
