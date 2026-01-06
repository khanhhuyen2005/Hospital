package view;

import dao.PhongBanDAO;
import model.PhongBan;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PhongBanDetailPanel extends JPanel {

    private JTextField txtTen;
    private JTextArea txtMoTa;
    private JSpinner spNgay;
    private int maPhong;
    private Mode mode;
    private MainForm mainForm;

    public PhongBanDetailPanel(MainForm mainForm, Integer maPhong, Mode mode) {
        this.mainForm = mainForm;
        this.mode = mode;
        this.maPhong = maPhong == null ? -1 : maPhong;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBackground(UIStyle.BG_LIGHT);

        txtTen = new JTextField();
        txtMoTa = new JTextArea(3, 20);
        spNgay = new JSpinner(new SpinnerDateModel());

        form.add(new JLabel("Tên phòng"));
        form.add(txtTen);
        form.add(new JLabel("Mô tả"));
        form.add(new JScrollPane(txtMoTa));
        form.add(new JLabel("Ngày thành lập"));
        form.add(spNgay);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton(" Lưu");
        JButton btnBack = new JButton("⬅ Quay lại");

        bottom.add(btnBack);
        if (mode != Mode.VIEW) bottom.add(btnSave);

        add(bottom, BorderLayout.SOUTH);

        btnBack.addActionListener(
                e -> mainForm.setContentPanel(new PhongBanPanel())
        );
        btnSave.addActionListener(e -> save());

        if (maPhong != null) loadData();
        if (mode == Mode.VIEW) disableForm();
    }

    private void loadData() {
        PhongBan pb = PhongBanDAO.getById(maPhong);
        txtTen.setText(pb.getTenPhong());
        txtMoTa.setText(pb.getMoTa());
        spNgay.setValue(pb.getNgayThanhLap());
    }

    private void save() {
        PhongBan pb = new PhongBan();
        pb.setTenPhong(txtTen.getText());
        pb.setMoTa(txtMoTa.getText());
        pb.setNgayThanhLap((Date) spNgay.getValue());

        if (mode == Mode.ADD) {
            PhongBanDAO.insert(pb);
        } else {
            pb.setMaPhong(maPhong);
            PhongBanDAO.update(pb);
        }

        mainForm.setContentPanel(new PhongBanPanel());
    }

    private void disableForm() {
        txtTen.setEnabled(false);
        txtMoTa.setEnabled(false);
        spNgay.setEnabled(false);
    }
}
