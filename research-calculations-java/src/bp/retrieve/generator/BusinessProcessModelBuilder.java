package bp.retrieve.generator;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.similarity.SemanticSimilarityUtil;
import bp.storing.BPModelValidator;

/**
 * Business process model builder is a simplified and enhanced version of {@link ProcessModelGenerator}.
 *
 * @author Andrii Kopp
 */
public class BusinessProcessModelBuilder {
    /* Ontology */
    public static class Concept {
        private String identifier;

        public Concept(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Concept that = (Concept) o;

            return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
        }

        @Override
        public int hashCode() {
            return identifier != null ? identifier.hashCode() : 0;
        }

        @Override
        public String toString() {
            return identifier;
        }
    }

    public static class FlowObject extends Concept {

        public FlowObject(String identifier) {
            super(identifier);
        }
    }

    public static class Event extends FlowObject {

        public Event(String identifier) {
            super(identifier);
        }
    }

    public static class Gateway extends FlowObject {

        public Gateway(String identifier) {
            super(identifier);
        }
    }

    public static class Function extends FlowObject {

        public Function(String identifier) {
            super(identifier);
        }
    }

    public static class Process extends Function {

        public Process(String identifier) {
            super(identifier);
        }
    }

    public static class SupportingSystem extends Concept {

        public SupportingSystem(String identifier) {
            super(identifier);
        }
    }

    public static class OrganizationalUnit extends Concept {

        public OrganizationalUnit(String identifier) {
            super(identifier);
        }
    }

    public static class Role extends OrganizationalUnit {

        public Role(String identifier) {
            super(identifier);
        }
    }

    public static class Department extends OrganizationalUnit {

        public Department(String identifier) {
            super(identifier);
        }
    }

    public static class BusinessObject extends Concept {

        public BusinessObject(String identifier) {
            super(identifier);
        }
    }

    public static class Resource extends BusinessObject {

        public Resource(String identifier) {
            super(identifier);
        }
    }

    public static class Material extends BusinessObject {

        public Material(String identifier) {
            super(identifier);
        }
    }

    public static class Information extends BusinessObject {

        public Information(String identifier) {
            super(identifier);
        }
    }

    /* Elements */
    public static Function function(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);

        return new Function(identifier);
    }

    public static Event event(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_EVENT);

        return new Event(identifier);
    }

    public static Event startEvent(BPModelRDFGraph rdfGraph) {
        rdfGraph.addStatement(BPModelValidator.BPMN_START_EVENT, BPModelValidator.PR_TYPE, BPModelValidator.RES_EVENT);

        return new Event(BPModelValidator.BPMN_START_EVENT);
    }

    public static Event endEvent(BPModelRDFGraph rdfGraph) {
        rdfGraph.addStatement(BPModelValidator.BPMN_END_EVENT, BPModelValidator.PR_TYPE, BPModelValidator.RES_EVENT);

        return new Event(BPModelValidator.BPMN_END_EVENT);
    }

    public static Gateway andGateway(BPModelRDFGraph rdfGraph) {
        rdfGraph.addStatement(BPModelValidator.AND_GATEWAY, BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);

        return new Gateway(BPModelValidator.AND_GATEWAY);
    }

    public static Gateway orGateway(BPModelRDFGraph rdfGraph) {
        rdfGraph.addStatement(BPModelValidator.OR_GATEWAY, BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);

        return new Gateway(BPModelValidator.OR_GATEWAY);
    }

    public static Gateway xorGateway(BPModelRDFGraph rdfGraph) {
        rdfGraph.addStatement(BPModelValidator.XOR_GATEWAY, BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);

        return new Gateway(BPModelValidator.XOR_GATEWAY);
    }

    public static Process process(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_PROCESS);

        return new Process(identifier);
    }

    public static SupportingSystem supportingSystem(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_SUPPORTING_SYSTEM);

        return new SupportingSystem(identifier);
    }

    public static Role role(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_ROLE);

        return new Role(identifier);
    }

    public static Department department(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_DEPARTMENT);

        return new Department(identifier);
    }

    public static Resource resource(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_RESOURCE);

        return new Resource(identifier);
    }

    public static Material material(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_MATERIAL);

        return new Material(identifier);
    }

    public static Information information(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_INFORMATION);

        return new Information(identifier);
    }

    /* Relations */
    public static void triggers(BPModelRDFGraph rdfGraph, FlowObject subject, FlowObject... objects) {
        for (FlowObject flowObject : objects)
            rdfGraph.addStatement(subject.getIdentifier(), BPModelValidator.PR_TRIGGERS, flowObject.getIdentifier());
    }

    public static void contains(BPModelRDFGraph rdfGraph, Process process, Function... functions) {
        for (Function function : functions)
            rdfGraph.addStatement(process.getIdentifier(), BPModelValidator.PR_CONTAINS, function.getIdentifier());
    }

    public static void usedBy(BPModelRDFGraph rdfGraph, SupportingSystem supportingSystem, Function[] functions) {
        for (Function function : functions)
            rdfGraph.addStatement(supportingSystem.getIdentifier(), BPModelValidator.PR_USED_BY, function.getIdentifier());
    }

    public static void executes(BPModelRDFGraph rdfGraph, OrganizationalUnit organizationalUnit, Function[] functions) {
        for (Function function : functions)
            rdfGraph.addStatement(organizationalUnit.getIdentifier(), BPModelValidator.PR_EXECUTES, function.getIdentifier());
    }

    public static void isInputFor(BPModelRDFGraph rdfGraph, BusinessObject businessObject, Function[] functions) {
        for (Function function : functions)
            rdfGraph.addStatement(businessObject.getIdentifier(), BPModelValidator.PR_IS_INPUT_FOR, function.getIdentifier());
    }

    public static void isOutputOf(BPModelRDFGraph rdfGraph, BusinessObject businessObject, Function[] functions) {
        for (Function function : functions)
            rdfGraph.addStatement(businessObject.getIdentifier(), BPModelValidator.PR_IS_OUTPUT_OF, function.getIdentifier());
    }

    public static void isRegulationOf(BPModelRDFGraph rdfGraph, Information information, Function[] functions) {
        for (Function function : functions)
            rdfGraph.addStatement(information.getIdentifier(), BPModelValidator.PR_IS_REGULATION_OF, function.getIdentifier());
    }

    /* Processing */
    public static BPModelRDFGraph normalized(BPModelRDFGraph rdfGraph) {
        return SemanticSimilarityUtil.normalizeBPModel(rdfGraph);
    }

    public static String triples(BPModelRDFGraph rdfGraph) {
        String result = "";

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            result += statement.getSubject() + "\t" + statement.getPredicate() + "\t" + statement.getObject() + " .\n";

        return result;
    }

    public static GenericProcessModel wrapped(BPModelRDFGraph rdfGraph) {
        return new GenericProcessModel(null, null, null) {
            @Override
            public BPModelRDFGraph getModelDescription() {
                return rdfGraph;
            }
        };
    }
}
