package bp.retrieve;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import bp.AppProperties;
import bp.storing.beans.Model;
import bp.storing.beans.Process;
import bp.storing.dao.api.IModelDAO;
import bp.storing.dao.api.IProcessDAO;

/**
 * Provides a tree of processes and related models.
 * 
 * @author Andrii Kopp
 */
public class BPModelsTree {
	private static final String RETRIEVE_PROCESSES = AppProperties.INSTANCE.getProperty("retrProcesses");

	private IProcessDAO processDAO;
	private IModelDAO modelDAO;

	public BPModelsTree(IProcessDAO processDAO, IModelDAO modelDAO) {
		this.processDAO = processDAO;
		this.modelDAO = modelDAO;
	}

	/**
	 * Returns an instance of tree of processes and related models.
	 * 
	 * @return - a tree of processes and related models.
	 */
	public DefaultMutableTreeNode generateModelsTree() {
		List<Process> processes = processDAO.retrieve();
		List<Model> models = modelDAO.retrieve();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(RETRIEVE_PROCESSES);

		for (Process process : processes) {
			DefaultMutableTreeNode processNode = new DefaultMutableTreeNode(process);

			for (Model model : models) {
				if (model.getProcess().equals(process.getId())) {
					processNode.add(new DefaultMutableTreeNode(model));
				}
			}

			root.add(processNode);
		}

		return root;
	}
}
