package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AMultBinop extends PBinop {
   private TMult _mult_;

   public AMultBinop() {
   }

   public AMultBinop(TMult _mult_) {
      this.setMult(_mult_);
   }

   public Object clone() {
      return new AMultBinop((TMult)this.cloneNode(this._mult_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMultBinop(this);
   }

   public TMult getMult() {
      return this._mult_;
   }

   public void setMult(TMult node) {
      if (this._mult_ != null) {
         this._mult_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._mult_ = node;
   }

   public String toString() {
      return "" + this.toString(this._mult_);
   }

   void removeChild(Node child) {
      if (this._mult_ == child) {
         this._mult_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._mult_ == oldChild) {
         this.setMult((TMult)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
