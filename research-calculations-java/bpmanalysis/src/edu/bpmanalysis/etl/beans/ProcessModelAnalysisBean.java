package edu.bpmanalysis.etl.beans;

import java.util.List;

public class ProcessModelAnalysisBean {
    private String id;
    private String modelId;
    private int notationId;
    private String timeId;
    private int levelId;

    private int elementsNumber;
    private int functionsNumber;
    private int connectorsBalance;
    private int functionsBalance;
    private int startEventsNumber;
    private int endEventsNumber;
    private int connectorsMismatch;
    private int orConnectorsNumber;
    private String textDescription;
    private String graphRepresentation;

    private List<StoredProcessModelBean> storedProcessModelBeanList;

    private int elementsChanges;
    private int functionsChanges;
    private int startEventsChanges;
    private int endEventsChanges;
    private int orConnectorsChanges;

    private int andSplitsChanges;
    private int andJoinsChanges;
    private int orSplitsChanges;
    private int orJoinsChanges;
    private int xorSplitsChanges;
    private int xorJoinsChanges;

    List<SimilarModelBean> similarModelBeans;

    private int hours;
    private int minutes;
    private int seconds;
    private int day;
    private int month;
    private int year;
    private int dayOfWeek;
    private String timeStamp;

    private String name;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessModelAnalysisBean that = (ProcessModelAnalysisBean) o;

