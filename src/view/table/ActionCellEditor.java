package view.table;

import dao.NhanVienDAO;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel panel;
    private JButton btnView, btnEdit, btnDelete;
    private JTable table;
    private MainForm mainForm;

    public ActionCellEditor(JTable table, MainForm mainForm) {
        this.table = table;
        this.mainForm = mainForm;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        btnView = new JButton("👁");
        btnEdit = new JButton("✏");
        btnDelete = new JButton("🗑");

        panel.add(btnView);
        panel.add(btnEdit);
        panel.add(btnDelete);

        // EVENTS
        btnView.addActionListener(e -> open(Mode.VIEW));
        btnEdit.addActionListener(e -> open(Mode.EDIT));
        btnDelete.addActionListener(e -> delete());
    }

    private void open(Mode mode) {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int maNV = (int) table.getModel().getValueAt(row, 0);

        mainForm.setContentPanel(
                new NhanVienDetailPanel(mainForm, maNV, mode)
        );

        fireEditingStopped();
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int maNV = (int) table.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(
                panel,
                "Xóa nhân viên này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            NhanVienDAO.delete(maNV);
            ((DefaultTableModel) table.getModel()).removeRow(row);
        }

        fireEditingStopped();
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
