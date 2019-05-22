package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AUnopBoolExpr extends PBoolExpr {
   private PUnopExpr _unopExpr_;

   public AUnopBoolExpr() {
   }

   public AUnopBoolExpr(PUnopExpr _unopExpr_) {
      this.setUnopExpr(_unopExpr_);
   }

   public Object clone() {
      return new AUnopBoolExpr((PUnopExpr)this.cloneNode(this._unopExpr_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAUnopBoolExpr(this);
   }

   public PUnopExpr getUnopExpr() {
      return this._unopExpr_;
   }

   public void setUnopExpr(PUnopExpr node) {
      if (this._unopExpr_ != null) {
         this._unopExpr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._unopExpr_ = node;
   }

   public String toString() {
      return "" + this.toString(this._unopExpr_);
   }

   void removeChild(Node child) {
      if (this._unopExpr_ == child) {
         this._unopExpr_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._unopExpr_ == oldChild) {
         this.setUnopExpr((PUnopExpr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
