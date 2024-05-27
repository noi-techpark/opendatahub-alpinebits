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
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageItemsType.ImageItem;

/**
 * Use this validator to validate the ImageItem in AlpineBits 2022
 * Inventory documents.
 *
 * @see ImageItem
 */
public class ImageItemValidator implements Validator<ImageItem, Void> {

    public static final String ELEMENT_NAME = Names.IMAGE_ITEM;

    private static final Validator<ImageItem, Void> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.ImageItemValidator();

    @Override
    public void validate(ImageItem imageItem, Void ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2017 implementation,
        // since the validation remains the same
        VALIDATION_DELEGATE.validate(imageItem, ctx, path);
    }
}
