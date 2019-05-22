package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AStaticInvokeExpr extends PInvokeExpr {
   private TStaticinvoke _staticinvoke_;
   private PMethodSignature _methodSignature_;
   private TLParen _lParen_;
   private PArgList _argList_;
   private TRParen _rParen_;

   public AStaticInvokeExpr() {
   }

   public AStaticInvokeExpr(TStaticinvoke _staticinvoke_, PMethodSignature _methodSignature_, TLParen _lParen_, PArgList _argList_, TRParen _rParen_) {
      this.setStaticinvoke(_staticinvoke_);
      this.setMethodSignature(_methodSignature_);
      this.setLParen(_lParen_);
      this.setArgList(_argList_);
      this.setRParen(_rParen_);
   }

   public Object clone() {
      return new AStaticInvokeExpr((TStaticinvoke)this.cloneNode(this._staticinvoke_), (PMethodSignature)this.cloneNode(this._methodSignature_), (TLParen)this.cloneNode(this._lParen_), (PArgList)this.cloneNode(this._argList_), (TRParen)this.cloneNode(this._rParen_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAStaticInvokeExpr(this);
   }

   public TStaticinvoke getStaticinvoke() {
      return this._staticinvoke_;
   }

   public void setStaticinvoke(TStaticinvoke node) {
      if (this._staticinvoke_ != null) {
         this._staticinvoke_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._staticinvoke_ = node;
   }

   public PMethodSignature getMethodSignature() {
      return this._methodSignature_;
   }

   public void setMethodSignature(PMethodSignature node) {
      if (this._methodSignature_ != null) {
         this._methodSignature_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._methodSignature_ = node;
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

   public PArgList getArgList() {
      return this._argList_;
   }

   public void setArgList(PArgList node) {
      if (this._argList_ != null) {
         this._argList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._argList_ = node;
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

   public String toString() {
      return "" + this.toString(this._staticinvoke_) + this.toString(this._methodSignature_) + this.toString(this._lParen_) + this.toString(this._argList_) + this.toString(this._rParen_);
   }

   void removeChild(Node child) {
      if (this._staticinvoke_ == child) {
         this._staticinvoke_ = null;
      } else if (this._methodSignature_ == child) {
         this._methodSignature_ = null;
      } else if (this._lParen_ == child) {
         this._lParen_ = null;
      } else if (this._argList_ == child) {
         this._argList_ = null;
      } else if (this._rParen_ == child) {
         this._rParen_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._staticinvoke_ == oldChild) {
         this.setStaticinvoke((TStaticinvoke)newChild);
      } else if (this._methodSignature_ == oldChild) {
         this.setMethodSignature((PMethodSignature)newChild);
      } else if (this._lParen_ == oldChild) {
         this.setLParen((TLParen)newChild);
      } else if (this._argList_ == oldChild) {
         this.setArgList((PArgList)newChild);
      } else if (this._rParen_ == oldChild) {
         this.setRParen((TRParen)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
