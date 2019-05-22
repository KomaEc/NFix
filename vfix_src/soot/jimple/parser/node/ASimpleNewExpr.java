package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ASimpleNewExpr extends PNewExpr {
   private TNew _new_;
   private PBaseType _baseType_;

   public ASimpleNewExpr() {
   }

   public ASimpleNewExpr(TNew _new_, PBaseType _baseType_) {
      this.setNew(_new_);
      this.setBaseType(_baseType_);
   }

   public Object clone() {
      return new ASimpleNewExpr((TNew)this.cloneNode(this._new_), (PBaseType)this.cloneNode(this._baseType_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseASimpleNewExpr(this);
   }

   public TNew getNew() {
      return this._new_;
   }

   public void setNew(TNew node) {
      if (this._new_ != null) {
         this._new_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._new_ = node;
   }

   public PBaseType getBaseType() {
      return this._baseType_;
   }

   public void setBaseType(PBaseType node) {
      if (this._baseType_ != null) {
         this._baseType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._baseType_ = node;
   }

   public String toString() {
      return "" + this.toString(this._new_) + this.toString(this._baseType_);
   }

   void removeChild(Node child) {
      if (this._new_ == child) {
         this._new_ = null;
      } else if (this._baseType_ == child) {
         this._baseType_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._new_ == oldChild) {
         this.setNew((TNew)newChild);
      } else if (this._baseType_ == oldChild) {
         this.setBaseType((PBaseType)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
