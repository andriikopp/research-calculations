package edu.kopp.phd.model.flow;

import edu.kopp.phd.model.environment.ApplicationSystem;
import edu.kopp.phd.model.environment.BusinessObject;
import edu.kopp.phd.model.environment.OrganizationalUnit;
import org.apache.jena.rdf.model.Resource;

import java.util.Set;

public class Function extends FlowObject {
    private Set<OrganizationalUnit> organizationalUnits;

    private Set<ApplicationSystem> applicationSystems;

    private Set<BusinessObject> inputs;
    private Set<BusinessObject> outputs;

    public Function(Resource resource) {
        super(resource);
    }

    public Set<OrganizationalUnit> getOrganizationalUnits() {
        return organizationalUnits;
    }

    public void setOrganizationalUnits(Set<OrganizationalUnit> organizationalUnits) {
        this.organizationalUnits = organizationalUnits;
    }

    public Set<ApplicationSystem> getApplicationSystems() {
        return applicationSystems;
    }

    public void setApplicationSystems(Set<ApplicationSystem> applicationSystems) {
        this.applicationSystems = applicationSystems;
    }

    public Set<BusinessObject> getInputs() {
        return inputs;
    }

    public void setInputs(Set<BusinessObject> inputs) {
        this.inputs = inputs;
    }

    public Set<BusinessObject> getOutputs() {
        return outputs;
    }

    public void setOutputs(Set<BusinessObject> outputs) {
        this.outputs = outputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Function function = (Function) o;

        if (organizationalUnits != null ? !organizationalUnits.equals(function.organizationalUnits) : function.organizationalUnits != null)
            return false;
        if (applicationSystems != null ? !applicationSystems.equals(function.applicationSystems) : function.applicationSystems != null)
            return false;
        if (inputs != null ? !inputs.equals(function.inputs) : function.inputs != null) return false;
        return outputs != null ? outputs.equals(function.outputs) : function.outputs == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (organizationalUnits != null ? organizationalUnits.hashCode() : 0);
        result = 31 * result + (applicationSystems != null ? applicationSystems.hashCode() : 0);
        result = 31 * result + (inputs != null ? inputs.hashCode() : 0);
        result = 31 * result + (outputs != null ? outputs.hashCode() : 0);
        return result;
    }
}
