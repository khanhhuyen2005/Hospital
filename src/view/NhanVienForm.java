package view;

import dao.NhanVienDAO;
import model.NhanVien;

import javax.swing.*;
import java.awt.*;

public class NhanVienForm extends JFrame {

    private Integer maNV;
    private boolean viewOnly;

    public NhanVienForm(Integer maNV) {
        this.maNV = maNV;
        this.viewOnly = false;
        initUI();

        if (maNV != null) {
            loadData(maNV);
        }
    }

    public NhanVienForm(int maNV, boolean viewOnly) {
        this.maNV = maNV;
        this.viewOnly = viewOnly;
        initUI();
        loadData(maNV);

        if (viewOnly) {
            disableAllFields();
        }
    }

    private void initUI() {
        setTitle("Thông tin nhân viên");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10));

        add(new JLabel("Họ tên:"));
        add(new JTextField());

        add(new JLabel("Giới tính:"));
        add(new JComboBox<>(new String[]{"Nam", "Nữ"}));

        add(new JLabel("Phòng ban:"));
        add(new JTextField());

        add(new JLabel("Chức vụ:"));
        add(new JTextField());

        add(new JLabel("Lương:"));
        add(new JTextField());

        JButton btnSave = new JButton("Lưu");
        JButton btnClose = new JButton("Đóng");

        add(btnSave);
        add(btnClose);

        btnClose.addActionListener(e -> dispose());
    }

    private void loadData(int maNV) {
        NhanVien nv = NhanVienDAO.getById(maNV);
        if (nv == null) return;
    }

    private void disableAllFields() {
    }
}
