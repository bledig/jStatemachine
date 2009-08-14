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
import working_it.jStatemachine.domain.PseudoState;
import working_it.jStatemachine.domain.State;
import working_it.jStatemachine.domain.StateGraph;
import working_it.jStatemachine.domain.Transition;

/**
 * This Statemachine runs on concret Stategraph
 */
public class Statemachine<ConcretContext extends Context> {

	private static Log log = LogFactory.getLog(Statemachine.class);
	
	private final StateGraph<ConcretContext> stateGraph;
	private PseudoState currentState;
	private ConcretContext context;
	private int deepCounter = 0;
	private ProcessingState processingState = new ProcessingState();
	
	private static final int MAX_DEEP_COUNTER = 50; // max. Anzahl Durchlaeufe (um Endlosschleife bei fehlerhaften Grafen zuvermeiden)

	/**
	 * Constructor
	 * @param stateGraph the concrete StateGraph-Instance on this machine runs
	 */
	public Statemachine(StateGraph<ConcretContext> stateGraph) {
		super();
		this.stateGraph = stateGraph;
	}
	
	
	/**
	 * Starts the machine doing:
	 *   - set the currentState to the InitState og the Stategraph if not set
	 *   - walk during graph for all event-less transitions
	 *   - inject ProcessingState(with event-queue) in the context
	 *   
	 * @param context	the currentContext for the guards and actions
	 * @return
	 */
	public State start(ConcretContext context){
		if(currentState==null) {
			currentState = stateGraph.getInitState();
			if(currentState==null)
				throw new IllegalStateException("No initial-state defined in Stategraph!");
		}
		this.context = context;
		context.setProcessingState(processingState);
		walkGraph(null); // alle Event-losen Transitionen abarbeiten
		return (State) currentState;
	}

	
	/**
	 * handle (process) the specified event 
	 * 
	 * @param event
	 */
	public void handleEvent(Object event) {
		if(currentState==null)
			throw new IllegalStateException("No current-state set!");
		
		log.info("handleEvent: "+currentState+" event="+event);
		
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
			PseudoState newState = transition.getToState();
			
			log.info("transition '"+currentState+"' => '"+newState+"' fired");

			if(newState!=currentState && currentState instanceof State){
				// dann exit-Actions auf current-state ausführen
				State state = (State) currentState;
				for (Action<ConcretContext> action : state.getExitActions()) {
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
			if(newState!=currentState && newState instanceof State){
				// dann entry-Actions auf new-state ausführen
				State state = (State) newState;
				for (Action<ConcretContext> action : state.getEntryActions()) {
					log.debug("execute entry action: "+action);
					action.execute(context);
				}
			}
			currentState = newState;
			isFired = true;
			break;
		}
		if(isFired) {
			// so nun nochmal mit Event=null durchlaufen
			// um die Event-losen Transitionen des neuen Zustands auszulösen
			walkGraph(null);
		}
		if(event!=null && !isFired) {
			log.warn("Event not handle: "+event);
		}
		deepCounter = 0;
	}


	public PseudoState getCurrentState() {
		return currentState;
	}


	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public void setCurrentState(Enum currentStateName) {
		this.currentState = stateGraph.getState(currentStateName);
	}

}
