package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.tagkit.ThrowCreatedByCompilerTag;

public class SynchronizedStmt extends Stmt implements Cloneable, FinallyHost {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected Map monitor_Body_values;
   protected boolean exceptionRanges_computed = false;
   protected ArrayList exceptionRanges_value;
   protected boolean label_begin_computed = false;
   protected soot.jimple.Stmt label_begin_value;
   protected boolean label_end_computed = false;
   protected soot.jimple.Stmt label_end_value;
   protected boolean label_finally_computed = false;
   protected soot.jimple.Stmt label_finally_value;
   protected boolean label_finally_block_computed = false;
   protected soot.jimple.Stmt label_finally_block_value;
   protected boolean label_exception_handler_computed = false;
   protected soot.jimple.Stmt label_exception_handler_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.monitor_Body_values = null;
      this.exceptionRanges_computed = false;
      this.exceptionRanges_value = null;
      this.label_begin_computed = false;
      this.label_begin_value = null;
      this.label_end_computed = false;
      this.label_end_value = null;
      this.label_finally_computed = false;
      this.label_finally_value = null;
      this.label_finally_block_computed = false;
      this.label_finally_block_value = null;
      this.label_exception_handler_computed = false;
      this.label_exception_handler_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public SynchronizedStmt clone() throws CloneNotSupportedException {
      SynchronizedStmt node = (SynchronizedStmt)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.monitor_Body_values = null;
      node.exceptionRanges_computed = false;
      node.exceptionRanges_value = null;
      node.label_begin_computed = false;
      node.label_begin_value = null;
      node.label_end_computed = false;
      node.label_end_value = null;
      node.label_finally_computed = false;
      node.label_finally_value = null;
      node.label_finally_block_computed = false;
      node.label_finally_block_value = null;
      node.label_exception_handler_computed = false;
      node.label_exception_handler_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SynchronizedStmt copy() {
      try {
         SynchronizedStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public SynchronizedStmt fullCopy() {
      SynchronizedStmt tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public void collectFinally(Stmt branchStmt, ArrayList list) {
      list.add(this);
      super.collectFinally(branchStmt, list);
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("synchronized(");
      this.getExpr().toString(s);
      s.append(") ");
      this.getBlock().toString(s);
   }

   public void typeCheck() {
      TypeDecl type = this.getExpr().type();
      if (!type.isReferenceType() || type.isNull()) {
         this.error("*** The type of the expression must be a reference");
      }

   }

   public void emitFinallyCode(Body b) {
      b.setLine(this);
      b.add(b.newExitMonitorStmt(this.monitor(b), this));
   }

   public void jimplify2(Body b) {
      b.setLine(this);
      b.add(b.newEnterMonitorStmt(this.monitor(b), this));
      b.addLabel(this.label_begin());
      this.exceptionRanges().add(this.label_begin());
      this.getBlock().jimplify2(b);
      if (this.getBlock().canCompleteNormally()) {
         this.emitFinallyCode(b);
         b.add(b.newGotoStmt(this.label_end(), this));
      }

      b.addLabel(this.label_exception_handler());
      Local l = b.newTemp(this.typeThrowable().getSootType());
      b.add(b.newIdentityStmt(l, b.newCaughtExceptionRef(this), this));
      this.emitFinallyCode(b);
      b.addLabel(this.label_end());
      soot.jimple.Stmt throwStmt = b.newThrowStmt(l, this);
      throwStmt.addTag(new ThrowCreatedByCompilerTag());
      b.add(throwStmt);
      Iterator iter = this.exceptionRanges().iterator();

      while(iter.hasNext()) {
         soot.jimple.Stmt stmtBegin = (soot.jimple.Stmt)iter.next();
         soot.jimple.Stmt stmtEnd;
         if (iter.hasNext()) {
            stmtEnd = (soot.jimple.Stmt)iter.next();
         } else {
            stmtEnd = this.label_end();
         }

         if (stmtBegin != stmtEnd) {
            b.addTrap(this.typeThrowable(), stmtBegin, stmtEnd, this.label_exception_handler());
         }
      }

   }

   public SynchronizedStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public SynchronizedStmt(Expr p0, Block p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setExpr(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(0);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setBlock(Block node) {
      this.setChild(node, 1);
   }

   public Block getBlock() {
      return (Block)this.getChild(1);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(1);
   }

   public boolean isDAafter(Variable v) {
      if (this.isDAafter_Variable_values == null) {
         this.isDAafter_Variable_values = new HashMap(4);
      }

      if (this.isDAafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafter_Variable_value = this.isDAafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, isDAafter_Variable_value);
         }

         return isDAafter_Variable_value;
      }
   }

   private boolean isDAafter_compute(Variable v) {
      return this.getBlock().isDAafter(v);
   }

   public boolean isDUafterFinally(Variable v) {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isDAafterFinally(Variable v) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isDUafter(Variable v) {
      if (this.isDUafter_Variable_values == null) {
         this.isDUafter_Variable_values = new HashMap(4);
      }

      if (this.isDUafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafter_Variable_value = this.isDUafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, isDUafter_Variable_value);
         }

         return isDUafter_Variable_value;
      }
   }

   private boolean isDUafter_compute(Variable v) {
      return this.getBlock().isDUafter(v);
   }

   public boolean canCompleteNormally() {
      if (this.canCompleteNormally_computed) {
         return this.canCompleteNormally_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.canCompleteNormally_value = this.canCompleteNormally_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.canCompleteNormally_computed = true;
         }

         return this.canCompleteNormally_value;
      }
   }

   private boolean canCompleteNormally_compute() {
      return this.getBlock().canCompleteNormally();
   }

   public Local monitor(Body b) {
      if (this.monitor_Body_values == null) {
         this.monitor_Body_values = new HashMap(4);
      }

      if (this.monitor_Body_values.containsKey(b)) {
         return (Local)this.monitor_Body_values.get(b);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Local monitor_Body_value = this.monitor_compute(b);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.monitor_Body_values.put(b, monitor_Body_value);
         }

         return monitor_Body_value;
      }
   }

   private Local monitor_compute(Body b) {
      return b.newTemp(this.getExpr().eval(b));
   }

   public boolean needsFinallyTrap() {
      ASTNode$State state = this.state();
      return this.enclosedByExceptionHandler();
   }

   public ArrayList exceptionRanges() {
      if (this.exceptionRanges_computed) {
         return this.exceptionRanges_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.exceptionRanges_value = this.exceptionRanges_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.exceptionRanges_computed = true;
         }

         return this.exceptionRanges_value;
      }
   }

