package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.Iterator;
import soot.tagkit.Attribute;

public class DependenceGraph implements Attribute {
   private static final String NAME = "DependenceGraph";
   HashSet<DependenceGraph.Edge> edges = new HashSet();

   public boolean areAdjacent(short from, short to) {
      if (from > to) {
         return this.areAdjacent(to, from);
      } else if (from >= 0 && to >= 0) {
         return from == to ? true : this.edges.contains(new DependenceGraph.Edge(from, to));
      } else {
         return false;
      }
   }

   public void addEdge(short from, short to) {
      if (from < 0) {
         throw new RuntimeException("from < 0");
      } else if (to < 0) {
         throw new RuntimeException("to < 0");
      } else if (from > to) {
         this.addEdge(to, from);
      } else {
         this.edges.add(new DependenceGraph.Edge(from, to));
      }
   }

   public String getName() {
      return "DependenceGraph";
   }

   public void setValue(byte[] v) {
      throw new RuntimeException("Not Supported");
   }

   public byte[] getValue() {
      byte[] ret = new byte[4 * this.edges.size()];
      int i = 0;

      for(Iterator var3 = this.edges.iterator(); var3.hasNext(); i += 4) {
         DependenceGraph.Edge e = (DependenceGraph.Edge)var3.next();
         ret[i + 0] = (byte)(e.from >> 8 & 255);
         ret[i + 1] = (byte)(e.from & 255);
         ret[i + 2] = (byte)(e.to >> 8 & 255);
         ret[i + 3] = (byte)(e.to & 255);
      }

      return ret;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("Dependences");
      Iterator var2 = this.edges.iterator();

      while(var2.hasNext()) {
         DependenceGraph.Edge e = (DependenceGraph.Edge)var2.next();
         buf.append("( " + e.from + ", " + e.to + " ) ");
      }

      return buf.toString();
   }

   protected class Edge {
      short from;
      short to;

      Edge(short from, short to) {
         this.from = from;
         this.to = to;
      }

      public int hashCode() {
         return (this.from << 16) + this.to;
      }

      public boolean equals(Object other) {
         DependenceGraph.Edge o = (DependenceGraph.Edge)other;
         return this.from == o.from && this.to == o.to;
      }
   }
}
