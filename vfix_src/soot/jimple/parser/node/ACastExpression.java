package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ACastExpression extends PExpression {
   private TLParen _lParen_;
   private PNonvoidType _nonvoidType_;
   private TRParen _rParen_;
   private PImmediate _immediate_;

   public ACastExpression() {
   }

   public ACastExpression(TLParen _lParen_, PNonvoidType _nonvoidType_, TRParen _rParen_, PImmediate _immediate_) {
      this.setLParen(_lParen_);
      this.setNonvoidType(_nonvoidType_);
      this.setRParen(_rParen_);
      this.setImmediate(_immediate_);
   }

   public Object clone() {
      return new ACastExpression((TLParen)this.cloneNode(this._lParen_), (PNonvoidType)this.cloneNode(this._nonvoidType_), (TRParen)this.cloneNode(this._rParen_), (PImmediate)this.cloneNode(this._immediate_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseACastExpression(this);
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

   public PImmediate getImmediate() {
      return this._immediate_;
   }

   public void setImmediate(PImmediate node) {
      if (this._immediate_ != null) {
         this._immediate_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._immediate_ = node;
   }

   public String toString() {
      return "" + this.toString(this._lParen_) + this.toString(this._nonvoidType_) + this.toString(this._rParen_) + this.toString(this._immediate_);
   }

   void removeChild(Node child) {
      if (this._lParen_ == child) {
         this._lParen_ = null;
      } else if (this._nonvoidType_ == child) {
         this._nonvoidType_ = null;
      } else if (this._rParen_ == child) {
         this._rParen_ = null;
      } else if (this._immediate_ == child) {
         this._immediate_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._lParen_ == oldChild) {
         this.setLParen((TLParen)newChild);
      } else if (this._nonvoidType_ == oldChild) {
         this.setNonvoidType((PNonvoidType)newChild);
      } else if (this._rParen_ == oldChild) {
         this.setRParen((TRParen)newChild);
      } else if (this._immediate_ == oldChild) {
         this.setImmediate((PImmediate)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
