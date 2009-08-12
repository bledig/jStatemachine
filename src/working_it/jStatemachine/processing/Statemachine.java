/**
 * 
 */
package working_it.jStatemachine.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;

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

	private static Log log = LogFactory.getLog(Statemachine.class);
	
	private final StateGraph<ConcretContext> stateGraph;
	private State currentState;
	private ConcretContext context;
	private int deepCounter = 0;
	private ProcessingState processingState = new ProcessingState();
	
	private static final int MAX_DEEP_COUNTER = 50; // max. Anzahl Durchlaeufe (um Endlosschleife bei fehlerhaften Grafen zuvermeiden)

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
		context.setProcessingState(processingState);
		walkGraph(null); // alle Event-losen Transitionen abarbeiten
		return currentState;
	}

	
	public void handleEvent(Object event) {
		if(currentState==null)
			throw new IllegalStateException("No current-state set!");
		
		log.info("handleEvent: state="+currentState.getName()+", event="+event);
		
		processingState.addEvent(event);
		while(processingState.hasEvent()) {
			walkGraph(processingState.nextEvent());
		}
	}

	/**
	 * @param event
	 */
	private void walkGraph(Object event) {
		if(++deepCounter > MAX_DEEP_COUNTER) {
			log.error("walkGraph: MAX_DEEP_COUNT!");
			throw new IllegalStateException("Call-Deep to high!");
		}
		
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
		if(event!=null) {
			if(isFired) {
				// so nun nochmal mit Event=null durchlaufen
				// um die Event-losen Transitionen des neuen Zustands auszulösen
				walkGraph(null);
			} else {
				log.warn("Event not handle: "+event);
			}
		}
		deepCounter = 0;
	}

}
