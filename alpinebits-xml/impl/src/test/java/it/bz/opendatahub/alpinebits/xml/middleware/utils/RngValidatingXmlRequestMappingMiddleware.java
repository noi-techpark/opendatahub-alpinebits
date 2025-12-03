// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml.middleware.utils;

import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.common.utils.middleware.ComposingMiddlewareBuilder;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.servlet.middleware.MultipartFormDataParserMiddleware;
import it.bz.opendatahub.alpinebits.xml.JAXBXmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlValidationSchemaProvider;
import it.bz.opendatahub.alpinebits.xml.middleware.XmlRequestMappingMiddleware;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAReadRQ;

import javax.xml.validation.Schema;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This helper class for XmlRequestMappingMiddleware configures the middleware
 * chain needed for integration tests, where the RNG validation is active.
 */
public class RngValidatingXmlRequestMappingMiddleware implements Middleware {

    private static final Key<OTAReadRQ> DEFAULT_CTX_KEY = Key.key("test", OTAReadRQ.class);

    private final Middleware middleware;

    public RngValidatingXmlRequestMappingMiddleware() {
        this.middleware = ComposingMiddlewareBuilder.compose(Arrays.asList(
                new MultipartFormDataParserMiddleware(),
                this.validatingMiddleware()
        ));
    }

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        this.middleware.handleContext(ctx, chain);

        OTAReadRQ otaReadRQ = ctx.getOrThrow(RngValidatingXmlRequestMappingMiddleware.DEFAULT_CTX_KEY);

        OutputStream os = ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_STREAM);

        try {
            os.write(otaReadRQ.getClass().toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private XmlRequestMappingMiddleware<OTAReadRQ> validatingMiddleware() {
        Schema schema = XmlValidationSchemaProvider.buildRngSchemaForAlpineBitsVersion("2017-10");
        XmlToObjectConverter<OTAReadRQ> converter = this.validatingConverter(OTAReadRQ.class, schema);
        return new XmlRequestMappingMiddleware<>(converter, DEFAULT_CTX_KEY);
    }

    private <T> XmlToObjectConverter<T> validatingConverter(Class<T> classToBeBound, Schema schema) {
        return new JAXBXmlToObjectConverter.Builder<>(classToBeBound).schema(schema).build();
    }
}
