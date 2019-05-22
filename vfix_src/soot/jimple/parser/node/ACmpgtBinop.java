package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpgtBinop extends PBinop {
   private TCmpgt _cmpgt_;

   public ACmpgtBinop() {
   }

   public ACmpgtBinop(TCmpgt _cmpgt_) {
      this.setCmpgt(_cmpgt_);
   }

   public Object clone() {
      return new ACmpgtBinop((TCmpgt)this.cloneNode(this._cmpgt_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpgtBinop(this);
   }

   public TCmpgt getCmpgt() {
      return this._cmpgt_;
   }

   public void setCmpgt(TCmpgt node) {
      if (this._cmpgt_ != null) {
         this._cmpgt_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmpgt_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmpgt_);
   }

   void removeChild(Node child) {
      if (this._cmpgt_ == child) {
         this._cmpgt_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmpgt_ == oldChild) {
         this.setCmpgt((TCmpgt)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
