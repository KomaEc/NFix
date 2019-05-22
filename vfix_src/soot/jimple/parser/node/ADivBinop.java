package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ADivBinop extends PBinop {
   private TDiv _div_;

   public ADivBinop() {
   }

   public ADivBinop(TDiv _div_) {
      this.setDiv(_div_);
   }

   public Object clone() {
      return new ADivBinop((TDiv)this.cloneNode(this._div_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseADivBinop(this);
   }

   public TDiv getDiv() {
      return this._div_;
   }

   public void setDiv(TDiv node) {
      if (this._div_ != null) {
         this._div_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._div_ = node;
   }

   public String toString() {
      return "" + this.toString(this._div_);
   }

   void removeChild(Node child) {
      if (this._div_ == child) {
         this._div_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._div_ == oldChild) {
         this.setDiv((TDiv)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
