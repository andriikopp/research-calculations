package edu.kopp.phd.service.beans;

public class AnalysisResult {
    private int size;
    private double connectivity;
    private double balance;
    private double errors;
    private double shortcomings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisResult that = (AnalysisResult) o;

        if (size != that.size) return false;
        if (Double.compare(that.connectivity, connectivity) != 0) return false;
        if (Double.compare(that.balance, balance) != 0) return false;
        if (Double.compare(that.errors, errors) != 0) return false;
        return Double.compare(that.shortcomings, shortcomings) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = size;
        temp = Double.doubleToLongBits(connectivity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(balance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(errors);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(shortcomings);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d\t%.2f\t%.2f\t%.0f\t%.0f\n",
                size, connectivity, balance, errors, shortcomings);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(double connectivity) {
        this.connectivity = connectivity;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getErrors() {
        return errors;
    }

    public void setErrors(double errors) {
        this.errors = errors;
    }

    public double getShortcomings() {
        return shortcomings;
    }

    public void setShortcomings(double shortcomings) {
        this.shortcomings = shortcomings;
    }
}
