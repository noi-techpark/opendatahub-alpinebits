// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.common.utils.middleware;

import it.bz.opendatahub.alpinebits.common.utils.middleware.utils.ContextBuilder;
import it.bz.opendatahub.alpinebits.common.utils.middleware.utils.MiddlewareBuilder;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link ComposingMiddlewareBuilder} class.
 */
public class ComposingMiddlewareBuilderTest {

    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String KEY_3 = "key3";
    private static final String KEY_4 = "key4";
    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_3 = "value3";
    private static final String VALUE_4 = "value4";

    @DataProvider(name = "testComposeDataProvider")
    public static Object[][] testComposeDataProvider() {
        return new Object[][]{
                {false, false, Optional.empty()},
                {false, true, Optional.empty()},
                {true, false, Optional.of(VALUE_2)},
                {true, true, Optional.of(VALUE_2)},
        };
    }

    @DataProvider(name = "testNestedComposeDataProvider")
    public static Object[][] testNestedComposeDataProvider() {
        return new Object[][]{
                {false, false, false, Optional.empty()},
                {false, false, true, Optional.empty()},
                {false, true, false, Optional.empty()},
                {false, true, true, Optional.empty()},
                {true, false, false, Optional.empty()},
                {true, false, true, Optional.empty()},
                {true, true, false, Optional.of(VALUE_3)},
                {true, true, true, Optional.of(VALUE_3)},
        };
    }


    @DataProvider(name = "testNestedComposeDataProvider_WithTwoComposedMiddlewares")
    public static Object[][] testNestedComposeDataProvider_WithTwoComposedMiddlewares() {
        return new Object[][]{
                {false, false, false, false, Optional.empty()},
                {false, false, false, true, Optional.empty()},
                {false, false, true, false, Optional.empty()},
                {false, false, true, true, Optional.empty()},
                {false, true, false, false, Optional.empty()},
                {false, true, false, true, Optional.empty()},
                {false, true, true, false, Optional.empty()},
                {false, true, true, true, Optional.empty()},
                {true, false, false, false, Optional.empty()},
                {true, false, false, true, Optional.empty()},
                {true, false, true, false, Optional.empty()},
                {true, false, true, true, Optional.empty()},
                {true, true, false, false, Optional.empty()},
                {true, true, false, true, Optional.empty()},
                {true, true, true, false, Optional.of(VALUE_4)},
                {true, true, true, true, Optional.of(VALUE_4)},
        };
    }

