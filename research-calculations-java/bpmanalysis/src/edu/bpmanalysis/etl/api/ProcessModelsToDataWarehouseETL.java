package edu.bpmanalysis.etl.api;

import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.etl.beans.ProcessModelAnalysisBean;

import java.util.List;

public interface ProcessModelsToDataWarehouseETL {

    List<Model> extract();

    List<ProcessModelAnalysisBean> transform(List<Model> models);

    void load(List<ProcessModelAnalysisBean> processModelAnalysisBeans);
}
