package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.Local;
import soot.tagkit.ThrowCreatedByCompilerTag;
import soot.tagkit.TryCatchTag;

public class TryStmt extends Stmt implements Cloneable, FinallyHost {
   protected boolean branches_computed = false;
   protected Collection branches_value;
   protected boolean branchesFromFinally_computed = false;
   protected Collection branchesFromFinally_value;
   protected boolean targetBranches_computed = false;
   protected Collection targetBranches_value;
   protected boolean escapedBranches_computed = false;
   protected Collection escapedBranches_value;
   protected Map isDAafter_Variable_values;
   protected Map isDUbefore_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map catchableException_TypeDecl_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean label_begin_computed = false;
   protected soot.jimple.Stmt label_begin_value;
   protected boolean label_block_end_computed = false;
   protected soot.jimple.Stmt label_block_end_value;
   protected boolean label_end_computed = false;
   protected soot.jimple.Stmt label_end_value;
   protected boolean label_finally_computed = false;
   protected soot.jimple.Stmt label_finally_value;
   protected boolean label_finally_block_computed = false;
   protected soot.jimple.Stmt label_finally_block_value;
   protected boolean label_exception_handler_computed = false;
   protected soot.jimple.Stmt label_exception_handler_value;
   protected boolean label_catch_end_computed = false;
   protected soot.jimple.Stmt label_catch_end_value;
   protected boolean exceptionRanges_computed = false;
   protected ArrayList exceptionRanges_value;
   protected Map handlesException_TypeDecl_values;
   protected boolean typeError_computed = false;
   protected TypeDecl typeError_value;
   protected boolean typeRuntimeException_computed = false;
   protected TypeDecl typeRuntimeException_value;

