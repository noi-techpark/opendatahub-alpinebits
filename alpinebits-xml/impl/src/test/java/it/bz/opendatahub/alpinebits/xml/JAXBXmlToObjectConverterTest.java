// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml;

import it.bz.opendatahub.alpinebits.xml.entity.TestEntity;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAReadRQ;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertNotNull;

/**
 * Test cases for {@link JAXBXmlToObjectConverter} class.
 */
public class JAXBXmlToObjectConverterTest {

    @Test(expectedExceptions = XmlConversionException.class)
    public void testToObject_Error() {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Throw error while reading");
            }
        };

        XmlToObjectConverter<TestEntity> converter = new JAXBXmlToObjectConverter.Builder<>(TestEntity.class).build();
        converter.toObject(is);
    }

    @Test(expectedExceptions = XmlConversionException.class)
    public void testToObject_Error_WhenXmlIsInvalid() {
        InputStream is = new ByteArrayInputStream("NOT AN XML".getBytes());
        XmlToObjectConverter<TestEntity> converter = new JAXBXmlToObjectConverter.Builder<>(TestEntity.class).build();
        converter.toObject(is);
    }

    @Test(expectedExceptions = XmlConversionException.class)
    public void testToObject_Error_OnXmlMappingError() {
        InputStream is = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?><some></some>".getBytes());
        XmlToObjectConverter<TestEntity> converter = new JAXBXmlToObjectConverter.Builder<>(TestEntity.class).build();
        converter.toObject(is);
    }

    @Test
    public void testToObject_Ok() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");
        XmlToObjectConverter<OTAReadRQ> converter = new JAXBXmlToObjectConverter.Builder<>(OTAReadRQ.class).build();
        OTAReadRQ otaReadRQ = converter.toObject(is);

        assertNotNull(otaReadRQ);
    }
}