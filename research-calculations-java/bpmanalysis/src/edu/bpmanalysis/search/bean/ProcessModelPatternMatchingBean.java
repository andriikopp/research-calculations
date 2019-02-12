package edu.bpmanalysis.search.bean;

public class ProcessModelPatternMatchingBean {
    private String subject;
    private String property;
    private String object;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessModelPatternMatchingBean that = (ProcessModelPatternMatchingBean) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (property != null ? !property.equals(that.property) : that.property != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (property != null ? property.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessModelPatternMatchingBean{" +
                "subject='" + subject + '\'' +
                ", property='" + property + '\'' +
                ", object='" + object + '\'' +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
