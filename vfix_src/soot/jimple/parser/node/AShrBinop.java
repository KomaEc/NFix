package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AShrBinop extends PBinop {
   private TShr _shr_;

   public AShrBinop() {
   }

   public AShrBinop(TShr _shr_) {
      this.setShr(_shr_);
   }

   public Object clone() {
      return new AShrBinop((TShr)this.cloneNode(this._shr_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAShrBinop(this);
   }

   public TShr getShr() {
      return this._shr_;
   }

   public void setShr(TShr node) {
      if (this._shr_ != null) {
         this._shr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._shr_ = node;
   }

   public String toString() {
      return "" + this.toString(this._shr_);
   }

   void removeChild(Node child) {
      if (this._shr_ == child) {
         this._shr_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._shr_ == oldChild) {
         this.setShr((TShr)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
