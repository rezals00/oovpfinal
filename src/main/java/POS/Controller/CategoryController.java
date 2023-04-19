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
}
