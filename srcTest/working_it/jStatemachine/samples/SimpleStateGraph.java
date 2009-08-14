package working_it.jStatemachine.samples;

import working_it.jStatemachine.domain.Action;
import working_it.jStatemachine.domain.Choice;
import working_it.jStatemachine.domain.Context;
import working_it.jStatemachine.domain.Guard;
import working_it.jStatemachine.domain.State;
import working_it.jStatemachine.domain.StateGraph;
import working_it.jStatemachine.domain.Transition;
import working_it.jStatemachine.processing.Statemachine;

import static working_it.jStatemachine.samples.SimpleStates.*;
import static working_it.jStatemachine.samples.SimpleChoices.*;

public class SimpleStateGraph extends StateGraph<SimpleContext> {
	
	class CounterCheckGuard implements Guard<SimpleContext> {
		@Override
		public boolean validate(SimpleContext context) {
			return context.counter < 3;
		}
	}
	
	public SimpleStateGraph() {
		super();
		
		state(ON).addEntryAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				System.out.println("Ich bin An zum "+context.counter+". mal");
			}
		});
		state(ON).addExitAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				System.out.println("Ich bin ausgeschalten wurden");
			}
		});
		
		setInitState(INIT);
		
		// Transitions
		from(INIT).to(OFF);
		
		from(ON).onEvent("click").to(SimpleStates.OFF);
		from(OFF).onEvent("click").toChoice(COUNTER_CHECK);
		
		fromChoice(COUNTER_CHECK).to(ON)
			.guard(new CounterCheckGuard())
			.withAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				context.counter++;
			}
		});
		
		fromChoice(COUNTER_CHECK).guardElse().to(HOT);
		from(HOT).onEvent("click").to(HOT).withAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				context.counter++;
			}
		});
		from(HOT).to(FINISH).guard(new Guard<SimpleContext>() {
			@Override
			public boolean validate(SimpleContext context) {
				return context.counter > 4;
			}
		});
	}

	

	public static void main(String[] args) {
		SimpleStateGraph graph = new SimpleStateGraph();
		SimpleContext context = new SimpleContext();
		Statemachine<SimpleContext> sm = new Statemachine<SimpleContext>(graph);
		sm.start(context);
		sm.handleEvent("click");
		sm.handleEvent("click");
		sm.handleEvent("click");
		sm.handleEvent("click");
		sm.handleEvent("click");
		sm.handleEvent("click");
		sm.handleEvent("click");
		sm.handleEvent("click");
	}

}
