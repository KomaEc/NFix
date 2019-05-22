package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AClzzConstant extends PConstant {
   private TClass _id_;
   private TStringConstant _stringConstant_;

   public AClzzConstant() {
   }

   public AClzzConstant(TClass _id_, TStringConstant _stringConstant_) {
      this.setId(_id_);
      this.setStringConstant(_stringConstant_);
   }

   public Object clone() {
      return new AClzzConstant((TClass)this.cloneNode(this._id_), (TStringConstant)this.cloneNode(this._stringConstant_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAClzzConstant(this);
   }

   public TClass getId() {
      return this._id_;
   }

   public void setId(TClass node) {
      if (this._id_ != null) {
         this._id_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._id_ = node;
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

   public String toString() {
      return "" + this.toString(this._id_) + this.toString(this._stringConstant_);
   }

   void removeChild(Node child) {
      if (this._id_ == child) {
         this._id_ = null;
      } else if (this._stringConstant_ == child) {
         this._stringConstant_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._id_ == oldChild) {
         this.setId((TClass)newChild);
      } else if (this._stringConstant_ == oldChild) {
         this.setStringConstant((TStringConstant)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
