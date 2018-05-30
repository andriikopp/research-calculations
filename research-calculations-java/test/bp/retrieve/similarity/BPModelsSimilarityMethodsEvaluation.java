package bp.retrieve.similarity;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Evaluation of various methods of similarity estimation between business process models.
 *
 * @author Andrii Kopp
 */
public class BPModelsSimilarityMethodsEvaluation {
    private Map<String, double[]> models = new LinkedHashMap<>();

    public BPModelsSimilarityMethodsEvaluation() {
        models.put("identical",         new double[] { 1.0, 1.0, 1.0, 1.0 });
        models.put("very good",         new double[] { 1.0, 1.0, 0.6, 0.8 });
        models.put("good",              new double[] { 1.0, 0.8, 0.6, 0.4 });
        models.put("satisfactorily",    new double[] { 0.6, 0.4, 0.5, 0.5 });
        models.put("bad",               new double[] { 0.4, 0.2, 0.0, 0.0 });
        models.put("very bad",          new double[] { 0.2, 0.2, 0.0, 0.0 });
        models.put("different",         new double[] { 0.0, 0.0, 0.0, 0.0 });
    }

    @Test
    public void evaluate() {
        for (Map.Entry<String, double[]> entry : models.entrySet()) {
            System.out.printf("%s\t", entry.getKey());

            System.out.printf("%.2f\t", BPModelsSimilarityUtil.directEstimation(entry.getValue(),
                    BPModelsSimilarityUtil.similarity));
            System.out.printf("%.2f\t", BPModelsSimilarityUtil.fishbernEstimation(entry.getValue(),
                    BPModelsSimilarityUtil.similarity, (index, n) -> BPModelsSimilarityUtil.fishbernFirstEquation(index, n)));
            System.out.printf("%.2f\t", BPModelsSimilarityUtil.fishbernEstimation(entry.getValue(),
                    BPModelsSimilarityUtil.similarity, (index, n) -> BPModelsSimilarityUtil.fishbernSecondEquation(index, n)));
            System.out.printf("%.2f\n", BPModelsSimilarityUtil.pairwiseComparison(entry.getValue(), BPModelsSimilarityUtil.similarity));
        }
    }
}