   private ArrayList exceptionRanges_compute() {
      return new ArrayList();
   }

   public soot.jimple.Stmt label_begin() {
      if (this.label_begin_computed) {
         return this.label_begin_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_begin_value = this.label_begin_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_begin_computed = true;
         }

         return this.label_begin_value;
      }
   }

   private soot.jimple.Stmt label_begin_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt label_end() {
      if (this.label_end_computed) {
         return this.label_end_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_end_value = this.label_end_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_end_computed = true;
         }

         return this.label_end_value;
      }
   }

   private soot.jimple.Stmt label_end_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt label_finally() {
      if (this.label_finally_computed) {
         return this.label_finally_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_finally_value = this.label_finally_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_finally_computed = true;
         }

         return this.label_finally_value;
      }
   }

   private soot.jimple.Stmt label_finally_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt label_finally_block() {
      if (this.label_finally_block_computed) {
         return this.label_finally_block_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_finally_block_value = this.label_finally_block_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_finally_block_computed = true;
         }

         return this.label_finally_block_value;
      }
   }

   private soot.jimple.Stmt label_finally_block_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt label_exception_handler() {
      if (this.label_exception_handler_computed) {
         return this.label_exception_handler_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_exception_handler_value = this.label_exception_handler_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_exception_handler_computed = true;
         }

         return this.label_exception_handler_value;
      }
   }

   private soot.jimple.Stmt label_exception_handler_compute() {
      return this.newLabel();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getBlock().modifiedInScope(var);
   }

   public boolean enclosedByExceptionHandler() {
      ASTNode$State state = this.state();
      boolean enclosedByExceptionHandler_value = this.getParent().Define_boolean_enclosedByExceptionHandler(this, (ASTNode)null);
      return enclosedByExceptionHandler_value;
   }

   public TypeDecl typeThrowable() {
      ASTNode$State state = this.state();
      TypeDecl typeThrowable_value = this.getParent().Define_TypeDecl_typeThrowable(this, (ASTNode)null);
      return typeThrowable_value;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.getExpr().isDAafter(v);
      } else {
         return caller == this.getExprNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.getExpr().isDUafter(v);
      } else {
         return caller == this.getExprNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.reachable() : this.getParent().Define_boolean_reachable(this, caller);
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.reachable() : this.getParent().Define_boolean_reportUnreachable(this, caller);
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? true : this.getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
   }

   public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.exceptionRanges() : this.getParent().Define_ArrayList_exceptionRanges(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
