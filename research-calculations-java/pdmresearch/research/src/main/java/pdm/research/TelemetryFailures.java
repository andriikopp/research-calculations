package pdm.research;

public class TelemetryFailures {
	private double volt;
	private double rotate;
	private double pressure;
	private double vibration;
	private String failure;

	public TelemetryFailures(double volt, double rotate, double pressure, double vibration, String failure) {
		super();
		this.volt = volt;
		this.rotate = rotate;
		this.pressure = pressure;
		this.vibration = vibration;
		this.failure = failure;
	}

	public double getVolt() {
		return volt;
	}

	public void setVolt(double volt) {
		this.volt = volt;
	}

	public double getRotate() {
		return rotate;
	}

	public void setRotate(double rotate) {
		this.rotate = rotate;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getVibration() {
		return vibration;
	}

	public void setVibration(double vibration) {
		this.vibration = vibration;
	}

	public String getFailure() {
		return failure;
	}

	public void setFailure(String failure) {
		this.failure = failure;
	}

	@Override
	public String toString() {
		return volt + ", " + rotate + ", " + pressure + ", " + vibration + ", " + failure;
	}
}
