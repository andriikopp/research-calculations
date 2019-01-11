package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.analysis.model.ConnectorsSteepestDescentOptimization;
import edu.bpmanalysis.analysis.model.FunctionsSteepestDescentOptimization;
import edu.bpmanalysis.collection.BPMNModels;
import edu.bpmanalysis.collection.DFDModels;
import edu.bpmanalysis.collection.EPCModels;
import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;

import java.util.Arrays;

public class OptimizationApp {
    private static Balance connectorsBalance = new ConnectorsBalance();
    private static Balance functionsBalance = new FunctionsBalance();

    public static void printModelOptimization(Model model) {
        double cBalance = connectorsBalance.balanceCoefficient(model);
        double fBalance = functionsBalance.balanceCoefficient(model);

        if (cBalance > 0 || fBalance > 0) {
            System.out.println(model.getName());
        }

        if (cBalance > 0) {
            System.out.printf("\tConnectors Optimization\n\t%s\n",
                    Arrays.toString(ConnectorsSteepestDescentOptimization.optimization(model)));
        }

        if (fBalance > 0) {
            System.out.println("\tFunctions Optimization");

            for (double[] row : FunctionsSteepestDescentOptimization.optimization(model)) {
                System.out.printf("\t%s\n", Arrays.toString(row));
            }
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

        models = new DFDModels();

        System.out.println("DFD Models Optimization");

        for (Model model : models.importModels()) {
            printModelOptimization(model);
        }
    }
}
