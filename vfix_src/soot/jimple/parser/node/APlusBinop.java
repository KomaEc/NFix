package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class APlusBinop extends PBinop {
   private TPlus _plus_;

   public APlusBinop() {
   }

   public APlusBinop(TPlus _plus_) {
      this.setPlus(_plus_);
   }

   public Object clone() {
      return new APlusBinop((TPlus)this.cloneNode(this._plus_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAPlusBinop(this);
   }

   public TPlus getPlus() {
      return this._plus_;
   }

   public void setPlus(TPlus node) {
      if (this._plus_ != null) {
         this._plus_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._plus_ = node;
   }

   public String toString() {
      return "" + this.toString(this._plus_);
   }

   void removeChild(Node child) {
      if (this._plus_ == child) {
         this._plus_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._plus_ == oldChild) {
         this.setPlus((TPlus)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
