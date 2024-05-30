// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType;

import java.util.List;

/**
 * Use this validator to validate HotelInfo in AlpineBits 2022 Inventory documents.
 *
 * @see HotelInfoType
 */
public class HotelInfoValidator implements Validator<HotelInfoType, Void> {

    public static final String ELEMENT_NAME = Names.HOTEL_INFO;

    public static final List<String> VALID_CODES =
            it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.HotelInfoValidator.VALID_CODES;
    public static final List<String> VALID_IMAGE_CATEGORIES =
            it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.HotelInfoValidator.VALID_IMAGE_CATEGORIES;
    public static final List<String> VALID_VIDEO_CATEGORIES =
            it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.HotelInfoValidator.VALID_VIDEO_CATEGORIES;

    private static final Validator<HotelInfoType, Void> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.HotelInfoValidator();

    @Override
    public void validate(HotelInfoType hotelInfo, Void ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(hotelInfo, ctx, path);
    }

}
