package model;

import java.sql.Date;
import java.sql.Time;

public class ChamCong {

    private int maNV;
    private Date ngayChamCong;
    private Time gioVao;
    private Time gioRa;

    private double soGioLam;
    private double soGioTangCa;
    private int soPhutDiTre;
    private boolean vangMat;
    private String trangThai;


    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public Date getNgayChamCong() {
        return ngayChamCong;
    }

    public void setNgayChamCong(Date ngayChamCong) {
        this.ngayChamCong = ngayChamCong;
    }

    public Time getGioVao() {
        return gioVao;
    }

    public void setGioVao(Time gioVao) {
        this.gioVao = gioVao;
    }

    public Time getGioRa() {
        return gioRa;
    }

    public void setGioRa(Time gioRa) {
        this.gioRa = gioRa;
    }

    public double getSoGioLam() {
        return soGioLam;
    }

    public void setSoGioLam(double soGioLam) {
        this.soGioLam = soGioLam;
    }

    public double getSoGioTangCa() {
        return soGioTangCa;
    }

    public void setSoGioTangCa(double soGioTangCa) {
        this.soGioTangCa = soGioTangCa;
    }

    public int getSoPhutDiTre() {
        return soPhutDiTre;
    }

    public void setSoPhutDiTre(int soPhutDiTre) {
        this.soPhutDiTre = soPhutDiTre;
    }

    public boolean isVangMat() {
        return vangMat;
    }

    public void setVangMat(boolean vangMat) {
        this.vangMat = vangMat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
