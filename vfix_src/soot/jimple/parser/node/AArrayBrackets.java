package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AArrayBrackets extends PArrayBrackets {
   private TLBracket _lBracket_;
   private TRBracket _rBracket_;

   public AArrayBrackets() {
   }

   public AArrayBrackets(TLBracket _lBracket_, TRBracket _rBracket_) {
      this.setLBracket(_lBracket_);
      this.setRBracket(_rBracket_);
   }

   public Object clone() {
      return new AArrayBrackets((TLBracket)this.cloneNode(this._lBracket_), (TRBracket)this.cloneNode(this._rBracket_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAArrayBrackets(this);
   }

   public TLBracket getLBracket() {
      return this._lBracket_;
   }

   public void setLBracket(TLBracket node) {
      if (this._lBracket_ != null) {
         this._lBracket_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lBracket_ = node;
   }

   public TRBracket getRBracket() {
      return this._rBracket_;
   }

   public void setRBracket(TRBracket node) {
      if (this._rBracket_ != null) {
         this._rBracket_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._rBracket_ = node;
   }

   public String toString() {
      return "" + this.toString(this._lBracket_) + this.toString(this._rBracket_);
   }

   void removeChild(Node child) {
      if (this._lBracket_ == child) {
         this._lBracket_ = null;
      } else if (this._rBracket_ == child) {
         this._rBracket_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._lBracket_ == oldChild) {
         this.setLBracket((TLBracket)newChild);
      } else if (this._rBracket_ == oldChild) {
         this.setRBracket((TRBracket)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
