/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Controller;

import POS.models.Category;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author erlanggafauzan
 */
public class CategoryController extends Controller {

    public List<Category> getCategories() {
        try (Session session = sessionFactory.openSession()) {
            Query<Category> query = session.createQuery("from Category", Category.class);
            List<Category> categories = query.getResultList();
            return categories;
        }
    }

    public Category Save(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(category);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }

    public List<Category> getCategories(String search) {
        try (Session session = sessionFactory.openSession()) {
            Query<Category> query = session.createQuery("from Category where name like :search", Category.class);
            query.setParameter("search", "%" + search + "%");
            List<Category> categories = query.getResultList();
            return categories;
        }
    }
}
