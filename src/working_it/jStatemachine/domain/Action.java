/**
 * 
 */
package working_it.jStatemachine.domain;


/**
 * @author bernd
 *
 */
public interface Action<ConcretContext extends Context> {
	
	public void execute(ConcretContext context);

}
