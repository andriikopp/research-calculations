package edu.kopp.phd.service.similarity.impl;

import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.service.similarity.api.SimilarityMethod;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.HashSet;
import java.util.Set;

public class RDFDefaultSimilarityMethod implements SimilarityMethod {

    @Override
    public double calculateSimilarityMeasure(Process process, Process pattern) {
        Set<String> subjectsUnion = setUnion(getProcessSubjects(process),
                getProcessSubjects(pattern));

        if (subjectsUnion.isEmpty())
            return 0;

        double sumOfPredicatesAndObjectsSimilarities = 0;

        for (String subjectName : subjectsUnion)
            sumOfPredicatesAndObjectsSimilarities +=
                    setSimilarity(getProcessPredicatesBySubject(subjectName, process),
                            getProcessPredicatesBySubject(subjectName, pattern)) +
                            setSimilarity(getProcessObjectsBySubject(subjectName, process),
                                    getProcessObjectsBySubject(subjectName, pattern));

        double similarity = sumOfPredicatesAndObjectsSimilarities / (2.0 * (double) subjectsUnion.size());

        return similarity;
    }

    private Set<String> getProcessObjectsBySubject(String subjectName, Process process) {
        Set<String> processObjectsBySubject = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(subjectName))
                processObjectsBySubject.add(((Resource)statement.getObject()).getLocalName());

        return processObjectsBySubject;
    }

    private Set<String> getProcessPredicatesBySubject(String subjectName, Process process) {
        Set<String> processPredicatesBySubject = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(subjectName))
                processPredicatesBySubject.add(statement.getPredicate().getLocalName());

        return processPredicatesBySubject;
    }

    private Set<String> getProcessSubjects(Process process) {
        Set<String> processSubjects = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            processSubjects.add(statement.getSubject().getLocalName());

        return processSubjects;
    }
}
