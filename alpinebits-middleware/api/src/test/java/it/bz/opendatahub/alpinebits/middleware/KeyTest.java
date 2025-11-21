// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.middleware;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for {@link Key} class.
 */
public class KeyTest {

    private static final String DEFAULT_IDENTIFIER = "identifier";
    private static final Class<String> DEFAULT_TYPE = String.class;

    @Test
    public void testKey() {
        Key<String> key = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertEquals(key.getIdentifier(), DEFAULT_IDENTIFIER);
        assertEquals(key.getType(), DEFAULT_TYPE);
    }

    @Test
    public void testEquals_FalseOnNullCompare() {
        Key<String> key1 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertNotEquals(key1, null);
    }

    @Test
    public void testEquals_FalseOnOtherTypeCompare() {
        Key<String> key1 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertNotEquals(key1, new Object());
    }

    @Test
    public void testEquals_TrueOnSameInstance() {
        Key<String> key1 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertEquals(key1, key1);
    }

    @Test
    public void testEquals_TrueOnSameIdentifierAndType() {
        Key<String> key1 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        Key<String> key2 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertTrue(key1.equals(key2));
    }

    @Test
    public void testHashCode() {
        Key<String> key1 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        Key<String> key2 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testToString() {
        Key<String> key1 = Key.key(DEFAULT_IDENTIFIER, DEFAULT_TYPE);
        assertTrue(key1.toString().contains(DEFAULT_IDENTIFIER) && key1.toString().contains(DEFAULT_TYPE.toString()));
    }
}