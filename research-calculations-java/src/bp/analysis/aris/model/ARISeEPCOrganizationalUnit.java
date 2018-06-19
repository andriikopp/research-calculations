package bp.analysis.aris.model;

public class ARISeEPCOrganizationalUnit {
    protected String name;

    protected ARISeEPCOrganizationalUnit(String name) {
        this.name = name;
    }

    public static ARISeEPCOrganizationalUnit organizationalUnit(String name) {
        return new ARISeEPCOrganizationalUnit(name);
    }

    public static class ARISeEPCPosition extends ARISeEPCOrganizationalUnit {

        private ARISeEPCPosition(String name) {
            super(name);
        }

        public static ARISeEPCPosition position(String name) {
            return new ARISeEPCPosition(name);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ARISeEPCOrganizationalUnit that = (ARISeEPCOrganizationalUnit) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ARISeEPCOrganizationalUnit{" +
                "name='" + name + '\'' +
                '}';
    }
}
