package com.ibm.hello.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

// Note PowerMock only works with Junit 4 currently!
@RunWith(PowerMockRunner.class)
@PrepareForTest(HelloUtil.class)
public class HelloUtilTest {

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void isEmptyStringWhenNullisProvided() {
        assertTrue(HelloUtil.isEmptyString(null));
    }

    @Test
    public void isEmptyStringWhenNonEmptyStringIsProvided() {
        assertFalse(HelloUtil.isEmptyString("not empty"));

        // Demo mock, override entire class
        PowerMockito.mockStatic(HelloUtil.class);
        when(HelloUtil.isEmptyString(Matchers.anyObject())).thenReturn(true);

        assertTrue(HelloUtil.isEmptyString("not empty"));
        assertFalse(HelloUtil.isEmpty("")); // Mock default boolean mock is false
    }

    @Test
    public void isEmptyWhenCollectionIsProvided() {
        // Partial override
        spy(HelloUtil.class);
        when(HelloUtil.isEmptyString(Matchers.anyObject())).thenReturn(false);

        assertTrue(HelloUtil.isEmpty(new ArrayList<>()));

        // Demo verify
        verifyStatic(HelloUtil.class, Mockito.times(1));
        // For PowerMockito.verifyStatic() you need to invoke the static method that you want to verify
        // AFTER the verifyStatic() call to do the check that it was called
        HelloUtil.isEmptyString(new ArrayList<>());

    }

    @Test
    public void isEmptyWhenStringIsProvided() {
        // Partial override
        spy(HelloUtil.class);
        when(HelloUtil.isEmptyString(Matchers.anyObject())).thenReturn(true);

        assertTrue(HelloUtil.isEmpty(""));
    }
}