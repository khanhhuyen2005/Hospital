package view;

import dao.PhongBanDAO;
import model.PhongBan;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PhongBanPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;

    public PhongBanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel row1 = new JPanel(new BorderLayout());
        row1.setOpaque(false);

        JLabel title = new JLabel("Danh sách phòng ban");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        row1.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setToolTipText("Tìm theo mã, tên phòng, mô tả, ngày thành lập...");

        JButton btnAdd = createBtn("➕ Thêm", UIStyle.SUCCESS);
        JButton btnEdit = createBtn("✏ Sửa", UIStyle.WARNING);
        JButton btnDelete = createBtn("🗑 Xóa", UIStyle.DANGER);

        actions.add(new JLabel("🔍"));
        actions.add(txtSearch);
        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);

        row1.add(actions, BorderLayout.EAST);

        header.add(row1);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "STT", "Tên phòng", "Mô tả", "Ngày thành lập"
        });

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220, 230, 255));
        table.setGridColor(UIStyle.BORDER);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(UIStyle.PRIMARY);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 36));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER));

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadData();

        btnAdd.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> editPhongBan());
        btnDelete.addActionListener(e -> deletePhongBan());

        // SEARCH REALTIME
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String keyword = txtSearch.getText().trim();
                if (keyword.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(
                            RowFilter.regexFilter(
                                    "(?i)" + Pattern.quote(keyword)
                            )
                    );
                }
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<PhongBan> list = PhongBanDAO.getAll();
        int stt = 1;
        for (PhongBan pb : list) {
            model.addRow(new Object[]{
                    stt++,
                    pb.getTenPhong(),
                    pb.getMoTa(),
                    pb.getNgayThanhLap()
            });
        }
    }

    private void showForm(PhongBan pb) {

        JTextField txtTen = new JTextField();
        JTextArea txtMoTa = new JTextArea(3, 20);
        JTextField txtNgay = new JTextField("2024-01-01");

        if (pb != null) {
            txtTen.setText(pb.getTenPhong());
            txtMoTa.setText(pb.getMoTa());
            txtNgay.setText(pb.getNgayThanhLap().toString());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Tên phòng"));
        panel.add(txtTen);
        panel.add(new JLabel("Mô tả"));
        panel.add(new JScrollPane(txtMoTa));
        panel.add(new JLabel("Ngày thành lập (yyyy-mm-dd)"));
        panel.add(txtNgay);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                pb == null ? "Thêm phòng ban" : "Sửa phòng ban",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                if (pb == null) pb = new PhongBan();

                pb.setTenPhong(txtTen.getText());
                pb.setMoTa(txtMoTa.getText());
                pb.setNgayThanhLap(Date.valueOf(txtNgay.getText()));

                if (pb.getMaPhong() == 0)
                    PhongBanDAO.insert(pb);
                else
                    PhongBanDAO.update(pb);

                loadData();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Dữ liệu không hợp lệ!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void editPhongBan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phòng ban cần sửa");
            return;
        }

        row = table.convertRowIndexToModel(row);

        PhongBan pb = new PhongBan();
        pb.setMaPhong((int) model.getValueAt(row, 0));
        pb.setTenPhong(model.getValueAt(row, 1).toString());
        pb.setMoTa(model.getValueAt(row, 2).toString());
        pb.setNgayThanhLap(Date.valueOf(model.getValueAt(row, 3).toString()));

        showForm(pb);
    }

    private void deletePhongBan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phòng ban cần xóa");
            return;
        }

        row = table.convertRowIndexToModel(row);
        int maPhong = (int) model.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(
                this,
                "Xóa phòng ban này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                PhongBanDAO.delete(maPhong);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không thể xóa phòng ban đang có nhân viên",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }
}
