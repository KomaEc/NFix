package soot.shimple.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DivExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.shimple.AbstractShimpleValueSwitch;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.CompleteBlockGraph;
import soot.toolkits.graph.Orderer;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.util.Switch;

public class ValueGraph {
   protected Map<Value, ValueGraph.Node> localToNode;
   protected Map<ValueGraph.Node, Value> nodeToLocal;
   protected List<ValueGraph.Node> nodeList;
   protected int currentNodeNumber;

   public ValueGraph(BlockGraph cfg) {
      if (!(cfg.getBody() instanceof ShimpleBody)) {
         throw new RuntimeException("ValueGraph requires SSA form");
      } else {
         this.localToNode = new HashMap();
         this.nodeToLocal = new HashMap();
         this.nodeList = new ArrayList();
         this.currentNodeNumber = 0;
         Orderer<Block> pto = new PseudoTopologicalOrderer();
         List<Block> blocks = pto.newList(cfg, false);
         Iterator blocksIt = blocks.iterator();

         while(blocksIt.hasNext()) {
            Block block = (Block)blocksIt.next();
            Iterator blockIt = block.iterator();

            while(blockIt.hasNext()) {
               this.handleStmt((Stmt)blockIt.next());
            }
         }

         blocksIt = this.nodeList.iterator();

         while(blocksIt.hasNext()) {
            ValueGraph.Node node = (ValueGraph.Node)blocksIt.next();
            node.patchStubs();
         }

      }
   }

   protected void handleStmt(Stmt stmt) {
      if (stmt instanceof DefinitionStmt) {
         DefinitionStmt dStmt = (DefinitionStmt)stmt;
         Value leftOp = dStmt.getLeftOp();
         if (leftOp instanceof Local) {
            Value rightOp = dStmt.getRightOp();
            ValueGraph.Node node = this.fetchGraph(rightOp);
            this.localToNode.put(leftOp, node);
            if (!(rightOp instanceof Local) && !node.isStub()) {
               this.nodeToLocal.put(node, leftOp);
            }

         }
      }
   }

   protected ValueGraph.Node fetchNode(Value value) {
      ValueGraph.Node ret = null;
      if (value instanceof Local) {
         ret = this.getNode(value);
         if (ret == null) {
            ret = new ValueGraph.Node(value, true);
         }
      } else {
         ret = new ValueGraph.Node(value);
      }

      return ret;
   }

   protected ValueGraph.Node fetchGraph(Value value) {
      AbstractShimpleValueSwitch vs;
      value.apply(vs = new AbstractShimpleValueSwitch() {
         public void defaultCase(Object object) {
            throw new RuntimeException("Internal error: " + object + " unhandled case.");
         }

         public void caseLocal(Local l) {
            this.setResult(ValueGraph.this.fetchNode(l));
         }

         public void handleConstant(Constant constant) {
            this.setResult(ValueGraph.this.fetchNode(constant));
         }

         public void handleRef(Ref ref) {
            this.setResult(ValueGraph.this.fetchNode(ref));
         }

         public void handleBinop(BinopExpr binop, boolean ordered) {
            ValueGraph.Node nop1 = ValueGraph.this.fetchNode(binop.getOp1());
            ValueGraph.Node nop2 = ValueGraph.this.fetchNode(binop.getOp2());
            List<ValueGraph.Node> children = new ArrayList();
            children.add(nop1);
            children.add(nop2);
            this.setResult(ValueGraph.this.new Node(binop, ordered, children));
         }

         public void handleUnknown(Expr expr) {
            this.setResult(ValueGraph.this.fetchNode(expr));
         }

         public void handleUnop(UnopExpr unop) {
            ValueGraph.Node nop = ValueGraph.this.fetchNode(unop.getOp());
            List<ValueGraph.Node> child = Collections.singletonList(nop);
            this.setResult(ValueGraph.this.new Node(unop, true, child));
         }

         public void caseFloatConstant(FloatConstant v) {
            this.handleConstant(v);
         }

         public void caseIntConstant(IntConstant v) {
            this.handleConstant(v);
         }

         public void caseLongConstant(LongConstant v) {
            this.handleConstant(v);
         }

         public void caseNullConstant(NullConstant v) {
            this.handleConstant(v);
         }

         public void caseStringConstant(StringConstant v) {
            this.handleConstant(v);
         }

         public void caseArrayRef(ArrayRef v) {
            this.handleRef(v);
         }

         public void caseStaticFieldRef(StaticFieldRef v) {
            this.handleRef(v);
         }

         public void caseInstanceFieldRef(InstanceFieldRef v) {
            this.handleRef(v);
         }

         public void caseParameterRef(ParameterRef v) {
            this.handleRef(v);
         }

         public void caseCaughtExceptionRef(CaughtExceptionRef v) {
            this.handleRef(v);
         }

         public void caseThisRef(ThisRef v) {
            this.handleRef(v);
         }

         public void caseAddExpr(AddExpr v) {
            this.handleBinop(v, false);
         }

         public void caseAndExpr(AndExpr v) {
            this.handleBinop(v, false);
         }

         public void caseCmpExpr(CmpExpr v) {
            this.handleBinop(v, true);
         }

         public void caseCmpgExpr(CmpgExpr v) {
            this.handleBinop(v, true);
         }

         public void caseCmplExpr(CmplExpr v) {
            this.handleBinop(v, true);
         }

         public void caseDivExpr(DivExpr v) {
            this.handleBinop(v, true);
         }

         public void caseEqExpr(EqExpr v) {
            this.handleBinop(v, false);
         }

         public void caseNeExpr(NeExpr v) {
            this.handleBinop(v, false);
         }

         public void caseGeExpr(GeExpr v) {
            this.handleBinop(v, true);
         }

         public void caseGtExpr(GtExpr v) {
            this.handleBinop(v, true);
         }

         public void caseLeExpr(LeExpr v) {
            this.handleBinop(v, true);
         }

         public void caseLtExpr(LtExpr v) {
            this.handleBinop(v, true);
         }

         public void caseMulExpr(MulExpr v) {
            this.handleBinop(v, false);
         }

         public void caseOrExpr(OrExpr v) {
            this.handleBinop(v, false);
         }

         public void caseRemExpr(RemExpr v) {
            this.handleBinop(v, true);
         }

         public void caseShlExpr(ShlExpr v) {
            this.handleBinop(v, true);
         }

         public void caseShrExpr(ShrExpr v) {
            this.handleBinop(v, true);
         }

         public void caseUshrExpr(UshrExpr v) {
            this.handleBinop(v, true);
         }

         public void caseSubExpr(SubExpr v) {
            this.handleBinop(v, true);
         }

         public void caseXorExpr(XorExpr v) {
            this.handleBinop(v, false);
         }

         public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
            this.handleUnknown(v);
         }

         public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
            this.handleUnknown(v);
         }

         public void caseStaticInvokeExpr(StaticInvokeExpr v) {
            this.handleUnknown(v);
         }

         public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
            this.handleUnknown(v);
         }

