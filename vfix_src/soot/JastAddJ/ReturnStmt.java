package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;

public class ReturnStmt extends Stmt implements Cloneable {
   protected boolean finallyList_computed;
   protected ArrayList finallyList_value;
   protected Map isDAafter_Variable_values;
   protected Map isDUafterReachedFinallyBlocks_Variable_values;
   protected Map isDAafterReachedFinallyBlocks_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed;
   protected boolean canCompleteNormally_value;
   protected boolean inSynchronizedBlock_computed;
   protected boolean inSynchronizedBlock_value;

   public void flushCache() {
      super.flushCache();
      this.finallyList_computed = false;
      this.finallyList_value = null;
      this.isDAafter_Variable_values = null;
      this.isDUafterReachedFinallyBlocks_Variable_values = null;
      this.isDAafterReachedFinallyBlocks_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.inSynchronizedBlock_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ReturnStmt clone() throws CloneNotSupportedException {
      ReturnStmt node = (ReturnStmt)super.clone();
      node.finallyList_computed = false;
      node.finallyList_value = null;
      node.isDAafter_Variable_values = null;
      node.isDUafterReachedFinallyBlocks_Variable_values = null;
      node.isDAafterReachedFinallyBlocks_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.inSynchronizedBlock_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ReturnStmt copy() {
      try {
         ReturnStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ReturnStmt fullCopy() {
      ReturnStmt tree = this.copy();
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
      c.add(this);
   }

   public ReturnStmt(Expr expr) {
      this(new Opt(expr));
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("return ");
      if (this.hasResult()) {
         this.getResult().toString(s);
      }

      s.append(";");
   }

   public void typeCheck() {
      if (this.hasResult() && !this.returnType().isVoid() && !this.getResult().type().assignConversionTo(this.returnType(), this.getResult())) {
         this.error("return value must be an instance of " + this.returnType().typeName() + " which " + this.getResult().type().typeName() + " is not");
      }

      if (this.returnType().isVoid() && this.hasResult()) {
         this.error("return stmt may not have an expression in void methods");
      }

      if (!this.returnType().isVoid() && !this.hasResult()) {
         this.error("return stmt must have an expression in non void methods");
      }

      if (this.enclosingBodyDecl() instanceof InstanceInitializer || this.enclosingBodyDecl() instanceof StaticInitializer) {
         this.error("Initializers may not return");
      }

   }

   public void jimplify2(Body b) {
      if (this.hasResult()) {
         TypeDecl type = this.returnType();
         if (type.isVoid()) {
            throw new Error("Can not return a value from a void body");
         }

         Local local = this.asLocal(b, this.getResult().type().emitCastTo(b, this.getResult().eval(b), type, this.getResult()), type.getSootType());
         ArrayList list = this.exceptionRanges();
         if (!this.inSynchronizedBlock()) {
            this.endExceptionRange(b, list);
         }

         Iterator iter = this.finallyList().iterator();

         while(iter.hasNext()) {
            FinallyHost stmt = (FinallyHost)iter.next();
            stmt.emitFinallyCode(b);
         }

         b.setLine(this);
         if (this.inSynchronizedBlock()) {
            this.endExceptionRange(b, list);
         }

         b.add(b.newReturnStmt(local, this));
         this.beginExceptionRange(b, list);
      } else {
         ArrayList list = this.exceptionRanges();
         if (!this.inSynchronizedBlock()) {
            this.endExceptionRange(b, list);
         }

         Iterator iter = this.finallyList().iterator();

         while(iter.hasNext()) {
            FinallyHost stmt = (FinallyHost)iter.next();
            stmt.emitFinallyCode(b);
         }

         b.setLine(this);
         if (this.inSynchronizedBlock()) {
            this.endExceptionRange(b, list);
         }

         b.add(b.newReturnVoidStmt(this));
         this.beginExceptionRange(b, list);
      }

   }

   public ReturnStmt() {
      this.finallyList_computed = false;
      this.canCompleteNormally_computed = false;
      this.inSynchronizedBlock_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new Opt(), 0);
   }

   public ReturnStmt(Opt<Expr> p0) {
      this.finallyList_computed = false;
      this.canCompleteNormally_computed = false;
      this.inSynchronizedBlock_computed = false;
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setResultOpt(Opt<Expr> opt) {
      this.setChild(opt, 0);
   }

   public boolean hasResult() {
      return this.getResultOpt().getNumChild() != 0;
   }

   public Expr getResult() {
      return (Expr)this.getResultOpt().getChild(0);
   }

   public void setResult(Expr node) {
      this.getResultOpt().setChild(node, 0);
   }

   public Opt<Expr> getResultOpt() {
      return (Opt)this.getChild(0);
   }

   public Opt<Expr> getResultOptNoTransform() {
      return (Opt)this.getChildNoTransform(0);
   }

   public ArrayList finallyList() {
      if (this.finallyList_computed) {
         return this.finallyList_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.finallyList_value = this.finallyList_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.finallyList_computed = true;
         }

         return this.finallyList_value;
      }
   }

   private ArrayList finallyList_compute() {
      ArrayList list = new ArrayList();
      this.collectFinally(this, list);
      return list;
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
      return true;
   }

   public boolean isDUafterReachedFinallyBlocks(Variable v) {
      if (this.isDUafterReachedFinallyBlocks_Variable_values == null) {
         this.isDUafterReachedFinallyBlocks_Variable_values = new HashMap(4);
      }

      if (this.isDUafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafterReachedFinallyBlocks_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafterReachedFinallyBlocks_Variable_value = this.isDUafterReachedFinallyBlocks_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafterReachedFinallyBlocks_Variable_values.put(v, isDUafterReachedFinallyBlocks_Variable_value);
         }

         return isDUafterReachedFinallyBlocks_Variable_value;
      }
   }

   private boolean isDUafterReachedFinallyBlocks_compute(Variable v) {
      if (!this.isDUbefore(v) && this.finallyList().isEmpty()) {
         return false;
      } else {
         Iterator iter = this.finallyList().iterator();

         FinallyHost f;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            f = (FinallyHost)iter.next();
         } while(f.isDUafterFinally(v));

         return false;
      }
   }

   public boolean isDAafterReachedFinallyBlocks(Variable v) {
      if (this.isDAafterReachedFinallyBlocks_Variable_values == null) {
         this.isDAafterReachedFinallyBlocks_Variable_values = new HashMap(4);
      }

      if (this.isDAafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterReachedFinallyBlocks_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterReachedFinallyBlocks_Variable_value = this.isDAafterReachedFinallyBlocks_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterReachedFinallyBlocks_Variable_values.put(v, isDAafterReachedFinallyBlocks_Variable_value);
         }

         return isDAafterReachedFinallyBlocks_Variable_value;
      }
   }

