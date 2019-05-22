package org.jboss.util.graph;

public class Edge<T> {
   private Vertex<T> from;
   private Vertex<T> to;
   private int cost;
   private boolean mark;

   public Edge(Vertex<T> from, Vertex<T> to) {
      this(from, to, 0);
   }

   public Edge(Vertex<T> from, Vertex<T> to, int cost) {
      this.from = from;
      this.to = to;
      this.cost = cost;
      this.mark = false;
   }

   public Vertex<T> getTo() {
      return this.to;
   }

   public Vertex<T> getFrom() {
      return this.from;
   }

   public int getCost() {
      return this.cost;
   }

   public void mark() {
      this.mark = true;
   }

   public void clearMark() {
      this.mark = false;
   }

   public boolean isMarked() {
      return this.mark;
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("Edge[from: ");
      tmp.append(this.from.getName());
      tmp.append(",to: ");
      tmp.append(this.to.getName());
      tmp.append(", cost: ");
      tmp.append(this.cost);
      tmp.append("]");
      return tmp.toString();
   }
}
