package project.pictaround.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Category;

import java.util.List;

@Repository
@Transactional
public class CategoryRepository {

    @PersistenceContext
    private final EntityManager em;

    public CategoryRepository(EntityManager em) {
        this.em = em;
    }

    public Category save(Category category) {
        em.persist(category);
        return category;
    };

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public Category findByName(String name) {
        return em.find(Category.class, name);
    }
}
