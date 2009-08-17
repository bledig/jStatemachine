/**
 * 
 */
package working_it.jStatemachine.processing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import working_it.jStatemachine.samples.SimpleContext;
import working_it.jStatemachine.samples.SimpleStateGraph;
import working_it.jStatemachine.samples.SimpleStates;
import working_it.jStatemachine.samples.SimpleStateGraph.CoolEvent;

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
		sMachine = new Statemachine<SimpleContext>(stategraph, context);
	}


	/**
	 * Test method for {@link working_it.jStatemachine.processing.Statemachine#handleEvent(java.lang.Object)}.
	 */
	@Test
	public void testHandleEventDefaultRun() {
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.ON, sMachine.getCurrentState().getName());
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.OFF, sMachine.getCurrentState().getName());
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.ON, sMachine.getCurrentState().getName());
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.OFF, sMachine.getCurrentState().getName());
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.ON, sMachine.getCurrentState().getName());
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.OFF, sMachine.getCurrentState().getName());
		
		// the 7. click should chnage the state to hot
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.HOT, sMachine.getCurrentState().getName());
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.HOT, sMachine.getCurrentState().getName());

		// the 9. click should change the state to finish
		sMachine.handleEvent("click");
		assertEquals(SimpleStates.FINISH, sMachine.getCurrentState().getName());
	}

	/**
	 * Test method for {@link working_it.jStatemachine.processing.Statemachine#handleEvent(java.lang.Object)}.
	 */
	@Test
	public void testHandleEventRunWithCoolEvent() {
		sMachine.setCurrentState(SimpleStates.HOT);
		sMachine.handleEvent(new CoolEvent("water"));
		assertEquals(SimpleStates.OFF, sMachine.getCurrentState().getName());
	}
}
