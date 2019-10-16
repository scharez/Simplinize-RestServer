package repository;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public abstract class AbstractRepository<T> {

    private Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    //EntityManager em = Persistence.createEntityManagerFactory("SimplinizePU").createEntityManager();





}
