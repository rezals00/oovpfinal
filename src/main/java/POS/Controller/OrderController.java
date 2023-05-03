/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Controller;

import POS.models.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author erlanggafauzan
 */
public class OrderController extends Controller {

    public void SaveOrder(Order order) {
        // fill the code to save
        
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Order> getOrders() {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery("from Order", Order.class);
            List<Order> orders = query.getResultList();
            return orders;
        }
    }
    public List<Order> getOrders(String search) {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery("from Order  where Order.customer.name like :search", Order.class);
            query.setParameter("search", "%" +  search +"%");
            List<Order> orders = query.getResultList();
            return orders;
        }
    }
}
