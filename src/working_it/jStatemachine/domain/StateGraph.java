package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  Base-Class to define a Stategraph
 */
abstract public class StateGraph<ConcretContext extends Context> {
	
	private Map<Enum, State> stateList = new HashMap<Enum, State>();
	private Map<Enum, Choice> choiceList = new HashMap<Enum, Choice>();
	private State initState;

	
	/**
	 * Get the state with specified name.
	 * If no state exist with this name, then a new State is created
	 *  
	 * @param name
	 * @return the State-Instance
	 */
	protected State state(Enum name) {
		State state = stateList.get(name);
		if(state==null) {
			state = new State(name);
			stateList.put(name, state);
		}
		return state;
	}

	/**
	 * Get the choice with specified name.
	 * If no choice exist with this name, then a new Choice is created
	 *  
	 * @param name
	 * @return the Choice-Instance
	 */
	protected Choice choice(Enum name) {
		Choice choice = choiceList.get(name);
		if(choice==null) {
			choice = new Choice(name);
			choiceList.put(name, choice);
		}
		return choice;
	}

	
	/**
	 * Define the Init State for this graph.
	 * @param name
	 */
	protected void setInitState(Enum name) {
		this.initState = state(name);
	}
	
	
	/**
	 * Starts (and create so) a Transition from the specified state.
	 *   
	 * @param statename the name of the state
	 * @return the new created Transition
	 */
	protected Transition<ConcretContext> from(Enum statename) {
		State state = state(statename);
		Transition<ConcretContext> transition = new Transition<ConcretContext>(this, state);
		return transition;
	}
	
	/**
	 * Starts (and create so) a Transition from the specified choice.
	 *   
	 * @param statename the name of the choice
	 * @return the new created Transition
	 */
	protected Transition<ConcretContext> fromChoice(Enum choiceName) {
		Choice choice = choice(choiceName);
		Transition<ConcretContext> transition = new Transition<ConcretContext>(this, choice);
		return transition;
	}
	
	
	/**
	 * Get the State-Instance for the state specified with his name
	 * @param name
	 * @return the State-Instance or NULL if not exist
	 */
	public State getState(Enum name) {
		return stateList.get(name);
	}
	

	/**
	 * Get the Init-State-Instance
	 * @return the State-Instance or NULL if not set
	 */
	public State getInitState() {
		return initState;
	}

}
