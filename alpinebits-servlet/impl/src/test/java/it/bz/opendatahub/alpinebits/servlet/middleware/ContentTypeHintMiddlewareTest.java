// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.common.constants.HttpContentTypeHeaderValues;
import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Tests for {@link ContentTypeHintMiddleware}.
 */
public class ContentTypeHintMiddlewareTest {

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_ShouldThrow_WhenResponseWasNotFoundInContext() {
        new ContentTypeHintMiddleware().handleContext(new SimpleContext(), () -> {
        });
    }

    @Test
    public void testHandleContext_ShouldDoNothing_WhenContentTypeHeaderWasFoundInContext() {
        Context ctx = new SimpleContext();
        HttpServletResponse response = prepareResponseMock(Collections.singletonList(ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER));

        ctx.put(ServletContextKey.SERVLET_RESPONSE, response);
        new ContentTypeHintMiddleware().handleContext(ctx, () -> {
        });

        Mockito.verify(response, never()).setHeader(anyString(), anyString());
    }

    @Test
    public void testHandleContext_ShouldDoNothing_WhenNeitherResponseHeaderNorHintAreSet() {
        Context ctx = new SimpleContext();
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        ctx.put(ServletContextKey.SERVLET_RESPONSE, response);
        new ContentTypeHintMiddleware().handleContext(ctx, () -> {
        });

        Mockito.verify(response, never()).setHeader(anyString(), anyString());

        String headerValue = ctx.getOrThrow(ServletContextKey.SERVLET_RESPONSE).getHeader(ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER);
        assertNull(headerValue);
    }

    @Test
    public void testHandleContext_ShouldSetContentTypeHint() {
        String headerValue = HttpContentTypeHeaderValues.TEXT_PLAIN;

        Context ctx = new SimpleContext();
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getHeader(ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER)).thenReturn(headerValue);

        ctx.put(ServletContextKey.SERVLET_RESPONSE, response);
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT, headerValue);

        new ContentTypeHintMiddleware().handleContext(ctx, () -> {
        });
        Mockito.verify(response, times(1)).setHeader(ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER, headerValue);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetHeaderValue_ShouldThrow_WhenResponseIsNull() {
        new ContentTypeHintMiddleware().getHeaderValue(null, ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER);
    }

    @Test
    public void testGetHeaderValue_ShouldReturnEmptyOptionalWhenNameIsNull() {
        HttpServletResponse response = prepareResponseMock(Collections.singletonList(ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER));
        Optional<String> result = new ContentTypeHintMiddleware().getHeaderValue(response, null);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetHeaderValue_ShouldReturnEmptyOptionalWhenNoHeadersAreSet() {
        HttpServletResponse response = prepareResponseMock(Collections.emptyList());
        Optional<String> result = new ContentTypeHintMiddleware().getHeaderValue(response, ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetHeaderValue_ShouldReturnEmptyOptionalWhenContentTypeHeaderIsNotSet() {
        HttpServletResponse response = prepareResponseMock(Collections.singletonList("some header"));
        Optional<String> result = new ContentTypeHintMiddleware().getHeaderValue(response, ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER);
        assertFalse(result.isPresent());
    }


    @Test
    public void testGetHeaderValue_ShouldReturnOptionalWithContentTypeHeaderValue() {
        String headerValue = HttpContentTypeHeaderValues.TEXT_PLAIN;

        // Use lower case header name to test for case insensitivity
        String contentTypeInResponse = ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER.toLowerCase();

        HttpServletResponse response = prepareResponseMock(Collections.singletonList(contentTypeInResponse));
        Mockito.when(response.getHeader(contentTypeInResponse)).thenReturn(headerValue);
        Optional<String> result = new ContentTypeHintMiddleware().getHeaderValue(response, ContentTypeHintMiddleware.RESPONSE_CONTENT_TYPE_HEADER);

        assertTrue(result.isPresent());
        assertEquals(result.get(), headerValue);
    }

    private HttpServletResponse prepareResponseMock(Collection<String> headerNames) {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getHeaderNames()).thenReturn(headerNames);
        return response;
    }
}