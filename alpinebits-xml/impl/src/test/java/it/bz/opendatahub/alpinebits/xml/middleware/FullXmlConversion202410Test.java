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
import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.xml.JAXBObjectToXmlConverter;
import it.bz.opendatahub.alpinebits.xml.JAXBXmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.ObjectToXmlConverter;
import it.bz.opendatahub.alpinebits.xml.XmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlValidationSchemaProvider;
import it.bz.opendatahub.alpinebits.xml.middleware.utils.UnitTestDifferenceEvaluator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveInfoRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveInfoRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelInvCountNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelInvCountNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelPostEventNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelPostEventNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelRatePlanNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelRatePlanNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelRatePlanRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelRatePlanRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelResNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelResNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTANotifReportRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAReadRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAResRetrieveRS;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import javax.xml.validation.Schema;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertFalse;

/**
 * This test does a full conversion of XML-to-object
 * and object-to-XML, using {@link XmlRequestMappingMiddleware}
 * and {@link XmlResponseMappingMiddleware}. Each test
 * asserts that the original XML and the result XML
 * are similar.
 */
public class FullXmlConversion202410Test {

    @DataProvider(name = "xmlValid")
    public static Object[][] badBasicAuthentication() {
        return new Object[][]{
                {"Activities-OTA_HotelPostEventNotifRQ.xml", OTAHotelPostEventNotifRQ.class},
                {"Activities-OTA_HotelPostEventNotifRQ-deletion.xml", OTAHotelPostEventNotifRQ.class},
                {"Activities-OTA_HotelPostEventNotifRS-success.xml", OTAHotelPostEventNotifRS.class},

                {"BaseRates-OTA_HotelRatePlanRQ.xml", OTAHotelRatePlanRQ.class},
                {"BaseRates-OTA_HotelRatePlanRS.xml", OTAHotelRatePlanRS.class},

                {"FreeRooms-OTA_HotelInvCountNotifRQ.xml", OTAHotelInvCountNotifRQ.class},
                {"FreeRooms-OTA_HotelInvCountNotifRQ-closing_seasons.xml", OTAHotelInvCountNotifRQ.class},
                {"FreeRooms-OTA_HotelInvCountNotifRQ-delta.xml", OTAHotelInvCountNotifRQ.class},
                {"FreeRooms-OTA_HotelInvCountNotifRQ-empty.xml", OTAHotelInvCountNotifRQ.class},
                {"FreeRooms-OTA_HotelInvCountNotifRS-advisory.xml", OTAHotelInvCountNotifRS.class},
                {"FreeRooms-OTA_HotelInvCountNotifRS-error.xml", OTAHotelInvCountNotifRS.class},
                {"FreeRooms-OTA_HotelInvCountNotifRS-success.xml", OTAHotelInvCountNotifRS.class},
                {"FreeRooms-OTA_HotelInvCountNotifRS-warning.xml", OTAHotelInvCountNotifRS.class},

                {"GuestRequests-Acknowledgments-OTA_NotifReportRQ.xml", OTANotifReportRQ.class},
                {"GuestRequests-Acknowledgments-OTA_NotifReportRQ-warning.xml", OTANotifReportRQ.class},

                {"GuestRequests-OTA_ReadRQ.xml", OTAReadRQ.class},
                {"GuestRequests-OTA_ReadRQ-selection-criteria.xml", OTAReadRQ.class},
                {"GuestRequests-OTA_ResRetrieveRS-cancellation.xml", OTAResRetrieveRS.class},
                {"GuestRequests-OTA_ResRetrieveRS-empty.xml", OTAResRetrieveRS.class},
                {"GuestRequests-OTA_ResRetrieveRS-error.xml", OTAResRetrieveRS.class},
                {"GuestRequests-OTA_ResRetrieveRS-newsletter.xml", OTAResRetrieveRS.class},
                {"GuestRequests-OTA_ResRetrieveRS-quote-request.xml", OTAResRetrieveRS.class},
                {"GuestRequests-OTA_ResRetrieveRS-reservation.xml", OTAResRetrieveRS.class},
                {"GuestRequests-Push-OTA_HotelResNotifRQ.xml", OTAHotelResNotifRQ.class},
                {"GuestRequests-Push-OTA_HotelResNotifRQ-StatusUpdate.xml", OTAHotelResNotifRQ.class},
                {"GuestRequests-Push-OTA_HotelResNotifRS.xml", OTAHotelResNotifRS.class},

                {"Handshake-OTA_PingRQ.xml", OTAPingRQ.class},
                {"Handshake-OTA_PingRS.xml", OTAPingRS.class},

                {"Inventory-Pull-OTA_HotelDescriptiveInfoRQ-basic.xml", OTAHotelDescriptiveInfoRQ.class},
                {"Inventory-Pull-OTA_HotelDescriptiveInfoRQ-hotelinfo.xml", OTAHotelDescriptiveInfoRQ.class},
                {"Inventory-Pull-OTA_HotelDescriptiveInfoRS-basic.xml", OTAHotelDescriptiveInfoRS.class},
                {"Inventory-Pull-OTA_HotelDescriptiveInfoRS-hotelinfo.xml", OTAHotelDescriptiveInfoRS.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRQ-basic.xml", OTAHotelDescriptiveContentNotifRQ.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRQ-delete-all.xml", OTAHotelDescriptiveContentNotifRQ.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRQ-hotelInfo.xml", OTAHotelDescriptiveContentNotifRQ.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRQ-hotelInfo-with-contact-infos.xml", OTAHotelDescriptiveContentNotifRQ.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRQ-hotelInfo-with-roomtype.xml", OTAHotelDescriptiveContentNotifRQ.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRS-advisory.xml", OTAHotelDescriptiveContentNotifRS.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRS-error.xml", OTAHotelDescriptiveContentNotifRS.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRS-success.xml", OTAHotelDescriptiveContentNotifRS.class},
                {"Inventory-Push-OTA_HotelDescriptiveContentNotifRS-warning.xml", OTAHotelDescriptiveContentNotifRS.class},

                {"RatePlans-OTA_HotelRatePlanNotifRQ.xml", OTAHotelRatePlanNotifRQ.class},
                {"RatePlans-OTA_HotelRatePlanNotifRS-advisory.xml", OTAHotelRatePlanNotifRS.class},
                {"RatePlans-OTA_HotelRatePlanNotifRS-error.xml", OTAHotelRatePlanNotifRS.class},
                {"RatePlans-OTA_HotelRatePlanNotifRS-success.xml", OTAHotelRatePlanNotifRS.class},
                {"RatePlans-OTA_HotelRatePlanNotifRS-warning.xml", OTAHotelRatePlanNotifRS.class},
        };
    }

