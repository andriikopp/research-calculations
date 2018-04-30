package bp.retrieve.config;

import java.util.Arrays;

import org.apache.jena.reasoner.IllegalParameterException;

import bp.retrieve.BPModelsSimilarity;
import bp.retrieve.CustomizableBPModelsSimilarity;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.collection.ProcessModelRepository;
import bp.retrieve.container.SimpleBPModelsContainer;
import bp.retrieve.container.TableBPModelsContainer;
import bp.retrieve.container.api.BPModelsContainer;
import bp.retrieve.pso.AcceleratedPSOBPModelsSimilarity;
import bp.retrieve.similarity.SemanticSimilarity;
import bp.retrieve.similarity.Similarity;

/**
 * Provides an easy way to build configurable business process model repository
 * using sequence of calls.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelRepositoryConfig {
	private Similarity similarityMeasure;
	private CustomizableBPModelsSimilarity modelsSimilarity;
	private BPModelsContainer modelsContainer;
	private ProcessModelRepository modelsRepository = ProcessModelRepository.INSTANCE;

	/**
	 * Starts configuration of the repository.
	 * 
	 * @return instance of itself.
	 */
	public static ProcessModelRepositoryConfig config() {
		return new ProcessModelRepositoryConfig();
	}

	/**
	 * Allows to use Accelerated Particle Swarm Optimization (APSO) for similarity
	 * search of business process models.
	 * 
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig apso() {
		modelsSimilarity = new AcceleratedPSOBPModelsSimilarity();
		return this;
	}

	/**
	 * Allows to set domain similarity coefficients when using manual configuration
	 * of similarity search instead of APSO-based. Sum of these coefficients must be
	 * equal to 1. In case it never called, coefficients would be set automatically
	 * using default values from {@code beans.xml}.
	 * 
	 * @param units
	 *            - importance of organizational units similarity [0, 1];
	 * @param systems
	 *            - importance of supporting systems similarity [0, 1];
	 * @param objects
	 *            - importance of process flow objects similarity [0, 1].
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig domain(double units, double systems, double objects) {
		if (modelsSimilarity instanceof AcceleratedPSOBPModelsSimilarity)
			throw new IllegalStateException("APSO implementation doesn't need manually selected coeffs!");

		modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.UNITS_COEFF, units);
		modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.SYSTEMS_COEFF, systems);
		modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.FLOW_OBJECTS_COEFF, objects);

		// Third-party coefficients would be considered in later release.
		modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.INPUTS_COEFF, 0);
		modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.OUTPUTS_COEFF, 0);
		modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.KPIS_COEFF, 0);

		return this;
	}

	/**
	 * Allows to set closeness similarity coefficients when using manual
	 * configuration of similarity search instead of APSO-based. Sum of these
	 * coefficients must be equal to 1. In case it never called, coefficients would
	 * be set automatically using default values from {@code beans.xml}.
	 * 
	 * @param label
	 *            - importance of label closeness [0, 1];
	 * @param structure
	 *            - importance of structure closeness [0, 1].
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig closeness(double label, double structure) {
		if (modelsSimilarity instanceof AcceleratedPSOBPModelsSimilarity)
			throw new IllegalStateException("APSO implementation doesn't need manually selected coeffs!");

		modelsSimilarity.setSimilarityCoefficient(BPModelsSimilarity.SEMANTIC_COEFF, label);
		modelsSimilarity.setSimilarityCoefficient(BPModelsSimilarity.STRUCTURE_COEFF, structure);

		return this;
	}

	/**
	 * Set similarity threshold. In case it never called, threshold would be set
	 * automatically using default value from {@code beans.xml}.
	 * 
	 * @param similarityThreshold
	 *            - value of the similarity threshold [0, 1].
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig threshold(double similarityThreshold) {
		if (similarityThreshold <= 0)
			throw new IllegalParameterException("Similarity threshold " + similarityThreshold + " is invalid!");

		modelsSimilarity.setSimilarityLevel(similarityThreshold);
		return this;
	}

	/**
	 * Use semantic similarity to measure business process models closeness. Current
	 * implementation uses Jaro-Winkler distance to measure similarity of concepts.
	 * In case it never called, default Jaccard index would be used.
	 * 
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig semantic() {
		similarityMeasure = new SemanticSimilarity(modelsSimilarity.getSimilarityLevel());
		modelsSimilarity.defineSimilarityMethod(similarityMeasure);
		return this;
	}

	/**
	 * Use semantic similarity to measure business process models closeness. In case
	 * it never called, default Jaccard index would be used.
	 * 
	 * @param concepts
	 *            - the weight of concepts similarity;
	 * @param synonyms
	 *            - the weight of synonyms similarity;
	 * @param equal
	 *            - the weight of total labels equality.
	 * @return instance of itself.
	 */
	@Deprecated
	public ProcessModelRepositoryConfig semantic(double concepts, double synonyms, double equal) {
		similarityMeasure = new SemanticSimilarity(concepts, synonyms, equal);
		modelsSimilarity.defineSimilarityMethod(similarityMeasure);
		return this;
	}

	/**
	 * Set label synonyms to measure semantic similarity properly. Using semantic
	 * similarity withoud specifying of the synonyms makes sence only if you are
	 * sure that nodes of business process models contains equal concepts. In case
	 * it never called, nothing more than plain Sorencen-Dice index would be used.
	 * 
	 * @param concept
	 *            - a concept represented by a label;
	 * @param synonyms
	 *            - one or several related synonyms.
	 * @return instance of itself.
	 */
	@Deprecated
	public ProcessModelRepositoryConfig synonyms(String concept, String... synonyms) {
		if (similarityMeasure instanceof SemanticSimilarity) {
			((SemanticSimilarity) similarityMeasure).addSynonyms(concept, synonyms);
		} else {
			throw new IllegalStateException("Synonyms might be used only with semantic closeness!");
		}

		return this;
	}

	/**
	 * Use list container as the colection of business process models. It provides
	 * pairwise comparison of business process models. Hence similarity search takes
	 * much more time to perform. It is vital to set one of the available
	 * containers, {@link SimpleBPModelsContainer} or
	 * {@link TableBPModelsContainer}.
	 * 
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig listContainer() {
		if (modelsContainer != null)
			throw new IllegalStateException("Container is already defined!");

		modelsContainer = new SimpleBPModelsContainer(modelsSimilarity);
		return this;
	}

	/**
	 * Use hashtable-based container as the collection of business process model. It
	 * provides custom data structure focused on faster similarity search. Although
	 * store operations take much more time to perform. It is vital to set one of
	 * the available containers, {@link SimpleBPModelsContainer} or
	 * {@link TableBPModelsContainer}.
	 * 
	 * @return instance of itself.
	 */
	public ProcessModelRepositoryConfig tableContainer() {
		if (modelsContainer != null)
			throw new IllegalStateException("Container is already defined!");

		modelsContainer = new TableBPModelsContainer(modelsSimilarity);
		return this;
	}

	/**
	 * Define a collection of business process models that might be stored in the
	 * repository. It is vital to set at least one buisiness process model.
	 * 
	 * @param processModels
	 *            - a sequence of business process models.
	 * @return instance of the repository.
	 */
	public ProcessModelRepository collection(GenericProcessModel... processModels) {
		if (modelsContainer == null)
			throw new IllegalStateException("Container is undefined!");

		modelsRepository.setProcessModelsContainer(modelsContainer);
		modelsRepository.addAll(Arrays.asList(processModels));
		return modelsRepository;
	}

	private ProcessModelRepositoryConfig() {
		modelsSimilarity = new CustomizableBPModelsSimilarity();
	}
}
