package main.java.edu.kopp.phd.repository.domain.category;

import main.java.edu.kopp.phd.repository.domain.category.api.Category;

public interface CategoryType {

    Category OPERATION = new Operation();

    Category MANAGEMENT = new Management();

    Category SUPPORT = new Support();
}
