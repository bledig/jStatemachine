/**
 * 
 */
package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * The State of a Stategraph
 */
public class State extends PseudoState {
	

	private List<Action> entryActions = new ArrayList<Action>();
	private List<Action> exitActions = new ArrayList<Action>();

	/**
	 * Constructor
	 * @param name	uniq name
	 */
	public State(Enum name) {
		super(name);
	}

	
	/**
	 * Add a Action to the entryActions-List.
	 * This Actions are called, when enter this state 
	 * 
	 * @param <ConcretContext>	the concret context class
	 * @param entryAction		the Action
	 * @return self
	 */
	public <ConcretContext extends Context> State addEntryAction(Action<ConcretContext> entryAction){
		entryActions.add(entryAction);
		return this;
	}
	

	/**
	 * Add a Action to the exitActions-List.
	 * This Actions are called, when exit this state 
	 * 
	 * @param <ConcretContext>	the concret context class
	 * @param exitAction		the Action
	 * @return self
	 */
	public <ConcretContext extends Context> State addExitAction(Action<ConcretContext> exitAction){
		exitActions.add(exitAction);
		return this;
	}

	
	public List<Action> getEntryActions() {
		return entryActions;
	}


	public List<Action> getExitActions() {
		return exitActions;
	}
	

}
