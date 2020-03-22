package pdm.research;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pdm.research.repository.TelemetryFailuresRepository;

@SpringBootApplication
public class App implements CommandLineRunner {
	@Autowired
	@Qualifier("jdbcTelemetryFailuresRepository")
	private TelemetryFailuresRepository telemetryFailuresRepository;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<TelemetryFailures> telemetryFailures = telemetryFailuresRepository.findAll();

		for (TelemetryFailures record : telemetryFailures) {
			int[] a = FailedComponentPredictionUtil.getFailureImage(record);
			int[] b = FailedComponentPredictionUtil.getPredictionImage(record);

			System.out.println(record + ", " + Arrays.toString(a) + ", " + Arrays.toString(b) + ", "
					+ HammingDistanceUtil.dist(a, b));
		}
	}
}
