// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing;

import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.routing.constants.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class implements the {@link Router} interface.
 * <p>
 * In addition, it provides a {@link DefaultRouter.Builder} to build new RoutingConfiguration.
 */
public final class DefaultRouter implements Router {

    private static final String VERSION_NULL_ERROR_MESSAGE = "The version must not be null";
    private static final String ACTION_NAME_NULL_ERROR_MESSAGE = "The action name must not be null";

    private final Map<String, VersionConfiguration> routes;

    private final String highestSupportedVersion;

    private DefaultRouter(Map<String, VersionConfiguration> routes, String highestSupportedVersion) {
        this.routes = routes;
        this.highestSupportedVersion = highestSupportedVersion;
    }

    @Override
    public Optional<Middleware> findMiddleware(String version, String actionRequestParam) {
        if (version == null) {
            throw new IllegalArgumentException(VERSION_NULL_ERROR_MESSAGE);
        }
        if (actionRequestParam == null) {
            throw new IllegalArgumentException("The action must not be null");
        }

        VersionConfiguration versionConfiguration = this.routes.get(version);

        if (versionConfiguration == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(versionConfiguration.findMiddleware(actionRequestParam));
    }

    @Override
    public String getVersion(String version) {
        Collection<String> versions = this.getVersions();
        if (versions.contains(version)) {
            return version;
        }
        return this.highestSupportedVersion;
    }

    @Override
    public Set<String> getVersions() {
        return this.routes.keySet();
    }

    @Override
    public Optional<Set<Action>> getActionsForVersion(String version) {
        if (version == null) {
            throw new IllegalArgumentException(VERSION_NULL_ERROR_MESSAGE);
        }

        VersionConfiguration versionConfiguration = this.routes.get(version);

        if (versionConfiguration == null) {
            return Optional.empty();
        }

        return Optional.of(versionConfiguration.getActions().keySet());
    }

    @Override
    public Optional<Set<String>> getCapabilitiesForVersion(String version) {
        if (version == null) {
            throw new IllegalArgumentException(VERSION_NULL_ERROR_MESSAGE);
        }

        VersionConfiguration versionConfiguration = this.routes.get(version);

        if (versionConfiguration == null) {
            return Optional.empty();
        }

        return Optional.of(versionConfiguration.getCapabilities());
    }

    @Override
    public Optional<Set<String>> getCapabilitiesForVersionAndActionName(String version, String actionName) {
        if (version == null) {
            throw new IllegalArgumentException(VERSION_NULL_ERROR_MESSAGE);
        }
        if (actionName == null) {
            throw new IllegalArgumentException(ACTION_NAME_NULL_ERROR_MESSAGE);
        }

        VersionConfiguration versionConfiguration = this.routes.get(version);
        if (versionConfiguration == null) {
            return Optional.empty();
        }

        Map<Action, ActionConfiguration> actionConfigurations = versionConfiguration.getActions();
        if (actionConfigurations == null) {
            return Optional.empty();
        }

        for (Map.Entry<Action, ActionConfiguration> entry : actionConfigurations.entrySet()) {
            Action action = entry.getKey();
            if (action.getName() != null && action.getName().equals(actionName)) {
                ActionConfiguration actionConfiguration = entry.getValue();
                return Optional.ofNullable(actionConfiguration.getCapabilitites());
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isVersionDefined(String version) {
        if (version == null) {
            throw new IllegalArgumentException(VERSION_NULL_ERROR_MESSAGE);
        }
        return this.routes.containsKey(version);
    }

    @Override
    public boolean isCapabilityDefined(String version, String capability) {
        Set<String> capabilities = this.getCapabilitiesForVersion(version).orElse(Collections.emptySet());
        return capabilities.contains(capability);
    }

    /**
     * Use this builder to create a {@link DefaultRouter}.
     * <p>
     * Example usage:
     * <pre>
     *     DefaultRouter router = new DefaultRouter.Builder()
     *          .version("2017-10")
     *              .supportsAction("action1")
     *                  .withCapabilities("cap1", "cap2")
     *                  .using(middleware1)
     *              .and()
     *              .supportsAction("action2")
     *                  .withCapabilities("cap3", "cap4")
     *                  .using(middleware2)
     *              .versionComplete()
     *          .version("2018-10")
     *              .supportsAction("action3")
     *                  .withCapabilities("cap5", "cap6")
     *                  .using(middleware3)
     *              .and()
     *              .supportsAction("action4")
     *                  .withCapabilities("cap7")
     *                  .using(middleware4)
     *              .versionComplete()
     *          .buildRouter();
     * </pre>
     */
    public static final class Builder {

        private final Map<String, VersionConfiguration> versionConfigurations = new HashMap<>();

        private final Supplier<Router> routerBuilder = () -> {
            List<String> versions = new ArrayList<>(this.versionConfigurations.keySet());
            Collections.sort(versions);
            String highestVersion = versions.get(versions.size() - 1);
            return new DefaultRouter(this.versionConfigurations, highestVersion);
        };

        /**
         * Start router building by providing a version.
         *
         * @param version the (first) <code>version</code> to configure
         *                <code>actions</code> for. Other versions with
         *                assigned actions may be configured using the
         *                builder.
         * @return a {@link RoutingBuilder} to configure actions
         * for the given <code>version</code>
         */
        public RoutingBuilder version(String version) {
            if (version == null) {
                throw new IllegalArgumentException(VERSION_NULL_ERROR_MESSAGE);
            }
            return new RoutingBuilder(
                    this,
                    this.versionHandler(version),
                    this.routerBuilder);
        }

        private Consumer<VersionConfiguration> versionHandler(String version) {
            return versionConfiguration -> this.versionConfigurations.put(version, versionConfiguration);
        }

    }
}
