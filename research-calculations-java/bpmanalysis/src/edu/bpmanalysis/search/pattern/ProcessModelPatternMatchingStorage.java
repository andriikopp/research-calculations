package edu.bpmanalysis.search.pattern;

import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.search.bean.ProcessModelPatternMatchingBean;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProcessModelPatternMatchingStorage {
    private static Model model = ModelFactory.createDefaultModel();

    public static final String URI_PREFIX = "http://";

    public static void loadModels(ProcessModelRepository processModelRepository) {
        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            addModel(processModelBean);
        }
    }

    public static ProcessModelBean getGraph(String modelId) {
        List<ProcessModelPatternMatchingBean> beans = new ArrayList<>();

        StmtIterator iterator = model.listStatements();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            if (statement.getSubject().toString().contains(modelId)) {
                ProcessModelPatternMatchingBean processModelPatternMatchingBean = new ProcessModelPatternMatchingBean();
                processModelPatternMatchingBean.setSubject(statement.getSubject().toString());
                processModelPatternMatchingBean.setProperty(statement.getPredicate().toString());
                processModelPatternMatchingBean.setObject(statement.getObject().toString());

                beans.add(processModelPatternMatchingBean);
            }
        }

        ProcessModelBean processModelBean = new ProcessModelBean();
        ProcessModelGraphBean processModelGraphBean = new ProcessModelGraphBean();
        processModelBean.setGraph(processModelGraphBean);
        List<ProcessModelGraphNodeBean> nodeBeans = new ArrayList<>();
        List<ProcessModelGraphEdgeBean> edgeBeans = new ArrayList<>();
        processModelGraphBean.setNodes(nodeBeans);
        processModelGraphBean.setEdges(edgeBeans);

        for (ProcessModelPatternMatchingBean bean : beans) {
            bean.setSubject(bean.getSubject().replace(URI_PREFIX + modelId + "/", ""));
            bean.setProperty(bean.getProperty().replace(URI_PREFIX + modelId + "/", ""));
            bean.setObject(bean.getObject().replace(URI_PREFIX + modelId + "/", ""));

            ProcessModelGraphEdgeBean graphEdgeBean = new ProcessModelGraphEdgeBean();
            graphEdgeBean.setId(UUID.randomUUID().toString());
            graphEdgeBean.setFrom(bean.getSubject());
            graphEdgeBean.setTo(bean.getObject());
            graphEdgeBean.setLabel(bean.getProperty());
            graphEdgeBean.setArrows("to");
            edgeBeans.add(graphEdgeBean);

            ProcessModelGraphNodeBean subjectBean = new ProcessModelGraphNodeBean();
            subjectBean.setId(bean.getSubject());
            subjectBean.setLabel(bean.getSubject());

            subjectBean.setColor(ProcessModelImportUtil.getNodeColorById(bean.getSubject()));

            if (!nodeBeans.contains(subjectBean)) {
                nodeBeans.add(subjectBean);
            }

            ProcessModelGraphNodeBean objectBean = new ProcessModelGraphNodeBean();
            objectBean.setId(bean.getObject());
            objectBean.setLabel(bean.getObject());

            objectBean.setColor(ProcessModelImportUtil.getNodeColorById(bean.getObject()));

            if (!nodeBeans.contains(objectBean)) {
                nodeBeans.add(objectBean);
            }
        }

        return processModelBean;
    }

    public static void addModel(ProcessModelBean processModelBean) {
        for (ProcessModelGraphEdgeBean processModelGraphEdgeBean : processModelBean.getGraph().getEdges()) {
            Resource subject = model.createResource(URI_PREFIX +
                    processModelBean.getId() + "/" + processModelGraphEdgeBean.getFrom());
            Property property = model.createProperty(URI_PREFIX +
                    processModelBean.getId() + "/" + processModelGraphEdgeBean.getLabel());
            Resource object = model.createResource(URI_PREFIX +
                    processModelBean.getId() + "/" + processModelGraphEdgeBean.getTo());

            subject.addProperty(property, object);
        }
    }
}
