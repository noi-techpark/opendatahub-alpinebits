// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.db.middleware;

import it.bz.opendatahub.alpinebits.db.PersistenceContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

/**
 * This middleware adds an {@link EntityManager} and its related
 * {@link EntityManagerFactory} to the context, using the
 * {@link PersistenceContextKey#ENTITY_MANAGER} and
 * {@link PersistenceContextKey#ENTITY_MANAGER_FACTORY} keys.
 * <p>
 * A new EntityManager will be created on each invocation of the
 * {@link Middleware#handleContext(Context, MiddlewareChain)}
 * method. This EntityManager will be closed (if it is not already
 * closed) after the invocation of the request-phase middlewares
 * returns. At this point, any open transaction will be rolled
 * back.
 */
public class EntityManagerProvidingMiddleware implements Middleware {

    private final EntityManagerFactory entityManagerFactory;

    public EntityManagerProvidingMiddleware() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        EntityManager em = this.entityManagerFactory.createEntityManager();

        ctx.put(PersistenceContextKey.ENTITY_MANAGER_FACTORY, this.entityManagerFactory);
        ctx.put(PersistenceContextKey.ENTITY_MANAGER, em);

        // Wrap execution into a try-finally block to ensure the current
        // EntityManager is closed after all request-phase middlewares
        // were invoked
        try {
            chain.next();
        } finally {
            if (em.isOpen()) {
                EntityTransaction tx = em.getTransaction();
                if (tx.isActive()) {
                    tx.rollback();
                }
                em.close();
            }
        }
    }

}
