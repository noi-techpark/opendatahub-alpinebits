// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory;

import it.bz.opendatahub.alpinebits.common.constants.OTACodeHotelAmenityType;
import it.bz.opendatahub.alpinebits.common.constants.OTACodeProximityType;
import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NotNullValidationException;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.CategoryCodesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.CategoryCodesType.HotelCategory;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FeaturesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FeaturesType.Feature;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType.Descriptions;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType.Descriptions.MultimediaDescriptions;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType.Position;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType.Services;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelInfoType.Services.Service;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageDescriptionType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageDescriptionType.ImageFormat;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageItemsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageItemsType.ImageItem;
import it.bz.opendatahub.alpinebits.xml.schema.ota.MultimediaDescriptionType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.TextItemsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.TextItemsType.TextItem;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoDescriptionType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoDescriptionType.VideoFormat;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoItemsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoItemsType.VideoItem;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link HotelInfoValidator}.
 */
@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class HotelInfoValidatorTest {

    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.HOTEL_INFO);

    @Test
    public void testValidate_ShouldThrow_WhenHotelInfoIsNull() {
        validateAndAssert(null, NullValidationException.class, ErrorMessage.EXPECT_HOTEL_INFO_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenNoSubElementExists() {
        validateAndAssert(new HotelInfoType(), ValidationException.class, ErrorMessage.EXPECT_HOTEL_INFO_TO_HAVE_AT_LEAST_ONE_SUBELEMENT);
    }

    @Test
    public void testValidate_ShouldThrow_WhenHotelCategoryCountIsLessThanOne() {
        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setCategoryCodes(new CategoryCodesType());

        String message = String.format(ErrorMessage.EXPECT_HOTEL_CATEGORY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenHotelCategoryCountIsMoreThanOne() {
        CategoryCodesType categoryCodes = new CategoryCodesType();
        categoryCodes.getHotelCategories().add(new HotelCategory());
        categoryCodes.getHotelCategories().add(new HotelCategory());

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setCategoryCodes(categoryCodes);

        String message = String.format(ErrorMessage.EXPECT_HOTEL_CATEGORY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCodeDetailIsNull() {
        CategoryCodesType categoryCodes = new CategoryCodesType();
        categoryCodes.getHotelCategories().add(new HotelCategory());

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setCategoryCodes(categoryCodes);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_CODE_DETAIL_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCodeDetailFormatDoesNotMatch() {
        String codeDetail = "Some value";

        HotelCategory hotelCategory = new HotelCategory();
        hotelCategory.setCodeDetail(codeDetail);

        CategoryCodesType categoryCodes = new CategoryCodesType();
        categoryCodes.getHotelCategories().add(hotelCategory);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setCategoryCodes(categoryCodes);

        String message = String.format(ErrorMessage.EXPECT_CODE_DETAIL_TO_MATCH_PATTERN, codeDetail);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenMultiMediaDescriptionsIsNull() {
        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setDescriptions(new Descriptions());

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenMultiMediaDescriptionsIsEmpty() {
        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(new MultimediaDescriptions());

        validateAndAssert(hotelInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_MULTIMEDIA_DESCRIPTION_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenMultiMediaDescriptionsHasMoreThan4Elements() {
        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(new MultimediaDescriptionType());
        multimediaDescriptions.getMultimediaDescriptions().add(new MultimediaDescriptionType());
        multimediaDescriptions.getMultimediaDescriptions().add(new MultimediaDescriptionType());
        multimediaDescriptions.getMultimediaDescriptions().add(new MultimediaDescriptionType());
        multimediaDescriptions.getMultimediaDescriptions().add(new MultimediaDescriptionType());

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_HAVE_AT_MOST_X_ELEMENTS, 4);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenInfoCodeIsNull() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode(null);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_INFO_CODE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenInfoCodeIsInvalid() {
        String infoCode = "Some value";

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode(infoCode);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_INFO_CODE_TO_BE_VALID, infoCode, HotelInfoValidator.VALID_CODES);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenDuplicateInfoCodesAreFound() {
        String infoCode = "1";

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode(infoCode);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_INFO_CODE, infoCode);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextMultimediaDescriptionHasNoTextItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("1");

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_TEXT_ITEMS_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextMultimediaDescriptionHasImageItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("1");
        multimediaDescriptionType.setTextItems(new TextItemsType());
        multimediaDescriptionType.setImageItems(new ImageItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NotNullValidationException.class, ErrorMessage.EXPECT_IMAGE_ITEMS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextMultimediaDescriptionHasVideoItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("1");
        multimediaDescriptionType.setTextItems(new TextItemsType());
        multimediaDescriptionType.setVideoItems(new VideoItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NotNullValidationException.class, ErrorMessage.EXPECT_VIDEO_ITEMS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextMultimediaDescriptionTextItemListIsEmpty() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("1");
        multimediaDescriptionType.setTextItems(new TextItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_TEXT_ITEM_LIST_TO_BE_NOT_EMPTY);
    }

    /**
     * The TextItem element validation is done by the TextItemValidator.
     * Therefor it is not necessary to test each property of a TextItem element.
     * We only test, that there is a TextItem element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenTextMultimediaDescriptionTextItemDescriptionListIsEmpty() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("1");

        TextItemsType textItems = new TextItemsType();
        textItems.getTextItems().add(new TextItem());
        multimediaDescriptionType.setTextItems(textItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionHasNoImageItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_IMAGE_ITEMS_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionHasTextItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");
        multimediaDescriptionType.setTextItems(new TextItemsType());
        multimediaDescriptionType.setImageItems(new ImageItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NotNullValidationException.class, ErrorMessage.EXPECT_TEXT_ITEMS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionHasVideoItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");
        multimediaDescriptionType.setImageItems(new ImageItemsType());
        multimediaDescriptionType.setVideoItems(new VideoItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NotNullValidationException.class, ErrorMessage.EXPECT_VIDEO_ITEMS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionImageItemListIsEmpty() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");
        multimediaDescriptionType.setImageItems(new ImageItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_IMAGE_ITEM_LIST_TO_BE_NOT_EMPTY);
    }

    /**
     * The ImageItem element validation is done by the ImageItemValidator.
     * Therefor it is not necessary to test each property of a ImageItem element.
     * We only test, that there is a ImageItem element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionImageItemCategoryIsNull() {
        ImageItemsType imageItems = new ImageItemsType();
        imageItems.getImageItems().add(new ImageItem());

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");
        multimediaDescriptionType.setImageItems(imageItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_CATEGORY_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionImageItemCategoryIsNotValid() {
        String category = "3";

        ImageItem imageItem = buildValidImageItem();
        imageItem.setCategory(category);

        ImageItemsType imageItems = new ImageItemsType();
        imageItems.getImageItems().add(imageItem);

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");
        multimediaDescriptionType.setImageItems(imageItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_CATEGORY_TO_BE_VALID, category, HotelInfoValidator.VALID_IMAGE_CATEGORIES);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenImageMultimediaDescriptionImageItemCategoryIsDuplicate() {
        String category = "1";

        ImageItem imageItem = buildValidImageItem();
        imageItem.setCategory(category);

        ImageItemsType imageItems = new ImageItemsType();
        imageItems.getImageItems().add(imageItem);
        imageItems.getImageItems().add(imageItem);

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("23");
        multimediaDescriptionType.setImageItems(imageItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_CATEGORY, category);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionHasNoVideoItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_VIDEO_ITEMS_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionHasTextItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");
        multimediaDescriptionType.setTextItems(new TextItemsType());
        multimediaDescriptionType.setVideoItems(new VideoItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NotNullValidationException.class, ErrorMessage.EXPECT_TEXT_ITEMS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionHasImageItems() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");
        multimediaDescriptionType.setImageItems(new ImageItemsType());
        multimediaDescriptionType.setVideoItems(new VideoItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NotNullValidationException.class, ErrorMessage.EXPECT_IMAGE_ITEMS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionVideoItemListIsEmpty() {
        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");
        multimediaDescriptionType.setVideoItems(new VideoItemsType());

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_VIDEO_ITEM_LIST_TO_BE_NOT_EMPTY);
    }

    /**
     * The VideoItem element validation is done by the VideoItemValidator.
     * Therefor it is not necessary to test each property of a VideoItem element.
     * We only test, that there is a VideoItem element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionVideoItemCategoryIsNull() {
        VideoItemsType videoItems = new VideoItemsType();
        videoItems.getVideoItems().add(new VideoItem());

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");
        multimediaDescriptionType.setVideoItems(videoItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_CATEGORY_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionVideoItemCategoryIsNotValid() {
        String category = "3";

        VideoItem videoItem = buildValidVideoItem();
        videoItem.setCategory(category);

        VideoItemsType videoItems = new VideoItemsType();
        videoItems.getVideoItems().add(videoItem);

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");
        multimediaDescriptionType.setVideoItems(videoItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_CATEGORY_TO_BE_VALID, category, HotelInfoValidator.VALID_VIDEO_CATEGORIES);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenVideoMultimediaDescriptionVideoItemCategoryIsDuplicate() {
        String category = "1";

        VideoItem videoItem = buildValidVideoItem();
        videoItem.setCategory(category);

        VideoItemsType videoItems = new VideoItemsType();
        videoItems.getVideoItems().add(videoItem);
        videoItems.getVideoItems().add(videoItem);

        MultimediaDescriptionType multimediaDescriptionType = new MultimediaDescriptionType();
        multimediaDescriptionType.setInfoCode("24");
        multimediaDescriptionType.setVideoItems(videoItems);

        MultimediaDescriptions multimediaDescriptions = new MultimediaDescriptions();
        multimediaDescriptions.getMultimediaDescriptions().add(multimediaDescriptionType);

        HotelInfoType hotelInfoType = buildHotelInfoTypeFromMultimediaDescriptions(multimediaDescriptions);

        String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_CATEGORY, category);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPositionHasNoneOfTheRequiredCouples() {
        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setPosition(new Position());

        validateAndAssert(hotelInfoType, ValidationException.class, ErrorMessage.EXPECT_POSITION_WITH_AT_LEAST_ONE_COUPLE);
    }

    @Test
    public void testValidate_ShouldThrow_WhenServiceListIsEmpty() {
        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(new Services());

        validateAndAssert(hotelInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_SERVICE_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenServiceCodeIsNull() {
        Service service = new Service();
        service.setCode(null);
        service.setProximityCode(OTACodeProximityType.ONSITE.getCode());

        Services services = new Services();
        services.getServices().add(service);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(services);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_CODE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenServiceCodeIsInvalid() {
        String code = "INVALID";

        Service service = new Service();
        service.setCode(code);
        service.setProximityCode(OTACodeProximityType.ONSITE.getCode());

        Services services = new Services();
        services.getServices().add(service);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(services);

        String message = String.format(ErrorMessage.EXPECT_HOTEL_AMENITY_CODE_TO_BE_DEFINED, code);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenServiceProximityCodeIsNull() {
        Service service = new Service();
        service.setCode(OTACodeHotelAmenityType._24_HOUR_FRONT_DESK.getCode());
        service.setProximityCode(null);

        Services services = new Services();
        services.getServices().add(service);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(services);

        validateAndAssert(hotelInfoType, NullValidationException.class, ErrorMessage.EXPECT_PROXIMITY_CODE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenServiceProximityCodeIsInvalid() {
        String proximityCode = "INVALID";

        Service service = new Service();
        service.setCode(OTACodeHotelAmenityType._24_HOUR_FRONT_DESK.getCode());
        service.setProximityCode(proximityCode);

        Services services = new Services();
        services.getServices().add(service);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(services);

        String message = String.format(ErrorMessage.EXPECT_PROXIMITY_CODE_TO_BE_DEFINED, proximityCode);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAccessibleCodeIsNull() {
        String accessibleCode = null;

        Feature feature = new Feature();
        feature.setAccessibleCode(accessibleCode);

        FeaturesType featuresType = new FeaturesType();
        featuresType.getFeatures().add(feature);

        Service service = new Service();
        service.setFeatures(featuresType);
        service.setCode(OTACodeHotelAmenityType.ACCESSIBLE_FACILITIES.getCode());
        service.setProximityCode(OTACodeProximityType.ONSITE.getCode());

        Services services = new Services();
        services.getServices().add(service);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(services);

        String message = String.format(ErrorMessage.EXPECT_DISABILITY_FEATURE_CODE_TO_BE_DEFINED, accessibleCode);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAccessibleCodeIsUnknown() {
        String accessibleCode = "Some value";

        Feature feature = new Feature();
        feature.setAccessibleCode(accessibleCode);

        FeaturesType featuresType = new FeaturesType();
        featuresType.getFeatures().add(feature);

        Service service = new Service();
        service.setFeatures(featuresType);
        service.setCode(OTACodeHotelAmenityType.ACCESSIBLE_FACILITIES.getCode());
        service.setProximityCode(OTACodeProximityType.ONSITE.getCode());

        Services services = new Services();
        services.getServices().add(service);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setServices(services);

        String message = String.format(ErrorMessage.EXPECT_DISABILITY_FEATURE_CODE_TO_BE_DEFINED, accessibleCode);
        validateAndAssert(hotelInfoType, ValidationException.class, message);
    }

    private HotelInfoType buildHotelInfoTypeFromMultimediaDescriptions(MultimediaDescriptions multimediaDescriptions) {
        Descriptions descriptions = new Descriptions();
        descriptions.setMultimediaDescriptions(multimediaDescriptions);

        HotelInfoType hotelInfoType = new HotelInfoType();
        hotelInfoType.setDescriptions(descriptions);
        return hotelInfoType;
    }

    private ImageItem buildValidImageItem() {
        ImageFormat imageFormat = new ImageFormat();
        imageFormat.setURL("http://example.com");

        ImageItem imageItem = new ImageItem();
        imageItem.setCategory("1");
        imageItem.getImageFormats().add(imageFormat);

        imageItem.getDescriptions().add(buildValidImageItemDescription());

        return imageItem;
    }

    private VideoItem buildValidVideoItem() {
        VideoFormat videoFormat = new VideoFormat();
        videoFormat.setURL("http://example.com");

        VideoItem videoItem = new VideoItem();
        videoItem.setCategory("1");
        videoItem.getVideoFormats().add(videoFormat);

        videoItem.getDescriptions().add(buildValidVideoItemDescription());

        return videoItem;
    }

    private ImageDescriptionType.Description buildValidImageItemDescription() {
        ImageDescriptionType.Description description = new ImageDescriptionType.Description();
        description.setTextFormat("PlainText");
        description.setLanguage("en");
        description.setValue("Some description");
        return description;
    }

    private VideoDescriptionType.Description buildValidVideoItemDescription() {
        VideoDescriptionType.Description description = new VideoDescriptionType.Description();
        description.setTextFormat("PlainText");
        description.setLanguage("en");
        description.setValue("Some description");
        return description;
    }

    protected void validateAndAssert(
            HotelInfoType data,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        HotelInfoValidator validator = new HotelInfoValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, null, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON

        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }
}