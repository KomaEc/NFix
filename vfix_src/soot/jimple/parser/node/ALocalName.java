package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ALocalName extends PLocalName {
   private PName _name_;

   public ALocalName() {
   }

   public ALocalName(PName _name_) {
      this.setName(_name_);
   }

   public Object clone() {
      return new ALocalName((PName)this.cloneNode(this._name_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseALocalName(this);
   }

   public PName getName() {
      return this._name_;
   }

   public void setName(PName node) {
      if (this._name_ != null) {
         this._name_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._name_ = node;
   }

   public String toString() {
      return "" + this.toString(this._name_);
   }

   void removeChild(Node child) {
      if (this._name_ == child) {
         this._name_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._name_ == oldChild) {
         this.setName((PName)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
