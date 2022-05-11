package agents;

import behaviours.ControlTowerBehaviour;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.wrapper.StaleProxyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import proposals.ControlTowerProposal;
import utils.Runway;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ControlTowerAgent extends Agent {

    private static int runwayLength; // In meters TODO: retirar
    private LaunchAgents launchAgents = LaunchAgents.getInstance();

    private Runway runway;

    @Override
    protected void setup() {
        Object[] args = this.getArguments();
        runwayLength = (int) args[0];

        runway = new Runway(runwayLength);

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        ArrayList<AgentController> airplaneAgents = this.launchAgents.getAirplaneAgents();

        for (AgentController agent : airplaneAgents) {
            try {
                message.addReceiver(new AID(agent.getName(), AID.ISGUID));
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate((new Date(System.currentTimeMillis() + 5000))); // 5 seconds reply limit

        ControlTowerProposal proposal = new ControlTowerProposal(runwayLength);

        try {
            message.setContentObject(proposal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addBehaviour(new ControlTowerBehaviour(this, message));
    }

    public boolean enoughSpace(int spaceRequiredToLand) {
        if (runwayLength >= spaceRequiredToLand) {
            return true;
        }
        return false;
    }

    public static void landPlane(int spaceRequired) {
        runwayLength = runwayLength - spaceRequired;
    }

    public static void parkPlane(int spaceRequired) {
        runwayLength = runwayLength + spaceRequired;
    }
}
