package edu.kopp.phd.service.similarity.impl;

import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.service.AnalysisService;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Set;

public class RDFWeightedSimilarityMethod extends RDFDefaultSimilarityMethod {

    @Override
    public double calculateSimilarityMeasure(Process process, Process pattern) {
        Set<String> processSubjects = getProcessSubjects(process);
        Set<String> patternSubjects = getProcessSubjects(pattern);

        Set<String> subjectsUnion = setUnion(processSubjects, patternSubjects);

        if (subjectsUnion.isEmpty())
            return 0;

        double sumOfPredicatesAndObjectsSimilarities = 0;

        double sumOfNodeWeights = 0;

        for (String subjectName : subjectsUnion) {
            double belongsToBothSubjectSets = processSubjects.contains(subjectName) &&
                    patternSubjects.contains(subjectName) ? 1.0 : 0;

            double processNodeWeight = defineNodeWeightByLabel(subjectName, process);
            double patternNodeWeight = defineNodeWeightByLabel(subjectName, pattern);

            double nodeWeight = Math.max(processNodeWeight, patternNodeWeight);

            sumOfPredicatesAndObjectsSimilarities += nodeWeight * belongsToBothSubjectSets * (
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

            sumOfNodeWeights += nodeWeight;
        }

        double similarity = sumOfPredicatesAndObjectsSimilarities / (4.0 * sumOfNodeWeights);

        LOGGER.info(String.format("Similarity;%s;%s;%.4f",
                process.getResource().getLocalName(),
                pattern.getResource().getLocalName(),
                similarity));

        return similarity;
    }

    protected double defineNodeWeightByLabel(String label, Process process) {
        RDFNode type = getNodeTypeByLabel(label, process);

        if (type == null)
            return 0;

        if (type.equals(REPOSITORY.getProcess()) || type.equals(REPOSITORY.getFunction()) ||
                type.equals(REPOSITORY.getEvent()))
            return AnalysisService.PROCESS_FLOW_OBJECTS_WEIGHT;

        if (type.equals(REPOSITORY.getAndGateway()))
            return AnalysisService.AND_GATEWAYS_WEIGHT;

        if (type.equals(REPOSITORY.getxOrGateway()) || type.equals(REPOSITORY.getOrGateway()))
            return AnalysisService.OR_XOR_GATEWAYS_WEIGHT;

        if (type.equals(REPOSITORY.getOrganizationalUnit()))
            return AnalysisService.UNITS_WEIGHT;

        if (type.equals(REPOSITORY.getApplicationSystem()))
            return AnalysisService.APPLICATIONS_WEIGHT;

        if (type.equals(REPOSITORY.getBusinessObject()) || type.equals(REPOSITORY.getInformation()) ||
                type.equals(REPOSITORY.getMaterial()))
            return AnalysisService.OBJECTS_WEIGHT;

        return 0;
    }
}
