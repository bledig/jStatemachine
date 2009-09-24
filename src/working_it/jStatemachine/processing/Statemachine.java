package working_it.jStatemachine.processing;

import java.util.List;

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
public class Statemachine <CONTEXT extends Context, STATENAME extends Enum<?>>{

	private static Log log = LogFactory.getLog(Statemachine.class);
	
	private final StateGraph<CONTEXT, STATENAME> stateGraph;
	private PseudoState<STATENAME> currentState;
	private CONTEXT context;
	private int deepCounter = 0;
	private ProcessingState processingState = new ProcessingState();
	
	private static final int MAX_DEEP_COUNTER = 50; // max. Anzahl Durchlaeufe (um Endlosschleife bei fehlerhaften Grafen zuvermeiden)

	/**
	 * Constructor
	 * @param stateGraph the concrete StateGraph-Instance on this machine runs
	 * @param context thr concrete context class for this machine
	 * @param currentState the currentState, if this null, thenn initState of the graph is used.
	 */
	public Statemachine(StateGraph<CONTEXT, STATENAME> stateGraph, CONTEXT context, State<CONTEXT, STATENAME> currentState ) {
		super();
		this.stateGraph = stateGraph;
		this.context = context;
		if(currentState==null) {
			currentState = stateGraph.getInitState();
			if(currentState==null)
				throw new IllegalStateException("No initial-state defined in Stategraph!");
		}
		this.currentState = currentState;
	}
	
	/**
	 * Constructor
	 * @param stateGraph the concrete StateGraph-Instance on this machine runs
	 * @param context thr concrete context class for this machine
	 */
	public Statemachine(StateGraph<CONTEXT, STATENAME> stateGraph, CONTEXT context ) {
		this(stateGraph, context, null);
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
		
		processingState.clearGlobalEvents(); // evtl. alte globale Events löschen
		
		context.setProcessingState(processingState);
		
		// im Vorfeld alle Event-losen Transitionen abarbeiten
		walkGraph(null); 
		
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
		for (Transition<CONTEXT, STATENAME> transition : currentState.getTransitions()) {
			
			// Event checken
			if(event==null && transition.getEvent()!=null) {
				continue;
			} else if(transition.getEvent()!=null) {
				// if event a String or enum, then event-Instance must be equals with given event,
				// otherwise the event-Class must be equals
				if(transition.hasSimpleEvent()) {
					 if(!transition.getEvent().equals(event)) {
						 continue;
					 }
				} else if(!transition.getEvent().equals(event.getClass())) {
					continue;
				}
			}
			
			// Guard checken
			if(transition.getGuard()!=null && !transition.getGuard().validate(context))
				continue;
			
			
			PseudoState<STATENAME> newState = transition.getToState();
			
			log.info("transition '"+currentState+"' => '"+newState+"' fired");

			if(newState!=currentState && currentState instanceof State<?,?>){
				// dann exit-Actions auf current-state ausführen
				State<CONTEXT, STATENAME> state = (State<CONTEXT, STATENAME>) currentState;
				for (Action<CONTEXT> action : state.getExitActions()) {
					log.debug("execute exit action: "+action);
					action.execute(context);
				}
			}
			if(transition.getActionList()!=null) {
				for (Action<CONTEXT> action : transition.getActionList()) {
					log.debug("execute transition action: "+action);
					action.execute(context);
				}
			}
			if(newState!=currentState && newState instanceof State<?,?>){
				// dann entry-Actions auf new-state ausführen
				State<CONTEXT, STATENAME> state = (State<CONTEXT, STATENAME>) newState;
				for (Action<CONTEXT> action : state.getEntryActions()) {
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

	
	/**
	 * liefert Liste der Events zur Weiterverarbeitung beim Aufrufenden
	 * (also der Events, die nicht in der lokalen Statemachine verarbeitet werden sollen)
	 * 
	 * @return Liste
	 */
	public List<Object> getGlobalEvents() {
		return processingState.getGlobalEvents();
	}

	

	public PseudoState<STATENAME> getCurrentState() {
		return currentState;
	}


	public void setCurrentState(State<CONTEXT, STATENAME> currentState) {
		this.currentState = currentState;
	}

	public void setCurrentState(STATENAME currentStateName) {
		this.currentState = stateGraph.getState(currentStateName);
	}

	public void setContext(CONTEXT context) {
		this.context = context;
	}

}
