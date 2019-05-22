package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AClassNameSingleClassNameList extends PClassNameList {
   private PClassName _className_;

   public AClassNameSingleClassNameList() {
   }

   public AClassNameSingleClassNameList(PClassName _className_) {
      this.setClassName(_className_);
   }

   public Object clone() {
      return new AClassNameSingleClassNameList((PClassName)this.cloneNode(this._className_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAClassNameSingleClassNameList(this);
   }

   public PClassName getClassName() {
      return this._className_;
   }

   public void setClassName(PClassName node) {
      if (this._className_ != null) {
         this._className_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._className_ = node;
   }

   public String toString() {
      return "" + this.toString(this._className_);
   }

   void removeChild(Node child) {
      if (this._className_ == child) {
         this._className_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._className_ == oldChild) {
         this.setClassName((PClassName)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
