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
        Set<String> processSubjects = getProcessSubjects(process);
        Set<String> patternSubjects = getProcessSubjects(pattern);

        Set<String> subjectsUnion = setUnion(processSubjects, patternSubjects);

        if (subjectsUnion.isEmpty())
            return 0;

        double sumOfPredicatesAndObjectsSimilarities = 0;

        for (String subjectName : subjectsUnion) {
            double belongsToBothSubjectSets = processSubjects.contains(subjectName) &&
                    patternSubjects.contains(subjectName) ? 1.0 : 0;

            sumOfPredicatesAndObjectsSimilarities += belongsToBothSubjectSets * (
                    // Predicates similarity
                    setSimilarity(getProcessPredicatesBySubject(subjectName, process),
                            getProcessPredicatesBySubject(subjectName, pattern)) +

                            // Domains similarity
                            setSimilarity(getProcessDomainsBySubject(subjectName, process),
                                    getProcessDomainsBySubject(subjectName, pattern)) +

                            // Objects similarity
                            setSimilarity(getProcessObjectsBySubject(subjectName, process),
                                    getProcessObjectsBySubject(subjectName, pattern)) +

                            // Types similarity
                            setSimilarity(getProcessTypesBySubject(subjectName, process),
                                    getProcessTypesBySubject(subjectName, pattern)));
        }

        double similarity = sumOfPredicatesAndObjectsSimilarities / (4.0 * (double) subjectsUnion.size());

        LOGGER.info(String.format("Similarity;%s;%s;%.4f",
                process.getResource().getLocalName(),
                pattern.getResource().getLocalName(),
                similarity));

        return similarity;
    }

    protected Set<String> getProcessTypesBySubject(String subjectName, Process process) {
        Set<String> processTypesBySubject = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(subjectName)) {
                String pair = String.format("(%s,%s)",
                        statement.getPredicate().getLocalName(),
                        ((Resource) statement.getObject()).getLocalName());

                processTypesBySubject.add(pair);
            }

        return processTypesBySubject;
    }

    protected Set<String> getProcessObjectsBySubject(String subjectName, Process process) {
        Set<String> processObjectsBySubject = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(subjectName) &&
                    !statement.getPredicate().equals(REPOSITORY.getA()))
                processObjectsBySubject.add(((Resource) statement.getObject()).getLocalName());

        return processObjectsBySubject;
    }

    protected Set<String> getProcessDomainsBySubject(String subjectName, Process process) {
        Set<String> processDomainsBySubject = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(subjectName) &&
                    statement.getPredicate().equals(REPOSITORY.getA()))
                processDomainsBySubject.add(((Resource) statement.getObject()).getLocalName());

        return processDomainsBySubject;
    }

    protected Set<String> getProcessPredicatesBySubject(String subjectName, Process process) {
        Set<String> processPredicatesBySubject = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(subjectName))
                processPredicatesBySubject.add(statement.getPredicate().getLocalName());

        return processPredicatesBySubject;
    }

    protected Set<String> getProcessSubjects(Process process) {
        Set<String> processSubjects = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            processSubjects.add(statement.getSubject().getLocalName());

        return processSubjects;
    }
}
