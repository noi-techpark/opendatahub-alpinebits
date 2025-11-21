// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.db;

import it.bz.opendatahub.alpinebits.middleware.Key;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * This class contains key definitions, that may be used e.g. in a middleware context.
 */
public final class PersistenceContextKey {

    /**
     * Context key for {@link EntityManagerFactory}.
     */
    public static final Key<EntityManagerFactory> ENTITY_MANAGER_FACTORY = Key.key(
            "persistence.db.entitiymanagerfactory", EntityManagerFactory.class
    );

    /**
     * Context key for {@link EntityManager}.
     */
    public static final Key<EntityManager> ENTITY_MANAGER = Key.key(
            "persistence.db.entitiymanager", EntityManager.class
    );

    private PersistenceContextKey() {
        // Empty
    }

}
