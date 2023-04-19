/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package POS;

import POS.Controller.*;
import POS.models.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author erlanggafauzan
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public CategoryController categoryController = new CategoryController();
    public ProductController productController = new ProductController();
    public PaymentMethodController paymentMethodController = new PaymentMethodController();
    public UserController userController = new UserController();

    public OrderController orderController = new OrderController();

    public NumberFormat formatter = NumberFormat.getInstance();
    public CustomerSelect cs;

    public List<OrderItem> currentOrders = new ArrayList<OrderItem>();
    public Customer selectedCustomer;
    ButtonGroup paymentGroup = new ButtonGroup();
    public PaymentMethod selectedPaymentMethod;

    public User activeUser;

    public Dashboard() {
        Setup();
    }

    public void Setup() {
        initComponents();
        Initialization();
        LoadCategory();
        RenderOrderItem();
        LoadPayment();
        SetupUser();
    }

    public Dashboard(User user) {
        activeUser = user;
        Setup();
    }

    public void SetupUser() {
        if (activeUser == null) {
            List<User> users = userController.getUsers();
            if (!users.isEmpty()) {
                activeUser = users.get(0);
            }
        }
        userBtn.setText(activeUser.getUsername());
    }

    public void LoadPayment() {
        paymentOption.removeAll();
        paymentOption.revalidate();
        paymentOption.repaint();
        paymentOption.setLayout(new GridLayout(2, 3));
        List<PaymentMethod> methods = paymentMethodController.getAll();
        System.out.print("Methods " + methods.size());
        for (int i = 0; i < methods.size(); i++) {
            PaymentMethod method = methods.get(i);
            JRadioButton radio = new JRadioButton(method.getName());
            radio.addActionListener((ActionEvent e) -> {
                // code to handle radio button selection
                selectedPaymentMethod = method;
            });
            paymentGroup.add(radio);
            paymentOption.add(radio);
        }
        for (int i = 0; i < 6 - methods.size(); i++) {
            JLabel radio = new JLabel("");
            paymentOption.add(radio);
        }
    }

    public void ActionOrderItem(int id) {
        String input = JOptionPane.showInputDialog(this, "Update Jumlah", currentOrders.get(id).getQuantity());
        int value = Integer.parseInt(input);
        if (value == 0) {
            currentOrders.remove(id);
        } else {
            currentOrders.get(id).setQuantity(value);
        }
        RenderOrderItem();
    }

    public void RenderOrderItem() {
        Double totalTrx = 0.0;
        orderItemList.removeAll();
        orderItemList.revalidate();
        orderItemList.repaint();

        orderItemList.setLayout(new GridLayout((currentOrders.size() < 12 ? 12 : currentOrders.size()), 1, 5, 5));
        for (int i = 0; i < currentOrders.size(); i++) {
            OrderItem currentOrder = currentOrders.get(i);
            Double trxAmount = (currentOrder.getQuantity() * currentOrder.getProduct().getPrice());
            JButton order = new JButton(
                    currentOrder.getProduct().getName() + " - " + currentOrder.getQuantity().toString() + "x" + ":Rp." + (formatter.format(trxAmount))
            );
            final int itemId = i;
            order.addActionListener((ActionEvent e) -> {
                // add code here to handle button click event
                ActionOrderItem(itemId);
            });
            totalTrx += trxAmount;
            orderItemList.add(order);
        }
        totalTrxLabel.setText("Rp" + formatter.format(totalTrx));
        if (currentOrders.size() < 12) {
            for (int i = 0; i < 12 - currentOrders.size(); i++) {
                JLabel btn = new JLabel("");

                orderItemList.add(btn);

            }
        }

    }

    public void ShowProduct(Product product, int index) {
        String input = JOptionPane.showInputDialog(this, "Jumlah terjual", 0);
        int value = Integer.parseInt(input);
        if (value < 0) {
            return;
        }
        OrderItem item = new OrderItem();
        item.setQuantity(value);
        item.setProduct(product);
        item.setPrice(product.getPrice());
        currentOrders.add(item);
        RenderOrderItem();

    }

    public void LoadProduct(long category_id) {
        productsPane.removeAll();
        productsPane.revalidate();
        productsPane.repaint();
        List<Product> prods = productController.getProducts(category_id);
        productsPane.setLayout(new GridLayout(4, 3, 10, 10));

        for (int i = 0; i < prods.size(); i++) {
            Product prod = prods.get(i);
            ImageIcon icon = new ImageIcon();
            if (prod.getImage_url().contains("base64")) {
                byte[] imageBytes = Base64.getDecoder().decode(prod.getImage_url().split(",")[1]);
                icon = new ImageIcon(imageBytes);
            } else if (prod.getImage_url().contains("http")) {
                icon = new ImageIcon(prod.getImage_url());
            }

            JButton btn = new JButton(prod.getName(), new ImageIcon(icon.getImage().getScaledInstance(64, 64, 64)));
            btn.addActionListener((ActionEvent e) -> {
                // add code here to handle button click event
                ShowProduct(prod, 0);
            });
            productsPane.add(btn);

        }
        for (int i = 0; i < 12 - prods.size(); i++) {
            JLabel btn = new JLabel("");

            productsPane.add(btn);

        }
    }

    public void SelectCustomer() {
        cs = new CustomerSelect();
        cs.setTitle("Pilih Customer");
        Consumer<Customer> callback = (Customer customer) -> {
            // do something with the selected customer
            selectCustomer.setText(customer.getName());
            selectedCustomer = customer;
            cs.setVisible(false);
            cs = null;
        };
        cs.setVisible(true);
        cs.setOnSelected(callback);
    }

    public void LoadCategory() {

        List<Category> categories = categoryController.getCategories();
        categoriesPane.removeAll();
        for (int i = 0; i < categories.size(); i++) {
            ImageIcon icon = new ImageIcon();

            Category cate = categories.get(i);
            if (cate.getImage_url().contains("base64")) {
                byte[] imageBytes = Base64.getDecoder().decode(cate.getImage_url().split(",")[1]);
                icon = new ImageIcon(imageBytes);
            } else if (cate.getImage_url().contains("http")) {
                icon = new ImageIcon(cate.getImage_url());
            }

            JButton btn = new JButton(cate.getName(), new ImageIcon(icon.getImage().getScaledInstance(64, 64, 64)));
            btn.addActionListener((ActionEvent e) -> {
                // add code here to handle button click event

                LoadProduct(cate.getId());
            });
            categoriesPane.add(btn);
        }
        for (int i = 0; i < 6 - categories.size(); i++) {
            JButton btn = new JButton("");
            categoriesPane.add(btn);
        }
        if (!categories.isEmpty()) {
            LoadProduct(categories.get(0).getId());
        }
    }

    public void Initialization() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Timer timer = new Timer(1000, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // get the current time
        LocalTime currentTime = LocalTime.now();

        // format the time as "hour:minutes:seconds"
        String timeText = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // set the label text
        waktu.setText(timeText);
    }
});

