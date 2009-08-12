/**
 * 
 */
package working_it.jStatemachine.domain;


/**
 * @author bernd
 *
 */
public interface Guard<ConcretContext extends Context> {
	
	public boolean validate(ConcretContext context);

}
