/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AffiliationInfoType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AffiliationInfoType.Awards;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AffiliationInfoType.Awards.Award;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link AffiliationInfoValidator}.
 */
public class AffiliationInfoValidatorTest {

    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.AFFILIATION_INFO);

    private static final String DEFAULT_RATING = "95";
    private static final String DEFAULT_PROVIDER = "TRUSTYOU";

    @Test
    public void testValidate_ShouldThrow_WhenAffiliationInfoTypeIsNull() {
        this.validateAndAssert(null, NullValidationException.class, ErrorMessage.EXPECT_AFFILIATION_INFO_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAwardsIsNull() {
        this.validateAndAssert(new AffiliationInfoType(), NullValidationException.class, ErrorMessage.EXPECT_AWARDS_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAwardListIsNull() {
        AffiliationInfoType affiliationInfoType = buildAffiliationInfoType(null);
        this.validateAndAssert(affiliationInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_AWARD_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAwardListIsEmpty() {
        AffiliationInfoType affiliationInfoType = buildAffiliationInfoType(new ArrayList<>());
        this.validateAndAssert(affiliationInfoType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_AWARD_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenRatingIsNull() {
        Award award = buildAward(null, DEFAULT_PROVIDER);
        AffiliationInfoType affiliationInfoType = buildAffiliationInfoType(Collections.singletonList(award));
        this.validateAndAssert(affiliationInfoType, NullValidationException.class, ErrorMessage.EXPECT_RATING_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenProviderIsNull() {
        Award award = buildAward(DEFAULT_RATING, null);
        AffiliationInfoType affiliationInfoType = buildAffiliationInfoType(Collections.singletonList(award));
        this.validateAndAssert(affiliationInfoType, NullValidationException.class, ErrorMessage.EXPECT_PROVIDER_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenProviderIsNotAllUpperCase() {
        Award award = buildAward(DEFAULT_RATING, "Not all uppercase");
        AffiliationInfoType affiliationInfoType = buildAffiliationInfoType(Collections.singletonList(award));
        this.validateAndAssert(affiliationInfoType, ValidationException.class, ErrorMessage.EXPECT_PROVIDER_TO_BE_ALL_UPPERCASE);
    }

    private AffiliationInfoType buildAffiliationInfoType(List<Award> awardList) {
        Awards awards = new Awards();
        if (awardList != null) {
            awards.getAwards().addAll(awardList);
        }

        AffiliationInfoType affiliationInfoType = new AffiliationInfoType();
        affiliationInfoType.setAwards(awards);

        return affiliationInfoType;
    }

    private Award buildAward(String rating, String provider) {
        Award award = new Award();
        award.setRating(rating);
        award.setProvider(provider);
        return award;
    }

    private void validateAndAssert(
            AffiliationInfoType data,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        AffiliationInfoValidator validator = new AffiliationInfoValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, null, VALIDATION_PATH)
        );

        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }
}