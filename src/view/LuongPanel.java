package view;

import dao.LuongDAO;
import model.Luong;
import util.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.text.DecimalFormat;

public class LuongPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtThang, txtNam;

    public LuongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTop(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);

        loadLuong(10, 2025);
    }

    private JPanel createTop() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(UIStyle.BG_CARD);

        panel.add(new JLabel("Tháng:"));
        txtThang = new JTextField("10", 5);
        panel.add(txtThang);

        panel.add(new JLabel("Năm:"));
        txtNam = new JTextField("2025", 6);
        panel.add(txtNam);

        JButton btnTinh = new JButton("Tính lương");
        btnTinh.setFont(UIStyle.BTN_PRIMARY);
        btnTinh.setBackground(UIStyle.PRIMARY);
        btnTinh.setForeground(Color.WHITE);
        btnTinh.setFocusPainted(false);

        JButton btnExcel = new JButton("⬇ Xuất Excel");
        btnExcel.setFont(UIStyle.BTN_PRIMARY);
        btnExcel.setBackground(UIStyle.SUCCESS);
        btnExcel.setForeground(Color.WHITE);
        btnExcel.setFocusPainted(false);

        btnTinh.addActionListener(e -> {
            int thang = Integer.parseInt(txtThang.getText());
            int nam = Integer.parseInt(txtNam.getText());

            LuongDAO.tinhLuongThang(thang, nam);
            loadLuong(thang, nam);
        });

        btnExcel.addActionListener(e -> {
            int thang = Integer.parseInt(txtThang.getText());
            int nam = Integer.parseInt(txtNam.getText());

            ExcelExporter.exportTable(
                    table,
                    "BẢNG LƯƠNG THÁNG " + thang + "/" + nam,
                    "BangLuong",
                    "Bang_luong_" + thang + "_" + nam
            );
        });

        panel.add(btnTinh);
        panel.add(btnExcel);

        return panel;
    }


    private JScrollPane createTable() {

        String[] cols = {
                "STT", "Nhân viên", "Thời gian",
                "Lương cơ bản", "Lương thực tế",
                "Phụ cấp", "Thưởng",
                "Khấu trừ", "Thuế", "Thực lĩnh"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(UIStyle.TABLE);
        table.getTableHeader().setFont(UIStyle.TABLE_HEADER);
        table.getTableHeader().setBackground(UIStyle.PRIMARY_LIGHT);
        table.getTableHeader().setForeground(UIStyle.TEXT_PRIMARY);

        return new JScrollPane(table);
    }


    private void loadLuong(int thang, int nam) {

        model.setRowCount(0);
        List<Luong> list = LuongDAO.getLuongThang(thang, nam);

        DecimalFormat df = new DecimalFormat("#,###");
        int stt = 1;

        for (Luong l : list) {
            model.addRow(new Object[]{
                    stt++,
                    l.getHoTen(),
                    thang + "/" + nam,
                    df.format(l.getLuongCoBan()),
                    df.format(l.getLuongTheoNgay()),
                    df.format(l.getPhuCap()),
                    df.format(l.getThuong()),
                    df.format(l.getKhauTru()),
                    df.format(l.getThue()),
                    df.format(l.getLuongThucNhan())
            });
        }

    }

}
