package com.example.tugas_rpl;

public class tugasAkhirModel {
    private String nama;
    private String nim;
    private String fileName;
    private String fileUrl;

    public tugasAkhirModel() {
        // Default constructor required for calls to DataSnapshot.getValue(tugasAkhirModel.class)
    }

    public tugasAkhirModel(String nama, String nim, String fileName, String fileUrl) {
        this.nama = nama;
        this.nim = nim;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getNama() {
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
