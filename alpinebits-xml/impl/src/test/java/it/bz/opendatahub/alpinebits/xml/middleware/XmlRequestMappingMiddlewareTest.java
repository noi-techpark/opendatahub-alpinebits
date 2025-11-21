// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml.middleware;

import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.xml.JAXBXmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlConversionException;
import it.bz.opendatahub.alpinebits.xml.XmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlValidationSchemaProvider;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAReadRQ;
import org.testng.annotations.Test;

import javax.xml.validation.Schema;
import java.io.InputStream;

import static org.testng.Assert.assertNotNull;

/**
 * Test cases for {@link XmlRequestMappingMiddleware} class.
 */
public class XmlRequestMappingMiddlewareTest {

    private static final Key<OTAReadRQ> DEFAULT_CTX_KEY = Key.key("test", OTAReadRQ.class);

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructor_ConverterIsNull() {
        new XmlRequestMappingMiddleware<>(null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructor_BusinessContextKeyIsNull() {
        XmlToObjectConverter<Object> converter = this.notValidatingConverter(Object.class);
        new XmlRequestMappingMiddleware<>(converter, null);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestContentStreamIsNull() {
        Context ctx = this.getDefaultCtx();
        ctx.remove(RequestContextKey.REQUEST_CONTENT_STREAM);
        Middleware middleware = this.notValidatingMiddleware();
        middleware.handleContext(ctx, null);
    }

    @Test
    public void testHandleContext_NoValidation() {
        Context ctx = this.getDefaultCtx();

        Middleware middleware = this.notValidatingMiddleware();
        middleware.handleContext(ctx, () -> {
        });

        OTAReadRQ businessData = ctx.getOrThrow(DEFAULT_CTX_KEY);
        assertNotNull(businessData);
    }

    @Test(expectedExceptions = XmlConversionException.class)
    public void testHandleContext_RngValidationError() {
        Context ctx = this.getDefaultCtx();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ-invalid.xml");
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);

        Schema schema = XmlValidationSchemaProvider.buildRngSchemaForAlpineBitsVersion("2017-10");
        XmlRequestMappingMiddleware<OTAReadRQ> middleware = this.validatingMiddleware(schema);
        middleware.handleContext(ctx, () -> {
        });
    }

    @Test
    public void testHandleContext_RngValidationOk() {
        Context ctx = this.getDefaultCtx();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);

        Schema schema = XmlValidationSchemaProvider.buildRngSchemaForAlpineBitsVersion("2017-10");
        XmlRequestMappingMiddleware<OTAReadRQ> middleware = this.validatingMiddleware(schema);
        middleware.handleContext(ctx, () -> {
        });

        OTAReadRQ businessData = ctx.getOrThrow(DEFAULT_CTX_KEY);
        assertNotNull(businessData);
    }

    @Test(expectedExceptions = XmlConversionException.class)
    public void testHandleContext_XsdValidationError() {
        Context ctx = this.getDefaultCtx();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ-invalid.xml");
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);

        Schema schema = XmlValidationSchemaProvider.buildXsdSchemaForAlpineBitsVersion("2017-10");
        XmlRequestMappingMiddleware<OTAReadRQ> middleware = this.validatingMiddleware(schema);
        middleware.handleContext(ctx, () -> {
        });
    }

    @Test
    public void testHandleContext_XsdValidationOk() {
        Context ctx = this.getDefaultCtx();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);

        Schema schema = XmlValidationSchemaProvider.buildXsdSchemaForAlpineBitsVersion("2017-10");
        XmlRequestMappingMiddleware<OTAReadRQ> middleware = this.validatingMiddleware(schema);
        middleware.handleContext(ctx, () -> {
        });

        OTAReadRQ businessData = ctx.getOrThrow(DEFAULT_CTX_KEY);
        assertNotNull(businessData);
    }

    private Context getDefaultCtx() {
        Context ctx = new SimpleContext();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);
        return ctx;
    }

    private XmlRequestMappingMiddleware<OTAReadRQ> notValidatingMiddleware() {
        XmlToObjectConverter<OTAReadRQ> converter = this.notValidatingConverter(OTAReadRQ.class);
        return new XmlRequestMappingMiddleware<>(converter, DEFAULT_CTX_KEY);
    }

    private XmlRequestMappingMiddleware<OTAReadRQ> validatingMiddleware(Schema schema) {
        XmlToObjectConverter<OTAReadRQ> converter = this.validatingConverter(OTAReadRQ.class, schema);
        return new XmlRequestMappingMiddleware<>(converter, DEFAULT_CTX_KEY);
    }

    private <T> XmlToObjectConverter<T> notValidatingConverter(Class<T> classToBeBound) {
        return new JAXBXmlToObjectConverter.Builder<>(classToBeBound).build();
    }

    private <T> XmlToObjectConverter<T> validatingConverter(Class<T> classToBeBound, Schema schema) {
        return new JAXBXmlToObjectConverter.Builder<>(classToBeBound).schema(schema).build();
    }

}