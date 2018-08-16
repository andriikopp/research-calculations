package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.BPMNLandscape;

public class ExpressApplication {

    public static void main(String[] args) {
        new BPMNLandscape().getGovernanceLog().analyze();
    }
}
