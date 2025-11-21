// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl;

import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.servlet.ContextBuilder;
import it.bz.opendatahub.alpinebits.servlet.ContextBuildingException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.ServletOutputStreamBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link DefaultContextBuilder} class.
 */
public class DefaultContextBuilderTest {

    @Test(expectedExceptions = ContextBuildingException.class)
    public void testFromRequest_OutputStreamError() throws Exception {
        ContextBuilder builder = new DefaultContextBuilder();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenThrow(new IOException("Throwing on response#getOutputStream()"));

        String requestId = "REQUEST-ID";

        builder.fromRequest(request, response, requestId);
    }

    @Test
    public void testFromRequest() throws Exception {
        ContextBuilder builder = new DefaultContextBuilder();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(ServletOutputStreamBuilder.getServletOutputStream(null));

        String requestId = "REQUEST-ID";

        Context ctx = builder.fromRequest(request, response, requestId);

        HttpServletRequest requestValue = ctx.getOrThrow(ServletContextKey.SERVLET_REQUEST);
        assertEquals(requestValue, request);

        HttpServletResponse responseValue = ctx.getOrThrow(ServletContextKey.SERVLET_RESPONSE);
        assertEquals(responseValue, response);

        String requestIdValue = ctx.getOrThrow(RequestContextKey.REQUEST_ID);
        assertEquals(requestIdValue, requestId);
    }
}