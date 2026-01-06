package dao;

import model.Luong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LuongDAO {

    private static final String URL =
            "jdbc:sqlserver://DESKTOP-5Q8KCPO\\KTEAM;"
                    + "databaseName=BenhVienBachMai;"
                    + "integratedSecurity=true;"
                    + "trustServerCertificate=true";

    public static void tinhLuongThang(int thang, int nam) {

        String sql = """
            INSERT INTO Luong
            (MaNV, Thang, Nam, LuongCoBan, PhuCap, Thuong, KhauTru, Thue, SoNgayLam, LuongTheoNgay, TienTangCa, TienPhatTre, LuongThucNhan, NgayChot)
            SELECT
                nv.MaNV,
                ?, ?,
                nv.LuongCoBan,

                CASE
                    WHEN nv.ChucVu LIKE N'%Bác sĩ%' THEN 2000000
                    WHEN nv.ChucVu LIKE N'%Điều dưỡng%' THEN 1000000
                    ELSE 500000
                END AS PhuCap,

                ISNULL(SUM(cc.SoGioTangCa),0) * 50000 AS Thuong,
                ISNULL(SUM(cc.SoPhutDiTre),0) * 5000 AS KhauTru,
                nv.LuongCoBan * 0.05 AS Thue,
                COUNT(CASE WHEN cc.VangMat = 0 THEN 1 END) AS SoNgayLam,
                nv.LuongCoBan / 26 AS LuongTheoNgay,
                ISNULL(SUM(cc.SoGioTangCa),0) * 50000 AS TienTangCa,
                ISNULL(SUM(cc.SoPhutDiTre),0) * 5000 AS TienPhatTre,
                
                (
                COUNT(CASE WHEN cc.VangMat = 0 THEN 1 END) * (nv.LuongCoBan / 26)
                + ISNULL(SUM(cc.SoGioTangCa),0) * 50000
                - ISNULL(SUM(cc.SoPhutDiTre),0) * 5000
                + CASE
                   WHEN nv.ChucVu LIKE N'%Bác sĩ%' THEN 2000000
                   WHEN nv.ChucVu LIKE N'%Điều dưỡng%' THEN 1000000
                   ELSE 500000
                  END
                - nv.LuongCoBan * 0.05
                ) AS LuongThucNhan,
                
                GETDATE()

            FROM NhanVien nv
            LEFT JOIN ChamCong cc
                ON nv.MaNV = cc.MaNV
               AND MONTH(cc.NgayChamCong) = ?
               AND YEAR(cc.NgayChamCong) = ?

            WHERE NOT EXISTS (
                SELECT 1 FROM Luong l
                WHERE l.MaNV = nv.MaNV
                  AND l.Thang = ?
                  AND l.Nam = ?
            )

            GROUP BY nv.MaNV, nv.LuongCoBan, nv.ChucVu;
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ps.setInt(3, thang);
            ps.setInt(4, nam);
            ps.setInt(5, thang);
            ps.setInt(6, nam);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Luong> getLuongThang(int thang, int nam) {

        List<Luong> list = new ArrayList<>();

        String sql = "EXEC spTinhLuong ?, ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Luong l = new Luong();
                l.setMaNV(rs.getInt("MaNV"));
                l.setHoTen(rs.getString("HoTen"));
                l.setTenPhong(rs.getString("TenPhong"));
                l.setLuongCoBan(rs.getDouble("LuongCoBan"));
                l.setLuongTheoNgay(rs.getDouble("LuongTheoNgay"));
                l.setPhuCap(rs.getDouble("PhuCap"));
                l.setThuong(rs.getDouble("Thuong"));
                l.setKhauTru(rs.getDouble("TienPhatTre"));
                l.setThue(rs.getDouble("Thue"));
                l.setLuongThucNhan(rs.getDouble("ThucLinh"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static List<Luong> getLuong(int thang, int nam) {
        return getLuongThang(thang, nam);
    }
}
