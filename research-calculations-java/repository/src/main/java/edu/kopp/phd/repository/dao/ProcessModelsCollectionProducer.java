package main.java.edu.kopp.phd.repository.dao;

import main.java.edu.kopp.phd.repository.dao.api.ProcessModelsCollection;

/**
 * Produces business process models collection instance.
 *
 * @author Andrii Kopp
 */
public final class ProcessModelsCollectionProducer {
    // Configurable field to provide process models collection implementation.
    public static final ProcessModelsCollection COLLECTION = new InMemoryProcessModelsCollection();

    private ProcessModelsCollectionProducer() {
    }

    public static ProcessModelsCollection getCollection() {
        return COLLECTION;
    }
}
