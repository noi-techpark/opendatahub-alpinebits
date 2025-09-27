// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.HotelInvCountNotifContext;
import it.bz.opendatahub.alpinebits.validation.context.freerooms.InventoriesContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType.InvCounts;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType.InvCounts.InvCount;
import it.bz.opendatahub.alpinebits.xml.schema.ota.InvCountType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.StatusApplicationControlType;
import org.testng.annotations.Test;

import java.util.Arrays;

import static it.bz.opendatahub.alpinebits.validation.schema.common.ValidationUtil.validateAndAssert;

/**
 * Tests for {@link InventoriesValidator}.
 */
public class InventoriesValidatorTest {
    private static final InventoriesValidator VALIDATOR = new InventoriesValidator();
    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.INVENTORIES);

    private static final String STATUS_APPLICATION_CONTROL_START = "2017-10-01";
    private static final String STATUS_APPLICATION_CONTROL_END = "2017-10-31";
    private static final String DEFAULT_HOTEL_CODE = "XYZ";
    private static final String DEFAULT_INV_TYPE_CODE = "DEF";
    private static final String DEFAULT_INV_CODE = "ABC";

    @Test
    public void testValidate_ShouldThrow_WhenInventoriesIsNull() {
        validateAndAssert(VALIDATOR, null, null, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_INVENTORIES_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenContextIsNull() {
        InvCountType inventories = new InvCountType();

        validateAndAssert(VALIDATOR, inventories, null, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_CONTEXT_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenBothHotelCodeAndHotelNameAreMissing() {
        InvCountType inventories = new InvCountType();

        InventoriesContext ctx = new InventoriesContext(null, null);

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_HOTEL_CODE_AND_HOTEL_NAME_TO_BE_NOT_BOTH_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenInventoryListIsEmpty() {
        InvCountType inventories = this.buildValidInventories();

        InventoriesContext ctx = new InventoriesContext(null, null);

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_INVENTORIES_LIST_TO_BE_NOT_EMPTY
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenClosingSeasonElement_AndClosingSeasonNotSupported() {
        BaseInvCountType closingSeasonInventory = this.buildValidInventoryForClosingSeason();
        InvCountType inventories = this.buildValidInventories(closingSeasonInventory);

        InventoriesContext ctx = new InventoriesContext(OTAHotelInvCountNotifRQValidator.COMPLETE_SET, null);

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_CLOSING_SEASONS
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenClosingSeasonElement_AndClosingSeasonElementNotOnTopOfList() {
        BaseInvCountType inventory = this.buildValidInventoryForRooms();
        BaseInvCountType closingSeasonInventory = this.buildValidInventoryForClosingSeason();
        InvCountType inventories = this.buildValidInventories(inventory, closingSeasonInventory);

        InventoriesContext ctx = new InventoriesContext(
                OTAHotelInvCountNotifRQValidator.COMPLETE_SET,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().withClosingSeasonsSupport().build()
        );

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_CLOSING_SEASON_TO_BE_ON_TOP_OF_LIST);
    }

    @Test
    public void testValidate_ShouldThrow_WhenClosingSeasonElement_AndInvCountsNotNull() {
        BaseInvCountType closingSeasonInventory = this.buildValidInventoryForClosingSeason();
        closingSeasonInventory.setInvCounts(new InvCounts());
        InvCountType inventories = this.buildValidInventories(closingSeasonInventory);

        InventoriesContext ctx = new InventoriesContext(
                OTAHotelInvCountNotifRQValidator.COMPLETE_SET,
                new HotelInvCountNotifContext.Builder().withClosingSeasonsSupport().build()
        );

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_INV_COUNTS_TO_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCategoryUnsupportedAndHasCategory() {
        BaseInvCountType inventory = this.buildValidInventoryForCategories();
        InvCountType inventories = this.buildValidInventories(inventory);

        InventoriesContext ctx = new InventoriesContext(null, null);

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_CATEGORIES
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenRoomsUnsupportedAndHasRoom() {
        BaseInvCountType inventory = this.buildValidInventoryForRooms();
        InvCountType inventories = this.buildValidInventories(inventory);

        InventoriesContext ctx = new InventoriesContext(null, null);

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_ROOMS);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCategoriesAndRoomsAreMixed() {
        BaseInvCountType categoryInventory = this.buildValidInventoryForCategories();
        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        InvCountType inventories = this.buildValidInventories(categoryInventory, roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withCategoriesSupport().withRoomsSupport().build()
        );

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_ROOM_CATEGORY_AND_DISTINCT_ROOM_TO_NOT_BE_MIXED
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenInvCountListIsEmpty() {
        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        roomsInventory.setInvCounts(new InvCounts());

        InvCountType inventories = this.buildValidInventories(roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().build()
        );

        String message = String.format(ErrorMessage.EXPECT_INV_COUNTS_TO_HAVE_BETWEEN_ONE_AND_THREE_ELEMENTS, 0);

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenInvCountListHasMoreThanThreeElements() {
        InvCounts invCounts = new InvCounts();
        for (int i = 0; i < 4; i++) {
            invCounts.getInvCounts().add(new InvCount());
        }

        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        roomsInventory.setInvCounts(invCounts);

        InvCountType inventories = this.buildValidInventories(roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().build()
        );

        String message = String.format(ErrorMessage.EXPECT_INV_COUNTS_TO_HAVE_BETWEEN_ONE_AND_THREE_ELEMENTS, 4);

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_OnDuplicateCountType_2() {
        this.throwOnDuplicateCountType("2");
    }

    @Test
    public void testValidate_ShouldThrow_OnDuplicateCountType_6() {
        this.throwOnDuplicateCountType("6");
    }

    @Test
    public void testValidate_ShouldThrow_OnDuplicateCountType_9() {
        this.throwOnDuplicateCountType("9");
    }

    @Test
    public void testValidate_ShouldThrow_WhenOutOfOrderUnsupportedAndHasOutOfOrder() {
        InvCount invCount = new InvCount();
        invCount.setCountType("6");

        InvCounts invCounts = new InvCounts();
        invCounts.getInvCounts().add(invCount);

        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        roomsInventory.setInvCounts(invCounts);

        InvCountType inventories = this.buildValidInventories(roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().build()
        );

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_OUT_OF_ORDER
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenOutOfMarketUnsupportedAndHasOutOfMarket() {
        InvCount invCount = new InvCount();
        invCount.setCountType("9");

        InvCounts invCounts = new InvCounts();
        invCounts.getInvCounts().add(invCount);

        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        roomsInventory.setInvCounts(invCounts);

        InvCountType inventories = this.buildValidInventories(roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().build()
        );

        validateAndAssert(
                VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_OUT_OF_MARKET
        );
    }

    @Test
    public void testValidate_ShouldThrow_OnUnknownCountType() {
        InvCount invCount = new InvCount();
        invCount.setCountType("0");

        InvCounts invCounts = new InvCounts();
        invCounts.getInvCounts().add(invCount);

        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        roomsInventory.setInvCounts(invCounts);

        InvCountType inventories = this.buildValidInventories(roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().build()
        );

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_COUNT_TYPE_TO_BE_ONE_OF_2_6_9);
    }

    private void throwOnDuplicateCountType(String countType) {
        InvCounts invCounts = new InvCounts();
        for (int i = 0; i < 2; i++) {
            InvCount invCount = new InvCount();
            invCount.setCountType(countType);
            invCounts.getInvCounts().add(invCount);
        }

        BaseInvCountType roomsInventory = this.buildValidInventoryForRooms();
        roomsInventory.setInvCounts(invCounts);

        InvCountType inventories = this.buildValidInventories(roomsInventory);

        InventoriesContext ctx = new InventoriesContext(
                null,
                new HotelInvCountNotifContext.Builder().withRoomsSupport().withOutOfMarketSupport().withOutOfOrderSupport().build()
        );

        String message = String.format(ErrorMessage.EXPECT_NO_DUPLICATE_COUNT_TYPE, countType);

        validateAndAssert(VALIDATOR, inventories, ctx, VALIDATION_PATH, ValidationException.class, message);
    }

    private InvCountType buildValidInventories(BaseInvCountType... inventoryList) {
        InvCountType inventories = new InvCountType();
        inventories.setHotelCode(DEFAULT_HOTEL_CODE);

        if (inventoryList != null) {
            inventories.getInventories().addAll(Arrays.asList(inventoryList));
        }

        return inventories;
    }

    private BaseInvCountType buildValidInventoryForClosingSeason() {
        StatusApplicationControlType statusApplicationControl = new StatusApplicationControlType();
        statusApplicationControl.setStart(STATUS_APPLICATION_CONTROL_START);
        statusApplicationControl.setEnd(STATUS_APPLICATION_CONTROL_END);
        statusApplicationControl.setAllInvCode(true);
        BaseInvCountType inventory = new BaseInvCountType();
        inventory.setStatusApplicationControl(statusApplicationControl);
        return inventory;
    }

    private BaseInvCountType buildValidInventoryForRooms() {
        BaseInvCountType inventory = this.buildValidInventoryForCategories();
        inventory.getStatusApplicationControl().setInvCode(DEFAULT_INV_CODE);
        return inventory;
    }

    private BaseInvCountType buildValidInventoryForCategories() {
        StatusApplicationControlType statusApplicationControl = this.buildValidStatusApplicationControl();
        BaseInvCountType inventory = new BaseInvCountType();
        inventory.setStatusApplicationControl(statusApplicationControl);
        return inventory;
    }

    private StatusApplicationControlType buildValidStatusApplicationControl() {
        StatusApplicationControlType statusApplicationControl = new StatusApplicationControlType();
        statusApplicationControl.setStart(STATUS_APPLICATION_CONTROL_START);
        statusApplicationControl.setEnd(STATUS_APPLICATION_CONTROL_END);
        statusApplicationControl.setInvTypeCode(DEFAULT_INV_TYPE_CODE);
        return statusApplicationControl;
    }
}