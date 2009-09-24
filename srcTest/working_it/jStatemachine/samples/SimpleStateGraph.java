package working_it.jStatemachine.samples;

import static working_it.jStatemachine.samples.SimpleStates.*;

import java.io.IOException;

import working_it.jStatemachine.domain.Action;
import working_it.jStatemachine.domain.Guard;
import working_it.jStatemachine.domain.StateGraph;
import working_it.jStatemachine.processing.Statemachine;

public class SimpleStateGraph extends StateGraph<SimpleContext, SimpleStates> {
	
	// same own guard-, action- and event-classes
	
	class CounterCheckGuard implements Guard<SimpleContext> {
		public boolean validate(SimpleContext context) {
			return context.counter < 3;
		}
		public String getName() { return "counter<3"; }
	}
	
	class CounterIncrement implements Action<SimpleContext> {
		public void execute(SimpleContext context) {
			context.counter++;
		}
		public String getName() { return "++counter"; }
	}
	
	public static class CoolEvent {
		public final String coolingMedium;

		public CoolEvent(String coolingMedium) {
			super();
			this.coolingMedium = coolingMedium;
		}
	}

	
	
	public SimpleStateGraph() {
		super();
		
		state(ON).addEntryAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				System.out.println("Ich bin An zum "+context.counter+". mal");
			}
			@Override public String getName() { return "Light ON"; }
		});
		state(ON).addExitAction(new Action<SimpleContext>() {
			@Override public void execute(SimpleContext context) { 
				System.out.println("Ich bin ausgeschalten wurden");
			}
			@Override public String getName() { return "Light OFF"; }
		});
		
		setInitState(INIT);
		
		// Transitions
		from(INIT).to(OFF);
		
		from(ON).onEvent("click").to(SimpleStates.OFF);
		from(OFF).onEvent("click").toChoice(COUNTER_CHECK);
		
		fromChoice(COUNTER_CHECK).to(ON).guard(new CounterCheckGuard()).withAction(new CounterIncrement());
		fromChoice(COUNTER_CHECK).to(HOT).guardElse();
		
		from(HOT).onEvent("click").to(HOT).withAction(new CounterIncrement());
		from(HOT).onEvent(CoolEvent.class).to(OFF).withAction(new Action<SimpleContext>() {
			@Override public void execute(SimpleContext context) { context.counter = 0; }
			@Override public String getName() { return "counter=0"; }
		});
		from(HOT).to(FINISH).guard(new Guard<SimpleContext>() {
			@Override public boolean validate(SimpleContext context) { return context.counter > 4; }
			@Override public String getName() { return "counter>4"; }
		});
	}

	

	public static void main(String[] args) throws IOException {
		SimpleStateGraph graph = new SimpleStateGraph();
		
		System.out.println(graph.makeGraphiz());
		graph.makeGraphizPng("srcTest");
		
		SimpleContext context = new SimpleContext();
		Statemachine<SimpleContext, SimpleStates> sm = new Statemachine<SimpleContext, SimpleStates>(graph, context);
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
