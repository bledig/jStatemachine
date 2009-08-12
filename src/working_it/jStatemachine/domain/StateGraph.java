package working_it.jStatemachine.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 
 * @author bernd ledig
 */
public class StateGraph<ConcretContext extends Context> {
	
	private State initState;

	protected void setInitState(State init) {
		this.initState = init;
	}
	
	protected Transition<ConcretContext> from(State state) {
		Transition<ConcretContext> transition = new Transition<ConcretContext>(state);
		return transition;
	}

	public State getInitState() {
		return initState;
	}


}
