/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.HotelInvCountNotifContext;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.InventoriesContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelInvCountNotifRQ;

/**
 * Use this validator to validate the OTAHotelInvCountNotifRQ in AlpineBits 2022
 * FreeRooms documents.
 *
 * @see OTAHotelInvCountNotifRQ
 */
public class OTAHotelInvCountNotifRQValidator implements Validator<OTAHotelInvCountNotifRQ, HotelInvCountNotifContext> {

    public static final String ELEMENT_NAME = Names.OTA_HOTEL_INV_COUNT_NOTIF_RQ;
    public static final String COMPLETE_SET = "CompleteSet";

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    private final UniqueIDValidator uniqueIDValidator = new UniqueIDValidator();
    private final InventoriesValidator inventoriesValidator = new InventoriesValidator();

    @Override
    public void validate(OTAHotelInvCountNotifRQ hotelInvCountNotifRQ, HotelInvCountNotifContext ctx, ValidationPath unused) {
        // Initialize validation path
        ValidationPath path = SimpleValidationPath.fromPath(ELEMENT_NAME);

        VALIDATOR.expectNotNull(
                hotelInvCountNotifRQ,
                ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_RQ_TO_BE_NOT_NULL,
                path
        );
        VALIDATOR.expectNotNull(ctx, ErrorMessage.EXPECT_CONTEXT_TO_BE_NOT_NULL);

        this.uniqueIDValidator.validate(
                hotelInvCountNotifRQ.getUniqueID(),
                ctx.isDeltasSupported(),
                path.withElement(UniqueIDValidator.ELEMENT_NAME)
        );

        InventoriesContext inventoriesContext = this.buildInventoryContext(hotelInvCountNotifRQ, ctx);
        this.inventoriesValidator.validate(
                hotelInvCountNotifRQ.getInventories(),
                inventoriesContext,
                path.withElement(InventoriesValidator.ELEMENT_NAME)
        );
    }

    private InventoriesContext buildInventoryContext(OTAHotelInvCountNotifRQ hotelInvCountNotifRQ, HotelInvCountNotifContext ctx) {
        String instance = hotelInvCountNotifRQ.getUniqueID() != null
                ? hotelInvCountNotifRQ.getUniqueID().getInstance()
                : null;
        return new InventoriesContext(instance, ctx);
    }

}
