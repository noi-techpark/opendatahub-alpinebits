/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory.fromxml;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.NotLesserOrEqualValidationException;
import it.bz.opendatahub.alpinebits.validation.NotNullValidationException;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.XmlSchemaType;
import it.bz.opendatahub.alpinebits.xml.XmlConversionException;
import org.testng.annotations.DataProvider;

/**
 * Data providing class for {@link InventoryFromXmlTest}.
 */
public class XmlDataProvider {

    private static final String BASIC = AlpineBitsAction.INVENTORY_BASIC_PUSH;
    private static final String HOTEL_INFO = AlpineBitsAction.INVENTORY_HOTEL_INFO_PUSH;

    @DataProvider(name = "xml")
    // Suppress line-length warnings for this test configuration
    @SuppressWarnings("checkstyle:linelength")
    public static Object[][] xmlProvider() {
        return new Object[][]{
                {"HotelDescriptiveContentNotifRQ_Basic-ok.xml", BASIC, XmlSchemaType.XSD_SCHEMA, null},
                {"HotelDescriptiveContentNotifRQ_Basic-ok.xml", BASIC, XmlSchemaType.RNG_SCHEMA, null},

                {"HotelDescriptiveContentNotifRQ_Basic-ok-delete-all.xml", BASIC, XmlSchemaType.XSD_SCHEMA, null},
                {"HotelDescriptiveContentNotifRQ_Basic-ok-delete-all.xml", BASIC, XmlSchemaType.RNG_SCHEMA, null},

                {"HotelDescriptiveContentNotifRQ_Basic-err-no-hotelcode-and-hotename.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-no-hotelcode-and-hotename.xml", BASIC, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-with-hotelinfo.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-with-hotelinfo.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-with-contactinfos-not-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-with-contactinfos-not-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-guestroom-code-mismatch.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-guestroom-code-mismatch.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-min-occupancy-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-min-occupancy-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-max-occupancy-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-max-occupancy-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-max-child-occupancy-condition.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotLesserOrEqualValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-max-child-occupancy-condition.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotLesserOrEqualValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-standard-occupancy-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-standard-occupancy-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-room-classification-code-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-room-classification-code-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-room-classification-code-invalid.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-room-classification-code-invalid.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-room-id-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-room-id-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-room-classification-code-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-room-classification-code-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-size-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-size-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-standard-occupancy-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-standard-occupancy-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-room-amenity-code-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-room-amenity-code-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-room-amenity-code-invalid.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-room-amenity-code-invalid.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-min-occupancy-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-min-occupancy-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-max-occupancy-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-max-occupancy-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-max-child-occupancy-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-max-child-occupancy-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-id-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-id-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-amenities-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-amenities-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-multimedia-descriptions-notnull.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-multimedia-descriptions-notnull.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-to-many-multimedia-descriptions.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-to-many-multimedia-descriptions.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-duplicate.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-duplicate.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-25-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-25-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-25-text-items-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-25-text-items-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                // TODO: is this still correct?
                // The following test throws a XmlConversionException for XSD Schema only, because
                // XSD schema requires the Description array to be non empty, whereas the RNG schema
                // allows an empty Description array.
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-25-descriptions-empty.xml", BASIC, XmlSchemaType.XSD_SCHEMA, XmlConversionException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-25-descriptions-empty.xml", BASIC, XmlSchemaType.RNG_SCHEMA, EmptyCollectionValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-language-invalid.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-language-invalid.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-language-and-text-format-duplicate.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-language-and-text-format-duplicate.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-1-text-items-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-1-text-items-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                // TODO: is this still correct?
                // The following test throws a XmlConversionException for XSD Schema only, because
                // XSD schema requires the Description array to be non empty, whereas the RNG schema
                // allows an empty Description array.
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-1-descriptions-empty.xml", BASIC, XmlSchemaType.XSD_SCHEMA, XmlConversionException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-1-descriptions-empty.xml", BASIC, XmlSchemaType.RNG_SCHEMA, EmptyCollectionValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-23-image-items-null.xml", BASIC, XmlSchemaType.XSD_SCHEMA, NullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-23-image-items-null.xml", BASIC, XmlSchemaType.RNG_SCHEMA, NullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-23-category-invalid.xml", BASIC, XmlSchemaType.XSD_SCHEMA, ValidationException.class},
                {"HotelDescriptiveContentNotifRQ_Basic-err-info-code-23-category-invalid.xml", BASIC, XmlSchemaType.RNG_SCHEMA, ValidationException.class},

                {"HotelDescriptiveContentNotifRQ_Basic-ok-info-code-23-descriptions-empty.xml", BASIC, XmlSchemaType.XSD_SCHEMA, null},
                {"HotelDescriptiveContentNotifRQ_Basic-ok-info-code-23-descriptions-empty.xml", BASIC, XmlSchemaType.RNG_SCHEMA, null},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-min-occupancy-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema requires the TypeRoom element (which contradicts the 2020-10 standard)
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-min-occupancy-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-max-occupancy-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema requires the TypeRoom element (which contradicts the 2020-10 standard)
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-max-occupancy-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-max-child-occupancy-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema requires the TypeRoom element (which contradicts the 2020-10 standard)
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-max-child-occupancy-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-id-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema requires the TypeRoom element (which contradicts the 2020-10 standard)
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-id-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-amenities-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema requires the TypeRoom element (which contradicts the 2020-10 standard)
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-amenities-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-type-room-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-type-room-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, NotNullValidationException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-info-code-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema is clever enough to recognize that for Inventory/HotelInfo action
                // no InfoCode element is allowed at this position.
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-info-code-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},

                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-text-items-notnull.xml", HOTEL_INFO, XmlSchemaType.XSD_SCHEMA, NotNullValidationException.class},
                // TODO: is this still correct?
                // The following test throws a XmlConversionException for RNG Schema only, because
                // RNG schema is clever enough to recognize that for Inventory/HotelInfo action
                // no TextItems element is allowed at this position.
                {"HotelDescriptiveContentNotifRQ_HotelInfo-err-text-items-notnull.xml", HOTEL_INFO, XmlSchemaType.RNG_SCHEMA, XmlConversionException.class},
        };
    }

}
