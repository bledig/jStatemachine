package working_it.jStatemachine.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import working_it.jStatemachine.processing.ProcessingState;

public class Context {
	
	private static Log log = LogFactory.getLog(Context.class);
	private ProcessingState processingState;
	
	/**
	 * add event to event-queue
	 * @param newEvent
	 */
	public void addEvent(Object newEvent) {
		if (log.isDebugEnabled()) {
			log.debug("addEvent(" + newEvent + ")");
		}
		processingState.addEvent(newEvent);
	}

	/**
	 * anfuegen eines Events an die Event-List  zur Weiterverarbeitung beim Aufrufenden
	 * (also der Events, die nicht in der lokalen Statemachine verarbeitet werden sollen)
	 * @param resultEvent
	 */
	public void addGlobalEvent(Object resultEvent) {
		processingState.addGlobalEvent(resultEvent);
	}
	
	

	public Object currentEvent() {
		return processingState.currentEvent();
	}

	public void setProcessingState(ProcessingState processingState) {
		this.processingState = processingState;
	}

}
