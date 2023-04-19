/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Controller;

import POS.models.Category;
import POS.models.Customer;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author erlanggafauzan
 */
public class CustomerController extends Controller {

    public List<Customer> getCustomers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("from Customer", Customer.class);
            List<Customer> customers = query.getResultList();
            return customers;
        }
    }
}
