package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpneBinop extends PBinop {
   private TCmpne _cmpne_;

   public ACmpneBinop() {
   }

   public ACmpneBinop(TCmpne _cmpne_) {
      this.setCmpne(_cmpne_);
   }

   public Object clone() {
      return new ACmpneBinop((TCmpne)this.cloneNode(this._cmpne_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpneBinop(this);
   }

   public TCmpne getCmpne() {
      return this._cmpne_;
   }

   public void setCmpne(TCmpne node) {
      if (this._cmpne_ != null) {
         this._cmpne_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmpne_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmpne_);
   }

   void removeChild(Node child) {
      if (this._cmpne_ == child) {
         this._cmpne_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmpne_ == oldChild) {
         this.setCmpne((TCmpne)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
