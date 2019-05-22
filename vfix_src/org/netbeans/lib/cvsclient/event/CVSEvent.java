package org.netbeans.lib.cvsclient.event;

import java.util.EventObject;

public abstract class CVSEvent extends EventObject {
   public CVSEvent(Object var1) {
      super(var1);
   }

   protected abstract void fireEvent(CVSListener var1);
}
