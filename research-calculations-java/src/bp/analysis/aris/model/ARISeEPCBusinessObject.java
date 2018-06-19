package bp.analysis.aris.model;

public class ARISeEPCBusinessObject {
    protected String name;

    protected ARISeEPCBusinessObject(String name) {
        this.name = name;
    }

    public static class ARISeEPCMaterial extends ARISeEPCBusinessObject {

        private ARISeEPCMaterial(String name) {
            super(name);
        }

        public static ARISeEPCMaterial material(String name) {
            return new ARISeEPCMaterial(name);
        }
    }

    public static class ARISeEPCInformation extends ARISeEPCBusinessObject {

        private ARISeEPCInformation(String name) {
            super(name);
        }

        public static ARISeEPCInformation information(String name) {
            return new ARISeEPCInformation(name);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ARISeEPCBusinessObject that = (ARISeEPCBusinessObject) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ARISeEPCBusinessObject{" +
                "name='" + name + '\'' +
                '}';
    }
}
