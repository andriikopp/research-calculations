package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.model.ConnectorsOneDimensionalReductionOptimization;
import edu.bpmanalysis.analysis.model.FunctionsOneDimensionalReductionOptimization;
import edu.bpmanalysis.analysis.model.NodesOneDimensionalReductionOptimization;
import edu.bpmanalysis.analysis.model.RoutingOneDimensionalReductionOptimization;
import edu.bpmanalysis.collection.BPMNModels;
import edu.bpmanalysis.collection.DFDModels;
import edu.bpmanalysis.collection.EPCModels;
import edu.bpmanalysis.collection.IDEF0Models;
import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.description.tools.Model;

import java.util.Arrays;

public class OptimizationApp {

    public static void printModelOptimization(Model model) {
        System.out.printf("%s\t", model.getName());

        double[] nodesChanges = NodesOneDimensionalReductionOptimization.optimization(model);
        double[] connectorsChanges = ConnectorsOneDimensionalReductionOptimization.optimization(model);
        double[][] routingChanges = RoutingOneDimensionalReductionOptimization.optimization(model);
        double[][] functionsChanges = FunctionsOneDimensionalReductionOptimization.optimization(model);

        System.out.printf("\n\tNodes Optimization\n\t%s\n", Arrays.toString(nodesChanges));

        System.out.printf("\tConnectors Optimization\n\t%s\n", Arrays.toString(connectorsChanges));

        System.out.println("\tRouting Optimization");

        for (double[] row : routingChanges) {
            System.out.printf("\t%s\n", Arrays.toString(row));
        }

        System.out.println("\tFunctions Optimization");

        for (double[] row : functionsChanges) {
            System.out.printf("\t%s\n", Arrays.toString(row));
        }
    }

    public static void main(String[] args) {
        Models models = new EPCModels();

        System.out.println("EPC Models Optimization");

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }

        models = new BPMNModels();

        System.out.println("BPMN Models Optimization");

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }

        models = new IDEF0Models();

        System.out.println("IDEF0 Models Optimization");

        for (Model model : models.loadModels()) {
            printModelOptimization(model);
        }

        models = new DFDModels();

        System.out.println("DFD Models Optimization");

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }
    }
}
