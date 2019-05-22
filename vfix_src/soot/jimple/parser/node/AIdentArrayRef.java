package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AIdentArrayRef extends PArrayRef {
   private TIdentifier _identifier_;
   private PFixedArrayDescriptor _fixedArrayDescriptor_;

   public AIdentArrayRef() {
   }

   public AIdentArrayRef(TIdentifier _identifier_, PFixedArrayDescriptor _fixedArrayDescriptor_) {
      this.setIdentifier(_identifier_);
      this.setFixedArrayDescriptor(_fixedArrayDescriptor_);
   }

   public Object clone() {
      return new AIdentArrayRef((TIdentifier)this.cloneNode(this._identifier_), (PFixedArrayDescriptor)this.cloneNode(this._fixedArrayDescriptor_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIdentArrayRef(this);
   }

   public TIdentifier getIdentifier() {
      return this._identifier_;
   }

   public void setIdentifier(TIdentifier node) {
      if (this._identifier_ != null) {
         this._identifier_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._identifier_ = node;
   }

   public PFixedArrayDescriptor getFixedArrayDescriptor() {
      return this._fixedArrayDescriptor_;
   }

   public void setFixedArrayDescriptor(PFixedArrayDescriptor node) {
      if (this._fixedArrayDescriptor_ != null) {
         this._fixedArrayDescriptor_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._fixedArrayDescriptor_ = node;
   }

   public String toString() {
      return "" + this.toString(this._identifier_) + this.toString(this._fixedArrayDescriptor_);
   }

   void removeChild(Node child) {
      if (this._identifier_ == child) {
         this._identifier_ = null;
      } else if (this._fixedArrayDescriptor_ == child) {
         this._fixedArrayDescriptor_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._identifier_ == oldChild) {
         this.setIdentifier((TIdentifier)newChild);
      } else if (this._fixedArrayDescriptor_ == oldChild) {
         this.setFixedArrayDescriptor((PFixedArrayDescriptor)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
