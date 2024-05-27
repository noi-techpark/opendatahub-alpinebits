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
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.StatusApplicationControlType;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * Tests for {@link ClosingSeasonsOverlapValidator}.
 */
public class ClosingSeasonsOverlapValidatorTest {

    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.INVENTORIES_LIST);

    @Test
    public void testValidate_ShouldThrow_WhenClosingSeasonsOverlap() {
        // Closing seasons p1 and p3 overlap
        ClosingSeasonsOverlapValidator.Period p1 = new ClosingSeasonsOverlapValidator.Period("2022-01-01", "2022-01-04");
        ClosingSeasonsOverlapValidator.Period p2 = new ClosingSeasonsOverlapValidator.Period("2022-01-04", "2022-01-10");
        ClosingSeasonsOverlapValidator.Period p3 = new ClosingSeasonsOverlapValidator.Period("2022-01-02", "2022-01-06");
        ClosingSeasonsOverlapValidator.Period p4 = new ClosingSeasonsOverlapValidator.Period("2022-02-07", "2022-02-14");
        BaseInvCountType inventory1 = this.buildValidInventoryForClosingSeason(p1);
        BaseInvCountType inventory2 = this.buildValidInventoryForClosingSeason(p2);
        BaseInvCountType inventory3 = this.buildValidInventoryForClosingSeason(p3);
        BaseInvCountType inventory4 = this.buildValidInventoryForClosingSeason(p4);

        String errorMessage = String.format(ErrorMessage.EXPECT_NO_OVERLAPPING_TIME_PERIODS, p1, p3);

        ClosingSeasonsOverlapValidator validator = new ClosingSeasonsOverlapValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                ValidationException.class,
                () -> validator.validate(Arrays.asList(inventory1, inventory2, inventory3, inventory4), null, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }

    @Test
    public void testValidate_ShouldAcceptNonOverlappingPeriods() {
        ClosingSeasonsOverlapValidator.Period p1 = new ClosingSeasonsOverlapValidator.Period("2022-01-01", "2022-01-04");
        ClosingSeasonsOverlapValidator.Period p2 = new ClosingSeasonsOverlapValidator.Period("2022-01-04", "2022-01-06");
        ClosingSeasonsOverlapValidator.Period p3 = new ClosingSeasonsOverlapValidator.Period("2022-02-07", "2022-02-14");
        ClosingSeasonsOverlapValidator.Period p4 = new ClosingSeasonsOverlapValidator.Period("2022-08-01", "2022-08-21");
        BaseInvCountType inventory1 = this.buildValidInventoryForClosingSeason(p1);
        BaseInvCountType inventory2 = this.buildValidInventoryForClosingSeason(p2);
        BaseInvCountType inventory3 = this.buildValidInventoryForClosingSeason(p3);
        BaseInvCountType inventory4 = this.buildValidInventoryForClosingSeason(p4);

        ClosingSeasonsOverlapValidator validator = new ClosingSeasonsOverlapValidator();
        validator.validate(Arrays.asList(inventory1, inventory2, inventory3, inventory4), null, VALIDATION_PATH);

        // Just run an assertion
        assertTrue(true);
    }

    private BaseInvCountType buildValidInventoryForClosingSeason(ClosingSeasonsOverlapValidator.Period period) {
        StatusApplicationControlType statusApplicationControl = new StatusApplicationControlType();
        statusApplicationControl.setStart(period.getStart().toString());
        statusApplicationControl.setEnd(period.getEnd().toString());
        statusApplicationControl.setAllInvCode(true);
        BaseInvCountType inventory = new BaseInvCountType();
        inventory.setStatusApplicationControl(statusApplicationControl);
        return inventory;
    }

}