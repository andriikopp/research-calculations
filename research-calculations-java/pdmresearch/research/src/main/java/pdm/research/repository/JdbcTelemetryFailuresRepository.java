package pdm.research.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pdm.research.TelemetryFailures;

@Repository
public class JdbcTelemetryFailuresRepository implements TelemetryFailuresRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<TelemetryFailures> findAll() {
		return jdbcTemplate.query("select * from pdm_telemetry_failures",
				(rs, rowNum) -> new TelemetryFailures(rs.getDouble("volt"), rs.getDouble("rotate"),
						rs.getDouble("pressure"), rs.getDouble("vibration"), rs.getString("failure")));
	}
}
