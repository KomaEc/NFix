package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AQuotedClassName extends PClassName {
   private TQuotedName _quotedName_;

   public AQuotedClassName() {
   }

   public AQuotedClassName(TQuotedName _quotedName_) {
      this.setQuotedName(_quotedName_);
   }

   public Object clone() {
      return new AQuotedClassName((TQuotedName)this.cloneNode(this._quotedName_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAQuotedClassName(this);
   }

   public TQuotedName getQuotedName() {
      return this._quotedName_;
   }

   public void setQuotedName(TQuotedName node) {
      if (this._quotedName_ != null) {
         this._quotedName_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._quotedName_ = node;
   }

   public String toString() {
      return "" + this.toString(this._quotedName_);
   }

   void removeChild(Node child) {
      if (this._quotedName_ == child) {
         this._quotedName_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._quotedName_ == oldChild) {
         this.setQuotedName((TQuotedName)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
