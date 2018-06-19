package bp.analysis.aris.model;

public class ARISeEPCApplicationSystem {
    private String name;

    private ARISeEPCApplicationSystem(String name) {
        this.name = name;
    }

    public static ARISeEPCApplicationSystem applicationSystem(String name) {
        return new ARISeEPCApplicationSystem(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ARISeEPCApplicationSystem that = (ARISeEPCApplicationSystem) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ARISeEPCApplicationSystem{" +
                "name='" + name + '\'' +
                '}';
    }
}
