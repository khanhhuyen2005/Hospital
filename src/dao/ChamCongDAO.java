package dao;

import connection.DBConnection;
import model.ChamCong;

import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class ChamCongDAO {

    // ================= CHẤM CÔNG =================
    public static void chamCong(ChamCong cc) {
        tinhGioLamVaDiTre(cc);

        String sql = """
            INSERT INTO ChamCong
            (MaNV, NgayChamCong, GioVao, GioRa,
             SoGioLam, SoGioTangCa, SoPhutDiTre, VangMat, TrangThai)
            VALUES (?,?,?,?,?,?,?,?,?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, cc.getMaNV());
            ps.setDate(2, cc.getNgayChamCong());
            ps.setTime(3, cc.getGioVao());
            ps.setTime(4, cc.getGioRa());
            ps.setDouble(5, cc.getSoGioLam());
            ps.setDouble(6, cc.getSoGioTangCa());
            ps.setInt(7, cc.getSoPhutDiTre());
            ps.setBoolean(8, cc.isVangMat());
            ps.setString(9, cc.getTrangThai());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= TÍNH GIỜ – TRỄ =================
    private static void tinhGioLamVaDiTre(ChamCong cc) {

        if (cc.getGioVao() == null || cc.getGioRa() == null) {
            cc.setSoGioLam(0);
            cc.setSoGioTangCa(0);
            cc.setSoPhutDiTre(0);
            cc.setVangMat(true);
            cc.setTrangThai("Vắng");
            return;
        }

        LocalTime vao = cc.getGioVao().toLocalTime();
        LocalTime ra = cc.getGioRa().toLocalTime();
        LocalTime gioChuan = LocalTime.of(8, 0);

        int phutTre = vao.isAfter(gioChuan)
                ? (int) Duration.between(gioChuan, vao).toMinutes()
                : 0;

        double gioLamThucTe = Duration.between(vao, ra).toMinutes() / 60.0;

        double gioCong = Math.max(0, 8 - phutTre / 60.0);

        cc.setSoPhutDiTre(phutTre);
        cc.setSoGioLam(Math.min(gioCong, gioLamThucTe));
        cc.setSoGioTangCa(Math.max(0, gioLamThucTe - 8));
        cc.setVangMat(false);
        cc.setTrangThai("Đi làm");
    }

    // ================= CHI TIẾT THEO THÁNG =================
    public static ArrayList<ChamCong> getChiTiet(int maNV, int thang, int nam) {
        ArrayList<ChamCong> list = new ArrayList<>();

        String sql = """
            SELECT NgayChamCong, SoPhutDiTre, SoGioTangCa, VangMat
            FROM ChamCong
            WHERE MaNV=? AND MONTH(NgayChamCong)=? AND YEAR(NgayChamCong)=?
            ORDER BY NgayChamCong
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, maNV);
            ps.setInt(2, thang);
            ps.setInt(3, nam);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChamCong cc = new ChamCong();
                cc.setNgayChamCong(rs.getDate("NgayChamCong"));
                cc.setSoPhutDiTre(rs.getInt("SoPhutDiTre"));
                cc.setSoGioTangCa(rs.getDouble("SoGioTangCa"));
                cc.setVangMat(rs.getBoolean("VangMat"));
                list.add(cc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
