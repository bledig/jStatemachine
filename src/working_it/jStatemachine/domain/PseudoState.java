package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Base-Class for States or Choices
 */
public abstract class PseudoState<STATENAME extends Enum<?>> {

	private final STATENAME name;
	
	@SuppressWarnings("unchecked")
	private List<Transition> transitions = new ArrayList<Transition>();

	/**
	 * Constructor
	 * @param name uniq Name of the State
	 */
	public PseudoState(STATENAME name) {
		super();
		this.name = name;
	}

	public STATENAME getName() {
		return name;
	}

	
	/**
	 * Add a transition to the intern list.
	 * 
	 * @param transition the transition to add
	 */
	@SuppressWarnings("unchecked")
	public void addTransition(Transition transition) {
		transitions.add(transition);
	}
	
	@SuppressWarnings("unchecked")
	public List<Transition> getTransitions() {
		return transitions;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"["+name+"]";
	}

	
}
