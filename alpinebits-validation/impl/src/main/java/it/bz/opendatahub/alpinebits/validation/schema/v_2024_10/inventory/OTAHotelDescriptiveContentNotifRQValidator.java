// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory;

import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRQ;

/**
 * Use this validator to validate the OTAHotelDescriptiveContentNotifRQ in AlpineBits 2024
 * Inventory documents.
 *
 * @see OTAHotelDescriptiveContentNotifRQ
 */
public class OTAHotelDescriptiveContentNotifRQValidator
        implements Validator<OTAHotelDescriptiveContentNotifRQ, InventoryContext> {

    public static final String ELEMENT_NAME = Names.OTA_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_RQ;

    private static final Validator<OTAHotelDescriptiveContentNotifRQ, InventoryContext> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.OTAHotelDescriptiveContentNotifRQValidator();

    @Override
    public void validate(
            OTAHotelDescriptiveContentNotifRQ hotelDescriptiveContentNotifRQ,
            InventoryContext ctx,
            ValidationPath unused
    ) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(hotelDescriptiveContentNotifRQ, ctx, unused);
    }

}
