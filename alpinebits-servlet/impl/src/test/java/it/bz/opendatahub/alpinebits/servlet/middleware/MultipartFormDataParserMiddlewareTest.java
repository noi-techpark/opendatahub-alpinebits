// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.servlet.InvalidRequestContentTypeException;
import it.bz.opendatahub.alpinebits.servlet.MultipartFormDataParseException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import it.bz.opendatahub.alpinebits.servlet.UndefinedActionException;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.MultipartFormDataRequestBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link MultipartFormDataParserMiddleware} class.
 */
public class MultipartFormDataParserMiddlewareTest {

    @DataProvider(name = "badContentType")
    public static Object[][] badContentType() {
        return new Object[][]{
                {null},
                {""},
                {"wrong-content-type"},
        };
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestIsNull() {
        Middleware middleware = new MultipartFormDataParserMiddleware();

        Context ctx = new SimpleContext();
        middleware.handleContext(ctx, null);
    }

    @Test(dataProvider = "badContentType", expectedExceptions = InvalidRequestContentTypeException.class)
    public void testHandleContext_NoContentType(String contentType) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentType())
                .thenReturn(contentType);

        this.executeMiddleware(request);
    }

    @Test(expectedExceptions = MultipartFormDataParseException.class)
    public void testHandleContext_MultipartFormDataParseError() throws Exception {
        HttpServletRequest request = MultipartFormDataRequestBuilder.buildRequest("");
        // Set an invalid content type (it misses a boundary)
        when(request.getContentType()).thenReturn("multipart/form-data");

        this.executeMiddleware(request);
    }

    @Test(expectedExceptions = UndefinedActionException.class)
    public void testHandleContext_NoActionParam() throws Exception {
        String s = MultipartFormDataRequestBuilder.buildMultiPartRequestOnly();
        HttpServletRequest request = MultipartFormDataRequestBuilder.buildRequest(s);

        this.executeMiddleware(request);
    }

    @Test
    public void testHandleContext_CheckActionParamInContext() throws Exception {
        String s = MultipartFormDataRequestBuilder.buildMultiPartActionOnly();
        HttpServletRequest request = MultipartFormDataRequestBuilder.buildRequest(s);

        Context ctx = this.executeMiddleware(request);
        String action = ctx.getOrThrow(RequestContextKey.REQUEST_ACTION);
        assertEquals(action, MultipartFormDataRequestBuilder.ALPINEBITS_ACTION_PARAM);
    }

    @Test
    public void testHandleContext_CheckRequestParamInContext() throws Exception {
        String s = MultipartFormDataRequestBuilder.buildMultiPartActionAndRequest();
        HttpServletRequest request = MultipartFormDataRequestBuilder.buildRequest(s);

        Context ctx = this.executeMiddleware(request);
        InputStream stream = ctx.getOrThrow(RequestContextKey.REQUEST_CONTENT_STREAM);
        String alpineBitsRequest = IOUtils.toString(stream, StandardCharsets.UTF_8);
        assertEquals(alpineBitsRequest, MultipartFormDataRequestBuilder.ALPINEBITS_REQUEST_PARAM);
    }

    private Context executeMiddleware(HttpServletRequest request) {
        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new MultipartFormDataParserMiddleware();
        middleware.handleContext(ctx, () -> {
        });

        return ctx;
    }
}