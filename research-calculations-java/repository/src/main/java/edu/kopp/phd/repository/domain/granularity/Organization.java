package main.java.edu.kopp.phd.repository.domain.granularity;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;

public class Organization implements Granularity {

    @Override
    public String getName() {
        return "Organization";
    }
}