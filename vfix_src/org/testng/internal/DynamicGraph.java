package org.testng.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;

public class DynamicGraph<T> {
   private static final boolean DEBUG = false;
   private Set<T> m_nodesReady = Sets.newLinkedHashSet();
   private Set<T> m_nodesRunning = Sets.newLinkedHashSet();
   private Set<T> m_nodesFinished = Sets.newLinkedHashSet();
   private Comparator<? super T> m_nodeComparator = null;
   private ListMultiMap<T, T> m_dependedUpon = Maps.newListMultiMap();
   private ListMultiMap<T, T> m_dependingOn = Maps.newListMultiMap();

   public void setComparator(Comparator<? super T> c) {
      this.m_nodeComparator = c;
   }

   public void addNode(T node) {
      this.m_nodesReady.add(node);
   }

   public void addEdge(T from, T to) {
      this.addNode(from);
      this.addNode(to);
      this.m_dependingOn.put(to, from);
      this.m_dependedUpon.put(from, to);
   }

   public List<T> getFreeNodes() {
      List<T> result = Lists.newArrayList();
      Iterator i$ = this.m_nodesReady.iterator();

      while(i$.hasNext()) {
         T m = i$.next();
         List<T> du = (List)this.m_dependedUpon.get(m);
         if (!this.m_dependedUpon.containsKey(m)) {
            result.add(m);
         } else if (this.getUnfinishedNodes(du).size() == 0) {
            result.add(m);
         }
      }

      if (result != null && !result.isEmpty() && this.m_nodeComparator != null) {
         Collections.sort(result, this.m_nodeComparator);
         ppp("Nodes after sorting:" + result.get(0));
      }

      return result;
   }

   private Collection<? extends T> getUnfinishedNodes(List<T> nodes) {
      Set<T> result = Sets.newHashSet();
      Iterator i$ = nodes.iterator();

      while(true) {
         Object node;
         do {
            if (!i$.hasNext()) {
               return result;
            }

            node = i$.next();
         } while(!this.m_nodesReady.contains(node) && !this.m_nodesRunning.contains(node));

         result.add(node);
      }
   }

   public void setStatus(Collection<T> nodes, DynamicGraph.Status status) {
      Iterator i$ = nodes.iterator();

      while(i$.hasNext()) {
         T n = i$.next();
         this.setStatus(n, status);
      }

   }

   public void setStatus(T node, DynamicGraph.Status status) {
      this.removeNode(node);
      switch(status) {
      case READY:
         this.m_nodesReady.add(node);
         break;
      case RUNNING:
         this.m_nodesRunning.add(node);
         break;
      case FINISHED:
         this.m_nodesFinished.add(node);
         break;
      default:
         throw new IllegalArgumentException();
      }

   }

   private void removeNode(T node) {
      if (!this.m_nodesReady.remove(node) && !this.m_nodesRunning.remove(node)) {
         this.m_nodesFinished.remove(node);
      }

   }

   public int getNodeCount() {
      int result = this.m_nodesReady.size() + this.m_nodesRunning.size() + this.m_nodesFinished.size();
      return result;
   }

   public int getNodeCountWithStatus(DynamicGraph.Status status) {
      switch(status) {
      case READY:
         return this.m_nodesReady.size();
      case RUNNING:
         return this.m_nodesRunning.size();
      case FINISHED:
         return this.m_nodesFinished.size();
      default:
         throw new IllegalArgumentException();
      }
   }

   private static void ppp(String string) {
   }

   public String toString() {
      StringBuilder result = new StringBuilder("[DynamicGraph ");
      result.append("\n  Ready:" + this.m_nodesReady);
      result.append("\n  Running:" + this.m_nodesRunning);
      result.append("\n  Finished:" + this.m_nodesFinished);
      result.append("\n  Edges:\n");
      Iterator i$ = this.m_dependingOn.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<T, List<T>> es = (Entry)i$.next();
         result.append("     " + es.getKey() + "\n");
         Iterator i$ = ((List)es.getValue()).iterator();

         while(i$.hasNext()) {
            T t = i$.next();
            result.append("        " + t + "\n");
         }
      }

      result.append("]");
      return result.toString();
   }

   private String getName(T t) {
      String s = t.toString();
      int n1 = s.lastIndexOf(46) + 1;
      int n2 = s.indexOf(40);
      return s.substring(n1, n2);
   }

   public String toDot() {
      String FREE = "[style=filled color=yellow]";
      String RUNNING = "[style=filled color=green]";
      String FINISHED = "[style=filled color=grey]";
      StringBuilder result = new StringBuilder("digraph g {\n");
      List<T> freeNodes = this.getFreeNodes();
      Iterator i$ = this.m_nodesReady.iterator();

      Object k;
      String color;
      while(i$.hasNext()) {
         k = i$.next();
         color = freeNodes.contains(k) ? FREE : "";
         result.append("  " + this.getName(k) + color + "\n");
      }

      i$ = this.m_nodesRunning.iterator();

      while(i$.hasNext()) {
         k = i$.next();
         color = freeNodes.contains(k) ? FREE : RUNNING;
         result.append("  " + this.getName(k) + color + "\n");
      }

      i$ = this.m_nodesFinished.iterator();

      while(i$.hasNext()) {
         k = i$.next();
         result.append("  " + this.getName(k) + FINISHED + "\n");
      }

      result.append("\n");
      i$ = this.m_dependingOn.keySet().iterator();

      while(i$.hasNext()) {
         k = i$.next();
         List<T> nodes = (List)this.m_dependingOn.get(k);
         Iterator i$ = nodes.iterator();

         while(i$.hasNext()) {
            T n = i$.next();
            String dotted = this.m_nodesFinished.contains(k) ? "style=dotted" : "";
            result.append("  " + this.getName(k) + " -> " + this.getName(n) + " [dir=back " + dotted + "]\n");
         }
      }

      result.append("}\n");
      return result.toString();
   }

   public ListMultiMap<T, T> getEdges() {
      return this.m_dependingOn;
   }

   public static enum Status {
      READY,
      RUNNING,
      FINISHED;
   }
}
