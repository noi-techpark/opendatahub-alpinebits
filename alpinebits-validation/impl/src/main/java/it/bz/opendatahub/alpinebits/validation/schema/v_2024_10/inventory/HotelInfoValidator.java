// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCodeHotelAmenityType;
import it.bz.opendatahub.alpinebits.common.constants.OTACodeDisabilityFeatureCode;
import it.bz.opendatahub.alpinebits.common.constants.OTACodeHotelAmenityType;
import it.bz.opendatahub.alpinebits.common.constants.OTACodeProximityType;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.CategoryCodesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FeaturesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageItemsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.MultimediaDescriptionType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.TextItemsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoItemsType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Use this validator to validate HotelInfo in AlpineBits 2024 Inventory documents.
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

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    // CodeDetail attribute is a string composed by two elements separated by colon, where the first
    // element contains a "_" character. Examples: "PCT2017_20:4s", "ASTAT2020_11:4s"
    private static final Pattern CODE_DETAIL_PATTERN = Pattern.compile("^[^_:]+_[^_]+:[^_:]+$");

    private final ImageItemValidator imageItemValidator = new ImageItemValidator();
    private final TextItemValidator textItemValidator = new TextItemValidator();
    private final VideoItemValidator videoItemValidator = new VideoItemValidator();

    @Override
    public void validate(HotelInfoType hotelInfo, Void ctx, ValidationPath path) {
        // Although HotelInfo is optional, a null-check is performed.
        // It turned out, that it is better that the caller decides if HotelInfo
        // validation needs to be invoked. This makes the code more reusable
        // e.g. when required/optional elements change with different AlpineBits versions
        VALIDATOR.expectNotNull(hotelInfo, ErrorMessage.EXPECT_HOTEL_INFO_TO_NOT_BE_NULL, path);

        this.validateAtLeastOneSubElementExists(hotelInfo, path);

        if (hotelInfo.getCategoryCodes() != null) {
            this.validateCategoryCodes(hotelInfo.getCategoryCodes(), path.withElement(Names.CATEGORY_CODES));
        }

        if (hotelInfo.getDescriptions() != null) {
            this.validateDescriptions(hotelInfo.getDescriptions(), path.withElement(Names.DESCRIPTIONS));
        }

        if (hotelInfo.getPosition() != null) {
            this.validatePosition(hotelInfo.getPosition(), path.withElement(Names.POSITION));
        }

        if (hotelInfo.getServices() != null) {
            this.validateServices(hotelInfo.getServices(), path.withElement(Names.SERVICES));
        }
    }

    private void validateAtLeastOneSubElementExists(HotelInfoType hotelInfo, ValidationPath path) {
        if (hotelInfo.getCategoryCodes() == null
                && hotelInfo.getDescriptions() == null
                && hotelInfo.getPosition() == null
                && hotelInfo.getServices() == null) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_HOTEL_INFO_TO_HAVE_AT_LEAST_ONE_SUBELEMENT, path);
        }
    }

    private void validateCategoryCodes(CategoryCodesType categoryCodes, ValidationPath path) {
        // HotelCategories is required
        VALIDATOR.expectNotNull(
                categoryCodes.getHotelCategories(),
                ErrorMessage.EXPECT_HOTEL_CATEGORIES_TO_BE_NOT_NULL,
                path.withElement(Names.HOTEL_CATEGORY_LIST)
        );

        int hotelCategoriesCount = categoryCodes.getHotelCategories().size();
        if (hotelCategoriesCount != 1) {
            String message = String.format(ErrorMessage.EXPECT_HOTEL_CATEGORY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, hotelCategoriesCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.HOTEL_CATEGORY_LIST));
        }

        // Validate CodeDetail
        ValidationPath codeDetailPath = path.withElement(Names.HOTEL_CATEGORY).withIndex(0).withElement(Names.CODE_DETAIL);
        String codeDetail = categoryCodes.getHotelCategories().get(0).getCodeDetail();

        VALIDATOR.expectNotNull(codeDetail, ErrorMessage.EXPECT_CODE_DETAIL_TO_BE_NOT_NULL, codeDetailPath);

        if (!CODE_DETAIL_PATTERN.matcher(codeDetail).matches()) {
            String message = String.format(ErrorMessage.EXPECT_CODE_DETAIL_TO_MATCH_PATTERN, codeDetail);
            VALIDATOR.throwValidationException(message, codeDetailPath);
        }
    }

    private void validateDescriptions(HotelInfoType.Descriptions descriptions, ValidationPath path) {
        ValidationPath multimediaDescriptionsPath = path.withElement(Names.MULTIMEDIA_DESCRIPTIONS);

        VALIDATOR.expectNotNull(
                descriptions.getMultimediaDescriptions(),
                ErrorMessage.EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_BE_NOT_NULL,
                multimediaDescriptionsPath
        );

        List<MultimediaDescriptionType> multimediaDescriptions = descriptions.getMultimediaDescriptions().getMultimediaDescriptions();
        VALIDATOR.expectNonEmptyCollection(
                multimediaDescriptions,
                ErrorMessage.EXPECT_MULTIMEDIA_DESCRIPTION_LIST_TO_BE_NOT_EMPTY,
                multimediaDescriptionsPath.withElement(Names.MULTIMEDIA_DESCRIPTION_LIST)
        );

        // At most 4 MultimediaDescription elements are allowed (with InfoCodes 1, 17, 23, and 24)
        if (multimediaDescriptions.size() > 4) {
            String message = String.format(ErrorMessage.EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_HAVE_AT_MOST_X_ELEMENTS, 4);
            VALIDATOR.throwValidationException(message, multimediaDescriptionsPath.withElement(Names.MULTIMEDIA_DESCRIPTION_LIST));
        }

        this.validateInfoCodes(multimediaDescriptions, VALID_CODES, multimediaDescriptionsPath);

        this.validateMultimediaDescriptions(multimediaDescriptions, multimediaDescriptionsPath);
    }

    private void validatePosition(HotelInfoType.Position position, ValidationPath path) {
        boolean hasLatLong = position.getLatitude() != null && position.getLongitude() != null;
        boolean hasAltitudeAndAltitudeUnit = position.getAltitude() != null && position.getAltitudeUnitOfMeasureCode() != null;

        if (!hasLatLong && !hasAltitudeAndAltitudeUnit) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_POSITION_WITH_AT_LEAST_ONE_COUPLE, path);
        }
    }

    private void validateServices(HotelInfoType.Services services, ValidationPath path) {
        VALIDATOR.expectNonEmptyCollection(
                services.getServices(),
                ErrorMessage.EXPECT_SERVICE_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.SERVICE_LIST)
        );

        for (int i = 0; i < services.getServices().size(); i++) {
            HotelInfoType.Services.Service service = services.getServices().get(i);
            ValidationPath servicePath = path.withElement(Names.SERVICE).withIndex(i);

            String code = service.getCode();
            VALIDATOR.expectNotNull(code, ErrorMessage.EXPECT_CODE_TO_BE_NOT_NULL, servicePath.withAttribute(Names.CODE));
            if (!OTACodeHotelAmenityType.isCodeDefined(code) && !AlpineBitsCodeHotelAmenityType.isCodeDefined(code)) {
                String message = String.format(ErrorMessage.EXPECT_HOTEL_AMENITY_CODE_TO_BE_DEFINED_2024_10, code);
                VALIDATOR.throwValidationException(message, servicePath.withAttribute(Names.CODE));
            }

            String proximityCode = service.getProximityCode();
            VALIDATOR.expectNotNull(proximityCode, ErrorMessage.EXPECT_PROXIMITY_CODE_TO_BE_NOT_NULL, servicePath.withAttribute(Names.PROXIMITY_CODE));
            if (!OTACodeProximityType.isCodeDefined(proximityCode)) {
                String message = String.format(ErrorMessage.EXPECT_PROXIMITY_CODE_TO_BE_DEFINED, proximityCode);
                VALIDATOR.throwValidationException(message, servicePath.withAttribute(Names.PROXIMITY_CODE));
            }

            if (OTACodeHotelAmenityType.ACCESSIBLE_FACILITIES.getCode().equals(code) && service.getFeatures() != null) {
                for (int j = 0; j < service.getFeatures().getFeatures().size(); j++) {
                    FeaturesType.Feature feature = service.getFeatures().getFeatures().get(j);

                    String accessibleCode = feature.getAccessibleCode();

                    if (!OTACodeDisabilityFeatureCode.isCodeDefined(accessibleCode)) {
                        String message = String.format(ErrorMessage.EXPECT_DISABILITY_FEATURE_CODE_TO_BE_DEFINED, accessibleCode);
                        VALIDATOR.throwValidationException(
                                message,
                                servicePath.withElement(Names.FEATURES).withElement(Names.FEATURE).withAttribute(Names.ACCESSIBLE_CODE)
                        );
                    }
                }
            }
        }
    }

    private void validateInfoCodes(List<MultimediaDescriptionType> descriptions, List<String> validCodes, ValidationPath path) {
        Set<String> validCodesIndex = new HashSet<>(validCodes);

        Set<String> codesFound = new HashSet<>();

        for (int i = 0; i < descriptions.size(); i++) {
            MultimediaDescriptionType md = descriptions.get(i);

            ValidationPath indexedPath = path.withElement(Names.MULTIMEDIA_DESCRIPTION).withIndex(i);

            String infoCode = md.getInfoCode();

            // InfoCode is required
            VALIDATOR.expectNotNull(
                    infoCode,
                    ErrorMessage.EXPECT_INFO_CODE_TO_BE_NOT_NULL,
                    indexedPath.withAttribute(Names.INFO_CODE)
            );

            // Check if InfoCode is valid
            if (!validCodesIndex.contains(infoCode)) {
                String message = String.format(ErrorMessage.EXPECT_INFO_CODE_TO_BE_VALID, infoCode, validCodes);
                VALIDATOR.throwValidationException(message, indexedPath.withAttribute(Names.INFO_CODE));
            }

            // Check for duplicate InfoCode values
            if (codesFound.contains(infoCode)) {
                String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_INFO_CODE, infoCode);
                VALIDATOR.throwValidationException(message, indexedPath.withAttribute(Names.INFO_CODE));
            }
            codesFound.add(infoCode);
        }
    }

    private void validateMultimediaDescriptions(List<MultimediaDescriptionType> descriptions, ValidationPath path) {
        for (int i = 0; i < descriptions.size(); i++) {
            ValidationPath indexedPath = path.withElement(Names.MULTIMEDIA_DESCRIPTION).withIndex(i);

            MultimediaDescriptionType mm = descriptions.get(i);

            switch (mm.getInfoCode()) {
                case "1":
                case "17":
                    this.validateTextItemsOnly(mm, indexedPath);
                    break;
                case "23":
                    this.validateImageItemsOnly(mm, VALID_IMAGE_CATEGORIES, indexedPath);
                    break;
                case "24":
                    this.validateVideoItemsOnly(mm, Arrays.asList("1", "2", "4", "12", "20", "22"), indexedPath);
                    break;
                default:
            }
        }
    }

    private void validateTextItemsOnly(MultimediaDescriptionType mm, ValidationPath path) {
        ValidationPath textItemsPath = path.withElement(Names.TEXT_ITEMS);

        VALIDATOR.expectNotNull(
                mm.getTextItems(),
                ErrorMessage.EXPECT_TEXT_ITEMS_TO_BE_NOT_NULL,
                textItemsPath
        );

        // Note: this condition is also checked by RNG (but not by XSD) - only TextItems allowed
        VALIDATOR.expectNull(
                mm.getImageItems(),
                ErrorMessage.EXPECT_IMAGE_ITEMS_TO_BE_NULL,
                path.withElement(Names.IMAGE_ITEMS)
        );
        // Note: this condition is also checked by RNG (but not by XSD) - only ImageItems allowed
        VALIDATOR.expectNull(
                mm.getVideoItems(),
                ErrorMessage.EXPECT_VIDEO_ITEMS_TO_BE_NULL,
                path.withElement(Names.VIDEO_ITEMS)
        );

        // Expecting at least one TextItem element
        VALIDATOR.expectNonEmptyCollection(
                mm.getTextItems().getTextItems(),
                ErrorMessage.EXPECT_TEXT_ITEM_LIST_TO_BE_NOT_EMPTY,
                textItemsPath.withElement(Names.TEXT_ITEM_LIST)
        );

        // Validate TextItem elements
        for (int i = 0; i < mm.getTextItems().getTextItems().size(); i++) {
            TextItemsType.TextItem textItem = mm.getTextItems().getTextItems().get(i);
            this.textItemValidator.validate(textItem, null, textItemsPath.withElement(Names.TEXT_ITEM).withIndex(i));
        }
    }

    private void validateImageItemsOnly(MultimediaDescriptionType mm, List<String> validCategories, ValidationPath path) {
        ValidationPath imageItemsPath = path.withElement(Names.IMAGE_ITEMS);

        VALIDATOR.expectNotNull(
                mm.getImageItems(),
                ErrorMessage.EXPECT_IMAGE_ITEMS_TO_BE_NOT_NULL,
                imageItemsPath
        );

        // Note: this condition is also checked by RNG (but not by XSD) - only ImageItems allowed
        VALIDATOR.expectNull(
                mm.getTextItems(),
                ErrorMessage.EXPECT_TEXT_ITEMS_TO_BE_NULL,
                path.withElement(Names.TEXT_ITEMS)
        );
        // Note: this condition is also checked by RNG (but not by XSD) - only ImageItems allowed
        VALIDATOR.expectNull(
                mm.getVideoItems(),
                ErrorMessage.EXPECT_VIDEO_ITEMS_TO_BE_NULL,
                path.withElement(Names.VIDEO_ITEMS)
        );

        // Expecting at least one ImageItem element
        VALIDATOR.expectNonEmptyCollection(
                mm.getImageItems().getImageItems(),
                ErrorMessage.EXPECT_IMAGE_ITEM_LIST_TO_BE_NOT_EMPTY,
                imageItemsPath.withElement(Names.IMAGE_ITEM_LIST)
        );

        // Validate ImageItem elements

        Set<String> validCategoriesIndex = new HashSet<>(validCategories);

        Set<String> categoriesFound = new HashSet<>();

        for (int i = 0; i < mm.getImageItems().getImageItems().size(); i++) {
            ValidationPath indexedPath = imageItemsPath.withElement(Names.IMAGE_ITEM).withIndex(i);

            ImageItemsType.ImageItem imageItem = mm.getImageItems().getImageItems().get(i);
            this.imageItemValidator.validate(imageItem, null, indexedPath);

            // Check that category is allowed
            String category = imageItem.getCategory();
            if (!validCategoriesIndex.contains(category)) {
                String message = String.format(ErrorMessage.EXPECT_CATEGORY_TO_BE_VALID, category, validCategories);
                VALIDATOR.throwValidationException(message, indexedPath.withAttribute(Names.CATEGORY));
            }

            // Check for duplicate Category values
            if (categoriesFound.contains(category)) {
                String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_CATEGORY, category);
                VALIDATOR.throwValidationException(message, indexedPath.withAttribute(Names.CATEGORY));
            }

            categoriesFound.add(category);
        }
    }

    private void validateVideoItemsOnly(MultimediaDescriptionType mm, List<String> validCategories, ValidationPath path) {
        ValidationPath videoItemsPath = path.withElement(Names.VIDEO_ITEMS);

        VALIDATOR.expectNotNull(
                mm.getVideoItems(),
                ErrorMessage.EXPECT_VIDEO_ITEMS_TO_BE_NOT_NULL,
                videoItemsPath
        );

        // Note: this condition is also checked by RNG (but not by XSD) - only VideoItems allowed
        VALIDATOR.expectNull(
                mm.getImageItems(),
                ErrorMessage.EXPECT_IMAGE_ITEMS_TO_BE_NULL,
                path.withElement(Names.IMAGE_ITEMS)
        );
        // Note: this condition is also checked by RNG (but not by XSD) - only VideoItems allowed
        VALIDATOR.expectNull(
                mm.getTextItems(),
                ErrorMessage.EXPECT_TEXT_ITEMS_TO_BE_NULL,
                path.withElement(Names.TEXT_ITEMS)
        );

        // Expecting at least one ImageItem element
        VALIDATOR.expectNonEmptyCollection(
                mm.getVideoItems().getVideoItems(),
                ErrorMessage.EXPECT_VIDEO_ITEM_LIST_TO_BE_NOT_EMPTY,
                videoItemsPath.withElement(Names.VIDEO_ITEM_LIST)
        );

        // Validate VideoItem elements

        Set<String> validCategoriesIndex = new HashSet<>(validCategories);

        Set<String> categoriesFound = new HashSet<>();

        for (int i = 0; i < mm.getVideoItems().getVideoItems().size(); i++) {
            ValidationPath indexedPath = videoItemsPath.withElement(Names.VIDEO_ITEM).withIndex(i);

            VideoItemsType.VideoItem videoItem = mm.getVideoItems().getVideoItems().get(i);
            this.videoItemValidator.validate(videoItem, null, indexedPath);

            // Check that category is allowed
            String category = videoItem.getCategory();
            if (!validCategoriesIndex.contains(category)) {
                String message = String.format(ErrorMessage.EXPECT_CATEGORY_TO_BE_VALID, category, validCategories);
                VALIDATOR.throwValidationException(message, indexedPath.withAttribute(Names.CATEGORY));
            }

            // Check for duplicate Category values
            if (categoriesFound.contains(category)) {
                String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_CATEGORY, category);
                VALIDATOR.throwValidationException(message, indexedPath.withAttribute(Names.CATEGORY));
            }

            categoriesFound.add(category);
        }
    }

}
