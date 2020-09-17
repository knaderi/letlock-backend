package com.landedexperts.letlock.filetransfer.backend.service;

public class UploadProgressStat {

    Double currentUploadSize = 0.00;
    Double uploadPercentage = 0.00;


    public Double getCurrentUploadSize() {
        return currentUploadSize;
    }

    public void setCurrentUploadSize(Double uploadSize) {
        this.currentUploadSize = uploadSize;
    }

    public Double getUploadPercentage() {
        return uploadPercentage;
    }

    public void setUploadPercentage(Double uploadPercentage) {
        this.uploadPercentage = uploadPercentage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentUploadSize == null) ? 0 : currentUploadSize.hashCode());
        result = prime * result + ((uploadPercentage == null) ? 0 : uploadPercentage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UploadProgressStat other = (UploadProgressStat) obj;
        if (currentUploadSize == null) {
            if (other.currentUploadSize != null)
                return false;
        } else if (!currentUploadSize.equals(other.currentUploadSize))
            return false;
        if (uploadPercentage == null) {
            if (other.uploadPercentage != null)
                return false;
        } else if (!uploadPercentage.equals(other.uploadPercentage))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UploadProgressStat [currentUploadSize=" + currentUploadSize + ", uploadPercentage=" + uploadPercentage + "]";
    }


}
