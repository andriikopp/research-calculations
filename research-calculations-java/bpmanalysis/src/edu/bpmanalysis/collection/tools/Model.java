package edu.bpmanalysis.collection.tools;

import edu.bpmanalysis.metamodel.Node;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String name;
    private ModelType modelType;
    private List<Node> nodesList;

    public enum ModelType {
        EPC, BPMN, DFD, IDEF0
    }

    public Node.ArcType[] getArcTypes() {
        if (modelType.equals(ModelType.IDEF0)) {
            return Node.arcTypes;
        }

        return new Node.ArcType[]{Node.arcTypes[0], Node.arcTypes[1]};
    }

    private Model(String name, ModelType modelType, List<Node> nodesList) {
        this.name = name;
        this.modelType = modelType;
        this.nodesList = nodesList;
    }

    public static Model createEPCModel(String name) {
        return new Model(name, ModelType.EPC, new ArrayList<>());
    }

    public static Model createBPMNModel(String name) {
        return new Model(name, ModelType.BPMN, new ArrayList<>());
    }

    public static Model createDFDModel(String name) {
        return new Model(name, ModelType.DFD, new ArrayList<>());
    }

    public static Model createIDEF0Model(String name) {
        return new Model(name, ModelType.IDEF0, new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public List<Node> getNodesList() {
        return nodesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (name != null ? !name.equals(model.name) : model.name != null) return false;
        if (modelType != model.modelType) return false;
        return nodesList != null ? nodesList.equals(model.nodesList) : model.nodesList == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (modelType != null ? modelType.hashCode() : 0);
        result = 31 * result + (nodesList != null ? nodesList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", modelType=" + modelType +
                ", nodesList=" + nodesList +
                '}';
    }
}
