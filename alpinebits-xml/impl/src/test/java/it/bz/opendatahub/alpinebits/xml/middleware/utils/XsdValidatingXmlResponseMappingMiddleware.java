// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml.middleware.utils;

import it.bz.opendatahub.alpinebits.common.utils.middleware.ComposingMiddlewareBuilder;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.servlet.middleware.ContentTypeHintMiddleware;
import it.bz.opendatahub.alpinebits.servlet.middleware.MultipartFormDataParserMiddleware;
import it.bz.opendatahub.alpinebits.xml.JAXBObjectToXmlConverter;
import it.bz.opendatahub.alpinebits.xml.JAXBXmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.ObjectToXmlConverter;
import it.bz.opendatahub.alpinebits.xml.XmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlValidationSchemaProvider;
import it.bz.opendatahub.alpinebits.xml.middleware.XmlResponseMappingMiddleware;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAResRetrieveRS;

import javax.xml.validation.Schema;
import java.io.InputStream;

/**
 * This helper class for XmlRequestMappingMiddleware configures the middleware
 * chain needed for integration tests, where the XSD validation is active.
 */
public class XsdValidatingXmlResponseMappingMiddleware implements Middleware {

    private static final Key<OTAResRetrieveRS> DEFAULT_CTX_KEY = Key.key("test", OTAResRetrieveRS.class);

    private final Middleware middleware;

    public XsdValidatingXmlResponseMappingMiddleware() {
        this.middleware = ComposingMiddlewareBuilder.compose(
                new ContentTypeHintMiddleware(),
                new MultipartFormDataParserMiddleware(),
                this.validatingMiddleware()
        );
    }

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ResRetrieveRS-reservation.xml");
        Schema schema = XmlValidationSchemaProvider.buildRngSchemaForAlpineBitsVersion("2017-10");
        XmlToObjectConverter<OTAResRetrieveRS> converter = new JAXBXmlToObjectConverter.Builder<>(OTAResRetrieveRS.class).schema(schema).build();
        OTAResRetrieveRS responseData = converter.toObject(is);
        ctx.put(DEFAULT_CTX_KEY, responseData);

        this.middleware.handleContext(ctx, chain);
    }

    private XmlResponseMappingMiddleware<OTAResRetrieveRS> validatingMiddleware() {
        Schema schema = XmlValidationSchemaProvider.buildRngSchemaForAlpineBitsVersion("2017-10");
        ObjectToXmlConverter converter = this.validatingConverter(schema);
        return new XmlResponseMappingMiddleware<>(converter, DEFAULT_CTX_KEY);
    }

    private ObjectToXmlConverter validatingConverter(Schema schema) {
        return new JAXBObjectToXmlConverter.Builder().prettyPrint(true).schema(schema).build();
    }
}
