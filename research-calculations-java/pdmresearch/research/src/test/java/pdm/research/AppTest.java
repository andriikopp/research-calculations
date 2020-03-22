package pdm.research;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class AppTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/test_data.csv", numLinesToSkip = 1)
	void testFindAll(double volt, double rotate, double pressure, double vibration, String failure) {
		TelemetryFailures record = new TelemetryFailures(vibration, vibration, vibration, vibration, failure);

		int[] a = FailedComponentPredictionUtil.getFailureImage(record);
		int[] b = FailedComponentPredictionUtil.getPredictionImage(record);

		int actual = HammingDistanceUtil.dist(a, b);
		assertEquals(0, actual);
	}
}
