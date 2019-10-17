# research-calculations
Research calculations made during PhD study.

# research-calculations-java
BPMAI (Business Process Model Analysis & Improvement) software.

To run the software you need to clone it from this repository and follow the steps below:

1. Download Intellij IDEA (if necessary) and open the clonned project 'research-calculations-java'. Deploy the MySQL database using bpmai.sql scripts. Use configuration class edu.bpmanalysis.config.Configuration to set up MySQL server credentials.

2. Run edu.bpmanalysis.web.BPMAIApplication class. It starts Spark web sever, which serves on the port 4567 (make sure it is not occupied).

3. After the software is started, the homepage will be automatically opened in your browser (set by default).

4. To use interactive dashboards, open the BPMAI_Dashboard.pbix in Power BI Desktop (it can be than published to Power BI cloud service).

At the research-calculations-java/processModelsStorage/test_models/ you can find a set of test models (IDEF0, BPMN, DFD, EPC) for BPMAI software evaluation. Set up a path in process-models-path.conf file if necessary.

# BPMAI_Dashboard.pbix
Power BI dashboards used to display results provide by the BPMAI software.

# research-calculations-web
Pure JavaScript lightweight solutions in the related field of research.

1. JS-BPMAI is a tool for BPMN models analysis.

2. JS-BIDT is a tool for BI (Business Intelligence) dashboards design and templating.

# research-calculations-* (NOT RELEVANT)
Related solutions/calculations in other languages, prototypes, algorithms test, etc.

Not worth your attention.
