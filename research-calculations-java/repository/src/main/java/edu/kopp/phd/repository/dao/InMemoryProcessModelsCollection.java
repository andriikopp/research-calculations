package main.java.edu.kopp.phd.repository.dao;

import main.java.edu.kopp.phd.repository.dao.api.ProcessModelsCollection;
import main.java.edu.kopp.phd.repository.domain.model.GenericModel;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProcessModelsCollection implements ProcessModelsCollection {
    private List<GenericModel> models;

    public InMemoryProcessModelsCollection() {
        this.models = new ArrayList<>();
    }

    @Override
    public void create(GenericModel model) {
        models.add(model);
    }

    @Override
    public List<GenericModel> retrieve() {
        return new ArrayList<>(models);
    }

    @Override
    public void update(int id, GenericModel model) {
        models.set(id, model);
    }

    @Override
    public void delete(int id) {
        models.remove(id);
    }
}
