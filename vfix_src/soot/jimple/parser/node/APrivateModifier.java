package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class APrivateModifier extends PModifier {
   private TPrivate _private_;

   public APrivateModifier() {
   }

   public APrivateModifier(TPrivate _private_) {
      this.setPrivate(_private_);
   }

   public Object clone() {
      return new APrivateModifier((TPrivate)this.cloneNode(this._private_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAPrivateModifier(this);
   }

   public TPrivate getPrivate() {
      return this._private_;
   }

   public void setPrivate(TPrivate node) {
      if (this._private_ != null) {
         this._private_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._private_ = node;
   }

   public String toString() {
      return "" + this.toString(this._private_);
   }

   void removeChild(Node child) {
      if (this._private_ == child) {
         this._private_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._private_ == oldChild) {
         this.setPrivate((TPrivate)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
