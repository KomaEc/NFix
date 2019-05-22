package polyglot.visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import polyglot.ast.Binary;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassMember;
import polyglot.ast.CodeDecl;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.Initializer;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.frontend.Job;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;

public class InitChecker extends DataFlow {
   protected InitChecker.ClassBodyInfo currCBI = null;
   protected static final DataFlow.Item BOTTOM = new DataFlow.Item() {
      public boolean equals(Object i) {
         return i == this;
      }

      public int hashCode() {
         return -5826349;
      }
   };

   public InitChecker(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf, true, false);
   }

   protected FlowGraph initGraph(CodeDecl code, Term root) {
      this.currCBI.currCodeDecl = code;
      return new FlowGraph(root, this.forward);
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      if (n instanceof ClassBody) {
         this.setupClassBody((ClassBody)n);
      }

      return super.enterCall(n);
   }

   public Node leaveCall(Node n) throws SemanticException {
      if (n instanceof ConstructorDecl) {
         this.currCBI.allConstructors.add(n);
         return n;
      } else {
         if (n instanceof ClassBody) {
            Iterator iter = this.currCBI.allConstructors.iterator();

            while(iter.hasNext()) {
               ConstructorDecl cd = (ConstructorDecl)iter.next();
               this.dataflow(cd);
            }

            this.checkStaticFinalFieldsInit((ClassBody)n);
            this.checkNonStaticFinalFieldsInit((ClassBody)n);
            if (this.currCBI.outer != null) {
               this.currCBI.outer.localsUsedInClassBodies.put(n, this.currCBI.outerLocalsUsed);
            }

            this.currCBI = this.currCBI.outer;
         }

         return super.leaveCall(n);
      }
   }

   protected void setupClassBody(ClassBody n) throws SemanticException {
      InitChecker.ClassBodyInfo newCDI = new InitChecker.ClassBodyInfo();
      newCDI.outer = this.currCBI;
      this.currCBI = newCDI;
      Iterator classMembers = n.members().iterator();

      while(classMembers.hasNext()) {
         ClassMember cm = (ClassMember)classMembers.next();
         if (cm instanceof FieldDecl) {
            FieldDecl fd = (FieldDecl)cm;
            if (fd.flags().isFinal()) {
               InitChecker.MinMaxInitCount initCount;
               if (fd.init() != null) {
                  initCount = new InitChecker.MinMaxInitCount(InitChecker.InitCount.ONE, InitChecker.InitCount.ONE);
                  if (this.currCBI.outer != null) {
                     this.dataflow(fd.init());
                  }
               } else {
                  initCount = new InitChecker.MinMaxInitCount(InitChecker.InitCount.ZERO, InitChecker.InitCount.ZERO);
               }

               newCDI.currClassFinalFieldInitCounts.put(fd.fieldInstance(), initCount);
            }
         }
      }

   }

   protected void checkStaticFinalFieldsInit(ClassBody cb) throws SemanticException {
      Iterator iter = this.currCBI.currClassFinalFieldInitCounts.entrySet().iterator();

      while(iter.hasNext()) {
         Entry e = (Entry)iter.next();
         if (e.getKey() instanceof FieldInstance) {
            FieldInstance fi = (FieldInstance)e.getKey();
            if (fi.flags().isStatic() && fi.flags().isFinal()) {
               InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)e.getValue();
               if (InitChecker.InitCount.ZERO.equals(initCount.getMin())) {
                  throw new SemanticException("field \"" + fi.name() + "\" might not have been initialized", cb.position());
               }
            }
         }
      }

   }

   protected void checkNonStaticFinalFieldsInit(ClassBody cb) throws SemanticException {
      Iterator iter = this.currCBI.currClassFinalFieldInitCounts.keySet().iterator();

      while(true) {
         FieldInstance fi;
         do {
            do {
               if (!iter.hasNext()) {
                  return;
               }

               fi = (FieldInstance)iter.next();
            } while(!fi.flags().isFinal());
         } while(fi.flags().isStatic());

         boolean fieldInitializedBeforeConstructors = false;
         InitChecker.MinMaxInitCount ic = (InitChecker.MinMaxInitCount)this.currCBI.currClassFinalFieldInitCounts.get(fi);
         if (ic != null && !InitChecker.InitCount.ZERO.equals(ic.getMin())) {
            fieldInitializedBeforeConstructors = true;
         }

         Iterator iter2 = this.currCBI.allConstructors.iterator();

         while(iter2.hasNext()) {
            ConstructorDecl cd = (ConstructorDecl)iter2.next();
            ConstructorInstance ciStart = cd.constructorInstance();
            ConstructorInstance ci = ciStart;

            boolean isInitialized;
            for(isInitialized = fieldInitializedBeforeConstructors; ci != null; ci = (ConstructorInstance)this.currCBI.constructorCalls.get(ci)) {
               Set s = (Set)this.currCBI.fieldsConstructorInitializes.get(ci);
               if (s != null && s.contains(fi)) {
                  if (isInitialized) {
                     throw new SemanticException("field \"" + fi.name() + "\" might have already been initialized", cd.position());
                  }

                  isInitialized = true;
               }
            }

            if (!isInitialized) {
               throw new SemanticException("field \"" + fi.name() + "\" might not have been initialized", ciStart.position());
            }
         }
      }
   }

   protected void dataflow(Expr root) throws SemanticException {
      FlowGraph g = new FlowGraph(root, this.forward);
      CFGBuilder v = this.createCFGBuilder(this.ts, g);
      v.visitGraph();
      this.dataflow(g);
      this.post(g, root);
   }

   public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
      return (DataFlow.Item)(node == graph.startNode() ? this.createInitDFI() : BOTTOM);
   }

   private InitChecker.DataFlowItem createInitDFI() {
      return new InitChecker.DataFlowItem(new HashMap(this.currCBI.currClassFinalFieldInitCounts));
   }

   protected DataFlow.Item confluence(List items, List itemKeys, Term node, FlowGraph graph) {
      if (!(node instanceof Initializer) && !(node instanceof ConstructorDecl)) {
         return this.confluence(items, node, graph);
      } else {
         List filtered = this.filterItemsNonException(items, itemKeys);
         if (filtered.isEmpty()) {
            return this.createInitDFI();
         } else {
            return filtered.size() == 1 ? (DataFlow.Item)filtered.get(0) : this.confluence(filtered, node, graph);
         }
      }
   }

   public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
      Iterator iter = inItems.iterator();
      HashMap m = null;

      while(true) {
         while(true) {
            DataFlow.Item itm;
            do {
               if (!iter.hasNext()) {
                  if (m == null) {
                     return BOTTOM;
                  }

                  return new InitChecker.DataFlowItem(m);
               }

               itm = (DataFlow.Item)iter.next();
            } while(itm == BOTTOM);

            if (m == null) {
               m = new HashMap(((InitChecker.DataFlowItem)itm).initStatus);
            } else {
               Map n = ((InitChecker.DataFlowItem)itm).initStatus;
               Iterator iter2 = n.entrySet().iterator();

               while(iter2.hasNext()) {
                  Entry entry = (Entry)iter2.next();
                  VarInstance v = (VarInstance)entry.getKey();
                  InitChecker.MinMaxInitCount initCount1 = (InitChecker.MinMaxInitCount)m.get(v);
                  InitChecker.MinMaxInitCount initCount2 = (InitChecker.MinMaxInitCount)entry.getValue();
                  m.put(v, InitChecker.MinMaxInitCount.join(initCount1, initCount2));
               }
            }
         }
      }
   }

   protected Map flow(List inItems, List inItemKeys, FlowGraph graph, Term n, Set edgeKeys) {
      return this.flowToBooleanFlow(inItems, inItemKeys, graph, n, edgeKeys);
   }

   public Map flow(DataFlow.Item trueItem, DataFlow.Item falseItem, DataFlow.Item otherItem, FlowGraph graph, Term n, Set succEdgeKeys) {
      DataFlow.Item inItem = this.safeConfluence((DataFlow.Item)trueItem, FlowGraph.EDGE_KEY_TRUE, (DataFlow.Item)falseItem, FlowGraph.EDGE_KEY_FALSE, otherItem, FlowGraph.EDGE_KEY_OTHER, n, graph);
      if (inItem == BOTTOM) {
         return itemToMap(BOTTOM, succEdgeKeys);
      } else {
         InitChecker.DataFlowItem inDFItem = (InitChecker.DataFlowItem)inItem;
         Map ret = null;
         if (n instanceof Formal) {
            ret = this.flowFormal(inDFItem, graph, (Formal)n, succEdgeKeys);
         } else if (n instanceof LocalDecl) {
            ret = this.flowLocalDecl(inDFItem, graph, (LocalDecl)n, succEdgeKeys);
         } else if (n instanceof LocalAssign) {
            ret = this.flowLocalAssign(inDFItem, graph, (LocalAssign)n, succEdgeKeys);
         } else if (n instanceof FieldAssign) {
            ret = this.flowFieldAssign(inDFItem, graph, (FieldAssign)n, succEdgeKeys);
         } else if (n instanceof ConstructorCall) {
            ret = this.flowConstructorCall(inDFItem, graph, (ConstructorCall)n, succEdgeKeys);
         } else if (n instanceof Expr && ((Expr)n).type().isBoolean() && (n instanceof Binary || n instanceof Unary)) {
            if (trueItem == null) {
               trueItem = inDFItem;
            }

            if (falseItem == null) {
               falseItem = inDFItem;
            }

            ret = this.flowBooleanConditions((DataFlow.Item)trueItem, (DataFlow.Item)falseItem, inDFItem, graph, (Expr)n, succEdgeKeys);
         }

         return ret != null ? ret : itemToMap(inItem, succEdgeKeys);
      }
   }

   protected Map flowFormal(InitChecker.DataFlowItem inItem, FlowGraph graph, Formal f, Set succEdgeKeys) {
      Map m = new HashMap(inItem.initStatus);
      m.put(f.localInstance(), new InitChecker.MinMaxInitCount(InitChecker.InitCount.ONE, InitChecker.InitCount.ONE));
      this.currCBI.localDeclarations.add(f.localInstance());
      return itemToMap(new InitChecker.DataFlowItem(m), succEdgeKeys);
   }

   protected Map flowLocalDecl(InitChecker.DataFlowItem inItem, FlowGraph graph, LocalDecl ld, Set succEdgeKeys) {
      Map m = new HashMap(inItem.initStatus);
      InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)m.get(ld.localInstance());
      if (ld.init() != null) {
         initCount = new InitChecker.MinMaxInitCount(InitChecker.InitCount.ONE, InitChecker.InitCount.ONE);
      } else {
         initCount = new InitChecker.MinMaxInitCount(InitChecker.InitCount.ZERO, InitChecker.InitCount.ZERO);
      }

      m.put(ld.localInstance(), initCount);
      this.currCBI.localDeclarations.add(ld.localInstance());
      return itemToMap(new InitChecker.DataFlowItem(m), succEdgeKeys);
   }

   protected Map flowLocalAssign(InitChecker.DataFlowItem inItem, FlowGraph graph, LocalAssign a, Set succEdgeKeys) {
      Local l = (Local)a.left();
      Map m = new HashMap(inItem.initStatus);
      InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)m.get(l.localInstance());
      if (initCount == null) {
         initCount = new InitChecker.MinMaxInitCount(InitChecker.InitCount.ZERO, InitChecker.InitCount.ZERO);
      }

      initCount = new InitChecker.MinMaxInitCount(initCount.getMin().increment(), initCount.getMax().increment());
      m.put(l.localInstance(), initCount);
      return itemToMap(new InitChecker.DataFlowItem(m), succEdgeKeys);
   }

   protected Map flowFieldAssign(InitChecker.DataFlowItem inItem, FlowGraph graph, FieldAssign a, Set succEdgeKeys) {
      Field f = (Field)a.left();
      FieldInstance fi = f.fieldInstance();
      if (fi.flags().isFinal() && this.isFieldsTargetAppropriate(f)) {
         Map m = new HashMap(inItem.initStatus);
         InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)m.get(fi);
         if (initCount != null) {
            initCount = new InitChecker.MinMaxInitCount(initCount.getMin().increment(), initCount.getMax().increment());
            m.put(fi, initCount);
            return itemToMap(new InitChecker.DataFlowItem(m), succEdgeKeys);
         }
      }

      return null;
   }

   protected Map flowConstructorCall(InitChecker.DataFlowItem inItem, FlowGraph graph, ConstructorCall cc, Set succEdgeKeys) {
      if (ConstructorCall.THIS.equals(cc.kind())) {
         this.currCBI.constructorCalls.put(((ConstructorDecl)this.currCBI.currCodeDecl).constructorInstance(), cc.constructorInstance());
      }

      return null;
   }

   protected boolean isFieldsTargetAppropriate(Field f) {
      if (f.fieldInstance().flags().isStatic()) {
         ClassType containingClass = (ClassType)this.currCBI.currCodeDecl.codeInstance().container();
         return containingClass.equals(f.fieldInstance().container());
      } else {
         return f.target() instanceof Special && Special.THIS.equals(((Special)f.target()).kind());
      }
   }

   public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
      InitChecker.DataFlowItem dfIn = (InitChecker.DataFlowItem)inItem;
      if (dfIn == null) {
         dfIn = this.createInitDFI();
      }

      InitChecker.DataFlowItem dfOut = null;
      if (outItems != null && !outItems.isEmpty()) {
         dfOut = (InitChecker.DataFlowItem)outItems.values().iterator().next();
      }

      if (n instanceof Local) {
         this.checkLocal(graph, (Local)n, dfIn, dfOut);
      } else if (n instanceof LocalAssign) {
         this.checkLocalAssign(graph, (LocalAssign)n, dfIn, dfOut);
      } else if (n instanceof FieldAssign) {
         this.checkFieldAssign(graph, (FieldAssign)n, dfIn, dfOut);
      } else if (n instanceof ClassBody) {
         Set localsUsed = (Set)this.currCBI.localsUsedInClassBodies.get(n);
         if (localsUsed != null) {
            this.checkLocalsUsedByInnerClass(graph, (ClassBody)n, localsUsed, dfIn, dfOut);
         }
      }

      if (n == graph.finishNode()) {
         if (this.currCBI.currCodeDecl instanceof Initializer) {
            this.finishInitializer(graph, (Initializer)this.currCBI.currCodeDecl, dfIn, dfOut);
         }

         if (this.currCBI.currCodeDecl instanceof ConstructorDecl) {
            this.finishConstructorDecl(graph, (ConstructorDecl)this.currCBI.currCodeDecl, dfIn, dfOut);
         }
      }

   }

   protected void finishInitializer(FlowGraph graph, Initializer initializer, InitChecker.DataFlowItem dfIn, InitChecker.DataFlowItem dfOut) {
      Iterator iter = dfOut.initStatus.entrySet().iterator();

      while(iter.hasNext()) {
         Entry e = (Entry)iter.next();
         if (e.getKey() instanceof FieldInstance) {
            FieldInstance fi = (FieldInstance)e.getKey();
            if (fi.flags().isFinal()) {
               this.currCBI.currClassFinalFieldInitCounts.put(fi, e.getValue());
            }
         }
      }

   }

   protected void finishConstructorDecl(FlowGraph graph, ConstructorDecl cd, InitChecker.DataFlowItem dfIn, InitChecker.DataFlowItem dfOut) {
      ConstructorInstance ci = cd.constructorInstance();
      Set s = new HashSet();
      Iterator iter = dfOut.initStatus.entrySet().iterator();

      while(true) {
         FieldInstance fi;
         InitChecker.MinMaxInitCount initCount;
         InitChecker.MinMaxInitCount origInitCount;
         do {
            do {
               Entry e;
               do {
                  do {
                     do {
                        if (!iter.hasNext()) {
                           if (!s.isEmpty()) {
                              this.currCBI.fieldsConstructorInitializes.put(ci, s);
                           }

                           return;
                        }

                        e = (Entry)iter.next();
                     } while(!(e.getKey() instanceof FieldInstance));
                  } while(!((FieldInstance)e.getKey()).flags().isFinal());
               } while(((FieldInstance)e.getKey()).flags().isStatic());

               fi = (FieldInstance)e.getKey();
               initCount = (InitChecker.MinMaxInitCount)e.getValue();
               origInitCount = (InitChecker.MinMaxInitCount)this.currCBI.currClassFinalFieldInitCounts.get(fi);
            } while(initCount.getMin() != InitChecker.InitCount.ONE);
         } while(origInitCount != null && origInitCount.getMin() != InitChecker.InitCount.ZERO);

         s.add(fi);
      }
   }

   protected void checkLocal(FlowGraph graph, Local l, InitChecker.DataFlowItem dfIn, InitChecker.DataFlowItem dfOut) throws SemanticException {
      if (!this.currCBI.localDeclarations.contains(l.localInstance())) {
         this.currCBI.outerLocalsUsed.add(l.localInstance());
      } else {
         InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)dfIn.initStatus.get(l.localInstance());
         if (initCount != null && InitChecker.InitCount.ZERO.equals(initCount.getMin()) && l.reachable()) {
            throw new SemanticException("Local variable \"" + l.name() + "\" may not have been initialized", l.position());
         }
      }

   }

   protected void checkLocalAssign(FlowGraph graph, LocalAssign a, InitChecker.DataFlowItem dfIn, InitChecker.DataFlowItem dfOut) throws SemanticException {
      LocalInstance li = ((Local)a.left()).localInstance();
      if (!this.currCBI.localDeclarations.contains(li)) {
         throw new SemanticException("Final local variable \"" + li.name() + "\" cannot be assigned to in an inner class.", a.position());
      } else {
         InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)dfOut.initStatus.get(li);
         if (li.flags().isFinal() && InitChecker.InitCount.MANY.equals(initCount.getMax())) {
            throw new SemanticException("variable \"" + li.name() + "\" might already have been assigned to", a.position());
         }
      }
   }

   protected void checkFieldAssign(FlowGraph graph, FieldAssign a, InitChecker.DataFlowItem dfIn, InitChecker.DataFlowItem dfOut) throws SemanticException {
      Field f = (Field)a.left();
      FieldInstance fi = f.fieldInstance();
      if (fi.flags().isFinal()) {
         if (!(this.currCBI.currCodeDecl instanceof ConstructorDecl) && !(this.currCBI.currCodeDecl instanceof Initializer) || !this.isFieldsTargetAppropriate(f)) {
            throw new SemanticException("Cannot assign a value to final field \"" + fi.name() + "\"", a.position());
         }

         InitChecker.MinMaxInitCount initCount = (InitChecker.MinMaxInitCount)dfOut.initStatus.get(fi);
         if (InitChecker.InitCount.MANY.equals(initCount.getMax())) {
            throw new SemanticException("field \"" + fi.name() + "\" might already have been assigned to", a.position());
         }
      }

   }

   protected void checkLocalsUsedByInnerClass(FlowGraph graph, ClassBody cb, Set localsUsed, InitChecker.DataFlowItem dfIn, InitChecker.DataFlowItem dfOut) throws SemanticException {
      Iterator iter = localsUsed.iterator();

      LocalInstance li;
      InitChecker.MinMaxInitCount initCount;
      label24:
      do {
         while(iter.hasNext()) {
            li = (LocalInstance)iter.next();
            initCount = (InitChecker.MinMaxInitCount)dfOut.initStatus.get(li);
            if (this.currCBI.localDeclarations.contains(li)) {
               continue label24;
            }

            this.currCBI.outerLocalsUsed.add(li);
         }

         return;
      } while(initCount != null && !InitChecker.InitCount.ZERO.equals(initCount.getMin()));

      throw new SemanticException("Local variable \"" + li.name() + "\" must be initialized before the class " + "declaration.", cb.position());
   }

   static class DataFlowItem extends DataFlow.Item {
      Map initStatus;

      DataFlowItem(Map m) {
         this.initStatus = Collections.unmodifiableMap(m);
      }

      public String toString() {
         return this.initStatus.toString();
      }

      public boolean equals(Object o) {
         return o instanceof InitChecker.DataFlowItem ? this.initStatus.equals(((InitChecker.DataFlowItem)o).initStatus) : false;
      }

      public int hashCode() {
         return this.initStatus.hashCode();
      }
   }

   protected static class MinMaxInitCount {
      protected InitChecker.InitCount min;
      protected InitChecker.InitCount max;

      MinMaxInitCount(InitChecker.InitCount min, InitChecker.InitCount max) {
         this.min = min;
         this.max = max;
      }

      InitChecker.InitCount getMin() {
         return this.min;
      }

      InitChecker.InitCount getMax() {
         return this.max;
      }

      public int hashCode() {
         return this.min.hashCode() * 4 + this.max.hashCode();
      }

      public String toString() {
         return "[ min: " + this.min + "; max: " + this.max + " ]";
      }

      public boolean equals(Object o) {
         if (!(o instanceof InitChecker.MinMaxInitCount)) {
            return false;
         } else {
            return this.min.equals(((InitChecker.MinMaxInitCount)o).min) && this.max.equals(((InitChecker.MinMaxInitCount)o).max);
         }
      }

      static InitChecker.MinMaxInitCount join(InitChecker.MinMaxInitCount initCount1, InitChecker.MinMaxInitCount initCount2) {
         if (initCount1 == null) {
            return initCount2;
         } else if (initCount2 == null) {
            return initCount1;
         } else {
            InitChecker.MinMaxInitCount t = new InitChecker.MinMaxInitCount(InitChecker.InitCount.min(initCount1.getMin(), initCount2.getMin()), InitChecker.InitCount.max(initCount1.getMax(), initCount2.getMax()));
            return t;
         }
      }
   }

   protected static class InitCount {
      static InitChecker.InitCount ZERO = new InitChecker.InitCount(0);
      static InitChecker.InitCount ONE = new InitChecker.InitCount(1);
      static InitChecker.InitCount MANY = new InitChecker.InitCount(2);
      protected int count;

      protected InitCount(int i) {
         this.count = i;
      }

      public int hashCode() {
         return this.count;
      }

      public boolean equals(Object o) {
         if (o instanceof InitChecker.InitCount) {
            return this.count == ((InitChecker.InitCount)o).count;
         } else {
            return false;
         }
      }

      public String toString() {
         if (this.count == 0) {
            return "0";
         } else if (this.count == 1) {
            return "1";
         } else if (this.count == 2) {
            return "many";
         } else {
            throw new RuntimeException("Unexpected value for count");
         }
      }

      public InitChecker.InitCount increment() {
         return this.count == 0 ? ONE : MANY;
      }

      public static InitChecker.InitCount min(InitChecker.InitCount a, InitChecker.InitCount b) {
         if (!ZERO.equals(a) && !ZERO.equals(b)) {
            return !ONE.equals(a) && !ONE.equals(b) ? MANY : ONE;
         } else {
            return ZERO;
         }
      }

      public static InitChecker.InitCount max(InitChecker.InitCount a, InitChecker.InitCount b) {
         if (!MANY.equals(a) && !MANY.equals(b)) {
            return !ONE.equals(a) && !ONE.equals(b) ? ZERO : ONE;
         } else {
            return MANY;
         }
      }
   }

   protected static class ClassBodyInfo {
      InitChecker.ClassBodyInfo outer = null;
      CodeDecl currCodeDecl = null;
      Map currClassFinalFieldInitCounts = new HashMap();
      List allConstructors = new ArrayList();
      Map constructorCalls = new HashMap();
      Map fieldsConstructorInitializes = new HashMap();
      Set outerLocalsUsed = new HashSet();
      Map localsUsedInClassBodies = new HashMap();
      Set localDeclarations = new HashSet();
   }
}
