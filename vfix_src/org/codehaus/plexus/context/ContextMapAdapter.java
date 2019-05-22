package org.codehaus.plexus.context;

import java.util.HashMap;

public class ContextMapAdapter extends HashMap {
   private Context context;

   public ContextMapAdapter(Context context) {
      this.context = context;
   }

   public Object get(Object key) {
      try {
         Object value = this.context.get(key);
         return value instanceof String ? value : null;
      } catch (ContextException var3) {
         return null;
      }
   }
}
