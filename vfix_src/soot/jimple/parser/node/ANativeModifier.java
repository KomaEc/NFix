package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ANativeModifier extends PModifier {
   private TNative _native_;

   public ANativeModifier() {
   }

   public ANativeModifier(TNative _native_) {
      this.setNative(_native_);
   }

   public Object clone() {
      return new ANativeModifier((TNative)this.cloneNode(this._native_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseANativeModifier(this);
   }

   public TNative getNative() {
      return this._native_;
   }

   public void setNative(TNative node) {
      if (this._native_ != null) {
         this._native_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._native_ = node;
   }

   public String toString() {
      return "" + this.toString(this._native_);
   }

   void removeChild(Node child) {
      if (this._native_ == child) {
         this._native_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._native_ == oldChild) {
         this.setNative((TNative)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
