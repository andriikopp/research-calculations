package bp.retrieve;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bp.retrieve.container.SimpleBPModelsContainer;
import bp.retrieve.container.TableBPModelsContainer;
import bp.retrieve.container.api.BPModelsContainer;
import bp.storing.BPModelValidator;

public class BPModelsContainerTest {
	private static final int EXPERIMENTS_AMOUNT = 10;
	private static final List<Integer> REPOSITORY_SIZE_LIST = Arrays.asList(10, 20, 50, 100, 200, 500, 1000, 2000,
			5000);
	private static final int STORE_SIZE = 10000;

	private BPModelsSimilarity bpModelsSimilarity;

	public BPModelsContainerTest() {
		bpModelsSimilarity = new BPModelsSimilarity();
	}

	@Test
	public void testPerformanceOfHashBPModelsContainerRetrieve() {
		BPModelsSimilarity.setLoggingOff();

		for (int experiment = 1; experiment <= EXPERIMENTS_AMOUNT; experiment++) {
			System.out.println("[" + experiment + "]\nHash container retrieve performance:");
			retrieveExperiment(new TableBPModelsContainer(bpModelsSimilarity));
		}
	}

	@Test
	public void testPerformanceOfListBPModelsContainerRetrieve() {
		BPModelsSimilarity.setLoggingOff();

		for (int experiment = 1; experiment <= EXPERIMENTS_AMOUNT; experiment++) {
			System.out.println("[" + experiment + "]\nList container retrieve performance:");
			retrieveExperiment(new SimpleBPModelsContainer(bpModelsSimilarity));
		}
	}

	@Test
	public void testPerformanceOfHashBPModelsContainerStore() {
		BPModelsSimilarity.setLoggingOff();
		System.out.println("Hash container store performance:");
		storeExperiment(new TableBPModelsContainer(bpModelsSimilarity));
	}

	@Test
	public void testPerformanceOfListBPModelsContainerStore() {
		BPModelsSimilarity.setLoggingOff();
		System.out.println("List container store performance:");
		storeExperiment(new SimpleBPModelsContainer(bpModelsSimilarity));
	}

	private void storeExperiment(BPModelsContainer bpModelsContainer) {
		bpModelsContainer.clear();

		for (int modelCounter = 1; modelCounter <= STORE_SIZE; modelCounter++) {
			long before = System.nanoTime();

			bpModelsContainer.storeBPModel(getStoredBPModel(modelCounter));

			long now = System.nanoTime();
			long time = now - before;

			System.out.println(modelCounter + "\t" + time);
		}
	}

	private void retrieveExperiment(BPModelsContainer bpModelsContainer) {
		for (int repositorySize : REPOSITORY_SIZE_LIST) {
			bpModelsContainer.clear();

			for (int modelCounter = 1; modelCounter <= repositorySize - 1; modelCounter++) {
				bpModelsContainer.storeBPModel(getStoredBPModel(modelCounter));
			}

			System.out.println("\tRepository size: " + repositorySize);

			BPModelRDFGraph retrievedModel = getRetrievedModel();
			bpModelsContainer.storeBPModel(retrievedModel);

			long before = System.nanoTime();

			Map<BPModelRDFGraph, Double> retrievedModels = bpModelsContainer.retrieveBPModels(retrievedModel);

			long now = System.nanoTime();
			long time = now - before;

			System.out.println("\tTime, ns: " + time);
			System.out.println("\tOperations: " + bpModelsContainer.getOperationsCount());
			System.out.println("\t\tRetrieved models: " + retrievedModels.size());
		}
	}

	private BPModelRDFGraph getStoredBPModel(int modelCounter) {
		BPModelRDFGraph storedBPModel = new BPModelRDFGraph("Model " + modelCounter);

		storedBPModel.addStatement("A" + modelCounter, BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
		storedBPModel.addStatement("B" + modelCounter, BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
		storedBPModel.addStatement("C" + modelCounter, BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
		storedBPModel.addStatement("D" + modelCounter, BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);

		storedBPModel.addStatement("U" + modelCounter, BPModelValidator.PR_TYPE, BPModelValidator.RES_DEPARTMENT);

		storedBPModel.addStatement("S" + modelCounter, BPModelValidator.PR_TYPE,
				BPModelValidator.RES_SUPPORTING_SYSTEM);

		storedBPModel.addStatement("AND", BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);

		storedBPModel.addStatement("U" + modelCounter, BPModelValidator.PR_EXECUTES, "A" + modelCounter);
		storedBPModel.addStatement("U" + modelCounter, BPModelValidator.PR_EXECUTES, "D" + modelCounter);

		storedBPModel.addStatement("S" + modelCounter, BPModelValidator.PR_USED_BY, "B" + modelCounter);

		storedBPModel.addStatement("A" + modelCounter, BPModelValidator.PR_TRIGGERS, "AND");
		storedBPModel.addStatement("AND", BPModelValidator.PR_TRIGGERS, "B" + modelCounter);
		storedBPModel.addStatement("AND", BPModelValidator.PR_TRIGGERS, "C" + modelCounter);
		storedBPModel.addStatement("B" + modelCounter, BPModelValidator.PR_TRIGGERS, "AND");
		storedBPModel.addStatement("C" + modelCounter, BPModelValidator.PR_TRIGGERS, "AND");
		storedBPModel.addStatement("AND", BPModelValidator.PR_TRIGGERS, "D" + modelCounter);

		return storedBPModel;
	}

	private BPModelRDFGraph getRetrievedModel() {
		BPModelRDFGraph storedBPModel = new BPModelRDFGraph("Model R");

		storedBPModel.addStatement("A1", BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
		storedBPModel.addStatement("B2", BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
		storedBPModel.addStatement("C3", BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
		storedBPModel.addStatement("D4", BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);

		storedBPModel.addStatement("U5", BPModelValidator.PR_TYPE, BPModelValidator.RES_DEPARTMENT);

		storedBPModel.addStatement("S6", BPModelValidator.PR_TYPE, BPModelValidator.RES_SUPPORTING_SYSTEM);

		storedBPModel.addStatement("AND", BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);

		storedBPModel.addStatement("U5", BPModelValidator.PR_EXECUTES, "A1");
		storedBPModel.addStatement("U5", BPModelValidator.PR_EXECUTES, "D4");

		storedBPModel.addStatement("S6", BPModelValidator.PR_USED_BY, "B2");

		storedBPModel.addStatement("A1", BPModelValidator.PR_TRIGGERS, "AND");
		storedBPModel.addStatement("AND", BPModelValidator.PR_TRIGGERS, "B2");
		storedBPModel.addStatement("AND", BPModelValidator.PR_TRIGGERS, "C3");
		storedBPModel.addStatement("B2", BPModelValidator.PR_TRIGGERS, "AND");
		storedBPModel.addStatement("C3", BPModelValidator.PR_TRIGGERS, "AND");
		storedBPModel.addStatement("AND", BPModelValidator.PR_TRIGGERS, "D4");

		return storedBPModel;
	}
}
