package com.ayubo.life.ayubolife.insurances.Classes;

public class RequestFileClaimImage {
    private String file_folder;
    private String file_contentType;
    private String file_extension;
    private String file_string;

    public RequestFileClaimImage(String file_folder, String file_contentType, String file_extension, String file_string) {
        this.file_folder = file_folder;
        this.file_contentType = file_contentType;
        this.file_extension = file_extension;
        this.file_string = file_string;
    }

    public String getFile_folder() {
        return file_folder;
    }

    public void setFile_folder(String file_folder) {
        this.file_folder = file_folder;
    }

    public String getFile_contentType() {
        return file_contentType;
    }

    public void setFile_contentType(String file_contentType) {
        this.file_contentType = file_contentType;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getFile_string() {
        return file_string;
    }

    public void setFile_string(String file_string) {
        this.file_string = file_string;
    }
}
