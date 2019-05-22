package org.testng.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class Tarjan<T> {
   int m_index = 0;
   private Stack<T> m_s = new Stack();
   Map<T, Integer> m_indices = Maps.newHashMap();
   Map<T, Integer> m_lowlinks = Maps.newHashMap();
   private List<T> m_cycle;

   public Tarjan(Graph<T> graph, T start) {
      this.run(graph, start);
   }

   private void run(Graph<T> graph, T v) {
      this.m_indices.put(v, this.m_index);
      this.m_lowlinks.put(v, this.m_index);
      ++this.m_index;
      this.m_s.push(v);
      Iterator i$ = graph.getPredecessors(v).iterator();

      while(i$.hasNext()) {
         T vprime = i$.next();
         if (!this.m_indices.containsKey(vprime)) {
            this.run(graph, vprime);
            int min = Math.min((Integer)this.m_lowlinks.get(v), (Integer)this.m_lowlinks.get(vprime));
            this.m_lowlinks.put(v, min);
         } else if (this.m_s.contains(vprime)) {
            this.m_lowlinks.put(v, Math.min((Integer)this.m_lowlinks.get(v), (Integer)this.m_indices.get(vprime)));
         }
      }

      if (this.m_lowlinks.get(v) == this.m_indices.get(v)) {
         this.m_cycle = Lists.newArrayList();

         Object n;
         do {
            n = this.m_s.pop();
            this.m_cycle.add(n);
         } while(!n.equals(v));
      }

   }

   public static void main(String[] args) {
      Graph<String> g = new Graph();
      g.addNode("a");
      g.addNode("b");
      g.addNode("c");
      g.addNode("d");
      String[] edges = new String[]{"a", "b", "b", "a", "c", "d", "d", "a", "a", "c"};

      for(int i = 0; i < edges.length; i += 2) {
         g.addPredecessor(edges[i], edges[i + 1]);
      }

      new Tarjan(g, "a");
   }

   public List<T> getCycle() {
      return this.m_cycle;
   }
}