        if (notationId != that.notationId) return false;
        if (levelId != that.levelId) return false;
        if (elementsNumber != that.elementsNumber) return false;
        if (functionsNumber != that.functionsNumber) return false;
        if (connectorsBalance != that.connectorsBalance) return false;
        if (functionsBalance != that.functionsBalance) return false;
        if (startEventsNumber != that.startEventsNumber) return false;
        if (endEventsNumber != that.endEventsNumber) return false;
        if (connectorsMismatch != that.connectorsMismatch) return false;
        if (orConnectorsNumber != that.orConnectorsNumber) return false;
        if (elementsChanges != that.elementsChanges) return false;
        if (functionsChanges != that.functionsChanges) return false;
        if (startEventsChanges != that.startEventsChanges) return false;
        if (endEventsChanges != that.endEventsChanges) return false;
        if (orConnectorsChanges != that.orConnectorsChanges) return false;
        if (andSplitsChanges != that.andSplitsChanges) return false;
        if (andJoinsChanges != that.andJoinsChanges) return false;
        if (orSplitsChanges != that.orSplitsChanges) return false;
        if (orJoinsChanges != that.orJoinsChanges) return false;
        if (xorSplitsChanges != that.xorSplitsChanges) return false;
        if (xorJoinsChanges != that.xorJoinsChanges) return false;
        if (hours != that.hours) return false;
        if (minutes != that.minutes) return false;
        if (seconds != that.seconds) return false;
        if (day != that.day) return false;
        if (month != that.month) return false;
        if (year != that.year) return false;
        if (dayOfWeek != that.dayOfWeek) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (modelId != null ? !modelId.equals(that.modelId) : that.modelId != null) return false;
        if (timeId != null ? !timeId.equals(that.timeId) : that.timeId != null) return false;
        if (textDescription != null ? !textDescription.equals(that.textDescription) : that.textDescription != null)
            return false;
        if (graphRepresentation != null ? !graphRepresentation.equals(that.graphRepresentation) : that.graphRepresentation != null)
            return false;
        if (storedProcessModelBeanList != null ? !storedProcessModelBeanList.equals(that.storedProcessModelBeanList) : that.storedProcessModelBeanList != null)
            return false;
        if (similarModelBeans != null ? !similarModelBeans.equals(that.similarModelBeans) : that.similarModelBeans != null)
            return false;
        if (timeStamp != null ? !timeStamp.equals(that.timeStamp) : that.timeStamp != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (modelId != null ? modelId.hashCode() : 0);
        result = 31 * result + notationId;
        result = 31 * result + (timeId != null ? timeId.hashCode() : 0);
        result = 31 * result + levelId;
        result = 31 * result + elementsNumber;
        result = 31 * result + functionsNumber;
        result = 31 * result + connectorsBalance;
        result = 31 * result + functionsBalance;
        result = 31 * result + startEventsNumber;
        result = 31 * result + endEventsNumber;
        result = 31 * result + connectorsMismatch;
        result = 31 * result + orConnectorsNumber;
        result = 31 * result + (textDescription != null ? textDescription.hashCode() : 0);
        result = 31 * result + (graphRepresentation != null ? graphRepresentation.hashCode() : 0);
        result = 31 * result + (storedProcessModelBeanList != null ? storedProcessModelBeanList.hashCode() : 0);
        result = 31 * result + elementsChanges;
        result = 31 * result + functionsChanges;
        result = 31 * result + startEventsChanges;
        result = 31 * result + endEventsChanges;
        result = 31 * result + orConnectorsChanges;
        result = 31 * result + andSplitsChanges;
        result = 31 * result + andJoinsChanges;
        result = 31 * result + orSplitsChanges;
        result = 31 * result + orJoinsChanges;
        result = 31 * result + xorSplitsChanges;
        result = 31 * result + xorJoinsChanges;
        result = 31 * result + (similarModelBeans != null ? similarModelBeans.hashCode() : 0);
        result = 31 * result + hours;
        result = 31 * result + minutes;
        result = 31 * result + seconds;
        result = 31 * result + day;
        result = 31 * result + month;
        result = 31 * result + year;
        result = 31 * result + dayOfWeek;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessModelAnalysisBean{" +
                "id='" + id + '\'' +
                ", modelId='" + modelId + '\'' +
                ", notationId=" + notationId +
                ", timeId='" + timeId + '\'' +
                ", levelId=" + levelId +
                ", elementsNumber=" + elementsNumber +
                ", functionsNumber=" + functionsNumber +
                ", connectorsBalance=" + connectorsBalance +
                ", functionsBalance=" + functionsBalance +
                ", startEventsNumber=" + startEventsNumber +
                ", endEventsNumber=" + endEventsNumber +
                ", connectorsMismatch=" + connectorsMismatch +
                ", orConnectorsNumber=" + orConnectorsNumber +
                ", textDescription='" + textDescription + '\'' +
                ", graphRepresentation='" + graphRepresentation + '\'' +
                ", storedProcessModelBeanList=" + storedProcessModelBeanList +
                ", elementsChanges=" + elementsChanges +
                ", functionsChanges=" + functionsChanges +
                ", startEventsChanges=" + startEventsChanges +
                ", endEventsChanges=" + endEventsChanges +
                ", orConnectorsChanges=" + orConnectorsChanges +
                ", andSplitsChanges=" + andSplitsChanges +
                ", andJoinsChanges=" + andJoinsChanges +
                ", orSplitsChanges=" + orSplitsChanges +
                ", orJoinsChanges=" + orJoinsChanges +
                ", xorSplitsChanges=" + xorSplitsChanges +
                ", xorJoinsChanges=" + xorJoinsChanges +
                ", similarModelBeans=" + similarModelBeans +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", dayOfWeek=" + dayOfWeek +
                ", timeStamp='" + timeStamp + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getNotationId() {
        return notationId;
    }

    public void setNotationId(int notationId) {
        this.notationId = notationId;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getElementsNumber() {
        return elementsNumber;
    }

    public void setElementsNumber(int elementsNumber) {
        this.elementsNumber = elementsNumber;
    }

    public int getFunctionsNumber() {
        return functionsNumber;
    }

    public void setFunctionsNumber(int functionsNumber) {
        this.functionsNumber = functionsNumber;
    }

    public int getConnectorsBalance() {
        return connectorsBalance;
    }

    public void setConnectorsBalance(int connectorsBalance) {
        this.connectorsBalance = connectorsBalance;
    }

    public int getFunctionsBalance() {
        return functionsBalance;
    }

    public void setFunctionsBalance(int functionsBalance) {
        this.functionsBalance = functionsBalance;
    }

    public int getStartEventsNumber() {
        return startEventsNumber;
    }

    public void setStartEventsNumber(int startEventsNumber) {
        this.startEventsNumber = startEventsNumber;
    }

    public int getEndEventsNumber() {
        return endEventsNumber;
    }

    public void setEndEventsNumber(int endEventsNumber) {
        this.endEventsNumber = endEventsNumber;
    }

    public int getConnectorsMismatch() {
        return connectorsMismatch;
    }

    public void setConnectorsMismatch(int connectorsMismatch) {
        this.connectorsMismatch = connectorsMismatch;
    }

    public int getOrConnectorsNumber() {
        return orConnectorsNumber;
    }

    public void setOrConnectorsNumber(int orConnectorsNumber) {
        this.orConnectorsNumber = orConnectorsNumber;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getGraphRepresentation() {
        return graphRepresentation;
    }

    public void setGraphRepresentation(String graphRepresentation) {
        this.graphRepresentation = graphRepresentation;
    }

    public List<StoredProcessModelBean> getStoredProcessModelBeanList() {
        return storedProcessModelBeanList;
    }

    public void setStoredProcessModelBeanList(List<StoredProcessModelBean> storedProcessModelBeanList) {
        this.storedProcessModelBeanList = storedProcessModelBeanList;
    }

    public int getElementsChanges() {
        return elementsChanges;
    }

    public void setElementsChanges(int elementsChanges) {
        this.elementsChanges = elementsChanges;
    }

    public int getFunctionsChanges() {
        return functionsChanges;
    }

    public void setFunctionsChanges(int functionsChanges) {
        this.functionsChanges = functionsChanges;
    }

    public int getStartEventsChanges() {
        return startEventsChanges;
    }

    public void setStartEventsChanges(int startEventsChanges) {
        this.startEventsChanges = startEventsChanges;
    }

    public int getEndEventsChanges() {
        return endEventsChanges;
    }

    public void setEndEventsChanges(int endEventsChanges) {
        this.endEventsChanges = endEventsChanges;
    }

    public int getOrConnectorsChanges() {
        return orConnectorsChanges;
    }

    public void setOrConnectorsChanges(int orConnectorsChanges) {
        this.orConnectorsChanges = orConnectorsChanges;
    }

    public int getAndSplitsChanges() {
        return andSplitsChanges;
    }

    public void setAndSplitsChanges(int andSplitsChanges) {
        this.andSplitsChanges = andSplitsChanges;
    }

    public int getAndJoinsChanges() {
        return andJoinsChanges;
    }

    public void setAndJoinsChanges(int andJoinsChanges) {
        this.andJoinsChanges = andJoinsChanges;
    }

    public int getOrSplitsChanges() {
        return orSplitsChanges;
    }

    public void setOrSplitsChanges(int orSplitsChanges) {
        this.orSplitsChanges = orSplitsChanges;
    }

    public int getOrJoinsChanges() {
        return orJoinsChanges;
    }

    public void setOrJoinsChanges(int orJoinsChanges) {
        this.orJoinsChanges = orJoinsChanges;
    }

    public int getXorSplitsChanges() {
        return xorSplitsChanges;
    }

    public void setXorSplitsChanges(int xorSplitsChanges) {
        this.xorSplitsChanges = xorSplitsChanges;
    }

    public int getXorJoinsChanges() {
        return xorJoinsChanges;
    }

    public void setXorJoinsChanges(int xorJoinsChanges) {
        this.xorJoinsChanges = xorJoinsChanges;
    }

    public List<SimilarModelBean> getSimilarModelBeans() {
        return similarModelBeans;
    }

    public void setSimilarModelBeans(List<SimilarModelBean> similarModelBeans) {
        this.similarModelBeans = similarModelBeans;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
