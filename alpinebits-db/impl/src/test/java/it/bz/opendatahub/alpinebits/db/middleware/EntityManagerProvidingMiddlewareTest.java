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
import it.bz.opendatahub.alpinebits.db.entity.TestUserEntity;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Test cases for {@link EntityManagerProvidingMiddleware} class.
 */
public class EntityManagerProvidingMiddlewareTest {

    @Test
    public void testHandleContext() {
        Middleware middleware = new EntityManagerProvidingMiddleware();

        Context ctx = new SimpleContext();
        middleware.handleContext(ctx, () -> {
            EntityManager em = ctx.getOrThrow(PersistenceContextKey.ENTITY_MANAGER);
            TestUserEntity user = new TestUserEntity();
            user.setName("user1");
            user.setAge(23);

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            tx.commit();
        });

        assertNotNull(ctx.getOrThrow(PersistenceContextKey.ENTITY_MANAGER_FACTORY));
        assertNotNull(ctx.getOrThrow(PersistenceContextKey.ENTITY_MANAGER));

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("default")) {
            EntityManager em = emf.createEntityManager();
            TestUserEntity user = em.find(TestUserEntity.class, "user1");
            assertNotNull(user);
        }
    }
}