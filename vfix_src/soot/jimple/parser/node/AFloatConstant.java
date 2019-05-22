package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AFloatConstant extends PConstant {
   private TMinus _minus_;
   private TFloatConstant _floatConstant_;

   public AFloatConstant() {
   }

   public AFloatConstant(TMinus _minus_, TFloatConstant _floatConstant_) {
      this.setMinus(_minus_);
      this.setFloatConstant(_floatConstant_);
   }

   public Object clone() {
      return new AFloatConstant((TMinus)this.cloneNode(this._minus_), (TFloatConstant)this.cloneNode(this._floatConstant_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFloatConstant(this);
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

   public TFloatConstant getFloatConstant() {
      return this._floatConstant_;
   }

   public void setFloatConstant(TFloatConstant node) {
      if (this._floatConstant_ != null) {
         this._floatConstant_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._floatConstant_ = node;
   }

   public String toString() {
      return "" + this.toString(this._minus_) + this.toString(this._floatConstant_);
   }

   void removeChild(Node child) {
      if (this._minus_ == child) {
         this._minus_ = null;
      } else if (this._floatConstant_ == child) {
         this._floatConstant_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._minus_ == oldChild) {
         this.setMinus((TMinus)newChild);
      } else if (this._floatConstant_ == oldChild) {
         this.setFloatConstant((TFloatConstant)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
