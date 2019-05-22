package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpBinop extends PBinop {
   private TCmp _cmp_;

   public ACmpBinop() {
   }

   public ACmpBinop(TCmp _cmp_) {
      this.setCmp(_cmp_);
   }

   public Object clone() {
      return new ACmpBinop((TCmp)this.cloneNode(this._cmp_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpBinop(this);
   }

   public TCmp getCmp() {
      return this._cmp_;
   }

   public void setCmp(TCmp node) {
      if (this._cmp_ != null) {
         this._cmp_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmp_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmp_);
   }

   void removeChild(Node child) {
      if (this._cmp_ == child) {
         this._cmp_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmp_ == oldChild) {
         this.setCmp((TCmp)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
