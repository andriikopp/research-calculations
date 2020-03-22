package pdm.research.repository;

import java.util.List;

import pdm.research.TelemetryFailures;

public interface TelemetryFailuresRepository {

	List<TelemetryFailures> findAll();
}
