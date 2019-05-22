package soot.jimple.spark.pag;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.FastHierarchy;
import soot.Kind;
import soot.Local;
import soot.PhaseOptions;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.builder.GlobalNodeFactory;
import soot.jimple.spark.builder.MethodNodeFactory;
import soot.jimple.spark.internal.ClientAccessibilityOracle;
import soot.jimple.spark.internal.SparkLibraryHelper;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.sets.BitPointsToSet;
import soot.jimple.spark.sets.DoublePointsToSet;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.HybridPointsToSet;
import soot.jimple.spark.sets.P2SetFactory;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.spark.sets.SharedHybridSet;
import soot.jimple.spark.sets.SharedListSet;
import soot.jimple.spark.sets.SortedArraySet;
import soot.jimple.spark.solver.OnFlyCallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.pointer.util.NativeMethodDriver;
import soot.options.CGOptions;
import soot.options.SparkOptions;
import soot.tagkit.LinkTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
import soot.toolkits.scalar.Pair;
import soot.util.ArrayNumberer;
import soot.util.HashMultiMap;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.Numberable;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public class PAG implements PointsToAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(PAG.class);
   protected static final Node[] EMPTY_NODE_ARRAY = new Node[0];
   protected P2SetFactory setFactory;
   protected boolean somethingMerged = false;
   ChunkedQueue<AllocNode> newAllocNodes = new ChunkedQueue();
   protected ChunkedQueue<Node> edgeQueue = new ChunkedQueue();
   private final ArrayNumberer<AllocNode> allocNodeNumberer = new ArrayNumberer();
   private final ArrayNumberer<VarNode> varNodeNumberer = new ArrayNumberer();
   private final ArrayNumberer<FieldRefNode> fieldRefNodeNumberer = new ArrayNumberer();
   private final ArrayNumberer<AllocDotField> allocDotFieldNodeNumberer = new ArrayNumberer();
   protected SparkOptions opts;
   protected CGOptions cgOpts;
   protected ClientAccessibilityOracle accessibilityOracle = Scene.v().getClientAccessibilityOracle();
   protected Map<VarNode, Object> simple = new HashMap();
   protected Map<FieldRefNode, Object> load = new HashMap();
   protected Map<VarNode, Object> store = new HashMap();
   protected Map<AllocNode, Object> alloc = new HashMap();
   protected Map<VarNode, Object> newInstance = new HashMap();
   protected Map<NewInstanceNode, Object> assignInstance = new HashMap();
   protected Map<VarNode, Object> simpleInv = new HashMap();
   protected Map<VarNode, Object> loadInv = new HashMap();
   protected Map<FieldRefNode, Object> storeInv = new HashMap();
   protected Map<VarNode, Object> allocInv = new HashMap();
   protected Map<NewInstanceNode, Object> newInstanceInv = new HashMap();
   protected Map<VarNode, Object> assignInstanceInv = new HashMap();
   private boolean runGeomPTA = false;
   protected MultiMap<Pair<Node, Node>, Edge> assign2edges = new HashMultiMap();
   private final Map<Object, LocalVarNode> valToLocalVarNode = new HashMap(1000);
   private final Map<Object, GlobalVarNode> valToGlobalVarNode = new HashMap(1000);
   private final Map<Object, AllocNode> valToAllocNode = new HashMap(1000);
   private final Table<Object, Type, AllocNode> valToReflAllocNode = HashBasedTable.create();
   private OnFlyCallGraph ofcg;
   private final ArrayList<VarNode> dereferences = new ArrayList();
   protected TypeManager typeManager;
   private final LargeNumberedMap<Local, LocalVarNode> localToNodeMap = new LargeNumberedMap(Scene.v().getLocalNumberer());
   private final Map<Value, NewInstanceNode> newInstToNodeMap = new HashMap();
   public int maxFinishNumber = 0;
   private Map<Node, Tag> nodeToTag;
   private final GlobalNodeFactory nodeFactory = new GlobalNodeFactory(this);
   public NativeMethodDriver nativeMethodDriver;
   public HashMultiMap<InvokeExpr, Pair<Node, Node>> callAssigns = new HashMultiMap();
   public Map<InvokeExpr, SootMethod> callToMethod = new HashMap();
   public Map<InvokeExpr, Node> virtualCallsToReceivers = new HashMap();

   public PAG(SparkOptions opts) {
      this.opts = opts;
      this.cgOpts = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
      if (opts.add_tags()) {
         this.nodeToTag = new HashMap();
      }

      if (opts.rta() && opts.on_fly_cg()) {
         throw new RuntimeException("Incompatible options rta:true and on-fly-cg:true for cg.spark. Use -p cg-.spark on-fly-cg:false when using RTA.");
      } else {
         this.typeManager = new TypeManager(this);
         if (!opts.ignore_types()) {
            this.typeManager.setFastHierarchy(Scene.v().getOrMakeFastHierarchy());
         }

         switch(opts.set_impl()) {
         case 1:
            this.setFactory = HashPointsToSet.getFactory();
            break;
         case 2:
            this.setFactory = BitPointsToSet.getFactory();
            break;
         case 3:
            this.setFactory = HybridPointsToSet.getFactory();
            break;
         case 4:
            this.setFactory = SortedArraySet.getFactory();
            break;
         case 5:
            this.setFactory = SharedHybridSet.getFactory();
            break;
         case 6:
            this.setFactory = SharedListSet.getFactory();
            break;
         case 7:
            P2SetFactory oldF;
            switch(opts.double_set_old()) {
            case 1:
               oldF = HashPointsToSet.getFactory();
               break;
            case 2:
               oldF = BitPointsToSet.getFactory();
               break;
            case 3:
               oldF = HybridPointsToSet.getFactory();
               break;
            case 4:
               oldF = SortedArraySet.getFactory();
               break;
            case 5:
               oldF = SharedHybridSet.getFactory();
               break;
            case 6:
               oldF = SharedListSet.getFactory();
               break;
            default:
               throw new RuntimeException();
            }

            P2SetFactory newF;
            switch(opts.double_set_new()) {
            case 1:
               newF = HashPointsToSet.getFactory();
               break;
            case 2:
               newF = BitPointsToSet.getFactory();
               break;
            case 3:
               newF = HybridPointsToSet.getFactory();
               break;
            case 4:
               newF = SortedArraySet.getFactory();
               break;
            case 5:
               newF = SharedHybridSet.getFactory();
               break;
            case 6:
               newF = SharedListSet.getFactory();
               break;
            default:
               throw new RuntimeException();
            }

            this.setFactory = DoublePointsToSet.getFactory(newF, oldF);
            break;
         default:
            throw new RuntimeException();
         }

         this.runGeomPTA = opts.geom_pta();
      }
   }

   public PointsToSet reachingObjects(Local l) {
      VarNode n = this.findLocalVarNode(l);
      return (PointsToSet)(n == null ? EmptyPointsToSet.v() : n.getP2Set());
   }

   public PointsToSet reachingObjects(Context c, Local l) {
      VarNode n = this.findContextVarNode(l, c);
      return (PointsToSet)(n == null ? EmptyPointsToSet.v() : n.getP2Set());
   }

   public PointsToSet reachingObjects(SootField f) {
      if (!f.isStatic()) {
         throw new RuntimeException("The parameter f must be a *static* field.");
      } else {
         VarNode n = this.findGlobalVarNode(f);
         return (PointsToSet)(n == null ? EmptyPointsToSet.v() : n.getP2Set());
      }
   }

   public PointsToSet reachingObjects(PointsToSet s, SootField f) {
      if (f.isStatic()) {
         throw new RuntimeException("The parameter f must be an *instance* field.");
      } else {
         return this.reachingObjectsInternal(s, f);
      }
   }

   public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
      return this.reachingObjectsInternal(s, ArrayElement.v());
   }

   private PointsToSet reachingObjectsInternal(PointsToSet s, final SparkField f) {
      if (!this.getOpts().field_based() && !this.getOpts().vta()) {
         if (this.getOpts().propagator() == 5) {
            throw new RuntimeException("The alias edge propagator does not compute points-to information for instance fields! Use a different propagator.");
         } else {
            PointsToSetInternal bases = (PointsToSetInternal)s;
            final PointsToSetInternal ret = this.setFactory.newSet(f instanceof SootField ? ((SootField)f).getType() : null, this);
            bases.forall(new P2SetVisitor() {
               public final void visit(Node n) {
                  Node nDotF = ((AllocNode)n).dot(f);
                  if (nDotF != null) {
                     ret.addAll(nDotF.getP2Set(), (PointsToSetInternal)null);
                  }

               }
            });
            return ret;
         }
      } else {
         VarNode n = this.findGlobalVarNode(f);
         return (PointsToSet)(n == null ? EmptyPointsToSet.v() : n.getP2Set());
      }
   }

   public P2SetFactory getSetFactory() {
      return this.setFactory;
   }

   private <K extends Node> void lookupInMap(Map<K, Object> map) {
      Iterator var2 = map.keySet().iterator();

      while(var2.hasNext()) {
         K object = (Node)var2.next();
         this.lookup(map, object);
      }

   }

   public void cleanUpMerges() {
      if (this.opts.verbose()) {
         logger.debug("Cleaning up graph for merged nodes");
      }

      this.lookupInMap(this.simple);
      this.lookupInMap(this.alloc);
      this.lookupInMap(this.store);
      this.lookupInMap(this.load);
      this.lookupInMap(this.simpleInv);
      this.lookupInMap(this.allocInv);
      this.lookupInMap(this.storeInv);
      this.lookupInMap(this.loadInv);
      this.somethingMerged = false;
      if (this.opts.verbose()) {
         logger.debug("Done cleaning up graph for merged nodes");
      }

   }

   public boolean doAddSimpleEdge(VarNode from, VarNode to) {
      return this.addToMap(this.simple, from, to) | this.addToMap(this.simpleInv, to, from);
   }

   public boolean doAddStoreEdge(VarNode from, FieldRefNode to) {
      return this.addToMap(this.store, from, to) | this.addToMap(this.storeInv, to, from);
   }

   public boolean doAddLoadEdge(FieldRefNode from, VarNode to) {
      return this.addToMap(this.load, from, to) | this.addToMap(this.loadInv, to, from);
   }

   public boolean doAddAllocEdge(AllocNode from, VarNode to) {
      return this.addToMap(this.alloc, from, to) | this.addToMap(this.allocInv, to, from);
   }

   public boolean doAddNewInstanceEdge(VarNode from, NewInstanceNode to) {
      return this.addToMap(this.newInstance, from, to) | this.addToMap(this.newInstanceInv, to, from);
   }

   public boolean doAddAssignInstanceEdge(NewInstanceNode from, VarNode to) {
      return this.addToMap(this.assignInstance, from, to) | this.addToMap(this.assignInstanceInv, to, from);
   }

   void mergedWith(Node n1, Node n2) {
      if (n1.equals(n2)) {
         throw new RuntimeException("oops");
      } else {
         this.somethingMerged = true;
         if (this.ofcg() != null) {
            this.ofcg().mergedWith(n1, n2);
         }

         Map[] maps = new Map[]{this.simple, this.alloc, this.store, this.load, this.simpleInv, this.allocInv, this.storeInv, this.loadInv};
         Map[] var4 = maps;
         int var5 = maps.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Map<Node, Object> m = var4[var6];
            if (m.keySet().contains(n2)) {
               Object[] os = new Object[]{m.get(n1), m.get(n2)};
               int size1 = getSize(os[0]);
               int size2 = getSize(os[1]);
               if (size1 == 0) {
                  if (os[1] != null) {
                     m.put(n1, os[1]);
                  }
               } else if (size2 != 0) {
                  Node[] a1;
                  Node[] a2;
                  int var13;
                  int j;
                  Node element0;
                  if (os[0] instanceof HashSet) {
                     if (os[1] instanceof HashSet) {
                        ((HashSet)os[0]).addAll((HashSet)os[1]);
                     } else {
                        a1 = (Node[])((Node[])os[1]);
                        a2 = a1;
                        var13 = a1.length;

                        for(j = 0; j < var13; ++j) {
                           element0 = a2[j];
                           ((HashSet)os[0]).add(element0);
                        }
                     }
                  } else if (os[1] instanceof HashSet) {
                     a1 = (Node[])((Node[])os[0]);
                     a2 = a1;
                     var13 = a1.length;

                     for(j = 0; j < var13; ++j) {
                        element0 = a2[j];
                        ((HashSet)os[1]).add(element0);
                     }

                     m.put(n1, os[1]);
                  } else {
                     int k;
                     if (size1 * size2 < 1000) {
                        a1 = (Node[])((Node[])os[0]);
                        a2 = (Node[])((Node[])os[1]);
                        Node[] ret = new Node[size1 + size2];
                        System.arraycopy(a1, 0, ret, 0, a1.length);
                        j = a1.length;
                        Node[] newArray = a2;
                        int var16 = a2.length;

                        label90:
                        for(int var17 = 0; var17 < var16; ++var17) {
                           Node rep = newArray[var17];

                           for(k = 0; k < j; ++k) {
                              if (rep == ret[k]) {
                                 continue label90;
                              }
                           }

                           ret[j++] = rep;
                        }

                        newArray = new Node[j];
                        System.arraycopy(ret, 0, newArray, 0, j);
                        m.put(n1, newArray);
                     } else {
                        HashSet<Node> s = new HashSet(size1 + size2);
                        Object[] var22 = os;
                        var13 = os.length;

                        for(j = 0; j < var13; ++j) {
                           Object o = var22[j];
                           if (o != null) {
                              if (o instanceof Set) {
                                 s.addAll((Set)o);
                              } else {
                                 Node[] ar = (Node[])((Node[])o);
                                 Node[] var27 = ar;
                                 int var28 = ar.length;

                                 for(k = 0; k < var28; ++k) {
                                    Node element1 = var27[k];
                                    s.add(element1);
                                 }
                              }
                           }
                        }

                        m.put(n1, s);
                     }
                  }
               }

               m.remove(n2);
            }
         }

      }
   }

   protected <K extends Node> Node[] lookup(Map<K, Object> m, K key) {
      Object valueList = m.get(key);
      if (valueList == null) {
         return EMPTY_NODE_ARRAY;
      } else {
         if (valueList instanceof Set) {
            try {
               m.put(key, valueList = ((Set)valueList).toArray(EMPTY_NODE_ARRAY));
            } catch (Exception var11) {
               Iterator it = ((Set)valueList).iterator();

               while(it.hasNext()) {
                  logger.debug("" + it.next());
               }

               throw new RuntimeException("" + valueList + var11);
            }
         }

         Node[] ret = (Node[])((Node[])valueList);
         if (this.somethingMerged) {
            for(int i = 0; i < ret.length; ++i) {
               Node reti = ret[i];
               Node rep = reti.getReplacement();
               if (rep != reti || rep == key) {
                  int j;
                  if (ret.length <= 75) {
                     label81:
                     for(j = i; i < ret.length; ++i) {
                        reti = ret[i];
                        rep = reti.getReplacement();
                        if (rep != key) {
                           for(int k = 0; k < j; ++k) {
                              if (rep == ret[k]) {
                                 continue label81;
                              }
                           }

                           ret[j++] = rep;
                        }
                     }

                     Node[] newArray = new Node[j];
                     System.arraycopy(ret, 0, newArray, 0, j);
                     ret = newArray;
                     m.put(key, newArray);
                  } else {
                     Set<Node> s = new HashSet(ret.length * 2);

                     for(j = 0; j < i; ++j) {
                        s.add(ret[j]);
                     }

                     for(j = i; j < ret.length; ++j) {
                        rep = ret[j].getReplacement();
                        if (rep != key) {
                           s.add(rep);
                        }
                     }

                     m.put(key, ret = (Node[])s.toArray(EMPTY_NODE_ARRAY));
                  }
                  break;
               }
            }
         }

         return ret;
      }
   }

   public Node[] simpleLookup(VarNode key) {
      return this.lookup(this.simple, key);
   }

   public Node[] simpleInvLookup(VarNode key) {
      return this.lookup(this.simpleInv, key);
   }

   public Node[] loadLookup(FieldRefNode key) {
      return this.lookup(this.load, key);
   }

   public Node[] loadInvLookup(VarNode key) {
      return this.lookup(this.loadInv, key);
   }

   public Node[] storeLookup(VarNode key) {
      return this.lookup(this.store, key);
   }

   public Node[] newInstanceLookup(VarNode key) {
      return this.lookup(this.newInstance, key);
   }

   public Node[] assignInstanceLookup(NewInstanceNode key) {
      return this.lookup(this.assignInstance, key);
   }

   public Node[] storeInvLookup(FieldRefNode key) {
      return this.lookup(this.storeInv, key);
   }

   public Node[] allocLookup(AllocNode key) {
      return this.lookup(this.alloc, key);
   }

   public Node[] allocInvLookup(VarNode key) {
      return this.lookup(this.allocInv, key);
   }

   public Set<VarNode> simpleSources() {
      return this.simple.keySet();
   }

   public Set<AllocNode> allocSources() {
      return this.alloc.keySet();
   }

   public Set<VarNode> storeSources() {
      return this.store.keySet();
   }

   public Set<FieldRefNode> loadSources() {
      return this.load.keySet();
   }

   public Set<VarNode> newInstanceSources() {
      return this.newInstance.keySet();
   }

   public Set<NewInstanceNode> assignInstanceSources() {
      return this.assignInstance.keySet();
   }

   public Set<VarNode> simpleInvSources() {
      return this.simpleInv.keySet();
   }

   public Set<VarNode> allocInvSources() {
      return this.allocInv.keySet();
   }

   public Set<FieldRefNode> storeInvSources() {
      return this.storeInv.keySet();
   }

   public Set<VarNode> loadInvSources() {
      return this.loadInv.keySet();
   }

   public Iterator<VarNode> simpleSourcesIterator() {
      return this.simple.keySet().iterator();
   }

   public Iterator<AllocNode> allocSourcesIterator() {
      return this.alloc.keySet().iterator();
   }

   public Iterator<VarNode> storeSourcesIterator() {
      return this.store.keySet().iterator();
   }

   public Iterator<FieldRefNode> loadSourcesIterator() {
      return this.load.keySet().iterator();
   }

   public Iterator<VarNode> simpleInvSourcesIterator() {
      return this.simpleInv.keySet().iterator();
   }

   public Iterator<VarNode> allocInvSourcesIterator() {
      return this.allocInv.keySet().iterator();
   }

   public Iterator<FieldRefNode> storeInvSourcesIterator() {
      return this.storeInv.keySet().iterator();
   }

   public Iterator<VarNode> loadInvSourcesIterator() {
      return this.loadInv.keySet().iterator();
   }

   private static int getSize(Object set) {
      if (set instanceof Set) {
         return ((Set)set).size();
      } else {
         return set == null ? 0 : ((Object[])((Object[])set)).length;
      }
   }

   public PointsToSet reachingObjects(Local l, SootField f) {
      return this.reachingObjects(this.reachingObjects(l), f);
   }

   public PointsToSet reachingObjects(Context c, Local l, SootField f) {
      return this.reachingObjects(this.reachingObjects(c, l), f);
   }

   private void addNodeTag(Node node, SootMethod m) {
      if (this.nodeToTag != null) {
         Object tag;
         if (m == null) {
            tag = new StringTag(node.toString());
         } else {
            tag = new LinkTag(node.toString(), m, m.getDeclaringClass().getName());
         }

         this.nodeToTag.put(node, tag);
      }

   }

   public AllocNode makeAllocNode(Object newExpr, Type type, SootMethod m) {
      if (this.opts.types_for_sites() || this.opts.vta()) {
         newExpr = type;
      }

      AllocNode ret = (AllocNode)this.valToAllocNode.get(newExpr);
      if (newExpr instanceof NewExpr) {
         if (ret == null) {
            this.valToAllocNode.put(newExpr, ret = new AllocNode(this, newExpr, type, m));
            this.newAllocNodes.add(ret);
            this.addNodeTag(ret, m);
         } else if (!ret.getType().equals(type)) {
            throw new RuntimeException("NewExpr " + newExpr + " of type " + type + " previously had type " + ret.getType());
         }
      } else {
         ret = (AllocNode)this.valToReflAllocNode.get(newExpr, type);
         if (ret == null) {
            this.valToReflAllocNode.put(newExpr, type, ret = new AllocNode(this, newExpr, type, m));
            this.newAllocNodes.add(ret);
            this.addNodeTag(ret, m);
         }
      }

      return ret;
   }

   public AllocNode makeStringConstantNode(String s) {
      if (!this.opts.types_for_sites() && !this.opts.vta()) {
         StringConstantNode ret = (StringConstantNode)this.valToAllocNode.get(s);
         if (ret == null) {
            this.valToAllocNode.put(s, ret = new StringConstantNode(this, s));
            this.newAllocNodes.add(ret);
            this.addNodeTag(ret, (SootMethod)null);
         }

         return ret;
      } else {
         return this.makeAllocNode(RefType.v("java.lang.String"), RefType.v("java.lang.String"), (SootMethod)null);
      }
   }

   public AllocNode makeClassConstantNode(ClassConstant cc) {
      if (!this.opts.types_for_sites() && !this.opts.vta()) {
         ClassConstantNode ret = (ClassConstantNode)this.valToAllocNode.get(cc);
         if (ret == null) {
            this.valToAllocNode.put(cc, ret = new ClassConstantNode(this, cc));
            this.newAllocNodes.add(ret);
            this.addNodeTag(ret, (SootMethod)null);
         }

         return ret;
      } else {
         return this.makeAllocNode(RefType.v("java.lang.Class"), RefType.v("java.lang.Class"), (SootMethod)null);
      }
   }

   public QueueReader<AllocNode> allocNodeListener() {
      return this.newAllocNodes.reader();
   }

   public GlobalVarNode findGlobalVarNode(Object value) {
      if (this.opts.rta()) {
         value = null;
      }

      return (GlobalVarNode)this.valToGlobalVarNode.get(value);
   }

   public LocalVarNode findLocalVarNode(Object value) {
      if (this.opts.rta()) {
         value = null;
      } else if (value instanceof Local) {
         return (LocalVarNode)this.localToNodeMap.get((Local)value);
      }

      return (LocalVarNode)this.valToLocalVarNode.get(value);
   }

   public GlobalVarNode makeGlobalVarNode(Object value, Type type) {
      if (this.opts.rta()) {
         value = null;
         type = RefType.v("java.lang.Object");
      }

      GlobalVarNode ret = (GlobalVarNode)this.valToGlobalVarNode.get(value);
      if (ret == null) {
         this.valToGlobalVarNode.put(value, ret = new GlobalVarNode(this, value, (Type)type));
         if (this.cgOpts.library() != 1 && value instanceof SootField) {
            SootField sf = (SootField)value;
            if (this.accessibilityOracle.isAccessible(sf)) {
               ((Type)type).apply(new SparkLibraryHelper(this, ret, (SootMethod)null));
            }
         }

         this.addNodeTag(ret, (SootMethod)null);
      } else if (!ret.getType().equals(type)) {
         throw new RuntimeException("Value " + value + " of type " + type + " previously had type " + ret.getType());
      }

      return ret;
   }

   public LocalVarNode makeLocalVarNode(Object value, Type type, SootMethod method) {
      if (this.opts.rta()) {
         value = null;
         type = RefType.v("java.lang.Object");
         method = null;
      } else if (value instanceof Local) {
         Local val = (Local)value;
         if (val.getNumber() == 0) {
            Scene.v().getLocalNumberer().add((Numberable)val);
         }

         LocalVarNode ret = (LocalVarNode)this.localToNodeMap.get(val);
         if (ret == null) {
            this.localToNodeMap.put((Local)value, ret = new LocalVarNode(this, value, (Type)type, method));
            this.addNodeTag(ret, method);
         } else if (!ret.getType().equals(type)) {
            throw new RuntimeException("Value " + value + " of type " + type + " previously had type " + ret.getType());
         }

         return ret;
      }

      LocalVarNode ret = (LocalVarNode)this.valToLocalVarNode.get(value);
      if (ret == null) {
         this.valToLocalVarNode.put(value, ret = new LocalVarNode(this, value, (Type)type, method));
         this.addNodeTag(ret, method);
      } else if (!ret.getType().equals(type)) {
         throw new RuntimeException("Value " + value + " of type " + type + " previously had type " + ret.getType());
      }

      return ret;
   }

   public NewInstanceNode makeNewInstanceNode(Value value, Type type, SootMethod method) {
      NewInstanceNode node = (NewInstanceNode)this.newInstToNodeMap.get(value);
      if (node == null) {
         node = new NewInstanceNode(this, value, type);
         this.newInstToNodeMap.put(value, node);
         this.addNodeTag(node, method);
      }

      return node;
   }

   public ContextVarNode findContextVarNode(Object baseValue, Context context) {
      LocalVarNode base = this.findLocalVarNode(baseValue);
      return base == null ? null : base.context(context);
   }

   public ContextVarNode makeContextVarNode(Object baseValue, Type baseType, Context context, SootMethod method) {
      LocalVarNode base = this.makeLocalVarNode(baseValue, baseType, method);
      return this.makeContextVarNode(base, context);
   }

   public ContextVarNode makeContextVarNode(LocalVarNode base, Context context) {
      ContextVarNode ret = base.context(context);
      if (ret == null) {
         ret = new ContextVarNode(this, base, context);
         this.addNodeTag(ret, base.getMethod());
      }

      return ret;
   }

   public FieldRefNode findLocalFieldRefNode(Object baseValue, SparkField field) {
      VarNode base = this.findLocalVarNode(baseValue);
      return base == null ? null : base.dot(field);
   }

   public FieldRefNode findGlobalFieldRefNode(Object baseValue, SparkField field) {
      VarNode base = this.findGlobalVarNode(baseValue);
      return base == null ? null : base.dot(field);
   }

   public FieldRefNode makeLocalFieldRefNode(Object baseValue, Type baseType, SparkField field, SootMethod method) {
      VarNode base = this.makeLocalVarNode(baseValue, baseType, method);
      FieldRefNode ret = this.makeFieldRefNode(base, field);
      if (this.cgOpts.library() != 1 && field instanceof SootField) {
         SootField sf = (SootField)field;
         Type type = sf.getType();
         if (this.accessibilityOracle.isAccessible(sf)) {
            type.apply(new SparkLibraryHelper(this, ret, method));
         }
      }

      return ret;
   }

   public FieldRefNode makeGlobalFieldRefNode(Object baseValue, Type baseType, SparkField field) {
      VarNode base = this.makeGlobalVarNode(baseValue, baseType);
      return this.makeFieldRefNode(base, field);
   }

   public FieldRefNode makeFieldRefNode(VarNode base, SparkField field) {
      FieldRefNode ret = base.dot(field);
      if (ret == null) {
         ret = new FieldRefNode(this, base, field);
         if (base instanceof LocalVarNode) {
            this.addNodeTag(ret, ((LocalVarNode)base).getMethod());
         } else {
            this.addNodeTag(ret, (SootMethod)null);
         }
      }

      return ret;
   }

   public AllocDotField findAllocDotField(AllocNode an, SparkField field) {
      return an.dot(field);
   }

   public AllocDotField makeAllocDotField(AllocNode an, SparkField field) {
      AllocDotField ret = an.dot(field);
      if (ret == null) {
         ret = new AllocDotField(this, an, field);
      }

      return ret;
   }

   public boolean addSimpleEdge(VarNode from, VarNode to) {
      boolean ret = false;
      if (this.doAddSimpleEdge(from, to)) {
         this.edgeQueue.add(from);
         this.edgeQueue.add(to);
         ret = true;
      }

      if (this.opts.simple_edges_bidirectional() && this.doAddSimpleEdge(to, from)) {
         this.edgeQueue.add(to);
         this.edgeQueue.add(from);
         ret = true;
      }

      return ret;
   }

   public boolean addStoreEdge(VarNode from, FieldRefNode to) {
      if (!this.opts.rta() && this.doAddStoreEdge(from, to)) {
         this.edgeQueue.add(from);
         this.edgeQueue.add(to);
         return true;
      } else {
         return false;
      }
   }

   public boolean addLoadEdge(FieldRefNode from, VarNode to) {
      if (!this.opts.rta() && this.doAddLoadEdge(from, to)) {
         this.edgeQueue.add(from);
         this.edgeQueue.add(to);
         return true;
      } else {
         return false;
      }
   }

   public boolean addAllocEdge(AllocNode from, VarNode to) {
      FastHierarchy fh = this.typeManager.getFastHierarchy();
      if ((fh == null || to.getType() == null || fh.canStoreType(from.getType(), to.getType())) && this.doAddAllocEdge(from, to)) {
         this.edgeQueue.add(from);
         this.edgeQueue.add(to);
         return true;
      } else {
         return false;
      }
   }

   public boolean addNewInstanceEdge(VarNode from, NewInstanceNode to) {
      if (!this.opts.rta() && this.doAddNewInstanceEdge(from, to)) {
         this.edgeQueue.add(from);
         this.edgeQueue.add(to);
         return true;
      } else {
         return false;
      }
   }

   public boolean addAssignInstanceEdge(NewInstanceNode from, VarNode to) {
      if (!this.opts.rta() && this.doAddAssignInstanceEdge(from, to)) {
         this.edgeQueue.add(from);
         this.edgeQueue.add(to);
         return true;
      } else {
         return false;
      }
   }

   public final boolean addEdge(Node from, Node to) {
      from = from.getReplacement();
      to = to.getReplacement();
      if (from instanceof VarNode) {
         if (to instanceof VarNode) {
            return this.addSimpleEdge((VarNode)from, (VarNode)to);
         } else if (to instanceof FieldRefNode) {
            return this.addStoreEdge((VarNode)from, (FieldRefNode)to);
         } else if (to instanceof NewInstanceNode) {
            return this.addNewInstanceEdge((VarNode)from, (NewInstanceNode)to);
         } else {
            throw new RuntimeException("Invalid node type");
         }
      } else if (from instanceof FieldRefNode) {
         return this.addLoadEdge((FieldRefNode)from, (VarNode)to);
      } else {
         return from instanceof NewInstanceNode ? this.addAssignInstanceEdge((NewInstanceNode)from, (VarNode)to) : this.addAllocEdge((AllocNode)from, (VarNode)to);
      }
   }

   public QueueReader<Node> edgeReader() {
      return this.edgeQueue.reader();
   }

   public int getNumAllocNodes() {
      return this.allocNodeNumberer.size();
   }

   public TypeManager getTypeManager() {
      return this.typeManager;
   }

   public void setOnFlyCallGraph(OnFlyCallGraph ofcg) {
      this.ofcg = ofcg;
   }

   public OnFlyCallGraph getOnFlyCallGraph() {
      return this.ofcg;
   }

   public OnFlyCallGraph ofcg() {
      return this.ofcg;
   }

   public void addDereference(VarNode base) {
      this.dereferences.add(base);
   }

   public List<VarNode> getDereferences() {
      return this.dereferences;
   }

   public Map<Node, Tag> getNodeTags() {
      return this.nodeToTag;
   }

   public ArrayNumberer<AllocNode> getAllocNodeNumberer() {
      return this.allocNodeNumberer;
   }

   public ArrayNumberer<VarNode> getVarNodeNumberer() {
      return this.varNodeNumberer;
   }

   public ArrayNumberer<FieldRefNode> getFieldRefNodeNumberer() {
      return this.fieldRefNodeNumberer;
   }

   public ArrayNumberer<AllocDotField> getAllocDotFieldNodeNumberer() {
      return this.allocDotFieldNodeNumberer;
   }

   public SparkOptions getOpts() {
      return this.opts;
   }

   public CGOptions getCGOpts() {
      return this.cgOpts;
   }

   public Pair<Node, Node> addInterproceduralAssignment(Node from, Node to, Edge e) {
      Pair<Node, Node> val = new Pair(from, to);
      if (this.runGeomPTA) {
         this.assign2edges.put(val, e);
      }

      return val;
   }

   public Set<Edge> lookupEdgesForAssignment(Pair<Node, Node> val) {
      return this.assign2edges.get(val);
   }

   public final void addCallTarget(Edge e) {
      if (e.passesParameters()) {
         MethodPAG srcmpag = MethodPAG.v(this, e.src());
         MethodPAG tgtmpag = MethodPAG.v(this, e.tgt());
         if (!e.isExplicit() && e.kind() != Kind.THREAD && e.kind() != Kind.ASYNCTASK) {
            Pair pval;
            InvokeExpr ie;
            boolean virtualCall;
            Node parm0;
            Node thiz;
            if (e.kind() == Kind.EXECUTOR) {
               ie = e.srcStmt().getInvokeExpr();
               virtualCall = this.callAssigns.containsKey(ie);
               parm0 = srcmpag.nodeFactory().getNode(ie.getArg(0));
               parm0 = srcmpag.parameterize(parm0, e.srcCtxt());
               parm0 = parm0.getReplacement();
               thiz = tgtmpag.nodeFactory().caseThis();
               thiz = tgtmpag.parameterize(thiz, e.tgtCtxt());
               thiz = thiz.getReplacement();
               this.addEdge(parm0, thiz);
               pval = this.addInterproceduralAssignment(parm0, thiz, e);
               this.callAssigns.put(ie, pval);
               this.callToMethod.put(ie, srcmpag.getMethod());
               if (virtualCall && !this.virtualCallsToReceivers.containsKey(ie)) {
                  this.virtualCallsToReceivers.put(ie, parm0);
               }
            } else if (e.kind() == Kind.HANDLER) {
               ie = e.srcStmt().getInvokeExpr();
               virtualCall = this.callAssigns.containsKey(ie);

               assert virtualCall;

               parm0 = srcmpag.nodeFactory().getNode(((VirtualInvokeExpr)ie).getBase());
               parm0 = srcmpag.parameterize(parm0, e.srcCtxt());
               parm0 = parm0.getReplacement();
               thiz = tgtmpag.nodeFactory().caseThis();
               thiz = tgtmpag.parameterize(thiz, e.tgtCtxt());
               thiz = thiz.getReplacement();
               this.addEdge(parm0, thiz);
               pval = this.addInterproceduralAssignment(parm0, thiz, e);
               this.callAssigns.put(ie, pval);
               this.callToMethod.put(ie, srcmpag.getMethod());
               this.virtualCallsToReceivers.put(ie, parm0);
            } else {
               Node parm1;
               Node ret;
               Node tgtThis;
               if (e.kind() == Kind.PRIVILEGED) {
                  ie = e.srcStmt().getInvokeExpr();
                  tgtThis = srcmpag.nodeFactory().getNode(ie.getArg(0));
                  tgtThis = srcmpag.parameterize(tgtThis, e.srcCtxt());
                  tgtThis = tgtThis.getReplacement();
                  parm0 = tgtmpag.nodeFactory().caseThis();
                  parm0 = tgtmpag.parameterize(parm0, e.tgtCtxt());
                  parm0 = parm0.getReplacement();
                  this.addEdge(tgtThis, parm0);
                  pval = this.addInterproceduralAssignment(tgtThis, parm0, e);
                  this.callAssigns.put(ie, pval);
                  this.callToMethod.put(ie, srcmpag.getMethod());
                  if (e.srcUnit() instanceof AssignStmt) {
                     AssignStmt as = (AssignStmt)e.srcUnit();
                     parm1 = tgtmpag.nodeFactory().caseRet();
                     parm1 = tgtmpag.parameterize(parm1, e.tgtCtxt());
                     parm1 = parm1.getReplacement();
                     ret = srcmpag.nodeFactory().getNode(as.getLeftOp());
                     ret = srcmpag.parameterize(ret, e.srcCtxt());
                     ret = ret.getReplacement();
                     this.addEdge(parm1, ret);
                     pval = this.addInterproceduralAssignment(parm1, ret, e);
                     this.callAssigns.put(ie, pval);
                     this.callToMethod.put(ie, srcmpag.getMethod());
                  }
               } else if (e.kind() == Kind.FINALIZE) {
                  Node srcThis = srcmpag.nodeFactory().caseThis();
                  srcThis = srcmpag.parameterize(srcThis, e.srcCtxt());
                  srcThis = srcThis.getReplacement();
                  tgtThis = tgtmpag.nodeFactory().caseThis();
                  tgtThis = tgtmpag.parameterize(tgtThis, e.tgtCtxt());
                  tgtThis = tgtThis.getReplacement();
                  this.addEdge(srcThis, tgtThis);
                  this.addInterproceduralAssignment(srcThis, tgtThis, e);
               } else {
                  Node lhs;
                  Stmt s;
                  InstanceInvokeExpr iie;
                  if (e.kind() == Kind.NEWINSTANCE) {
                     s = (Stmt)e.srcUnit();
                     iie = (InstanceInvokeExpr)s.getInvokeExpr();
                     parm0 = srcmpag.nodeFactory().getNode(iie.getBase());
                     parm0 = srcmpag.parameterize(parm0, e.srcCtxt());
                     parm0 = parm0.getReplacement();
                     thiz = this.nodeFactory.caseNewInstance((VarNode)parm0);
                     parm1 = tgtmpag.nodeFactory().caseThis();
                     parm1 = tgtmpag.parameterize(parm1, e.tgtCtxt());
                     parm1 = parm1.getReplacement();
                     this.addEdge(thiz, parm1);
                     if (s instanceof AssignStmt) {
                        AssignStmt as = (AssignStmt)s;
                        lhs = srcmpag.nodeFactory().getNode(as.getLeftOp());
                        lhs = srcmpag.parameterize(lhs, e.srcCtxt());
                        lhs = lhs.getReplacement();
                        this.addEdge(thiz, lhs);
                     }

                     pval = this.addInterproceduralAssignment(thiz, parm1, e);
                     this.callAssigns.put(s.getInvokeExpr(), pval);
                     this.callToMethod.put(s.getInvokeExpr(), srcmpag.getMethod());
                  } else {
                     Node tgtParmI;
                     if (e.kind() == Kind.REFL_INVOKE) {
                        ie = e.srcStmt().getInvokeExpr();
                        Value arg0 = ie.getArg(0);
                        if (arg0 != NullConstant.v()) {
                           parm0 = srcmpag.nodeFactory().getNode(arg0);
                           parm0 = srcmpag.parameterize(parm0, e.srcCtxt());
                           parm0 = parm0.getReplacement();
                           thiz = tgtmpag.nodeFactory().caseThis();
                           thiz = tgtmpag.parameterize(thiz, e.tgtCtxt());
                           thiz = thiz.getReplacement();
                           this.addEdge(parm0, thiz);
                           pval = this.addInterproceduralAssignment(parm0, thiz, e);
                           this.callAssigns.put(ie, pval);
                           this.callToMethod.put(ie, srcmpag.getMethod());
                        }

                        Value arg1 = ie.getArg(1);
                        SootMethod tgt = e.getTgt().method();
                        if (arg1 != NullConstant.v() && tgt.getParameterCount() > 0) {
                           parm1 = srcmpag.nodeFactory().getNode(arg1);
                           parm1 = srcmpag.parameterize(parm1, e.srcCtxt());
                           parm1 = parm1.getReplacement();
                           FieldRefNode parm1contents = this.makeFieldRefNode((VarNode)parm1, ArrayElement.v());

                           for(int i = 0; i < tgt.getParameterCount(); ++i) {
                              if (tgt.getParameterType(i) instanceof RefLikeType) {
                                 tgtParmI = tgtmpag.nodeFactory().caseParm(i);
                                 tgtParmI = tgtmpag.parameterize(tgtParmI, e.tgtCtxt());
                                 tgtParmI = tgtParmI.getReplacement();
                                 this.addEdge(parm1contents, tgtParmI);
                                 pval = this.addInterproceduralAssignment(parm1contents, tgtParmI, e);
                                 this.callAssigns.put(ie, pval);
                              }
                           }
                        }

                        if (e.srcUnit() instanceof AssignStmt && tgt.getReturnType() instanceof RefLikeType) {
                           AssignStmt as = (AssignStmt)e.srcUnit();
                           ret = tgtmpag.nodeFactory().caseRet();
                           ret = tgtmpag.parameterize(ret, e.tgtCtxt());
                           ret = ret.getReplacement();
                           lhs = srcmpag.nodeFactory().getNode(as.getLeftOp());
                           lhs = srcmpag.parameterize(lhs, e.srcCtxt());
                           lhs = lhs.getReplacement();
                           this.addEdge(ret, lhs);
                           pval = this.addInterproceduralAssignment(ret, lhs, e);
                           this.callAssigns.put(ie, pval);
                        }
                     } else {
                        if (e.kind() != Kind.REFL_CLASS_NEWINSTANCE && e.kind() != Kind.REFL_CONSTR_NEWINSTANCE) {
                           throw new RuntimeException("Unhandled edge " + e);
                        }

                        s = (Stmt)e.srcUnit();
                        iie = (InstanceInvokeExpr)s.getInvokeExpr();
                        parm0 = srcmpag.nodeFactory().getNode(iie.getBase());
                        parm0 = srcmpag.parameterize(parm0, e.srcCtxt());
                        Node cls = parm0.getReplacement();
                        if (cls instanceof ContextVarNode) {
                           cls = this.findLocalVarNode(((VarNode)cls).getVariable());
                        }

                        VarNode newObject = this.makeGlobalVarNode(cls, RefType.v("java.lang.Object"));
                        SootClass tgtClass = e.getTgt().method().getDeclaringClass();
                        RefType tgtType = tgtClass.getType();
                        AllocNode site = this.makeAllocNode(new Pair(cls, tgtClass), tgtType, (SootMethod)null);
                        this.addEdge(site, newObject);
                        tgtParmI = tgtmpag.nodeFactory().caseThis();
                        tgtParmI = tgtmpag.parameterize(tgtParmI, e.tgtCtxt());
                        tgtParmI = tgtParmI.getReplacement();
                        this.addEdge(newObject, tgtParmI);
                        this.addInterproceduralAssignment(newObject, tgtParmI, e);
                        if (e.kind() == Kind.REFL_CONSTR_NEWINSTANCE) {
                           Value arg = iie.getArg(0);
                           SootMethod tgt = e.getTgt().method();
                           if (arg != NullConstant.v() && tgt.getParameterCount() > 0) {
                              Node parm0 = srcmpag.nodeFactory().getNode(arg);
                              parm0 = srcmpag.parameterize(parm0, e.srcCtxt());
                              parm0 = parm0.getReplacement();
                              FieldRefNode parm1contents = this.makeFieldRefNode((VarNode)parm0, ArrayElement.v());

                              for(int i = 0; i < tgt.getParameterCount(); ++i) {
                                 if (tgt.getParameterType(i) instanceof RefLikeType) {
                                    Node tgtParmI = tgtmpag.nodeFactory().caseParm(i);
                                    tgtParmI = tgtmpag.parameterize(tgtParmI, e.tgtCtxt());
                                    tgtParmI = tgtParmI.getReplacement();
                                    this.addEdge(parm1contents, tgtParmI);
                                    pval = this.addInterproceduralAssignment(parm1contents, tgtParmI, e);
                                    this.callAssigns.put(iie, pval);
                                 }
                              }
                           }
                        }

                        if (s instanceof AssignStmt) {
                           AssignStmt as = (AssignStmt)s;
                           Node asLHS = srcmpag.nodeFactory().getNode(as.getLeftOp());
                           asLHS = srcmpag.parameterize(asLHS, e.srcCtxt());
                           asLHS = asLHS.getReplacement();
                           this.addEdge(newObject, asLHS);
                        }

                        pval = this.addInterproceduralAssignment(newObject, tgtParmI, e);
                        this.callAssigns.put(s.getInvokeExpr(), pval);
                        this.callToMethod.put(s.getInvokeExpr(), srcmpag.getMethod());
                     }
                  }
               }
            }
         } else {
            this.addCallTarget(srcmpag, tgtmpag, (Stmt)e.srcUnit(), e.srcCtxt(), e.tgtCtxt(), e);
         }

      }
   }

   public final void addCallTarget(MethodPAG srcmpag, MethodPAG tgtmpag, Stmt s, Context srcContext, Context tgtContext, Edge e) {
      MethodNodeFactory srcnf = srcmpag.nodeFactory();
      MethodNodeFactory tgtnf = tgtmpag.nodeFactory();
      InvokeExpr ie = s.getInvokeExpr();
      boolean virtualCall = this.callAssigns.containsKey(ie);
      int numArgs = ie.getArgCount();

      Node retNode;
      for(int i = 0; i < numArgs; ++i) {
         Value arg = ie.getArg(i);
         if (arg.getType() instanceof RefLikeType && !(arg instanceof NullConstant)) {
            retNode = srcnf.getNode(arg);
            retNode = srcmpag.parameterize(retNode, srcContext);
            retNode = retNode.getReplacement();
            Node parm = tgtnf.caseParm(i);
            parm = tgtmpag.parameterize(parm, tgtContext);
            parm = parm.getReplacement();
            this.addEdge(retNode, parm);
            Pair<Node, Node> pval = this.addInterproceduralAssignment(retNode, parm, e);
            this.callAssigns.put(ie, pval);
            this.callToMethod.put(ie, srcmpag.getMethod());
         }
      }

      Node destNode;
      Pair pval;
      if (ie instanceof InstanceInvokeExpr) {
         InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
         destNode = srcnf.getNode(iie.getBase());
         destNode = srcmpag.parameterize(destNode, srcContext);
         destNode = destNode.getReplacement();
         retNode = tgtnf.caseThis();
         retNode = tgtmpag.parameterize(retNode, tgtContext);
         retNode = retNode.getReplacement();
         this.addEdge(destNode, retNode);
         pval = this.addInterproceduralAssignment(destNode, retNode, e);
         this.callAssigns.put(ie, pval);
         this.callToMethod.put(ie, srcmpag.getMethod());
         if (virtualCall && !this.virtualCallsToReceivers.containsKey(ie)) {
            this.virtualCallsToReceivers.put(ie, destNode);
         }
      }

      if (s instanceof AssignStmt) {
         Value dest = ((AssignStmt)s).getLeftOp();
         if (dest.getType() instanceof RefLikeType && !(dest instanceof NullConstant)) {
            destNode = srcnf.getNode(dest);
            destNode = srcmpag.parameterize(destNode, srcContext);
            destNode = destNode.getReplacement();
            retNode = tgtnf.caseRet();
            retNode = tgtmpag.parameterize(retNode, tgtContext);
            retNode = retNode.getReplacement();
            this.addEdge(retNode, destNode);
            pval = this.addInterproceduralAssignment(retNode, destNode, e);
            this.callAssigns.put(ie, pval);
            this.callToMethod.put(ie, srcmpag.getMethod());
         }
      }

   }

   public void cleanPAG() {
      this.simple.clear();
      this.load.clear();
      this.store.clear();
      this.alloc.clear();
      this.simpleInv.clear();
      this.loadInv.clear();
      this.storeInv.clear();
      this.allocInv.clear();
   }

   protected <K extends Node> boolean addToMap(Map<K, Object> m, K key, Node value) {
      Object valueList = m.get(key);
      if (valueList == null) {
         m.put(key, valueList = new HashSet(4));
      } else if (!(valueList instanceof Set)) {
         Node[] ar = (Node[])((Node[])valueList);
         HashSet<Node> vl = new HashSet(ar.length + 4);
         m.put(key, vl);
         Node[] var7 = ar;
         int var8 = ar.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Node element = var7[var9];
            vl.add(element);
         }

         return vl.add(value);
      }

      return ((Set)valueList).add(value);
   }

   public GlobalNodeFactory nodeFactory() {
      return this.nodeFactory;
   }
}
