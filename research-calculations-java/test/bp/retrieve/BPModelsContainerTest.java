package bp.retrieve;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bp.retrieve.container.SimpleBPModelsContainer;
import bp.retrieve.container.TableBPModelsContainer;
import bp.retrieve.container.api.BPModelsContainer;

public class BPModelsContainerTest {
	private static final int EXPERIMENTS_AMOUNT = 10;
	private static final List<Integer> REPOSITORY_SIZE_LIST = Arrays.asList(10, 20, 50, 100, 200, 500, 1000, 2000, 5000,
			10000);
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

		storedBPModel.addBPMNStartEvent();
		storedBPModel.addBPMNEndEvent();
		storedBPModel.addFunction("A" + modelCounter);
		storedBPModel.addFunction("B" + modelCounter);
		storedBPModel.addFunction("C" + modelCounter);
		storedBPModel.addFunction("D" + modelCounter);
		storedBPModel.addAndGateway();
		storedBPModel.addDepartment("U" + modelCounter);
		storedBPModel.addSupportingSystem("S" + modelCounter);

		storedBPModel.executes("U" + modelCounter, "A" + modelCounter);
		storedBPModel.executes("U" + modelCounter, "D" + modelCounter);
		storedBPModel.usedBy("S" + modelCounter, "B" + modelCounter);

		storedBPModel.addStartEventSequence("A" + modelCounter);
		storedBPModel.addAndSplit("A" + modelCounter, "B" + modelCounter, "C" + modelCounter);
		storedBPModel.addAndJoin("D" + modelCounter, "B" + modelCounter, "C" + modelCounter);
		storedBPModel.addEndEventSequence("D" + modelCounter);

		return storedBPModel;
	}

	private BPModelRDFGraph getRetrievedModel() {
		BPModelRDFGraph storedBPModel = new BPModelRDFGraph("Model R");

		storedBPModel.addFunction("A1");
		storedBPModel.addFunction("B2");
		storedBPModel.addFunction("C3");
		storedBPModel.addFunction("D4");
		storedBPModel.addAndGateway();
		storedBPModel.addDepartment("U5");
		storedBPModel.addSupportingSystem("S6");

		storedBPModel.executes("U5", "A1");
		storedBPModel.executes("U5", "D4");
		storedBPModel.usedBy("S6", "B2");

		storedBPModel.addStartEventSequence("A1");
		storedBPModel.addAndSplit("A1", "B2", "C3");
		storedBPModel.addAndJoin("D4", "B2", "C3");
		storedBPModel.addEndEventSequence("D4");

		return storedBPModel;
	}
}
