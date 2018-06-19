package bp.analysis.aris.model;

import static bp.analysis.aris.model.ARISeEPCBusinessModel.AND;
import static bp.analysis.aris.model.ARISeEPCBusinessModel.OR;
import static bp.analysis.aris.model.ARISeEPCBusinessModel.XOR;

public class ARISeEPCGateway extends ARISeEPCFlowObject {

    protected ARISeEPCGateway(String name) {
        super(name);
    }

    public static class ARISeEPCAndGateway extends ARISeEPCGateway {

        private ARISeEPCAndGateway(String name) {
            super(name);
        }

        public static ARISeEPCAndGateway andGateway() {
            return new ARISeEPCAndGateway(AND);
        }

        public static ARISeEPCAndGateway andGateway(String name) {
            return new ARISeEPCAndGateway(name);
        }
    }

    public static class ARISeEPCOrGateway extends ARISeEPCGateway {

        private ARISeEPCOrGateway(String name) {
            super(name);
        }

        public static ARISeEPCOrGateway orGateway() {
            return new ARISeEPCOrGateway(OR);
        }

        public static ARISeEPCOrGateway orGateway(String name) {
            return new ARISeEPCOrGateway(name);
        }
    }

    public static class ARISeEPCXorGateway extends ARISeEPCGateway {

        private ARISeEPCXorGateway(String name) {
            super(name);
        }

        public static ARISeEPCXorGateway xorGateway() {
            return new ARISeEPCXorGateway(XOR);
        }

        public static ARISeEPCXorGateway xorGateway(String name) {
            return new ARISeEPCXorGateway(name);
        }
    }
}
