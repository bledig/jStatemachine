package working_it.jStatemachine.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base-Class to define a Stategraph
 */
abstract public class StateGraph<ConcretContext extends Context> {

	private Map<Enum, State> stateMap = new HashMap<Enum, State>();
	private Map<Enum, Choice> choiceMap = new HashMap<Enum, Choice>();
	
	/** Liste aller PseudoStates um Reihenfolge des Anlegens zu behalten */
	private List<PseudoState> allStates = new ArrayList<PseudoState>();
	
	private State initState;

	/**
	 * Get the state with specified name. If no state exist with this name, then
	 * a new State is created
	 * 
	 * @param name
	 * @return the State-Instance
	 */
	protected State state(Enum name) {
		State state = stateMap.get(name);
		if (state == null) {
			state = new State(name);
			stateMap.put(name, state);
			allStates.add(state);
		}
		return state;
	}

	/**
	 * Get the choice with specified name. If no choice exist with this name,
	 * then a new Choice is created
	 * 
	 * @param name
	 * @return the Choice-Instance
	 */
	protected Choice choice(Enum name) {
		Choice choice = choiceMap.get(name);
		if (choice == null) {
			choice = new Choice(name);
			choiceMap.put(name, choice);
			allStates.add(choice);
		}
		return choice;
	}

	/**
	 * Define the Init State for this graph.
	 * 
	 * @param name
	 */
	protected void setInitState(Enum name) {
		this.initState = state(name);
	}

	/**
	 * Starts (and create so) a Transition from the specified state.
	 * 
	 * @param statename
	 *            the name of the state
	 * @return the new created Transition
	 */
	protected Transition<ConcretContext> from(Enum statename) {
		State state = state(statename);
		Transition<ConcretContext> transition = new Transition<ConcretContext>(
				this, state);
		return transition;
	}

	/**
	 * Starts (and create so) a Transition from the specified choice.
	 * 
	 * @param statename
	 *            the name of the choice
	 * @return the new created Transition
	 */
	protected Transition<ConcretContext> fromChoice(Enum choiceName) {
		Choice choice = choice(choiceName);
		Transition<ConcretContext> transition = new Transition<ConcretContext>(
				this, choice);
		return transition;
	}

	
	/**
	 * Generate graphic presentation of the statechsrt as DOT-Description for Graphiv
	 * @return String with Dot-Commands
	 */
	public String makeGraphiz() {
		StringBuffer sb = new StringBuffer();
		sb.append("digraph JStateGraph {\n");
		for (PseudoState pstate : allStates) {
			if (pstate instanceof State) {
				State state = (State) pstate;
				sb.append("\t").append(state.getName());
				if (!state.getEntryActions().isEmpty()
						|| !state.getExitActions().isEmpty()) {
					sb
							.append(" [shape=box, style=rounded, label=<\n"
									+ "\t\t<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">\n"
									+ "\t\t<TR><TD>" + state.getName()
									+ "</TD></TR>\n");
					for (Action<Context> action : state.getEntryActions()) {
						sb.append("\t\t<TR><TD><FONT POINT-SIZE=\"9\">entry / "
								+ action.getName() + "</FONT></TD></TR>\n");
					}
					for (Action<Context> action : state.getExitActions()) {
						sb.append("\t\t<TR><TD><FONT POINT-SIZE=\"9\">exit / "
								+ action.getName() + "</FONT></TD></TR>\n");
					}
					sb.append("\t\t</TABLE>>]");
					
				} else if(state == initState) {
					//sb.append(" [style=bold]");
					sb.append(" [shape=circle, style=filled, fillcolor=black, height=0.2, width=0.2, label=\"\"]");
				}
				sb.append(";\n");
			} else if (pstate instanceof Choice) {
				Choice choice = (Choice) pstate;
				sb.append("\t" + choice.getName()
						+ " [shape=diamond, label=\"\"];\n");
			} else {
				throw new IllegalStateException("Unkwon Pseudostate-Instance "+pstate.getClass().getName());
			}
		}
		sb.append("\n");
		for (PseudoState state : allStates) {
			makeGraphizTransition(sb, state);
		}
		sb.append("\n}\n");
		return sb.toString();
	}

	/**
	 * @param sb
	 * @param state
	 */
	private void makeGraphizTransition(StringBuffer sb, PseudoState state) {
		for (Transition<Context> t : state.getTransitions()) {
			sb.append("\t" + t.getFromState().getName() + " -> "+ t.getToState().getName());
			if (t.getEvent() != null
					|| t.getGuard() != null
					|| t.getFromState() instanceof Choice
					|| (t.getActionList() != null && !t.getActionList().isEmpty())) {

				sb.append("[fontsize=\"9\", label=\"");
				
				// Event
				if (t.getEvent() != null) {
					if(t.hasSimpleEvent()) {
						sb.append(t.getEvent());
					} else {
						String n =  ((Class)t.getEvent()).getSimpleName();
						if(n.startsWith("Event"))
							n = n.replaceFirst("Event", "");
						sb.append(n);
					}
				}
				
				// Guard
				if (t.getGuard() != null)
					sb.append(" [" + t.getGuard().getName() + "]");
				else if (t.getFromState() instanceof Choice)
					sb.append("[else]");
				
				// Action
				if (t.getActionList() != null
						&& !t.getActionList().isEmpty()) {
					boolean first = true;
					for (Action action : t.getActionList()) {
						if (first) {
							first = false;
							sb.append(" / " + action.getName());
						} else {
							sb.append("\\n" + action.getName());
						}
					}
				}
				sb.append("\"]");
			}
			sb.append(";\n");
		}
	}

	/**
	 * make Statechart as PNG-Grafik
	 * 
	 * @param outfolder
	 * @throws IOException
	 */
	public void makeGraphizPng(String outfolder) throws IOException {
		final String graphiz_src = makeGraphiz();
		String outDirName = outfolder + "/"
				+ getClass().getPackage().getName().replace('.', '/');
		final String pngFileName = outDirName + "/"
				+ getClass().getSimpleName() + ".png";
		File outdir = new File(outDirName);
		if (!outdir.exists()) {
			System.out.println("create Dir: " + outdir.getAbsolutePath());
			outdir.mkdirs();
		} else if (!outdir.isDirectory()) {
			throw new RuntimeException("Outdir " + outDirName
					+ " exist but is not a directory!");
		}
		System.out.println(outdir.getAbsolutePath());
		System.out.println(pngFileName);
		ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng");
		Process process = pb.start();
		final OutputStream os = process.getOutputStream();
		final InputStream is = process.getInputStream();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FileOutputStream fout = new FileOutputStream(pngFileName);
					int b;
					while( (b=is.read()) >=0 ) {
						fout.write(b);
					}
					fout.close();
				} catch (Exception e) {
					throw new IllegalArgumentException("Exception: "+ e.getMessage(), e);
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					os.write(graphiz_src.getBytes());
					os.close();
				} catch (Exception e) {
					throw new IllegalArgumentException("Exception: "+ e.getMessage(), e);
				}
			}
		}).start();

		try{
			process.waitFor();
        } catch ( InterruptedException e ) {
        	Thread.currentThread().interrupt();
        }
        process.getErrorStream().close();

	}

	/**
	 * Get the State-Instance for the state specified with his name
	 * 
	 * @param name
	 * @return the State-Instance or NULL if not exist
	 */
	public State getState(Enum name) {
		return stateMap.get(name);
	}

	/**
	 * Get the Init-State-Instance
	 * 
	 * @return the State-Instance or NULL if not set
	 */
	public State getInitState() {
		return initState;
	}

}
