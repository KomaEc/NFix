package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.Unit;

public class TransitiveTargets {
   private CallGraph cg;
   private Filter filter;

   public TransitiveTargets(CallGraph cg) {
      this.cg = cg;
   }

   public TransitiveTargets(CallGraph cg, Filter filter) {
      this.cg = cg;
      this.filter = filter;
   }

   public Iterator<MethodOrMethodContext> iterator(Unit u) {
      ArrayList<MethodOrMethodContext> methods = new ArrayList();
      Iterator<Edge> it = this.cg.edgesOutOf(u);
      if (this.filter != null) {
         it = this.filter.wrap(it);
      }

      while(it.hasNext()) {
         Edge e = (Edge)it.next();
         methods.add(e.getTgt());
      }

      return this.iterator(methods.iterator());
   }

   public Iterator<MethodOrMethodContext> iterator(MethodOrMethodContext momc) {
      ArrayList<MethodOrMethodContext> methods = new ArrayList();
      Iterator<Edge> it = this.cg.edgesOutOf(momc);
      if (this.filter != null) {
         it = this.filter.wrap(it);
      }

      while(it.hasNext()) {
         Edge e = (Edge)it.next();
         methods.add(e.getTgt());
      }

      return this.iterator(methods.iterator());
   }

   public Iterator<MethodOrMethodContext> iterator(Iterator<? extends MethodOrMethodContext> methods) {
      Set<MethodOrMethodContext> s = new HashSet();
      ArrayList worklist = new ArrayList();

      while(methods.hasNext()) {
         MethodOrMethodContext method = (MethodOrMethodContext)methods.next();
         if (s.add(method)) {
            worklist.add(method);
         }
      }

      return this.iterator(s, worklist);
   }

   private Iterator<MethodOrMethodContext> iterator(Set<MethodOrMethodContext> s, ArrayList<MethodOrMethodContext> worklist) {
      for(int i = 0; i < worklist.size(); ++i) {
         MethodOrMethodContext method = (MethodOrMethodContext)worklist.get(i);
         Iterator<Edge> it = this.cg.edgesOutOf(method);
         if (this.filter != null) {
            it = this.filter.wrap(it);
         }

         while(it.hasNext()) {
            Edge e = (Edge)it.next();
            if (s.add(e.getTgt())) {
               worklist.add(e.getTgt());
            }
         }
      }

      return worklist.iterator();
   }
}
