package bp.storing.ui;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.UUID;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import bp.AppContext;
import bp.AppProperties;
import bp.storing.BPModelValidator;
import bp.storing.RDFStatementsContainer;
import bp.storing.dao.api.IModelDAO;
import bp.storing.dao.api.IProcessDAO;
import bp.storing.beans.Model;
import bp.storing.beans.Process;

/**
 * Form provides business process models storing.
 * 
 * @author Andrii Kopp
 */
public class BPModelStoringFrame extends JFrame {
	private static final String[] PREDICATE_PROPERTIES = new String[] { BPModelValidator.PR_TRIGGERS,
			BPModelValidator.PR_USED_BY, BPModelValidator.PR_EXECUTES, BPModelValidator.PR_IS_INPUT_FOR,
			BPModelValidator.PR_IS_OUTPUT_OF, BPModelValidator.PR_MEASURES };
	private static final String[] RESOURCE_TYPES = new String[] { BPModelValidator.RES_FUNCTION,
			BPModelValidator.RES_PROCESS, BPModelValidator.RES_GATEWAY, BPModelValidator.RES_EVENT,
			BPModelValidator.RES_ROLE, BPModelValidator.RES_DEPARTMENT, BPModelValidator.RES_SUPPORTING_SYSTEM,
			BPModelValidator.RES_BUSINESS_OBJECT, BPModelValidator.RES_KPI };
	private static final String BUTTON_CLEAR = AppProperties.INSTANCE.getProperty("buttonClear");
	private static final String DEFAULT_MODEL_EXT_NAME = AppProperties.INSTANCE.getProperty("defaultModelFileExtName");
	private static final String DEFAULT_MODEL_FILE_NAME = AppProperties.INSTANCE.getProperty("defaultModelFileName");
	private static final String TRIPLESTORE_PATH = AppProperties.INSTANCE.getProperty("triplestorePath");
	private static final String MESSAGE_EMPTY_SET = AppProperties.INSTANCE.getProperty("messageEmptySet");
	private static final String BUTTON_SAVE = AppProperties.INSTANCE.getProperty("buttonSave");
	private static final String BUTTON_ADD = AppProperties.INSTANCE.getProperty("buttonAdd");
	private static final String LABEL_OBJECT = AppProperties.INSTANCE.getProperty("labelObject");
	private static final String LABEL_SUBJECT = AppProperties.INSTANCE.getProperty("labelSubject");
	private static final String LABEL_PREDICATE = AppProperties.INSTANCE.getProperty("labelPredicate");
	private static final String LABEL_SELECT_PROCESS = AppProperties.INSTANCE.getProperty("labelSelectProcess");
	private static final String LABEL_NEW_PROCESS = AppProperties.INSTANCE.getProperty("labelNewProcess");
	private static final String LABEL_PROCESS_NAME = AppProperties.INSTANCE.getProperty("labelProcessName");
	private static final String LABEL_PROCESS_DESCR = AppProperties.INSTANCE.getProperty("labelProcessDescr");
	private static final String WINDOW_TITLE = AppProperties.INSTANCE.getProperty("windowTitle");
	private static final String CHCKBX_DECLARE_FLOW_OBJECT = AppProperties.INSTANCE
			.getProperty("chckbxDeclareFlowObject");
	private static final String PROPERTY_TYPE = "type";

	private static final long serialVersionUID = 1L;

	private RDFStatementsContainer rdfContainer;
	private IProcessDAO processDAO = (IProcessDAO) AppContext.CONTEXT.getBean("processDAO");
	private IModelDAO modelDAO = (IModelDAO) AppContext.CONTEXT.getBean("modelDAO");

