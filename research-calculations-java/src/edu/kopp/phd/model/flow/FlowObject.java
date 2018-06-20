package edu.kopp.phd.model.flow;

import edu.kopp.phd.model.Node;
import org.apache.jena.rdf.model.Resource;

import java.util.Set;

public class FlowObject extends Node {
    private Set<FlowObject> preceding;
    private Set<FlowObject> subsequent;

    public FlowObject(Resource resource) {
        super(resource);
    }

    public Set<FlowObject> getPreceding() {
        return preceding;
    }

    public void setPreceding(Set<FlowObject> preceding) {
        this.preceding = preceding;
    }

    public Set<FlowObject> getSubsequent() {
        return subsequent;
    }

    public void setSubsequent(Set<FlowObject> subsequent) {
        this.subsequent = subsequent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FlowObject that = (FlowObject) o;

        if (preceding != null ? !preceding.equals(that.preceding) : that.preceding != null) return false;
        return subsequent != null ? subsequent.equals(that.subsequent) : that.subsequent == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (preceding != null ? preceding.hashCode() : 0);
        result = 31 * result + (subsequent != null ? subsequent.hashCode() : 0);
        return result;
    }
}
