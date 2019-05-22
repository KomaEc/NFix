package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public abstract class BranchTargetStmt extends Stmt implements Cloneable, BranchPropagation {
   protected boolean reachableBreak_computed = false;
   protected boolean reachableBreak_value;
   protected boolean reachableContinue_computed = false;
   protected boolean reachableContinue_value;
   protected boolean targetBranches_computed = false;
   protected Collection targetBranches_value;
   protected boolean escapedBranches_computed = false;
   protected Collection escapedBranches_value;
   protected boolean branches_computed = false;
   protected Collection branches_value;
   protected boolean targetContinues_computed = false;
   protected Collection targetContinues_value;
   protected boolean targetBreaks_computed = false;
   protected Collection targetBreaks_value;

   public void flushCache() {
      super.flushCache();
      this.reachableBreak_computed = false;
      this.reachableContinue_computed = false;
      this.targetBranches_computed = false;
      this.targetBranches_value = null;
      this.escapedBranches_computed = false;
      this.escapedBranches_value = null;
      this.branches_computed = false;
      this.branches_value = null;
      this.targetContinues_computed = false;
      this.targetContinues_value = null;
      this.targetBreaks_computed = false;
      this.targetBreaks_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BranchTargetStmt clone() throws CloneNotSupportedException {
      BranchTargetStmt node = (BranchTargetStmt)super.clone();
      node.reachableBreak_computed = false;
      node.reachableContinue_computed = false;
      node.targetBranches_computed = false;
      node.targetBranches_value = null;
      node.escapedBranches_computed = false;
      node.escapedBranches_value = null;
      node.branches_computed = false;
      node.branches_value = null;
      node.targetContinues_computed = false;
      node.targetContinues_value = null;
      node.targetBreaks_computed = false;
      node.targetBreaks_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void collectBranches(Collection c) {
      c.addAll(this.escapedBranches());
   }

   public Stmt branchTarget(Stmt branchStmt) {
      return (Stmt)(this.targetBranches().contains(branchStmt) ? this : super.branchTarget(branchStmt));
   }

   public void collectFinally(Stmt branchStmt, ArrayList list) {
      if (!this.targetBranches().contains(branchStmt)) {
         super.collectFinally(branchStmt, list);
      }
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public abstract boolean targetOf(ContinueStmt var1);

   public abstract boolean targetOf(BreakStmt var1);

   public boolean reachableBreak() {
      if (this.reachableBreak_computed) {
         return this.reachableBreak_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.reachableBreak_value = this.reachableBreak_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.reachableBreak_computed = true;
         }

         return this.reachableBreak_value;
      }
   }

   private boolean reachableBreak_compute() {
      Iterator iter = this.targetBreaks().iterator();

      BreakStmt stmt;
      do {
         if (!iter.hasNext()) {
            return false;
         }

         stmt = (BreakStmt)iter.next();
      } while(!stmt.reachable());

      return true;
   }

   public boolean reachableContinue() {
      if (this.reachableContinue_computed) {
         return this.reachableContinue_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.reachableContinue_value = this.reachableContinue_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.reachableContinue_computed = true;
         }

         return this.reachableContinue_value;
      }
   }

   private boolean reachableContinue_compute() {
      Iterator iter = this.targetContinues().iterator();

      Stmt stmt;
      do {
         if (!iter.hasNext()) {
            return false;
         }

         stmt = (Stmt)iter.next();
      } while(!stmt.reachable());

      return true;
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
      Iterator iter = this.branches().iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         if (o instanceof ContinueStmt && this.targetOf((ContinueStmt)o)) {
            set.add(o);
         }

         if (o instanceof BreakStmt && this.targetOf((BreakStmt)o)) {
            set.add(o);
         }
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
      Iterator iter = this.branches().iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         if (o instanceof ContinueStmt && !this.targetOf((ContinueStmt)o)) {
            set.add(o);
         }

         if (o instanceof BreakStmt && !this.targetOf((BreakStmt)o)) {
            set.add(o);
         }

         if (o instanceof ReturnStmt) {
            set.add(o);
         }
      }

      return set;
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
      super.collectBranches(set);
      return set;
   }

   public Collection targetContinues() {
      if (this.targetContinues_computed) {
         return this.targetContinues_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.targetContinues_value = this.targetContinues_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetContinues_computed = true;
         }

         return this.targetContinues_value;
      }
   }

   private Collection targetContinues_compute() {
      HashSet set = new HashSet();
      Iterator iter = this.targetBranches().iterator();

      Object o;
      while(iter.hasNext()) {
         o = iter.next();
         if (o instanceof ContinueStmt) {
            set.add(o);
         }
      }

      if (this.getParent() instanceof LabeledStmt) {
         iter = ((LabeledStmt)this.getParent()).targetBranches().iterator();

         while(iter.hasNext()) {
            o = iter.next();
            if (o instanceof ContinueStmt) {
               set.add(o);
            }
         }
      }

      return set;
   }

   public Collection targetBreaks() {
      if (this.targetBreaks_computed) {
         return this.targetBreaks_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.targetBreaks_value = this.targetBreaks_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetBreaks_computed = true;
         }

         return this.targetBreaks_value;
      }
   }

   private Collection targetBreaks_compute() {
      HashSet set = new HashSet();
      Iterator iter = this.targetBranches().iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         if (o instanceof BreakStmt) {
            set.add(o);
         }
      }

      return set;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
