package edu.kopp.phd.service.similarity;

import edu.kopp.phd.service.similarity.api.SimilarityMethod;
import edu.kopp.phd.service.similarity.impl.RDFDefaultSimilarityMethod;

public interface SimilarityMethodFactory {

    default SimilarityMethod getSimilarityMethod() {
        return new RDFDefaultSimilarityMethod();
    }
}
