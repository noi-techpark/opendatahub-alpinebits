// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml;

import it.bz.opendatahub.alpinebits.xml.schema.ota.ObjectFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * This class provides methods to convert OTA-2015A objects to XML.
 * <p>
 * Please note, that this class can only be used with OTA-2015A objects
 * declared in the {@link ObjectFactory}, since those are the only
 */
public final class JAXBObjectToXmlConverter implements ObjectToXmlConverter {

    private final Schema schema;
    private final boolean doPrettyPrintXml;
    private final Schema otaSchema;

    private JAXBObjectToXmlConverter(Schema schema, boolean doPrettyPrintXml) {
        this.schema = schema;
        this.doPrettyPrintXml = doPrettyPrintXml;
        this.otaSchema = OtaSchemaSingleton.getInstance();
    }

    @Override
    public void toXml(Object objectToConvert, OutputStream os) {
        try {
            Marshaller marshaller = JAXBContextSingleton.getInstance().createMarshaller();
            marshaller.setSchema(this.schema);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, this.doPrettyPrintXml);

            // Write XML to temporary string such that it can be reused for OTA 2015a XSD validation
            ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
            marshaller.marshal(objectToConvert, tmpStream);
            String xml = tmpStream.toString();

            // Validate against OTA 2015a XSD
            Validator validator = this.otaSchema.newValidator();
            validator.validate(toSource(xml));

            // Write XML to output stream
            os.write(xml.getBytes(StandardCharsets.UTF_8));
        } catch (JAXBException | SAXException | IOException e) {
            String message = "Object-to-XML conversion error: " + (e.getMessage() == null ? e.toString() : e.getMessage());
            throw new XmlConversionException(message, e);
        }
    }

    private Source toSource(String xml) {
        return new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Builder to create instances of {@link JAXBObjectToXmlConverter}.
     */
    public static class Builder {

        private Schema schema;
        private boolean doPrettyPrintXml;

        /**
         * The {@link Schema} is used for XML validation.
         * <p>
         * If the schema is null (default: null), no validation will
         * be performed.
         *
         * @param schema the {@link Schema} used for XML validation
         * @return the current Builder
         */
        public Builder schema(Schema schema) {
            this.schema = schema;
            return this;
        }

        /**
         * Configure, if the resulting XML should be pretty printed
         * (default: false).
         *
         * @param doPrettyPrintXml if <code>true</code>, the resuling
         *                         XML will be pretty printed
         * @return the current Builder
         */
        public Builder prettyPrint(boolean doPrettyPrintXml) {
            this.doPrettyPrintXml = doPrettyPrintXml;
            return this;
        }

        /**
         * Build an instance of {@link JAXBObjectToXmlConverter} with
         * the current configuration.
         *
         * @return instance of {@link JAXBObjectToXmlConverter}
         */
        public ObjectToXmlConverter build() {
            return new JAXBObjectToXmlConverter(this.schema, this.doPrettyPrintXml);
        }
    }
}
