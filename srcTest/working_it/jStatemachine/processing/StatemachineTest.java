/**
 * 
 */
package working_it.jStatemachine.processing;

import static org.junit.Assert.*;

import javax.xml.ws.ServiceMode;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import working_it.jStatemachine.samples.SimpleContext;
import working_it.jStatemachine.samples.SimpleStateGraph;
import working_it.jStatemachine.samples.SimpleStates;

/**
 * @author bernd
 *
 */
public class StatemachineTest {

	private static SimpleStateGraph stategraph;
	private static SimpleContext context;
	private static Statemachine<SimpleContext> sMachine;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		stategraph = new SimpleStateGraph();
	}
	
	
	@Before
	public void setup(){
		context = new SimpleContext();
		sMachine = new Statemachine<SimpleContext>(stategraph);
	}

	/**
	 * Test method for {@link working_it.jStatemachine.processing.Statemachine#start(working_it.jStatemachine.domain.Context)}.
	 */
	@Test
	public void testStart() {
		sMachine.start(context);
		assertEquals(SimpleStates.OFF, sMachine.getCurrentState().getName());
	}

	/**
	 * Test method for {@link working_it.jStatemachine.processing.Statemachine#handleEvent(java.lang.Object)}.
	 */
	@Test
	public void testHandleEvent() {
		sMachine.start(context);
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.ON, sMachine.getCurrentState().getName());
	}

}
