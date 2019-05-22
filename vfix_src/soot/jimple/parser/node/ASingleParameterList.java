package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ASingleParameterList extends PParameterList {
   private PParameter _parameter_;

   public ASingleParameterList() {
   }

   public ASingleParameterList(PParameter _parameter_) {
      this.setParameter(_parameter_);
   }

   public Object clone() {
      return new ASingleParameterList((PParameter)this.cloneNode(this._parameter_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseASingleParameterList(this);
   }

   public PParameter getParameter() {
      return this._parameter_;
   }

   public void setParameter(PParameter node) {
      if (this._parameter_ != null) {
         this._parameter_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._parameter_ = node;
   }

   public String toString() {
      return "" + this.toString(this._parameter_);
   }

   void removeChild(Node child) {
      if (this._parameter_ == child) {
         this._parameter_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._parameter_ == oldChild) {
         this.setParameter((PParameter)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
