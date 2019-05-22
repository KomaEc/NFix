package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ABinopExpression extends PExpression {
   private PBinopExpr _binopExpr_;

   public ABinopExpression() {
   }

   public ABinopExpression(PBinopExpr _binopExpr_) {
      this.setBinopExpr(_binopExpr_);
   }

   public Object clone() {
      return new ABinopExpression((PBinopExpr)this.cloneNode(this._binopExpr_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseABinopExpression(this);
   }

   public PBinopExpr getBinopExpr() {
      return this._binopExpr_;
   }

   public void setBinopExpr(PBinopExpr node) {
      if (this._binopExpr_ != null) {
         this._binopExpr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._binopExpr_ = node;
   }

   public String toString() {
      return "" + this.toString(this._binopExpr_);
   }

   void removeChild(Node child) {
      if (this._binopExpr_ == child) {
         this._binopExpr_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._binopExpr_ == oldChild) {
         this.setBinopExpr((PBinopExpr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
