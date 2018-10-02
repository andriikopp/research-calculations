package main.java.edu.kopp.phd.repository.domain.granularity;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;

public interface GranularityType {

    Granularity FOUNDATION = new Foundation();

    Granularity COMMON = new Common();

    Granularity INDUSTRY = new Industry();

    Granularity ORGANIZATION = new Organization();
}
