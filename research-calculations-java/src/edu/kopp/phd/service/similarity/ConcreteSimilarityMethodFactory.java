package edu.kopp.phd.service.similarity;

import edu.kopp.phd.service.similarity.api.SimilarityMethod;
import edu.kopp.phd.service.similarity.impl.FunctionEnvironmentWeightedSimilarityMethod;

public class ConcreteSimilarityMethodFactory implements SimilarityMethodFactory {

    public static ConcreteSimilarityMethodFactory createInstance() {
        return new ConcreteSimilarityMethodFactory();
    }

    @Override
    public SimilarityMethod getSimilarityMethod() {
        return new FunctionEnvironmentWeightedSimilarityMethod();
    }
}
