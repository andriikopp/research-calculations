package edu.kopp.phd.express.landscape;

import edu.kopp.phd.express.governance.GovernanceLog;

public class Landscape {
    private GovernanceLog governanceLog;

    public Landscape(GovernanceLog governanceLog) {
        this.governanceLog = governanceLog;
    }

    public GovernanceLog getGovernanceLog() {
        return governanceLog;
    }

    public void setGovernanceLog(GovernanceLog governanceLog) {
        this.governanceLog = governanceLog;
    }
}
