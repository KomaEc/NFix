package soot;

import java.util.Iterator;

public class ScenePack extends Pack {
   public ScenePack(String name) {
      super(name);
   }

   protected void internalApply() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         Transform t = (Transform)var1.next();
         t.apply();
      }

   }
}
