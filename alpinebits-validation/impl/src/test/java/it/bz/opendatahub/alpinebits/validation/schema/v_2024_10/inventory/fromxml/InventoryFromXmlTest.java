// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory.fromxml;

import it.bz.opendatahub.alpinebits.validation.XmlSchemaType;
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory.OTAHotelDescriptiveContentNotifRQValidator;
import it.bz.opendatahub.alpinebits.xml.JAXBXmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlValidationSchemaProvider;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRQ;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.validation.Schema;
import java.io.InputStream;

import static org.testng.Assert.expectThrows;

/**
 * Tests with OTA_HotelDescriptiveContentNotifRQ XML documents.
 */
public class InventoryFromXmlTest {

    private final Schema xsdSchema = XmlValidationSchemaProvider.buildXsdSchemaForAlpineBitsVersion("2024-10");

    private XmlToObjectConverter<OTAHotelDescriptiveContentNotifRQ> withXsdSchema;

    @BeforeClass
    public void beforeClass() {
        this.withXsdSchema = new JAXBXmlToObjectConverter.Builder<>(OTAHotelDescriptiveContentNotifRQ.class).schema(xsdSchema).build();
    }

    @Test(dataProvider = "xml", dataProviderClass = XmlDataProvider.class)
    public void testXml(String xmlFile, String action, XmlSchemaType xmlSchemaType, Class<Exception> exceptionClass) {
        String filename = "examples/v_2024_10/inventory/" + xmlFile;
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);

        XmlToObjectConverter<OTAHotelDescriptiveContentNotifRQ> xmlConverter = this.getConverter(xmlSchemaType);

        InventoryContext ctx = new InventoryContext(action);

        OTAHotelDescriptiveContentNotifRQValidator validator = new OTAHotelDescriptiveContentNotifRQValidator();

        if (exceptionClass == null) {
            // Expect no exception

            OTAHotelDescriptiveContentNotifRQ rq = xmlConverter.toObject(is);
            validator.validate(rq, ctx, null);
        } else {
            // Expect exception

            // Wrap XML conversion and validation into "expectThrows", since the
            // XSD/RNG schemas differ in subtle ways. For example, the TypeRoom
            // element is mandatory in RNG schema, where it is not in XSD schema.
            // Therefor, a given file may throw an exception for XSD validation,
            // while it doesn't for RNG validation (and vice versa).
            expectThrows(
                    exceptionClass,
                    // CHECKSTYLE:OFF
                    () -> {
                        OTAHotelDescriptiveContentNotifRQ rq = xmlConverter.toObject(is);
                        validator.validate(rq, ctx, null);
                    }
                    // CHECKSTYLE:ON
            );
        }
    }

    private XmlToObjectConverter<OTAHotelDescriptiveContentNotifRQ> getConverter(XmlSchemaType xmlSchemaType) {
        if (XmlSchemaType.XSD_SCHEMA.equals(xmlSchemaType)) {
            return this.withXsdSchema;
        }
        if (XmlSchemaType.RNG_SCHEMA.equals(xmlSchemaType)) {
            throw new RuntimeException("RNG schema not supported for AlpineBits 2024-10 and later");
        }
        throw new RuntimeException("Unhandled schema type");
    }

}
