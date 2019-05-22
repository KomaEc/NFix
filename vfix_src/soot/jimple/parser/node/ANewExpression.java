package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANewExpression extends PExpression {
   private PNewExpr _newExpr_;

   public ANewExpression() {
   }

   public ANewExpression(PNewExpr _newExpr_) {
      this.setNewExpr(_newExpr_);
   }

   public Object clone() {
      return new ANewExpression((PNewExpr)this.cloneNode(this._newExpr_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANewExpression(this);
   }

   public PNewExpr getNewExpr() {
      return this._newExpr_;
   }

   public void setNewExpr(PNewExpr node) {
      if (this._newExpr_ != null) {
         this._newExpr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._newExpr_ = node;
   }

   public String toString() {
      return "" + this.toString(this._newExpr_);
   }

   void removeChild(Node child) {
      if (this._newExpr_ == child) {
         this._newExpr_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._newExpr_ == oldChild) {
         this.setNewExpr((PNewExpr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
