package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACmpeqBinop extends PBinop {
   private TCmpeq _cmpeq_;

   public ACmpeqBinop() {
   }

   public ACmpeqBinop(TCmpeq _cmpeq_) {
      this.setCmpeq(_cmpeq_);
   }

   public Object clone() {
      return new ACmpeqBinop((TCmpeq)this.cloneNode(this._cmpeq_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACmpeqBinop(this);
   }

   public TCmpeq getCmpeq() {
      return this._cmpeq_;
   }

   public void setCmpeq(TCmpeq node) {
      if (this._cmpeq_ != null) {
         this._cmpeq_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._cmpeq_ = node;
   }

   public String toString() {
      return "" + this.toString(this._cmpeq_);
   }

   void removeChild(Node child) {
      if (this._cmpeq_ == child) {
         this._cmpeq_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._cmpeq_ == oldChild) {
         this.setCmpeq((TCmpeq)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
