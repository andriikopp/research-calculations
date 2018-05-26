package bp.retrieve.container.clustering;

import bp.retrieve.collection.GenericProcessModel;

/**
 * Represents clustering results of business process models.
 *
 * @author Andrii Kopp
 */
public class ProcessModelCloseness implements Comparable<ProcessModelCloseness> {
    private GenericProcessModel processModel;
    private double closenessValue;

    public ProcessModelCloseness(GenericProcessModel processModel, double closenessValue) {
        this.processModel = processModel;
        this.closenessValue = closenessValue;
    }

    public GenericProcessModel getProcessModel() {
        return processModel;
    }

    public void setProcessModel(GenericProcessModel processModel) {
        this.processModel = processModel;
    }

    public double getClosenessValue() {
        return closenessValue;
    }

    public void setClosenessValue(double closenessValue) {
        this.closenessValue = closenessValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessModelCloseness that = (ProcessModelCloseness) o;

        if (Double.compare(that.closenessValue, closenessValue) != 0) return false;
        return processModel != null ? processModel.equals(that.processModel) : that.processModel == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = processModel != null ? processModel.hashCode() : 0;
        temp = Double.doubleToLongBits(closenessValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ProcessModelCloseness{" +
                "processModel=" + processModel +
                ", closenessValue=" + closenessValue +
                '}';
    }

    @Override
    public int compareTo(ProcessModelCloseness processModelCloseness) {
        return Double.compare(closenessValue, processModelCloseness.closenessValue);
    }
}
