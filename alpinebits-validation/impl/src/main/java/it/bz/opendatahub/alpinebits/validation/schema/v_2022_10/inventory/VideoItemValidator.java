/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.common.constants.OTACodePictureCategoryCode;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.common.Description;
import it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.common.DescriptionsValidator;
import it.bz.opendatahub.alpinebits.validation.utils.ListUtil;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoItemsType.VideoItem;

import java.util.List;

/**
 * Use this validator to validate the VideoItem in AlpineBits 2022 Inventory documents.
 *
 * @see VideoItem
 */
public class VideoItemValidator implements Validator<VideoItem, Void> {

    public static final String ELEMENT_NAME = Names.VIDEO_ITEM;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    private final DescriptionsValidator descriptionsValidator = new DescriptionsValidator();

    @Override
    public void validate(VideoItem videoItem, Void ctx, ValidationPath path) {
        VALIDATOR.expectNotNull(videoItem, ErrorMessage.EXPECT_VIDEO_ITEM_TO_BE_NOT_NULL, path);
        // Note: this condition is also checked by XSD/RNG
        VALIDATOR.expectNotNull(videoItem.getCategory(), ErrorMessage.EXPECT_CATEGORY_TO_BE_NOT_NULL, path.withAttribute(Names.CATEGORY));

        ValidationPath videoFormatPath = path.withElement(Names.VIDEO_FORMAT);
        // Note: this condition is also checked by XSD/RNG
        VALIDATOR.expectNotNull(ListUtil.extractFirst(videoItem.getVideoFormats()), ErrorMessage.EXPECT_VIDEO_FORMAT_TO_BE_NOT_NULL, videoFormatPath);
        // Note: this condition is also checked by XSD/RNG
        VALIDATOR.expectNotNull(
                ListUtil.extractFirst(videoItem.getVideoFormats()).getURL(),
                ErrorMessage.EXPECT_URL_TO_BE_NOT_NULL,
                videoFormatPath.withElement("URL")
        );

        String categoryCode = videoItem.getCategory();
        if (!OTACodePictureCategoryCode.isCodeDefined(categoryCode)) {
            String message = String.format(ErrorMessage.EXPECT_PICTURE_CATEGORY_CODE_TO_BE_DEFINED, categoryCode);
            VALIDATOR.throwValidationException(message, path.withAttribute(Names.CATEGORY));
        }

        if (!videoItem.getDescriptions().isEmpty()) {
            List<Description> descriptions = Description.fromVideoItemDescriptions(videoItem.getDescriptions());
            this.descriptionsValidator.validate(descriptions, null, path.withElement(Names.DESCRIPTION_LIST));
        }
    }

}
