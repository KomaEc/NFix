package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AShlBinop extends PBinop {
   private TShl _shl_;

   public AShlBinop() {
   }

   public AShlBinop(TShl _shl_) {
      this.setShl(_shl_);
   }

   public Object clone() {
      return new AShlBinop((TShl)this.cloneNode(this._shl_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAShlBinop(this);
   }

   public TShl getShl() {
      return this._shl_;
   }

   public void setShl(TShl node) {
      if (this._shl_ != null) {
         this._shl_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._shl_ = node;
   }

   public String toString() {
      return "" + this.toString(this._shl_);
   }

   void removeChild(Node child) {
      if (this._shl_ == child) {
         this._shl_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._shl_ == oldChild) {
         this.setShl((TShl)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
