package bp.retrieve.ui;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import bp.AppProperties;
import bp.retrieve.BPModelsSimilarity;

/**
 * Form allows to configure retrieve settings.
 * 
 * @author Andrii Kopp
 */
public class RetrieveSettingsFrame extends JFrame {
	private static final String SETTINGS_TITLE = AppProperties.INSTANCE.getProperty("settTitle");
	private static final String APPLY = AppProperties.INSTANCE.getProperty("settApply");
	private static final String LEVEL = AppProperties.INSTANCE.getProperty("settLevel");
	private static final String STRUCTURE = AppProperties.INSTANCE.getProperty("settStr");
	private static final String SEMANTIC = AppProperties.INSTANCE.getProperty("settSem");
	private static final String SIMILARITY_COEFFICIENTS = AppProperties.INSTANCE.getProperty("settSimCoeffs");
	private static final String DOMAIN_COEFFICIENTS = AppProperties.INSTANCE.getProperty("settDomCoeffs");
	private static final String KPIS = AppProperties.INSTANCE.getProperty("settKPIs");
	private static final String OUTPUTS = AppProperties.INSTANCE.getProperty("settOut");
	private static final String INPUTS = AppProperties.INSTANCE.getProperty("settIn");
	private static final String FLOW_OBJECTS = AppProperties.INSTANCE.getProperty("settFObj");
	private static final String SUPPORTING_SYSTEMS = AppProperties.INSTANCE.getProperty("settSSyst");
	private static final String ORGANIZATIONAL_UNITS = AppProperties.INSTANCE.getProperty("settOUnit");

	private static final long serialVersionUID = 1L;

	private static final String[] SAATY_SCALE = new String[] { "1", "3", "5", "7", "9" };
	
	private JPanel contentPane;
	private JTextField textFieldOU;
	private JTextField textFieldSS;
	private JTextField textFieldFO;
	private JTextField textFieldIn;
	private JTextField textFieldOut;
	private JTextField textFieldKPIs;
	private JTextField textFieldSem;
	private JTextField textFieldStr;
	private JSpinner spinnerLevel;

