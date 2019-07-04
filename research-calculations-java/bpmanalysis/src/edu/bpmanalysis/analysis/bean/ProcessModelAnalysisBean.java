package edu.bpmanalysis.analysis.bean;

import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.Arrays;
import java.util.Map;

@Document(collection = "analysisResults", schemaVersion = "1.0")
public class ProcessModelAnalysisBean {
    @Id
    private String id;

    private String name;
    private String timeStamp;
    private String notation;
    private String level;
    private ProcessModelGraphBean graph;
    private String description;
    private String fileName;

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

    private double quality;
    private double hasErrors;

    private String userName;

    @Override
    public String toString() {
        return "ProcessModelAnalysisBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", notation='" + notation + '\'' +
                ", level='" + level + '\'' +
                ", graph=" + graph +
                ", description='" + description + '\'' +
                ", fileName='" + fileName + '\'' +
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
                ", quality=" + quality +
                ", hasErrors=" + hasErrors +
                ", userName='" + userName + '\'' +
                '}';
    }

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
        if (Double.compare(that.quality, quality) != 0) return false;
        if (Double.compare(that.hasErrors, hasErrors) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (timeStamp != null ? !timeStamp.equals(that.timeStamp) : that.timeStamp != null) return false;
        if (notation != null ? !notation.equals(that.notation) : that.notation != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (graph != null ? !graph.equals(that.graph) : that.graph != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (!Arrays.equals(nodesChanges, that.nodesChanges)) return false;
        if (connectorsChanges != null ? !connectorsChanges.equals(that.connectorsChanges) : that.connectorsChanges != null)
            return false;
        if (routingChanges != null ? !routingChanges.equals(that.routingChanges) : that.routingChanges != null)
            return false;
        if (functionsChanges != null ? !functionsChanges.equals(that.functionsChanges) : that.functionsChanges != null)
            return false;
        if (similarModels != null ? !similarModels.equals(that.similarModels) : that.similarModels != null)
            return false;
        return userName != null ? userName.equals(that.userName) : that.userName == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        result = 31 * result + (notation != null ? notation.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (graph != null ? graph.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
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
        temp = Double.doubleToLongBits(quality);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(hasErrors);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ProcessModelGraphBean getGraph() {
        return graph;
    }

    public void setGraph(ProcessModelGraphBean graph) {
        this.graph = graph;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(double hasErrors) {
        this.hasErrors = hasErrors;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
