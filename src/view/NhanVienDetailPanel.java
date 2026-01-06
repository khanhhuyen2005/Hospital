package view;

import dao.NhanVienDAO;
import dao.PhongBanDAO;
import model.NhanVien;
import model.PhongBan;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NhanVienDetailPanel extends JPanel {

    private final MainForm mainForm;
    private final Mode mode;

    private JTextField txtMaNV = new JTextField();
    private JTextField txtHoTen = new JTextField();
    private JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
    private JTextField txtNgaySinh = new JTextField();
    private JTextField txtDiaChi = new JTextField();
    private JTextField txtSDT = new JTextField();
    private JTextField txtEmail = new JTextField();
    private JComboBox<String> cbPhongBan;
    private int[] phongBanIds;
    private JTextField txtChucVu = new JTextField();
    private JTextField txtHocVi = new JTextField();
    private JTextField txtNgayVaoLam = new JTextField();
    private JTextField txtLuong = new JTextField();
    private JTextField txtTrangThai = new JTextField();

    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public NhanVienDetailPanel(MainForm mainForm, Integer maNV, Mode mode) {
        this.mainForm = mainForm;
        this.mode = mode;

        df.setLenient(false);

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        loadPhongBan();

        add(createForm(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        txtMaNV.setEnabled(false);

        txtNgaySinh.setToolTipText("yyyy-MM-dd");
        txtNgayVaoLam.setToolTipText("yyyy-MM-dd");

        if (maNV != null) {
            loadData(maNV);
        }

        if (mode == Mode.VIEW) {
            disableAll();
        }
    }

    private void loadPhongBan() {
        ArrayList<PhongBan> listPB = PhongBanDAO.getAll();
        String[] names = new String[listPB.size()];
        phongBanIds = new int[listPB.size()];

        for (int i = 0; i < listPB.size(); i++) {
            names[i] = listPB.get(i).getTenPhong();
            phongBanIds[i] = listPB.get(i).getMaPhong();
        }

        cbPhongBan = new JComboBox<>(names);
    }

    private JPanel createForm() {
        JPanel p = new JPanel(new GridLayout(7, 4, 15, 12));
        p.setOpaque(false);

        addField(p, "Mã NV", txtMaNV);
        addField(p, "Họ tên", txtHoTen);
        addField(p, "Giới tính", cbGioiTinh);
        addField(p, "Ngày sinh", txtNgaySinh);
        addField(p, "Địa chỉ", txtDiaChi);
        addField(p, "SĐT", txtSDT);
        addField(p, "Email", txtEmail);
        addField(p, "Phòng ban", cbPhongBan);
        addField(p, "Chức vụ", txtChucVu);
        addField(p, "Học vị", txtHocVi);
        addField(p, "Ngày vào làm", txtNgayVaoLam);
        addField(p, "Lương cơ bản", txtLuong);
        addField(p, "Trạng thái", txtTrangThai);

        return p;
    }

    private void addField(JPanel p, String label, JComponent c) {
        JLabel lb = new JLabel(label);
        lb.setFont(UIStyle.NORMAL);
        p.add(lb);

        c.setFont(UIStyle.INPUT);
        p.add(c);
    }

    private JPanel createFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setOpaque(false);

        JButton btnBack = new JButton("← Quay lại");
        JButton btnSave = new JButton(" Lưu");

        btnBack.addActionListener(e ->
                mainForm.setContentPanel(new NhanVienPanel(mainForm))
        );

        btnSave.setVisible(mode != Mode.VIEW);
        btnSave.addActionListener(e -> save());

        p.add(btnBack);
        p.add(btnSave);
        return p;
    }

    private void save() {
        try {
            if (txtHoTen.getText().trim().isEmpty()) {
                showError("Họ tên không được để trống");
                return;
            }

            java.util.Date utilNgaySinh = df.parse(txtNgaySinh.getText());
            java.sql.Date sqlNgaySinh = new java.sql.Date(utilNgaySinh.getTime());

            java.util.Date utilNgayVaoLam = df.parse(txtNgayVaoLam.getText());
            java.sql.Date sqlNgayVaoLam = new java.sql.Date(utilNgayVaoLam.getTime());

            double luong;
            try {
                luong = Double.parseDouble(txtLuong.getText().replace(",", "").trim());
            } catch (NumberFormatException e) {
                showError("Lương không hợp lệ");
                return;
            }

            NhanVien nv = new NhanVien();

            if (!txtMaNV.getText().isEmpty()) {
                nv.setMaNV(Integer.parseInt(txtMaNV.getText()));
            }

            nv.setHoTen(txtHoTen.getText().trim());
            nv.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
            nv.setNgaySinh(sqlNgaySinh);
            nv.setDiaChi(txtDiaChi.getText().trim());
            nv.setSoDienThoai(txtSDT.getText().trim());
            nv.setEmail(txtEmail.getText().trim());
            nv.setChucVu(txtChucVu.getText().trim());
            nv.setHocVi(txtHocVi.getText().trim());
            nv.setNgayVaoLam(sqlNgayVaoLam);
            nv.setLuongCoBan(luong);
            nv.setTrangThai(txtTrangThai.getText().trim());

            int index = cbPhongBan.getSelectedIndex();
            nv.setMaPhong(phongBanIds[index]);

            if (mode == Mode.ADD) {
                NhanVienDAO.insert(nv);
            } else if (mode == Mode.EDIT) {
                NhanVienDAO.update(nv);
            }

            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            mainForm.setContentPanel(new NhanVienPanel(mainForm));

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Dữ liệu không hợp lệ\nNgày phải đúng yyyy-MM-dd");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void loadData(int maNV) {
        NhanVien nv = NhanVienDAO.getById(maNV);
        if (nv == null) return;

        txtMaNV.setText(String.valueOf(nv.getMaNV()));
        txtHoTen.setText(nv.getHoTen());
        cbGioiTinh.setSelectedItem(nv.getGioiTinh());
        txtNgaySinh.setText(df.format(nv.getNgaySinh()));
        txtDiaChi.setText(nv.getDiaChi());
        txtSDT.setText(nv.getSoDienThoai());
        txtEmail.setText(nv.getEmail());

        // chọn phòng ban đúng
        for (int i = 0; i < phongBanIds.length; i++) {
            if (phongBanIds[i] == nv.getMaPhong()) {
                cbPhongBan.setSelectedIndex(i);
                break;
            }
        }

        txtChucVu.setText(nv.getChucVu());
        txtHocVi.setText(nv.getHocVi());
        txtNgayVaoLam.setText(df.format(nv.getNgayVaoLam()));
        txtLuong.setText(moneyFormat.format(nv.getLuongCoBan()));
        txtTrangThai.setText(nv.getTrangThai());
    }

    private void disableAll() {
        disableRecursively(this);
    }

    private void disableRecursively(Container container) {
        for (Component c : container.getComponents()) {
            c.setEnabled(false);
            if (c instanceof Container) {
                disableRecursively((Container) c);
            }
        }
    }
}
