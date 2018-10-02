package main.java.edu.kopp.phd.repository.dao.api;

import main.java.edu.kopp.phd.repository.domain.model.GenericModel;

import java.util.List;

/**
 * Process models collection API.
 *
 * @author Andrii Kopp
 */
public interface ProcessModelsCollection {

    /**
     * Store business process instance.
     *
     * @param model
     */
    void create(GenericModel model);

    /**
     * Retrieve all business process instances.
     *
     * @return
     */
    List<GenericModel> retrieve();

    /**
     * Update business process instance by a given id.
     *
     * @param id
     * @param model
     */
    void update(int id, GenericModel model);

    /**
     * Remove business process instance by a given id.
     * @param id
     */
    void delete(int id);
}
