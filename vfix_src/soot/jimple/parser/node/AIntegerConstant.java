package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AIntegerConstant extends PConstant {
   private TMinus _minus_;
   private TIntegerConstant _integerConstant_;

   public AIntegerConstant() {
   }

   public AIntegerConstant(TMinus _minus_, TIntegerConstant _integerConstant_) {
      this.setMinus(_minus_);
      this.setIntegerConstant(_integerConstant_);
   }

   public Object clone() {
      return new AIntegerConstant((TMinus)this.cloneNode(this._minus_), (TIntegerConstant)this.cloneNode(this._integerConstant_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIntegerConstant(this);
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

   public TIntegerConstant getIntegerConstant() {
      return this._integerConstant_;
   }

   public void setIntegerConstant(TIntegerConstant node) {
      if (this._integerConstant_ != null) {
         this._integerConstant_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._integerConstant_ = node;
   }

   public String toString() {
      return "" + this.toString(this._minus_) + this.toString(this._integerConstant_);
   }

   void removeChild(Node child) {
      if (this._minus_ == child) {
         this._minus_ = null;
      } else if (this._integerConstant_ == child) {
         this._integerConstant_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._minus_ == oldChild) {
         this.setMinus((TMinus)newChild);
      } else if (this._integerConstant_ == oldChild) {
         this.setIntegerConstant((TIntegerConstant)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
