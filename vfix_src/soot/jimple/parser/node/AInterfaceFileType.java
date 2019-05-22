package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AInterfaceFileType extends PFileType {
   private TInterface _interface_;

   public AInterfaceFileType() {
   }

   public AInterfaceFileType(TInterface _interface_) {
      this.setInterface(_interface_);
   }

   public Object clone() {
      return new AInterfaceFileType((TInterface)this.cloneNode(this._interface_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAInterfaceFileType(this);
   }

   public TInterface getInterface() {
      return this._interface_;
   }

   public void setInterface(TInterface node) {
      if (this._interface_ != null) {
         this._interface_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._interface_ = node;
   }

   public String toString() {
      return "" + this.toString(this._interface_);
   }

   void removeChild(Node child) {
      if (this._interface_ == child) {
         this._interface_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._interface_ == oldChild) {
         this.setInterface((TInterface)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
