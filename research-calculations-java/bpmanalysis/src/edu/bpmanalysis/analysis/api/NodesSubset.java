package edu.bpmanalysis.analysis.api;

import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.metamodel.Node;

import java.util.List;

public interface NodesSubset {

    List<Node> getSubset(Model model);
}