    @Test(dataProvider = "xmlValid")
    public <T> void fullConversion(String xmlFile, Class<T> classToBeBound) {
        String filename = "examples/v_2024_10/" + xmlFile;
        Context ctx = this.prepareCtx(filename);

        Key<T> key = Key.key("data key", classToBeBound);
        Schema schema = XmlValidationSchemaProvider.buildXsdSchemaForAlpineBitsVersion("2024-10");

        // XML to object
        XmlRequestMappingMiddleware<T> xmlToObjectMiddleware = this.validatingXmlToObjectMiddleware(key, classToBeBound, schema);
        xmlToObjectMiddleware.handleContext(ctx, () -> {
        });

        // Object to XML
        XmlResponseMappingMiddleware<T> objectToXmlMiddleware = this.validatingObjectToXmlMiddleware(key, schema);
        objectToXmlMiddleware.handleContext(ctx, () -> {
        });

        InputStream inputXmlStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        ByteArrayOutputStream outputXmlStream = (ByteArrayOutputStream) ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_STREAM);
        String outputXml = outputXmlStream.toString(StandardCharsets.UTF_8);

        Diff xmlDiff = DiffBuilder.compare(inputXmlStream).withTest(outputXml)
                .checkForSimilar()
                .ignoreWhitespace()
                .ignoreComments()
                .withDifferenceEvaluator(new UnitTestDifferenceEvaluator())
                .build();

        if (xmlDiff.hasDifferences()) {
            //CHECKSTYLE:OFF
            System.err.println(filename + " XML conversion difference found:");
            //CHECKSTYLE:ON
            xmlDiff.getDifferences().forEach(System.err::println);
        }

        assertFalse(xmlDiff.hasDifferences());
    }

    private Context prepareCtx(String xmlFile) {
        Context ctx = new SimpleContext();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlFile);
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);

        OutputStream os = new ByteArrayOutputStream();
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_STREAM, os);
        return ctx;
    }

    private <T> XmlRequestMappingMiddleware<T> validatingXmlToObjectMiddleware(Key<T> key, Class<T> classToBeBound, Schema schema) {
        XmlToObjectConverter<T> converter = this.validatingXmlToObjectConverter(classToBeBound, schema);
        return new XmlRequestMappingMiddleware<>(converter, key);
    }

    private <T> XmlToObjectConverter<T> validatingXmlToObjectConverter(Class<T> classToBeBound, Schema schema) {
        return new JAXBXmlToObjectConverter.Builder<>(classToBeBound).schema(schema).build();
    }

    private <T> XmlResponseMappingMiddleware<T> validatingObjectToXmlMiddleware(Key<T> key, Schema schema) {
        ObjectToXmlConverter converter = this.validatingObjectToXmlConverter(schema);
        return new XmlResponseMappingMiddleware<>(converter, key);
    }

    private ObjectToXmlConverter validatingObjectToXmlConverter(Schema schema) {
        return new JAXBObjectToXmlConverter.Builder().schema(schema).prettyPrint(true).build();
    }
}
