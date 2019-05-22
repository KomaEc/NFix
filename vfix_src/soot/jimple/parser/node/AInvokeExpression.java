package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AInvokeExpression extends PExpression {
   private PInvokeExpr _invokeExpr_;

   public AInvokeExpression() {
   }

   public AInvokeExpression(PInvokeExpr _invokeExpr_) {
      this.setInvokeExpr(_invokeExpr_);
   }

   public Object clone() {
      return new AInvokeExpression((PInvokeExpr)this.cloneNode(this._invokeExpr_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAInvokeExpression(this);
   }

   public PInvokeExpr getInvokeExpr() {
      return this._invokeExpr_;
   }

   public void setInvokeExpr(PInvokeExpr node) {
      if (this._invokeExpr_ != null) {
         this._invokeExpr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._invokeExpr_ = node;
   }

   public String toString() {
      return "" + this.toString(this._invokeExpr_);
   }

   void removeChild(Node child) {
      if (this._invokeExpr_ == child) {
         this._invokeExpr_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._invokeExpr_ == oldChild) {
         this.setInvokeExpr((PInvokeExpr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
