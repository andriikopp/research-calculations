package bp.analysis.aris.model;

public class ARISeEPCEvent extends ARISeEPCFlowObject {

    private ARISeEPCEvent(String name) {
        super(name);
    }

    public static ARISeEPCEvent event(String name) {
        return new ARISeEPCEvent(name);
    }
}
