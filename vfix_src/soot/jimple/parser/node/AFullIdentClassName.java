package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AFullIdentClassName extends PClassName {
   private TFullIdentifier _fullIdentifier_;

   public AFullIdentClassName() {
   }

   public AFullIdentClassName(TFullIdentifier _fullIdentifier_) {
      this.setFullIdentifier(_fullIdentifier_);
   }

   public Object clone() {
      return new AFullIdentClassName((TFullIdentifier)this.cloneNode(this._fullIdentifier_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFullIdentClassName(this);
   }

   public TFullIdentifier getFullIdentifier() {
      return this._fullIdentifier_;
   }

   public void setFullIdentifier(TFullIdentifier node) {
      if (this._fullIdentifier_ != null) {
         this._fullIdentifier_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._fullIdentifier_ = node;
   }

   public String toString() {
      return "" + this.toString(this._fullIdentifier_);
   }

   void removeChild(Node child) {
      if (this._fullIdentifier_ == child) {
         this._fullIdentifier_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._fullIdentifier_ == oldChild) {
         this.setFullIdentifier((TFullIdentifier)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
