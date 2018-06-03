package bp.retrieve.generator;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.similarity.*;
import bp.storing.BPModelValidator;
import j2html.tags.ContainerTag;

import java.util.*;

import static j2html.TagCreator.*;

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

    public static class DataStorage extends Process {

        public DataStorage(String identifier) {
            super(identifier);
        }
    }

    public static class ExternalEntity extends Process {

        public ExternalEntity(String identifier) {
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

    public static DataStorage dataStorage(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_DATA_STORAGE);

        return new DataStorage(identifier);
    }

    public static ExternalEntity externalEntity(BPModelRDFGraph rdfGraph, String identifier) {
        rdfGraph.addStatement(identifier, BPModelValidator.PR_TYPE, BPModelValidator.RES_EXTERNAL_ENTITY);

        return new ExternalEntity(identifier);
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

    public static void contains(BPModelRDFGraph rdfGraph, Process process, FlowObject... flowObjects) {
        for (FlowObject flowObject : flowObjects)
            rdfGraph.addStatement(process.getIdentifier(), BPModelValidator.PR_CONTAINS, flowObject.getIdentifier());
    }

    public static void usedBy(BPModelRDFGraph rdfGraph, SupportingSystem supportingSystem, Function... functions) {
        for (Function function : functions)
            rdfGraph.addStatement(supportingSystem.getIdentifier(), BPModelValidator.PR_USED_BY, function.getIdentifier());
    }

    public static void executes(BPModelRDFGraph rdfGraph, OrganizationalUnit organizationalUnit, Function... functions) {
        for (Function function : functions)
            rdfGraph.addStatement(organizationalUnit.getIdentifier(), BPModelValidator.PR_EXECUTES, function.getIdentifier());
    }

    public static void isInputFor(BPModelRDFGraph rdfGraph, BusinessObject businessObject, Function... functions) {
        for (Function function : functions)
            rdfGraph.addStatement(businessObject.getIdentifier(), BPModelValidator.PR_IS_INPUT_FOR, function.getIdentifier());
    }

    public static void isOutputOf(BPModelRDFGraph rdfGraph, BusinessObject businessObject, Function... functions) {
        for (Function function : functions)
            rdfGraph.addStatement(businessObject.getIdentifier(), BPModelValidator.PR_IS_OUTPUT_OF, function.getIdentifier());
    }

    public static void isRegulationOf(BPModelRDFGraph rdfGraph, Information information, Function... functions) {
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

    /* Extract */
    public static Set<String> extractOrganizationalUnits(BPModelRDFGraph rdfGraph) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getPredicate().equals(BPModelValidator.PR_TYPE) &&
                    (statement.getObject().equals(BPModelValidator.RES_ROLE) ||
                            statement.getObject().equals(BPModelValidator.RES_DEPARTMENT)))
                result.add(statement.getSubject());

        return result;
    }

    public static Set<String> extractSupportingSystems(BPModelRDFGraph rdfGraph) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getPredicate().equals(BPModelValidator.PR_TYPE) &&
                    statement.getObject().equals(BPModelValidator.RES_SUPPORTING_SYSTEM))
                result.add(statement.getSubject());

        return result;
    }

    public static Set<String> extractFlowObjects(BPModelRDFGraph rdfGraph) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getPredicate().equals(BPModelValidator.PR_TYPE) &&
                    (statement.getObject().equals(BPModelValidator.RES_FUNCTION) ||
                            statement.getObject().equals(BPModelValidator.RES_PROCESS) ||
                            statement.getObject().equals(BPModelValidator.RES_DATA_STORAGE) ||
                            statement.getObject().equals(BPModelValidator.RES_EXTERNAL_ENTITY) ||
                            statement.getObject().equals(BPModelValidator.RES_EVENT) ||
                            statement.getObject().equals(BPModelValidator.RES_GATEWAY)))
                result.add(statement.getSubject());

        return result;
    }

    public static Set<String> extractLabeledFlowObjects(BPModelRDFGraph rdfGraph) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getPredicate().equals(BPModelValidator.PR_TYPE) &&
                    (statement.getObject().equals(BPModelValidator.RES_FUNCTION) ||
                            statement.getObject().equals(BPModelValidator.RES_PROCESS) ||
                            statement.getObject().equals(BPModelValidator.RES_DATA_STORAGE) ||
                            statement.getObject().equals(BPModelValidator.RES_EXTERNAL_ENTITY) ||
                            statement.getObject().equals(BPModelValidator.RES_EVENT)))
                // If not BPMN-specific start/end event.
                if (!statement.getSubject().equals(BPModelValidator.BPMN_START_EVENT) &&
                        !statement.getSubject().equals(BPModelValidator.BPMN_END_EVENT))
                    result.add(statement.getSubject());

        return result;
    }

    public static Set<String> extractBusinessObjects(BPModelRDFGraph rdfGraph) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getPredicate().equals(BPModelValidator.PR_TYPE) &&
                    (statement.getObject().equals(BPModelValidator.RES_INFORMATION) ||
                            statement.getObject().equals(BPModelValidator.RES_MATERIAL) ||
                            statement.getObject().equals(BPModelValidator.RES_RESOURCE)))
                result.add(statement.getSubject());

        return result;
    }

    public static Set<String> extractRelatedToOrganizationalUnit(BPModelRDFGraph rdfGraph, OrganizationalUnit organizationalUnit) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getSubject().equals(organizationalUnit.getIdentifier()) &&
                    statement.getPredicate().equals(BPModelValidator.PR_EXECUTES))
                result.add(statement.getObject());

        return result;
    }

    public static Set<String> extractRelatedToSupportingSystem(BPModelRDFGraph rdfGraph, SupportingSystem supportingSystem) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getSubject().equals(supportingSystem.getIdentifier()) &&
                    statement.getPredicate().equals(BPModelValidator.PR_USED_BY))
                result.add(statement.getObject());

        return result;
    }

    public static Set<String> extractRelatedToFlowObject(BPModelRDFGraph rdfGraph, FlowObject flowObject) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getSubject().equals(flowObject.getIdentifier()) &&
                    (statement.getPredicate().equals(BPModelValidator.PR_TRIGGERS) ||
                            statement.getPredicate().equals(BPModelValidator.PR_CONTAINS)))
                result.add(statement.getObject());

        return result;
    }

    public static Set<String> extractRelatedToBusinessObject(BPModelRDFGraph rdfGraph, BusinessObject businessObject) {
        Set<String> result = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getSubject().equals(businessObject.getIdentifier()) &&
                    (statement.getPredicate().equals(BPModelValidator.PR_IS_INPUT_FOR) ||
                            statement.getPredicate().equals(BPModelValidator.PR_IS_OUTPUT_OF) ||
                            statement.getPredicate().equals(BPModelValidator.PR_IS_REGULATION_OF)))
                result.add(statement.getObject());

        return result;
    }

    public static Set<String> extractSubjects(BPModelRDFGraph rdfGraph) {
        Set<String> subjects = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            subjects.add(statement.getSubject());

        return subjects;
    }

    public static Set<String> extractPredicates(BPModelRDFGraph rdfGraph, String subject) {
        Set<String> predicates = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getSubject().equals(subject))
                predicates.add(statement.getPredicate());

        return predicates;
    }

    public static Set<String> extractObjects(BPModelRDFGraph rdfGraph, String subject, String predicate) {
        Set<String> objects = new HashSet<>();

        for (BPModelRDFGraph.BPModelRDFStatement statement : rdfGraph.getStatements())
            if (statement.getSubject().equals(subject) && statement.getPredicate().equals(predicate))
                objects.add(statement.getObject());

        return objects;
    }

    public static Map<String, Map<String, Set<String>>> extractStructure(BPModelRDFGraph rdfGraph) {
        Map<String, Map<String, Set<String>>> structure = new HashMap<>();

        for (String subject : extractSubjects(rdfGraph)) {
            Map<String, Set<String>> relatedObjects = new LinkedHashMap<>();

            for (String predicate : extractPredicates(rdfGraph, subject))
                if (!predicate.equals(BPModelValidator.PR_TYPE))
                    relatedObjects.put(predicate, extractObjects(rdfGraph, subject, predicate));

            structure.put(subject, relatedObjects);
        }

        return structure;
    }

    public static String extractType(BPModelRDFGraph rdfGraph, String subject) {
        return (String) extractObjects(rdfGraph, subject, BPModelValidator.PR_TYPE).toArray()[0];
    }

    /* Label similarity */
    public static double organizationalUnitsLabelSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        return Similarity.similarity(extractOrganizationalUnits(first), extractOrganizationalUnits(second), similarity);
    }

    public static double supportingSystemsLabelSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        return Similarity.similarity(extractSupportingSystems(first), extractSupportingSystems(second), similarity);
    }

    public static double flowObjectsLabelSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        // Without gateways and BPMN-specific start/end events.
        return Similarity.similarity(extractLabeledFlowObjects(first), extractLabeledFlowObjects(second), similarity);
    }

    public static double businessObjectsLabelSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        return Similarity.similarity(extractBusinessObjects(first), extractBusinessObjects(second), similarity);
    }

    /* Structure similarity */
    public static double organizationalUnitsStructureSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        Set<String> firstObjects = new HashSet<String>();
        Set<String> secondObjects = new HashSet<String>();

        Set<String> intersection = Similarity.intersection(extractOrganizationalUnits(first), extractOrganizationalUnits(second));

        for (String object : intersection) {
            firstObjects.addAll(extractRelatedToOrganizationalUnit(first, new OrganizationalUnit(object)));
        }

        for (String object : intersection) {
            secondObjects.addAll(extractRelatedToOrganizationalUnit(second, new OrganizationalUnit(object)));
        }

        return Similarity.similarity(firstObjects, secondObjects, similarity);
    }

    public static double supportingSystemsStructureSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        Set<String> firstObjects = new HashSet<String>();
        Set<String> secondObjects = new HashSet<String>();

        Set<String> intersection = Similarity.intersection(extractSupportingSystems(first), extractSupportingSystems(second));

        for (String object : intersection) {
            firstObjects.addAll(extractRelatedToSupportingSystem(first, new SupportingSystem(object)));
        }

        for (String object : intersection) {
            secondObjects.addAll(extractRelatedToSupportingSystem(second, new SupportingSystem(object)));
        }

        return Similarity.similarity(firstObjects, secondObjects, similarity);
    }

    public static double flowObjectsStructureSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        Set<String> firstObjects = new HashSet<String>();
        Set<String> secondObjects = new HashSet<String>();

        // With all process flow objects.
        Set<String> intersection = Similarity.intersection(extractFlowObjects(first), extractFlowObjects(second));

        for (String object : intersection) {
            firstObjects.addAll(extractRelatedToFlowObject(first, new FlowObject(object)));
        }

        for (String object : intersection) {
            secondObjects.addAll(extractRelatedToFlowObject(second, new FlowObject(object)));
        }

        return Similarity.similarity(firstObjects, secondObjects, similarity);
    }

    public static double businessObjectsStructureSimilarity(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        Set<String> firstObjects = new HashSet<String>();
        Set<String> secondObjects = new HashSet<String>();

        Set<String> intersection = Similarity.intersection(extractBusinessObjects(first), extractBusinessObjects(second));

        for (String object : intersection) {
            firstObjects.addAll(extractRelatedToBusinessObject(first, new BusinessObject(object)));
        }

        for (String object : intersection) {
            secondObjects.addAll(extractRelatedToBusinessObject(second, new BusinessObject(object)));
        }

        return Similarity.similarity(firstObjects, secondObjects, similarity);
    }

    /* Total similarity */
    public static double[] similarities(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        List<Double> usedSimilarities = new ArrayList<>();

        final int numberOfSimilarities = 4;
        boolean[] isSimilarityUsed = new boolean[numberOfSimilarities];

        if (!extractOrganizationalUnits(first).isEmpty() && !extractOrganizationalUnits(second).isEmpty()) {
            usedSimilarities.add(organizationalUnitsLabelSimilarity(first, second, similarity));
            isSimilarityUsed[0] = true;
        }

        if (!extractSupportingSystems(first).isEmpty() && !extractSupportingSystems(second).isEmpty()) {
            usedSimilarities.add(supportingSystemsLabelSimilarity(first, second, similarity));
            isSimilarityUsed[1] = true;
        }

        if (!extractFlowObjects(first).isEmpty() && !extractFlowObjects(second).isEmpty()) {
            usedSimilarities.add(flowObjectsLabelSimilarity(first, second, similarity));
            isSimilarityUsed[2] = true;
        }

        if (!extractBusinessObjects(first).isEmpty() && !extractBusinessObjects(second).isEmpty()) {
            usedSimilarities.add(businessObjectsLabelSimilarity(first, second, similarity));
            isSimilarityUsed[3] = true;
        }

        if (isSimilarityUsed[0])
            usedSimilarities.add(organizationalUnitsStructureSimilarity(first, second, similarity));

        if (isSimilarityUsed[1])
            usedSimilarities.add(supportingSystemsStructureSimilarity(first, second, similarity));

        if (isSimilarityUsed[2])
            usedSimilarities.add(flowObjectsStructureSimilarity(first, second, similarity));

        if (isSimilarityUsed[3])
            usedSimilarities.add(businessObjectsStructureSimilarity(first, second, similarity));

        double[] similarities = new double[usedSimilarities.size()];

        for (int i = 0; i < usedSimilarities.size(); i++)
            similarities[i] = usedSimilarities.get(i);

        return similarities;
    }

    public static class ClosenessResult {
        private double[] weights;
        private double[] similarities;
        private double value;

        public ClosenessResult(double[] weights, double[] similarities, double value) {
            this.weights = weights;
            this.similarities = similarities;
            this.value = value;
        }

        public double[] getWeights() {
            return weights;
        }

        public double[] getSimilarities() {
            return similarities;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            String result = String.format("%.2f\n", value);

            result += "Weights: ";
            for (double x : weights)
                result += String.format("%.2f ", x);
            result += "\n";

            result += "Similarities: ";
            for (double x : similarities)
                result += String.format("%.2f ", x);
            result += "\n";

            return result;
        }
    }

    public static ClosenessResult closeness(BPModelRDFGraph first, BPModelRDFGraph second, Similarity similarity) {
        double[] similarities = similarities(first, second, similarity);

        return new Object() {
            private double[] averageWeights;

            private SimilarityFunction similarity = (weights, values) -> {
                if (averageWeights == null)
                    averageWeights = new double[weights.length];

                for (int i = 0; i < weights.length; i++)
                    averageWeights[i] += weights[i];

                double measure = 0;

                for (int i = 0; i < weights.length; i++)
                    measure += weights[i] * values[i];

                return measure;
            };

            public ClosenessResult measure() {
                BPModelsSimilarityUtil.directEstimation(similarities, similarity);

                BPModelsSimilarityUtil.fishbernEstimation(similarities, similarity,
                        (index, n) -> BPModelsSimilarityUtil.fishbernFirstEquation(index, n));

                BPModelsSimilarityUtil.fishbernEstimation(similarities, similarity,
                        (index, n) -> BPModelsSimilarityUtil.fishbernSecondEquation(index, n));

                BPModelsSimilarityUtil.pairwiseComparison(similarities, similarity);

                for (int i = 0; i < averageWeights.length; i++)
                    averageWeights[i] /= 4.0;

                BPModelsSimilarityUtil.norm(averageWeights);

                return new ClosenessResult(averageWeights,
                        similarities,
                        BPModelsSimilarityUtil.similarity.measure(averageWeights, similarities));
            }
        }.measure();
    }

    /* Description */
    public static ContainerTag description(BPModelRDFGraph rdfGraph) {
        Map<String, Map<String, Set<String>>> structure = extractStructure(rdfGraph);

        return small(
                table(
                        tbody(
                                each(structure.keySet(), subject -> tr(
                                        td(small(a(subject).withName(subject))),
                                        td(small(extractType(rdfGraph, subject))),
                                        each(structure.get(subject).keySet(), predicate -> td(
                                                table(
                                                        tr(td(small(i(predicate))),
                                                                td(each(structure.get(subject).get(predicate), object ->
                                                                                span(small(a(object).withHref("#" + object)), br())
                                                                        )
                                                                )
                                                        )
                                                )
                                                )
                                        )
                                )
                        )
                )).attr("border", "1")
        );
    }
}
