package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AOrBinop extends PBinop {
   private TOr _or_;

   public AOrBinop() {
   }

   public AOrBinop(TOr _or_) {
      this.setOr(_or_);
   }

   public Object clone() {
      return new AOrBinop((TOr)this.cloneNode(this._or_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAOrBinop(this);
   }

   public TOr getOr() {
      return this._or_;
   }

   public void setOr(TOr node) {
      if (this._or_ != null) {
         this._or_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._or_ = node;
   }

   public String toString() {
      return "" + this.toString(this._or_);
   }

   void removeChild(Node child) {
      if (this._or_ == child) {
         this._or_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._or_ == oldChild) {
         this.setOr((TOr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
