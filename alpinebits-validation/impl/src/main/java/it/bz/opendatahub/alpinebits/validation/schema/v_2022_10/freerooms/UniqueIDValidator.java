// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.UniqueIDType;

/**
 * Use this validator to validate the UniqueID in AlpineBits 2022
 * FreeRooms documents.
 *
 * @see UniqueIDType
 */
public class UniqueIDValidator implements Validator<UniqueIDType, Boolean> {

    public static final String ELEMENT_NAME = Names.UNIQUE_ID;

    private static final Validator<UniqueIDType, Boolean> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.freerooms.UniqueIDValidator(
                    ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_DELTAS
            );

    @Override
    public void validate(UniqueIDType uniqueID, Boolean supportsDeltas, ValidationPath path) {
        // Delegate validation to AlpineBits 2017 implementation,
        // since the validation remains the same
        VALIDATION_DELEGATE.validate(uniqueID, supportsDeltas, path);
    }
}
