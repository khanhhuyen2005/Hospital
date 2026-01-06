package view;

import dao.NhanVienDAO;
import model.NhanVien;
import util.ExcelExporter;
import view.table.ActionCellEditor;
import view.table.ActionCellRenderer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NhanVienPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private MainForm mainForm;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public NhanVienPanel(MainForm mainForm) {
        this.mainForm = mainForm;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.LIGHT);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel row1 = new JPanel(new BorderLayout());
        row1.setOpaque(false);

        JLabel title = new JLabel("Danh sách nhân viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        row1.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JTextField txtSearch = new JTextField(20);
        JButton btnExcel = new JButton("⬇ Xuất Excel");
        JButton btnAdd = new JButton("➕ Thêm");

        actions.add(txtSearch);
        actions.add(btnExcel);
        actions.add(btnAdd);

        row1.add(actions, BorderLayout.EAST);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        row2.setOpaque(false);

        JComboBox<String> cbPhong = new JComboBox<>(new String[]{
                "Tất cả phòng", "Khoa Nội", "Khoa Nhi", "Khoa Dược"
        });
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{
                "Tất cả giới tính", "Nam", "Nữ"
        });
        JComboBox<String> cbChucVu = new JComboBox<>(new String[]{
                "Tất cả chức vụ", "Bác sĩ", "Y tá", "Hành chính"
        });
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{
                "Tất cả trạng thái", "Đang làm việc", "Nghỉ"
        });

        row2.add(cbPhong);
        row2.add(cbGioiTinh);
        row2.add(cbChucVu);
        row2.add(cbTrangThai);

        header.add(row1);
        header.add(Box.createVerticalStrut(8));
        header.add(row2);

        model = new DefaultTableModel(new String[]{
               "MaNV", "STT", "Họ tên", "Giới tính",
                "Phòng ban", "Chức vụ", "Lương",
                "Trạng thái", "Thao tác"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220, 230, 255));
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(UIStyle.PRIMARY);
        th.setForeground(Color.WHITE);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.getColumn("Thao tác").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Thao tác").setCellEditor(
                new ActionCellEditor(table, mainForm)
        );

        JScrollPane scroll = new JScrollPane(table);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadData();

        Runnable applyFilter = () -> {
            String keyword = txtSearch.getText().trim().toLowerCase();

            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> e) {
                    for (int i = 0; i < e.getValueCount(); i++) {
                        Object v = e.getValue(i);
                        if (v != null && v.toString().toLowerCase().contains(keyword)) {
                            return true;
                        }
                    }
                    return keyword.isEmpty();
                }
            });
        };

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter.run(); }
            public void removeUpdate(DocumentEvent e) { applyFilter.run(); }
            public void changedUpdate(DocumentEvent e) { applyFilter.run(); }
        });

        btnExcel.addActionListener(e -> {
            ExcelExporter.exportTable(
                    table,
                    "DANH SÁCH NHÂN VIÊN BỆNH VIỆN BẠCH MAI",
                    "NhanVien",
                    "Danh_sach_nhan_vien"
            );
        });


        btnAdd.addActionListener(e ->
                mainForm.setContentPanel(new NhanVienDetailPanel(mainForm, null, Mode.ADD))
        );
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<NhanVien> list = NhanVienDAO.getAll();
        int stt = 1;
        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    stt++,
                    nv.getHoTen(),
                    nv.getGioiTinh(),
                    nv.getTenPhong(),
                    nv.getChucVu(),
                    moneyFormat.format(nv.getLuongCoBan()),
                    nv.getTrangThai(),
                    ""
            });
        }
    }
}