	private BPModelsSimilarity modelsSimilarity;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RetrieveSettingsFrame frame = new RetrieveSettingsFrame(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Shows settings on the form.
	 */
	public void viewModelsSimilaritySettings() {
		textFieldOU.setText(
				String.format("%.2f", modelsSimilarity.getDomainCoefficients().get(BPModelsSimilarity.UNITS_COEFF)));
		textFieldSS.setText(
				String.format("%.2f", modelsSimilarity.getDomainCoefficients().get(BPModelsSimilarity.SYSTEMS_COEFF)));
		textFieldFO.setText(String.format("%.2f",
				modelsSimilarity.getDomainCoefficients().get(BPModelsSimilarity.FLOW_OBJECTS_COEFF)));
		textFieldIn.setText(
				String.format("%.2f", modelsSimilarity.getDomainCoefficients().get(BPModelsSimilarity.INPUTS_COEFF)));
		textFieldOut.setText(
				String.format("%.2f", modelsSimilarity.getDomainCoefficients().get(BPModelsSimilarity.OUTPUTS_COEFF)));
		textFieldKPIs.setText(
				String.format("%.2f", modelsSimilarity.getDomainCoefficients().get(BPModelsSimilarity.KPIS_COEFF)));

		textFieldSem.setText(String.format("%.2f",
				modelsSimilarity.getSimilarityCoefficients().get(BPModelsSimilarity.SEMANTIC_COEFF)));
		textFieldStr.setText(String.format("%.2f",
				modelsSimilarity.getSimilarityCoefficients().get(BPModelsSimilarity.STRUCTURE_COEFF)));

		spinnerLevel.setValue((int) (modelsSimilarity.getSimilarityLevel() * 100));
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RetrieveSettingsFrame(BPModelsSimilarity modelsSimilarity) {
		this.modelsSimilarity = modelsSimilarity;

		setResizable(false);
		setTitle(SETTINGS_TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 515, 406);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox comboBoxOrgUnits = new JComboBox();
		comboBoxOrgUnits.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxOrgUnits.setBounds(200, 36, 200, 20);
		contentPane.add(comboBoxOrgUnits);

		JLabel lblOrganizationalUnits = new JLabel(ORGANIZATIONAL_UNITS);
		lblOrganizationalUnits.setBounds(10, 39, 180, 14);
		contentPane.add(lblOrganizationalUnits);

		JComboBox comboBoxSuppSystems = new JComboBox();
		comboBoxSuppSystems.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxSuppSystems.setBounds(200, 67, 200, 20);
		contentPane.add(comboBoxSuppSystems);

		JLabel lblSupportingSystems = new JLabel(SUPPORTING_SYSTEMS);
		lblSupportingSystems.setBounds(10, 70, 180, 14);
		contentPane.add(lblSupportingSystems);

		JComboBox comboBoxFlowObjects = new JComboBox();
		comboBoxFlowObjects.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxFlowObjects.setBounds(200, 98, 200, 20);
		contentPane.add(comboBoxFlowObjects);

		JLabel lblFlowObjects = new JLabel(FLOW_OBJECTS);
		lblFlowObjects.setBounds(10, 101, 180, 14);
		contentPane.add(lblFlowObjects);

		JComboBox comboBoxInputs = new JComboBox();
		comboBoxInputs.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxInputs.setBounds(200, 129, 200, 20);
		contentPane.add(comboBoxInputs);

		JLabel lblInputs = new JLabel(INPUTS);
		lblInputs.setBounds(10, 132, 46, 14);
		contentPane.add(lblInputs);

		JComboBox comboBoxOutputs = new JComboBox();
		comboBoxOutputs.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxOutputs.setBounds(200, 160, 200, 20);
		contentPane.add(comboBoxOutputs);

		JLabel lblOutputs = new JLabel(OUTPUTS);
		lblOutputs.setBounds(10, 163, 46, 14);
		contentPane.add(lblOutputs);

		JComboBox comboBoxKPIs = new JComboBox();
		comboBoxKPIs.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxKPIs.setBounds(200, 191, 200, 20);
		contentPane.add(comboBoxKPIs);

		JLabel lblKpis = new JLabel(KPIS);
		lblKpis.setBounds(10, 194, 46, 14);
		contentPane.add(lblKpis);

		JLabel lblDomainCoefficients = new JLabel(DOMAIN_COEFFICIENTS);
		lblDomainCoefficients.setBounds(10, 11, 180, 14);
		contentPane.add(lblDomainCoefficients);

		JLabel lblSimilarityCoefficients = new JLabel(SIMILARITY_COEFFICIENTS);
		lblSimilarityCoefficients.setBounds(10, 225, 180, 14);
		contentPane.add(lblSimilarityCoefficients);

		JComboBox comboBoxSem = new JComboBox();
		comboBoxSem.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxSem.setBounds(200, 250, 200, 20);
		contentPane.add(comboBoxSem);

		JLabel lblSemantic = new JLabel(SEMANTIC);
		lblSemantic.setBounds(10, 253, 180, 14);
		contentPane.add(lblSemantic);

		JComboBox comboBoxStr = new JComboBox();
		comboBoxStr.setModel(new DefaultComboBoxModel(SAATY_SCALE));
		comboBoxStr.setBounds(200, 281, 200, 20);
		contentPane.add(comboBoxStr);

		JLabel lblStructure = new JLabel(STRUCTURE);
		lblStructure.setBounds(10, 284, 180, 14);
		contentPane.add(lblStructure);

		spinnerLevel = new JSpinner();
		spinnerLevel.setModel(new SpinnerNumberModel(100, 0, 100, 1));
		spinnerLevel.setBounds(200, 312, 200, 20);
		contentPane.add(spinnerLevel);

		JLabel lblLevel = new JLabel(LEVEL);
		lblLevel.setBounds(10, 315, 180, 14);
		contentPane.add(lblLevel);

		textFieldOU = new JTextField();
		textFieldOU.setEditable(false);
		textFieldOU.setBounds(410, 36, 86, 20);
		contentPane.add(textFieldOU);
		textFieldOU.setColumns(10);

		textFieldSS = new JTextField();
		textFieldSS.setEditable(false);
		textFieldSS.setBounds(410, 67, 86, 20);
		contentPane.add(textFieldSS);
		textFieldSS.setColumns(10);

		textFieldFO = new JTextField();
		textFieldFO.setEditable(false);
		textFieldFO.setBounds(410, 98, 86, 20);
		contentPane.add(textFieldFO);
		textFieldFO.setColumns(10);

		textFieldIn = new JTextField();
		textFieldIn.setEditable(false);
		textFieldIn.setBounds(410, 129, 86, 20);
		contentPane.add(textFieldIn);
		textFieldIn.setColumns(10);

		textFieldOut = new JTextField();
		textFieldOut.setEditable(false);
		textFieldOut.setBounds(410, 160, 86, 20);
		contentPane.add(textFieldOut);
		textFieldOut.setColumns(10);

		textFieldKPIs = new JTextField();
		textFieldKPIs.setEditable(false);
		textFieldKPIs.setBounds(410, 191, 86, 20);
		contentPane.add(textFieldKPIs);
		textFieldKPIs.setColumns(10);

		textFieldSem = new JTextField();
		textFieldSem.setEditable(false);
		textFieldSem.setBounds(410, 250, 86, 20);
		contentPane.add(textFieldSem);
		textFieldSem.setColumns(10);

		textFieldStr = new JTextField();
		textFieldStr.setEditable(false);
		textFieldStr.setBounds(410, 281, 86, 20);
		contentPane.add(textFieldStr);
		textFieldStr.setColumns(10);

		viewModelsSimilaritySettings();

		JButton btnNewButton = new JButton(APPLY);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				double orgUnits = Double.parseDouble(comboBoxOrgUnits.getSelectedItem().toString());
				double suppSystems = Double.parseDouble(comboBoxSuppSystems.getSelectedItem().toString());
				double flowObjects = Double.parseDouble(comboBoxFlowObjects.getSelectedItem().toString());
				double inputs = Double.parseDouble(comboBoxInputs.getSelectedItem().toString());
				double outputs = Double.parseDouble(comboBoxOutputs.getSelectedItem().toString());
				double kpis = Double.parseDouble(comboBoxKPIs.getSelectedItem().toString());

				double sem = Double.parseDouble(comboBoxSem.getSelectedItem().toString());
				double str = Double.parseDouble(comboBoxStr.getSelectedItem().toString());

				double level = Double.parseDouble(spinnerLevel.getValue().toString());

				double domainCoeffsSum = orgUnits + suppSystems + flowObjects + inputs + outputs + kpis;
				double similarityCoeffsSum = sem + str;

				modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.UNITS_COEFF, orgUnits / domainCoeffsSum);
				modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.SYSTEMS_COEFF, suppSystems / domainCoeffsSum);
				modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.FLOW_OBJECTS_COEFF,
						flowObjects / domainCoeffsSum);
				modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.INPUTS_COEFF, inputs / domainCoeffsSum);
				modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.OUTPUTS_COEFF, outputs / domainCoeffsSum);
				modelsSimilarity.setDomainCoefficient(BPModelsSimilarity.KPIS_COEFF, kpis / domainCoeffsSum);

				modelsSimilarity.setSimilarityCoefficient(BPModelsSimilarity.SEMANTIC_COEFF, sem / similarityCoeffsSum);
				modelsSimilarity.setSimilarityCoefficient(BPModelsSimilarity.STRUCTURE_COEFF,
						str / similarityCoeffsSum);

				modelsSimilarity.setSimilarityLevel(level / 100);

				viewModelsSimilaritySettings();
			}
		});
		btnNewButton.setBounds(200, 343, 130, 23);
		contentPane.add(btnNewButton);
	}
}
