package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AClassFileType extends PFileType {
   private TClass _theclass_;

   public AClassFileType() {
   }

   public AClassFileType(TClass _theclass_) {
      this.setTheclass(_theclass_);
   }

   public Object clone() {
      return new AClassFileType((TClass)this.cloneNode(this._theclass_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAClassFileType(this);
   }

   public TClass getTheclass() {
      return this._theclass_;
   }

   public void setTheclass(TClass node) {
      if (this._theclass_ != null) {
         this._theclass_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._theclass_ = node;
   }

   public String toString() {
      return "" + this.toString(this._theclass_);
   }

   void removeChild(Node child) {
      if (this._theclass_ == child) {
         this._theclass_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._theclass_ == oldChild) {
         this.setTheclass((TClass)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
