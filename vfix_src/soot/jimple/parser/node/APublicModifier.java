package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class APublicModifier extends PModifier {
   private TPublic _public_;

   public APublicModifier() {
   }

   public APublicModifier(TPublic _public_) {
      this.setPublic(_public_);
   }

   public Object clone() {
      return new APublicModifier((TPublic)this.cloneNode(this._public_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAPublicModifier(this);
   }

   public TPublic getPublic() {
      return this._public_;
   }

   public void setPublic(TPublic node) {
      if (this._public_ != null) {
         this._public_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._public_ = node;
   }

   public String toString() {
      return "" + this.toString(this._public_);
   }

   void removeChild(Node child) {
      if (this._public_ == child) {
         this._public_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._public_ == oldChild) {
         this.setPublic((TPublic)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
