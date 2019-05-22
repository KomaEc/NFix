package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANullConstant extends PConstant {
   private TNull _null_;

   public ANullConstant() {
   }

   public ANullConstant(TNull _null_) {
      this.setNull(_null_);
   }

   public Object clone() {
      return new ANullConstant((TNull)this.cloneNode(this._null_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANullConstant(this);
   }

   public TNull getNull() {
      return this._null_;
   }

   public void setNull(TNull node) {
      if (this._null_ != null) {
         this._null_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._null_ = node;
   }

   public String toString() {
      return "" + this.toString(this._null_);
   }

   void removeChild(Node child) {
      if (this._null_ == child) {
         this._null_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._null_ == oldChild) {
         this.setNull((TNull)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
