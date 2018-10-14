package edu.kopp.phd.express.search.accuracy;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.search.accuracy.api.IAccuracy;

public class ConnectivityBasedAccuracy implements IAccuracy {

    @Override
    public boolean getPreDefinedSimilarity(Model first, Model second) {
        return Math.abs(first.connectivity() - second.connectivity()) <= 10E-6;
    }
}
