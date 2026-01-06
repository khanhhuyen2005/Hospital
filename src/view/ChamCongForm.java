package view;

import dao.ThongKeChamCongDAO;
import dao.ChamCongDAO;
import model.ChamCong;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChamCongForm extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextArea txtChiTiet;
    private JTextField txtSearch;
    private JComboBox<Integer> cboThang, cboNam;
    private TableRowSorter<DefaultTableModel> sorter;

    public ChamCongForm() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.BG_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        LocalDate now = LocalDate.now();
        cboThang.setSelectedItem(now.getMonthValue());
        cboNam.setSelectedItem(now.getYear());
        loadData(now.getMonthValue(), now.getYear());
    }


    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh sách chấm công nhân viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        txtSearch = new JTextField(18);
        txtSearch.setBorder(BorderFactory.createTitledBorder("Tìm theo tên"));

        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);
        cboThang.setBorder(BorderFactory.createTitledBorder("Tháng"));

        cboNam = new JComboBox<>();
        for (int i = 2023; i <= 2030; i++) cboNam.addItem(i);
        cboNam.setBorder(BorderFactory.createTitledBorder("Năm"));

        JButton btnLoc = new JButton("Lọc");
        btnLoc.addActionListener(e ->
                loadData(
                        (int) cboThang.getSelectedItem(),
                        (int) cboNam.getSelectedItem()
                )
        );

        right.add(txtSearch);
        right.add(cboThang);
        right.add(cboNam);
        right.add(btnLoc);

        panel.add(title, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }


    private JSplitPane createMainContent() {
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                createTablePanel(),
                createDetailPanel()
        );
        split.setDividerLocation(360);
        split.setResizeWeight(0.7);
        split.setBorder(null);
        return split;
    }


    private JScrollPane createTablePanel() {
        model = new DefaultTableModel(
                new String[]{
                        "MaNV",
                        "STT",
                        "Họ tên",
                        "Tháng/Năm",
                        "Ngày làm",
                        "Ngày nghỉ",
                        "Phút trễ",
                        "Giờ OT"
                }, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ẩn MaNV
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        table.getSelectionModel().addListSelectionListener(e -> showChiTiet());

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        return new JScrollPane(table);
    }

    private void filter() {
        String text = txtSearch.getText();
        sorter.setRowFilter(text.isBlank()
                ? null
                : RowFilter.regexFilter("(?i)" + text, 2));
    }


    private void loadData(int thang, int nam) {
        model.setRowCount(0);
        int stt = 1;

        try (ResultSet rs = ThongKeChamCongDAO.thongKe(thang, nam)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("MaNV"),
                        stt++,
                        rs.getString("HoTen"),
                        thang + "/" + nam,
                        rs.getInt("SoNgayLam"),
                        rs.getInt("SoNgayNghi"),
                        rs.getInt("TongPhutTre"),
                        rs.getDouble("TongOT")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu");
        }
    }


    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết chấm công"));

        txtChiTiet = new JTextArea();
        txtChiTiet.setEditable(false);
        txtChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(new JScrollPane(txtChiTiet), BorderLayout.CENTER);
        return panel;
    }

    private void showChiTiet() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            txtChiTiet.setText("");
            return;
        }

        int row = table.convertRowIndexToModel(viewRow);

        int maNV = (int) model.getValueAt(row, 0);
        String tenNV = model.getValueAt(row, 2).toString();
        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        ArrayList<ChamCong> list =
                ChamCongDAO.getChiTiet(maNV, thang, nam);

        StringBuilder sb = new StringBuilder();
        sb.append("Nhân viên: ").append(tenNV).append("\n");
        sb.append("Tháng: ").append(thang).append("/").append(nam).append("\n\n");

        sb.append("Nghỉ:\n");
        list.stream().filter(ChamCong::isVangMat)
                .forEach(cc -> sb.append("  - ").append(cc.getNgayChamCong()).append("\n"));
        sb.append("\nĐi trễ:\n");
        list.stream().filter(cc -> cc.getSoPhutDiTre() > 0)
                .forEach(cc -> sb.append("  - ")
                        .append(cc.getNgayChamCong())
                        .append(": ")
                        .append(cc.getSoPhutDiTre())
                        .append(" phút\n"));

        sb.append("\nTăng ca:\n");
        list.stream().filter(cc -> cc.getSoGioTangCa() > 0)
                .forEach(cc -> sb.append("  - ")
                        .append(cc.getNgayChamCong())
                        .append(": ")
                        .append(cc.getSoGioTangCa())
                        .append(" giờ\n"));

        txtChiTiet.setText(sb.toString());
    }
}
