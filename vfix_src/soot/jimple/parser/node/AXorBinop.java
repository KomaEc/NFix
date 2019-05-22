package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AXorBinop extends PBinop {
   private TXor _xor_;

   public AXorBinop() {
   }

   public AXorBinop(TXor _xor_) {
      this.setXor(_xor_);
   }

   public Object clone() {
      return new AXorBinop((TXor)this.cloneNode(this._xor_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAXorBinop(this);
   }

   public TXor getXor() {
      return this._xor_;
   }

   public void setXor(TXor node) {
      if (this._xor_ != null) {
         this._xor_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._xor_ = node;
   }

   public String toString() {
      return "" + this.toString(this._xor_);
   }

   void removeChild(Node child) {
      if (this._xor_ == child) {
         this._xor_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._xor_ == oldChild) {
         this.setXor((TXor)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
