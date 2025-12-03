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
import jakarta.servlet.ServletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides methods to parse a {@link ServletConfig} and return instances of {@link Middleware},
 * {@link RequestExceptionHandler} and {@link ContextBuilder}.
 */
public class ServletConfigParser {

    private static final Logger LOG = LoggerFactory.getLogger(ServletConfigParser.class);

    /**
     * Build a {@link Middleware} using the MIDDLEWARE_CLASSNAME parameter in the {@link ServletConfig}.
     *
     * @param config the config must provide the mandatory MIDDLEWARE_CLASSNAME parameter, specifying a
     *               class that implements the {@link Middleware} interface. If no such parameter could be
     *               found, an {@link IllegalArgumentException} is thrown.
     * @return the class specified by the MIDDLEWARE_CLASSNAME parameter in the {@link ServletConfig}.
     * @throws IllegalArgumentException if no MIDDLEWARE_CLASSNAME parameter could be found
     * @throws ClassNotFoundException   if the class specified by MIDDLEWARE_CLASSNAME could not be found
     * @throws IllegalAccessException   if access to the constructor of the specified class is not allowed
     * @throws InstantiationException   if the class could not be instantiated
     */
    public Middleware getMiddleware(ServletConfig config)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String middlewareClassname = config.getInitParameter(AlpineBitsServlet.MIDDLEWARE_CLASSNAME);

        if (middlewareClassname == null) {
            throw new IllegalArgumentException("The middleware classname provided by the ServletConfig " +
                    "must not be null. Use the MIDDLEWARE_CLASSNAME parameter to provide a classname " +
                    "for a class implementing the Middleware interface");
        }

        LOG.debug("Initializing configured request handler {}", middlewareClassname);

        Class<?> middlewareClass = Class.forName(middlewareClassname);
        return (Middleware) middlewareClass.newInstance();
    }

    /**
     * Build a {@link RequestExceptionHandler} from the given {@link ServletConfig}.
     * <p>
     * If the ServletConfig contains a parameter REQUEST_EXCEPTION_HANDLER_CLASSNAME, an instance of that class
     * is build and returned. If no such parameter exists, an instance of
     * {@link DefaultRequestExceptionHandler} is returned.
     *
     * @param config {@link ServletConfig} that may contain a parameter REQUEST_EXCEPTION_HANDLER_CLASSNAME
     * @return an instance of the class given by REQUEST_EXCEPTION_HANDLER_CLASSNAME, or
     * {@link DefaultRequestExceptionHandler} if that parameter is null.
     * @throws ClassNotFoundException if the class specified by REQUEST_EXCEPTION_HANDLER_CLASSNAME could not be found
     * @throws IllegalAccessException if access to the constructor of the specified class is not allowed
     * @throws InstantiationException if the class could not be instantiated
     */
    public RequestExceptionHandler getRequestExceptionHandler(ServletConfig config)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String requestExceptionHandlerClassname = config.getInitParameter(AlpineBitsServlet.REQUEST_EXCEPTION_HANDLER_CLASSNAME);

        if (requestExceptionHandlerClassname == null) {
            LOG.debug("No request exception handler given, using default {}. Use the " +
                            "REQUEST_EXCEPTION_HANDLER_CLASSNAME parameter in the ServletConfig " +
                            "to provide a classname for a class implementing the " +
                            "RequestExceptionHandler interface",
                    DefaultRequestExceptionHandler.class);
            return new DefaultRequestExceptionHandler();
        }

        LOG.debug("Initializing configured request exception handler {}", requestExceptionHandlerClassname);

        Class<?> requestExceptionHandlerClass = Class.forName(requestExceptionHandlerClassname);
        return (RequestExceptionHandler) requestExceptionHandlerClass.newInstance();
    }

    /**
     * Build a {@link ContextBuilder} from the given {@link ServletConfig}.
     * <p>
     * If the ServletConfig contains a parameter CONTEXT_BUILDER_CLASSNAME, an instance of that class
     * is build and returned. If no such parameter exists, an instance of
     * {@link DefaultContextBuilder} is returned.
     *
     * @param config {@link ServletConfig} that may contain a parameter CONTEXT_BUILDER_CLASSNAME
     * @return an instance of the class given by CONTEXT_BUILDER_CLASSNAME, or
     * {@link DefaultContextBuilder} if that parameter is null.
     * @throws ClassNotFoundException if the class specified by CONTEXT_BUILDER_CLASSNAME could not be found
     * @throws IllegalAccessException if access to the constructor of the specified class is not allowed
     * @throws InstantiationException if the class could not be instantiated
     */
    public ContextBuilder getContextBuilder(ServletConfig config)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String contextBuilderClassname = config.getInitParameter(AlpineBitsServlet.CONTEXT_BUILDER_CLASSNAME);

        if (contextBuilderClassname == null) {
            LOG.debug("No context builder given, using default {}. Use the " +
                            "CONTEXT_BUILDER_CLASSNAME parameter in the ServletConfig " +
                            "to provide a classname for a class implementing the " +
                            "ContextBuilder interface",
                    DefaultRequestExceptionHandler.class);
            return new DefaultContextBuilder();
        }

        LOG.debug("Initializing configured context builder {}", contextBuilderClassname);

        Class<?> contextBuilderClass = Class.forName(contextBuilderClassname);
        return (ContextBuilder) contextBuilderClass.newInstance();
    }
}
