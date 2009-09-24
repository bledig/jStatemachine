/**
 * 
 */
package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * The State of a Stategraph
 */
public class State<CONTEXT extends Context, STATENAME extends Enum<?>> extends PseudoState<STATENAME> {
	

	private List<Action<CONTEXT>> entryActions = new ArrayList<Action<CONTEXT>>();
	private List<Action<CONTEXT>> exitActions = new ArrayList<Action<CONTEXT>>();

	/**
	 * Constructor
	 * @param name	uniq name
	 */
	public State(STATENAME name) {
		super(name);
	}

	
	/**
	 * Add a Action to the entryActions-List.
	 * This Actions are called, when enter this state 
	 * 
	 * @param entryAction		the Action
	 * @return self
	 */
	public State<CONTEXT, STATENAME> addEntryAction(Action<CONTEXT> entryAction){
		entryActions.add(entryAction);
		return this;
	}
	

	/**
	 * Add a Action to the exitActions-List.
	 * This Actions are called, when exit this state 
	 * 
	 * @param exitAction		the Action
	 * @return self
	 */
	public State<CONTEXT, STATENAME> addExitAction(Action<CONTEXT> exitAction){
		exitActions.add(exitAction);
		return this;
	}

	
	public List<Action<CONTEXT>> getEntryActions() {
		return entryActions;
	}


	public List<Action<CONTEXT>> getExitActions() {
		return exitActions;
	}
	

}
