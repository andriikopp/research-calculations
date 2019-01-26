package edu.bpmanalysis.application;

import edu.bpmanalysis.etl.BPMNModelsToMySQLDataWarehouseETL;
import edu.bpmanalysis.etl.api.ProcessModelsToDataWarehouseETL;

public class ETLApp {

    public static void main(String[] args) {
        ProcessModelsToDataWarehouseETL processModelsToDataWarehouseETL = new BPMNModelsToMySQLDataWarehouseETL();

        processModelsToDataWarehouseETL
                .load(processModelsToDataWarehouseETL
                        .transform(processModelsToDataWarehouseETL
                                .extract()));
    }
}
