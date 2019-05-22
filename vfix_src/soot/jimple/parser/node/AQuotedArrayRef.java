package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AQuotedArrayRef extends PArrayRef {
   private TQuotedName _quotedName_;
   private PFixedArrayDescriptor _fixedArrayDescriptor_;

   public AQuotedArrayRef() {
   }

   public AQuotedArrayRef(TQuotedName _quotedName_, PFixedArrayDescriptor _fixedArrayDescriptor_) {
      this.setQuotedName(_quotedName_);
      this.setFixedArrayDescriptor(_fixedArrayDescriptor_);
   }

   public Object clone() {
      return new AQuotedArrayRef((TQuotedName)this.cloneNode(this._quotedName_), (PFixedArrayDescriptor)this.cloneNode(this._fixedArrayDescriptor_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAQuotedArrayRef(this);
   }

   public TQuotedName getQuotedName() {
      return this._quotedName_;
   }

   public void setQuotedName(TQuotedName node) {
      if (this._quotedName_ != null) {
         this._quotedName_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._quotedName_ = node;
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
      return "" + this.toString(this._quotedName_) + this.toString(this._fixedArrayDescriptor_);
   }

   void removeChild(Node child) {
      if (this._quotedName_ == child) {
         this._quotedName_ = null;
      } else if (this._fixedArrayDescriptor_ == child) {
         this._fixedArrayDescriptor_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._quotedName_ == oldChild) {
         this.setQuotedName((TQuotedName)newChild);
      } else if (this._fixedArrayDescriptor_ == oldChild) {
         this.setFixedArrayDescriptor((PFixedArrayDescriptor)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
