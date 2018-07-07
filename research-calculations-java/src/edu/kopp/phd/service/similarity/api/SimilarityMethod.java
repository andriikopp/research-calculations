package edu.kopp.phd.service.similarity.api;

import bp.retrieve.similarity.Similarity;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import org.apache.jena.rdf.model.Statement;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface SimilarityMethod {
    RDFRepository REPOSITORY = RDFRepository.getInstance();

    Logger LOGGER = Logger.getLogger(SimilarityMethod.class);

    double calculateSimilarityMeasure(Process process, Process pattern);

    default double setSimilarity(Set<String> first, Set<String> second) {
        if (first.isEmpty() && second.isEmpty())
            return 0;

        return (double) setIntersection(first, second).size() /
                (double) setUnion(first, second).size();
    }

    default Set<String> setUnion(Set<String> first, Set<String> second) {
        return Similarity.union(first, second);
    }

    default Set<String> setIntersection(Set<String> first, Set<String> second) {
        return Similarity.intersection(first, second);
    }

    default List<Statement> getProcessStatements(Process process) {
        List<Statement> processStatements = new ArrayList<>();

        for (List<Statement> statements : REPOSITORY.retrieveProcess(process).values())
            for (Statement statement : statements)
                processStatements.add(statement);

        return processStatements;
    }
}