package edu.kopp.phd.service;

import bp.retrieve.similarity.SemanticSimilarityUtil;
import bp.retrieve.similarity.Similarity;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.*;

public class SimilarityService {
    private RDFRepository repository = RDFRepository.getInstance();

    private ControlFlowService controlFlowService;

    public SimilarityService(ControlFlowService controlFlowService) {
        this.controlFlowService = controlFlowService;
    }

    public List<Map.Entry<Process, Double>> getSimilarProcessesByProcessPattern(Process pattern) {
        List<Map.Entry<Process, Double>> similarProcesses = new ArrayList<>();

        for (Process process : controlFlowService.getAllProcesses()) {
            if (!process.equals(pattern)) {
                double similarity = calculateSimilarityMeasure(process, pattern);

                if (similarity > 0)
                    similarProcesses.add(new Map.Entry<Process, Double>() {
                        @Override
                        public Process getKey() {
                            return process;
                        }

                        @Override
                        public Double getValue() {
                            return similarity;
                        }

                        @Override
                        public Double setValue(Double value) {
                            return value;
                        }
                    });
            }
        }

        similarProcesses.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        return similarProcesses;
    }

    public double calculateSimilarityMeasure(Process process, Process pattern) {
        Set<String> subjectsIntersection = setIntersection(getProcessSubjects(process),
                getProcessSubjects(pattern));

        if (subjectsIntersection.isEmpty())
            return 0;

        double sumOfPredicatesAndObjectsSimilarities = 0;

        for (String subjectName : subjectsIntersection)
            sumOfPredicatesAndObjectsSimilarities +=
                    setSimilarity(getProcessPredicatesBySubject(subjectName, process),
                            getProcessPredicatesBySubject(subjectName, pattern)) +
                            setSimilarity(getProcessObjectsBySubject(subjectName, process),
                                    getProcessObjectsBySubject(subjectName, pattern));

        double similarity = sumOfPredicatesAndObjectsSimilarities / (2.0 * (double) subjectsIntersection.size());

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

    private List<Statement> getProcessStatements(Process process) {
        List<Statement> processStatements = new ArrayList<>();

        for (List<Statement> statements : repository.retrieveProcess(process).values())
            for (Statement statement : statements)
                processStatements.add(statement);

        return processStatements;
    }

    private double setSimilarity(Set<String> first, Set<String> second) {
        return SemanticSimilarityUtil.jaccardSimilarity.coefficient(first, second);
    }

    private Set<String> setIntersection(Set<String> first, Set<String> second) {
        return Similarity.intersection(first, second);
    }
}
