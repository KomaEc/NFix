package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AAnnotationModifier extends PModifier {
   private TAnnotation _annotation_;

   public AAnnotationModifier() {
   }

   public AAnnotationModifier(TAnnotation _annotation_) {
      this.setAnnotation(_annotation_);
   }

   public Object clone() {
      return new AAnnotationModifier((TAnnotation)this.cloneNode(this._annotation_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAAnnotationModifier(this);
   }

   public TAnnotation getAnnotation() {
      return this._annotation_;
   }

   public void setAnnotation(TAnnotation node) {
      if (this._annotation_ != null) {
         this._annotation_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._annotation_ = node;
   }

   public String toString() {
      return "" + this.toString(this._annotation_);
   }

   void removeChild(Node child) {
      if (this._annotation_ == child) {
         this._annotation_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._annotation_ == oldChild) {
         this.setAnnotation((TAnnotation)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
