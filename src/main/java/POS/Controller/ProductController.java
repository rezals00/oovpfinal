/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Controller;

import POS.models.Category;
import POS.models.Product;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author erlanggafauzan
 */
public class ProductController extends Controller {

    public List<Product> getProducts(long categoryId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery("from Product p WHERE p.category.id = :categoryId", Product.class);
            System.out.print(categoryId);
            query.setParameter("categoryId", categoryId);
            
            List<Product> products = query.getResultList();
                        System.out.print("Total Products" + products.size());

            return products;
        }
    }
    public List<Product> getProducts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery("from Product", Product.class);
            List<Product> products = query.getResultList();
            return products;
        }
    }
}
