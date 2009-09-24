package working_it.jStatemachine.domain;

/**
 * Class to define Choices in Stategraphs
 */
public class Choice<STATENAME extends Enum<?>> extends PseudoState<STATENAME> {

	/**
	 * Constructor
	 * 
	 * @param name	uniq Name for the Choice
	 */
	public Choice(STATENAME name) {
		super(name);
	}

}
