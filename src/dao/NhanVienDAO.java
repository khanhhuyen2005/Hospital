package dao;

import model.NhanVien;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class NhanVienDAO {

    private static final String URL =
            "jdbc:sqlserver://DESKTOP-5Q8KCPO\\KTEAM;"
                    + "databaseName=BenhVienBachMai;"
                    + "integratedSecurity=true;"
                    + "trustServerCertificate=true";

    public static ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> list = new ArrayList<>();
        String sql = """
            SELECT nv.*, pb.TenPhong
            FROM NhanVien nv
            LEFT JOIN PhongBan pb ON nv.MaPhong = pb.MaPhong
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setMaPhong(rs.getInt("MaPhong"));
                nv.setTenPhong(rs.getString("TenPhong"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setHocVi(rs.getString("HocVi"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nv.setTrangThai(rs.getString("TrangThai"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static NhanVien getById(int maNV) {
        String sql = """
            SELECT nv.*, pb.TenPhong
            FROM NhanVien nv
            LEFT JOIN PhongBan pb ON nv.MaPhong = pb.MaPhong
            WHERE nv.MaNV = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNV);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setMaPhong(rs.getInt("MaPhong"));
                nv.setTenPhong(rs.getString("TenPhong"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setHocVi(rs.getString("HocVi"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nv.setTrangThai(rs.getString("TrangThai"));
                return nv;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insert(NhanVien nv) {
        String sql = """
            INSERT INTO NhanVien
            (HoTen, GioiTinh, NgaySinh, DiaChi, SoDienThoai, Email,
             MaPhong, ChucVu, HocVi, NgayVaoLam, LuongCoBan, TrangThai)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getGioiTinh());
            ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(4, nv.getDiaChi());
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getEmail());
            ps.setInt(7, nv.getMaPhong());
            ps.setString(8, nv.getChucVu());
            ps.setString(9, nv.getHocVi());
            ps.setDate(10, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            ps.setDouble(11, nv.getLuongCoBan());
            ps.setString(12, nv.getTrangThai());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(NhanVien nv) {
        String sql = """
            UPDATE NhanVien SET
            HoTen=?, GioiTinh=?, NgaySinh=?, DiaChi=?, SoDienThoai=?,
            Email=?, MaPhong=?, ChucVu=?, HocVi=?, NgayVaoLam=?,
            LuongCoBan=?, TrangThai=?
            WHERE MaNV=?
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getGioiTinh());
            ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(4, nv.getDiaChi());
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getEmail());
            ps.setInt(7, nv.getMaPhong());
            ps.setString(8, nv.getChucVu());
            ps.setString(9, nv.getHocVi());
            ps.setDate(10, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            ps.setDouble(11, nv.getLuongCoBan());
            ps.setString(12, nv.getTrangThai());
            ps.setInt(13, nv.getMaNV());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(int maNV) {
        String sql = "UPDATE NhanVien SET TrangThai = N'Ngừng làm việc' WHERE MaNV = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNV);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Không thể xóa nhân viên đã có chấm công!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

}
