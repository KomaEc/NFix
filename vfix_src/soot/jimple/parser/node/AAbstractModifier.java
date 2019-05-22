package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AAbstractModifier extends PModifier {
   private TAbstract _abstract_;

   public AAbstractModifier() {
   }

   public AAbstractModifier(TAbstract _abstract_) {
      this.setAbstract(_abstract_);
   }

   public Object clone() {
      return new AAbstractModifier((TAbstract)this.cloneNode(this._abstract_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAAbstractModifier(this);
   }

   public TAbstract getAbstract() {
      return this._abstract_;
   }

   public void setAbstract(TAbstract node) {
      if (this._abstract_ != null) {
         this._abstract_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._abstract_ = node;
   }

   public String toString() {
      return "" + this.toString(this._abstract_);
   }

   void removeChild(Node child) {
      if (this._abstract_ == child) {
         this._abstract_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._abstract_ == oldChild) {
         this.setAbstract((TAbstract)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
