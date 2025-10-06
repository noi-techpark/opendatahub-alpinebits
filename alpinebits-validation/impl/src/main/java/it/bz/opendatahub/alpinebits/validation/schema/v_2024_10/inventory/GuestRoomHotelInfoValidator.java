// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType.GuestRooms.GuestRoom;

/**
 * Use this validator to validate the GuestRoom in AlpineBits 2024
 * Inventory documents.
 *
 * @see GuestRoom
 */
public class GuestRoomHotelInfoValidator implements Validator<GuestRoom, Void> {

    private static final Validator<GuestRoom, Void> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.GuestRoomHotelInfoValidator();

    @Override
    public void validate(GuestRoom guestRoom, Void ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same
        VALIDATION_DELEGATE.validate(guestRoom, ctx, path);
    }

}
