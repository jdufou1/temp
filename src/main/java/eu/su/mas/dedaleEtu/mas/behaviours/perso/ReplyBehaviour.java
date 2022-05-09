package eu.su.mas.dedaleEtu.mas.behaviours.perso;

import eu.su.mas.dedaleEtu.mas.agents.dummies.perso.AgentProjet;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReplyBehaviour extends OneShotBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReplyBehaviour(AgentProjet a) {
		super(a);
	}

	@Override
	public void action() {
		MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msgReceived = this.myAgent.receive(msgTemplate);
		if (msgReceived!=null) {
			String content = msgReceived.getContent();
			if(content.contains(EnvoiBehaviour.START_COM)) { // Si le message recu contient le signal du début de com :
				System.out.println("salut");
			}
		}
	}

}
