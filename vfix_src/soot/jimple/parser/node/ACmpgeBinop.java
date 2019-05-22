package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpgeBinop extends PBinop {
   private TCmpge _cmpge_;

   public ACmpgeBinop() {
   }

   public ACmpgeBinop(TCmpge _cmpge_) {
      this.setCmpge(_cmpge_);
   }

   public Object clone() {
      return new ACmpgeBinop((TCmpge)this.cloneNode(this._cmpge_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpgeBinop(this);
   }

   public TCmpge getCmpge() {
      return this._cmpge_;
   }

   public void setCmpge(TCmpge node) {
      if (this._cmpge_ != null) {
         this._cmpge_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmpge_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmpge_);
   }

   void removeChild(Node child) {
      if (this._cmpge_ == child) {
         this._cmpge_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmpge_ == oldChild) {
         this.setCmpge((TCmpge)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
