package edu.bpmanalysis.web.bpmq.entity;

public class BPMMeasures {
    public int totalNodes;
    public int sequenceFlows;

    public int tasks;

    public int startEvents;
    public int endEvents;
    public int intermediateEvents;

    public int andGateways;
    public int orGateways;
    public int xorGateways;

    @Override
    public String toString() {
        return "\t" + totalNodes +
                "\t" + sequenceFlows +
                "\t" + tasks +
                "\t" + startEvents +
                "\t" + endEvents +
                "\t" + intermediateEvents +
                "\t" + andGateways +
                "\t" + orGateways +
                "\t" + xorGateways;
    }
}
