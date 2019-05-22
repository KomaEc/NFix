package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ADynamicInvokeExpr extends PInvokeExpr {
   private TDynamicinvoke _dynamicinvoke_;
   private TStringConstant _stringConstant_;
   private PUnnamedMethodSignature _dynmethod_;
   private TLParen _firstl_;
   private PArgList _dynargs_;
   private TRParen _firstr_;
   private PMethodSignature _bsm_;
   private TLParen _lParen_;
   private PArgList _staticargs_;
   private TRParen _rParen_;

   public ADynamicInvokeExpr() {
   }

   public ADynamicInvokeExpr(TDynamicinvoke _dynamicinvoke_, TStringConstant _stringConstant_, PUnnamedMethodSignature _dynmethod_, TLParen _firstl_, PArgList _dynargs_, TRParen _firstr_, PMethodSignature _bsm_, TLParen _lParen_, PArgList _staticargs_, TRParen _rParen_) {
      this.setDynamicinvoke(_dynamicinvoke_);
      this.setStringConstant(_stringConstant_);
      this.setDynmethod(_dynmethod_);
      this.setFirstl(_firstl_);
      this.setDynargs(_dynargs_);
      this.setFirstr(_firstr_);
      this.setBsm(_bsm_);
      this.setLParen(_lParen_);
      this.setStaticargs(_staticargs_);
      this.setRParen(_rParen_);
   }

   public Object clone() {
      return new ADynamicInvokeExpr((TDynamicinvoke)this.cloneNode(this._dynamicinvoke_), (TStringConstant)this.cloneNode(this._stringConstant_), (PUnnamedMethodSignature)this.cloneNode(this._dynmethod_), (TLParen)this.cloneNode(this._firstl_), (PArgList)this.cloneNode(this._dynargs_), (TRParen)this.cloneNode(this._firstr_), (PMethodSignature)this.cloneNode(this._bsm_), (TLParen)this.cloneNode(this._lParen_), (PArgList)this.cloneNode(this._staticargs_), (TRParen)this.cloneNode(this._rParen_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseADynamicInvokeExpr(this);
   }

   public TDynamicinvoke getDynamicinvoke() {
      return this._dynamicinvoke_;
   }

   public void setDynamicinvoke(TDynamicinvoke node) {
      if (this._dynamicinvoke_ != null) {
         this._dynamicinvoke_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._dynamicinvoke_ = node;
   }

   public TStringConstant getStringConstant() {
      return this._stringConstant_;
   }

   public void setStringConstant(TStringConstant node) {
      if (this._stringConstant_ != null) {
         this._stringConstant_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._stringConstant_ = node;
   }

   public PUnnamedMethodSignature getDynmethod() {
      return this._dynmethod_;
   }

   public void setDynmethod(PUnnamedMethodSignature node) {
      if (this._dynmethod_ != null) {
         this._dynmethod_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._dynmethod_ = node;
   }

   public TLParen getFirstl() {
      return this._firstl_;
   }

   public void setFirstl(TLParen node) {
      if (this._firstl_ != null) {
         this._firstl_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._firstl_ = node;
   }

   public PArgList getDynargs() {
      return this._dynargs_;
   }

   public void setDynargs(PArgList node) {
      if (this._dynargs_ != null) {
         this._dynargs_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._dynargs_ = node;
   }

   public TRParen getFirstr() {
      return this._firstr_;
   }

   public void setFirstr(TRParen node) {
      if (this._firstr_ != null) {
         this._firstr_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._firstr_ = node;
   }

   public PMethodSignature getBsm() {
      return this._bsm_;
   }

   public void setBsm(PMethodSignature node) {
      if (this._bsm_ != null) {
         this._bsm_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._bsm_ = node;
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

   public PArgList getStaticargs() {
      return this._staticargs_;
   }

   public void setStaticargs(PArgList node) {
      if (this._staticargs_ != null) {
         this._staticargs_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._staticargs_ = node;
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
      return "" + this.toString(this._dynamicinvoke_) + this.toString(this._stringConstant_) + this.toString(this._dynmethod_) + this.toString(this._firstl_) + this.toString(this._dynargs_) + this.toString(this._firstr_) + this.toString(this._bsm_) + this.toString(this._lParen_) + this.toString(this._staticargs_) + this.toString(this._rParen_);
   }

   void removeChild(Node child) {
      if (this._dynamicinvoke_ == child) {
         this._dynamicinvoke_ = null;
      } else if (this._stringConstant_ == child) {
         this._stringConstant_ = null;
      } else if (this._dynmethod_ == child) {
         this._dynmethod_ = null;
      } else if (this._firstl_ == child) {
         this._firstl_ = null;
      } else if (this._dynargs_ == child) {
         this._dynargs_ = null;
      } else if (this._firstr_ == child) {
         this._firstr_ = null;
      } else if (this._bsm_ == child) {
         this._bsm_ = null;
      } else if (this._lParen_ == child) {
         this._lParen_ = null;
      } else if (this._staticargs_ == child) {
         this._staticargs_ = null;
      } else if (this._rParen_ == child) {
         this._rParen_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._dynamicinvoke_ == oldChild) {
         this.setDynamicinvoke((TDynamicinvoke)newChild);
      } else if (this._stringConstant_ == oldChild) {
         this.setStringConstant((TStringConstant)newChild);
      } else if (this._dynmethod_ == oldChild) {
         this.setDynmethod((PUnnamedMethodSignature)newChild);
      } else if (this._firstl_ == oldChild) {
         this.setFirstl((TLParen)newChild);
      } else if (this._dynargs_ == oldChild) {
         this.setDynargs((PArgList)newChild);
      } else if (this._firstr_ == oldChild) {
         this.setFirstr((TRParen)newChild);
      } else if (this._bsm_ == oldChild) {
         this.setBsm((PMethodSignature)newChild);
      } else if (this._lParen_ == oldChild) {
         this.setLParen((TLParen)newChild);
      } else if (this._staticargs_ == oldChild) {
         this.setStaticargs((PArgList)newChild);
      } else if (this._rParen_ == oldChild) {
         this.setRParen((TRParen)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
