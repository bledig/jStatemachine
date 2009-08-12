package working_it.jStatemachine.samples;

import working_it.jStatemachine.domain.Action;
import working_it.jStatemachine.domain.Context;
import working_it.jStatemachine.domain.Guard;
import working_it.jStatemachine.domain.State;
import working_it.jStatemachine.domain.StateGraph;
import working_it.jStatemachine.domain.Transition;
import working_it.jStatemachine.execute.Statemachine;

public class SimpleStateGraph extends StateGraph<SimpleContext> {
	
	public SimpleStateGraph() {
		super();
		
		State init = new State("init");
		State on = new State("on");
		on.addEntryAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				System.out.println("Ich bin An zum "+context.counter+". mal");
			}
		});
		on.addExitAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				System.out.println("Ich bin ausgeschalten wurden");
			}
		});
		State off = new State("off");
		State finish = new State("finish");
		
		setInitState(init);
		// Transitions
		from(init).to(off);
		
		from(on).onEvent("click").to(off);
		
		from(off).onEvent("click").to(on)
			.guard(new Guard<SimpleContext>() {
				@Override
				public boolean validate(SimpleContext context) {
					return context.counter < 3;
				}
			})
			.withAction(new Action<SimpleContext>() {
			@Override
			public void execute(SimpleContext context) {
				context.counter++;
			}
		});
		
		from(off).onEvent("click").to(finish)
			.guard(new Guard<SimpleContext>() {
				@Override
				public boolean validate(SimpleContext context) {
					return context.counter >=3;
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
