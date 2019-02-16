package edu.bpmanalysis.analysis.api;

import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

import java.util.List;

public interface NodesSubset {

    List<Node> getSubset(Model model);
}
