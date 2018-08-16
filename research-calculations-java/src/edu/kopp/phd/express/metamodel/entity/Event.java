package edu.kopp.phd.express.metamodel.entity;

public class Event extends Node {
    private boolean precedeEvent;
    private boolean precedeOrXorSplit;

    public Event(int preceding, int subsequent) {
        super("", preceding, subsequent);
    }

    public Event(String label, int preceding, int subsequent) {
        super(label, preceding, subsequent);
    }

    public Event(String label, int preceding, int subsequent, boolean precedeEvent, boolean precedeOrXorSplit) {
        super(label, preceding, subsequent);
        this.precedeEvent = precedeEvent;
        this.precedeOrXorSplit = precedeOrXorSplit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Event event = (Event) o;

        if (precedeEvent != event.precedeEvent) return false;
        return precedeOrXorSplit == event.precedeOrXorSplit;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (precedeEvent ? 1 : 0);
        result = 31 * result + (precedeOrXorSplit ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "precedeEvent=" + precedeEvent +
                ", precedeOrXorSplit=" + precedeOrXorSplit +
                '}';
    }

    public boolean isPrecedeEvent() {
        return precedeEvent;
    }

    public void setPrecedeEvent(boolean precedeEvent) {
        this.precedeEvent = precedeEvent;
    }

    public boolean isPrecedeOrXorSplit() {
        return precedeOrXorSplit;
    }

    public void setPrecedeOrXorSplit(boolean precedeOrXorSplit) {
        this.precedeOrXorSplit = precedeOrXorSplit;
    }
}
