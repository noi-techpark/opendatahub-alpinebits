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
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType.GuestRooms;

/**
 * Use this validator to validate the GuestRooms in AlpineBits 2024
 * Inventory documents.
 *
 * @see GuestRooms
 */
public class GuestRoomsValidator implements Validator<GuestRooms, InventoryContext> {

    public static final String ELEMENT_NAME = Names.GUEST_ROOMS;

    private static final Validator<GuestRooms, InventoryContext> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.GuestRoomsValidator();

    @Override
    public void validate(GuestRooms guestRooms, InventoryContext ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same
        VALIDATION_DELEGATE.validate(guestRooms, ctx, path);
    }

}
