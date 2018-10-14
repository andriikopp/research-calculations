package edu.kopp.phd.express.search.accuracy;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.search.accuracy.api.IAccuracy;

public class DensityBasedAccuracy implements IAccuracy {

    @Override
    public boolean getPreDefinedSimilarity(Model first, Model second) {
        return Math.abs(first.density() - second.density()) <= 10E-6;
    }
}
