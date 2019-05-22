package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.Unit;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public class SlowCallGraph extends CallGraph {
   private final Set<Edge> edges = new HashSet();
   private final MultiMap srcMap = new HashMultiMap();
   private final MultiMap unitMap = new HashMultiMap();
   private final MultiMap tgtMap = new HashMultiMap();
   private final ChunkedQueue stream = new ChunkedQueue();
   private final QueueReader reader;

   public SlowCallGraph() {
      this.reader = this.stream.reader();
   }

   public boolean addEdge(Edge e) {
      if (!this.edges.add(e)) {
         return false;
      } else {
         this.stream.add(e);
         this.srcMap.put(e.getSrc(), e);
         this.tgtMap.put(e.getTgt(), e);
         this.unitMap.put(e.srcUnit(), e);
         return true;
      }
   }

   public boolean removeEdge(Edge e) {
      if (!this.edges.remove(e)) {
         return false;
      } else {
         this.srcMap.remove(e.getSrc(), e);
         this.tgtMap.remove(e.getTgt(), e);
         this.unitMap.remove(e.srcUnit(), e);
         return true;
      }
   }

   public Iterator sourceMethods() {
      return (new ArrayList(this.srcMap.keySet())).iterator();
   }

   public Iterator edgesOutOf(Unit u) {
      return (new ArrayList(this.unitMap.get(u))).iterator();
   }

   public Iterator edgesOutOf(MethodOrMethodContext m) {
      return (new ArrayList(this.srcMap.get(m))).iterator();
   }

   public Iterator edgesInto(MethodOrMethodContext m) {
      return (new ArrayList(this.tgtMap.get(m))).iterator();
   }

   public QueueReader listener() {
      return this.reader.clone();
   }

   public QueueReader newListener() {
      return this.stream.reader();
   }

   public String toString() {
      QueueReader reader = this.listener();
      StringBuffer out = new StringBuffer();

      while(reader.hasNext()) {
         Edge e = (Edge)reader.next();
         out.append(e.toString() + "\n");
      }

      return out.toString();
   }

   public int size() {
      return this.edges.size();
   }
}
