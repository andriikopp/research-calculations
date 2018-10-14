package edu.kopp.phd.express.search.accuracy.api;

import edu.kopp.phd.express.metamodel.Model;

public interface IAccuracy {

    boolean getPreDefinedSimilarity(Model first, Model second);
}
