// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.freerooms.fromxml;

import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.XmlSchemaType;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.HotelInvCountNotifContext;
import it.bz.opendatahub.alpinebits.xml.XmlConversionException;
import org.testng.annotations.DataProvider;

/**
 * Data providing class for {@link FreeRoomsFromXmlTest}.
 */
public class XmlDataProvider {

    private static final HotelInvCountNotifContext DEFAULT_CTX = new HotelInvCountNotifContext.Builder().build();

    private static final HotelInvCountNotifContext CTX_WITH_CATEGORIES_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withCategoriesSupport().build();
    private static final HotelInvCountNotifContext CTX_WITH_CATEGORIES_AND_ROOMS_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withCategoriesSupport().withRoomsSupport().build();

    private static final HotelInvCountNotifContext CTX_WITH_ROOMS_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withRoomsSupport().build();

    private static final HotelInvCountNotifContext CTX_WITH_OUT_OF_MARKET_AND_CATEGORIES_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withOutOfMarketSupport().withCategoriesSupport().build();

    private static final HotelInvCountNotifContext CTX_WITH_OUT_OF_ORDER_AND_CATEGORIES_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withOutOfOrderSupport().withCategoriesSupport().build();

    private static final HotelInvCountNotifContext CTX_WITH_CLOSING_SEASONS_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withClosingSeasonsSupport().build();
    private static final HotelInvCountNotifContext CTX_WITH_CLOSING_SEASONS_AND_CATEGORIES_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withClosingSeasonsSupport().withCategoriesSupport().build();

    private static final HotelInvCountNotifContext CTX_WITH_DELTAS_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withDeltasSupport().build();
    private static final HotelInvCountNotifContext CTX_WITH_DELTAS_AND_ROOMS_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withDeltasSupport().withRoomsSupport().build();
    private static final HotelInvCountNotifContext CTX_WITH_DELTAS_AND_CATEGORIES_SUPPORT = new HotelInvCountNotifContext.Builder()
            .withDeltasSupport().withCategoriesSupport().build();

    @DataProvider(name = "xml")
    // Suppress line-length warnings for this test configuration
    @SuppressWarnings("checkstyle:linelength")
    public static Object[][] xmlProvider() {
        return new Object[][]{
                ////////////////////////////////
                // Semantic Errors
                ////////////////////////////////

                // FreeRooms update for categories and rooms can not be mixed
                {"HotelAvailNotifRQ_err_categories-and-rooms-mixed.xml", CTX_WITH_CATEGORIES_AND_ROOMS_SUPPORT, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Closing-Seasons elements must not have InvCounts elements
                {"HotelAvailNotifRQ_err_closing-seasons-no-inv-counts-element-allowed.xml", CTX_WITH_CLOSING_SEASONS_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Closing-Seasons elements need to be on the top of the Inventory list
                {"HotelAvailNotifRQ_err_closing-seasons-not-on-top.xml", CTX_WITH_CLOSING_SEASONS_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Closing seasons must not overlap
                {"HotelAvailNotifRQ_err_closing-seasons-must-not-overlap.xml", CTX_WITH_CLOSING_SEASONS_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Since 2024-10 HotelCode is mandatory
                {"HotelAvailNotifRQ_err_no-hotelcode.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, XmlConversionException.class},

                // Reset message (single empty "Inventory" element) requires "UniqueID" element, independently of delta supported.
                // To not trigger a different error ("UniqueID" element requires delta support), delta support is set for this test
                {"HotelAvailNotifRQ_err_no-uniqueid-and-reset.xml", CTX_WITH_DELTAS_SUPPORT, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},

                ////////////////////////////////
                // OK
                ////////////////////////////////

                // Closing-Seasons elements need to be on the top of the Inventory list, have StatusApplicationControl->AllInvCode attribute
                // set to "true" and have no InvCounts elements
                {"HotelAvailNotifRQ_ok_closing-seasons.xml", CTX_WITH_CLOSING_SEASONS_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Full update for categories
                {"HotelAvailNotifRQ_ok_complete-set-and-categories.xml", CTX_WITH_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Full update for rooms
                {"HotelAvailNotifRQ_ok_complete-set-and-rooms.xml", CTX_WITH_ROOMS_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Delta update for categories
                {"HotelAvailNotifRQ_ok_delta-and-categories.xml", CTX_WITH_DELTAS_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Delta update for rooms
                {"HotelAvailNotifRQ_ok_delta-and-rooms.xml", CTX_WITH_DELTAS_AND_ROOMS_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // A reset message needs to have a "UniqueID" element with "Instance"="CompleteSet" and an empty Inventory element
                {"HotelAvailNotifRQ_ok_reset.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, null},

                ////////////////////////////////
                // Server Support
                ////////////////////////////////

                // Categories support enabled
                {"HotelAvailNotifRQ_support_categories.xml", CTX_WITH_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Categories not supported
                {"HotelAvailNotifRQ_support_categories.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Closing-Seasons support enabled
                {"HotelAvailNotifRQ_support_closing-seasons.xml", CTX_WITH_CLOSING_SEASONS_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Closing-Seasons not supported
                {"HotelAvailNotifRQ_support_closing-seasons.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Deltas support enabled (need additional either categories or rooms support)
                {"HotelAvailNotifRQ_support_deltas.xml", CTX_WITH_DELTAS_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Deltas not supported
                {"HotelAvailNotifRQ_support_deltas.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Out-of-market support enabled (need additional either categories or rooms support)
                {"HotelAvailNotifRQ_support_out-of-market.xml", CTX_WITH_OUT_OF_MARKET_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Out-of-market not supported
                {"HotelAvailNotifRQ_support_out-of-market.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Out-of-order support enabled (need additional either categories or rooms support)
                {"HotelAvailNotifRQ_support_out-of-order.xml", CTX_WITH_OUT_OF_ORDER_AND_CATEGORIES_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Out-of-order not supported
                {"HotelAvailNotifRQ_support_out-of-order.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, ValidationException.class},

                // Rooms support enabled
                {"HotelAvailNotifRQ_support_rooms.xml", CTX_WITH_ROOMS_SUPPORT, XmlSchemaType.XSD_SCHEMA, null},

                // Rooms not supported
                {"HotelAvailNotifRQ_support_rooms.xml", DEFAULT_CTX, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
        };
    }

}
