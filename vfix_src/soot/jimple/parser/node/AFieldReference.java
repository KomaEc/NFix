package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AFieldReference extends PReference {
   private PFieldRef _fieldRef_;

   public AFieldReference() {
   }

   public AFieldReference(PFieldRef _fieldRef_) {
      this.setFieldRef(_fieldRef_);
   }

   public Object clone() {
      return new AFieldReference((PFieldRef)this.cloneNode(this._fieldRef_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFieldReference(this);
   }

   public PFieldRef getFieldRef() {
      return this._fieldRef_;
   }

   public void setFieldRef(PFieldRef node) {
      if (this._fieldRef_ != null) {
         this._fieldRef_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._fieldRef_ = node;
   }

   public String toString() {
      return "" + this.toString(this._fieldRef_);
   }

   void removeChild(Node child) {
      if (this._fieldRef_ == child) {
         this._fieldRef_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._fieldRef_ == oldChild) {
         this.setFieldRef((PFieldRef)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
