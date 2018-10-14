package edu.kopp.phd.express.search.accuracy;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.search.accuracy.api.IAccuracy;

public class SizeBasedAccuracy implements IAccuracy {

    @Override
    public boolean getPreDefinedSimilarity(Model first, Model second) {
        return first.getNodes().size() == second.getNodes().size();
    }
}
