package edu.bpmanalysis.collection.api;

import edu.bpmanalysis.description.tools.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public interface Models {

    List<Model> importModels();

    default List<Model> loadModels() {
        throw new NotImplementedException();
    }
}
