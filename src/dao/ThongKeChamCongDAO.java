package dao;

import connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThongKeChamCongDAO {

    public static ResultSet thongKe(int thang, int nam) throws SQLException {
        String sql = """
            SELECT nv.MaNV,
                   nv.HoTen,

                   COUNT(cc.NgayChamCong) AS SoNgayLam,

                   SUM(CASE WHEN cc.VangMat = 1 THEN 1 ELSE 0 END) AS SoNgayNghi,

                   SUM(ISNULL(cc.SoPhutDiTre,0)) AS TongPhutTre,

                   SUM(ISNULL(cc.SoGioTangCa,0)) AS TongOT
            FROM NhanVien nv
            LEFT JOIN ChamCong cc
              ON nv.MaNV = cc.MaNV
             AND MONTH(cc.NgayChamCong)=?
             AND YEAR(cc.NgayChamCong)=?
            GROUP BY nv.MaNV, nv.HoTen
        """;

        Connection c = DBConnection.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, thang);
        ps.setInt(2, nam);

        return ps.executeQuery();
    }
}

