package working_it.jStatemachine.domain;


/**
 * This define the Interface for a Guard of a Transition.
 */
public interface Guard<ConcretContext extends Context> {
	
	/**
	 * Calls the Validation with current context.
	 * If return false, the guard denied to fired the transition.
	 * 
	 * @param context	the current context (with current Event and other Data)
	 * @return true if Guard passed
	 */
	public boolean validate(ConcretContext context);

}
