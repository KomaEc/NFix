package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AMinusBinop extends PBinop {
   private TMinus _minus_;

   public AMinusBinop() {
   }

   public AMinusBinop(TMinus _minus_) {
      this.setMinus(_minus_);
   }

   public Object clone() {
      return new AMinusBinop((TMinus)this.cloneNode(this._minus_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMinusBinop(this);
   }

   public TMinus getMinus() {
      return this._minus_;
   }

   public void setMinus(TMinus node) {
      if (this._minus_ != null) {
         this._minus_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._minus_ = node;
   }

   public String toString() {
      return "" + this.toString(this._minus_);
   }

   void removeChild(Node child) {
      if (this._minus_ == child) {
         this._minus_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._minus_ == oldChild) {
         this.setMinus((TMinus)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