   private boolean isDAafterReachedFinallyBlocks_compute(Variable v) {
      if (this.hasResult()) {
         if (this.getResult().isDAafter(v)) {
            return true;
         }
      } else if (this.isDAbefore(v)) {
         return true;
      }

      if (this.finallyList().isEmpty()) {
         return false;
      } else {
         Iterator iter = this.finallyList().iterator();

         FinallyHost f;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            f = (FinallyHost)iter.next();
         } while(f.isDAafterFinally(v));

         return false;
      }
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
      return true;
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
      return false;
   }

   public boolean inSynchronizedBlock() {
      if (this.inSynchronizedBlock_computed) {
         return this.inSynchronizedBlock_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.inSynchronizedBlock_value = this.inSynchronizedBlock_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.inSynchronizedBlock_computed = true;
         }

         return this.inSynchronizedBlock_value;
      }
   }

   private boolean inSynchronizedBlock_compute() {
      return !this.finallyList().isEmpty() && this.finallyList().iterator().next() instanceof SynchronizedStmt;
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl returnType() {
      ASTNode$State state = this.state();
      TypeDecl returnType_value = this.getParent().Define_TypeDecl_returnType(this, (ASTNode)null);
      return returnType_value;
   }

   public ArrayList exceptionRanges() {
      ASTNode$State state = this.state();
      ArrayList exceptionRanges_value = this.getParent().Define_ArrayList_exceptionRanges(this, (ASTNode)null);
      return exceptionRanges_value;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getResultOptNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getResultOptNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      return caller == this.getResultOptNoTransform() ? this.returnType() : this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
