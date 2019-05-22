package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AImplementsClause extends PImplementsClause {
   private TImplements _implements_;
   private PClassNameList _classNameList_;

   public AImplementsClause() {
   }

   public AImplementsClause(TImplements _implements_, PClassNameList _classNameList_) {
      this.setImplements(_implements_);
      this.setClassNameList(_classNameList_);
   }

   public Object clone() {
      return new AImplementsClause((TImplements)this.cloneNode(this._implements_), (PClassNameList)this.cloneNode(this._classNameList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAImplementsClause(this);
   }

   public TImplements getImplements() {
      return this._implements_;
   }

   public void setImplements(TImplements node) {
      if (this._implements_ != null) {
         this._implements_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._implements_ = node;
   }

   public PClassNameList getClassNameList() {
      return this._classNameList_;
   }

   public void setClassNameList(PClassNameList node) {
      if (this._classNameList_ != null) {
         this._classNameList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._classNameList_ = node;
   }

   public String toString() {
      return "" + this.toString(this._implements_) + this.toString(this._classNameList_);
   }

   void removeChild(Node child) {
      if (this._implements_ == child) {
         this._implements_ = null;
      } else if (this._classNameList_ == child) {
         this._classNameList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._implements_ == oldChild) {
         this.setImplements((TImplements)newChild);
      } else if (this._classNameList_ == oldChild) {
         this.setClassNameList((PClassNameList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
