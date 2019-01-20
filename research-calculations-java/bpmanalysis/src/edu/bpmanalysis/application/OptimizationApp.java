package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.analysis.model.*;
import edu.bpmanalysis.collection.BPMNModels;
import edu.bpmanalysis.collection.DFDModels;
import edu.bpmanalysis.collection.EPCModels;
import edu.bpmanalysis.collection.IDEF0Models;
import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;

import java.util.Arrays;

public class OptimizationApp {

    public static void printModelOptimization(Model model) {
        System.out.printf("%s\t", model.getName());

        double[] nodesChanges = NodesOneDimensionalReductionOptimization.optimization(model);
        double[] connectorsChanges = ConnectorsSteepestDescentOptimization.optimization(model);
        double[][] routingChanges = RoutingOneDimensionalReductionOptimization.optimization(model);
        double[][] functionsChanges = FunctionsSteepestDescentOptimization.optimization(model);

        if (false) {
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

        double connectorsBalance = new ConnectorsBalance().balanceCoefficient(model);
        double functionsBalance = new FunctionsBalance().balanceCoefficient(model);

        double newSize = model.getNodesList().size() + sum(nodesChanges) + sum(routingChanges);
        double newArcs = ModelDensity.arcs(model) - connectorsBalance - functionsBalance;

        System.out.printf("%.2f\t%.2f\n",
                PredictionModel.understandabilityTime(newSize),
                PredictionModel.modifiabilityTime(0,
                        ModelDensity.density(newArcs, newSize)));
    }

    private static double sum(double[] array) {
        double result = 0;

        for (double value : array) {
            result += value;
        }

        return result;
    }

    private static double sum(double[][] array) {
        double result = 0;

        for (double[] line : array) {
            for (double value : line) {
                result += value;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Models models = new EPCModels();

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }

        models = new BPMNModels();

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }

        models = new IDEF0Models();

        for (Model model : models.loadModels()) {
            printModelOptimization(model);
        }

        models = new DFDModels();

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }
    }
}
