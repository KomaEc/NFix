package org.jboss.util.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jboss.logging.Logger;

public class StateMachine implements Cloneable {
   private static Logger log = Logger.getLogger(StateMachine.class);
   private String description;
   private HashSet states;
   private State startState;
   private State currentState;

   public StateMachine(Set states, State startState) {
      this(states, startState, (String)null);
   }

   public StateMachine(Set states, State startState, String description) {
      this.states = new HashSet(states);
      this.startState = startState;
      this.currentState = startState;
      this.description = description;
   }

   public Object clone() {
      StateMachine clone = new StateMachine(this.states, this.startState, this.description);
      clone.currentState = this.currentState;
      return clone;
   }

   public String getDescription() {
      return this.description;
   }

   public State getCurrentState() {
      return this.currentState;
   }

   public State getStartState() {
      return this.startState;
   }

   public Set getStates() {
      return this.states;
   }

   public State nextState(String actionName) throws IllegalTransitionException {
      Transition t = this.currentState.getTransition(actionName);
      if (t == null) {
         String msg = "No transition for action: '" + actionName + "' from state: '" + this.currentState.getName() + "'";
         throw new IllegalTransitionException(msg);
      } else {
         State nextState = t.getTarget();
         log.trace("nextState(" + actionName + ") = " + nextState);
         this.currentState = nextState;
         return this.currentState;
      }
   }

   public State reset() {
      this.currentState = this.startState;
      return this.currentState;
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("StateMachine[:\n");
      tmp.append("\tCurrentState: " + this.currentState.getName());
      Iterator i = this.states.iterator();

      while(i.hasNext()) {
         tmp.append('\n').append(i.next());
      }

      tmp.append(']');
      return tmp.toString();
   }
}
