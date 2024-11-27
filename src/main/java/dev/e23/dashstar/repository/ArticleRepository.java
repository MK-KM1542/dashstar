package dev.e23.dashstar.repository;

import dev.e23.dashstar.model.Article;
import dev.e23.dashstar.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.List;

@ApplicationScoped  // 声明这是一个应用范围的 Bean，需要的地方可以使用 @Inject 注入
public class ArticleRepository {

    public List<Article> findAll(int page ,int size) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        List<Article> articles = null;
        try {
            em.getTransaction().begin();
            int start = (page - 1) * size;//添加分页功能，计算查询的起始位置
            articles = em
                    .createQuery("SELECT a FROM Article a", Article.class)
                    .setFirstResult(start)//添加分页功能
                    .setMaxResults(size)//添加分页功能
                    .getResultList();    //添加分页功能
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new RuntimeException("articles_not_found", e);
        } finally {
            em.close();
        }
        return articles;
    }

    public Article findByID(Integer id) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        Article article = null;
        try {
            em.getTransaction().begin();  // 开始事务
            article = em
                    .createQuery("SELECT a FROM Article a WHERE a.id = :id", Article.class)
                    .setParameter("id", id)
                    .getSingleResult();  // 查询指定 ID 的文章
            em.getTransaction().commit();  // 提交事务
        } catch (PersistenceException e) {
            em.getTransaction().rollback();  // 当出了异常时回滚事务
            throw new RuntimeException("article_not_found", e);
        } finally {
            em.close();  // 关闭 EntityManager
        }
        return article;
    }

    public void create(Article article) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();  // 开始事务
            em.persist(article);  // 保存文章
            em.getTransaction().commit();  // 提交事务
        } catch (PersistenceException e) {
            em.getTransaction().rollback();  // 当出了异常时回滚事务
            throw new RuntimeException("", e);
        } finally {
            em.close();  // 关闭 EntityManager
        }
    }

    public void update(Article article) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();  // 开始事务
            Article existingArticle = em.find(Article.class, article.getId());  // 查找已经存在的 article
            try {
                HibernateUtil.copyNonNullProperties(article, existingArticle);  // 手动合并需要更改的字段
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            em.merge(existingArticle);  // 更新文章
            em.getTransaction().commit();  // 提交事务
        } catch (PersistenceException e) {
            em.getTransaction().rollback();  // 当出了异常时回滚事务
            throw new RuntimeException("", e);
        } finally {
            em.close();  // 关闭 EntityManager
        }
    }
    public long countAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        return em.createQuery("SELECT COUNT(a) FROM Article a", Long.class).getSingleResult();
    }
}
