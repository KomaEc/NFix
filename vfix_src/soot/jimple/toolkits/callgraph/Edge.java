package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;

public final class Edge {
   private MethodOrMethodContext src;
   private Unit srcUnit;
   private MethodOrMethodContext tgt;
   private Kind kind;
   private Edge nextByUnit = this;
   private Edge prevByUnit = this;
   private Edge nextBySrc = this;
   private Edge prevBySrc = this;
   private Edge nextByTgt = this;
   private Edge prevByTgt = this;

   public SootMethod src() {
      return this.src == null ? null : this.src.method();
   }

   public Context srcCtxt() {
      return this.src == null ? null : this.src.context();
   }

   public MethodOrMethodContext getSrc() {
      return this.src;
   }

   public Unit srcUnit() {
      return this.srcUnit;
   }

   public Stmt srcStmt() {
      return (Stmt)this.srcUnit;
   }

   public SootMethod tgt() {
      return this.tgt.method();
   }

   public Context tgtCtxt() {
      return this.tgt.context();
   }

   public MethodOrMethodContext getTgt() {
      return this.tgt;
   }

   public Kind kind() {
      return this.kind;
   }

   public Edge(MethodOrMethodContext src, Unit srcUnit, MethodOrMethodContext tgt, Kind kind) {
      this.src = src;
      this.srcUnit = srcUnit;
      this.tgt = tgt;
      this.kind = kind;
   }

   public Edge(MethodOrMethodContext src, Stmt srcUnit, MethodOrMethodContext tgt) {
      this.kind = ieToKind(srcUnit.getInvokeExpr());
      this.src = src;
      this.srcUnit = srcUnit;
      this.tgt = tgt;
   }

   public static Kind ieToKind(InvokeExpr ie) {
      if (ie instanceof VirtualInvokeExpr) {
         return Kind.VIRTUAL;
      } else if (ie instanceof SpecialInvokeExpr) {
         return Kind.SPECIAL;
      } else if (ie instanceof InterfaceInvokeExpr) {
         return Kind.INTERFACE;
      } else if (ie instanceof StaticInvokeExpr) {
         return Kind.STATIC;
      } else {
         throw new RuntimeException();
      }
   }

   public boolean isExplicit() {
      return this.kind.isExplicit();
   }

   public boolean isInstance() {
      return this.kind.isInstance();
   }

   public boolean isVirtual() {
      return this.kind.isVirtual();
   }

   public boolean isSpecial() {
      return this.kind.isSpecial();
   }

   public boolean isClinit() {
      return this.kind.isClinit();
   }

   public boolean isStatic() {
      return this.kind.isStatic();
   }

   public boolean isThreadRunCall() {
      return this.kind.isThread();
   }

   public boolean passesParameters() {
      return this.kind.passesParameters();
   }

   public int hashCode() {
      int ret = this.tgt.hashCode() + 20 + this.kind.getNumber();
      if (this.src != null) {
         ret = ret * 32 + this.src.hashCode();
      }

      if (this.srcUnit != null) {
         ret = ret * 32 + this.srcUnit.hashCode();
      }

      return ret;
   }

   public boolean equals(Object other) {
      Edge o = (Edge)other;
      if (o == null) {
         return false;
      } else if (o.src != this.src) {
         return false;
      } else if (o.srcUnit != this.srcUnit) {
         return false;
      } else if (o.tgt != this.tgt) {
         return false;
      } else {
         return o.kind == this.kind;
      }
   }

   public String toString() {
      return this.kind.toString() + " edge: " + this.srcUnit + " in " + this.src + " ==> " + this.tgt;
   }

   void insertAfterByUnit(Edge other) {
      this.nextByUnit = other.nextByUnit;
      this.nextByUnit.prevByUnit = this;
      other.nextByUnit = this;
      this.prevByUnit = other;
   }

   void insertAfterBySrc(Edge other) {
      this.nextBySrc = other.nextBySrc;
      this.nextBySrc.prevBySrc = this;
      other.nextBySrc = this;
      this.prevBySrc = other;
   }

   void insertAfterByTgt(Edge other) {
      this.nextByTgt = other.nextByTgt;
      this.nextByTgt.prevByTgt = this;
      other.nextByTgt = this;
      this.prevByTgt = other;
   }

   void insertBeforeByUnit(Edge other) {
      this.prevByUnit = other.prevByUnit;
      this.prevByUnit.nextByUnit = this;
      other.prevByUnit = this;
      this.nextByUnit = other;
   }

   void insertBeforeBySrc(Edge other) {
      this.prevBySrc = other.prevBySrc;
      this.prevBySrc.nextBySrc = this;
      other.prevBySrc = this;
      this.nextBySrc = other;
   }

   void insertBeforeByTgt(Edge other) {
      this.prevByTgt = other.prevByTgt;
      this.prevByTgt.nextByTgt = this;
      other.prevByTgt = this;
      this.nextByTgt = other;
   }

   void remove() {
      this.nextByUnit.prevByUnit = this.prevByUnit;
      this.prevByUnit.nextByUnit = this.nextByUnit;
      this.nextBySrc.prevBySrc = this.prevBySrc;
      this.prevBySrc.nextBySrc = this.nextBySrc;
      this.nextByTgt.prevByTgt = this.prevByTgt;
      this.prevByTgt.nextByTgt = this.nextByTgt;
   }

   Edge nextByUnit() {
      return this.nextByUnit;
   }

   Edge nextBySrc() {
      return this.nextBySrc;
   }

   Edge nextByTgt() {
      return this.nextByTgt;
   }

   Edge prevByUnit() {
      return this.prevByUnit;
   }

   Edge prevBySrc() {
      return this.prevBySrc;
   }

   Edge prevByTgt() {
      return this.prevByTgt;
   }
}
