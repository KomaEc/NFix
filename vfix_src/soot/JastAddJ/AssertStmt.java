package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class AssertStmt extends Stmt implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AssertStmt clone() throws CloneNotSupportedException {
      AssertStmt node = (AssertStmt)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AssertStmt copy() {
      try {
         AssertStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AssertStmt fullCopy() {
      AssertStmt tree = this.copy();
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

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("assert ");
      this.getfirst().toString(s);
      if (this.hasExpr()) {
         s.append(" : ");
         this.getExpr().toString(s);
      }

      s.append(";");
   }

   public void typeCheck() {
      if (!this.getfirst().type().isBoolean()) {
         this.error("Assert requires boolean condition");
      }

      if (this.hasExpr() && this.getExpr().type().isVoid()) {
         this.error("The second part of an assert statement may not be void");
      }

   }

   public void transformation() {
      super.transformation();
      FieldDeclaration f = this.hostType().topLevelType().createStaticClassField(this.hostType().topLevelType().referenceClassFieldName());
      FieldDeclaration assertionsDisabled = this.hostType().createAssertionsDisabled();
      Expr condition = (Expr)this.getfirst().fullCopy();
      List args = new List();
      if (this.hasExpr()) {
         if (this.getExpr().type().isString()) {
            args.add(new CastExpr(new TypeAccess("java.lang", "Object"), (Expr)this.getExpr().fullCopy()));
         } else {
            args.add(this.getExpr().fullCopy());
         }
      }

      Stmt stmt = new IfStmt(new LogNotExpr(new ParExpr(new OrLogicalExpr(new BoundFieldAccess(assertionsDisabled), condition))), new ThrowStmt(new ClassInstanceExpr(this.lookupType("java.lang", "AssertionError").createQualifiedAccess(), args, new Opt())), new Opt());
      this.replace(this).with(stmt);
      stmt.transformation();
   }

   public AssertStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new Opt(), 1);
   }

   public AssertStmt(Expr p0, Opt<Expr> p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setfirst(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getfirst() {
      return (Expr)this.getChild(0);
   }

   public Expr getfirstNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setExprOpt(Opt<Expr> opt) {
      this.setChild(opt, 1);
   }

   public boolean hasExpr() {
      return this.getExprOpt().getNumChild() != 0;
   }

   public Expr getExpr() {
      return (Expr)this.getExprOpt().getChild(0);
   }

   public void setExpr(Expr node) {
      this.getExprOpt().setChild(node, 0);
   }

   public Opt<Expr> getExprOpt() {
      return (Opt)this.getChild(1);
   }

   public Opt<Expr> getExprOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
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
      return this.getfirst().isDAafter(v);
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
      return this.getfirst().isDUafter(v);
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getExprOptNoTransform() ? this.getfirst().isDAafter(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
