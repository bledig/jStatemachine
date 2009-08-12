/**
 * 
 */
package working_it.jStatemachine.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bernd ledig
 *
 */
public class ProcessingState {
	
	public static final int MAX_EVENT_QUEUE_SIZE = 25;
	private static Log log = LogFactory.getLog(ProcessingState.class);

	private Queue<Object> eventQueue = new ArrayBlockingQueue<Object>(MAX_EVENT_QUEUE_SIZE);
	private final List<Object> globalEvents = new ArrayList<Object>();
	private Object currentEvent;

	/**
	 * add event to event-queue
	 * @param newEvent
	 */
	public void addEvent(Object newEvent) {
		if(newEvent==null)
			return;
		eventQueue.offer(newEvent);
	}

	/**
	 * anfuegen eines Events an die Event-List  zur Weiterverarbeitung beim Aufrufenden
	 * (also der Events, die nicht in der lokalen Statemachine verarbeitet werden sollen)
	 * @param resultEvent
	 */
	public void addGlobalEvent(Object resultEvent) {
		if(resultEvent!=null) {
			globalEvents.add(resultEvent);
			if (log.isDebugEnabled()) {
				log.debug("addGlobalEvent(" + resultEvent + ")");
			}
		}
	}
	


	/**
	 * has event-queu events?
	 * @return
	 */
	public boolean hasEvent() {
		return eventQueue.size()>0;
	}
	
	/**
	 * give next even from list (and remove from event-queue)
	 * and set this as currentEvent
	 * @return the Event
	 */
	public Object nextEvent() {
		currentEvent = eventQueue.poll();
//		if (log.isDebugEnabled()) {
//			log.debug("nextEvent return=" + currentEvent);
//		}
		return currentEvent;
	}
	
	/**
	 * give current event from list (without remove from event-queue)
	 * @return
	 */
	public Object currentEvent() {
		return currentEvent;
	}
	
	
	/**
	 * liefert Liste der Events zur Weiterverarbeitung beim Aufrufenden
	 * (also der Events, die nicht in der lokalen Statemachine verarbeitet werden sollen)
	 * 
	 * @return
	 */
	public List<Object> getGlobalEvents() {
		return this.globalEvents;
	}


	public boolean hasGlobalEvents() {
		return !globalEvents.isEmpty();
	}

	public void clearGlobalEvents() {
		globalEvents.clear();
	}

}
