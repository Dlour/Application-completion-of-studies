package com.example.tugas_rpl;

public class mahasiswaModel {
    String prodi, angkatan, nama, nim, email, password, mahasiswa;
    int sks;
    double ipk;
    boolean yudisium;

    public mahasiswaModel() {

    }


    public mahasiswaModel(String nama, String nim, String email, String password, String prodi, String angkatan, int sks, boolean yudisium, String mahasiswa, double ipk) {
        if (nama == null || nim == null || email == null || password == null || prodi == null || angkatan == null || mahasiswa == null) {
            throw new IllegalArgumentException("Input tidak boleh kosong");
        }

        this.nama = nama;
        this.nim = nim;
        this.email = email;
        this.password = password;
        this.prodi = prodi;
        this.angkatan = angkatan;
        this.sks = sks;
        this.yudisium = yudisium;
        this.mahasiswa = mahasiswa;
        this.ipk=ipk;

    }

    public double getIpk() {
        return ipk;
    }

    public void setIpk(double ipk) {
        this.ipk = ipk;
    }

    // Getter and Setter methods





    public String getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(String mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public  String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getAngkatan() {
        return angkatan;
    }

    public void setAngkatan(String angkatan) {
        this.angkatan = angkatan;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public boolean isYudisium() {
        return yudisium;
    }

    public void setYudisium(boolean yudisium) {
        this.yudisium = yudisium;
    }


}
