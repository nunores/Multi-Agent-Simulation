package proposals;

import java.io.Serializable;

public class ControlTowerProposal implements Serializable {

    private int runwayLength;

    public ControlTowerProposal(int runwayLength) {
        this.runwayLength = runwayLength;
    }

    public int getRunwayLength() {
        return runwayLength;
    }
}
