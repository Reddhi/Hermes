package org.gtre.itg.hermes;

public class Constants {

    private int thresholdTemperature;
    private String filePath;

    public Constants() {
    }

    public int getThresholdTemperature() {
        return thresholdTemperature;
    }

    public void setThresholdTemperature(int thresholdTemperature) {
        this.thresholdTemperature = thresholdTemperature;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}