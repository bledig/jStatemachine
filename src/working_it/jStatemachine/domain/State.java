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
public class State {
	
	private final String name;
	private List<Transition> transitions = new ArrayList<Transition>();
	private List<Action> entryActions = new ArrayList<Action>();
	private List<Action> exitActions = new ArrayList<Action>();

	/**
	 * @param name
	 */
	public State(String name) {
		super();
		this.name = name;
	}
	
	
	public <ConcretContext extends Context> void addTransition(Transition<ConcretContext> transition) {
		transitions.add(transition);
	}
	
	public <ConcretContext extends Context> State addEntryAction(Action<ConcretContext> entryAction){
		entryActions.add(entryAction);
		return this;
	}
	
	public <ConcretContext extends Context> State addExitAction(Action<ConcretContext> exitAction){
		exitActions.add(exitAction);
		return this;
	}

	public String getName() {
		return name;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}


	public List<Action> getEntryActions() {
		return entryActions;
	}


	public List<Action> getExitActions() {
		return exitActions;
	}
	

}
