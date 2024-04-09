package com.mjc.school.repository.implementation;


import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.BaseEntity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class AbstractDBRepository<T extends BaseEntity<K>, K> implements BaseRepository<T, K> {
    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;
    private final Class<K> idClass;

    abstract void update(T prevState, T nextState);

    protected AbstractDBRepository() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
        idClass = (Class<K>) type.getActualTypeArguments()[1];
    }

    @Override
    public List<T> readAll(int page, int size, String sortBy) {
        String[] sort = sortBy.split(",");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        CriteriaQuery<T> select = criteriaQuery.select(root);
        CriteriaQuery<T> ordered;

        if (sort[1].equalsIgnoreCase("ASC") && sort[1] != null) {
            ordered = select.orderBy(criteriaBuilder.asc(root.get(sort[0])));
        } else {
            ordered = select.orderBy(criteriaBuilder.desc(root.get(sort[0])));
        }
        TypedQuery<T> result = entityManager.createQuery(ordered).setFirstResult(page * size).setMaxResults(size);
        return result.getResultList();
    }


    @Override
    public Optional<T> readById(K id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }


    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return readById(entity.getId()).map(a -> {
            update(a, entity);
            T updated = entityManager.merge(a);
            entityManager.flush();
            return updated;
        }).orElse(null);
    }

    @Override
    public boolean deleteById(K id) {
        if (id != null) {
            T exsist = entityManager.getReference(this.entityClass, id);
            try {
                entityManager.remove(exsist);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    @Override
    public boolean existById(K id) {
        T exist = entityManager.getReference(this.entityClass, id);
        return exist != null;

    }

    @Override
    public T getReference(K id) {
        return entityManager.getReference(this.entityClass, id);
    }
}






