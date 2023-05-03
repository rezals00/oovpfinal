/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Components;

/**
 *
 * @author erlanggafauzan
 */
import POS.models.Customer;
import POS.models.Order;
import POS.models.User;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class HistoryTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Customer Name", "Total Transaksi", "Tanggal"};
    private final List<Order> orders;

    public HistoryTableModel(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int getRowCount() {
        return orders.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = orders.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return order.getId();
            case 1:
                return order.getCustomer().getName();
            case 2:
                return order.getTotal();
             case 3:
                return order.getOrderDate();
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    public Order getOrder(int rowIndex) {
        return orders.get(rowIndex);
    }
}

