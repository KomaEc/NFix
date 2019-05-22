package org.jboss.util.state;

public class IllegalTransitionException extends Exception {
   private static final long serialVersionUID = -3392564168782896452L;

   public IllegalTransitionException(String msg) {
      super(msg);
   }
}
