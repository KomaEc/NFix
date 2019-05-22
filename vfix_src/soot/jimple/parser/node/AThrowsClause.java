package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AThrowsClause extends PThrowsClause {
   private TThrows _throws_;
   private PClassNameList _classNameList_;

   public AThrowsClause() {
   }

   public AThrowsClause(TThrows _throws_, PClassNameList _classNameList_) {
      this.setThrows(_throws_);
      this.setClassNameList(_classNameList_);
   }

   public Object clone() {
      return new AThrowsClause((TThrows)this.cloneNode(this._throws_), (PClassNameList)this.cloneNode(this._classNameList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAThrowsClause(this);
   }

   public TThrows getThrows() {
      return this._throws_;
   }

   public void setThrows(TThrows node) {
      if (this._throws_ != null) {
         this._throws_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._throws_ = node;
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
      return "" + this.toString(this._throws_) + this.toString(this._classNameList_);
   }

   void removeChild(Node child) {
      if (this._throws_ == child) {
         this._throws_ = null;
      } else if (this._classNameList_ == child) {
         this._classNameList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._throws_ == oldChild) {
         this.setThrows((TThrows)newChild);
      } else if (this._classNameList_ == oldChild) {
         this.setClassNameList((PClassNameList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
