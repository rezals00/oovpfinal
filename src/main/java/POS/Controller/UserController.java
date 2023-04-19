/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Controller;

/**
 *
 * @author erlanggafauzan
 */

import POS.models.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.query.Query;

class HashUtil {
    public static String sha1(String input) {
        return DigestUtils.sha1Hex(input);
    }
}
public class UserController extends Controller {
    public List<User> getUsers() {
         try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            List<User> users = query.getResultList();
            return users;
        }
    }

    public User login(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            User user = (User) session.createQuery("FROM User WHERE username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            if (user != null) {
                String hashedPassword = HashUtil.sha1(password);
                if (user.getPassword().equals(hashedPassword)) {
                    return user;
                }
            }
        }
        return null;
    }
}

