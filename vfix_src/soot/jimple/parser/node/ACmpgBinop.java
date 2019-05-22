package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpgBinop extends PBinop {
   private TCmpg _cmpg_;

   public ACmpgBinop() {
   }

   public ACmpgBinop(TCmpg _cmpg_) {
      this.setCmpg(_cmpg_);
   }

   public Object clone() {
      return new ACmpgBinop((TCmpg)this.cloneNode(this._cmpg_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpgBinop(this);
   }

   public TCmpg getCmpg() {
      return this._cmpg_;
   }

   public void setCmpg(TCmpg node) {
      if (this._cmpg_ != null) {
         this._cmpg_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmpg_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmpg_);
   }

   void removeChild(Node child) {
      if (this._cmpg_ == child) {
         this._cmpg_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmpg_ == oldChild) {
         this.setCmpg((TCmpg)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
