/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Controller;

import POS.models.Category;
import POS.models.PaymentMethod;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author erlanggafauzan
 */
public class PaymentMethodController  extends Controller {

    public List<PaymentMethod> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<PaymentMethod> query = session.createQuery("from PaymentMethod", PaymentMethod.class);
            List<PaymentMethod> all = query.getResultList();
            return all;
        }
    }
}
