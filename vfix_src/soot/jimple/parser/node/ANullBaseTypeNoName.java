package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANullBaseTypeNoName extends PBaseTypeNoName {
   private TNullType _nullType_;

   public ANullBaseTypeNoName() {
   }

   public ANullBaseTypeNoName(TNullType _nullType_) {
      this.setNullType(_nullType_);
   }

   public Object clone() {
      return new ANullBaseTypeNoName((TNullType)this.cloneNode(this._nullType_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANullBaseTypeNoName(this);
   }

   public TNullType getNullType() {
      return this._nullType_;
   }

   public void setNullType(TNullType node) {
      if (this._nullType_ != null) {
         this._nullType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._nullType_ = node;
   }

   public String toString() {
      return "" + this.toString(this._nullType_);
   }

   void removeChild(Node child) {
      if (this._nullType_ == child) {
         this._nullType_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._nullType_ == oldChild) {
         this.setNullType((TNullType)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
