package bp.retrieve.ui;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;

import bp.AppContext;
import bp.AppProperties;
import bp.retrieve.BPModelsSimilarity;
import bp.retrieve.BPModelsTree;
import bp.storing.beans.Model;
import bp.storing.beans.Process;
import bp.storing.dao.api.IModelDAO;
import bp.storing.dao.api.IProcessDAO;
import bp.storing.ui.BPModelStoringFrame;
import javax.swing.JButton;

/**
 * Form provides retrieve business process models.
 * 
 * @author Andrii Kopp
 */
public class BPModelRetrieveFrame extends JFrame {
	private static final String RETRIEVE_TITLE = AppProperties.INSTANCE.getProperty("retrTitle");
	private static final String RETRIEVE_FILE = AppProperties.INSTANCE.getProperty("retrFile");
	private static final String RETRIEVE_STORE = AppProperties.INSTANCE.getProperty("retrStore");
	private static final int MODEL_PATH = 3;
	private static final String RETRIEVE_PNAME = AppProperties.INSTANCE.getProperty("retrPName");
	private static final String RETRIEVE_PDESCR = AppProperties.INSTANCE.getProperty("retrPDescr");
	private static final String RETRIEVE_RMODELS = AppProperties.INSTANCE.getProperty("retrRModels");
	private static final String RETRIEVE_MFILE = AppProperties.INSTANCE.getProperty("retrMFile");
	private static final String RETRIEVE_RSIMILAR = AppProperties.INSTANCE.getProperty("retrRSimilar");
	private static final String RETRIEVE_SETTINGS = AppProperties.INSTANCE.getProperty("retrSettings");

	private static final long serialVersionUID = 1L;

	private BPModelsTree modelsTree;
	private BPModelsSimilarity modelsSimilarity;

	private IProcessDAO processDAO = (IProcessDAO) AppContext.CONTEXT.getBean("processDAO");
	private IModelDAO modelDAO = (IModelDAO) AppContext.CONTEXT.getBean("modelDAO");

	private JPanel contentPane;
	private JTextField textFieldPName;
	private JTextField textFieldMFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BPModelRetrieveFrame frame = new BPModelRetrieveFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BPModelRetrieveFrame() {
		modelsTree = new BPModelsTree(processDAO, modelDAO);
		modelsSimilarity = new BPModelsSimilarity(processDAO, modelDAO);

		setResizable(false);
		setTitle(RETRIEVE_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 450);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(RETRIEVE_FILE);
		menuBar.add(mnFile);

		JMenuItem mntmStoreModel = new JMenuItem(RETRIEVE_STORE);
		mntmStoreModel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				BPModelStoringFrame frame = new BPModelStoringFrame();
				frame.setVisible(true);
			}
		});
		mnFile.add(mntmStoreModel);

		JMenu mnSettings = new JMenu(RETRIEVE_SETTINGS);
		mnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RetrieveSettingsFrame frame = new RetrieveSettingsFrame(modelsSimilarity);
				frame.setVisible(true);
			}
		});
		menuBar.add(mnSettings);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textFieldPName = new JTextField();
		textFieldPName.setEditable(false);
		textFieldPName.setBounds(410, 11, 250, 20);
		contentPane.add(textFieldPName);
		textFieldPName.setColumns(10);

		JLabel lblProcessName = new JLabel(RETRIEVE_PNAME);
		lblProcessName.setBounds(260, 14, 140, 14);
		contentPane.add(lblProcessName);

		JScrollPane scrollPaneTree = new JScrollPane();
		scrollPaneTree.setBounds(10, 11, 240, 375);
		contentPane.add(scrollPaneTree);

		JScrollPane scrollPanePDescr = new JScrollPane();
		scrollPanePDescr.setBounds(410, 42, 250, 80);
		contentPane.add(scrollPanePDescr);

		JTextArea textAreaPDescr = new JTextArea();
		textAreaPDescr.setEditable(false);
		scrollPanePDescr.setViewportView(textAreaPDescr);

		JLabel lblProcessDescription = new JLabel(RETRIEVE_PDESCR);
		lblProcessDescription.setBounds(260, 43, 140, 14);
		contentPane.add(lblProcessDescription);

		JScrollPane scrollPaneRetr = new JScrollPane();
		scrollPaneRetr.setBounds(410, 198, 394, 188);
		contentPane.add(scrollPaneRetr);

		JTree treeRetr = new JTree();
		scrollPaneRetr.setViewportView(treeRetr);
		treeRetr.setModel(null);

		JLabel lblRetrievedModels = new JLabel(RETRIEVE_RMODELS);
		lblRetrievedModels.setBounds(260, 200, 140, 14);
		contentPane.add(lblRetrievedModels);

		textFieldMFile = new JTextField();
		textFieldMFile.setEditable(false);
		textFieldMFile.setBounds(410, 133, 250, 20);
		contentPane.add(textFieldMFile);
		textFieldMFile.setColumns(10);

		JLabel lblModelFile = new JLabel(RETRIEVE_MFILE);
		lblModelFile.setBounds(260, 136, 140, 14);
		contentPane.add(lblModelFile);

		JButton btnRetrieveSimilarModels = new JButton(RETRIEVE_RSIMILAR);
		btnRetrieveSimilarModels.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String modelFile = textFieldMFile.getText();

				if (modelFile != null && !modelFile.isEmpty()) {
					treeRetr.setModel(new DefaultTreeModel(modelsSimilarity.getSimilarModels(modelFile)));
				}
			}
		});
		btnRetrieveSimilarModels.setBounds(410, 164, 180, 23);
		contentPane.add(btnRetrieveSimilarModels);

		JTree tree = new JTree();
		scrollPaneTree.setViewportView(tree);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int path = tree.getSelectionPath().getPath().length;

				if (path == MODEL_PATH) {
					String modelName = tree.getSelectionPath().getLastPathComponent().toString();

					Model model = modelDAO.retrieveByName(modelName);
					Process process = processDAO.retrieveById(model.getProcess());

					textFieldPName.setText(process.getName());
					textAreaPDescr.setText(process.getDescription());
					textFieldMFile.setText(model.getFile());
				}
			}
		});

		tree.setModel(new DefaultTreeModel(modelsTree.generateModelsTree()));
	}
}
