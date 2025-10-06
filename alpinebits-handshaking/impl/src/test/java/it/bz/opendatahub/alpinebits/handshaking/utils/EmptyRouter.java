// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking.utils;

import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.routing.Router;
import it.bz.opendatahub.alpinebits.routing.constants.Action;

import java.util.Optional;
import java.util.Set;

/**
 * Simple {@link Router} implementation that returns null or empty lists,
 * for testing purpose only.
 */
public class EmptyRouter implements Router {
    @Override
    public Optional<Middleware> findMiddleware(String version, String action) {
        return Optional.empty();
    }

    @Override
    public String getVersion(String requestedVersion) {
        return null;
    }

    @Override
    public Set<String> getVersions() {
        return null;
    }

    @Override
    public Optional<Set<Action>> getActionsForVersion(String version) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<String>> getCapabilitiesForVersion(String version) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<String>> getCapabilitiesForVersionAndActionName(String version, String actionName) {
        return Optional.empty();
    }

    @Override
    public boolean isVersionDefined(String version) {
        return false;
    }

    @Override
    public boolean isCapabilityDefined(String version, String capability) {
        return false;
    }
}
