package bp.retrieve.similarity.scales;

/**
 * Harrington's scale.
 *
 * @author Andrii Kopp
 */
public class HarringtonScale {
    /**
     * Very bad state threshold.
     */
    public static final double VERY_BAD = 0;

    /**
     * Bad state threshold.
     */
    public static final double BAD = 0.2;

    /**
     * Satisfactorily state threshold.
     */
    public static final double SATISFACTORILY = 0.37;

    /**
     * Good state threshold.
     */
    public static final double GOOD = 0.63;

    /**
     * Very good state threshold.
     */
    public static final double VERY_GOOD = 0.8;

    public static final HarringtonScale VERY_BAD_THRESHOLD = new HarringtonScale(VERY_BAD);
    public static final HarringtonScale BAD_THRESHOLD = new HarringtonScale(BAD);
    public static final HarringtonScale SATISFACTORILY_THRESHOLD = new HarringtonScale(SATISFACTORILY);
    public static final HarringtonScale GOOD_THRESHOLD = new HarringtonScale(GOOD);
    public static final HarringtonScale VERY_GOOD_THRESHOLD = new HarringtonScale(VERY_GOOD);

    // Threshold.
    private double thresholdValue;

    private HarringtonScale(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    /**
     * Returns value of the Saaty's scale using Harrington's scale mapping.
     *
     * @param value - the value in range {@code [0, 1]}.
     * @return the corresponding value of the Saaty's scale.
     */
    public static double harringtonToSaaty(double value) {
        if (value > VERY_GOOD)
            return SaatyScale.VERY_GOOD;
        if (value > GOOD)
            return SaatyScale.GOOD;
        if (value > SATISFACTORILY)
            return SaatyScale.SATISFACTORILY;
        if (value > BAD)
            return SaatyScale.BAD;
        return SaatyScale.VERY_BAD;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }
}
