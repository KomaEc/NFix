package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.RefLikeType;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;

public class PurityGraph {
   private static final Logger logger = LoggerFactory.getLogger(PurityGraph.class);
   public static final boolean doCheck = false;
   protected Set nodes;
   protected Set paramNodes;
   protected MultiMap edges;
   protected MultiMap locals;
   protected Set ret;
   protected Set globEscape;
   protected MultiMap backEdges;
   protected MultiMap backLocals;
   protected MultiMap mutated;
   private static final Map<PurityNode, PurityNode> nodeCache = new HashMap();
   private static final Map<PurityEdge, PurityEdge> edgeCache = new HashMap();
   static final int PARAM_RW = 0;
   static final int PARAM_RO = 1;
   static final int PARAM_SAFE = 2;
   private static int maxInsideNodes = 0;
   private static int maxLoadNodes = 0;
   private static int maxInsideEdges = 0;
   private static int maxOutsideEdges = 0;
   private static int maxMutated = 0;

   PurityGraph() {
      this.nodes = new HashSet();
      this.paramNodes = new HashSet();
      this.edges = new HashMultiMap();
      this.locals = new HashMultiMap();
      this.ret = new HashSet();
      this.globEscape = new HashSet();
      this.backEdges = new HashMultiMap();
      this.backLocals = new HashMultiMap();
      this.mutated = new HashMultiMap();
   }

   PurityGraph(PurityGraph x) {
      this.nodes = new HashSet(x.nodes);
      this.paramNodes = new HashSet(x.paramNodes);
      this.edges = new HashMultiMap(x.edges);
      this.locals = new HashMultiMap(x.locals);
      this.ret = new HashSet(x.ret);
      this.globEscape = new HashSet(x.globEscape);
      this.backEdges = new HashMultiMap(x.backEdges);
      this.backLocals = new HashMultiMap(x.backLocals);
      this.mutated = new HashMultiMap(x.mutated);
   }

