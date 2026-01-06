package model;

import java.util.Date;

public class PhongBan {
    private int maPhong;
    private String tenPhong;
    private String moTa;
    private Date ngayThanhLap;

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Date getNgayThanhLap() { return ngayThanhLap; }
    public void setNgayThanhLap(Date ngayThanhLap) { this.ngayThanhLap = ngayThanhLap; }
}
