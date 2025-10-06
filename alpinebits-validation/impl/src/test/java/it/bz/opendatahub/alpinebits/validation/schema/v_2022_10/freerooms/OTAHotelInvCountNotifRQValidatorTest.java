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
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelInvCountNotifRQ;
import org.testng.annotations.Test;

import static it.bz.opendatahub.alpinebits.validation.schema.common.ValidationUtil.validateAndAssert;

/**
 * Tests for {@link OTAHotelInvCountNotifRQValidator}.
 */
public class OTAHotelInvCountNotifRQValidatorTest {

    private static final OTAHotelInvCountNotifRQValidator VALIDATOR = new OTAHotelInvCountNotifRQValidator();
    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.OTA_HOTEL_INV_COUNT_NOTIF_RQ);

    @Test
    public void testValidate_ShouldThrow_WhenOTAHotelInvCountNotifRQIsNull() {
        validateAndAssert(VALIDATOR, null, null, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_HOTEL_INV_COUNT_NOTIF_RQ_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenContextIsNull() {
        OTAHotelInvCountNotifRQ rq = new OTAHotelInvCountNotifRQ();
        validateAndAssert(VALIDATOR, rq, null, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_CONTEXT_TO_BE_NOT_NULL);
    }

}