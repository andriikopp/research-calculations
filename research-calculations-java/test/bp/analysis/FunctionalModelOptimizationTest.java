package bp.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bp.analysis.FunctionalModelOptimization;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionalModelOptimizationTest {
	private static FunctionalModelOptimization optimization;

	private static final double DELTA = 10E-3;

	@BeforeClass
	public static void setUp() {
		int[][] arrows = { { 4, 1, 1, 2, 1 }, { 1, 2, 1, 2, 1 }, { 1, 2, 1, 2, 0 }, { 1, 2, 1, 2, 0 } };
		double[] weights = { 0.2, 0.2, 0.1, 0.13, 0.06 };
		int[][] tunnels = { { 0, 0, 0, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };

		optimization = new FunctionalModelOptimization(arrows, weights, tunnels);
	}

	@Test
	public void testCalculateBalanceCoefficient() {
		double actual = optimization.calculateBalanceCoefficient();
		double expected = 0.27;

		assertEquals("Balance coefficient before optimization", expected, actual, DELTA);
	}

	@Test
	public void testPerformOptimization() {
		optimization.performOptimization();

		int[][] actuals = optimization.getChanges();
		int[][] expecteds = { { -2, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0 } };

		for (int i = 0; i < actuals.length; i++) {
			for (int j = 0; j < actuals[i].length; j++) {
				assertEquals(String.format("Value in changes matrix at [%d, %d]", i, j), expecteds[i][j],
						actuals[i][j]);
			}
		}

		double actual = optimization.calculateBalanceCoefficient();
		double expected = 0.08;

		assertEquals("Balance coefficient after optimization", expected, actual, DELTA);
	}
}
