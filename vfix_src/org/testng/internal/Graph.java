package org.testng.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class Graph<T> {
   private static boolean m_verbose = false;
   private Map<T, Graph.Node<T>> m_nodes = Maps.newLinkedHashMap();
   private List<T> m_strictlySortedNodes = null;
   private Map<T, Graph.Node<T>> m_independentNodes = null;

   public void addNode(T tm) {
      ppp("ADDING NODE " + tm + " " + tm.hashCode());
      this.m_nodes.put(tm, new Graph.Node(tm));
   }

   public Set<T> getPredecessors(T node) {
      return this.findNode(node).getPredecessors().keySet();
   }

   public boolean isIndependent(T object) {
      return this.m_independentNodes.containsKey(object);
   }

   private Graph.Node<T> findNode(T object) {
      return (Graph.Node)this.m_nodes.get(object);
   }

   public void addPredecessor(T tm, T predecessor) {
      Graph.Node<T> node = this.findNode(tm);
      if (null == node) {
         throw new TestNGException("Non-existing node: " + tm);
      } else {
         node.addPredecessor(predecessor);
         this.addNeighbor(tm, predecessor);
         if (null == this.m_independentNodes) {
            this.m_independentNodes = Maps.newHashMap();
            this.m_independentNodes.putAll(this.m_nodes);
         }

         this.m_independentNodes.remove(predecessor);
         this.m_independentNodes.remove(tm);
         ppp("  REMOVED " + predecessor + " FROM INDEPENDENT OBJECTS");
      }
   }

   private void addNeighbor(T tm, T predecessor) {
      this.findNode(tm).addNeighbor(this.findNode(predecessor));
   }

   public Set<T> getNeighbors(T t) {
      Set<T> result = new HashSet();
      Iterator i$ = this.findNode(t).getNeighbors().iterator();

      while(i$.hasNext()) {
         Graph.Node<T> n = (Graph.Node)i$.next();
         result.add(n.getObject());
      }

      return result;
   }

   private Collection<Graph.Node<T>> getNodes() {
      return this.m_nodes.values();
   }

   public Collection<T> getNodeValues() {
      return this.m_nodes.keySet();
   }

   public Set<T> getIndependentNodes() {
      return this.m_independentNodes.keySet();
   }

   public List<T> getStrictlySortedNodes() {
      return this.m_strictlySortedNodes;
   }

   public void topologicalSort() {
      ppp("================ SORTING");
      this.m_strictlySortedNodes = Lists.newArrayList();
      if (null == this.m_independentNodes) {
         this.m_independentNodes = Maps.newHashMap();
      }

      List<Graph.Node<T>> nodes2 = Lists.newArrayList();
      Iterator i$ = this.getNodes().iterator();

      while(i$.hasNext()) {
         Graph.Node<T> n = (Graph.Node)i$.next();
         if (!this.isIndependent(n.getObject())) {
            ppp("ADDING FOR SORT: " + n.getObject());
            nodes2.add(n.clone());
         } else {
            ppp("SKIPPING INDEPENDENT NODE " + n);
         }
      }

      Collections.sort(nodes2);

      while(!nodes2.isEmpty()) {
         Graph.Node<T> node = this.findNodeWithNoPredecessors(nodes2);
         if (null == node) {
            List<T> cycle = (new Tarjan(this, ((Graph.Node)nodes2.get(0)).getObject())).getCycle();
            StringBuilder sb = new StringBuilder();
            sb.append("The following methods have cyclic dependencies:\n");
            Iterator i$ = cycle.iterator();

            while(i$.hasNext()) {
               T m = i$.next();
               sb.append(m).append("\n");
            }

            throw new TestNGException(sb.toString());
         }

         this.m_strictlySortedNodes.add(node.getObject());
         this.removeFromNodes(nodes2, node);
      }

      ppp("=============== DONE SORTING");
      if (m_verbose) {
         this.dumpSortedNodes();
      }

   }

   private void dumpSortedNodes() {
      System.out.println("====== SORTED NODES");
      Iterator i$ = this.m_strictlySortedNodes.iterator();

      while(i$.hasNext()) {
         T n = i$.next();
         System.out.println("              " + n);
      }

      System.out.println("====== END SORTED NODES");
   }

   private void removeFromNodes(List<Graph.Node<T>> nodes, Graph.Node<T> node) {
      nodes.remove(node);
      Iterator i$ = nodes.iterator();

      while(i$.hasNext()) {
         Graph.Node<T> n = (Graph.Node)i$.next();
         n.removePredecessor(node.getObject());
      }

   }

   private static void ppp(String s) {
      if (m_verbose) {
         System.out.println("[Graph] " + s);
      }

   }

   private Graph.Node<T> findNodeWithNoPredecessors(List<Graph.Node<T>> nodes) {
      Iterator i$ = nodes.iterator();

      Graph.Node n;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         n = (Graph.Node)i$.next();
      } while(n.hasPredecessors());

      return n;
   }

   public List<T> findPredecessors(T o) {
      Graph.Node<T> node = this.findNode(o);
      if (null == node) {
         return Lists.newArrayList();
      } else {
         LinkedList<T> result = new LinkedList();
         Set<T> visited = new HashSet();
         LinkedList<T> queue = new LinkedList();
         visited.add(o);
         queue.addLast(o);

         while(!queue.isEmpty()) {
            Iterator i$ = this.getPredecessors(queue.removeFirst()).iterator();

            while(i$.hasNext()) {
               T obj = i$.next();
               if (!visited.contains(obj)) {
                  visited.add(obj);
                  queue.addLast(obj);
                  result.addFirst(obj);
               }
            }
         }

         return result;
      }
   }

   public String toString() {
      StringBuilder result = new StringBuilder("[Graph ");
      Iterator i$ = this.m_nodes.keySet().iterator();

      while(i$.hasNext()) {
         T node = i$.next();
         result.append(this.findNode(node)).append(" ");
      }

      result.append("]");
      return result.toString();
   }

   public static class Node<T> implements Comparable<Graph.Node<T>> {
      private T m_object = null;
      private Map<T, T> m_predecessors = Maps.newHashMap();
      private Set<Graph.Node<T>> m_neighbors = new HashSet();

      public Node(T tm) {
         this.m_object = tm;
      }

      public void addNeighbor(Graph.Node<T> neighbor) {
         this.m_neighbors.add(neighbor);
      }

      public Set<Graph.Node<T>> getNeighbors() {
         return this.m_neighbors;
      }

      public Graph.Node<T> clone() {
         Graph.Node<T> result = new Graph.Node(this.m_object);
         Iterator i$ = this.m_predecessors.values().iterator();

         while(i$.hasNext()) {
            T pred = i$.next();
            result.addPredecessor(pred);
         }

         return result;
      }

      public T getObject() {
         return this.m_object;
      }

      public Map<T, T> getPredecessors() {
         return this.m_predecessors;
      }

      public boolean removePredecessor(T o) {
         boolean result = false;
         T pred = this.m_predecessors.get(o);
         if (null != pred) {
            result = null != this.m_predecessors.remove(o);
            if (result) {
               Graph.ppp("  REMOVED PRED " + o + " FROM NODE " + this.m_object);
            } else {
               Graph.ppp("  FAILED TO REMOVE PRED " + o + " FROM NODE " + this.m_object);
            }
         }

         return result;
      }

      public String toString() {
         StringBuilder sb = new StringBuilder("[Node:" + this.m_object);
         sb.append("  pred:");
         Iterator i$ = this.m_predecessors.values().iterator();

         while(i$.hasNext()) {
            T o = i$.next();
            sb.append(" ").append(o);
         }

         sb.append("]");
         String result = sb.toString();
         return result;
      }

      public void addPredecessor(T tm) {
         Graph.ppp("  ADDING PREDECESSOR FOR " + this.m_object + " ==> " + tm);
         this.m_predecessors.put(tm, tm);
      }

      public boolean hasPredecessors() {
         return this.m_predecessors.size() > 0;
      }

      public boolean hasPredecessor(T m) {
         return this.m_predecessors.containsKey(m);
      }

      public int compareTo(Graph.Node<T> o) {
         return this.getObject().toString().compareTo(o.getObject().toString());
      }
   }
}
