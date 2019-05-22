package org.netbeans.lib.cvsclient.event;

public class TerminationEvent extends CVSEvent {
   private boolean error;

   public TerminationEvent(Object var1, boolean var2) {
      super(var1);
      this.setError(var2);
   }

   public TerminationEvent(Object var1) {
      this(var1, false);
   }

   public boolean isError() {
      return this.error;
   }

   public void setError(boolean var1) {
      this.error = var1;
   }

   protected void fireEvent(CVSListener var1) {
      var1.commandTerminated(this);
   }
}