// start the timer
timer.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        checkout = new javax.swing.JButton();
        categoryScroll = new javax.swing.JScrollPane();
        categoriesPane = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        selectCustomer = new javax.swing.JButton();
        productsPane = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderItemList = new javax.swing.JPanel();
        totalTrxLabel = new javax.swing.JLabel();
        paymentOption = new javax.swing.JPanel();
        navbar = new javax.swing.JPanel();
        userBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        checkout.setBackground(new java.awt.Color(34, 40, 49));
        checkout.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        checkout.setForeground(new java.awt.Color(255, 255, 255));
        checkout.setText("Checkout");
        checkout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkoutActionPerformed(evt);
            }
        });

        categoryScroll.setBackground(new java.awt.Color(255, 255, 255));

        categoriesPane.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        jButton2.setText("jButton2");
        categoriesPane.add(jButton2);

        jButton3.setText("jButton3");
        categoriesPane.add(jButton3);

        categoryScroll.setViewportView(categoriesPane);

        selectCustomer.setBackground(new java.awt.Color(57, 62, 70));
        selectCustomer.setForeground(new java.awt.Color(255, 255, 255));
        selectCustomer.setText("Select Customer");
        selectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCustomerActionPerformed(evt);
            }
        });

        productsPane.setLayout(new java.awt.GridLayout(2, 2, 10, 10));

        jButton4.setText("jButton4");
        productsPane.add(jButton4);

        jButton5.setText("jButton4");
        productsPane.add(jButton5);

        jButton6.setText("jButton4");
        productsPane.add(jButton6);

        jButton7.setText("jButton4");
        productsPane.add(jButton7);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        orderItemList.setBackground(new java.awt.Color(255, 255, 255));
        orderItemList.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane2.setViewportView(orderItemList);

        totalTrxLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        totalTrxLabel.setText("Rp.0");

        paymentOption.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout paymentOptionLayout = new javax.swing.GroupLayout(paymentOption);
        paymentOption.setLayout(paymentOptionLayout);
        paymentOptionLayout.setHorizontalGroup(
            paymentOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        paymentOptionLayout.setVerticalGroup(
            paymentOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        navbar.setBackground(new java.awt.Color(57, 62, 70));

        userBtn.setBackground(new java.awt.Color(0, 173, 181));
        userBtn.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        userBtn.setForeground(new java.awt.Color(255, 255, 255));
        userBtn.setText("jButton1");

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ERLAPOS");

        waktu.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        waktu.setForeground(new java.awt.Color(255, 255, 255));
        waktu.setText("00:00:00");

        javax.swing.GroupLayout navbarLayout = new javax.swing.GroupLayout(navbar);
        navbar.setLayout(navbarLayout);
        navbarLayout.setHorizontalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navbarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(waktu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 685, Short.MAX_VALUE)
                .addComponent(userBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        navbarLayout.setVerticalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(waktu)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navbar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(productsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categoryScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(totalTrxLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(paymentOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)
                            .addComponent(checkout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(selectCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addComponent(paymentOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(totalTrxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkout, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(categoryScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(52, 52, 52))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkoutActionPerformed
        // TODO add your handling code here:
        Double totalTransaksi = 0.0;

        for (var order : currentOrders) {
            totalTransaksi = order.getQuantity() * order.getProduct().getPrice();
        }
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Pilih customer terlebih dahulu", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedPaymentMethod == null) {
            JOptionPane.showMessageDialog(this, "Pilih Methode Pembayaran", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (totalTransaksi == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada transaksi", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String message = "Detail Transaksi \n"
                + "---------------------\n";
        for (var order : currentOrders) {
            message += order.getProduct().getName() + "  -   " + order.getQuantity() + "x Rp." + formatter.format(order.getProduct().getPrice()) + "\n";
        }
        message += "--------------------- \n"
                + "Pembayaran: " + selectedPaymentMethod.getName() 
                 + "\nTotal: Rp." + formatter.format(totalTransaksi);

        var result = JOptionPane.showConfirmDialog(this, message, "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String input = JOptionPane.showInputDialog("Masukan jumlah uang yang di terima (Rp." + formatter.format(totalTransaksi) + ")", 0);
        Double value = Double.parseDouble(input);

        if (value < totalTransaksi) {
            JOptionPane.showMessageDialog(this, "Jumlah Kurang dari nilai transaksi", "Invalid", JOptionPane.ERROR_MESSAGE);
            checkoutActionPerformed(evt);
            return;
        }
        Double remaining = value - totalTransaksi;
        if (remaining > 0) {
            JOptionPane.showMessageDialog(this, "Kembalian Rp." + formatter.format(remaining), "Kembalian", JOptionPane.INFORMATION_MESSAGE);

        }

        Order order = new Order();
        order.setCustomer(selectedCustomer);
        order.setOrderItems(currentOrders.stream().collect(Collectors.toSet()));
        order.setTotal(totalTransaksi);
        order.setUser(activeUser);
        order.setOrderDate(new Date());
        orderController.SaveOrder(order);

        JOptionPane.showMessageDialog(this, "Transkasi Berhasil disimpan", "Berhasil", JOptionPane.INFORMATION_MESSAGE);

        currentOrders.clear();
        RenderOrderItem();


    }//GEN-LAST:event_checkoutActionPerformed

    private void selectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCustomerActionPerformed
        // TODO add your handling code here:
        SelectCustomer();
    }//GEN-LAST:event_selectCustomerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            UIManager.setLookAndFeel(new FlatLightLaf());

            //</editor-fold>
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Dashboard().setVisible(true);
                }
            });

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        // add some components to the panel
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel categoriesPane;
    private javax.swing.JScrollPane categoryScroll;
    private javax.swing.JButton checkout;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel navbar;
    private javax.swing.JPanel orderItemList;
    private javax.swing.JPanel paymentOption;
    private javax.swing.JPanel productsPane;
    private javax.swing.JButton selectCustomer;
    private javax.swing.JLabel totalTrxLabel;
    private javax.swing.JButton userBtn;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
