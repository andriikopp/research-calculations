package bp.analysis.aris.model;

import java.util.Arrays;

public class ARISeEPCFunction extends ARISeEPCFlowObject {
    protected ARISeEPCOrganizationalUnit[] organizationalUnits;
    protected ARISeEPCApplicationSystem[] applicationSystems;
    protected ARISeEPCBusinessObject[] inputs;
    protected ARISeEPCBusinessObject[] outputs;

    protected ARISeEPCFunction(String name) {
        super(name);
    }

    public void isPerformedBy(ARISeEPCOrganizationalUnit... organizationalUnits) {
        this.organizationalUnits = new ARISeEPCOrganizationalUnit[organizationalUnits.length];

        for (int i = 0; i < organizationalUnits.length; i++)
            this.organizationalUnits[i] = organizationalUnits[i];
    }

    public void isSupportedBy(ARISeEPCApplicationSystem... applicationSystems) {
        this.applicationSystems = new ARISeEPCApplicationSystem[applicationSystems.length];

        for (int i = 0; i < applicationSystems.length; i++)
            this.applicationSystems[i] = applicationSystems[i];
    }

    public void requires(ARISeEPCBusinessObject... businessObjects) {
        this.inputs = new ARISeEPCBusinessObject[businessObjects.length];

        for (int i = 0; i < businessObjects.length; i++)
            this.inputs[i] = businessObjects[i];
    }

    public void produces(ARISeEPCBusinessObject... businessObjects) {
        this.outputs = new ARISeEPCBusinessObject[businessObjects.length];

        for (int i = 0; i < businessObjects.length; i++)
            this.outputs[i] = businessObjects[i];
    }

    public static ARISeEPCFunction function(String name) {
        return new ARISeEPCFunction(name);
    }

    public ARISeEPCOrganizationalUnit[] getOrganizationalUnits() {
        return organizationalUnits;
    }

    public ARISeEPCApplicationSystem[] getApplicationSystems() {
        return applicationSystems;
    }

    public ARISeEPCBusinessObject[] getInputs() {
        return inputs;
    }

    public ARISeEPCBusinessObject[] getOutputs() {
        return outputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ARISeEPCFunction function = (ARISeEPCFunction) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(organizationalUnits, function.organizationalUnits)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(applicationSystems, function.applicationSystems)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(inputs, function.inputs)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(outputs, function.outputs);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(organizationalUnits);
        result = 31 * result + Arrays.hashCode(applicationSystems);
        result = 31 * result + Arrays.hashCode(inputs);
        result = 31 * result + Arrays.hashCode(outputs);
        return result;
    }

    @Override
    public String toString() {
        return "ARISeEPCFunction{" +
                "organizationalUnits=" + Arrays.toString(organizationalUnits) +
                ", applicationSystems=" + Arrays.toString(applicationSystems) +
                ", inputs=" + Arrays.toString(inputs) +
                ", outputs=" + Arrays.toString(outputs) +
                '}';
    }
}