         public void caseCastExpr(CastExpr v) {
            this.setResult(ValueGraph.this.fetchNode(v.getOp()));
         }

         public void caseInstanceOfExpr(InstanceOfExpr v) {
            ValueGraph.Node nop1 = ValueGraph.this.fetchNode(v.getOp());
            Value op2 = new ValueGraph.TypeValueWrapper(v.getCheckType());
            ValueGraph.Node nop2 = ValueGraph.this.fetchNode(op2);
            List<ValueGraph.Node> children = new ArrayList();
            children.add(nop1);
            children.add(nop2);
            this.setResult(ValueGraph.this.new Node(v, true, children));
         }

         public void caseNewArrayExpr(NewArrayExpr v) {
            this.handleUnknown(v);
         }

         public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
            this.handleUnknown(v);
         }

         public void caseNewExpr(NewExpr v) {
            this.handleUnknown(v);
         }

         public void caseLengthExpr(LengthExpr v) {
            this.handleUnop(v);
         }

         public void caseNegExpr(NegExpr v) {
            this.handleUnop(v);
         }

         public void casePhiExpr(PhiExpr v) {
            List<ValueGraph.Node> children = new ArrayList();
            Iterator argsIt = v.getValues().iterator();

            while(argsIt.hasNext()) {
               Value arg = (Value)argsIt.next();
               children.add(ValueGraph.this.fetchNode(arg));
            }

            this.setResult(ValueGraph.this.new Node(v, true, children));
         }
      });
      return (ValueGraph.Node)vs.getResult();
   }

   public ValueGraph.Node getNode(Value local) {
      return (ValueGraph.Node)this.localToNode.get(local);
   }

   public Collection<ValueGraph.Node> getTopNodes() {
      return this.localToNode.values();
   }

   public Local getLocal(ValueGraph.Node node) {
      return (Local)this.nodeToLocal.get(node);
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer();

      for(int i = 0; i < this.nodeList.size(); ++i) {
         tmp.append(this.nodeList.get(i));
         tmp.append("\n");
      }

      return tmp.toString();
   }

   public static void main(String[] args) {
      Scene.v().loadClassAndSupport(args[0]);
      SootClass sc = Scene.v().getSootClass(args[0]);
      SootMethod sm = sc.getMethod(args[1]);
      Body b = sm.retrieveActiveBody();
      ShimpleBody sb = Shimple.v().newBody(b);
      CompleteBlockGraph cfg = new CompleteBlockGraph(sb);
      ValueGraph vg = new ValueGraph(cfg);
      System.out.println(vg);
   }

   protected static class TypeValueWrapper implements Value {
      protected Type type;

      protected TypeValueWrapper(Type type) {
         this.type = type;
      }

      public List<ValueBox> getUseBoxes() {
         return Collections.emptyList();
      }

      public Type getType() {
         return this.type;
      }

      public Object clone() {
         return new ValueGraph.TypeValueWrapper(this.type);
      }

      public void toString(UnitPrinter up) {
         up.literal("[Wrapped] " + this.type);
      }

      public void apply(Switch sw) {
         throw new RuntimeException("Not Implemented.");
      }

      public boolean equals(Object o) {
         return !(o instanceof ValueGraph.TypeValueWrapper) ? false : this.getType().equals(((ValueGraph.TypeValueWrapper)o).getType());
      }

      public int hashCode() {
         return this.getType().hashCode();
      }

      public boolean equivTo(Object o) {
         return this.equals(o);
      }

      public int equivHashCode() {
         return this.hashCode();
      }
   }

   public class Node {
      protected int nodeNumber;
      protected Value node;
      protected String nodeLabel;
      protected boolean ordered;
      protected List<ValueGraph.Node> children;
      protected boolean stub;

      protected Node(Value local, boolean ignored) {
         this.stub = false;
         this.stub = true;
         this.setNode(local);
      }

      protected void patchStubs() {
         if (this.isStub()) {
            throw new RuntimeException("Assertion failed.");
         } else {
            for(int i = 0; i < this.children.size(); ++i) {
               ValueGraph.Node child = (ValueGraph.Node)this.children.get(i);
               if (child.isStub()) {
                  ValueGraph.Node newChild = (ValueGraph.Node)ValueGraph.this.localToNode.get(child.node);
                  if (newChild == null || newChild.isStub()) {
                     throw new RuntimeException("Assertion failed.");
                  }

                  this.children.set(i, newChild);
               }
            }

         }
      }

      protected void checkIfStub() {
         if (this.isStub()) {
            throw new RuntimeException("Assertion failed:  Attempted operation on invalid node (stub)");
         }
      }

      protected Node(Value node) {
         this(node, true, Collections.emptyList());
      }

      protected Node(Value node, boolean ordered, List<ValueGraph.Node> children) {
         this.stub = false;
         this.setNode(node);
         this.setOrdered(ordered);
         this.setChildren(children);
         this.nodeNumber = ValueGraph.this.currentNodeNumber++;
         this.updateLabel();
         ValueGraph.this.nodeList.add(this.nodeNumber, this);
      }

      protected void setNode(Value node) {
         this.node = node;
      }

      protected void setOrdered(boolean ordered) {
         this.ordered = ordered;
      }

      protected void setChildren(List<ValueGraph.Node> children) {
         this.children = children;
      }

      protected void updateLabel() {
         if (!this.children.isEmpty()) {
            this.nodeLabel = this.node.getClass().getName();
            if (this.node instanceof PhiExpr) {
               this.nodeLabel = this.nodeLabel + ((PhiExpr)this.node).getBlockId();
            }
         } else {
            this.nodeLabel = this.node.toString();
            if (this.node instanceof NewExpr || this.node instanceof NewArrayExpr || this.node instanceof NewMultiArrayExpr || this.node instanceof Ref || this.node instanceof InvokeExpr) {
               this.nodeLabel = this.nodeLabel + " " + this.getNodeNumber();
            }
         }

      }

      public boolean isStub() {
         return this.stub;
      }

      public String getLabel() {
         this.checkIfStub();
         return this.nodeLabel;
      }

      public boolean isOrdered() {
         this.checkIfStub();
         return this.ordered;
      }

      public List<ValueGraph.Node> getChildren() {
         this.checkIfStub();
         return this.children;
      }

      public int getNodeNumber() {
         this.checkIfStub();
         return this.nodeNumber;
      }

      public String toString() {
         this.checkIfStub();
         StringBuffer tmp = new StringBuffer();
         Local local = ValueGraph.this.getLocal(this);
         if (local != null) {
            tmp.append(local.toString());
         }

         tmp.append("\tNode " + this.getNodeNumber() + ": " + this.getLabel());
         List<ValueGraph.Node> children = this.getChildren();
         if (!children.isEmpty()) {
            tmp.append(" [" + (this.isOrdered() ? "ordered" : "unordered") + ": ");

            for(int i = 0; i < children.size(); ++i) {
               if (i != 0) {
                  tmp.append(", ");
               }

               tmp.append(((ValueGraph.Node)children.get(i)).getNodeNumber());
            }

            tmp.append("]");
         }

         return tmp.toString();
      }
   }
}
