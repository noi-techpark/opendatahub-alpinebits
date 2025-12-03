// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl;

import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.servlet.ContextBuilder;
import it.bz.opendatahub.alpinebits.servlet.RequestExceptionHandler;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.EmptyMiddleware;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.EmptyRequestExceptionHandler;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.NullContextBuilder;
import jakarta.servlet.ServletConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

/**
 * Test cases for {@link ServletConfigParserTest} class.
 */
public class ServletConfigParserTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetMiddleware_NoClassname() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.MIDDLEWARE_CLASSNAME))
                .thenReturn(null);

        ServletConfigParser parser = new ServletConfigParser();
        parser.getMiddleware(config);
    }

    @Test(expectedExceptions = ClassNotFoundException.class)
    public void testGetMiddleware_ClassNotFound() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.MIDDLEWARE_CLASSNAME))
                .thenReturn("UnknownClassname");

        ServletConfigParser parser = new ServletConfigParser();
        parser.getMiddleware(config);
    }

    @Test
    public void testGetMiddleware() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.MIDDLEWARE_CLASSNAME))
                .thenReturn(EmptyMiddleware.class.getName());

        ServletConfigParser parser = new ServletConfigParser();
        Middleware middleware = parser.getMiddleware(config);
        assertTrue(middleware instanceof EmptyMiddleware);
    }

    @Test
    public void testGetRequestExceptionHandler_DefaultHandler() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.REQUEST_EXCEPTION_HANDLER_CLASSNAME))
                .thenReturn(null);

        ServletConfigParser parser = new ServletConfigParser();
        RequestExceptionHandler handler = parser.getRequestExceptionHandler(config);
        assertTrue(handler instanceof DefaultRequestExceptionHandler);
    }

    @Test(expectedExceptions = ClassNotFoundException.class)
    public void testGetRequestExceptionHandler_ClassNotFound() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.REQUEST_EXCEPTION_HANDLER_CLASSNAME))
                .thenReturn("UnknownClassname");

        ServletConfigParser parser = new ServletConfigParser();
        parser.getRequestExceptionHandler(config);
    }

    @Test
    public void testGetRequestExceptionHandler() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.REQUEST_EXCEPTION_HANDLER_CLASSNAME))
                .thenReturn(EmptyRequestExceptionHandler.class.getName());

        ServletConfigParser parser = new ServletConfigParser();
        RequestExceptionHandler handler = parser.getRequestExceptionHandler(config);
        assertTrue(handler instanceof EmptyRequestExceptionHandler);
    }

    @Test
    public void testGetContextBuilder_DefaultContextBuilder() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.CONTEXT_BUILDER_CLASSNAME))
                .thenReturn(null);

        ServletConfigParser parser = new ServletConfigParser();
        ContextBuilder builder = parser.getContextBuilder(config);
        assertTrue(builder instanceof DefaultContextBuilder);
    }

    @Test(expectedExceptions = ClassNotFoundException.class)
    public void testGetContextBuilder_ClassNotFound() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.CONTEXT_BUILDER_CLASSNAME))
                .thenReturn("UnknownClassname");

        ServletConfigParser parser = new ServletConfigParser();
        parser.getContextBuilder(config);
    }

    @Test
    public void testGetContextBuilder() throws Exception {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(AlpineBitsServlet.CONTEXT_BUILDER_CLASSNAME))
                .thenReturn(NullContextBuilder.class.getName());

        ServletConfigParser parser = new ServletConfigParser();
        ContextBuilder builder = parser.getContextBuilder(config);
        assertTrue(builder instanceof NullContextBuilder);
    }
}