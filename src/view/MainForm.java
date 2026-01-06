package view;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

public class MainForm extends JFrame {

    private JPanel contentPanel;
    private JLabel clockLabel;

    public MainForm() {
        setTitle("BACH MAI HOSPITAL MANAGEMENT SYSTEM");
        setSize(1300, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = createDashboardHeader();
        add(header, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(UIStyle.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(Box.createVerticalStrut(25));

        sidebar.add(sidebarButton("Nhân viên", () ->
                setContentPanel(new NhanVienPanel(this))
        ));

        sidebar.add(sidebarButton("Phòng ban", () ->
                setContentPanel(new PhongBanPanel())
        ));

        sidebar.add(sidebarButton("Chấm công", () ->
                setContentPanel(new ChamCongForm())
        ));

        sidebar.add(sidebarButton("Lương", () ->
                setContentPanel(new LuongPanel())
        ));

        sidebar.add(Box.createVerticalGlue());

        sidebar.add(sidebarButton("Đăng xuất", () -> System.exit(0)));

        add(sidebar, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyle.BG_LIGHT);
        add(contentPanel, BorderLayout.CENTER);

        setContentPanel(new NhanVienPanel(this));
        startClock();
    }

    private JPanel createDashboardHeader() {

        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(85, 150, 155),
                        0, getHeight(), new Color(65, 120, 135)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        header.setPreferredSize(new Dimension(0, 100));
        header.setLayout(new BorderLayout());

        JLabel logo = new JLabel("🏥");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        logo.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 15));
        logo.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel mainTitle = new JLabel("BACH MAI HOSPITAL MANAGEMENT SYSTEM");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(Color.WHITE);

        JLabel subTitle = new JLabel("Human Resources & Payroll Dashboard");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(220, 240, 240));

        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(mainTitle);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subTitle);
        titlePanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clockLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Admin");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(clockLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(userLabel);
        rightPanel.add(Box.createVerticalGlue());

        header.add(logo, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);

        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0,
                new Color(0, 0, 0, 40)));

        return header;
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss  |  dd/MM/yyyy");
            clockLabel.setText(sdf.format(new Date()));
        });
        timer.start();
    }

    public void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JButton sidebarButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setFont(UIStyle.SIDEBAR_BTN);
        btn.setBackground(UIStyle.BG_SIDEBAR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addActionListener(e -> action.run());
        return btn;
    }
}