    @Test(dataProvider = "testComposeDataProvider")
    public void testCompose_WithVarArgs(
            boolean middleware1CallsNext,
            boolean middleware2CallsNext,
            Optional<String> expectedValue
    ) {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, middleware1CallsNext);
        Middleware middleware2 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, middleware2CallsNext);

        Middleware composedMiddleware = ComposingMiddlewareBuilder.compose(middleware1, middleware2);
        composedMiddleware.handleContext(ctx, null);

        assertEquals(ctx.get(key1), Optional.of(VALUE_1));
        assertEquals(ctx.get(key2), expectedValue);

        // Assert the value of the toString() method for 100% test coverage
        assertEquals(composedMiddleware.toString(), ComposingMiddlewareBuilder.COMPOSING_MIDDLEWARE_NAME);
    }

    @Test(dataProvider = "testComposeDataProvider")
    public void testCompose_WithList(
            boolean middleware1CallsNext,
            boolean middleware2CallsNext,
            Optional<String> expectedValue
    ) {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, middleware1CallsNext);
        Middleware middleware2 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, middleware2CallsNext);

        Middleware composedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, middleware2));
        composedMiddleware.handleContext(ctx, null);

        assertEquals(ctx.get(key1), Optional.of(VALUE_1));
        assertEquals(ctx.get(key2), expectedValue);

        // Assert the value of the toString() method for 100% test coverage
        assertEquals(composedMiddleware.toString(), ComposingMiddlewareBuilder.COMPOSING_MIDDLEWARE_NAME);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testComposeOneMiddlewareNull() {
        List<Middleware> middlewares = new ArrayList<>();
        middlewares.add(null);
        ComposingMiddlewareBuilder.compose(middlewares);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = MiddlewareBuilder.EXCEPTION_MESSAGE)
    public void testComposeWithFirstMiddlewareThrowing() {
        Context ctx = ContextBuilder.buildSimpleContext();

        Middleware middleware1 = MiddlewareBuilder.buildThrowingMiddleware();
        Middleware composedMiddleware = ComposingMiddlewareBuilder.compose(Collections.singletonList(middleware1));
        composedMiddleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = MiddlewareBuilder.EXCEPTION_MESSAGE)
    public void testComposeWithSecondMiddlewareThrowing() {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key = Key.key(KEY_1, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key, VALUE_1, true);
        Middleware middleware2 = MiddlewareBuilder.buildThrowingMiddleware();
        Middleware composedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, middleware2));
        composedMiddleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = MiddlewareBuilder.EXCEPTION_MESSAGE)
    public void testComposeWithFirstComposingMiddlewareThrowing() {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, true);
        Middleware middleware2 = MiddlewareBuilder.buildThrowingMiddleware();
        Middleware middleware3 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, true);

        Middleware tmpComposedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, middleware2));
        Middleware finalComposedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(tmpComposedMiddleware, middleware3));

        finalComposedMiddleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = MiddlewareBuilder.EXCEPTION_MESSAGE)
    public void testComposeWithSecondComposingMiddlewareThrowing() {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, true);
        Middleware middleware2 = MiddlewareBuilder.buildThrowingMiddleware();
        Middleware middleware3 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, true);

        Middleware tmpComposedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware2, middleware3));
        Middleware finalComposedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, tmpComposedMiddleware));

        finalComposedMiddleware.handleContext(ctx, null);
    }

    @Test(dataProvider = "testNestedComposeDataProvider")
    public void testNestedCompose_WhereComposedMiddlewareIsFirst(
            boolean middleware1CallsNext,
            boolean middleware2CallsNext,
            boolean middleware3CallsNext,
            Optional<String> expectedValue
    ) {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);
        Key<String> key3 = Key.key(KEY_3, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, middleware1CallsNext);
        Middleware middleware2 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, middleware2CallsNext);
        Middleware middleware3 = MiddlewareBuilder.buildMiddleware(key3, VALUE_3, middleware3CallsNext);

        // Compose middlewares 1 and 2
        Middleware tmpComposedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, middleware2));

        // Compose tmpComposedMiddleware and middleware 3
        Middleware finalComposedMiddlewar = ComposingMiddlewareBuilder.compose(Arrays.asList(tmpComposedMiddleware, middleware3));

        finalComposedMiddlewar.handleContext(ctx, null);

        assertEquals(ctx.get(key1), Optional.of(VALUE_1));
        assertEquals(ctx.get(key3), expectedValue);
    }

    @Test(dataProvider = "testNestedComposeDataProvider")
    public void testNestedCompose_WhereComposedMiddlewareIsLast(
            boolean middleware1CallsNext,
            boolean middleware2CallsNext,
            boolean middleware3CallsNext,
            Optional<String> expectedValue
    ) {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);
        Key<String> key3 = Key.key(KEY_3, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, middleware1CallsNext);
        Middleware middleware2 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, middleware2CallsNext);
        Middleware middleware3 = MiddlewareBuilder.buildMiddleware(key3, VALUE_3, middleware3CallsNext);

        // Compose middlewares 2 and 3
        Middleware tmpComposedMiddleware = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware2, middleware3));

        // Compose middleware1 and tmpComposedMiddleware and middleware 3
        Middleware finalComposedMiddlewar = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, tmpComposedMiddleware));

        finalComposedMiddlewar.handleContext(ctx, null);

        assertEquals(ctx.get(key1), Optional.of(VALUE_1));
        assertEquals(ctx.get(key3), expectedValue);
    }

    @Test(dataProvider = "testNestedComposeDataProvider_WithTwoComposedMiddlewares")
    public void testNestedCompose_WithTwoComposedMiddlewares(
            boolean middleware1CallsNext,
            boolean middleware2CallsNext,
            boolean middleware3CallsNext,
            boolean middleware4CallsNext,
            Optional<String> expectedValue
    ) {
        Context ctx = ContextBuilder.buildSimpleContext();

        Key<String> key1 = Key.key(KEY_1, String.class);
        Key<String> key2 = Key.key(KEY_2, String.class);
        Key<String> key3 = Key.key(KEY_3, String.class);
        Key<String> key4 = Key.key(KEY_4, String.class);

        Middleware middleware1 = MiddlewareBuilder.buildMiddleware(key1, VALUE_1, middleware1CallsNext);
        Middleware middleware2 = MiddlewareBuilder.buildMiddleware(key2, VALUE_2, middleware2CallsNext);
        Middleware middleware3 = MiddlewareBuilder.buildMiddleware(key3, VALUE_3, middleware3CallsNext);
        Middleware middleware4 = MiddlewareBuilder.buildMiddleware(key4, VALUE_4, middleware4CallsNext);

        // Compose middlewares 1 and 2
        Middleware tmpComposedMiddleware1 = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware1, middleware2));

        // Compose middlewares 3 and 4
        Middleware tmpComposedMiddleware2 = ComposingMiddlewareBuilder.compose(Arrays.asList(middleware3, middleware4));

        // Compose middleware1 and tmpComposedMiddleware and middleware 3
        Middleware finalComposedMiddlewar = ComposingMiddlewareBuilder.compose(Arrays.asList(tmpComposedMiddleware1, tmpComposedMiddleware2));

        finalComposedMiddlewar.handleContext(ctx, null);

        assertEquals(ctx.get(key1), Optional.of(VALUE_1));
        assertEquals(ctx.get(key4), expectedValue);
    }
}