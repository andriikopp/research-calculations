package edu.bpmanalysis.util;

import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;

public class ImplicitDecisionMakingUtil {

    public static void detect(ProcessModelBean processModelBean) {

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {

            String from = edgeBean.getFrom();
            String to = edgeBean.getTo();

            if ((to.toLowerCase().contains("xor#") || to.toLowerCase().contains("or#")) &&
                    !from.toLowerCase().contains("function#")) {

                System.out.println(String.format("%s\t%s\t%s\t%s",
                        processModelBean.getFileName(),
                        from,
                        to));
            }
        }
    }
}
