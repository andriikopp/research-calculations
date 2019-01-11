package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.model.ConnectorsSteepestDescentOptimization;
import edu.bpmanalysis.collection.BPMNModels;
import edu.bpmanalysis.collection.EPCModels;
import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;

import java.util.Arrays;

public class OptimizationApp {

    public static void main(String[] args) {
        Models models = new EPCModels();

        System.out.println("EPC Models Connectors Optimization");

        for (Model model : models.importModels()) {
            if (new ConnectorsBalance().balanceCoefficient(model) > 0) {
                System.out.printf("%s\t%s\n", model.getName(),
                        Arrays.toString(ConnectorsSteepestDescentOptimization.optimization(model)));
            }
        }

        models = new BPMNModels();

        System.out.println("BPMN Models Connectors Optimization");

        for (Model model : models.importModels()) {
            if (new ConnectorsBalance().balanceCoefficient(model) > 0) {
                System.out.printf("%s\t%s\n", model.getName(),
                        Arrays.toString(ConnectorsSteepestDescentOptimization.optimization(model)));
            }
        }
    }
}
