package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashMutableEdgeLabelledDirectedGraph<N, L> implements MutableEdgeLabelledDirectedGraph<N, L> {
   private static final Logger logger = LoggerFactory.getLogger(HashMutableEdgeLabelledDirectedGraph.class);
   protected Map<N, List<N>> nodeToPreds = new HashMap();
   protected Map<N, List<N>> nodeToSuccs = new HashMap();
   protected Map<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>, List<L>> edgeToLabels = new HashMap();
   protected Map<L, List<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>>> labelToEdges = new HashMap();
   protected Set<N> heads = new HashSet();
   protected Set<N> tails = new HashSet();

   private static <T> List<T> getCopy(Collection<? extends T> c) {
      return Collections.unmodifiableList(new ArrayList(c));
   }

   public void clearAll() {
      this.nodeToPreds.clear();
      this.nodeToSuccs.clear();
      this.edgeToLabels.clear();
      this.labelToEdges.clear();
      this.heads.clear();
      this.tails.clear();
   }

   public HashMutableEdgeLabelledDirectedGraph<N, L> clone() {
      HashMutableEdgeLabelledDirectedGraph<N, L> g = new HashMutableEdgeLabelledDirectedGraph();
      g.nodeToPreds.putAll(this.nodeToPreds);
      g.nodeToSuccs.putAll(this.nodeToSuccs);
      g.edgeToLabels.putAll(this.edgeToLabels);
      g.labelToEdges.putAll(this.labelToEdges);
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
      List<N> preds = (List)this.nodeToPreds.get(s);
      if (preds != null) {
         return Collections.unmodifiableList(preds);
      } else {
         throw new RuntimeException(s + "not in graph!");
      }
   }

   public List<N> getSuccsOf(N s) {
      List<N> succs = (List)this.nodeToSuccs.get(s);
      if (succs != null) {
         return Collections.unmodifiableList(succs);
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

   public void addEdge(N from, N to, L label) {
      if (from != null && to != null) {
         if (label == null) {
            throw new RuntimeException("edge with null label");
         } else if (!this.containsEdge(from, to, label)) {
            List<N> succsList = (List)this.nodeToSuccs.get(from);
            if (succsList == null) {
               throw new RuntimeException(from + " not in graph!");
            } else {
               List<N> predsList = (List)this.nodeToPreds.get(to);
               if (predsList == null) {
                  throw new RuntimeException(to + " not in graph!");
               } else {
                  this.heads.remove(to);
                  this.tails.remove(from);
                  if (!succsList.contains(to)) {
                     succsList.add(to);
                  }

                  if (!predsList.contains(from)) {
                     predsList.add(from);
                  }

                  HashMutableEdgeLabelledDirectedGraph.DGEdge<N> edge = new HashMutableEdgeLabelledDirectedGraph.DGEdge(from, to);
                  if (!this.edgeToLabels.containsKey(edge)) {
                     this.edgeToLabels.put(edge, new ArrayList());
                  }

                  List<L> labels = (List)this.edgeToLabels.get(edge);
                  if (!this.labelToEdges.containsKey(label)) {
                     this.labelToEdges.put(label, new ArrayList());
                  }

                  List<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>> edges = (List)this.labelToEdges.get(label);
                  labels.add(label);
                  edges.add(edge);
               }
            }
         }
      } else {
         throw new RuntimeException("edge from or to null");
      }
   }

   public List<L> getLabelsForEdges(N from, N to) {
      HashMutableEdgeLabelledDirectedGraph.DGEdge<N> edge = new HashMutableEdgeLabelledDirectedGraph.DGEdge(from, to);
      return (List)this.edgeToLabels.get(edge);
   }

   public MutableDirectedGraph<N> getEdgesForLabel(L label) {
      List<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>> edges = (List)this.labelToEdges.get(label);
      MutableDirectedGraph<N> ret = new HashMutableDirectedGraph();
      if (edges == null) {
         return ret;
      } else {
         HashMutableEdgeLabelledDirectedGraph.DGEdge edge;
         for(Iterator var4 = edges.iterator(); var4.hasNext(); ret.addEdge(edge.from(), edge.to())) {
            edge = (HashMutableEdgeLabelledDirectedGraph.DGEdge)var4.next();
            if (!ret.containsNode(edge.from())) {
               ret.addNode(edge.from());
            }

            if (!ret.containsNode(edge.to())) {
               ret.addNode(edge.to());
            }
         }

         return ret;
      }
   }

   public void removeEdge(N from, N to, L label) {
      if (this.containsEdge(from, to, label)) {
         HashMutableEdgeLabelledDirectedGraph.DGEdge<N> edge = new HashMutableEdgeLabelledDirectedGraph.DGEdge(from, to);
         List<L> labels = (List)this.edgeToLabels.get(edge);
         if (labels == null) {
            throw new RuntimeException("edge " + edge + " not in graph!");
         } else {
            List<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>> edges = (List)this.labelToEdges.get(label);
            if (edges == null) {
               throw new RuntimeException("label " + label + " not in graph!");
            } else {
               labels.remove(label);
               edges.remove(edge);
               if (labels.isEmpty()) {
                  this.edgeToLabels.remove(edge);
                  List<N> succsList = (List)this.nodeToSuccs.get(from);
                  if (succsList == null) {
                     throw new RuntimeException(from + " not in graph!");
                  }

                  List<N> predsList = (List)this.nodeToPreds.get(to);
                  if (predsList == null) {
                     throw new RuntimeException(to + " not in graph!");
                  }

                  succsList.remove(to);
                  predsList.remove(from);
                  if (succsList.isEmpty()) {
                     this.tails.add(from);
                  }

                  if (predsList.isEmpty()) {
                     this.heads.add(to);
                  }
               }

               if (edges.isEmpty()) {
                  this.labelToEdges.remove(label);
               }

            }
         }
      }
   }

   public void removeAllEdges(N from, N to) {
      if (this.containsAnyEdge(from, to)) {
         HashMutableEdgeLabelledDirectedGraph.DGEdge<N> edge = new HashMutableEdgeLabelledDirectedGraph.DGEdge(from, to);
         List<L> labels = (List)this.edgeToLabels.get(edge);
         if (labels == null) {
            throw new RuntimeException("edge " + edge + " not in graph!");
         } else {
            Iterator var5 = getCopy(labels).iterator();

            while(var5.hasNext()) {
               L label = var5.next();
               this.removeEdge(from, to, label);
            }

         }
      }
   }

   public void removeAllEdges(L label) {
      if (this.containsAnyEdge(label)) {
         List<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>> edges = (List)this.labelToEdges.get(label);
         if (edges == null) {
            throw new RuntimeException("label " + label + " not in graph!");
         } else {
            Iterator var3 = getCopy(edges).iterator();

            while(var3.hasNext()) {
               HashMutableEdgeLabelledDirectedGraph.DGEdge<N> edge = (HashMutableEdgeLabelledDirectedGraph.DGEdge)var3.next();
               this.removeEdge(edge.from(), edge.to(), label);
            }

         }
      }
   }

   public boolean containsEdge(N from, N to, L label) {
      List<L> labels = (List)this.edgeToLabels.get(new HashMutableEdgeLabelledDirectedGraph.DGEdge(from, to));
      return labels != null && labels.contains(label);
   }

   public boolean containsAnyEdge(N from, N to) {
      List<L> labels = (List)this.edgeToLabels.get(new HashMutableEdgeLabelledDirectedGraph.DGEdge(from, to));
      return labels != null && !labels.isEmpty();
   }

   public boolean containsAnyEdge(L label) {
      List<HashMutableEdgeLabelledDirectedGraph.DGEdge<N>> edges = (List)this.labelToEdges.get(label);
      return edges != null && !edges.isEmpty();
   }

   public boolean containsNode(N node) {
      return this.nodeToPreds.keySet().contains(node);
   }

   public void addNode(N node) {
      if (this.containsNode(node)) {
         throw new RuntimeException("Node already in graph");
      } else {
         this.nodeToSuccs.put(node, new ArrayList());
         this.nodeToPreds.put(node, new ArrayList());
         this.heads.add(node);
         this.tails.add(node);
      }
   }

   public void removeNode(N node) {
      Iterator var2 = (new ArrayList((Collection)this.nodeToSuccs.get(node))).iterator();

      Object n;
      while(var2.hasNext()) {
         n = var2.next();
         this.removeAllEdges(node, n);
      }

      this.nodeToSuccs.remove(node);
      var2 = (new ArrayList((Collection)this.nodeToPreds.get(node))).iterator();

      while(var2.hasNext()) {
         n = var2.next();
         this.removeAllEdges(n, node);
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

         Object succ;
         HashMutableEdgeLabelledDirectedGraph.DGEdge edge;
         List labels;
         while(var3.hasNext()) {
            succ = var3.next();
            edge = new HashMutableEdgeLabelledDirectedGraph.DGEdge(succ, node);
            labels = (List)this.edgeToLabels.get(edge);
            logger.debug("     " + succ + " [" + labels + "]");
         }

         logger.debug("Succs:");
         var3 = this.getSuccsOf(node).iterator();

         while(var3.hasNext()) {
            succ = var3.next();
            edge = new HashMutableEdgeLabelledDirectedGraph.DGEdge(node, succ);
            labels = (List)this.edgeToLabels.get(edge);
            logger.debug("     " + succ + " [" + labels + "]");
         }
      }

   }

   private static class DGEdge<N> {
      N from;
      N to;

      public DGEdge(N from, N to) {
         this.from = from;
         this.to = to;
      }

      public N from() {
         return this.from;
      }

      public N to() {
         return this.to;
      }

      public boolean equals(Object o) {
         if (!(o instanceof HashMutableEdgeLabelledDirectedGraph.DGEdge)) {
            return false;
         } else {
            HashMutableEdgeLabelledDirectedGraph.DGEdge<?> other = (HashMutableEdgeLabelledDirectedGraph.DGEdge)o;
            return this.from.equals(other.from) && this.to.equals(other.to);
         }
      }

      public int hashCode() {
         return Arrays.hashCode(new Object[]{this.from, this.to});
      }
   }
}
