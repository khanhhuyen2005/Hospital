package view;

import dao.ThongKeDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ThongKePanel extends JPanel {

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.BG_LIGHT);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Phòng ban", "Số nhân viên"}, 0
        );

        JTable table = new JTable(model);
        table.setRowHeight(30);

        Map<String, Integer> data = ThongKeDao.soNhanVienTheoPhong();
        data.forEach((k, v) -> model.addRow(new Object[]{k, v}));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
