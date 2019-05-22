package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AConstantImmediate extends PImmediate {
   private PConstant _constant_;

   public AConstantImmediate() {
   }

   public AConstantImmediate(PConstant _constant_) {
      this.setConstant(_constant_);
   }

   public Object clone() {
      return new AConstantImmediate((PConstant)this.cloneNode(this._constant_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAConstantImmediate(this);
   }

   public PConstant getConstant() {
      return this._constant_;
   }

   public void setConstant(PConstant node) {
      if (this._constant_ != null) {
         this._constant_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._constant_ = node;
   }

   public String toString() {
      return "" + this.toString(this._constant_);
   }

   void removeChild(Node child) {
      if (this._constant_ == child) {
         this._constant_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._constant_ == oldChild) {
         this.setConstant((PConstant)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
