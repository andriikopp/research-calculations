package edu.bpmanalysis.web.bpmq.entity;

public class BPMQualitySummary {
    private int totalModels;
    private int correctModels;
    private int invalidModels;

    private int sizeQualityCount;
    private int degreesQualityCount;
    private int eventsQualityCount;
    private int gatewaysQualityCount;
    private int orQualityCount;

    public BPMQualitySummary(int totalModels, int correctModels, int invalidModels, int sizeQualityCount, int degreesQualityCount, int eventsQualityCount, int gatewaysQualityCount, int orQualityCount) {
        this.totalModels = totalModels;
        this.correctModels = correctModels;
        this.invalidModels = invalidModels;
        this.sizeQualityCount = sizeQualityCount;
        this.degreesQualityCount = degreesQualityCount;
        this.eventsQualityCount = eventsQualityCount;
        this.gatewaysQualityCount = gatewaysQualityCount;
        this.orQualityCount = orQualityCount;
    }

    public int getTotalModels() {
        return totalModels;
    }

    public void setTotalModels(int totalModels) {
        this.totalModels = totalModels;
    }

    public int getCorrectModels() {
        return correctModels;
    }

    public void setCorrectModels(int correctModels) {
        this.correctModels = correctModels;
    }

    public int getInvalidModels() {
        return invalidModels;
    }

    public void setInvalidModels(int invalidModels) {
        this.invalidModels = invalidModels;
    }

    public int getSizeQualityCount() {
        return sizeQualityCount;
    }

    public void setSizeQualityCount(int sizeQualityCount) {
        this.sizeQualityCount = sizeQualityCount;
    }

    public int getDegreesQualityCount() {
        return degreesQualityCount;
    }

    public void setDegreesQualityCount(int degreesQualityCount) {
        this.degreesQualityCount = degreesQualityCount;
    }

    public int getEventsQualityCount() {
        return eventsQualityCount;
    }

    public void setEventsQualityCount(int eventsQualityCount) {
        this.eventsQualityCount = eventsQualityCount;
    }

    public int getGatewaysQualityCount() {
        return gatewaysQualityCount;
    }

    public void setGatewaysQualityCount(int gatewaysQualityCount) {
        this.gatewaysQualityCount = gatewaysQualityCount;
    }

    public int getOrQualityCount() {
        return orQualityCount;
    }

    public void setOrQualityCount(int orQualityCount) {
        this.orQualityCount = orQualityCount;
    }
}
