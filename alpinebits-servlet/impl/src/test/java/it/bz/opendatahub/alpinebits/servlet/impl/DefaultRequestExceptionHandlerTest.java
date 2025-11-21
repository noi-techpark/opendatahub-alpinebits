// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl;

import it.bz.opendatahub.alpinebits.common.exception.AlpineBitsException;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.ServletOutputStreamBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link DefaultRequestExceptionHandlerTest} class.
 */
public class DefaultRequestExceptionHandlerTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @DataProvider(name = "exceptions")
    public static Object[][] exceptions() {
        return new Object[][]{
                {new AlpineBitsException(EXCEPTION_MESSAGE, 99), HttpServletResponse.SC_INTERNAL_SERVER_ERROR},
                {new AlpineBitsException(EXCEPTION_MESSAGE, 100), 100},
                {new AlpineBitsException(EXCEPTION_MESSAGE, 599), 599},
                {new AlpineBitsException(EXCEPTION_MESSAGE, 600), HttpServletResponse.SC_INTERNAL_SERVER_ERROR},
                {new RequiredContextKeyMissingException(EXCEPTION_MESSAGE), HttpServletResponse.SC_INTERNAL_SERVER_ERROR},
                {new Exception(EXCEPTION_MESSAGE), HttpServletResponse.SC_INTERNAL_SERVER_ERROR},
        };
    }

    @Test(dataProvider = "exceptions")
    public void testHandleRequestException(Exception e, int expectedStatusCode) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        when(response.getOutputStream()).thenReturn(ServletOutputStreamBuilder.getServletOutputStream(stringWriter));

        DefaultRequestExceptionHandler handler = new DefaultRequestExceptionHandler();

        handler.handleRequestException(request, response, e);

        String expectedErrorMessage = ResponseWriter.buildErrorMessage(e.getMessage(), null);

        verify(response).setStatus(expectedStatusCode);
        assertEquals(stringWriter.toString(), expectedErrorMessage);
    }
}