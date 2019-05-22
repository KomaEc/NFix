package org.jboss.util.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph<T> {
   public static final int VISIT_COLOR_WHITE = 1;
   public static final int VISIT_COLOR_GREY = 2;
   public static final int VISIT_COLOR_BLACK = 3;
   private Map<String, Vertex<T>> verticies = new LinkedHashMap();
   private List<Edge<T>> edges = new ArrayList();
   private Vertex<T> rootVertex;

   public boolean isEmpty() {
      return this.verticies.size() == 0;
   }

   public boolean addVertex(Vertex<T> v) {
      if (!this.verticies.containsValue(v)) {
         this.verticies.put(v.getName(), v);
         return true;
      } else {
         return false;
      }
   }

   public int size() {
      return this.verticies.size();
   }

   public Vertex<T> getRootVertex() {
      return this.rootVertex;
   }

   public void setRootVertex(Vertex<T> root) {
      this.rootVertex = root;
      if (!this.verticies.containsValue(root)) {
         this.addVertex(root);
      }

   }

   public Vertex<T> getVertex(int n) {
      return (Vertex)this.getVerticies().get(n);
   }

   public List<Vertex<T>> getVerticies() {
      return new ArrayList(this.verticies.values());
   }

   public boolean addEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException {
      if (!this.verticies.containsValue(from)) {
         throw new IllegalArgumentException("from is not in graph");
      } else if (!this.verticies.containsValue(to)) {
         throw new IllegalArgumentException("to is not in graph");
      } else {
         Edge<T> e = new Edge(from, to, cost);
         if (from.findEdge(to) != null) {
            return false;
         } else {
            from.addEdge(e);
            to.addEdge(e);
            this.edges.add(e);
            return true;
         }
      }
   }

   public boolean insertBiEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException {
      return this.addEdge(from, to, cost) && this.addEdge(to, from, cost);
   }

   public List<Edge<T>> getEdges() {
      return this.edges;
   }

   public boolean removeVertex(Vertex<T> v) {
      if (!this.verticies.containsValue(v)) {
         return false;
      } else {
         this.verticies.remove(v.getName());
         if (v == this.rootVertex) {
            this.rootVertex = null;
         }

         int n;
         Edge e;
         Vertex predecessor;
         for(n = 0; n < v.getOutgoingEdgeCount(); ++n) {
            e = v.getOutgoingEdge(n);
            v.remove(e);
            predecessor = e.getTo();
            predecessor.remove(e);
            this.edges.remove(e);
         }

         for(n = 0; n < v.getIncomingEdgeCount(); ++n) {
            e = v.getIncomingEdge(n);
            v.remove(e);
            predecessor = e.getFrom();
            predecessor.remove(e);
         }

         return true;
      }
   }

   public boolean removeEdge(Vertex<T> from, Vertex<T> to) {
      Edge<T> e = from.findEdge(to);
      if (e == null) {
         return false;
      } else {
         from.remove(e);
         to.remove(e);
         this.edges.remove(e);
         return true;
      }
   }

   public void clearMark() {
      Iterator i$ = this.verticies.values().iterator();

      while(i$.hasNext()) {
         Vertex<T> w = (Vertex)i$.next();
         w.clearMark();
      }

   }

   public void clearEdges() {
      Iterator i$ = this.edges.iterator();

      while(i$.hasNext()) {
         Edge<T> e = (Edge)i$.next();
         e.clearMark();
      }

   }

   public void depthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
      VisitorEX<T, RuntimeException> wrapper = new VisitorEX<T, RuntimeException>() {
         public void visit(Graph<T> g, Vertex<T> v) throws RuntimeException {
            if (visitor != null) {
               visitor.visit(g, v);
            }

         }
      };
      this.depthFirstSearch(v, wrapper);
   }

   public <E extends Exception> void depthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E {
      if (visitor != null) {
         visitor.visit(this, v);
      }

      v.visit();

      for(int i = 0; i < v.getOutgoingEdgeCount(); ++i) {
         Edge<T> e = v.getOutgoingEdge(i);
         if (!e.getTo().visited()) {
            this.depthFirstSearch(e.getTo(), visitor);
         }
      }

   }

   public void breadthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
      VisitorEX<T, RuntimeException> wrapper = new VisitorEX<T, RuntimeException>() {
         public void visit(Graph<T> g, Vertex<T> v) throws RuntimeException {
            if (visitor != null) {
               visitor.visit(g, v);
            }

         }
      };
      this.breadthFirstSearch(v, wrapper);
   }

   public <E extends Exception> void breadthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E {
      LinkedList<Vertex<T>> q = new LinkedList();
      q.add(v);
      if (visitor != null) {
         visitor.visit(this, v);
      }

      v.visit();

      while(!q.isEmpty()) {
         v = (Vertex)q.removeFirst();

         for(int i = 0; i < v.getOutgoingEdgeCount(); ++i) {
            Edge<T> e = v.getOutgoingEdge(i);
            Vertex<T> to = e.getTo();
            if (!to.visited()) {
               q.add(to);
               if (visitor != null) {
                  visitor.visit(this, to);
               }

               to.visit();
            }
         }
      }

   }

   public void dfsSpanningTree(Vertex<T> v, DFSVisitor<T> visitor) {
      v.visit();
      if (visitor != null) {
         visitor.visit(this, v);
      }

      for(int i = 0; i < v.getOutgoingEdgeCount(); ++i) {
         Edge<T> e = v.getOutgoingEdge(i);
         if (!e.getTo().visited()) {
            if (visitor != null) {
               visitor.visit(this, v, e);
            }

            e.mark();
            this.dfsSpanningTree(e.getTo(), visitor);
         }
      }

   }

   public Vertex<T> findVertexByName(String name) {
      return (Vertex)this.verticies.get(name);
   }

   public Vertex<T> findVertexByData(T data, Comparator<T> compare) {
      Vertex<T> match = null;
      Iterator i$ = this.verticies.values().iterator();

      while(i$.hasNext()) {
         Vertex<T> v = (Vertex)i$.next();
         if (compare.compare(data, v.getData()) == 0) {
            match = v;
            break;
         }
      }

      return match;
   }

   public Edge<T>[] findCycles() {
      ArrayList<Edge<T>> cycleEdges = new ArrayList();

      int n;
      Vertex v;
      for(n = 0; n < this.verticies.size(); ++n) {
         v = this.getVertex(n);
         v.setMarkState(1);
      }

      for(n = 0; n < this.verticies.size(); ++n) {
         v = this.getVertex(n);
         this.visit(v, cycleEdges);
      }

      Edge<T>[] cycles = new Edge[cycleEdges.size()];
      cycleEdges.toArray(cycles);
      return cycles;
   }

   private void visit(Vertex<T> v, ArrayList<Edge<T>> cycleEdges) {
      v.setMarkState(2);
      int count = v.getOutgoingEdgeCount();

      for(int n = 0; n < count; ++n) {
         Edge<T> e = v.getOutgoingEdge(n);
         Vertex<T> u = e.getTo();
         if (u.getMarkState() == 2) {
            cycleEdges.add(e);
         } else if (u.getMarkState() == 1) {
            this.visit(u, cycleEdges);
         }
      }

      v.setMarkState(3);
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("Graph[");
      Iterator i$ = this.verticies.values().iterator();

      while(i$.hasNext()) {
         Vertex<T> v = (Vertex)i$.next();
         tmp.append(v);
      }

      tmp.append(']');
      return tmp.toString();
   }
}
