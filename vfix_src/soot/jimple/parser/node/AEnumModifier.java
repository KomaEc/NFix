package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AEnumModifier extends PModifier {
   private TEnum _enum_;

   public AEnumModifier() {
   }

   public AEnumModifier(TEnum _enum_) {
      this.setEnum(_enum_);
   }

   public Object clone() {
      return new AEnumModifier((TEnum)this.cloneNode(this._enum_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAEnumModifier(this);
   }

   public TEnum getEnum() {
      return this._enum_;
   }

   public void setEnum(TEnum node) {
      if (this._enum_ != null) {
         this._enum_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._enum_ = node;
   }

   public String toString() {
      return "" + this.toString(this._enum_);
   }

   void removeChild(Node child) {
      if (this._enum_ == child) {
         this._enum_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._enum_ == oldChild) {
         this.setEnum((TEnum)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
