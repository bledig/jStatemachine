package working_it.jStatemachine.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import working_it.jStatemachine.processing.ProcessingState;

/**
 * This is the context (enviroment) for a running statemachine.
 * The current context-instance is injected in every Guard-validation and 
 * Action-execute.
 * 
 * This Class can extend with own custom Data.
 */
public class Context {
	
	private static Log log = LogFactory.getLog(Context.class);
	private ProcessingState processingState;
	
	/**
	 * Add event to local event-queue.
	 * This event is later processed in the current statemachine
	 * @param newEvent
	 */
	public void addEvent(Object newEvent) {
		if (log.isDebugEnabled()) {
			log.debug("addEvent(" + newEvent + ")");
		}
		processingState.addEvent(newEvent);
	}

	/**
	 * Add event to the global event-queue.
	 * The even reperesents a Result and was sent to the global event handler.
	 * This event are NOT processed in the current statemachine
	 * @param resultEvent
	 */
	public void addGlobalEvent(Object resultEvent) {
		processingState.addGlobalEvent(resultEvent);
	}
	
	
	/**
	 * Returns the current event
	 * @return the event-object
	 */
	public Object currentEvent() {
		return processingState.currentEvent();
	}

	
	/**
	 * Setter for Processing-State
	 * @param processingState
	 */
	public void setProcessingState(ProcessingState processingState) {
		this.processingState = processingState;
	}

}
