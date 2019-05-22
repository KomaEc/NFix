package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class WeightedDirectedSparseGraph {
   private boolean isUnknown;
   private Hashtable<Object, Hashtable<Object, IntContainer>> sources;
   private HashSet vertexes;
   private final HashSet<Object> reachableNodes;
   private final HashSet<WeightedDirectedEdge> reachableEdges;
   private final Hashtable<Object, IntContainer> distance;
   private final Hashtable<Object, Object> pei;

   public WeightedDirectedSparseGraph(HashSet vertexset) {
      this(vertexset, false);
   }

   public WeightedDirectedSparseGraph(HashSet vertexset, boolean isTop) {
      this.sources = new Hashtable();
      this.vertexes = new HashSet();
      this.reachableNodes = new HashSet();
      this.reachableEdges = new HashSet();
      this.distance = new Hashtable();
      this.pei = new Hashtable();
      this.vertexes = vertexset;
      this.isUnknown = !isTop;
   }

   public void setTop() {
      this.isUnknown = false;
      this.sources.clear();
   }

   public HashSet getVertexes() {
      return this.vertexes;
   }

   public void setVertexes(HashSet newset) {
      this.vertexes = newset;
      this.sources.clear();
   }

   public void addEdge(Object from, Object to, int w) {
      if (this.isUnknown) {
         throw new RuntimeException("Unknown graph can not have edges");
      } else {
         Hashtable<Object, IntContainer> targets = (Hashtable)this.sources.get(from);
         if (targets == null) {
            targets = new Hashtable();
            this.sources.put(from, targets);
         }

         IntContainer weight = (IntContainer)targets.get(to);
         if (weight == null) {
            weight = new IntContainer(w);
            targets.put(to, weight);
         } else if (weight.value > w) {
            weight.value = w;
         }

      }
   }

   public void addMutualEdges(Object from, Object to, int weight) {
      this.addEdge(from, to, weight);
      this.addEdge(to, from, -weight);
   }

   public void removeEdge(Object from, Object to) {
      Hashtable targets = (Hashtable)this.sources.get(from);
      if (targets != null) {
         targets.remove(to);
         if (targets.size() == 0) {
            this.sources.remove(from);
         }

      }
   }

   public boolean hasEdge(Object from, Object to) {
      Hashtable targets = (Hashtable)this.sources.get(from);
      return targets == null ? false : targets.containsKey(to);
   }

   public int edgeWeight(Object from, Object to) {
      Hashtable targets = (Hashtable)this.sources.get(from);
      if (targets == null) {
         throw new RuntimeException("No such edge (" + from + " ," + to + ") exists.");
      } else {
         IntContainer weight = (IntContainer)targets.get(to);
         if (weight == null) {
            throw new RuntimeException("No such edge (" + from + ", " + to + ") exists.");
         } else {
            return weight.value;
         }
      }
   }

   public void unionSelf(WeightedDirectedSparseGraph other) {
      if (other != null) {
         WeightedDirectedSparseGraph othergraph = other;
         if (!other.isUnknown) {
            if (this.isUnknown) {
               this.addAll(other);
            }

            List<Object> sourceList = new ArrayList(this.sources.keySet());
            Iterator firstSrcIt = sourceList.iterator();

            while(true) {
               while(firstSrcIt.hasNext()) {
                  Object srcKey = firstSrcIt.next();
                  Hashtable src1 = (Hashtable)this.sources.get(srcKey);
                  Hashtable src2 = (Hashtable)othergraph.sources.get(srcKey);
                  if (src2 == null) {
                     this.sources.remove(srcKey);
                  } else {
                     List targetList = new ArrayList(src1.keySet());
                     Iterator targetIt = targetList.iterator();

                     while(targetIt.hasNext()) {
                        Object target = targetIt.next();
                        IntContainer w1 = (IntContainer)src1.get(target);
                        IntContainer w2 = (IntContainer)src2.get(target);
                        if (w2 == null) {
                           src1.remove(target);
                        } else if (w2.value > w1.value) {
                           w1.value = w2.value;
                        }
                     }

                     if (src1.size() == 0) {
                        this.sources.remove(srcKey);
                     }
                  }
               }

               return;
            }
         }
      }
   }

   public void widenEdges(WeightedDirectedSparseGraph othergraph) {
      if (!othergraph.isUnknown) {
         Hashtable<Object, Hashtable<Object, IntContainer>> othersources = othergraph.sources;
         List<Object> sourceList = new ArrayList(this.sources.keySet());
         Iterator srcIt = sourceList.iterator();

         while(true) {
            while(srcIt.hasNext()) {
               Object src = srcIt.next();
               Hashtable thistargets = (Hashtable)this.sources.get(src);
               Hashtable othertargets = (Hashtable)othersources.get(src);
               if (othertargets == null) {
                  this.sources.remove(src);
               } else {
                  List targetList = new ArrayList(thistargets.keySet());
                  Iterator targetIt = targetList.iterator();

                  while(targetIt.hasNext()) {
                     Object target = targetIt.next();
                     IntContainer thisweight = (IntContainer)thistargets.get(target);
                     IntContainer otherweight = (IntContainer)othertargets.get(target);
                     if (otherweight == null) {
                        thistargets.remove(target);
                     } else if (thisweight.value > otherweight.value) {
                        thistargets.remove(target);
                     }
                  }

                  if (thistargets.size() == 0) {
                     this.sources.remove(src);
                  }
               }
            }

            return;
         }
      }
   }

   public void killNode(Object tokill) {
      if (this.vertexes.contains(tokill)) {
         this.makeShortestPathGraph();
         List<Object> sourceList = new ArrayList(this.sources.keySet());
         Iterator srcIt = sourceList.iterator();

         while(srcIt.hasNext()) {
            Object src = srcIt.next();
            Hashtable targets = (Hashtable)this.sources.get(src);
            targets.remove(tokill);
            if (targets.size() == 0) {
               this.sources.remove(src);
            }
         }

         this.sources.remove(tokill);
         this.makeShortestPathGraph();
      }
   }

   public void updateWeight(Object which, int c) {
      Iterator srcIt = this.sources.keySet().iterator();

      while(srcIt.hasNext()) {
         Object from = srcIt.next();
         Hashtable targets = (Hashtable)this.sources.get(from);
         IntContainer weight = (IntContainer)targets.get(which);
         if (weight != null) {
            weight.value += c;
         }
      }

      Hashtable toset = (Hashtable)this.sources.get(which);
      if (toset != null) {
         IntContainer weight;
         for(Iterator toIt = toset.keySet().iterator(); toIt.hasNext(); weight.value -= c) {
            Object to = toIt.next();
            weight = (IntContainer)toset.get(to);
         }

      }
   }

   public void clear() {
      this.sources.clear();
   }

   public void replaceAllEdges(WeightedDirectedSparseGraph other) {
      this.isUnknown = other.isUnknown;
      this.vertexes = other.vertexes;
      this.sources = other.sources;
   }

   public void addBoundedAll(WeightedDirectedSparseGraph another) {
      this.isUnknown = another.isUnknown;
      Hashtable<Object, Hashtable<Object, IntContainer>> othersources = another.sources;
      Iterator thisnodeIt = this.vertexes.iterator();

      while(true) {
         Object src;
         Hashtable othertargets;
         do {
            if (!thisnodeIt.hasNext()) {
               return;
            }

            src = thisnodeIt.next();
            othertargets = (Hashtable)othersources.get(src);
         } while(othertargets == null);

         Hashtable<Object, IntContainer> thistargets = new Hashtable();
         Iterator othertargetIt = othertargets.keySet().iterator();

         while(othertargetIt.hasNext()) {
            Object key = othertargetIt.next();
            if (this.vertexes.contains(key)) {
               IntContainer weight = (IntContainer)othertargets.get(key);
               thistargets.put(key, weight.dup());
            }
         }

         if (thistargets.size() > 0) {
            this.sources.put(src, thistargets);
         }
      }
   }

   public void addAll(WeightedDirectedSparseGraph othergraph) {
      this.isUnknown = othergraph.isUnknown;
      this.clear();
      Hashtable<Object, Hashtable<Object, IntContainer>> othersources = othergraph.sources;
      Iterator othersrcIt = othersources.keySet().iterator();

      while(othersrcIt.hasNext()) {
         Object src = othersrcIt.next();
         Hashtable othertargets = (Hashtable)othersources.get(src);
         Hashtable<Object, IntContainer> thistargets = new Hashtable(othersources.size());
         this.sources.put(src, thistargets);
         Iterator targetIt = othertargets.keySet().iterator();

         while(targetIt.hasNext()) {
            Object target = targetIt.next();
            IntContainer otherweight = (IntContainer)othertargets.get(target);
            thistargets.put(target, otherweight.dup());
         }
      }

   }

   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else if (!(other instanceof WeightedDirectedSparseGraph)) {
         return false;
      } else {
         WeightedDirectedSparseGraph othergraph = (WeightedDirectedSparseGraph)other;
         if (this.isUnknown != othergraph.isUnknown) {
            return false;
         } else if (this.isUnknown) {
            return true;
         } else {
            Hashtable<Object, Hashtable<Object, IntContainer>> othersources = othergraph.sources;
            if (this.sources.size() != othersources.size()) {
               return false;
            } else {
               Iterator sourceIt = this.sources.keySet().iterator();

               while(sourceIt.hasNext()) {
                  Object src = sourceIt.next();
                  Hashtable thistarget = (Hashtable)this.sources.get(src);
                  Hashtable othertarget = (Hashtable)othersources.get(src);
                  if (othertarget == null) {
                     return false;
                  }

                  if (thistarget.size() != othertarget.size()) {
                     return false;
                  }

                  Iterator targetIt = thistarget.keySet().iterator();

                  while(targetIt.hasNext()) {
                     Object target = targetIt.next();
                     IntContainer thisweight = (IntContainer)thistarget.get(target);
                     IntContainer otherweight = (IntContainer)othertarget.get(target);
                     if (otherweight == null) {
                        return false;
                     }

                     if (thisweight.value != otherweight.value) {
                        return false;
                     }
                  }
               }

               return true;
            }
         }
      }
   }

   public String toString() {
      String graphstring = "WeightedDirectedSparseGraph:\n";
      graphstring = graphstring + this.vertexes + "\n";

      for(Iterator srcIt = this.sources.keySet().iterator(); srcIt.hasNext(); graphstring = graphstring + "\n") {
         Object src = srcIt.next();
         graphstring = graphstring + src + " : ";
         Hashtable targets = (Hashtable)this.sources.get(src);

         Object target;
         IntContainer weight;
         for(Iterator targetIt = targets.keySet().iterator(); targetIt.hasNext(); graphstring = graphstring + target + "(" + weight.value + ")  ") {
            target = targetIt.next();
            weight = (IntContainer)targets.get(target);
         }
      }

      return graphstring;
   }

   public WeightedDirectedSparseGraph dup() {
      WeightedDirectedSparseGraph newone = new WeightedDirectedSparseGraph(this.vertexes);
      newone.addAll(this);
      return newone;
   }

   public boolean makeShortestPathGraph() {
      boolean nonegcycle = true;
      List<Object> srcList = new ArrayList(this.sources.keySet());
      Iterator srcIt = srcList.iterator();

      while(srcIt.hasNext()) {
         Object src = srcIt.next();
         if (!this.SSSPFinder(src)) {
            nonegcycle = false;
         }
      }

      return nonegcycle;
   }

   private boolean SSSPFinder(Object src) {
      Hashtable<Object, IntContainer> outedges = (Hashtable)this.sources.get(src);
      if (outedges == null) {
         return true;
      } else if (outedges.size() == 0) {
         return true;
      } else {
         this.InitializeSingleSource(src);
         this.getReachableNodesAndEdges(src);
         int vSize = this.reachableNodes.size();

         for(int i = 0; i < vSize; ++i) {
            Iterator edgeIt = this.reachableEdges.iterator();

            while(edgeIt.hasNext()) {
               WeightedDirectedEdge edge = (WeightedDirectedEdge)edgeIt.next();
               this.Relax(edge.from, edge.to, edge.weight);
            }
         }

         this.distance.remove(src);
         Iterator targetIt = this.reachableEdges.iterator();

         IntContainer dfrom;
         while(targetIt.hasNext()) {
            WeightedDirectedEdge edge = (WeightedDirectedEdge)targetIt.next();
            dfrom = (IntContainer)this.distance.get(edge.from);
            if (dfrom != null) {
               IntContainer dto = (IntContainer)this.distance.get(edge.to);
               if (dto != null && dto.value > dfrom.value + edge.weight) {
                  return false;
               }
            }
         }

         outedges.clear();
         targetIt = this.distance.keySet().iterator();

         while(targetIt.hasNext()) {
            Object to = targetIt.next();
            dfrom = (IntContainer)this.distance.get(to);
            outedges.put(to, dfrom.dup());
         }

         return true;
      }
   }

   private void InitializeSingleSource(Object src) {
      this.reachableNodes.clear();
      this.reachableEdges.clear();
      this.pei.clear();
      this.distance.clear();
      this.distance.put(src, new IntContainer(0));
   }

   private void getReachableNodesAndEdges(Object src) {
      LinkedList<Object> worklist = new LinkedList();
      this.reachableNodes.add(src);
      worklist.add(src);

      while(true) {
         Object from;
         Hashtable targets;
         do {
            if (worklist.isEmpty()) {
               return;
            }

            from = worklist.removeFirst();
            targets = (Hashtable)this.sources.get(from);
         } while(targets == null);

         Iterator targetIt = targets.keySet().iterator();

         while(targetIt.hasNext()) {
            Object target = targetIt.next();
            if (!this.reachableNodes.contains(target)) {
               worklist.add(target);
               this.reachableNodes.add(target);
            }

            IntContainer weight = (IntContainer)targets.get(target);
            this.reachableEdges.add(new WeightedDirectedEdge(from, target, weight.value));
         }
      }
   }

   private void Relax(Object from, Object to, int weight) {
      IntContainer dfrom = (IntContainer)this.distance.get(from);
      IntContainer dto = (IntContainer)this.distance.get(to);
      if (dfrom != null) {
         int vfrom = dfrom.value;
         int vnew = vfrom + weight;
         if (dto == null) {
            this.distance.put(to, new IntContainer(vnew));
            this.pei.put(to, from);
         } else {
            int vto = dto.value;
            if (vto > vnew) {
               this.distance.put(to, new IntContainer(vnew));
               this.pei.put(to, from);
            }
         }
      }

   }
}
