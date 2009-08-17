package working_it.jStatemachine.domain;


/**
 * This define the Interface for State(entry/exit)- or Transition-Actions
 */
public interface Action<ConcretContext extends Context> {

	/**
	 * execute the Action with current context
	 * 
	 * @param context	the current context (with current Event and other Data)
	 */
	public void execute(ConcretContext context);

	/**
	 * 
	 * @return a short-Name (Label-Description) for the action
	 */
	public String getName();

}
