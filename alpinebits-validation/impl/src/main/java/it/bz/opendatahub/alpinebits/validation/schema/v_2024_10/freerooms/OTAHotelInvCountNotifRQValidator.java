// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.HotelInvCountNotifContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelInvCountNotifRQ;

/**
 * Use this validator to validate the OTAHotelInvCountNotifRQ in AlpineBits 2024
 * FreeRooms documents.
 *
 * @see OTAHotelInvCountNotifRQ
 */
public class OTAHotelInvCountNotifRQValidator implements Validator<OTAHotelInvCountNotifRQ, HotelInvCountNotifContext> {

    public static final String ELEMENT_NAME = Names.OTA_HOTEL_INV_COUNT_NOTIF_RQ;
    public static final String COMPLETE_SET = "CompleteSet";

    private static final Validator<OTAHotelInvCountNotifRQ, HotelInvCountNotifContext> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.freerooms.OTAHotelInvCountNotifRQValidator();

    @Override
    public void validate(OTAHotelInvCountNotifRQ hotelInvCountNotifRQ, HotelInvCountNotifContext ctx, ValidationPath unused) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(hotelInvCountNotifRQ, ctx, unused);
    }

}