	private JPanel contentPane;
	private JTextField textFieldSubject;
	private JTextField textFieldObject;
	private JTextField textFieldProcessName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BPModelStoringFrame frame = new BPModelStoringFrame();
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
	public BPModelStoringFrame() {
		setResizable(false);
		rdfContainer = new RDFStatementsContainer();

		setTitle(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSubject = new JLabel(LABEL_SUBJECT);
		lblSubject.setBounds(10, 11, 86, 14);
		contentPane.add(lblSubject);

		textFieldSubject = new JTextField();
		textFieldSubject.setBounds(106, 8, 220, 20);
		contentPane.add(textFieldSubject);
		textFieldSubject.setColumns(10);

		textFieldObject = new JTextField();
		textFieldObject.setBounds(106, 70, 220, 20);
		contentPane.add(textFieldObject);
		textFieldObject.setColumns(10);
		textFieldObject.setEnabled(false);

		JLabel lblObject = new JLabel(LABEL_OBJECT);
		lblObject.setBounds(10, 73, 86, 14);
		contentPane.add(lblObject);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 135, 504, 183);
		contentPane.add(scrollPane);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JComboBox flowObjectComboBox = new JComboBox();
		flowObjectComboBox.setModel(new DefaultComboBoxModel(RESOURCE_TYPES));
		flowObjectComboBox.setBounds(336, 70, 178, 20);
		contentPane.add(flowObjectComboBox);

		JComboBox predicateComboBox = new JComboBox();
		predicateComboBox.setModel(new DefaultComboBoxModel(PREDICATE_PROPERTIES));
		predicateComboBox.setBounds(106, 39, 220, 20);
		contentPane.add(predicateComboBox);
		predicateComboBox.setEnabled(false);

		JLabel lblPredicate = new JLabel(LABEL_PREDICATE);
		lblPredicate.setBounds(10, 42, 86, 14);
		contentPane.add(lblPredicate);

		JLabel lblSelectProcess = new JLabel(LABEL_SELECT_PROCESS);
		lblSelectProcess.setBounds(10, 332, 130, 14);
		contentPane.add(lblSelectProcess);

		JComboBox selectProcessComboBox = new JComboBox();
		selectProcessComboBox.setModel(new DefaultComboBoxModel(processDAO.retrieve().toArray()));
		selectProcessComboBox.setBounds(150, 329, 364, 20);
		contentPane.add(selectProcessComboBox);

		textFieldProcessName = new JTextField();
		textFieldProcessName.setBounds(150, 383, 364, 20);
		contentPane.add(textFieldProcessName);
		textFieldProcessName.setColumns(10);
		textFieldProcessName.setEnabled(false);

		JLabel lblProcessName = new JLabel(LABEL_PROCESS_NAME);
		lblProcessName.setBounds(10, 386, 130, 14);
		contentPane.add(lblProcessName);

		JScrollPane processDescrScrollPane = new JScrollPane();
		processDescrScrollPane.setBounds(150, 414, 364, 52);
		contentPane.add(processDescrScrollPane);

		JTextArea processDescrTextArea = new JTextArea();
		processDescrScrollPane.setViewportView(processDescrTextArea);
		processDescrTextArea.setEnabled(false);

		JLabel lblProcessDescription = new JLabel(LABEL_PROCESS_DESCR);
		lblProcessDescription.setBounds(10, 415, 130, 14);
		contentPane.add(lblProcessDescription);

		JCheckBox chckbxDeclareFlowObject = new JCheckBox(CHCKBX_DECLARE_FLOW_OBJECT);
		chckbxDeclareFlowObject.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (chckbxDeclareFlowObject.isSelected()) {
					textFieldObject.setEnabled(false);
					predicateComboBox.setEnabled(false);
					flowObjectComboBox.setEnabled(true);
				} else {
					textFieldObject.setEnabled(true);
					predicateComboBox.setEnabled(true);
					flowObjectComboBox.setEnabled(false);
				}
			}
		});
		chckbxDeclareFlowObject.setBounds(332, 7, 182, 23);
		chckbxDeclareFlowObject.setSelected(true);
		contentPane.add(chckbxDeclareFlowObject);

		JButton btnAddStatement = new JButton(BUTTON_ADD);
		btnAddStatement.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String subject = textFieldSubject.getText();
				String property;
				String object;

				if (chckbxDeclareFlowObject.isSelected()) {
					property = PROPERTY_TYPE;
					object = flowObjectComboBox.getSelectedItem().toString();
				} else {
					property = predicateComboBox.getSelectedItem().toString();
					object = textFieldObject.getText();
				}

				try {
					rdfContainer.addStatement(subject, property, object);
					textArea.setText("");

					for (String statement : rdfContainer.getStatements()) {
						textArea.append(statement + "\n");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		btnAddStatement.setBounds(106, 101, 130, 23);
		contentPane.add(btnAddStatement);

		JCheckBox chckbxOrCreateNew = new JCheckBox(LABEL_NEW_PROCESS);
		chckbxOrCreateNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (chckbxOrCreateNew.isSelected()) {
					selectProcessComboBox.setEnabled(false);
					textFieldProcessName.setEnabled(true);
					processDescrTextArea.setEnabled(true);
				} else {
					selectProcessComboBox.setEnabled(true);
					textFieldProcessName.setEnabled(false);
					processDescrTextArea.setEnabled(false);
				}
			}
		});
		chckbxOrCreateNew.setBounds(10, 353, 200, 23);
		contentPane.add(chckbxOrCreateNew);

		JButton btnSaveDescription = new JButton(BUTTON_SAVE);
		btnSaveDescription.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (rdfContainer.getStatements().isEmpty()) {
						JOptionPane.showMessageDialog(null, MESSAGE_EMPTY_SET);
					} else {
						JFileChooser saveFileDialog = new JFileChooser();

						saveFileDialog.setCurrentDirectory(new File(TRIPLESTORE_PATH));
						saveFileDialog.setSelectedFile(new File(DEFAULT_MODEL_FILE_NAME));
						saveFileDialog.setFileFilter(new FileNameExtensionFilter(DEFAULT_MODEL_EXT_NAME, "nt"));

						int result = saveFileDialog.showSaveDialog(null);

						if (result == JFileChooser.APPROVE_OPTION) {
							File targetFile = saveFileDialog.getSelectedFile();
							rdfContainer.saveStatements(targetFile);

							String processId = null;

							if (chckbxOrCreateNew.isSelected()) {
								processId = UUID.randomUUID().toString();

								String processName = textFieldProcessName.toString();
								String processDescr = processDescrTextArea.toString();

								processDAO.store(new Process(processId, processName, processDescr));
							} else {
								String processName = selectProcessComboBox.getSelectedItem().toString();

								processId = processDAO.retrieveByName(processName).getId();
							}

							String modelId = UUID.randomUUID().toString();
							String fileName = targetFile.getName();

							modelDAO.store(new Model(modelId, processId, fileName));
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		btnSaveDescription.setBounds(10, 477, 130, 23);
		contentPane.add(btnSaveDescription);

		JButton btnClearAll = new JButton(BUTTON_CLEAR);
		btnClearAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setText("");
				rdfContainer.clearStatements();
			}
		});
		btnClearAll.setBounds(150, 477, 100, 23);
		contentPane.add(btnClearAll);
	}
}
