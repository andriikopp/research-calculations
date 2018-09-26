package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.validation.ControlFlowValidation;
import edu.kopp.phd.express.landscape.validation.DataFlowValidation;
import edu.kopp.phd.express.landscape.validation.ARISEnvironmentValidation;
import edu.kopp.phd.express.landscape.validation.IDEF0EnvironmentValidation;

public class ValidationApp {

    public static void main(String[] args) {
        new ControlFlowValidation().getGovernanceLog().analyze();
        new DataFlowValidation().getGovernanceLog().analyze();
        new ARISEnvironmentValidation().getGovernanceLog().processEnvironment().ignoreRegulations().analyze();
        new IDEF0EnvironmentValidation().getGovernanceLog().processEnvironment().analyze();
    }
}
