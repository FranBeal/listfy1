package br.com.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    // Fábrica de EntityManager para a unidade de persistência "testPostgresPU"
    private static final EntityManagerFactory emf = Persistence
            .createEntityManagerFactory("testPostgresPU");

    // Obtém uma instância de EntityManager
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Fecha a fábrica de EntityManager
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}