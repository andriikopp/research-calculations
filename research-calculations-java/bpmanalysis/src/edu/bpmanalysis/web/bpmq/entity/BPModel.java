package edu.bpmanalysis.web.bpmq.entity;

public class BPModel {
    private String fileName;

    private int totalNodes;
    private int invalidNodes;
    private int startEvents;
    private int endEvents;
    private int unmatchedGateways;
    private int totalGateways;
    private int orGateways;

    private int sizeQualityH;
    private int degreesQualityH;
    private int eventsQualityH;
    private int gatewaysQualityH;
    private int orQualityH;

    private double sizeQualityS;
    private double degreesQualityS;
    private double eventsQualityS;
    private double gatewaysQualityS;
    private double orQualityS;

    private double hardQuality;
    private double softQuality;

    public BPModel(String fileName, int totalNodes, int invalidNodes, int startEvents, int endEvents, int unmatchedGateways, int totalGateways, int orGateways, int sizeQualityH, int degreesQualityH, int eventsQualityH, int gatewaysQualityH, int orQualityH, double sizeQualityS, double degreesQualityS, double eventsQualityS, double gatewaysQualityS, double orQualityS, double hardQuality, double softQuality) {
        this.fileName = fileName;
        this.totalNodes = totalNodes;
        this.invalidNodes = invalidNodes;
        this.startEvents = startEvents;
        this.endEvents = endEvents;
        this.unmatchedGateways = unmatchedGateways;
        this.totalGateways = totalGateways;
        this.orGateways = orGateways;
        this.sizeQualityH = sizeQualityH;
        this.degreesQualityH = degreesQualityH;
        this.eventsQualityH = eventsQualityH;
        this.gatewaysQualityH = gatewaysQualityH;
        this.orQualityH = orQualityH;
        this.sizeQualityS = sizeQualityS;
        this.degreesQualityS = degreesQualityS;
        this.eventsQualityS = eventsQualityS;
        this.gatewaysQualityS = gatewaysQualityS;
        this.orQualityS = orQualityS;
        this.hardQuality = hardQuality;
        this.softQuality = softQuality;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }

    public int getInvalidNodes() {
        return invalidNodes;
    }

    public void setInvalidNodes(int invalidNodes) {
        this.invalidNodes = invalidNodes;
    }

    public int getStartEvents() {
        return startEvents;
    }

    public void setStartEvents(int startEvents) {
        this.startEvents = startEvents;
    }

    public int getEndEvents() {
        return endEvents;
    }

    public void setEndEvents(int endEvents) {
        this.endEvents = endEvents;
    }

    public int getUnmatchedGateways() {
        return unmatchedGateways;
    }

    public void setUnmatchedGateways(int unmatchedGateways) {
        this.unmatchedGateways = unmatchedGateways;
    }

    public int getTotalGateways() {
        return totalGateways;
    }

    public void setTotalGateways(int totalGateways) {
        this.totalGateways = totalGateways;
    }

    public int getOrGateways() {
        return orGateways;
    }

    public void setOrGateways(int orGateways) {
        this.orGateways = orGateways;
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

    public double getSizeQualityS() {
        return sizeQualityS;
    }

    public void setSizeQualityS(double sizeQualityS) {
        this.sizeQualityS = sizeQualityS;
    }

    public double getDegreesQualityS() {
        return degreesQualityS;
    }

    public void setDegreesQualityS(double degreesQualityS) {
        this.degreesQualityS = degreesQualityS;
    }

    public double getEventsQualityS() {
        return eventsQualityS;
    }

    public void setEventsQualityS(double eventsQualityS) {
        this.eventsQualityS = eventsQualityS;
    }

    public double getGatewaysQualityS() {
        return gatewaysQualityS;
    }

    public void setGatewaysQualityS(double gatewaysQualityS) {
        this.gatewaysQualityS = gatewaysQualityS;
    }

    public double getOrQualityS() {
        return orQualityS;
    }

    public void setOrQualityS(double orQualityS) {
        this.orQualityS = orQualityS;
    }

    public double getHardQuality() {
        return hardQuality;
    }

    public void setHardQuality(double hardQuality) {
        this.hardQuality = hardQuality;
    }

    public double getSoftQuality() {
        return softQuality;
    }

    public void setSoftQuality(double softQuality) {
        this.softQuality = softQuality;
    }
}
