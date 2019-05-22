package groovy.swing.impl;

import groovy.lang.Closure;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class DefaultAction extends AbstractAction {
   private Closure closure;

   public void actionPerformed(ActionEvent event) {
      if (this.closure == null) {
         throw new NullPointerException("No closure has been configured for this Action");
      } else {
         this.closure.call((Object)event);
      }
   }

   public Closure getClosure() {
      return this.closure;
   }

   public void setClosure(Closure closure) {
      this.closure = closure;
   }
}
