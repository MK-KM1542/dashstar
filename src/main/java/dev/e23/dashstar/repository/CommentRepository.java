package dev.e23.dashstar.repository;

import dev.e23.dashstar.model.Comment;
import dev.e23.dashstar.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

import java.util.List;

@ApplicationScoped 
public class CommentRepository {

    public List<Comment> findByArticleId(Integer id) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        List<Comment> comments = null;
        try {
            em.getTransaction().begin(); 
            comments = em
                    .createQuery("SELECT c FROM Comment c WHERE c.articleId = :articleId", Comment.class)
                    .setParameter("articleId", id)
                    .getResultList(); 
            em.getTransaction().commit(); 
        } catch (PersistenceException e) {
            em.getTransaction().rollback(); 
            throw new RuntimeException("", e);
        } finally {
            em.close(); 
        }
        return comments;
    }

    public void delete(Comment comment) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin(); 
            em.remove(em.contains(comment) ? comment : em.merge(comment));
            em.getTransaction().commit();

        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new RuntimeException("", e);
        } finally {
            em.close();
        }
    }

    public void create(Comment comment) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin(); 
            em.persist(comment); 
            em.getTransaction().commit(); 
        } catch (PersistenceException e) {
            em.getTransaction().rollback(); 
            throw new RuntimeException("", e);
        } finally {
            em.close(); 
        }
    }

    public Comment findById(Integer id) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        Comment comment = null;
        try {
            em.getTransaction().begin();
            comment = em.find(Comment.class, id);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new RuntimeException("", e);
        } finally {
            em.close();
        }// 当出了异常时回滚事务

        return comment;
    }
}
