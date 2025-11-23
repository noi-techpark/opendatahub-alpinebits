// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;

/**
 * This class provides builder methods to create {@link Schema}
 * instances for XML validation.
 */
public final class XmlValidationSchemaProvider {

    private XmlValidationSchemaProvider() {
        // Empty
    }

    /**
     * Build and return a RNG schema for the specified AlpineBits version.
     * <p>
     * This method assumes, that the RNG schema file is loadable from the
     * classpath and that the filename corresponds to the following pattern:
     * <p>
     * <code>alpinebits-VERSION.rng</code>
     * <p>
     * where <code>VERSION</code> denotes a valid AlpineBits version (e.g. 2017-10).
     * <p>
     * If no valid RNG file could be found a {@link InvalidSchemaException}
     * will be thrown.
     *
     * @param version build RNG schema for this AlpineBits version
     * @return a {@link Schema} that can be used for XML validation
     * @throws InvalidSchemaException if the file could not be found
     *                                or it wasn't a valid RNG file.
     */
    public static Schema buildRngSchemaForAlpineBitsVersion(String version) {
        return XmlValidationSchemaProvider.buildRngSchema("alpinebits-" + version + ".rng");
    }

    /**
     * Build and return a RNG schema from the file specified by <code>filename</code>.
     * <p>
     * This method assumes, that the RNG schema file is loadable from the
     * classpath.
     * <p>
     * If no valid RNG file could be found a {@link InvalidSchemaException}
     * will be thrown.
     *
     * @param filename name of the RNG file in the classpath
     * @return a {@link Schema} that can be used for XML validation
     * @throws InvalidSchemaException if the file could not be found
     *                                or it wasn't a valid RNG file.
     */
    @SuppressWarnings("checkstyle:illegalcatch")
    public static Schema buildRngSchema(String filename) {
        System.setProperty(
                SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory"
        );
        try {
            return XmlValidationSchemaProvider.buildKnownSchema(filename, XMLConstants.RELAXNG_NS_URI);
        } catch (Exception e) {
            throw XmlValidationSchemaProvider.buildValidationException(filename, "RNG", e);
        }
    }

    /**
     * Build and return a XSD schema for the specified AlpineBits version.
     * <p>
     * This method assumes, that the XSD schema file is loadable from the
     * classpath and that the filename corresponds to the following pattern:
     * <p>
     * <code>alpinebits-VERSION.xsd</code>
     * <p>
     * where <code>VERSION</code> denotes a valid AlpineBits version (e.g. 2017-10).
     * <p>
     * If no valid XSD file could be found a {@link InvalidSchemaException}
     * will be thrown.
     *
     * @param version build XSD schema for this AlpineBits version
     * @return a {@link Schema} that can be used for XML validation
     * @throws InvalidSchemaException if the file could not be found
     *                                or it wasn't a valid XSD file.
     */
    public static Schema buildXsdSchemaForAlpineBitsVersion(String version) {
        return XmlValidationSchemaProvider.buildXsdSchema("alpinebits-" + version + ".xsd");
    }

    /**
     * Build and return a XSD schema from the file specified by <code>filename</code>.
     * <p>
     * This method assumes, that the XSD schema file is loadable from the
     * classpath.
     * <p>
     * If no valid XSD file could be found a {@link InvalidSchemaException}
     * will be thrown.
     *
     * @param filename name of the XSD file in the classpath
     * @return a {@link Schema} that can be used for XML validation
     * @throws InvalidSchemaException if the file could not be found
     *                                or it wasn't a valid XSD file.
     */
    @SuppressWarnings("checkstyle:illegalcatch")
    public static Schema buildXsdSchema(String filename) {
        try {
            return XmlValidationSchemaProvider.buildKnownSchema(filename, XMLConstants.W3C_XML_SCHEMA_NS_URI);
        } catch (Exception e) {
            throw XmlValidationSchemaProvider.buildValidationException(filename, "XSD", e);
        }
    }

    // Suppress "SchemaFactory.newInstance(String) is a security-sensitive method and should not be called with untrusted input"
    @SuppressWarnings("java:S2755")
    private static Schema buildKnownSchema(String filename, String schemaLanguage) throws SAXException {
        URL xsdUrl = XmlValidationSchemaProvider.class.getClassLoader().getResource(filename);
        SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(schemaLanguage)) {
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file");
        }
        return factory.newSchema(xsdUrl);
    }

    private static InvalidSchemaException buildValidationException(String filename, String validationType, Throwable e) {
        return new InvalidSchemaException("Error while building " + validationType + " schema from file " + filename, e);
    }

}
