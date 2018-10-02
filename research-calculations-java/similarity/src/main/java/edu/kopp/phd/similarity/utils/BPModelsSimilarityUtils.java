package main.java.edu.kopp.phd.similarity.utils;

import main.java.edu.kopp.phd.repository.domain.model.GenericModel;
import org.apache.jena.rdf.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class BPModelsSimilarityUtils {

    public static double domainSimilarity(double weight, GenericModel first, GenericModel second, Property property) {
        Map<String, Set<String>> firstStatements = getStatementsProperty(first, property);
        Map<String, Set<String>> secondStatements = getStatementsProperty(second, property);

        Set<String> subjectsUnion = setsUnion(firstStatements.keySet(), secondStatements.keySet());

        double value = 0;

        for (String subject : subjectsUnion) {
            Set<String> firstObjects = firstStatements.get(subject);
            Set<String> secondObjects = secondStatements.get(subject);

            value += setsSimilarity(firstObjects == null ? new HashSet<>() : firstObjects,
                    secondObjects == null ? new HashSet<>() : secondObjects);
        }

        return (weight / (double) subjectsUnion.size()) * value;
    }

    public static double setsSimilarity(Set<String> first, Set<String> second) {
        // If both sets are empty define Jaccard index J(first, second) = 1
        if (first.isEmpty() && second.isEmpty())
            return 1.0;

        return (double) BPModelsSimilarityUtils.setsIntersection(first, second).size() /
                (double) BPModelsSimilarityUtils.setsUnion(first, second).size();
    }

    public static Set<String> setsUnion(Set<String> first, Set<String> second) {
        Set<String> result = new HashSet<String>(first);
        result.addAll(second);
        return result;
    }

    public static Set<String> setsIntersection(Set<String> first, Set<String> second) {
        Set<String> result = new HashSet<String>(first);
        result.retainAll(second);
        return result;
    }

    private static Map<String, Set<String>> getStatementsProperty(GenericModel model, Property property) {
        Map<String, Set<String>> flowObjects = new HashMap<>();

        for (ResIterator iterator = model.getStatements().listSubjects(); iterator.hasNext(); ) {
            Resource resource = iterator.nextResource();

            if (resource.hasProperty(model.getHasLabel())) {
                Set<String> subsequents = new HashSet<>();

                for (StmtIterator stmt = resource.listProperties(property); stmt.hasNext(); ) {
                    subsequents.add(getLabel(model, stmt.nextStatement().getObject()));
                }

                flowObjects.put(getLabel(model, resource), subsequents);
            }
        }

        return flowObjects;
    }

    private static String getLabel(GenericModel model, RDFNode node) {
        for (StmtIterator iterator = model.getStatements().listStatements(); iterator.hasNext(); ) {
            Statement statement = iterator.nextStatement();

            if (statement.getSubject().equals(node)) {
                return statement.getSubject().getProperty(model.getHasLabel()).getObject().asLiteral().getString();
            }

            if (statement.getPredicate().equals(model.getA()) && statement.getObject().equals(node)) {
                return statement.getObject().asLiteral().getString();
            }
        }

        return null;
    }
}
