package bp.analysis.aris.model;

import java.util.Arrays;

public class ARISeEPCProcess extends ARISeEPCFunction {
    private ARISeEPCFlowObject[] flowObjects;

    private ARISeEPCProcess(String name) {
        super(name);
    }

    public void isComposedBy(ARISeEPCFlowObject... flowObjects) {
        this.flowObjects = new ARISeEPCFlowObject[flowObjects.length];

        for (int i = 0; i < flowObjects.length; i++)
            this.flowObjects[i] = flowObjects[i];
    }

    public static ARISeEPCProcess process(String name) {
        return new ARISeEPCProcess(name);
    }

    public ARISeEPCFlowObject[] getFlowObjects() {
        return flowObjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ARISeEPCProcess that = (ARISeEPCProcess) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(flowObjects, that.flowObjects);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(flowObjects);
        return result;
    }

    @Override
    public String toString() {
        return "ARISeEPCProcess{" +
                "flowObjects=" + Arrays.toString(flowObjects) +
                '}';
    }
}
