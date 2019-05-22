package soot.jimple.toolkits.callgraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public class ReachableMethods {
   protected CallGraph cg;
   protected Iterator<Edge> edgeSource;
   protected final ChunkedQueue<MethodOrMethodContext> reachables;
   protected final Set<MethodOrMethodContext> set;
   protected QueueReader<MethodOrMethodContext> unprocessedMethods;
   protected final QueueReader<MethodOrMethodContext> allReachables;
   protected Filter filter;

   public ReachableMethods(CallGraph graph, Iterator<? extends MethodOrMethodContext> entryPoints) {
      this(graph, entryPoints, (Filter)null);
   }

   public ReachableMethods(CallGraph graph, Iterator<? extends MethodOrMethodContext> entryPoints, Filter filter) {
      this.reachables = new ChunkedQueue();
      this.set = new HashSet();
      this.allReachables = this.reachables.reader();
      this.filter = filter;
      this.cg = graph;
      this.addMethods(entryPoints);
      this.unprocessedMethods = this.reachables.reader();
      this.edgeSource = graph.listener();
      if (filter != null) {
         this.edgeSource = filter.wrap(this.edgeSource);
      }

   }

   public ReachableMethods(CallGraph graph, Collection<? extends MethodOrMethodContext> entryPoints) {
      this(graph, entryPoints.iterator());
   }

   protected void addMethods(Iterator<? extends MethodOrMethodContext> methods) {
      while(methods.hasNext()) {
         this.addMethod((MethodOrMethodContext)methods.next());
      }

   }

   protected void addMethod(MethodOrMethodContext m) {
      if (this.set.add(m)) {
         this.reachables.add(m);
      }

   }

   public void update() {
      while(this.edgeSource.hasNext()) {
         Edge e = (Edge)this.edgeSource.next();
         if (this.set.contains(e.getSrc())) {
            this.addMethod(e.getTgt());
         }
      }

      Iterator targets;
      for(; this.unprocessedMethods.hasNext(); this.addMethods(new Targets(targets))) {
         MethodOrMethodContext m = (MethodOrMethodContext)this.unprocessedMethods.next();
         targets = this.cg.edgesOutOf(m);
         if (this.filter != null) {
            targets = this.filter.wrap(targets);
         }
      }

   }

   public QueueReader<MethodOrMethodContext> listener() {
      return this.allReachables.clone();
   }

   public QueueReader<MethodOrMethodContext> newListener() {
      return this.reachables.reader();
   }

   public boolean contains(MethodOrMethodContext m) {
      return this.set.contains(m);
   }

   public int size() {
      return this.set.size();
   }
}
