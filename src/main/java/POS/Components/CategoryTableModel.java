/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS.Components;

/**
 *
 * @author erlanggafauzan
 */
import POS.models.Category;
import java.util.Base64;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class CategoryTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Name", "Image"};
    private final List<Category> categories;

    public CategoryTableModel(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getRowCount() {
        return categories.size();
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
        Category category = categories.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return category.getId();
            case 1:
                return category.getName();
            case 2:
                return category.getImage_url();
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
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    public Category getCategory(int rowIndex) {
        return categories.get(rowIndex);
    }
}
