// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.common;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for {@link AlpineBitsVersionChecker}.
 */
public class AlpineBitsVersionCheckerTest {

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2010_08() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2010_08));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2010_10() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2010_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2011_09() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2011_09));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2011_10() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2011_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2011_11() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2011_11));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2012_05() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2012_05));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2012_05B() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2012_05B));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2013_04() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2013_04));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2014_04() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2014_04));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2015_07() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2015_07));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2015_07B() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2015_07B));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnTrueForAlpineBitsV_2017_10() {
        assertTrue(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2017_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnFalseForAlpineBitsV_2018_10() {
        assertFalse(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2018_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnFalseForAlpineBitsV_2020_10() {
        assertFalse(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2020_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnFalseForAlpineBitsV_2022_10() {
        assertFalse(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2022_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnFalseForAlpineBitsV_2024_10() {
        assertFalse(AlpineBitsVersionChecker.isLegacyVersion(AlpineBitsVersion.V_2024_10));
    }

    @Test
    public void testIsLegacyVersion_ShouldReturnFalseForUnknownVersion() {
        assertFalse(AlpineBitsVersionChecker.isLegacyVersion("some version"));
    }

}