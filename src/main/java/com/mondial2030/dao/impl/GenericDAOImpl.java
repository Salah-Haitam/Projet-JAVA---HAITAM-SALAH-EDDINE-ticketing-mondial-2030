package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.GenericDAO;
import com.mondial2030.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation générique du DAO avec Hibernate.
 * Fournit les opérations CRUD de base.
 * 
 * @param <T> Type de l'entité
 * @param <ID> Type de l'identifiant
 * @author Mondial 2030 Team
 * @version 1.0
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {
    
    protected final Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
    
    /**
     * Obtient une session Hibernate
     */
    protected Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }
    
    /**
     * Obtient la session courante
     */
    protected Session getCurrentSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }
    
    @Override
    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la sauvegarde: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la mise à jour: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(ID id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la suppression: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<T> findById(ID id) {
        try (Session session = getSession()) {
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<T> findAll() {
        try (Session session = getSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            Query<T> query = session.createQuery(hql, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les entités: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        try (Session session = getSession()) {
            String hql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du comptage: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
    
    /**
     * Exécute une requête HQL avec paramètres
     */
    protected List<T> executeQuery(String hql, Object... params) {
        try (Session session = getSession()) {
            Query<T> query = session.createQuery(hql, entityClass);
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter((String) params[i], params[i + 1]);
            }
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de la requête: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exécute une requête HQL retournant un résultat unique
     */
    protected Optional<T> executeSingleResultQuery(String hql, Object... params) {
        try (Session session = getSession()) {
            Query<T> query = session.createQuery(hql, entityClass);
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter((String) params[i], params[i + 1]);
            }
            query.setMaxResults(1);
            List<T> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de la requête: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exécute une mise à jour HQL
     */
    protected int executeUpdate(String hql, Object... params) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            Query<?> query = session.createQuery(hql);
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter((String) params[i], params[i + 1]);
            }
            int result = query.executeUpdate();
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la mise à jour: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exécute une requête de comptage
     */
    protected long executeCountQuery(String hql, Object... params) {
        try (Session session = getSession()) {
            Query<Long> query = session.createQuery(hql, Long.class);
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter((String) params[i], params[i + 1]);
            }
            Long result = query.getSingleResult();
            return result != null ? result : 0L;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du comptage: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exécute une requête retournant un Double
     */
    protected Double executeDoubleQuery(String hql, Object... params) {
        try (Session session = getSession()) {
            Query<Double> query = session.createQuery(hql, Double.class);
            for (int i = 0; i < params.length; i += 2) {
                query.setParameter((String) params[i], params[i + 1]);
            }
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de la requête: " + e.getMessage(), e);
        }
    }
}
