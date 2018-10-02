package main.java.edu.kopp.phd.repository.domain.granularity;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;

public class Industry implements Granularity {

    @Override
    public String getName() {
        return "Industry";
    }
}
