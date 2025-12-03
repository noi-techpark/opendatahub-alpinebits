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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An AlpineBits version configuration, containing
 * information about the actions defined.
 */
public class VersionConfiguration {

    private final Map<Action, ActionConfiguration> actions = new HashMap<>();
    private final Map<String, ActionConfiguration> actionsByActionRequestParam = new HashMap<>();

    public void addActionConfiguration(Action action, ActionConfiguration actionConfiguration) {
        this.actions.put(action, actionConfiguration);
        this.actionsByActionRequestParam.put(action.getRequestParameter(), actionConfiguration);
    }

    public Map<Action, ActionConfiguration> getActions() {
        return actions;
    }

    public Set<String> getCapabilities() {
        return this.actions.values().stream()
                .map(ActionConfiguration::getCapabilitites)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public Middleware findMiddleware(String actionRequestParam) {
        return this.actionsByActionRequestParam.containsKey(actionRequestParam)
                ? this.actionsByActionRequestParam.get(actionRequestParam).getMiddleware()
                : null;
    }

    @Override
    public String toString() {
        return "VersionConfiguration{" +
                "actions=" + actions +
                '}';
    }
}
