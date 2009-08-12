/**
 * 
 */
package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * @author bernd
 *
 */
public class Transition<ConcretContext extends Context> {
	
	private State fromState;
	private State toState;
	private Object event;
	private Guard<ConcretContext> guard;
	private List<Action<ConcretContext>> actionList;

	/**
	 * @param from
	 */
	public Transition(State from) {
		super();
		this.fromState = from;
		from.addTransition(this);
	}

	public Transition<ConcretContext> to(State toState) {
		this.toState = toState;
		return this;
	}
	
	
	public Transition<ConcretContext> onEvent(Object event) {
		this.event = event;
		return this;
	}

	
	public Transition<ConcretContext> guard(Guard<ConcretContext> guard) {
		this.guard = guard;
		return this;
	}

	
	public Transition<ConcretContext> withAction(Action<ConcretContext> action) {
		if(actionList==null)
			actionList = new ArrayList<Action<ConcretContext>>();
		actionList.add(action);
		return this;
	}

	

	public State getFromState() {
		return fromState;
	}

	public State getToState() {
		return toState;
	}

	public Object getEvent() {
		return event;
	}

	public Guard<ConcretContext> getGuard() {
		return guard;
	}

	public List<Action<ConcretContext>> getActionList() {
		return actionList;
	}

}
