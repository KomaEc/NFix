package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashMutableDirectedGraph<N> implements MutableDirectedGraph<N> {
   private static final Logger logger = LoggerFactory.getLogger(HashMutableDirectedGraph.class);
   protected Map<N, Set<N>> nodeToPreds = new HashMap();
   protected Map<N, Set<N>> nodeToSuccs = new HashMap();
   protected Set<N> heads = new HashSet();
   protected Set<N> tails = new HashSet();

   private static <T> List<T> getCopy(Collection<? extends T> c) {
      return Collections.unmodifiableList(new ArrayList(c));
   }

   public void clearAll() {
      this.nodeToPreds.clear();
      this.nodeToSuccs.clear();
      this.heads.clear();
      this.tails.clear();
   }

   public Object clone() {
      HashMutableDirectedGraph<N> g = new HashMutableDirectedGraph();
      g.nodeToPreds.putAll(this.nodeToPreds);
      g.nodeToSuccs.putAll(this.nodeToSuccs);
      g.heads.addAll(this.heads);
      g.tails.addAll(this.tails);
      return g;
   }

   public List<N> getHeads() {
      return getCopy(this.heads);
   }

   public List<N> getTails() {
      return getCopy(this.tails);
   }

   public List<N> getPredsOf(N s) {
      Set<N> preds = (Set)this.nodeToPreds.get(s);
      if (preds != null) {
         return getCopy(preds);
      } else {
         throw new RuntimeException(s + "not in graph!");
      }
   }

   public Set<N> getPredsOfAsSet(N s) {
      Set<N> preds = (Set)this.nodeToPreds.get(s);
      if (preds != null) {
         return Collections.unmodifiableSet(preds);
      } else {
         throw new RuntimeException(s + "not in graph!");
      }
   }

   public List<N> getSuccsOf(N s) {
      Set<N> succs = (Set)this.nodeToSuccs.get(s);
      if (succs != null) {
         return getCopy(succs);
      } else {
         throw new RuntimeException(s + "not in graph!");
      }
   }

   public Set<N> getSuccsOfAsSet(N s) {
      Set<N> succs = (Set)this.nodeToSuccs.get(s);
      if (succs != null) {
         return Collections.unmodifiableSet(succs);
      } else {
         throw new RuntimeException(s + "not in graph!");
      }
   }

   public int size() {
      return this.nodeToPreds.keySet().size();
   }

   public Iterator<N> iterator() {
      return this.nodeToPreds.keySet().iterator();
   }

   public void addEdge(N from, N to) {
      if (from != null && to != null) {
         if (!this.containsEdge(from, to)) {
            Set<N> succsList = (Set)this.nodeToSuccs.get(from);
            if (succsList == null) {
               throw new RuntimeException(from + " not in graph!");
            } else {
               Set<N> predsList = (Set)this.nodeToPreds.get(to);
               if (predsList == null) {
                  throw new RuntimeException(to + " not in graph!");
               } else {
                  this.heads.remove(to);
                  this.tails.remove(from);
                  succsList.add(to);
                  predsList.add(from);
               }
            }
         }
      } else {
         throw new RuntimeException("edge from or to null");
      }
   }

   public void removeEdge(N from, N to) {
      if (this.containsEdge(from, to)) {
         Set<N> succs = (Set)this.nodeToSuccs.get(from);
         if (succs == null) {
            throw new RuntimeException(from + " not in graph!");
         } else {
            Set<N> preds = (Set)this.nodeToPreds.get(to);
            if (preds == null) {
               throw new RuntimeException(to + " not in graph!");
            } else {
               succs.remove(to);
               preds.remove(from);
               if (succs.isEmpty()) {
                  this.tails.add(from);
               }

               if (preds.isEmpty()) {
                  this.heads.add(to);
               }

            }
         }
      }
   }

   public boolean containsEdge(N from, N to) {
      Set<N> succs = (Set)this.nodeToSuccs.get(from);
      return succs == null ? false : succs.contains(to);
   }

   public boolean containsNode(Object node) {
      return this.nodeToPreds.keySet().contains(node);
   }

   public List<N> getNodes() {
      return getCopy(this.nodeToPreds.keySet());
   }

   public void addNode(N node) {
      if (this.containsNode(node)) {
         throw new RuntimeException("Node already in graph");
      } else {
         this.nodeToSuccs.put(node, new LinkedHashSet());
         this.nodeToPreds.put(node, new LinkedHashSet());
         this.heads.add(node);
         this.tails.add(node);
      }
   }

   public void removeNode(N node) {
      Iterator var2 = (new ArrayList((Collection)this.nodeToSuccs.get(node))).iterator();

      Object n;
      while(var2.hasNext()) {
         n = var2.next();
         this.removeEdge(node, n);
      }

      this.nodeToSuccs.remove(node);
      var2 = (new ArrayList((Collection)this.nodeToPreds.get(node))).iterator();

      while(var2.hasNext()) {
         n = var2.next();
         this.removeEdge(n, node);
      }

      this.nodeToPreds.remove(node);
      this.heads.remove(node);
      this.tails.remove(node);
   }

   public void printGraph() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         N node = var1.next();
         logger.debug("Node = " + node);
         logger.debug("Preds:");
         Iterator var3 = this.getPredsOf(node).iterator();

         Object s;
         while(var3.hasNext()) {
            s = var3.next();
            logger.debug("     ");
            logger.debug("" + s);
         }

         logger.debug("Succs:");
         var3 = this.getSuccsOf(node).iterator();

         while(var3.hasNext()) {
            s = var3.next();
            logger.debug("     ");
            logger.debug("" + s);
         }
      }

   }
}