   public int hashCode() {
      return this.nodes.hashCode() + this.edges.hashCode() + this.locals.hashCode() + this.ret.hashCode() + this.globEscape.hashCode() + this.mutated.hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof PurityGraph)) {
         return false;
      } else {
         PurityGraph g = (PurityGraph)o;
         return this.nodes.equals(g.nodes) && this.edges.equals(g.edges) && this.locals.equals(g.locals) && this.ret.equals(g.ret) && this.globEscape.equals(g.globEscape) && this.mutated.equals(g.mutated);
      }
   }

   private static PurityNode cacheNode(PurityNode p) {
      if (!nodeCache.containsKey(p)) {
         nodeCache.put(p, p);
      }

      return (PurityNode)nodeCache.get(p);
   }

   private static PurityEdge cacheEdge(PurityEdge e) {
      if (!edgeCache.containsKey(e)) {
         edgeCache.put(e, e);
      }

      return (PurityEdge)edgeCache.get(e);
   }

   public static PurityGraph conservativeGraph(SootMethod m, boolean withEffect) {
      PurityGraph g = new PurityGraph();
      PurityNode glob = PurityGlobalNode.node;
      g.nodes.add(glob);
      Iterator it = m.getParameterTypes().iterator();

      for(int i = 0; it.hasNext(); ++i) {
         if (it.next() instanceof RefLikeType) {
            PurityNode n = cacheNode(new PurityParamNode(i));
            g.globEscape.add(n);
            g.nodes.add(n);
            g.paramNodes.add(n);
         }
      }

      if (m.getReturnType() instanceof RefLikeType) {
         g.ret.add(glob);
      }

      if (withEffect) {
         g.mutated.put(glob, "outside-world");
      }

      return g;
   }

   public static PurityGraph freshGraph(SootMethod m) {
      PurityGraph g = new PurityGraph();
      if (m.getReturnType() instanceof RefLikeType) {
         PurityNode n = cacheNode(new PurityMethodNode(m));
         g.ret.add(n);
         g.nodes.add(n);
      }

      return g;
   }

   void union(PurityGraph arg) {
      this.nodes.addAll(arg.nodes);
      this.paramNodes.addAll(arg.paramNodes);
      this.edges.putAll(arg.edges);
      this.locals.putAll(arg.locals);
      this.ret.addAll(arg.ret);
      this.globEscape.addAll(arg.globEscape);
      this.backEdges.putAll(arg.backEdges);
      this.backLocals.putAll(arg.backLocals);
      this.mutated.putAll(arg.mutated);
   }

   protected void sanityCheck() {
      boolean err = false;
      Iterator it = this.edges.keySet().iterator();

      PurityNode n;
      Iterator itt;
      PurityEdge e;
      while(it.hasNext()) {
         n = (PurityNode)it.next();
         itt = this.edges.get(n).iterator();

         while(itt.hasNext()) {
            e = (PurityEdge)itt.next();
            if (!n.equals(e.getSource())) {
               logger.debug("invalid edge source " + e + ", should be " + n);
               err = true;
            }

            if (!this.nodes.contains(e.getSource())) {
               logger.debug("nodes does not contain edge source " + e);
               err = true;
            }

            if (!this.nodes.contains(e.getTarget())) {
               logger.debug("nodes does not contain edge target " + e);
               err = true;
            }

            if (!this.backEdges.get(e.getTarget()).contains(e)) {
               logger.debug("backEdges does not contain edge " + e);
               err = true;
            }

            if (!e.isInside() && !e.getTarget().isLoad()) {
               logger.debug("target of outside edge is not a load node " + e);
               err = true;
            }
         }
      }

      it = this.backEdges.keySet().iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         itt = this.backEdges.get(n).iterator();

         while(itt.hasNext()) {
            e = (PurityEdge)itt.next();
            if (!n.equals(e.getTarget())) {
               logger.debug("invalid backEdge dest " + e + ", should be " + n);
               err = true;
            }

            if (!this.edges.get(e.getSource()).contains(e)) {
               logger.debug("backEdge not in edges " + e);
               err = true;
            }
         }
      }

      it = this.nodes.iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (n.isParam() && !this.paramNodes.contains(n)) {
            logger.debug("paramNode not in paramNodes " + n);
            err = true;
         }
      }

      it = this.paramNodes.iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (!n.isParam()) {
            logger.debug("paramNode contains a non-param node " + n);
            err = true;
         }

         if (!this.nodes.contains(n)) {
            logger.debug("paramNode not in nodes " + n);
            err = true;
         }
      }

      it = this.globEscape.iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (!this.nodes.contains(n)) {
            logger.debug("globEscape not in nodes " + n);
            err = true;
         }
      }

      it = this.locals.keySet().iterator();

      while(it.hasNext()) {
         Local l = (Local)it.next();
         itt = this.locals.get(l).iterator();

         while(itt.hasNext()) {
            PurityNode n = (PurityNode)itt.next();
            if (!this.nodes.contains(n)) {
               logger.debug("target of local node in nodes " + l + " / " + n);
               err = true;
            }

            if (!this.backLocals.get(n).contains(l)) {
               logger.debug("backLocals does contain local " + l + " / " + n);
               err = true;
            }
         }
      }

      it = this.backLocals.keySet().iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         itt = this.backLocals.get(n).iterator();

         while(itt.hasNext()) {
            Local l = (Local)itt.next();
            if (!this.nodes.contains(n)) {
               logger.debug("backLocal node not in in nodes " + l + " / " + n);
               err = true;
            }

            if (!this.locals.get(l).contains(n)) {
               logger.debug("locals does contain backLocal " + l + " / " + n);
               err = true;
            }
         }
      }

      it = this.ret.iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (!this.nodes.contains(n)) {
            logger.debug("target of ret not in nodes " + n);
            err = true;
         }
      }

      it = this.mutated.keySet().iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (!this.nodes.contains(n)) {
            logger.debug("mutated node not in nodes " + n);
            err = true;
         }
      }

      if (err) {
         this.dump();
         DotGraph dot = new DotGraph("sanityCheckFailure");
         this.fillDotGraph("chk", dot);
         dot.plot("sanityCheckFailure.dot");
         throw new Error("PurityGraph sanity check failed!!!");
      }
   }

   protected void internalPassEdges(Set toColor, Set<PurityNode> dest, boolean consider_inside) {
      Iterator it = toColor.iterator();

      while(true) {
         PurityEdge edge;
         do {
            if (!it.hasNext()) {
               return;
            }

            edge = (PurityEdge)it.next();
         } while(!consider_inside && edge.isInside());

         PurityNode node = edge.getTarget();
         if (!dest.contains(node)) {
            dest.add(node);
            this.internalPassEdges(this.edges.get(node), dest, consider_inside);
         }
      }
   }

   protected void internalPassNode(PurityNode node, Set<PurityNode> dest, boolean consider_inside) {
      if (!dest.contains(node)) {
         dest.add(node);
         this.internalPassEdges(this.edges.get(node), dest, consider_inside);
      }

   }

   protected void internalPassNodes(Set toColor, Set<PurityNode> dest, boolean consider_inside) {
      Iterator it = toColor.iterator();

      while(it.hasNext()) {
         this.internalPassNode((PurityNode)it.next(), dest, consider_inside);
      }

   }

   protected Set<PurityNode> getEscaping() {
      Set<PurityNode> escaping = new HashSet();
      this.internalPassNodes(this.ret, escaping, true);
      this.internalPassNodes(this.globEscape, escaping, true);
      this.internalPassNode(PurityGlobalNode.node, escaping, true);
      this.internalPassNodes(this.paramNodes, escaping, true);
      return escaping;
   }

   public boolean isPure() {
      if (!this.mutated.get(PurityGlobalNode.node).isEmpty()) {
         return false;
      } else {
         Set<PurityNode> A = new HashSet();
         Set<PurityNode> B = new HashSet();
         this.internalPassNodes(this.paramNodes, A, false);
         this.internalPassNodes(this.globEscape, B, true);
         this.internalPassNode(PurityGlobalNode.node, B, true);
         Iterator it = A.iterator();

         PurityNode n;
         do {
            if (!it.hasNext()) {
               return true;
            }

            n = (PurityNode)it.next();
         } while(!B.contains(n) && this.mutated.get(n).isEmpty());

         return false;
      }
   }

   public boolean isPureConstructor() {
      if (!this.mutated.get(PurityGlobalNode.node).isEmpty()) {
         return false;
      } else {
         Set<PurityNode> A = new HashSet();
         Set<PurityNode> B = new HashSet();
         this.internalPassNodes(this.paramNodes, A, false);
         this.internalPassNodes(this.globEscape, B, true);
         this.internalPassNode(PurityGlobalNode.node, B, true);
         PurityNode th = PurityThisNode.node;
         Iterator it = A.iterator();

         PurityNode n;
         do {
            if (!it.hasNext()) {
               return true;
            }

            n = (PurityNode)it.next();
         } while(!B.contains(n) && (n.equals(th) || this.mutated.get(n).isEmpty()));

         return false;
      }
   }

   protected int internalParamStatus(PurityNode p) {
      if (!this.paramNodes.contains(p)) {
         return 0;
      } else {
         Set<PurityNode> S1 = new HashSet();
         this.internalPassNode(p, S1, false);
         Iterator it = S1.iterator();

         PurityNode n;
         do {
            do {
               if (!it.hasNext()) {
                  Set<PurityNode> S2 = new HashSet();
                  this.internalPassNodes(this.ret, S2, true);
                  this.internalPassNodes(this.paramNodes, S2, true);
                  it = S2.iterator();

                  while(it.hasNext()) {
                     Iterator itt = this.edges.get(it.next()).iterator();

                     while(itt.hasNext()) {
                        PurityEdge e = (PurityEdge)itt.next();
                        if (e.isInside() && S1.contains(e.getTarget())) {
                           return 1;
                        }
                     }
                  }

                  return 2;
               }

               n = (PurityNode)it.next();
            } while(!n.isLoad() && !n.equals(p));
         } while(this.mutated.get(n).isEmpty() && !this.globEscape.contains(n));

         return 0;
      }
   }

   public int paramStatus(int param) {
      return this.internalParamStatus(cacheNode(new PurityParamNode(param)));
   }

   public int thisStatus() {
      return this.internalParamStatus(PurityThisNode.node);
   }

   public Object clone() {
      return new PurityGraph(this);
   }

   protected final boolean localsRemove(Local local) {
      Iterator it = this.locals.get(local).iterator();

      while(it.hasNext()) {
         Object node = it.next();
         this.backLocals.remove(node, local);
      }

      return this.locals.remove(local);
   }

   protected final boolean localsPut(Local local, PurityNode node) {
      this.backLocals.put(node, local);
      return this.locals.put(local, node);
   }

   protected final boolean localsPutAll(Local local, Set nodes) {
      Iterator it = nodes.iterator();

      while(it.hasNext()) {
         Object node = it.next();
         this.backLocals.put(node, local);
      }

      return this.locals.putAll(local, nodes);
   }

   protected final void removeNode(PurityNode n) {
      Iterator it = this.edges.get(n).iterator();

      PurityEdge e;
      while(it.hasNext()) {
         e = (PurityEdge)it.next();
         this.backEdges.remove(e.getTarget(), e);
      }

      it = this.backEdges.get(n).iterator();

      while(it.hasNext()) {
         e = (PurityEdge)it.next();
         this.edges.remove(e.getSource(), e);
      }

      it = this.backLocals.get(n).iterator();

      while(it.hasNext()) {
         Local l = (Local)it.next();
         this.locals.remove(l, n);
      }

      this.ret.remove(n);
      this.edges.remove(n);
      this.backEdges.remove(n);
      this.backLocals.remove(n);
      this.nodes.remove(n);
      this.paramNodes.remove(n);
      this.globEscape.remove(n);
      this.mutated.remove(n);
   }

   protected final void mergeNodes(PurityNode src, PurityNode dst) {
      Iterator it = (new LinkedList(this.edges.get(src))).iterator();

      PurityEdge e;
      PurityNode n;
      PurityEdge ee;
      while(it.hasNext()) {
         e = (PurityEdge)it.next();
         n = e.getTarget();
         if (n.equals(src)) {
            n = dst;
         }

         ee = cacheEdge(new PurityEdge(dst, e.getField(), n, e.isInside()));
         this.edges.remove(src, e);
         this.edges.put(dst, ee);
         this.backEdges.remove(n, e);
         this.backEdges.put(n, ee);
      }

      it = (new LinkedList(this.backEdges.get(src))).iterator();

      while(it.hasNext()) {
         e = (PurityEdge)it.next();
         n = e.getSource();
         if (n.equals(src)) {
            n = dst;
         }

         ee = cacheEdge(new PurityEdge(n, e.getField(), dst, e.isInside()));
         this.edges.remove(n, e);
         this.edges.put(n, ee);
         this.backEdges.remove(src, e);
         this.backEdges.put(dst, ee);
      }

      it = (new LinkedList(this.backLocals.get(src))).iterator();

      while(it.hasNext()) {
         Local l = (Local)it.next();
         this.locals.remove(l, src);
         this.backLocals.remove(src, l);
         this.locals.put(l, dst);
         this.backLocals.put(dst, l);
      }

      Set m = this.mutated.get(src);
      this.mutated.remove(src);
      this.mutated.putAll(dst, m);
      if (this.ret.contains(src)) {
         this.ret.remove(src);
         this.ret.add(dst);
      }

      if (this.globEscape.contains(src)) {
         this.globEscape.remove(src);
         this.globEscape.add(dst);
      }

      this.nodes.remove(src);
      this.nodes.add(dst);
      this.paramNodes.remove(src);
      if (dst.isParam()) {
         this.paramNodes.add(dst);
      }

   }

   void simplifyLoad() {
      Iterator it = (new LinkedList(this.nodes)).iterator();

      label38:
      while(it.hasNext()) {
         PurityNode p = (PurityNode)it.next();
         Map<String, PurityNode> fmap = new HashMap();
         Iterator itt = (new LinkedList(this.edges.get(p))).iterator();

         while(true) {
            while(true) {
               PurityEdge e;
               PurityNode tgt;
               do {
                  do {
                     if (!itt.hasNext()) {
                        continue label38;
                     }

                     e = (PurityEdge)itt.next();
                     tgt = e.getTarget();
                  } while(e.isInside());
               } while(tgt.equals(p));

               String f = e.getField();
               if (fmap.containsKey(f) && this.nodes.contains(fmap.get(f))) {
                  this.mergeNodes(tgt, (PurityNode)fmap.get(f));
               } else {
                  fmap.put(f, tgt);
               }
            }
         }
      }

   }

   void simplifyInside() {
      Set<PurityNode> r = new HashSet();
      this.internalPassNodes(this.paramNodes, r, true);
      this.internalPassNodes(this.ret, r, true);
      this.internalPassNodes(this.globEscape, r, true);
      this.internalPassNode(PurityGlobalNode.node, r, true);
      Iterator it = this.nodes.iterator();

      PurityNode n;
      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (n.isLoad()) {
            this.internalPassNode(n, r, true);
         }
      }

      it = (new LinkedList(this.nodes)).iterator();

      while(it.hasNext()) {
         n = (PurityNode)it.next();
         if (n.isInside() && !r.contains(n)) {
            this.removeNode(n);
         }
      }

   }

   void removeLocals() {
      this.locals = new HashMultiMap();
      this.backLocals = new HashMultiMap();
   }

   void assignParamToLocal(int right, Local left) {
      PurityNode node = cacheNode(new PurityParamNode(right));
      this.localsRemove(left);
      this.localsPut(left, node);
      this.nodes.add(node);
      this.paramNodes.add(node);
   }

   void assignThisToLocal(Local left) {
      PurityNode node = PurityThisNode.node;
      this.localsRemove(left);
      this.localsPut(left, node);
      this.nodes.add(node);
      this.paramNodes.add(node);
   }

   void assignLocalToLocal(Local right, Local left) {
      this.localsRemove(left);
      this.localsPutAll(left, this.locals.get(right));
   }

   void returnLocal(Local right) {
      this.ret.clear();
      this.ret.addAll(this.locals.get(right));
   }

   void assignFieldToLocal(Stmt stmt, Local right, String field, Local left) {
      Set<PurityNode> esc = new HashSet();
      Set<PurityNode> escaping = this.getEscaping();
      this.localsRemove(left);
      Iterator itRight = this.locals.get(right).iterator();

      PurityNode loadNode;
      Iterator itEsc;
      while(itRight.hasNext()) {
         loadNode = (PurityNode)itRight.next();
         itEsc = this.edges.get(loadNode).iterator();

         while(itEsc.hasNext()) {
            PurityEdge edge = (PurityEdge)itEsc.next();
            if (edge.isInside() && edge.getField().equals(field)) {
               this.localsPut(left, edge.getTarget());
            }
         }

         if (escaping.contains(loadNode)) {
            esc.add(loadNode);
         }
      }

      if (!esc.isEmpty()) {
         loadNode = cacheNode(new PurityStmtNode(stmt, false));
         this.nodes.add(loadNode);
         itEsc = esc.iterator();

         while(itEsc.hasNext()) {
            PurityNode node = (PurityNode)itEsc.next();
            PurityEdge edge = cacheEdge(new PurityEdge(node, field, loadNode, false));
            if (this.edges.put(node, edge)) {
               this.backEdges.put(loadNode, edge);
            }
         }

         this.localsPut(left, loadNode);
      }

   }

   void assignLocalToField(Local right, Local left, String field) {
      Iterator itLeft = this.locals.get(left).iterator();

      while(itLeft.hasNext()) {
         PurityNode nodeLeft = (PurityNode)itLeft.next();
         Iterator itRight = this.locals.get(right).iterator();

         while(itRight.hasNext()) {
            PurityNode nodeRight = (PurityNode)itRight.next();
            PurityEdge edge = cacheEdge(new PurityEdge(nodeLeft, field, nodeRight, true));
            if (this.edges.put(nodeLeft, edge)) {
               this.backEdges.put(nodeRight, edge);
            }
         }

         if (!nodeLeft.isInside()) {
            this.mutated.put(nodeLeft, field);
         }
      }

   }

   void assignNewToLocal(Stmt stmt, Local left) {
      PurityNode node = cacheNode(new PurityStmtNode(stmt, true));
      this.localsRemove(left);
      this.localsPut(left, node);
      this.nodes.add(node);
   }

   void localEscapes(Local l) {
      this.globEscape.addAll(this.locals.get(l));
   }

   void localIsUnknown(Local l) {
      PurityNode node = PurityGlobalNode.node;
      this.localsRemove(l);
      this.localsPut(l, node);
      this.nodes.add(node);
   }

   void assignLocalToStaticField(Local right, String field) {
      PurityNode node = PurityGlobalNode.node;
      this.localEscapes(right);
      this.mutated.put(node, field);
      this.nodes.add(node);
   }

   void mutateField(Local left, String field) {
      Iterator it = this.locals.get(left).iterator();

      while(it.hasNext()) {
         PurityNode n = (PurityNode)it.next();
         if (!n.isInside()) {
            this.mutated.put(n, field);
         }
      }

   }

   void mutateStaticField(String field) {
      PurityNode node = PurityGlobalNode.node;
      this.mutated.put(node, field);
      this.nodes.add(node);
   }

   void methodCall(PurityGraph g, Local right, List args, Local left) {
      MultiMap mu = new HashMultiMap();
      Iterator it = args.iterator();

      for(int nb = 0; it.hasNext(); ++nb) {
         Value arg = (Value)it.next();
         if (arg instanceof Local && ((Local)arg).getType() instanceof RefLikeType) {
            mu.putAll(cacheNode(new PurityParamNode(nb)), this.locals.get(arg));
         }
      }

      if (right != null) {
         mu.putAll(PurityThisNode.node, this.locals.get(right));
      }

      boolean hasChanged = true;

      PurityNode n1;
      Iterator it3;
      Iterator it34;
      Iterator it12;
      while(hasChanged) {
         hasChanged = false;
         it = (new LinkedList(mu.keySet())).iterator();

         PurityNode n3;
         while(it.hasNext()) {
            n1 = (PurityNode)it.next();
            it3 = (new LinkedList(mu.get(n1))).iterator();

            label237:
            while(it3.hasNext()) {
               n3 = (PurityNode)it3.next();
               Iterator it12 = g.edges.get(n1).iterator();

               while(true) {
                  PurityEdge e12;
                  do {
                     if (!it12.hasNext()) {
                        continue label237;
                     }

                     e12 = (PurityEdge)it12.next();
                  } while(e12.isInside());

                  it34 = this.edges.get(n3).iterator();

                  while(it34.hasNext()) {
                     PurityEdge e34 = (PurityEdge)it34.next();
                     if (e34.isInside() && e12.getField().equals(e34.getField()) && mu.put(e12.getTarget(), e34.getTarget())) {
                        hasChanged = true;
                     }
                  }
               }
            }
         }

         it = g.edges.keySet().iterator();

         label293:
         while(it.hasNext()) {
            n1 = (PurityNode)it.next();
            it3 = g.edges.keySet().iterator();

            label291:
            while(true) {
               boolean cond;
               do {
                  do {
                     if (!it3.hasNext()) {
                        continue label293;
                     }

                     n3 = (PurityNode)it3.next();
                     Set mu1 = new HashSet(mu.get(n1));
                     Set mu3 = new HashSet(mu.get(n3));
                     cond = n1.equals(n3) || mu1.contains(n3) || mu3.contains(n1);

                     for(Iterator itt = mu1.iterator(); !cond && itt.hasNext(); cond = cond || mu3.contains(itt.next())) {
                     }
                  } while(!cond);
               } while(n1.equals(n3) && !n1.isLoad());

               it12 = g.edges.get(n1).iterator();

               while(true) {
                  PurityEdge e12;
                  do {
                     if (!it12.hasNext()) {
                        continue label291;
                     }

                     e12 = (PurityEdge)it12.next();
                  } while(e12.isInside());

                  Iterator it34 = g.edges.get(n3).iterator();

                  while(it34.hasNext()) {
                     PurityEdge e34 = (PurityEdge)it34.next();
                     if (e34.isInside() && e12.getField().equals(e34.getField())) {
                        PurityNode n2 = e12.getTarget();
                        PurityNode n4 = e34.getTarget();
                        if (!n4.isParam() && mu.put(n2, n4)) {
                           hasChanged = true;
                        }

                        if (mu.putAll(n2, mu.get(n4))) {
                           hasChanged = true;
                        }
                     }
                  }
               }
            }
         }
      }

      it = g.nodes.iterator();

      while(it.hasNext()) {
         n1 = (PurityNode)it.next();
         if (!n1.isParam()) {
            mu.put(n1, n1);
            this.nodes.add(n1);
         }
      }

      it = g.edges.keySet().iterator();

      while(it.hasNext()) {
         n1 = (PurityNode)it.next();
         it3 = g.edges.get(n1).iterator();

         label205:
         while(it3.hasNext()) {
            PurityEdge e12 = (PurityEdge)it3.next();
            String f = e12.getField();
            PurityNode n2 = e12.getTarget();
            it34 = mu.get(n1).iterator();

            while(true) {
               while(true) {
                  if (!it34.hasNext()) {
                     continue label205;
                  }

                  PurityNode mu1 = (PurityNode)it34.next();
                  if (e12.isInside()) {
                     it12 = mu.get(n2).iterator();

                     while(it12.hasNext()) {
                        PurityNode mu2 = (PurityNode)it12.next();
                        PurityEdge edge = cacheEdge(new PurityEdge(mu1, f, mu2, true));
                        this.edges.put(mu1, edge);
                        this.backEdges.put(mu2, edge);
                     }
                  } else {
                     PurityEdge edge = cacheEdge(new PurityEdge(mu1, f, n2, false));
                     this.edges.put(mu1, edge);
                     this.backEdges.put(n2, edge);
                  }
               }
            }
         }
      }

      if (left != null) {
         this.localsRemove(left);
         it = g.ret.iterator();

         while(it.hasNext()) {
            this.localsPutAll(left, mu.get(it.next()));
         }
      }

      it = g.globEscape.iterator();

      while(it.hasNext()) {
         this.globEscape.addAll(mu.get(it.next()));
      }

      Set<PurityNode> escaping = this.getEscaping();
      it = (new LinkedList(this.nodes)).iterator();

      while(true) {
         while(true) {
            PurityNode n;
            Iterator itt;
            do {
               if (!it.hasNext()) {
                  it = g.mutated.keySet().iterator();

                  label153:
                  while(it.hasNext()) {
                     n = (PurityNode)it.next();
                     itt = mu.get(n).iterator();

                     while(true) {
                        PurityNode nn;
                        do {
                           do {
                              if (!itt.hasNext()) {
                                 continue label153;
                              }

                              nn = (PurityNode)itt.next();
                           } while(!this.nodes.contains(nn));
                        } while(nn.isInside());

                        Iterator ittt = g.mutated.get(n).iterator();

                        while(ittt.hasNext()) {
                           String f = (String)ittt.next();
                           this.mutated.put(nn, f);
                        }
                     }
                  }

                  return;
               }

               n = (PurityNode)it.next();
            } while(escaping.contains(n));

            if (n.isLoad()) {
               this.removeNode(n);
            } else {
               itt = (new LinkedList(this.edges.get(n))).iterator();

               while(itt.hasNext()) {
                  PurityEdge e = (PurityEdge)itt.next();
                  if (!e.isInside()) {
                     this.edges.remove(n, e);
                     this.backEdges.remove(e.getTarget(), e);
                  }
               }
            }
         }
      }
   }

   void fillDotGraph(String prefix, DotGraph out) {
      Map<PurityNode, String> nodeId = new HashMap();
      int id = 0;

      Iterator it;
      PurityNode n;
      String label;
      DotGraphNode node;
      for(it = this.nodes.iterator(); it.hasNext(); ++id) {
         n = (PurityNode)it.next();
         label = "N" + prefix + "_" + id;
         node = out.drawNode(label);
         node.setLabel(n.toString());
         if (!n.isInside()) {
            node.setStyle("dashed");
            node.setAttribute("color", "gray50");
         }

         if (this.globEscape.contains(n)) {
            node.setAttribute("fontcolor", "red");
         }

         nodeId.put(n, label);
      }

      it = this.edges.keySet().iterator();

      Iterator itt;
      while(it.hasNext()) {
         n = (PurityNode)it.next();
         itt = this.edges.get(n).iterator();

         while(itt.hasNext()) {
            PurityEdge e = (PurityEdge)itt.next();
            DotGraphEdge edge = out.drawEdge((String)nodeId.get(e.getSource()), (String)nodeId.get(e.getTarget()));
            edge.setLabel(e.getField());
            if (!e.isInside()) {
               edge.setStyle("dashed");
               edge.setAttribute("color", "gray50");
               edge.setAttribute("fontcolor", "gray40");
            }
         }
      }

      it = this.locals.keySet().iterator();

      while(true) {
         Local local;
         do {
            if (!it.hasNext()) {
               if (!this.ret.isEmpty()) {
                  DotGraphNode node = out.drawNode("ret_" + prefix);
                  node.setLabel("ret");
                  node.setShape("plaintext");
                  itt = this.ret.iterator();

                  while(itt.hasNext()) {
                     PurityNode dst = (PurityNode)itt.next();
                     out.drawEdge("ret_" + prefix, (String)nodeId.get(dst));
                  }
               }

               it = this.mutated.keySet().iterator();

               while(it.hasNext()) {
                  n = (PurityNode)it.next();

                  for(itt = this.mutated.get(n).iterator(); itt.hasNext(); ++id) {
                     String f = (String)itt.next();
                     String label = "M" + prefix + "_" + id;
                     DotGraphNode node = out.drawNode(label);
                     node.setLabel("");
                     node.setShape("plaintext");
                     DotGraphEdge edge = out.drawEdge((String)nodeId.get(n), label);
                     edge.setLabel(f);
                  }
               }

               return;
            }

            local = (Local)it.next();
         } while(this.locals.get(local).isEmpty());

         label = "L" + prefix + "_" + id;
         node = out.drawNode(label);
         node.setLabel(local.toString());
         node.setShape("plaintext");
         Iterator itt = this.locals.get(local).iterator();

         while(itt.hasNext()) {
            PurityNode dst = (PurityNode)itt.next();
            out.drawEdge(label, (String)nodeId.get(dst));
         }

         ++id;
      }
   }

   private static void dumpSet(String name, Set s) {
      logger.debug("" + name);
      Iterator it = s.iterator();

      while(it.hasNext()) {
         logger.debug("  " + it.next().toString());
      }

   }

   private static void dumpMultiMap(String name, MultiMap s) {
      logger.debug("" + name);
      Iterator it = s.keySet().iterator();

      while(it.hasNext()) {
         Object o = it.next();
         logger.debug("  " + o.toString());
         Iterator itt = s.get(o).iterator();

         while(itt.hasNext()) {
            logger.debug("    " + itt.next().toString());
         }
      }

   }

   void dump() {
      dumpSet("nodes Set:", this.nodes);
      dumpSet("paramNodes Set:", this.paramNodes);
      dumpMultiMap("edges MultiMap:", this.edges);
      dumpMultiMap("locals MultiMap:", this.locals);
      dumpSet("ret Set:", this.ret);
      dumpSet("globEscape Set:", this.globEscape);
      dumpMultiMap("backEdges MultiMap:", this.backEdges);
      dumpMultiMap("backLocals MultiMap:", this.backLocals);
      dumpMultiMap("mutated MultiMap:", this.mutated);
      logger.debug("");
   }

   void dumpStat() {
      logger.debug("Stat: " + maxInsideNodes + " inNodes, " + maxLoadNodes + " loadNodes, " + maxInsideEdges + " inEdges, " + maxOutsideEdges + " outEdges, " + maxMutated + " mutated.");
   }

   void updateStat() {
      Iterator it = this.nodes.iterator();
      int insideNodes = 0;
      int loadNodes = 0;

      while(it.hasNext()) {
         PurityNode n = (PurityNode)it.next();
         if (n.isInside()) {
            ++insideNodes;
         } else if (n.isLoad()) {
            ++loadNodes;
         }
      }

      int insideEdges = 0;
      int outsideEdges = 0;
      it = this.edges.keySet().iterator();

      while(it.hasNext()) {
         Iterator itt = this.edges.get(it.next()).iterator();

         while(itt.hasNext()) {
            PurityEdge e = (PurityEdge)itt.next();
            if (e.isInside()) {
               ++insideEdges;
            } else {
               ++outsideEdges;
            }
         }
      }

      int mutatedFields = 0;

      for(it = this.mutated.keySet().iterator(); it.hasNext(); mutatedFields += this.mutated.get(it.next()).size()) {
      }

      boolean changed = false;
      if (insideNodes > maxInsideNodes) {
         maxInsideNodes = insideNodes;
         changed = true;
      }

      if (loadNodes > maxLoadNodes) {
         maxLoadNodes = loadNodes;
         changed = true;
      }

      if (insideEdges > maxInsideEdges) {
         maxInsideEdges = insideEdges;
         changed = true;
      }

      if (outsideEdges > maxOutsideEdges) {
         maxOutsideEdges = outsideEdges;
         changed = true;
      }

      if (mutatedFields > maxMutated) {
         maxMutated = mutatedFields;
         changed = true;
      }

      if (changed) {
         this.dumpStat();
      }

   }
}
