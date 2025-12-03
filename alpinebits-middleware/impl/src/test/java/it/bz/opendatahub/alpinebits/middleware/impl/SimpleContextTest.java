// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.middleware.impl;

import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.*;

/**
 * Test cases for {@link SimpleContext} class.
 */
public class SimpleContextTest {

    @Test
    public void testGetValue() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey = Key.key("intKey", Integer.class);
        int intValue = 1;
        ctx.put(intKey, intValue);

        Optional<Integer> ctxIntValue = ctx.get(intKey);
        assertTrue(ctxIntValue.isPresent());
        assertEquals(ctxIntValue.get().intValue(), intValue);

        Key<Long> longKey = Key.key("longKey", Long.class);
        Long longValue = 2L;
        ctx.put(longKey, longValue);

        Optional<Long> ctxLongValue = ctx.get(longKey);
        assertTrue(ctxLongValue.isPresent());
        assertEquals(ctxLongValue.get(), longValue);

        Key<String> stringKey = Key.key("stringKey", String.class);
        String stringValue = "one";
        ctx.put(stringKey, stringValue);

        Optional<String> ctxStringValue = ctx.get(stringKey);
        assertTrue(ctxStringValue.isPresent());
        assertEquals(ctxStringValue.get(), stringValue);

        Key<Object> objectKey = Key.key("objectKey", Object.class);
        Object objectValue = new Object();
        ctx.put(objectKey, objectValue);

        Optional<Object> ctxObjectValue = ctx.get(objectKey);
        assertTrue(ctxObjectValue.isPresent());
        assertEquals(objectValue, ctxObjectValue.get());
    }

    @Test
    public void testGetOrThrow_ValuePresent() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey = Key.key("intKey", Integer.class);
        int intValue = 1;
        ctx.put(intKey, intValue);

        Integer ctxIntValue = ctx.getOrThrow(intKey);
        assertEquals(ctxIntValue.intValue(), intValue);

        Key<Long> longKey = Key.key("longKey", Long.class);
        Long longValue = 2L;
        ctx.put(longKey, longValue);

        Long ctxLongValue = ctx.getOrThrow(longKey);
        assertEquals(ctxLongValue, longValue);

        Key<String> stringKey = Key.key("stringKey", String.class);
        String stringValue = "one";
        ctx.put(stringKey, stringValue);

        String ctxStringValue = ctx.getOrThrow(stringKey);
        assertEquals(ctxStringValue, stringValue);

        Key<Object> objectKey = Key.key("objectKey", Object.class);
        Object objectValue = new Object();
        ctx.put(objectKey, objectValue);

        Object ctxObjectValue = ctx.getOrThrow(objectKey);
        assertEquals(objectValue, ctxObjectValue);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testGetOrThrow_ValueNotPresent() {
        Context ctx = this.buildSimpleContext();

        Key<Object> undefinedKey = Key.key("undefined key", Object.class);

        ctx.getOrThrow(undefinedKey);
    }

    @Test
    public void testSetValue() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey = Key.key("intKey", Integer.class);
        int intValue1 = 1;
        ctx.put(intKey, intValue1);

        Optional<Integer> ctxValue = ctx.get(intKey);
        assertTrue(ctxValue.isPresent());
        assertEquals(ctxValue.get().intValue(), intValue1);

        int intValue2 = 2;
        ctx.put(intKey, intValue2);

        ctxValue = ctx.get(intKey);
        assertTrue(ctxValue.isPresent());
        assertEquals(ctxValue.get().intValue(), intValue2);
    }

    @Test
    public void testRemoveValue() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey = Key.key("intKey", Integer.class);
        int intValue = 1;
        ctx.put(intKey, intValue);

        Optional<Integer> ctxValue = ctx.get(intKey);
        assertTrue(ctxValue.isPresent());
        assertEquals(ctxValue.get().intValue(), intValue);

        int removedValue = ctx.remove(intKey);

        assertEquals(removedValue, intValue);
        assertFalse(ctx.get(intKey).isPresent());
    }

    @Test
    public void testContextContainsKey() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey = Key.key("intKey", Integer.class);
        int intValue = 1;
        ctx.put(intKey, intValue);

        assertTrue(ctx.contains(intKey));

        Key<Object> undefinedKey = Key.key("undefined key", Object.class);
        assertFalse(ctx.contains(undefinedKey));
    }

    @Test
    public void testContextDoesntContainKey() {
        Context ctx = this.buildSimpleContext();

        Key<Object> undefinedKey = Key.key("undefined key", Object.class);

        assertFalse(ctx.contains(undefinedKey));
        assertEquals(ctx.get(undefinedKey), Optional.empty());
    }

    @Test
    public void testSameKeys_SameType() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey1 = Key.key("intKey", Integer.class);
        Key<Integer> intKey2 = Key.key("intKey", Integer.class);

        int intValue1 = 1;
        int intValue2 = 2;

        ctx.put(intKey1, intValue1);
        ctx.put(intKey2, intValue2);

        int intResult1 = ctx.getOrThrow(intKey1);
        int intResult2 = ctx.getOrThrow(intKey2);

        assertEquals(intResult1, intResult2);
    }

    @Test
    public void testSameKeys_DifferentTypes() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> key1 = Key.key("key", Integer.class);
        Key<String> key2 = Key.key("key", String.class);

        int value1 = 1;
        String value2 = "s";

        ctx.put(key1, value1);
        ctx.put(key2, value2);

        int result1 = ctx.getOrThrow(key1);
        String result2 = ctx.getOrThrow(key2);

        assertNotEquals(result1, result2);
    }

    @Test
    public void testDifferentKeys_SameTypes() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> intKey1 = Key.key("intKey1", Integer.class);
        Key<Integer> intKey2 = Key.key("intKey2", Integer.class);

        int intValue1 = 1;
        int intValue2 = 2;

        ctx.put(intKey1, intValue1);
        ctx.put(intKey2, intValue2);

        int intResult1 = ctx.getOrThrow(intKey1);
        int intResult2 = ctx.getOrThrow(intKey2);

        assertNotEquals(intResult1, intResult2);
    }

    @Test
    public void testDifferentKeys_DifferentTypes() {
        Context ctx = this.buildSimpleContext();

        Key<Integer> key1 = Key.key("key1", Integer.class);
        Key<String> key2 = Key.key("key2", String.class);

        int value1 = 1;
        String value2 = "s";

        ctx.put(key1, value1);
        ctx.put(key2, value2);

        int result1 = ctx.getOrThrow(key1);
        String result2 = ctx.getOrThrow(key2);

        assertNotEquals(result1, result2);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testHandleException() {
        Context ctx = this.buildSimpleContext();
        ctx.handleException(new Exception());
    }

    private Context buildSimpleContext() {
        return new SimpleContext();
    }
}