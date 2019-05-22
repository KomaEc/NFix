package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AInterfaceNonstaticInvoke extends PNonstaticInvoke {
   private TInterfaceinvoke _interfaceinvoke_;

   public AInterfaceNonstaticInvoke() {
   }

   public AInterfaceNonstaticInvoke(TInterfaceinvoke _interfaceinvoke_) {
      this.setInterfaceinvoke(_interfaceinvoke_);
   }

   public Object clone() {
      return new AInterfaceNonstaticInvoke((TInterfaceinvoke)this.cloneNode(this._interfaceinvoke_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAInterfaceNonstaticInvoke(this);
   }

   public TInterfaceinvoke getInterfaceinvoke() {
      return this._interfaceinvoke_;
   }

   public void setInterfaceinvoke(TInterfaceinvoke node) {
      if (this._interfaceinvoke_ != null) {
         this._interfaceinvoke_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._interfaceinvoke_ = node;
   }

   public String toString() {
      return "" + this.toString(this._interfaceinvoke_);
   }

   void removeChild(Node child) {
      if (this._interfaceinvoke_ == child) {
         this._interfaceinvoke_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._interfaceinvoke_ == oldChild) {
         this.setInterfaceinvoke((TInterfaceinvoke)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
