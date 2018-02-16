package bp.similarity;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
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

import bp.AppProperties;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;

public class NewRDFModelFrame extends JFrame {
	private static final String[] FLOW_OBJECT_TYPES = new String[] { "Activity", "SubProcess", "Gateway", "Event" };
	private static final String BUTTON_CLEAR = AppProperties.INSTANCE.getProperty("buttonClear");
	private static final String DEFAULT_MODEL_EXT_NAME = AppProperties.INSTANCE.getProperty("defaultModelFileExtName");
	private static final String DEFAULT_MODEL_FILE_NAME = AppProperties.INSTANCE.getProperty("defaultModelFileName");
	private static final String TRIPLESTORE_PATH = AppProperties.INSTANCE.getProperty("triplestorePath");
	private static final String MESSAGE_EMPTY_SET = AppProperties.INSTANCE.getProperty("messageEmptySet");
	private static final String BUTTON_SAVE = AppProperties.INSTANCE.getProperty("buttonSave");
	private static final String BUTTON_ADD = AppProperties.INSTANCE.getProperty("buttonAdd");
	private static final String LABEL_OBJECT = AppProperties.INSTANCE.getProperty("labelObject");
	private static final String LABEL_SUBJECT = AppProperties.INSTANCE.getProperty("labelSubject");
	private static final String WINDOW_TITLE = AppProperties.INSTANCE.getProperty("windowTitle");
	private static final String CHCKBX_DECLARE_FLOW_OBJECT = AppProperties.INSTANCE
			.getProperty("chckbxDeclareFlowObject");
	private static final String PROPERTY_CONNECTING_OBJECT = "ConnectingObject";
	private static final String PROPERTY_TYPE = "type";

	private static final long serialVersionUID = 1L;

	private RDFStatementsContainer rdfContainer;

	private JPanel contentPane;
	private JTextField textFieldSubject;
	private JTextField textFieldObject;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewRDFModelFrame frame = new NewRDFModelFrame();
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
	public NewRDFModelFrame() {
		setResizable(false);
		rdfContainer = new RDFStatementsContainer();

		setTitle(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 400);
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
		textFieldObject.setBounds(106, 39, 220, 20);
		contentPane.add(textFieldObject);
		textFieldObject.setColumns(10);
		textFieldObject.setEnabled(false);

		JLabel lblObject = new JLabel(LABEL_OBJECT);
		lblObject.setBounds(10, 42, 86, 14);
		contentPane.add(lblObject);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 104, 504, 214);
		contentPane.add(scrollPane);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(FLOW_OBJECT_TYPES));
		comboBox.setBounds(336, 39, 178, 20);
		contentPane.add(comboBox);

		JCheckBox chckbxDeclareFlowObject = new JCheckBox(CHCKBX_DECLARE_FLOW_OBJECT);
		chckbxDeclareFlowObject.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (chckbxDeclareFlowObject.isSelected()) {
					textFieldObject.setEnabled(false);
					comboBox.setEnabled(true);
				} else {
					textFieldObject.setEnabled(true);
					comboBox.setEnabled(false);
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
					object = comboBox.getSelectedItem().toString();
				} else {
					property = PROPERTY_CONNECTING_OBJECT;
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
		btnAddStatement.setBounds(106, 70, 130, 23);
		contentPane.add(btnAddStatement);

		JButton btnSaveDescription = new JButton(BUTTON_SAVE);
		btnSaveDescription.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
					}
				}
			}
		});
		btnSaveDescription.setBounds(10, 327, 130, 23);
		contentPane.add(btnSaveDescription);

		JButton btnClearAll = new JButton(BUTTON_CLEAR);
		btnClearAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setText("");
				rdfContainer.clearStatements();
			}
		});
		btnClearAll.setBounds(150, 327, 100, 23);
		contentPane.add(btnClearAll);
	}
}
