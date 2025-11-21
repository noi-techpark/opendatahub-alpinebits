// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl;

import it.bz.opendatahub.alpinebits.common.constants.HttpContentTypeHeaderValues;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.ServletOutputStreamBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link ResponseWriter} class.
 */
public class ResponseWriterTest {

    @Test
    public void testWriteMessage_toHttpServletResponse() throws IOException {
        String message = "some test message";
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getOutputStream()).thenReturn(ServletOutputStreamBuilder.getServletOutputStream(stringWriter));

        ResponseWriter.writeMessage(response, message);
        assertEquals(stringWriter.toString(), message);
    }

    @Test
    public void testWriteError() throws IOException {
        String message = "some test message";
        String requestId = "1234567";
        String errorMessage = ResponseWriter.buildErrorMessage(message, requestId);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getOutputStream()).thenReturn(ServletOutputStreamBuilder.getServletOutputStream(stringWriter));

        ResponseWriter.writeError(response, 500, requestId, message);
        verify(response).setStatus(500);
        verify(response, times(1)).setHeader(ResponseWriter.RESPONSE_CONTENT_TYPE_HEADER, HttpContentTypeHeaderValues.TEXT_PLAIN);
        assertEquals(stringWriter.toString(), errorMessage);
    }

    @Test
    public void testBuildErrorMessage() {
        String message = "some test message";
        String requestId = "1234567";

        String errorMessage = ResponseWriter.buildErrorMessage(message, requestId);

        assertEquals(errorMessage, "ERROR:" + message + " [rid=" + requestId + "]");
    }
}