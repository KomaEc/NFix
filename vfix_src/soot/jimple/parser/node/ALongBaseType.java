package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ALongBaseType extends PBaseType {
   private TLong _long_;

   public ALongBaseType() {
   }

   public ALongBaseType(TLong _long_) {
      this.setLong(_long_);
   }

   public Object clone() {
      return new ALongBaseType((TLong)this.cloneNode(this._long_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseALongBaseType(this);
   }

   public TLong getLong() {
      return this._long_;
   }

   public void setLong(TLong node) {
      if (this._long_ != null) {
         this._long_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._long_ = node;
   }

   public String toString() {
      return "" + this.toString(this._long_);
   }

   void removeChild(Node child) {
      if (this._long_ == child) {
         this._long_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._long_ == oldChild) {
         this.setLong((TLong)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
