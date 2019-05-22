package soot.jimple.toolkits.callgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public class CallGraph implements Iterable<Edge> {
   protected Set<Edge> edges = new HashSet();
   protected ChunkedQueue<Edge> stream = new ChunkedQueue();
   protected QueueReader<Edge> reader;
   protected Map<MethodOrMethodContext, Edge> srcMethodToEdge;
   protected Map<Unit, Edge> srcUnitToEdge;
   protected Map<MethodOrMethodContext, Edge> tgtToEdge;
   protected Edge dummy;

   public CallGraph() {
      this.reader = this.stream.reader();
      this.srcMethodToEdge = new HashMap();
      this.srcUnitToEdge = new HashMap();
      this.tgtToEdge = new HashMap();
      this.dummy = new Edge((MethodOrMethodContext)null, (Unit)null, (MethodOrMethodContext)null, Kind.INVALID);
   }

   public boolean addEdge(Edge e) {
      if (!this.edges.add(e)) {
         return false;
      } else {
         this.stream.add(e);
         Edge position = null;
         position = (Edge)this.srcUnitToEdge.get(e.srcUnit());
         if (position == null) {
            this.srcUnitToEdge.put(e.srcUnit(), e);
            position = this.dummy;
         }

         e.insertAfterByUnit(position);
         position = (Edge)this.srcMethodToEdge.get(e.getSrc());
         if (position == null) {
            this.srcMethodToEdge.put(e.getSrc(), e);
            position = this.dummy;
         }

         e.insertAfterBySrc(position);
         position = (Edge)this.tgtToEdge.get(e.getTgt());
         if (position == null) {
            this.tgtToEdge.put(e.getTgt(), e);
            position = this.dummy;
         }

         e.insertAfterByTgt(position);
         return true;
      }
   }

   public boolean removeAllEdgesOutOf(Unit u) {
      boolean hasRemoved = false;
      QueueReader edgeRdr = this.listener();

      while(edgeRdr.hasNext()) {
         Edge e = (Edge)edgeRdr.next();
         if (e.srcUnit() == u) {
            this.removeEdge(e);
            hasRemoved = true;
         }
      }

      return hasRemoved;
   }

   public boolean swapEdgesOutOf(Stmt out, Stmt in) {
      boolean hasSwapped = false;
      QueueReader edgeRdr = this.listener();

      while(edgeRdr.hasNext()) {
         Edge e = (Edge)edgeRdr.next();
         if (e.srcUnit() == out) {
            this.removeEdge(e);
            this.addEdge(new Edge(e.getSrc(), in, e.getTgt()));
            hasSwapped = true;
         }
      }

      return hasSwapped;
   }

   public boolean removeEdge(Edge e) {
      if (!this.edges.remove(e)) {
         return false;
      } else {
         e.remove();
         if (this.srcUnitToEdge.get(e.srcUnit()) == e) {
            if (e.nextByUnit().srcUnit() == e.srcUnit()) {
               this.srcUnitToEdge.put(e.srcUnit(), e.nextByUnit());
            } else {
               this.srcUnitToEdge.put(e.srcUnit(), (Object)null);
            }
         }

         if (this.srcMethodToEdge.get(e.getSrc()) == e) {
            if (e.nextBySrc().getSrc() == e.getSrc()) {
               this.srcMethodToEdge.put(e.getSrc(), e.nextBySrc());
            } else {
               this.srcMethodToEdge.put(e.getSrc(), (Object)null);
            }
         }

         if (this.tgtToEdge.get(e.getTgt()) == e) {
            if (e.nextByTgt().getTgt() == e.getTgt()) {
               this.tgtToEdge.put(e.getTgt(), e.nextByTgt());
            } else {
               this.tgtToEdge.put(e.getTgt(), (Object)null);
            }
         }

         return true;
      }
   }

   public boolean isEntryMethod(SootMethod method) {
      return !this.tgtToEdge.containsKey(method);
   }

   public Edge findEdge(Unit u, SootMethod callee) {
      for(Edge e = (Edge)this.srcUnitToEdge.get(u); e.srcUnit() == u && e.kind() != Kind.INVALID; e = e.nextByUnit()) {
         if (e.tgt() == callee) {
            return e;
         }
      }

      return null;
   }

   public Iterator<MethodOrMethodContext> sourceMethods() {
      return this.srcMethodToEdge.keySet().iterator();
   }

   public Iterator<Edge> edgesOutOf(Unit u) {
      return new CallGraph.TargetsOfUnitIterator(u);
   }

   public Iterator<Edge> edgesOutOf(MethodOrMethodContext m) {
      return new CallGraph.TargetsOfMethodIterator(m);
   }

   public Iterator<Edge> edgesInto(MethodOrMethodContext m) {
      return new CallGraph.CallersOfMethodIterator(m);
   }

   public QueueReader<Edge> listener() {
      return this.reader.clone();
   }

   public QueueReader<Edge> newListener() {
      return this.stream.reader();
   }

   public String toString() {
      QueueReader<Edge> reader = this.listener();
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

   public Iterator<Edge> iterator() {
      return this.edges.iterator();
   }

   class CallersOfMethodIterator implements Iterator<Edge> {
      private Edge position = null;
      private MethodOrMethodContext m;

      CallersOfMethodIterator(MethodOrMethodContext m) {
         this.m = m;
         if (m == null) {
            throw new RuntimeException();
         } else {
            this.position = (Edge)CallGraph.this.tgtToEdge.get(m);
            if (this.position == null) {
               this.position = CallGraph.this.dummy;
            }

         }
      }

      public boolean hasNext() {
         if (this.position.getTgt() != this.m) {
            return false;
         } else {
            return this.position.kind() != Kind.INVALID;
         }
      }

      public Edge next() {
         Edge ret = this.position;
         this.position = this.position.nextByTgt();
         return ret;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   class TargetsOfMethodIterator implements Iterator<Edge> {
      private Edge position = null;
      private MethodOrMethodContext m;

      TargetsOfMethodIterator(MethodOrMethodContext m) {
         this.m = m;
         if (m == null) {
            throw new RuntimeException();
         } else {
            this.position = (Edge)CallGraph.this.srcMethodToEdge.get(m);
            if (this.position == null) {
               this.position = CallGraph.this.dummy;
            }

         }
      }

      public boolean hasNext() {
         if (this.position.getSrc() != this.m) {
            return false;
         } else {
            return this.position.kind() != Kind.INVALID;
         }
      }

      public Edge next() {
         Edge ret = this.position;
         this.position = this.position.nextBySrc();
         return ret;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   class TargetsOfUnitIterator implements Iterator<Edge> {
      private Edge position = null;
      private Unit u;

      TargetsOfUnitIterator(Unit u) {
         this.u = u;
         if (u == null) {
            throw new RuntimeException();
         } else {
            this.position = (Edge)CallGraph.this.srcUnitToEdge.get(u);
            if (this.position == null) {
               this.position = CallGraph.this.dummy;
            }

         }
      }

      public boolean hasNext() {
         if (this.position.srcUnit() != this.u) {
            return false;
         } else {
            return this.position.kind() != Kind.INVALID;
         }
      }

      public Edge next() {
         Edge ret = this.position;
         this.position = this.position.nextByUnit();
         return ret;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
