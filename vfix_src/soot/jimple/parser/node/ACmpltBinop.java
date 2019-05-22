package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpltBinop extends PBinop {
   private TCmplt _cmplt_;

   public ACmpltBinop() {
   }

   public ACmpltBinop(TCmplt _cmplt_) {
      this.setCmplt(_cmplt_);
   }

   public Object clone() {
      return new ACmpltBinop((TCmplt)this.cloneNode(this._cmplt_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpltBinop(this);
   }

   public TCmplt getCmplt() {
      return this._cmplt_;
   }

   public void setCmplt(TCmplt node) {
      if (this._cmplt_ != null) {
         this._cmplt_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmplt_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmplt_);
   }

   void removeChild(Node child) {
      if (this._cmplt_ == child) {
         this._cmplt_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmplt_ == oldChild) {
         this.setCmplt((TCmplt)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
