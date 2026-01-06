package dao;

import connection.DBConnection;
import model.PhongBan;

import java.sql.*;
import java.util.ArrayList;

public class PhongBanDAO {

    private static final String URL =
            "jdbc:sqlserver://DESKTOP-5Q8KCPO\\KTEAM;"
                    + "databaseName=BenhVienBachMai;"
                    + "integratedSecurity=true;"
                    + "trustServerCertificate=true";


    public static ArrayList<PhongBan> getAll() {
        ArrayList<PhongBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setMaPhong(rs.getInt("MaPhong"));
                pb.setTenPhong(rs.getString("TenPhong"));
                pb.setMoTa(rs.getString("MoTa"));
                pb.setNgayThanhLap(rs.getDate("NgayThanhLap"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insert(PhongBan pb) {
        String sql = "INSERT INTO PhongBan(TenPhong, MoTa, NgayThanhLap) VALUES (?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pb.getTenPhong());
            ps.setString(2, pb.getMoTa());
            ps.setDate(3, new java.sql.Date(pb.getNgayThanhLap().getTime()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(PhongBan pb) {
        String sql = """
            UPDATE PhongBan
            SET TenPhong=?, MoTa=?, NgayThanhLap=?
            WHERE MaPhong=?
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pb.getTenPhong());
            ps.setString(2, pb.getMoTa());
            ps.setDate(3, new java.sql.Date(pb.getNgayThanhLap().getTime()));
            ps.setInt(4, pb.getMaPhong());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(int maPhong) {
        String sql = "DELETE FROM PhongBan WHERE MaPhong=?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa phòng ban đang có nhân viên");
        }
    }


    public static PhongBan getById(int maPhong) {

        String sql = "SELECT * FROM PhongBan WHERE MaPhong=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setMaPhong(rs.getInt("MaPhong"));
                pb.setTenPhong(rs.getString("TenPhong"));
                pb.setMoTa(rs.getString("MoTa"));
                pb.setNgayThanhLap(rs.getDate("NgayThanhLap"));
                return pb;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
