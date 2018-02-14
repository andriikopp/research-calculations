package bp.idef0;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides approaches used for analysis and optimization of functional IDEF0
 * models.
 * 
 * @author Andrii Kopp
 */
public class FunctionalModelOptimization {
	private int[][] arrows;
	private double[] weights;
	private int[][] tunnels;
	private int[][] changes;

	/**
	 * Default amount of IDEF0 arrow types (input, output, control, mechanism,
	 * call)
	 */
	private static final int ARROW_TYPES_LENGTH = 5;

	/**
	 * Initializes instance state with specified matrices and array, which
	 * describe IDEF0 functional models.
	 * 
	 * @param arrows
	 *            - matrix of arrows connected with blocks;
	 * @param weights
	 *            - array of weights related to each arrow type (input, output,
	 *            control, mechanism, call);
	 * @param tunnels
	 *            - matrix of tunneled arrows connected with blocks.
	 */
	public FunctionalModelOptimization(int[][] arrows, double[] weights, int[][] tunnels) {
		if (tunnels.length == arrows.length) {
			if (arrows[0].length != ARROW_TYPES_LENGTH) {
				throw new RuntimeException("Matrix 'weights' should have 5 columns!");
			}

			if (tunnels.length != ARROW_TYPES_LENGTH - 1) {
				throw new RuntimeException("Matrix 'tunnels' should have 4 rows!");
			}
		} else {
			throw new RuntimeException("Matrix 'tunnels' should have the same length!");
		}

		if (weights.length != ARROW_TYPES_LENGTH) {
			throw new RuntimeException("Array 'weights' should have 5 elements!");
		}

		this.arrows = arrows;
		this.weights = weights;
		this.tunnels = tunnels;
	}

	/**
	 * Calculates and returns balance coefficient.
	 * 
	 * @return balance coefficient value [0..1], where 0 - the best value of the
	 *         balance coefficient
	 */
	public double calculateBalanceCoefficient() {
		double tunnelCoefficient = calculateTunnelCoefficient();
		double firstPart = 0;
		double secondPart = 0;

		for (int i = 0; i < arrows.length; i++) {
			double sumElement = 0;

			int callIndex = ARROW_TYPES_LENGTH - 1;

			for (int j = 0; j < ARROW_TYPES_LENGTH - 1; j++) {
				sumElement += weights[j] * (arrows[i][j] - tunnelCoefficient * tunnels[i][j]);
			}

			sumElement += weights[callIndex] * arrows[i][callIndex];

			if (sumElement > secondPart) {
				secondPart = sumElement;
			}

			firstPart += sumElement;
		}

		return Math.abs(firstPart / (double) arrows.length - secondPart);
	}

	/**
	 * Allows to define changes in order to provide optimization of functional
	 * IDEF0 model.
	 */
	public void performOptimization() {
		double avgBlocks = defineAverageBlocksAmount();

		if (avgBlocks % 1 == 0) {
			int avg = (int) avgBlocks;

			changes = defineChanges(avg);
		} else {
			int avgDown = (int) defineAverageBlocksAmount();
			int avgUp = avgDown + 1;

			int[][] changesDown = defineChanges(avgDown);
			changes = changesDown;
			applyChanges();
			double balanceDown = calculateBalanceCoefficient();

			int[][] changesUp = defineChanges(avgUp);
			changes = changesUp;
			applyChanges();
			double balanceUp = calculateBalanceCoefficient();

			if (balanceDown < balanceUp) {
				changes = changesDown;
			} else {
				changes = changesUp;
			}
		}

		applyChanges();
	}

	private void applyChanges() {
		for (int i = 0; i < arrows.length; i++) {
			for (int j = 0; j < ARROW_TYPES_LENGTH; j++) {
				arrows[i][j] += changes[i][j];
			}
		}
	}

	private int[][] defineChanges(int avg) {
		int[][] changes = new int[arrows.length][ARROW_TYPES_LENGTH];

		for (int i = 0; i < arrows.length; i++) {
			int blockArrows = countArrowsConnectedToBlock(i);
			int change = avg - blockArrows;

			if (change != 0) {
				List<Integer> exceptIndex = new ArrayList<Integer>();

				for (int j = 0; j < ARROW_TYPES_LENGTH - 1; j++) {
					if (arrows[i][j] + change <= 0) {
						exceptIndex.add(j);
					}
				}

				int arrow;

				if (change < 0) {
					arrow = defineArrowWithMaxWeight();
				} else {
					arrow = defineArrowWithMinWeight(exceptIndex);
				}

				changes[i][arrow] += change;
			}
		}

		return changes;
	}

	private double calculateTunnelCoefficient() {
		double tunnelsSum = 0;

		for (int i = 0; i < tunnels.length; i++) {
			for (int j = 0; j < ARROW_TYPES_LENGTH - 1; j++) {
				tunnelsSum += tunnels[i][j];
			}
		}

		double arrowsSum = 0;

		for (int i = 0; i < arrows.length; i++) {
			for (int j = 0; j < ARROW_TYPES_LENGTH - 1; j++) {
				arrowsSum += arrows[i][j];
			}
		}

		return 1.0 - tunnelsSum / arrowsSum;
	}

	private double defineAverageBlocksAmount() {
		double value = 0;

		for (int i = 0; i < arrows.length; i++) {
			for (int j = 0; j < ARROW_TYPES_LENGTH; j++) {
				value += arrows[i][j];
			}
		}

		return value / (double) arrows.length;
	}

	private int countArrowsConnectedToBlock(int blockIndex) {
		int value = 0;

		for (int i = 0; i < ARROW_TYPES_LENGTH; i++) {
			value += arrows[blockIndex][i];
		}

		return value;
	}

	private int defineArrowWithMinWeight(List<Integer> exceptIndex) {
		int index = -1;
		double value = 1.0;

		for (int i = 0; i < ARROW_TYPES_LENGTH - 1; i++) {
			if (!exceptIndex.isEmpty() && exceptIndex.contains(i)) {
				continue;
			}

			if (weights[i] < value) {
				value = weights[i];
				index = i;
			}
		}

		return index;
	}

	private int defineArrowWithMaxWeight() {
		int index = -1;
		double value = 0;

		for (int i = 0; i < ARROW_TYPES_LENGTH - 1; i++) {
			if (weights[i] > value) {
				value = weights[i];
				index = i;
			}
		}

		return index;
	}

	public int[][] getChanges() {
		return changes;
	}

	public int[][] getArrows() {
		return arrows;
	}
}
