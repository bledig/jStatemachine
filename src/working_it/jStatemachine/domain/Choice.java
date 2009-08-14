/**
 * 
 */
package working_it.jStatemachine.domain;

/**
 * Class to define Choices in Stategraphs
 */
public class Choice extends PseudoState {

	/**
	 * Constructor
	 * 
	 * @param name	uniq Name for the Choice
	 */
	public Choice(Enum name) {
		super(name);
	}

}
