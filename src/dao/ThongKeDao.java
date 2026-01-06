package dao;

import connection.DBConnection;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeDao {
    public static Map<String, Integer> soNhanVienTheoPhong() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = """
            SELECT pb.TenPhong, COUNT(*) AS SoLuong
            FROM NhanVien nv
            JOIN PhongBan pb ON nv.MaPhong = pb.MaPhong
            GROUP BY pb.TenPhong
        """;

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                map.put(rs.getString("TenPhong"), rs.getInt("SoLuong"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
