package view;

import dao.PhongBanDAO;
import model.PhongBan;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PhongBanForm extends JDialog {

    private JTextField txtTenPhong;
    private JTextArea txtMoTa;
    private JSpinner spNgay;
    private PhongBan phongBan;

    public PhongBanForm(Frame parent, PhongBan pb) {
        super(parent, true);
        this.phongBan = pb;

        setTitle(pb == null ? "Thêm phòng ban" : "Sửa phòng ban");
        setSize(400, 320);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridLayout(0,1,5,5));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtTenPhong = new JTextField();
        txtMoTa = new JTextArea(3,20);
        spNgay = new JSpinner(new SpinnerDateModel());

        form.add(new JLabel("Tên phòng"));
        form.add(txtTenPhong);
        form.add(new JLabel("Mô tả"));
        form.add(new JScrollPane(txtMoTa));
        form.add(new JLabel("Ngày thành lập"));
        form.add(spNgay);

        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> save());

        add(form, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        if (pb != null) {
            txtTenPhong.setText(pb.getTenPhong());
            txtMoTa.setText(pb.getMoTa());
            spNgay.setValue(pb.getNgayThanhLap());
        }
    }

    private void save() {
        if (txtTenPhong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phòng không được trống");
            return;
        }

        if (phongBan == null)
            phongBan = new PhongBan();

        phongBan.setTenPhong(txtTenPhong.getText());
        phongBan.setMoTa(txtMoTa.getText());
        phongBan.setNgayThanhLap((Date) spNgay.getValue());

        if (phongBan.getMaPhong() == 0)
            PhongBanDAO.insert(phongBan);
        else
            PhongBanDAO.update(phongBan);

        dispose();
    }
}
