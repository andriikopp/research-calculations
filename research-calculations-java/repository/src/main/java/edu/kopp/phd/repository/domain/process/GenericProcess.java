package main.java.edu.kopp.phd.repository.domain.process;

import main.java.edu.kopp.phd.repository.domain.category.api.Category;
import main.java.edu.kopp.phd.repository.domain.model.GenericModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a generic business process.
 *
 * @author Andrii Kopp
 */
public class GenericProcess {
    private String name;
    private Category category;

    private List<GenericModel> models;

    protected GenericProcess(String name, Category category) {
        this.name = name;
        this.category = category;

        this.models = new ArrayList<>();
    }

    public static GenericProcess createProcess(String name, Category category) {
        return new GenericProcess(name, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericProcess that = (GenericProcess) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return category != null ? category.equals(that.category) : that.category == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GenericProcess{" +
                "name='" + name + '\'' +
                ", category=" + category +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<GenericModel> getModels() {
        return models;
    }

    public void setModels(List<GenericModel> models) {
        this.models = models;
    }
}
