/**
 * 
 */
package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Base-Class for States or Choices
 */
public abstract class PseudoState {

	private final Enum name;
	private List<Transition> transitions = new ArrayList<Transition>();

	/**
	 * Constructor
	 * @param name uniq Name of the State
	 */
	public PseudoState(Enum name) {
		super();
		this.name = name;
	}

	public Enum getName() {
		return name;
	}

	/**
	 * Add a transition to the intern list.
	 * @param <ConcretContext> the congrete context class
	 * @param transition the transition to add
	 */
	public <ConcretContext extends Context> void addTransition(Transition<ConcretContext> transition) {
		transitions.add(transition);
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"["+name+"]";
	}

	
}
