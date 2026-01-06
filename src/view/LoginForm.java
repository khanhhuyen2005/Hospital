package view;

import dao.TaiKhoanDAO;
import javax.swing.*;

public class LoginForm extends JFrame {

    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JButton btnLogin = new JButton("Đăng nhập");

    public LoginForm() {
        setTitle("Đăng nhập");
        setSize(300, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lb1 = new JLabel("User:");
        JLabel lb2 = new JLabel("Pass:");

        lb1.setBounds(30, 30, 80, 25);
        txtUser.setBounds(120, 30, 130, 25);

        lb2.setBounds(30, 70, 80, 25);
        txtPass.setBounds(120, 70, 130, 25);

        btnLogin.setBounds(90, 120, 120, 30);

        add(lb1); add(txtUser);
        add(lb2); add(txtPass);
        add(btnLogin);

        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        if (TaiKhoanDAO.login(txtUser.getText(), new String(txtPass.getPassword()))) {
            new MainForm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu");
        }
    }
}
