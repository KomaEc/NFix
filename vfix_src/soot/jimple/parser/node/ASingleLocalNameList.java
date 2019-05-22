package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ASingleLocalNameList extends PLocalNameList {
   private PLocalName _localName_;

   public ASingleLocalNameList() {
   }

   public ASingleLocalNameList(PLocalName _localName_) {
      this.setLocalName(_localName_);
   }

   public Object clone() {
      return new ASingleLocalNameList((PLocalName)this.cloneNode(this._localName_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseASingleLocalNameList(this);
   }

   public PLocalName getLocalName() {
      return this._localName_;
   }

   public void setLocalName(PLocalName node) {
      if (this._localName_ != null) {
         this._localName_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._localName_ = node;
   }

   public String toString() {
      return "" + this.toString(this._localName_);
   }

   void removeChild(Node child) {
      if (this._localName_ == child) {
         this._localName_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._localName_ == oldChild) {
         this.setLocalName((PLocalName)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
