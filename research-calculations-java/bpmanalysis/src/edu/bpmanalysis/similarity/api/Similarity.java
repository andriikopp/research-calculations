package edu.bpmanalysis.similarity.api;

import edu.bpmanalysis.collection.tools.Model;

public interface Similarity {

    double compare(Model first, Model second);
}
