package edu.bpmanalysis.analysis.bean;

import java.util.Arrays;
import java.util.Map;

public class ProcessModelAnalysisBean {
    private String id;

    private String name;

    private int size;
    private int functions;
    private double connectorsBalance;
    private double functionsBalance;
    private int startEvents;
    private int endEvents;
    private double mismatch;
    private int orConnectors;

    private double[] nodesChanges;
    private Map<String, Double> connectorsChanges;
    private Map<String, double[]> routingChanges;
    private Map<String, double[]> functionsChanges;

    private Map<String, String> similarModels;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessModelAnalysisBean that = (ProcessModelAnalysisBean) o;

        if (size != that.size) return false;
        if (functions != that.functions) return false;
        if (Double.compare(that.connectorsBalance, connectorsBalance) != 0) return false;
        if (Double.compare(that.functionsBalance, functionsBalance) != 0) return false;
        if (startEvents != that.startEvents) return false;
        if (endEvents != that.endEvents) return false;
        if (Double.compare(that.mismatch, mismatch) != 0) return false;
        if (orConnectors != that.orConnectors) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!Arrays.equals(nodesChanges, that.nodesChanges)) return false;
        if (connectorsChanges != null ? !connectorsChanges.equals(that.connectorsChanges) : that.connectorsChanges != null)
            return false;
        if (routingChanges != null ? !routingChanges.equals(that.routingChanges) : that.routingChanges != null)
            return false;
        if (functionsChanges != null ? !functionsChanges.equals(that.functionsChanges) : that.functionsChanges != null)
            return false;
        return similarModels != null ? similarModels.equals(that.similarModels) : that.similarModels == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + functions;
        temp = Double.doubleToLongBits(connectorsBalance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(functionsBalance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + startEvents;
        result = 31 * result + endEvents;
        temp = Double.doubleToLongBits(mismatch);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + orConnectors;
        result = 31 * result + Arrays.hashCode(nodesChanges);
        result = 31 * result + (connectorsChanges != null ? connectorsChanges.hashCode() : 0);
        result = 31 * result + (routingChanges != null ? routingChanges.hashCode() : 0);
        result = 31 * result + (functionsChanges != null ? functionsChanges.hashCode() : 0);
        result = 31 * result + (similarModels != null ? similarModels.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessModelAnalysisBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", functions=" + functions +
                ", connectorsBalance=" + connectorsBalance +
                ", functionsBalance=" + functionsBalance +
                ", startEvents=" + startEvents +
                ", endEvents=" + endEvents +
                ", mismatch=" + mismatch +
                ", orConnectors=" + orConnectors +
                ", nodesChanges=" + Arrays.toString(nodesChanges) +
                ", connectorsChanges=" + connectorsChanges +
                ", routingChanges=" + routingChanges +
                ", functionsChanges=" + functionsChanges +
                ", similarModels=" + similarModels +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFunctions() {
        return functions;
    }

    public void setFunctions(int functions) {
        this.functions = functions;
    }

    public double getConnectorsBalance() {
        return connectorsBalance;
    }

    public void setConnectorsBalance(double connectorsBalance) {
        this.connectorsBalance = connectorsBalance;
    }

    public double getFunctionsBalance() {
        return functionsBalance;
    }

    public void setFunctionsBalance(double functionsBalance) {
        this.functionsBalance = functionsBalance;
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

    public double getMismatch() {
        return mismatch;
    }

    public void setMismatch(double mismatch) {
        this.mismatch = mismatch;
    }

    public int getOrConnectors() {
        return orConnectors;
    }

    public void setOrConnectors(int orConnectors) {
        this.orConnectors = orConnectors;
    }

    public double[] getNodesChanges() {
        return nodesChanges;
    }

    public void setNodesChanges(double[] nodesChanges) {
        this.nodesChanges = nodesChanges;
    }

    public Map<String, Double> getConnectorsChanges() {
        return connectorsChanges;
    }

    public void setConnectorsChanges(Map<String, Double> connectorsChanges) {
        this.connectorsChanges = connectorsChanges;
    }

    public Map<String, double[]> getRoutingChanges() {
        return routingChanges;
    }

    public void setRoutingChanges(Map<String, double[]> routingChanges) {
        this.routingChanges = routingChanges;
    }

    public Map<String, double[]> getFunctionsChanges() {
        return functionsChanges;
    }

    public void setFunctionsChanges(Map<String, double[]> functionsChanges) {
        this.functionsChanges = functionsChanges;
    }

    public Map<String, String> getSimilarModels() {
        return similarModels;
    }

    public void setSimilarModels(Map<String, String> similarModels) {
        this.similarModels = similarModels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
