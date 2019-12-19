package edu.bpmanalysis.web.bpmq.entity;

public class BinBPMQualityTuple {
    private int sizeQualityH;
    private int degreesQualityH;
    private int eventsQualityH;
    private int gatewaysQualityH;
    private int orQualityH;

    public BinBPMQualityTuple(int sizeQualityH, int degreesQualityH, int eventsQualityH, int gatewaysQualityH, int orQualityH) {
        this.sizeQualityH = sizeQualityH;
        this.degreesQualityH = degreesQualityH;
        this.eventsQualityH = eventsQualityH;
        this.gatewaysQualityH = gatewaysQualityH;
        this.orQualityH = orQualityH;
    }

    public int getSizeQualityH() {
        return sizeQualityH;
    }

    public void setSizeQualityH(int sizeQualityH) {
        this.sizeQualityH = sizeQualityH;
    }

    public int getDegreesQualityH() {
        return degreesQualityH;
    }

    public void setDegreesQualityH(int degreesQualityH) {
        this.degreesQualityH = degreesQualityH;
    }

    public int getEventsQualityH() {
        return eventsQualityH;
    }

    public void setEventsQualityH(int eventsQualityH) {
        this.eventsQualityH = eventsQualityH;
    }

    public int getGatewaysQualityH() {
        return gatewaysQualityH;
    }

    public void setGatewaysQualityH(int gatewaysQualityH) {
        this.gatewaysQualityH = gatewaysQualityH;
    }

    public int getOrQualityH() {
        return orQualityH;
    }

    public void setOrQualityH(int orQualityH) {
        this.orQualityH = orQualityH;
    }
}