   public void flushCache() {
      super.flushCache();
      this.branches_computed = false;
      this.branches_value = null;
      this.branchesFromFinally_computed = false;
      this.branchesFromFinally_value = null;
      this.targetBranches_computed = false;
      this.targetBranches_value = null;
      this.escapedBranches_computed = false;
      this.escapedBranches_value = null;
      this.isDAafter_Variable_values = null;
      this.isDUbefore_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.catchableException_TypeDecl_values = null;
      this.canCompleteNormally_computed = false;
      this.label_begin_computed = false;
      this.label_begin_value = null;
      this.label_block_end_computed = false;
      this.label_block_end_value = null;
      this.label_end_computed = false;
      this.label_end_value = null;
      this.label_finally_computed = false;
      this.label_finally_value = null;
      this.label_finally_block_computed = false;
      this.label_finally_block_value = null;
      this.label_exception_handler_computed = false;
      this.label_exception_handler_value = null;
      this.label_catch_end_computed = false;
      this.label_catch_end_value = null;
      this.exceptionRanges_computed = false;
      this.exceptionRanges_value = null;
      this.handlesException_TypeDecl_values = null;
      this.typeError_computed = false;
      this.typeError_value = null;
      this.typeRuntimeException_computed = false;
      this.typeRuntimeException_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public TryStmt clone() throws CloneNotSupportedException {
      TryStmt node = (TryStmt)super.clone();
      node.branches_computed = false;
      node.branches_value = null;
      node.branchesFromFinally_computed = false;
      node.branchesFromFinally_value = null;
      node.targetBranches_computed = false;
      node.targetBranches_value = null;
      node.escapedBranches_computed = false;
      node.escapedBranches_value = null;
      node.isDAafter_Variable_values = null;
      node.isDUbefore_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.catchableException_TypeDecl_values = null;
      node.canCompleteNormally_computed = false;
      node.label_begin_computed = false;
      node.label_begin_value = null;
      node.label_block_end_computed = false;
      node.label_block_end_value = null;
      node.label_end_computed = false;
      node.label_end_value = null;
      node.label_finally_computed = false;
      node.label_finally_value = null;
      node.label_finally_block_computed = false;
      node.label_finally_block_value = null;
      node.label_exception_handler_computed = false;
      node.label_exception_handler_value = null;
      node.label_catch_end_computed = false;
      node.label_catch_end_value = null;
      node.exceptionRanges_computed = false;
      node.exceptionRanges_value = null;
      node.handlesException_TypeDecl_values = null;
      node.typeError_computed = false;
      node.typeError_value = null;
      node.typeRuntimeException_computed = false;
      node.typeRuntimeException_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public TryStmt copy() {
      try {
         TryStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public TryStmt fullCopy() {
      TryStmt tree = this.copy();
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

   public void collectBranches(Collection c) {
      c.addAll(this.escapedBranches());
   }

   public Stmt branchTarget(Stmt branchStmt) {
      return (Stmt)(this.targetBranches().contains(branchStmt) ? this : super.branchTarget(branchStmt));
   }

   public void collectFinally(Stmt branchStmt, ArrayList list) {
      if (this.hasFinally() && !this.branchesFromFinally().contains(branchStmt)) {
         list.add(this);
      }

      if (!this.targetBranches().contains(branchStmt)) {
         super.collectFinally(branchStmt, list);
      }
   }

   protected boolean reachedException(TypeDecl type) {
      boolean found = false;

      int i;
      for(i = 0; i < this.getNumCatchClause() && !found; ++i) {
         if (this.getCatchClause(i).handles(type)) {
            found = true;
         }
      }

      if (!found && (!this.hasFinally() || this.getFinally().canCompleteNormally()) && this.getBlock().reachedException(type)) {
         return true;
      } else {
         for(i = 0; i < this.getNumCatchClause(); ++i) {
            if (this.getCatchClause(i).reachedException(type)) {
               return true;
            }
         }

         return this.hasFinally() && this.getFinally().reachedException(type);
      }
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("try ");
      this.getBlock().toString(s);

      for(int i = 0; i < this.getNumCatchClause(); ++i) {
         s.append(this.indent());
         this.getCatchClause(i).toString(s);
      }

      if (this.hasFinally()) {
         s.append(this.indent());
         s.append("finally ");
         this.getFinally().toString(s);
      }

   }

   public void emitFinallyCode(Body b) {
      if (this.hasFinally()) {
         this.getFinally().flushCaches();
         this.getFinally().jimplify2(b);
      }

   }

   public void jimplify2(Body b) {
      ArrayList ranges = this.exceptionRanges();
      b.addLabel(this.label_begin());
      ranges.add(this.label_begin());
      this.getBlock().jimplify2(b);
      soot.jimple.Stmt label_block_end = null;
      soot.jimple.Stmt label_end = null;
      if (this.getBlock().canCompleteNormally()) {
         if (this.hasFinally() && this.getNumCatchClause() != 0) {
            label_block_end = this.label_block_end();
            b.addLabel(label_block_end);
         }

         this.emitFinallyCode(b);
         b.setLine(this);
         if ((!this.hasFinally() || this.getFinally().canCompleteNormally()) && (this.getNumCatchClause() != 0 || this.hasFinally())) {
            b.add(b.newGotoStmt(label_end = this.label_end(), this));
         }
      }

      int i;
      if (this.getNumCatchClause() != 0) {
         if (label_block_end == null) {
            label_block_end = ((BasicCatch)this.getCatchClause(0)).label();
         }

         ranges.add(label_block_end);
         ranges.add(label_block_end);

         for(i = 0; i < this.getNumCatchClause(); ++i) {
            this.getCatchClause(i).jimplify2(b);
            if (this.getCatchClause(i).getBlock().canCompleteNormally()) {
               b.setLine(this.getCatchClause(i));
               this.endExceptionRange(b, ranges);
               this.emitFinallyCode(b);
               if (!this.hasFinally() || this.getFinally().canCompleteNormally()) {
                  b.add(b.newGotoStmt(label_end = this.label_end(), this));
               }

               this.beginExceptionRange(b, ranges);
            }

            b.setLine(this.getCatchClause(i));
         }
      }

      if (this.hasFinally()) {
         b.addLabel(this.label_exception_handler());
         this.emitExceptionHandler(b);
         b.setLine(this.getFinally());
      }

      if (label_end != null) {
         b.addLabel(label_end);
      }

      soot.jimple.Stmt stmtEnd;
      soot.jimple.Stmt lbl;
      for(i = 0; i < this.getNumCatchClause(); ++i) {
         Iterator iter = ranges.iterator();

         while(iter.hasNext()) {
            stmtEnd = (soot.jimple.Stmt)iter.next();
            lbl = (soot.jimple.Stmt)iter.next();
            if (stmtEnd != lbl) {
               soot.jimple.Stmt lbl = ((BasicCatch)this.getCatchClause(i)).label();
               b.addTrap(((BasicCatch)this.getCatchClause(i)).getParameter().type(), stmtEnd, lbl, lbl);
               this.addFallThroughLabelTag(b, lbl, label_end);
            }

            if (lbl == label_block_end) {
               break;
            }
         }
      }

      if (this.hasFinally()) {
         Iterator iter = ranges.iterator();

         while(iter.hasNext()) {
            soot.jimple.Stmt stmtBegin = (soot.jimple.Stmt)iter.next();
            if (iter.hasNext()) {
               stmtEnd = (soot.jimple.Stmt)iter.next();
            } else {
               stmtEnd = this.label_exception_handler();
            }

            if (stmtBegin != stmtEnd) {
               lbl = this.label_exception_handler();
               b.addTrap(this.typeThrowable(), stmtBegin, stmtEnd, lbl);
               this.addFallThroughLabelTag(b, lbl, label_end);
            }
         }
      }

   }

   protected void addFallThroughLabelTag(Body b, soot.jimple.Stmt handler, soot.jimple.Stmt fallThrough) {
      soot.Body body = b.body;
      TryCatchTag tag = (TryCatchTag)body.getTag("TryCatchTag");
      if (tag == null) {
         tag = new TryCatchTag();
         body.addTag(tag);
      }

      tag.register(handler, fallThrough);
   }

   public void emitExceptionHandler(Body b) {
      Local l = b.newTemp(this.typeThrowable().getSootType());
      b.setLine(this);
      b.add(b.newIdentityStmt(l, b.newCaughtExceptionRef(this), this));
      this.emitFinallyCode(b);
      soot.jimple.Stmt throwStmt = b.newThrowStmt(l, this);
      throwStmt.addTag(new ThrowCreatedByCompilerTag());
      b.add(throwStmt);
   }

   public TryStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new Opt(), 2);
   }

   public TryStmt(Block p0, List<CatchClause> p1, Opt<Block> p2) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setBlock(Block node) {
      this.setChild(node, 0);
   }

   public Block getBlock() {
      return (Block)this.getChild(0);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(0);
   }

   public void setCatchClauseList(List<CatchClause> list) {
      this.setChild(list, 1);
   }

   public int getNumCatchClause() {
      return this.getCatchClauseList().getNumChild();
   }

   public int getNumCatchClauseNoTransform() {
      return this.getCatchClauseListNoTransform().getNumChildNoTransform();
   }

   public CatchClause getCatchClause(int i) {
      return (CatchClause)this.getCatchClauseList().getChild(i);
   }

   public void addCatchClause(CatchClause node) {
      List<CatchClause> list = this.parent != null && state != null ? this.getCatchClauseList() : this.getCatchClauseListNoTransform();
      list.addChild(node);
   }

   public void addCatchClauseNoTransform(CatchClause node) {
      List<CatchClause> list = this.getCatchClauseListNoTransform();
      list.addChild(node);
   }

   public void setCatchClause(CatchClause node, int i) {
      List<CatchClause> list = this.getCatchClauseList();
      list.setChild(node, i);
   }

   public List<CatchClause> getCatchClauses() {
      return this.getCatchClauseList();
   }

   public List<CatchClause> getCatchClausesNoTransform() {
      return this.getCatchClauseListNoTransform();
   }

   public List<CatchClause> getCatchClauseList() {
      List<CatchClause> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<CatchClause> getCatchClauseListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setFinallyOpt(Opt<Block> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasFinally() {
      return this.getFinallyOpt().getNumChild() != 0;
   }

   public Block getFinally() {
      return (Block)this.getFinallyOpt().getChild(0);
   }

   public void setFinally(Block node) {
      this.getFinallyOpt().setChild(node, 0);
   }

   public Opt<Block> getFinallyOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<Block> getFinallyOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
   }

   public Collection branches() {
      if (this.branches_computed) {
         return this.branches_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.branches_value = this.branches_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.branches_computed = true;
         }

         return this.branches_value;
      }
   }

   private Collection branches_compute() {
      HashSet set = new HashSet();
      this.getBlock().collectBranches(set);

      for(int i = 0; i < this.getNumCatchClause(); ++i) {
         this.getCatchClause(i).collectBranches(set);
      }

      return set;
   }

   public Collection branchesFromFinally() {
      if (this.branchesFromFinally_computed) {
         return this.branchesFromFinally_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.branchesFromFinally_value = this.branchesFromFinally_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.branchesFromFinally_computed = true;
         }

         return this.branchesFromFinally_value;
      }
   }

   private Collection branchesFromFinally_compute() {
      HashSet set = new HashSet();
      if (this.hasFinally()) {
         this.getFinally().collectBranches(set);
      }

      return set;
   }

   public Collection targetBranches() {
      if (this.targetBranches_computed) {
         return this.targetBranches_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.targetBranches_value = this.targetBranches_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetBranches_computed = true;
         }

         return this.targetBranches_value;
      }
   }

   private Collection targetBranches_compute() {
      HashSet set = new HashSet();
      if (this.hasFinally() && !this.getFinally().canCompleteNormally()) {
         set.addAll(this.branches());
      }

      return set;
   }

   public Collection escapedBranches() {
      if (this.escapedBranches_computed) {
         return this.escapedBranches_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.escapedBranches_value = this.escapedBranches_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.escapedBranches_computed = true;
         }

         return this.escapedBranches_value;
      }
   }

