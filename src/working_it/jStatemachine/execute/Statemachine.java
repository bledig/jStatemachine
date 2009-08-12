/**
 * 
 */
package working_it.jStatemachine.execute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import working_it.jStatemachine.domain.Action;
import working_it.jStatemachine.domain.Context;
import working_it.jStatemachine.domain.State;
import working_it.jStatemachine.domain.StateGraph;
import working_it.jStatemachine.domain.Transition;

/**
 * @author bernd ledig
 */
public class Statemachine<ConcretContext extends Context> {

	private Log log = LogFactory.getLog(Statemachine.class);
	
	private final StateGraph<ConcretContext> stateGraph;
	private State currentState;
	private ConcretContext context;

	/**
	 * @param stateGraph
	 */
	public Statemachine(StateGraph<ConcretContext> stateGraph) {
		super();
		this.stateGraph = stateGraph;
	}
	
	public State start(ConcretContext context){
		if(currentState==null) {
			currentState = stateGraph.getInitState();
			if(currentState==null)
				throw new IllegalStateException("No initial-state defined in Stategraph!");
		}
		this.context = context;
		handleEvent(null);
		return currentState;
	}

	public void handleEvent(Object event) {
		if(currentState==null)
			throw new IllegalStateException("No current-state set!");
		
		log.info("handleEvent: state="+currentState.getName()+", event="+event);
		boolean isFired = false;
		for (Transition<ConcretContext> transition : currentState.getTransitions()) {
			if(event==null && transition.getEvent()!=null)
				continue;
			if(transition.getEvent()!=null && !transition.getEvent().equals(event))
				continue;
			if(transition.getGuard()!=null && !transition.getGuard().validate(context))
				continue;
			State newState = transition.getToState();
			
			log.info("transition '"+currentState.getName()+"' => '"+newState.getName()+"' fired");

			if(newState!=currentState){
				// dann exit-Actions auf current-state ausführen
				for (Action<ConcretContext> action : currentState.getExitActions()) {
					log.debug("execute exit action: "+action);
					action.execute(context);
				}
			}
			if(transition.getActionList()!=null) {
				for (Action<ConcretContext> action : transition.getActionList()) {
					log.debug("execute transition action: "+action);
					action.execute(context);
				}
			}
			if(newState!=currentState){
				// dann entry-Actions auf new-state ausführen
				for (Action<ConcretContext> action : newState.getEntryActions()) {
					log.debug("execute entry action: "+action);
					action.execute(context);
				}
			}
			currentState = newState;
			isFired = true;
			break;
		}
		if(!isFired && event!=null)
			log.warn("Event not handle: "+event);
	}

	
}
