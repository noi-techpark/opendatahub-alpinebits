// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for {@link SimpleValidationPath}.
 */
public class SimpleValidationPathTest {

    private static final String PATH_1 = "one";
    private static final String PATH_2 = "two";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromPath_ShouldThrow_WhenInitialPathIsNull() {
        SimpleValidationPath.fromPath(null);
    }

    @Test
    public void testFromPath() {
        ValidationPath validationPath = SimpleValidationPath.fromPath(PATH_1);
        assertEquals(validationPath.toString(), PATH_1);
    }

    @Test
    public void testWithAttribute() {
        String attribute = "two";
        ValidationPath validationPath1 = SimpleValidationPath.fromPath(PATH_1);
        ValidationPath validationPath2 = validationPath1.withAttribute(attribute);

        String expectedPath = PATH_1 + SimpleValidationPath.OPEN_ATTRIBUTE_CHAR + attribute + SimpleValidationPath.CLOSE_ATTRIBUTE_CHAR;
        assertEquals(validationPath2.toString(), expectedPath);
    }

    @Test
    public void testWithElement() {
        ValidationPath validationPath1 = SimpleValidationPath.fromPath(PATH_1);
        ValidationPath validationPath2 = validationPath1.withElement(PATH_2);

        String expectedPath = PATH_1 + SimpleValidationPath.DELIMITER + PATH_2;
        assertEquals(validationPath2.toString(), expectedPath);
    }

    @Test
    public void testWithIndex() {
        int index = 0;
        ValidationPath validationPath1 = SimpleValidationPath.fromPath(PATH_1);
        ValidationPath validationPath2 = validationPath1.withIndex(index);

        String expectedPath = PATH_1 + SimpleValidationPath.OPEN_INDEX_CHAR + index + SimpleValidationPath.CLOSE_INDEX_CHAR;
        assertEquals(validationPath2.toString(), expectedPath);
    }

    @Test
    public void testEquals_ShouldReturnTrue_WhenSameObject() {
        SimpleValidationPath path = SimpleValidationPath.fromPath(PATH_1);
        assertEquals(path, path);
    }

    @Test
    public void testEquals_ShouldReturnTrue_WhenSamePath() {
        SimpleValidationPath path1 = SimpleValidationPath.fromPath(PATH_1);
        SimpleValidationPath path2 = SimpleValidationPath.fromPath(PATH_1);
        assertEquals(path1, path2);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenComparedWithNull() {
        SimpleValidationPath path = SimpleValidationPath.fromPath(PATH_1);
        assertNotEquals(path, null);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenDifferentType() {
        SimpleValidationPath path = SimpleValidationPath.fromPath(PATH_1);
        assertNotEquals(path, "");
    }

    @Test
    public void testHashCode_ShouldMatch_WhenSamePath() {
        SimpleValidationPath path1 = SimpleValidationPath.fromPath(PATH_1);
        SimpleValidationPath path2 = SimpleValidationPath.fromPath(PATH_1);
        assertEquals(path1.hashCode(), path2.hashCode());
    }

}