   private Collection escapedBranches_compute() {
      HashSet set = new HashSet();
      if (this.hasFinally()) {
         set.addAll(this.branchesFromFinally());
      }

      if (!this.hasFinally() || this.getFinally().canCompleteNormally()) {
         set.addAll(this.branches());
      }

      return set;
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
      int i;
      if (!this.hasFinally()) {
         if (!this.getBlock().isDAafter(v)) {
            return false;
         } else {
            for(i = 0; i < this.getNumCatchClause(); ++i) {
               if (!this.getCatchClause(i).getBlock().isDAafter(v)) {
                  return false;
               }
            }

            return true;
         }
      } else if (this.getFinally().isDAafter(v)) {
         return true;
      } else if (!this.getBlock().isDAafter(v)) {
         return false;
      } else {
         for(i = 0; i < this.getNumCatchClause(); ++i) {
            if (!this.getCatchClause(i).getBlock().isDAafter(v)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean isDUafterFinally(Variable v) {
      ASTNode$State state = this.state();
      return this.getFinally().isDUafter(v);
   }

   public boolean isDAafterFinally(Variable v) {
      ASTNode$State state = this.state();
      return this.getFinally().isDAafter(v);
   }

   public boolean isDUbefore(Variable v) {
      if (this.isDUbefore_Variable_values == null) {
         this.isDUbefore_Variable_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.isDUbefore_Variable_values.containsKey(v)) {
         Object _o = this.isDUbefore_Variable_values.get(v);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.isDUbefore_Variable_values.put(v, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_isDUbefore_Variable_value = this.isDUbefore_compute(v);
            if (state.RESET_CYCLE) {
               this.isDUbefore_Variable_values.remove(v);
            } else if (new_isDUbefore_Variable_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_isDUbefore_Variable_value;
            }

            return new_isDUbefore_Variable_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_isDUbefore_Variable_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_isDUbefore_Variable_value = this.isDUbefore_compute(v);
            if (new_isDUbefore_Variable_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_isDUbefore_Variable_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUbefore_Variable_values.put(v, new_isDUbefore_Variable_value);
         } else {
            this.isDUbefore_Variable_values.remove(v);
            state.RESET_CYCLE = true;
            this.isDUbefore_compute(v);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_isDUbefore_Variable_value;
      }
   }

   private boolean isDUbefore_compute(Variable v) {
      return super.isDUbefore(v);
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
      if (!this.hasFinally()) {
         if (!this.getBlock().isDUafter(v)) {
            return false;
         } else {
            for(int i = 0; i < this.getNumCatchClause(); ++i) {
               if (!this.getCatchClause(i).getBlock().isDUafter(v)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return this.getFinally().isDUafter(v);
      }
   }

   public boolean catchableException(TypeDecl type) {
      if (this.catchableException_TypeDecl_values == null) {
         this.catchableException_TypeDecl_values = new HashMap(4);
      }

      if (this.catchableException_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.catchableException_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean catchableException_TypeDecl_value = this.catchableException_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.catchableException_TypeDecl_values.put(type, catchableException_TypeDecl_value);
         }

         return catchableException_TypeDecl_value;
      }
   }

   private boolean catchableException_compute(TypeDecl type) {
      return this.getBlock().reachedException(type);
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
      boolean anyCatchClauseCompleteNormally = false;

      for(int i = 0; i < this.getNumCatchClause() && !anyCatchClauseCompleteNormally; ++i) {
         anyCatchClauseCompleteNormally = this.getCatchClause(i).getBlock().canCompleteNormally();
      }

      return (this.getBlock().canCompleteNormally() || anyCatchClauseCompleteNormally) && (!this.hasFinally() || this.getFinally().canCompleteNormally());
   }

   public soot.jimple.Stmt break_label() {
      ASTNode$State state = this.state();
      return this.label_finally();
   }

   public soot.jimple.Stmt continue_label() {
      ASTNode$State state = this.state();
      return this.label_finally();
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

   public soot.jimple.Stmt label_block_end() {
      if (this.label_block_end_computed) {
         return this.label_block_end_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_block_end_value = this.label_block_end_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_block_end_computed = true;
         }

         return this.label_block_end_value;
      }
   }

   private soot.jimple.Stmt label_block_end_compute() {
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

   public soot.jimple.Stmt label_catch_end() {
      if (this.label_catch_end_computed) {
         return this.label_catch_end_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_catch_end_value = this.label_catch_end_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_catch_end_computed = true;
         }

         return this.label_catch_end_value;
      }
   }

   private soot.jimple.Stmt label_catch_end_compute() {
      return this.newLabel();
   }

   public boolean needsFinallyTrap() {
      ASTNode$State state = this.state();
      return this.getNumCatchClause() != 0 || this.enclosedByExceptionHandler();
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

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      if (this.getBlock().modifiedInScope(var)) {
         return true;
      } else {
         Iterator var3 = this.getCatchClauseList().iterator();

         while(var3.hasNext()) {
            CatchClause cc = (CatchClause)var3.next();
            if (cc.modifiedInScope(var)) {
               return true;
            }
         }

         return this.hasFinally() && this.getFinally().modifiedInScope(var);
      }
   }

   public boolean handlesException(TypeDecl exceptionType) {
      if (this.handlesException_TypeDecl_values == null) {
         this.handlesException_TypeDecl_values = new HashMap(4);
      }

      if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.handlesException_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, handlesException_TypeDecl_value);
         }

         return handlesException_TypeDecl_value;
      }
   }

   public TypeDecl typeError() {
      if (this.typeError_computed) {
         return this.typeError_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeError_value = this.getParent().Define_TypeDecl_typeError(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeError_computed = true;
         }

         return this.typeError_value;
      }
   }

   public TypeDecl typeRuntimeException() {
      if (this.typeRuntimeException_computed) {
         return this.typeRuntimeException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeRuntimeException_value = this.getParent().Define_TypeDecl_typeRuntimeException(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeRuntimeException_computed = true;
         }

         return this.typeRuntimeException_value;
      }
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
      if (caller == this.getFinallyOptNoTransform()) {
         return this.isDAbefore(v);
      } else if (caller == this.getCatchClauseListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.getBlock().isDAbefore(v);
      } else {
         return caller == this.getBlockNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getFinallyOptNoTransform()) {
         if (!this.getBlock().isDUeverywhere(v)) {
            return false;
         } else {
            for(int i = 0; i < this.getNumCatchClause(); ++i) {
               if (!this.getCatchClause(i).getBlock().unassignedEverywhere(v, this)) {
                  return false;
               }
            }

            return true;
         }
      } else if (caller == this.getCatchClauseListNoTransform()) {
         caller.getIndexOfChild(child);
         if (!this.getBlock().isDUafter(v)) {
            return false;
         } else {
            return this.getBlock().isDUeverywhere(v);
         }
      } else {
         return caller == this.getBlockNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getBlockNoTransform()) {
         for(int i = 0; i < this.getNumCatchClause(); ++i) {
            if (this.getCatchClause(i).handles(exceptionType)) {
               return true;
            }
         }

         if (this.hasFinally() && !this.getFinally().canCompleteNormally()) {
            return true;
         } else {
            return this.handlesException(exceptionType);
         }
      } else if (caller == this.getCatchClauseListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.hasFinally() && !this.getFinally().canCompleteNormally() ? true : this.handlesException(exceptionType);
      } else {
         return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
      }
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller == this.getFinallyOptNoTransform()) {
         return this.reachable();
      } else {
         return caller == this.getBlockNoTransform() ? this.reachable() : this.getParent().Define_boolean_reachable(this, caller);
      }
   }

   public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getCatchClauseListNoTransform()) {
         int childIndex = caller.getIndexOfChild(child);

         for(int i = 0; i < childIndex; ++i) {
            if (this.getCatchClause(i).handles(exceptionType)) {
               return false;
            }
         }

         if (this.catchableException(exceptionType)) {
            return true;
         } else if (!exceptionType.mayCatch(this.typeError()) && !exceptionType.mayCatch(this.typeRuntimeException())) {
            return false;
         } else {
            return true;
         }
      } else {
         return this.getParent().Define_boolean_reachableCatchClause(this, caller, exceptionType);
      }
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      if (caller == this.getFinallyOptNoTransform()) {
         return this.reachable();
      } else if (caller == this.getCatchClauseListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.reachable();
      } else {
         return caller == this.getBlockNoTransform() ? this.reachable() : this.getParent().Define_boolean_reportUnreachable(this, caller);
      }
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? true : this.getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
   }

   public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
      if (caller == this.getCatchClauseListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.exceptionRanges();
      } else {
         return caller == this.getBlockNoTransform() ? this.exceptionRanges() : this.getParent().Define_ArrayList_exceptionRanges(this, caller);
      }
   }

   public Collection<TypeDecl> Define_Collection_TypeDecl__caughtExceptions(ASTNode caller, ASTNode child) {
      if (caller != this.getCatchClauseListNoTransform()) {
         return this.getParent().Define_Collection_TypeDecl__caughtExceptions(this, caller);
      } else {
         int index = caller.getIndexOfChild(child);
         Collection<TypeDecl> excp = new HashSet();
         this.getBlock().collectExceptions(excp, this);
         Collection<TypeDecl> caught = new LinkedList();
         Iterator iter = excp.iterator();

         while(true) {
            TypeDecl exception;
            do {
               if (!iter.hasNext()) {
                  return caught;
               }

               exception = (TypeDecl)iter.next();
            } while(!this.getCatchClause(index).handles(exception));

            boolean already = false;

            for(int i = 0; i < index; ++i) {
               if (this.getCatchClause(i).handles(exception)) {
                  already = true;
                  break;
               }
            }

            if (!already) {
               caught.add(exception);
            }
         }
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
