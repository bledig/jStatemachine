/**
 * 
 */
package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;

import working_it.jStatemachine.samples.SimpleContext;


/**
 * This Class represents a State-Transition from a state to a other state
 * optionaly guarded with a Guard and Actions 
 */
public class Transition<ConcretContext extends Context> {
	
	private PseudoState fromState;
	private PseudoState toState;
	private Object event;
	private Guard<ConcretContext> guard;
	private List<Action<ConcretContext>> actionList;
	private StateGraph<ConcretContext> stategraph;
	

	/**
	 * Constructor
	 * @param stategraph	the stategraph
	 * @param from 			the from-State
	 */
	public Transition(StateGraph<ConcretContext> stategraph, PseudoState from) {
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
	public Transition<ConcretContext> to(Enum stateName) {
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
	public Transition<ConcretContext> toChoice(Enum choiceName) {
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
	public Transition<ConcretContext> onEvent(Object event) {
		if(this.event!=null)
			throw new IllegalStateException("Event already defined!");
		this.event = event;
		return this;
	}

	
	/**
	 * Define the Guard for this transition. 
	 * @param guard a Guard-Implemetion-Instance
	 * @return self
	 */
	public Transition<ConcretContext> guard(Guard<ConcretContext> guard) {
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
	public Transition<ConcretContext> guardElse() {
		if(this.guard!=null)
			throw new IllegalStateException("Guard already defined!");
		if( ! (fromState instanceof Choice) )
			throw new IllegalStateException("Else-Guard only allowed for Choices!");
		// Testen ob mindestens eine vorherige Transition mit guard existiert
		boolean hasGuardTransition = false;
		for (Transition<Context> transition : fromState.getTransitions()) {
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
	public Transition<ConcretContext> withAction(Action<ConcretContext> action) {
		if(actionList==null)
			actionList = new ArrayList<Action<ConcretContext>>();
		actionList.add(action);
		return this;
	}
	

	public PseudoState getFromState() {
		return fromState;
	}

	public PseudoState getToState() {
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
