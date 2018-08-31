package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.ARISLandscape;
import edu.kopp.phd.express.landscape.BPMNLandscape;
import edu.kopp.phd.express.landscape.DFDLandscape;

public class ExpressApplication {

    public static void main(String[] args) {
        System.out.println("ARIS");
        new ARISLandscape().getGovernanceLog().analyze();

        System.out.println("BPMN");
        new BPMNLandscape().getGovernanceLog().analyze();

        System.out.println("DFD");
        new DFDLandscape().getGovernanceLog().analyze();
    }
}
