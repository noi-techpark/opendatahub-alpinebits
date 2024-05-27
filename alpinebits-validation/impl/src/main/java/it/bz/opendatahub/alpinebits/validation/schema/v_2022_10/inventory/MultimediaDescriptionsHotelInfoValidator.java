/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.MultimediaDescriptionsType;

/**
 * Use this validator to validate the MultimediaDescriptionsType in AlpineBits 2022
 * Inventory documents.
 *
 * @see MultimediaDescriptionsType
 */
public class MultimediaDescriptionsHotelInfoValidator implements Validator<MultimediaDescriptionsType, Void> {

    public static final String ELEMENT_NAME = Names.MULTIMEDIA_DESCRIPTIONS;

    private static final Validator<MultimediaDescriptionsType, Void> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.MultimediaDescriptionsHotelInfoValidator();

    @Override
    public void validate(MultimediaDescriptionsType multimediaDescriptions, Void ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2017 implementation,
        // since the validation remains the same
        VALIDATION_DELEGATE.validate(multimediaDescriptions, ctx, path);
    }
}