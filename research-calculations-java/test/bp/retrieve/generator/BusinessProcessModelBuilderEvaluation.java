package bp.retrieve.generator;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.similarity.SemanticSimilarityUtil;
import main.resources.repository.BPModelsCollection;
import main.resources.repository.files.GoodsOrderBPMN;
import main.resources.repository.files.GoodsOrderEPC;
import org.junit.Test;

import static bp.retrieve.generator.BusinessProcessModelBuilder.closeness;
import static bp.retrieve.generator.BusinessProcessModelBuilder.normalized;

/**
 * Evaluation of business process model builder {@link BusinessProcessModelBuilder}.
 *
 * @author Andrii Kopp
 */
public class BusinessProcessModelBuilderEvaluation {

    @Test
    public void evaluate() {
        BPModelRDFGraph goodsOrderBPMN = new GoodsOrderBPMN().getModelDescription();
        BPModelRDFGraph goodsOrderEPC = new GoodsOrderEPC().getModelDescription();

        System.out.printf("%s <> %s\t", goodsOrderBPMN.getName(), goodsOrderEPC.getName());

        System.out.printf("%.2f\n", closeness(normalized(goodsOrderBPMN),
                normalized(goodsOrderEPC),
                SemanticSimilarityUtil.jaccardSimilarity).getValue());

        System.out.println();

        for (GenericProcessModel first : BPModelsCollection.models)
            for (GenericProcessModel second : BPModelsCollection.models) {
                System.out.printf("%s <> %s\t", first.getName(), second.getName());

                System.out.printf("%.2f\n", closeness(normalized(first.getModelDescription()),
                        normalized(second.getModelDescription()),
                        SemanticSimilarityUtil.jaccardSimilarity).getValue());
            }
    }
}
