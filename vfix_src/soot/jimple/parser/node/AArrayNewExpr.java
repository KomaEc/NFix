package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AArrayNewExpr extends PNewExpr {
   private TNewarray _newarray_;
   private TLParen _lParen_;
   private PNonvoidType _nonvoidType_;
   private TRParen _rParen_;
   private PFixedArrayDescriptor _fixedArrayDescriptor_;

   public AArrayNewExpr() {
   }

   public AArrayNewExpr(TNewarray _newarray_, TLParen _lParen_, PNonvoidType _nonvoidType_, TRParen _rParen_, PFixedArrayDescriptor _fixedArrayDescriptor_) {
      this.setNewarray(_newarray_);
      this.setLParen(_lParen_);
      this.setNonvoidType(_nonvoidType_);
      this.setRParen(_rParen_);
      this.setFixedArrayDescriptor(_fixedArrayDescriptor_);
   }

   public Object clone() {
      return new AArrayNewExpr((TNewarray)this.cloneNode(this._newarray_), (TLParen)this.cloneNode(this._lParen_), (PNonvoidType)this.cloneNode(this._nonvoidType_), (TRParen)this.cloneNode(this._rParen_), (PFixedArrayDescriptor)this.cloneNode(this._fixedArrayDescriptor_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAArrayNewExpr(this);
   }

   public TNewarray getNewarray() {
      return this._newarray_;
   }

   public void setNewarray(TNewarray node) {
      if (this._newarray_ != null) {
         this._newarray_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._newarray_ = node;
   }

   public TLParen getLParen() {
      return this._lParen_;
   }

   public void setLParen(TLParen node) {
      if (this._lParen_ != null) {
         this._lParen_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lParen_ = node;
   }

   public PNonvoidType getNonvoidType() {
      return this._nonvoidType_;
   }

   public void setNonvoidType(PNonvoidType node) {
      if (this._nonvoidType_ != null) {
         this._nonvoidType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._nonvoidType_ = node;
   }

   public TRParen getRParen() {
      return this._rParen_;
   }

   public void setRParen(TRParen node) {
      if (this._rParen_ != null) {
         this._rParen_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._rParen_ = node;
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
      return "" + this.toString(this._newarray_) + this.toString(this._lParen_) + this.toString(this._nonvoidType_) + this.toString(this._rParen_) + this.toString(this._fixedArrayDescriptor_);
   }

   void removeChild(Node child) {
      if (this._newarray_ == child) {
         this._newarray_ = null;
      } else if (this._lParen_ == child) {
         this._lParen_ = null;
      } else if (this._nonvoidType_ == child) {
         this._nonvoidType_ = null;
      } else if (this._rParen_ == child) {
         this._rParen_ = null;
      } else if (this._fixedArrayDescriptor_ == child) {
         this._fixedArrayDescriptor_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._newarray_ == oldChild) {
         this.setNewarray((TNewarray)newChild);
      } else if (this._lParen_ == oldChild) {
         this.setLParen((TLParen)newChild);
      } else if (this._nonvoidType_ == oldChild) {
         this.setNonvoidType((PNonvoidType)newChild);
      } else if (this._rParen_ == oldChild) {
         this.setRParen((TRParen)newChild);
      } else if (this._fixedArrayDescriptor_ == oldChild) {
         this.setFixedArrayDescriptor((PFixedArrayDescriptor)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
