package edu.kopp.phd.express.metamodel.entity;

public class Event extends Node {
    private boolean precedesEvent;
    private boolean makesDecision;

    public Event(String label, int preceding, int subsequent) {
        super(label, preceding, subsequent);
    }

    public Event(String label, int preceding, int subsequent, boolean precedesEvent, boolean makesDecision) {
        super(label, preceding, subsequent);
        this.precedesEvent = precedesEvent;
        this.makesDecision = makesDecision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Event event = (Event) o;

        if (precedesEvent != event.precedesEvent) return false;
        return makesDecision == event.makesDecision;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (precedesEvent ? 1 : 0);
        result = 31 * result + (makesDecision ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "precedesEvent=" + precedesEvent +
                ", makesDecision=" + makesDecision +
                '}';
    }

    public boolean isPrecedesEvent() {
        return precedesEvent;
    }

    public void setPrecedesEvent(boolean precedesEvent) {
        this.precedesEvent = precedesEvent;
    }

    public boolean isMakesDecision() {
        return makesDecision;
    }

    public void setMakesDecision(boolean makesDecision) {
        this.makesDecision = makesDecision;
    }
}
