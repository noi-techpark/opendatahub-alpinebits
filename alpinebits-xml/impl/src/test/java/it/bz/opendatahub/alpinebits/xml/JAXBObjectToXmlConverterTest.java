// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml;

import it.bz.opendatahub.alpinebits.xml.schema.ota.MessageAcknowledgementType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelAvailNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ObjectFactory;
import it.bz.opendatahub.alpinebits.xml.schema.ota.SuccessType;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import static org.testng.Assert.assertTrue;

/**
 * Test cases for {@link JAXBObjectToXmlConverter} class.
 */
public class JAXBObjectToXmlConverterTest {

    @Test(expectedExceptions = XmlConversionException.class)
    public void testToXml_Error() {
        OTAPingRQ pingRQ = new OTAPingRQ();
        pingRQ.setEchoData("Some echo data");

        OutputStream os = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Throw error while writing");
            }
        };

        ObjectToXmlConverter converter = new JAXBObjectToXmlConverter.Builder().prettyPrint(true).build();
        converter.toXml(pingRQ, os);
    }

    @Test
    public void testToXml_Ok() {
        OTAPingRQ pingRQ = new OTAPingRQ();
        pingRQ.setVersion(BigDecimal.ONE);
        pingRQ.setEchoData("Some echo data");

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        ObjectToXmlConverter converter = new JAXBObjectToXmlConverter.Builder().prettyPrint(true).build();
        converter.toXml(pingRQ, os);

        assertTrue(os.size() > 0);
    }

    @Test
    public void testToXml_ShouldUseGenericType_WhenClassToBeBoundExtendsJAXBElement() {
        MessageAcknowledgementType mat = new MessageAcknowledgementType();
        mat.setSuccess(new SuccessType());
        ObjectFactory of = new ObjectFactory();
        OTAHotelAvailNotifRS data = of.createOTAHotelAvailNotifRS(mat);
        data.getValue().setVersion(BigDecimal.ONE);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        ObjectToXmlConverter converter = new JAXBObjectToXmlConverter.Builder().prettyPrint(true).build();
        converter.toXml(data, os);

        assertTrue(os.size() > 0);
    }
}