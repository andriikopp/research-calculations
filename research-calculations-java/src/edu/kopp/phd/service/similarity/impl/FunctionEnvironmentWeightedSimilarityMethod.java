package edu.kopp.phd.service.similarity.impl;

import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.service.AnalysisService;
import edu.kopp.phd.service.similarity.api.SimilarityMethod;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.HashSet;
import java.util.Set;

public class FunctionEnvironmentWeightedSimilarityMethod implements SimilarityMethod {

    @Override
    public double calculateSimilarityMeasure(Process process, Process pattern) {
        Set<String> processFunctions = getProcessFunctions(process);
        Set<String> patternFunctions = getProcessFunctions(pattern);

        Set<String> functionsUnion = setUnion(processFunctions, patternFunctions);

        if (functionsUnion.isEmpty())
            return 0;

        double orgUnitsSimilarity = 0;

        for (String function : functionsUnion)
            orgUnitsSimilarity += setSimilarity(getOrganizationalUnitsThatPerformFunction(function, process),
                    getOrganizationalUnitsThatPerformFunction(function, pattern));

        double appSystemsSimilarity = 0;

        for (String function : functionsUnion)
            appSystemsSimilarity += setSimilarity(getApplicationSystemsThatSupportFunction(function, process),
                    getApplicationSystemsThatSupportFunction(function, pattern));

        double inputsSimilarity = 0;

        for (String function : functionsUnion)
            inputsSimilarity += setSimilarity(getBusinessObjectsRequiredByFunction(function, process),
                    getBusinessObjectsRequiredByFunction(function, pattern));

        double outputsSimilarity = 0;

        for (String function : functionsUnion)
            outputsSimilarity += setSimilarity(getBusinessObjectsProducedByFunction(function, process),
                    getBusinessObjectsProducedByFunction(function, pattern));

        orgUnitsSimilarity /= (double) functionsUnion.size();
        appSystemsSimilarity /= (double) functionsUnion.size();
        inputsSimilarity /= (double) functionsUnion.size();
        outputsSimilarity /= (double) functionsUnion.size();

        double functionsSimilarity = setSimilarity(processFunctions, patternFunctions);

        double similarity = functionsSimilarity +
                AnalysisService.FUNCTIONAL_WEIGHT * orgUnitsSimilarity +
                AnalysisService.COMMUNICATION_WEIGHT * appSystemsSimilarity +
                AnalysisService.SEQUENTIAL_WEIGHT * (inputsSimilarity + outputsSimilarity);

        similarity /= (1.0 + AnalysisService.FUNCTIONAL_WEIGHT + AnalysisService.COMMUNICATION_WEIGHT +
            2.0 * AnalysisService.SEQUENTIAL_WEIGHT);

        LOGGER.info(String.format("Similarity;%s;%s;%.4f;%.4f;%.4f;%.4f;%.4f;%.4f",
                process.getResource().getLocalName(),
                pattern.getResource().getLocalName(),
                functionsSimilarity,
                orgUnitsSimilarity,
                appSystemsSimilarity,
                inputsSimilarity,
                outputsSimilarity,
                similarity));

        return similarity;
    }

    private Set<String> getBusinessObjectsProducedByFunction(String function, Process process) {
        Set<String> businessObjects = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(function) &&
                    statement.getPredicate().equals(REPOSITORY.getProduces()))
                businessObjects.add(((Resource) statement.getObject()).getLocalName());

        return businessObjects;
    }

    private Set<String> getBusinessObjectsRequiredByFunction(String function, Process process) {
        Set<String> businessObjects = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(function) &&
                    statement.getPredicate().equals(REPOSITORY.getRequires()))
                businessObjects.add(((Resource) statement.getObject()).getLocalName());

        return businessObjects;
    }

    private Set<String> getApplicationSystemsThatSupportFunction(String function, Process process) {
        Set<String> applicationSystems = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(function) &&
                    statement.getPredicate().equals(REPOSITORY.getIsSupportedBy()))
                applicationSystems.add(((Resource) statement.getObject()).getLocalName());

        return applicationSystems;
    }

    private Set<String> getOrganizationalUnitsThatPerformFunction(String function, Process process) {
        Set<String> organizationalUnits = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getSubject().getLocalName().equals(function) &&
                    statement.getPredicate().equals(REPOSITORY.getIsPerformedBy()))
                organizationalUnits.add(((Resource) statement.getObject()).getLocalName());

        return organizationalUnits;
    }

    private Set<String> getProcessFunctions(Process process) {
        Set<String> functions = new HashSet<>();

        for (Statement statement : getProcessStatements(process))
            if (statement.getPredicate().equals(REPOSITORY.getA()) &&
                    statement.getObject().equals(REPOSITORY.getFunction()))
                functions.add(statement.getSubject().getLocalName());

        return functions;
    }
}
