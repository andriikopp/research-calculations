package pdm.research;

public class FailedComponentPredictionUtil {

	public static int[] getPredictionImage(TelemetryFailures bean) {
		boolean x1 = getX1(bean);
		boolean x2 = getX2(bean);
		boolean x3 = getX3(bean);
		boolean x4 = getX4(bean);
		boolean x5 = getX5(bean);
		boolean x6 = getX6(bean);
		boolean x7 = getX7(bean);

		int[] image = { 0, 0, 0, 0 };
		int index = -1;

		if (!x1 && x3 && x4 && x6) {
			index = 0;
		}

		if ((x1 && x2) || (!x1 && x3 && x4 && !x6 && x7)) {
			index = 1;
		}

		if ((!x1 && x3 && !x4) || (!x1 && !x3 && x5)) {
			index = 2;
		}

		if ((x1 && !x2) || (!x1 && x3 && x4 && !x6 && !x7) || (!x1 && !x3 && !x5)) {
			index = 3;
		}

		image[index] = 1;

		return image;
	}

	public static int[] getFailureImage(TelemetryFailures bean) {
		int[] image = { 0, 0, 0, 0 };
		String failedComponent = String.valueOf(bean.getFailure().charAt(4));
		int index = Integer.parseInt(failedComponent) - 1;
		image[index] = 1;
		return image;
	}

	private static boolean getX1(TelemetryFailures bean) {
		return bean.getRotate() < 406;
	}

	private static boolean getX2(TelemetryFailures bean) {
		return bean.getVibration() < 60;
	}

	private static boolean getX3(TelemetryFailures bean) {
		return bean.getVibration() < 48;
	}

	private static boolean getX4(TelemetryFailures bean) {
		return bean.getPressure() < 116;
	}

	private static boolean getX5(TelemetryFailures bean) {
		return bean.getPressure() >= 124;
	}

	private static boolean getX6(TelemetryFailures bean) {
		return bean.getVolt() >= 169;
	}

	private static boolean getX7(TelemetryFailures bean) {
		return bean.getRotate() < 474;
	}
}
