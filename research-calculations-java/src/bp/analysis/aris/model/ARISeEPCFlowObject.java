package bp.analysis.aris.model;

import java.util.Arrays;

public class ARISeEPCFlowObject {
    protected String name;

    private ARISeEPCFlowObject[] subsequent;

    protected ARISeEPCFlowObject(String name) {
        this.name = name;
    }

    public void isPredecessorOf(ARISeEPCFlowObject... flowObjects) {
        subsequent = new ARISeEPCFlowObject[flowObjects.length];

        for (int i = 0; i < flowObjects.length; i++)
            subsequent[i] = flowObjects[i];
    }

    public String getName() {
        return name;
    }

    public ARISeEPCFlowObject[] getSubsequent() {
        return subsequent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ARISeEPCFlowObject that = (ARISeEPCFlowObject) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(subsequent, that.subsequent);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(subsequent);
        return result;
    }

    @Override
    public String toString() {
        return "ARISeEPCFlowObject{" +
                "name='" + name + '\'' +
                ", subsequent=" + Arrays.toString(subsequent) +
                '}';
    }
}
