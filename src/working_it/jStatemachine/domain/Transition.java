package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * This Class represents a State-Transition from a state to a other state
 * optionaly guarded with a Guard and Actions 
 */
public class Transition<CONTEXT extends Context, STATENAME extends Enum<?>> {
	
	private PseudoState<STATENAME> fromState;
	private PseudoState<STATENAME> toState;
	private Object event;
	private Guard<CONTEXT> guard;
	private List<Action<CONTEXT>> actionList;
	private StateGraph<CONTEXT, STATENAME> stategraph;
	

	/**
	 * Constructor
	 * @param stategraph	the stategraph
	 * @param from 			the from-State
	 */
	public Transition(StateGraph<CONTEXT, STATENAME> stategraph, PseudoState<STATENAME> from) {
		super();
		this.stategraph = stategraph;
		this.fromState = from;
		from.addTransition(this);
	}

	
	/**
	 * Define the Destination (to) to the specified state
	 * 
	 * @param stateName	the name of the to-State
	 * @return			self
	 */
	public Transition<CONTEXT, STATENAME> to(STATENAME stateName) {
		if(toState!=null)
			throw new IllegalStateException("toState already defined!");
		toState = stategraph.state(stateName);
		return this;
	}
	
	/**
	 * Define the Destination (to) to the specified choice
	 * 
	 * @param choiceName	the name of the to-Choice
	 * @return			self
	 */
	public Transition<CONTEXT, STATENAME> toChoice(STATENAME choiceName) {
		if(toState!=null)
			throw new IllegalStateException("toState already defined!");
		toState = stategraph.choice(choiceName);
		return this;
	}
	
	
	/**
	 * Define the event, on which this transition fired.
	 * 
	 * @param event	the event-Object
	 * @return self
	 */
	public Transition<CONTEXT, STATENAME> onEvent(Object event) {
		if(this.event!=null)
			throw new IllegalStateException("Event already defined!");
		this.event = event;
		if(!hasSimpleEvent() && !(event instanceof Class<?>))
			throw new IllegalStateException("Event is not a String, Enum or Class!");
		return this;
	}

	
	/**
	 * Define the Guard for this transition. 
	 * @param guard a Guard-Implemetion-Instance
	 * @return self
	 */
	public Transition<CONTEXT, STATENAME> guard(Guard<CONTEXT> guard) {
		if(this.guard!=null)
			throw new IllegalStateException("Guard already defined!");
		this.guard = guard;
		return this;
	}

	
	/**
	 * Define a else-Guard.
	 * This is a alway-true-Guard.
	 * It is only allowed to set this, if fromState is a Choice 
	 * and this Choice has yet a other Transition with a Guard
	 *  
	 * @return self
	 */
	public Transition<CONTEXT, STATENAME> guardElse() {
		if(this.guard!=null)
			throw new IllegalStateException("Guard already defined!");
		if( ! (fromState instanceof Choice<?>) )
			throw new IllegalStateException("Else-Guard only allowed for Choices!");
		// Testen ob mindestens eine vorherige Transition mit guard existiert
		boolean hasGuardTransition = false;
		for (Transition<Context, STATENAME> transition : fromState.getTransitions()) {
			if(transition.guard!=null) {
				hasGuardTransition = true;
				break;
			}
		}
		if( !hasGuardTransition )
			throw new IllegalStateException("No prev. Guard defined!");
		this.guard = null;
		return this;
	}
	
	
	/**
	 * Add the specifed Action to the intern ActionList
	 * @param action a Action-Implementation-Instance
	 * @return self
	 */
	public Transition<CONTEXT, STATENAME> withAction(Action<CONTEXT> action) {
		if(actionList==null)
			actionList = new ArrayList<Action<CONTEXT>>();
		actionList.add(action);
		return this;
	}
	

	/**
	 * SimpleEvent is true if the event a String or enum. 
	 * @return 
	 */
	public boolean hasSimpleEvent() {
		return (event instanceof Enum<?>) || (event instanceof String);
	}


	public PseudoState<STATENAME> getFromState() {
		return fromState;
	}

	public PseudoState<STATENAME> getToState() {
		return toState;
	}

	public Object getEvent() {
		return event;
	}

	public Guard<CONTEXT> getGuard() {
		return guard;
	}

	public List<Action<CONTEXT>> getActionList() {
		return actionList;
	}


}
