package soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class MethodToContexts {
   private final Map<SootMethod, List<MethodOrMethodContext>> map = new HashMap();

   public void add(MethodOrMethodContext momc) {
      SootMethod m = momc.method();
      List<MethodOrMethodContext> l = (List)this.map.get(m);
      if (l == null) {
         this.map.put(m, l = new ArrayList());
      }

      ((List)l).add(momc);
   }

   public MethodToContexts() {
   }

   public MethodToContexts(Iterator<MethodOrMethodContext> it) {
      this.add(it);
   }

   public void add(Iterator<MethodOrMethodContext> it) {
      while(it.hasNext()) {
         MethodOrMethodContext momc = (MethodOrMethodContext)it.next();
         this.add(momc);
      }

   }

   public List<MethodOrMethodContext> get(SootMethod m) {
      List<MethodOrMethodContext> ret = (List)this.map.get(m);
      if (ret == null) {
         ret = new ArrayList();
      }

      return (List)ret;
   }
}
