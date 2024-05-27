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
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.InventoriesContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType.InvCounts;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType.InvCounts.InvCount;
import it.bz.opendatahub.alpinebits.xml.schema.ota.InvCountType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.StatusApplicationControlType;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this validator to validate the Inventories in AlpineBits 2022
 * FreeRooms documents.
 *
 * @see InvCountType
 */
public class InventoriesValidator implements Validator<InvCountType, InventoriesContext> {

    public static final String ELEMENT_NAME = Names.INVENTORIES;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    private final StatusApplicationControlValidator statusApplicationControlValidator = new StatusApplicationControlValidator();
    private final ClosingSeasonStatusApplicationControlValidator closingSeasonStatusApplicationControlValidator
            = new ClosingSeasonStatusApplicationControlValidator();
    private final ClosingSeasonsOverlapValidator closingSeasonsOverlapValidator = new ClosingSeasonsOverlapValidator();

    @Override
    public void validate(InvCountType inventories, InventoriesContext ctx, ValidationPath path) {
        VALIDATOR.expectNotNull(inventories, ErrorMessage.EXPECT_INVENTORIES_TO_BE_NOT_NULL, path);
        VALIDATOR.expectNotNull(ctx, ErrorMessage.EXPECT_CONTEXT_TO_BE_NOT_NULL, path);

        VALIDATOR.expectHotelCodeAndNameNotBothNull(
                inventories.getHotelCode(),
                inventories.getHotelName(),
                ErrorMessage.EXPECT_HOTEL_CODE_AND_HOTEL_NAME_TO_BE_NOT_BOTH_NULL,
                path.withAttribute(String.format("%s/%s", Names.HOTEL_CODE, Names.HOTEL_NAME))
        );

        VALIDATOR.expectNonEmptyCollection(
                inventories.getInventories(),
                ErrorMessage.EXPECT_INVENTORIES_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.INVENTORIES_LIST)
        );

        List<BaseInvCountType> inventoryList = inventories.getInventories();

