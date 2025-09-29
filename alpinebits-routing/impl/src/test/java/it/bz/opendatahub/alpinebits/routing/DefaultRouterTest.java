// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Test cases for {@link DefaultRouter} class.
 */
public class DefaultRouterTest {

    private static final String DEFAULT_VERSION = AlpineBitsVersion.V_2017_10;
    private static final String DEFAULT_ACTION_REQUEST_PARAM = "ACTION_REQUEST_PARAM";
    private static final String DEFAULT_ACTION_NAME = "ACTION_NAME";
    private static final Action DEFAULT_ACTION = Action.of(DEFAULT_ACTION_REQUEST_PARAM, DEFAULT_ACTION_NAME);
    private static final String DEFAULT_CAPABILITY = "DEFAULT_CAPABILITY";
    private static final String UNKNOWN_VERSION = "some_version";
    private static final String UNKNOWN_ACTION_REQUEST_PARAM = "some_action";
    private static final String UNKNOWN_CAPABILITY = "some_capability";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindMiddleware_throwsOnNullVersion() {
        this.getRouter(null, DEFAULT_ACTION);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindMiddleware_throwsOnNullAction() {
        this.getRouter(DEFAULT_VERSION, null);
    }

    @Test
    public void testFindMiddleware_emptyOptionalOnUnknownVersion() {
        Router router = this.getRouter(DEFAULT_VERSION, DEFAULT_ACTION);
        Optional<Middleware> middleware = router.findMiddleware(UNKNOWN_VERSION, DEFAULT_ACTION_REQUEST_PARAM);
        assertFalse(middleware.isPresent());
    }

    @Test
    public void testFindMiddleware_emptyOptionalOnUnknownAction() {
        Router router = this.getRouter(DEFAULT_VERSION, DEFAULT_ACTION);
        Optional<Middleware> middleware = router.findMiddleware(DEFAULT_VERSION, UNKNOWN_ACTION_REQUEST_PARAM);
        assertFalse(middleware.isPresent());
    }

    @Test
    public void testFindMiddleware_notEmptyIfFound() {
        Router router = this.getDefaultRouter();
        Optional<Middleware> middleware = router.findMiddleware(DEFAULT_VERSION, DEFAULT_ACTION_REQUEST_PARAM);
        assertTrue(middleware.isPresent());
    }

    @Test
    public void testFindMiddleware_expectedMiddlewareExecutionWhenInvoked() {
        Key<String> ctxKey = Key.key("ctxKey", String.class);
        String ctxValue = "ctxValue";

        Middleware middleware = (ctx, chain) -> ctx.put(ctxKey, ctxValue);
        Router router = this.getRouter(DEFAULT_VERSION, DEFAULT_ACTION, middleware);
        Optional<Middleware> optionalMiddleware = router.findMiddleware(DEFAULT_VERSION, DEFAULT_ACTION_REQUEST_PARAM);

        assertTrue(optionalMiddleware.isPresent());
        Middleware resultMiddleware = optionalMiddleware.get();

        Context ctx = new SimpleContext();
        resultMiddleware.handleContext(ctx, null);

        String value = ctx.getOrThrow(ctxKey);

        assertEquals(value, ctxValue);
    }

    @Test
    public void testGetVersions_listOfDefinedVersions() {
        String version1 = "2017-10";
        String version2 = "2018-10";

        Router router = new DefaultRouter.Builder()
                .version(version1)
                .supportsAction(Action.GET_CAPABILITIES)
                .withCapabilities()
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .and()
                .version(version2)
                .supportsAction(Action.GET_VERSION)
                .withCapabilities()
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();
        Collection<String> versions = router.getVersions();
        assertEquals(versions, Arrays.asList(version1, version2));
    }

    @Test
    public void testGetVersion_returnVersionOnMatch() {
        Router router = this.getDefaultRouter();
        assertEquals(router.getVersion(DEFAULT_VERSION), DEFAULT_VERSION);
    }

    @Test
    public void testGetVersion_returnHighestVersionIfNoMatch() {
        String version1 = "2017-10";
        String version2 = "2018-10";

        Router router = new DefaultRouter.Builder()
                .version(version1)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities()
                .using(null)
                .versionComplete()
                .and()
                .version(version2)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities()
                .using(null)
                .versionComplete()
                .buildRouter();
        assertEquals(router.getVersion("xxx"), version2);
    }

    @Test
    public void testGetActionsForVersion_emptyForUnknownVersion() {
        Router router = this.getDefaultRouter();
        Optional<Set<Action>> optional = router.getActionsForVersion(UNKNOWN_VERSION);
        assertFalse(optional.isPresent());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetActionsForVersion_throwsOnNullVersion() {
        this.getDefaultRouter().getActionsForVersion(null);
    }

    @Test
    public void testGetActionsForVersion_listOfActions() {
        Middleware middleware = (ctx, chain) -> {
        };

        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(Action.GET_VERSION)
                .withCapabilities()
                .using(middleware)
                .and()
                .supportsAction(Action.GET_CAPABILITIES)
                .withCapabilities()
                .using(middleware)
                .versionComplete()
                .buildRouter();

        assertTrue(router.getActionsForVersion(DEFAULT_VERSION).isPresent());
        Set<Action> actions = router.getActionsForVersion(DEFAULT_VERSION).get();

        assertEquals(actions.size(), 2);
    }

    @Test
    public void testGetCapabilitiesForVersion_listOfCapabilities() {
        String capability1 = "cap1";
        String capability2 = "cap2";

        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities(capability1, capability2)
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();

        assertTrue(router.getCapabilitiesForVersion(DEFAULT_VERSION).isPresent());
        Set<String> capabilities = router.getCapabilitiesForVersion(DEFAULT_VERSION).get();

        assertEquals(capabilities.size(), 2);
    }

    @Test
    public void testGetCapabilitiesForVersion_emptyCapabilities() {
        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities()
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();

        assertTrue(router.getCapabilitiesForVersion(DEFAULT_VERSION).isPresent());
        Set<String> capabilities = router.getCapabilitiesForVersion(DEFAULT_VERSION).get();

        assertEquals(capabilities.size(), 0);
    }


    @Test
    public void testGetCapabilitiesForVersion_emptyCapabilitiesOnUnknownVersion() {
        Optional<Set<String>> capabilities = this.getDefaultRouter().getCapabilitiesForVersion(UNKNOWN_VERSION);
        assertFalse(capabilities.isPresent());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetCapabilitiesForVersion_throwsOnNullVersion() {
        this.getDefaultRouter().getCapabilitiesForVersion(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetCapabilitiesForVersionAndAction_throwsOnNullVersion() {
        this.getDefaultRouter().getCapabilitiesForVersionAndActionName(null, DEFAULT_ACTION_NAME);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetCapabilitiesForVersionAndAction_throwsOnNullAction() {
        this.getDefaultRouter().getCapabilitiesForVersionAndActionName(DEFAULT_VERSION, null);
    }

    @Test
    public void testGetCapabilitiesForVersionAndAction_ShouldReturnEmptyOptional_IfVersionIsNotConfigured() {
        Optional<Set<String>> result = this.getDefaultRouter()
                .getCapabilitiesForVersionAndActionName(DEFAULT_VERSION + 1, DEFAULT_ACTION_NAME);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetCapabilitiesForVersionAndAction_ShouldReturnEmptyOptional_IfActionForVersionIsNotConfigured() {
        String otherActionName = DEFAULT_ACTION_NAME + 1;
        Optional<Set<String>> result = this.getDefaultRouter()
                .getCapabilitiesForVersionAndActionName(DEFAULT_VERSION, otherActionName);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetCapabilitiesForVersionAndAction_ShouldReturnEmptySet_IfNoCapabilitiesConfiguredForVersionAndAction() {
        Optional<Set<String>> result = this.getRouter(DEFAULT_VERSION, DEFAULT_ACTION)
                .getCapabilitiesForVersionAndActionName(DEFAULT_VERSION, DEFAULT_ACTION_NAME);
        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void testGetCapabilitiesForVersionAndAction_ShouldReturnCapabilities_IfCapabilitiesConfiguredForVersionAndAction() {
        Set<String> capabilities = new HashSet<>();
        capabilities.add("CAP1");
        capabilities.add("CAP2");

        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities(capabilities)
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();

        Optional<Set<String>> result = router.getCapabilitiesForVersionAndActionName(DEFAULT_VERSION, DEFAULT_ACTION_NAME);
        assertTrue(result.isPresent());
        assertEquals(result.get(), capabilities);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsVersionDefined_throwsOnNullVersion() {
        Router router = this.getDefaultRouter();
        router.isVersionDefined(null);
    }

    @Test
    public void testIsVersionDefined_trueIfDefined() {
        Router router = this.getDefaultRouter();
        assertTrue(router.isVersionDefined(DEFAULT_VERSION));
    }

    @Test
    public void testIsCapabilityDefined_trueIfDefined() {
        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities(DEFAULT_CAPABILITY)
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();

        boolean isDefined = router.isCapabilityDefined(DEFAULT_VERSION, DEFAULT_CAPABILITY);
        assertTrue(isDefined);
    }

    @Test
    public void testIsCapabilityDefined_falseOnUnknownVersion() {
        boolean isDefined = this.getDefaultRouter().isCapabilityDefined(UNKNOWN_VERSION, DEFAULT_CAPABILITY);
        assertFalse(isDefined);
    }

    @Test
    public void testIsCapabilityDefined_falseOnUnknownAction() {
        boolean isDefined = this.getDefaultRouter().isCapabilityDefined(DEFAULT_VERSION, UNKNOWN_CAPABILITY);
        assertFalse(isDefined);
    }

    private Router getDefaultRouter() {
        return this.getRouter(DEFAULT_VERSION, DEFAULT_ACTION);
    }

    private Router getRouter(String version, Action action) {
        return this.getRouter(version, action, (ctx, chain) -> {
        });
    }

    private Router getRouter(String version, Action action, Middleware middleware) {
        return new DefaultRouter.Builder()
                .version(version)
                .supportsAction(action)
                .withCapabilities()
                .using(middleware)
                .versionComplete()
                .buildRouter();
    }
}