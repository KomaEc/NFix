package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AProtectedModifier extends PModifier {
   private TProtected _protected_;

   public AProtectedModifier() {
   }

   public AProtectedModifier(TProtected _protected_) {
      this.setProtected(_protected_);
   }

   public Object clone() {
      return new AProtectedModifier((TProtected)this.cloneNode(this._protected_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAProtectedModifier(this);
   }

   public TProtected getProtected() {
      return this._protected_;
   }

   public void setProtected(TProtected node) {
      if (this._protected_ != null) {
         this._protected_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._protected_ = node;
   }

   public String toString() {
      return "" + this.toString(this._protected_);
   }

   void removeChild(Node child) {
      if (this._protected_ == child) {
         this._protected_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._protected_ == oldChild) {
         this.setProtected((TProtected)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