        if (this.isRoomReset(ctx.getInstance(), inventoryList)) {
            // Room reset means, that there exists only one Inventory
            // element without any attributes and elements. This has to be validated.

            this.validateRoomReset(inventoryList.get(0), path.withElement(Names.INVENTORY).withIndex(0));
        } else {
            // Ordinary FreeRooms update

            this.validateInventories(inventoryList, ctx, path);
        }
    }

    private boolean isRoomReset(String instance, List<BaseInvCountType> inventoryList) {
        // A criteria for FreeRooms reset is, that
        // there is only one Inventory message
        if (inventoryList.size() != 1) {
            return false;
        }

        // A criteria for FreeRooms reset is, that
        // the instance is "CompleteSet"
        if (!OTAHotelInvCountNotifRQValidator.COMPLETE_SET.equals(instance)) {
            return false;
        }

        // A criteria for FreeRooms reset is, that
        // the single Inventory has no child element and attributes
        BaseInvCountType inventory = inventoryList.get(0);
        return inventory.getInvCounts() == null
                && inventory.getStatusApplicationControl() == null;
    }

    private void validateRoomReset(BaseInvCountType inventory, ValidationPath path) {
        VALIDATOR.expectNull(
                inventory.getInvCounts(),
                ErrorMessage.EXPECT_INV_COUNTS_TO_BE_NULL,
                path.withElement(Names.INV_COUNTS)
        );
        VALIDATOR.expectNull(
                inventory.getStatusApplicationControl(),
                ErrorMessage.EXPECT_STATUS_APPLICATION_CONTROL_TO_BE_NULL,
                path.withElement(Names.STATUS_APPLICATION_CONTROL)
        );
    }

    private void validateInventories(
            List<BaseInvCountType> inventoryList,
            InventoriesContext ctx,
            ValidationPath path
    ) {
        // Helper for closing season validation
        boolean nonClosingSeasonElementFound = false;

        // Helpers to check if room / category updates are mixed (not allowed)
        boolean hasRoom = false;
        boolean hasCategory = false;

        // Keep a list of closing-season-elements to check for overlapping time periods
        List<BaseInvCountType> closingSeasonElements = new ArrayList<>();

        for (int i = 0; i < inventoryList.size(); i++) {
            ValidationPath indexedPath = path.withElement(Names.INVENTORY).withIndex(i);

            BaseInvCountType inventory = inventoryList.get(i);

            // Distinguish between closing-seasons and ordinary Inventory elements

            if (this.isClosingSeasonElement(inventory, ctx)) {
                // Validate closing-seasons element

                validateClosingSeasonsElement(inventory, nonClosingSeasonElementFound, ctx, indexedPath);

                // Add inventory to list of closing-season-elements that is checked later on for overlapping time periods
                closingSeasonElements.add(inventory);
            } else {
                // Validate non closing-seasons element

                nonClosingSeasonElementFound = true;

                // Validate StatusApplicationControl
                StatusApplicationControlType statusApplicationControl = inventory.getStatusApplicationControl();
                this.statusApplicationControlValidator.validate(
                        statusApplicationControl,
                        null,
                        indexedPath.withElement(StatusApplicationControlValidator.ELEMENT_NAME)
                );

                // Validate for room category / distinct room
                if (this.isForRoomCategory(statusApplicationControl)) {
                    // Validate room category availability info
                    this.validateCategorySupport(ctx, indexedPath);
                    hasRoom = true;
                } else {
                    // Validate distinct room availability info
                    this.validateRoomsSupport(ctx, indexedPath);
                    hasCategory = true;
                }

                // Validate inventory counts
                this.validateInvCounts(inventory.getInvCounts(), ctx, indexedPath.withElement(Names.INV_COUNTS));

                // Check that room category information and distinct room
                // information is not mixed
                if (hasRoom && hasCategory) {
                    VALIDATOR.throwValidationException(
                            ErrorMessage.EXPECT_ROOM_CATEGORY_AND_DISTINCT_ROOM_TO_NOT_BE_MIXED,
                            path
                    );
                }
            }
        }

        // Check that closing season don't overlap
        this.closingSeasonsOverlapValidator.validate(closingSeasonElements, null, path);
    }

    private boolean isClosingSeasonElement(BaseInvCountType inventory, InventoriesContext ctx) {
        return OTAHotelInvCountNotifRQValidator.COMPLETE_SET.equals(ctx.getInstance())
                && inventory.getStatusApplicationControl() != null
                && inventory.getStatusApplicationControl().getStart() != null
                && inventory.getStatusApplicationControl().getEnd() != null
                && Boolean.TRUE.equals(inventory.getStatusApplicationControl().isAllInvCode());
    }

    private void validateClosingSeasonsElement(
            BaseInvCountType inventory,
            boolean nonClosingSeasonElementFound,
            InventoriesContext ctx,
            ValidationPath indexedPath
    ) {
        // Check support for closing-seasons
        if (!ctx.getHotelInvCountNotifContext().isClosingSeasonsSupported()) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_CLOSING_SEASONS, indexedPath);
        }

        // All closing-seasons elements must be on top of the Inventories list
        if (nonClosingSeasonElementFound) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CLOSING_SEASON_TO_BE_ON_TOP_OF_LIST, indexedPath);
        }

        // A closing-seasons element must not have a InvCounts sub element
        VALIDATOR.expectNull(inventory.getInvCounts(), ErrorMessage.EXPECT_INV_COUNTS_TO_BE_NULL, indexedPath);

        // Validate StatusApplicationControl for closing-seasons element
        this.closingSeasonStatusApplicationControlValidator.validate(
                inventory.getStatusApplicationControl(),
                null,
                indexedPath.withElement(StatusApplicationControlValidator.ELEMENT_NAME)
        );
    }

    private boolean isForRoomCategory(StatusApplicationControlType statusApplicationControl) {
        return statusApplicationControl.getInvCode() == null;
    }

    private void validateCategorySupport(InventoriesContext ctx, ValidationPath path) {
        // Check that server supports availability information for room categories
        // (OTA_HotelInvCountNotif_accept_categories capability)
        if (!ctx.getHotelInvCountNotifContext().isCategoriesSupported()) {
            VALIDATOR.throwValidationException(
                    ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_CATEGORIES,
                    path.withElement(StatusApplicationControlValidator.ELEMENT_NAME)
            );
        }
    }

    private void validateRoomsSupport(InventoriesContext ctx, ValidationPath path) {
        // Check that server supports availability information for rooms
        // (OTA_HotelInvCountNotif_accept_rooms capability)
        if (!ctx.getHotelInvCountNotifContext().isRoomsSupported()) {
            VALIDATOR.throwValidationException(
                    ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_ROOMS,
                    path.withElement(StatusApplicationControlValidator.ELEMENT_NAME)
            );
        }
    }

    private void validateInvCounts(InvCounts invCounts, InventoriesContext ctx, ValidationPath path) {
        // InvCounts may be null. In this case, it is considered as if the whole
        // room/room category is fully booked in the given time period
        if (invCounts == null) {
            return;
        }

        List<InvCount> invCountList = invCounts.getInvCounts();

        // It is expected that there are one to three InvCount elements
        if (invCountList.isEmpty() || invCountList.size() > 3) {
            String message = String.format(ErrorMessage.EXPECT_INV_COUNTS_TO_HAVE_BETWEEN_ONE_AND_THREE_ELEMENTS, invCountList.size());
            VALIDATOR.throwValidationException(message, path);
        }

        boolean hasCountType2 = false;
        boolean hasCountType6 = false;
        boolean hasCountType9 = false;

        for (int i = 0; i < invCountList.size(); i++) {
            ValidationPath indexedPath = path.withElement(Names.INV_COUNT).withIndex(i);

            InvCount invCount = invCountList.get(i);

            String countType = invCount.getCountType();

            switch (countType) {
                case "2":
                    // Check for duplicate entry
                    this.throwOnDuplicateCountType(hasCountType2, countType, indexedPath);
                    hasCountType2 = true;

                    break;
                case "6":
                    // Check support for out-of-order rooms
                    if (!ctx.getHotelInvCountNotifContext().isOutOfOrderSupported()) {
                        VALIDATOR.throwValidationException(ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_OUT_OF_ORDER, indexedPath);
                    }

                    // Check for duplicate entry
                    this.throwOnDuplicateCountType(hasCountType6, countType, indexedPath);
                    hasCountType6 = true;

                    break;
                case "9":
                    // Check support for out-of-market rooms
                    if (!ctx.getHotelInvCountNotifContext().isOutOfMarketSupported()) {
                        VALIDATOR.throwValidationException(ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_OUT_OF_MARKET, indexedPath);
                    }

                    // Check for duplicate entry
                    this.throwOnDuplicateCountType(hasCountType9, countType, indexedPath);
                    hasCountType9 = true;

                    break;
                default:
                    VALIDATOR.throwValidationException(ErrorMessage.EXPECT_COUNT_TYPE_TO_BE_ONE_OF_2_6_9, indexedPath);
            }
        }
    }

    private void throwOnDuplicateCountType(boolean hasCountType, String countType, ValidationPath path) {
        if (hasCountType) {
            String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_COUNT_TYPE, countType);
            VALIDATOR.throwValidationException(message, path.withAttribute(Names.COUNT_TYPE));
        }
    }
}
