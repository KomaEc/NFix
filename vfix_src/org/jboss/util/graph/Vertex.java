package org.jboss.util.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Vertex<T> {
   private List<Edge<T>> incomingEdges;
   private List<Edge<T>> outgoingEdges;
   private String name;
   private boolean mark;
   private int markState;
   private T data;

   public Vertex() {
      this((String)null, (Object)null);
   }

   public Vertex(String n) {
      this(n, (Object)null);
   }

   public Vertex(String n, T data) {
      this.incomingEdges = new ArrayList();
      this.outgoingEdges = new ArrayList();
      this.name = n;
      this.mark = false;
      this.data = data;
   }

   public String getName() {
      return this.name;
   }

   public T getData() {
      return this.data;
   }

   public void setData(T data) {
      this.data = data;
   }

   public boolean addEdge(Edge<T> e) {
      if (e.getFrom() == this) {
         this.outgoingEdges.add(e);
      } else {
         if (e.getTo() != this) {
            return false;
         }

         this.incomingEdges.add(e);
      }

      return true;
   }

   public void addOutgoingEdge(Vertex<T> to, int cost) {
      Edge<T> out = new Edge(this, to, cost);
      this.outgoingEdges.add(out);
   }

   public void addIncomingEdge(Vertex<T> from, int cost) {
      Edge<T> out = new Edge(this, from, cost);
      this.incomingEdges.add(out);
   }

   public boolean hasEdge(Edge<T> e) {
      if (e.getFrom() == this) {
         return this.outgoingEdges.contains(e);
      } else {
         return e.getTo() == this ? this.incomingEdges.contains(e) : false;
      }
   }

   public boolean remove(Edge<T> e) {
      if (e.getFrom() == this) {
         this.outgoingEdges.remove(e);
      } else {
         if (e.getTo() != this) {
            return false;
         }

         this.incomingEdges.remove(e);
      }

      return true;
   }

   public int getIncomingEdgeCount() {
      return this.incomingEdges.size();
   }

   public Edge<T> getIncomingEdge(int i) {
      return (Edge)this.incomingEdges.get(i);
   }

   public List getIncomingEdges() {
      return this.incomingEdges;
   }

   public int getOutgoingEdgeCount() {
      return this.outgoingEdges.size();
   }

   public Edge<T> getOutgoingEdge(int i) {
      return (Edge)this.outgoingEdges.get(i);
   }

   public List getOutgoingEdges() {
      return this.outgoingEdges;
   }

   public Edge<T> findEdge(Vertex<T> dest) {
      Iterator i$ = this.outgoingEdges.iterator();

      Edge e;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         e = (Edge)i$.next();
      } while(e.getTo() != dest);

      return e;
   }

   public Edge<T> findEdge(Edge<T> e) {
      return this.outgoingEdges.contains(e) ? e : null;
   }

   public int cost(Vertex<T> dest) {
      if (dest == this) {
         return 0;
      } else {
         Edge<T> e = this.findEdge(dest);
         int cost = Integer.MAX_VALUE;
         if (e != null) {
            cost = e.getCost();
         }

         return cost;
      }
   }

   public boolean hasEdge(Vertex<T> dest) {
      return this.findEdge(dest) != null;
   }

   public boolean visited() {
      return this.mark;
   }

   public void mark() {
      this.mark = true;
   }

   public void setMarkState(int state) {
      this.markState = state;
   }

   public int getMarkState() {
      return this.markState;
   }

   public void visit() {
      this.mark();
   }

   public void clearMark() {
      this.mark = false;
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("Vertex(");
      tmp.append(this.name);
      tmp.append(", data=");
      tmp.append(this.data);
      tmp.append("), in:[");

      int i;
      Edge e;
      for(i = 0; i < this.incomingEdges.size(); ++i) {
         e = (Edge)this.incomingEdges.get(i);
         if (i > 0) {
            tmp.append(',');
         }

         tmp.append('{');
         tmp.append(e.getFrom().name);
         tmp.append(',');
         tmp.append(e.getCost());
         tmp.append('}');
      }

      tmp.append("], out:[");

      for(i = 0; i < this.outgoingEdges.size(); ++i) {
         e = (Edge)this.outgoingEdges.get(i);
         if (i > 0) {
            tmp.append(',');
         }

         tmp.append('{');
         tmp.append(e.getTo().name);
         tmp.append(',');
         tmp.append(e.getCost());
         tmp.append('}');
      }

      tmp.append(']');
      return tmp.toString();
   }
}
