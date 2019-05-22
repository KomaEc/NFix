package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AExtendsClause extends PExtendsClause {
   private TExtends _extends_;
   private PClassName _className_;

   public AExtendsClause() {
   }

   public AExtendsClause(TExtends _extends_, PClassName _className_) {
      this.setExtends(_extends_);
      this.setClassName(_className_);
   }

   public Object clone() {
      return new AExtendsClause((TExtends)this.cloneNode(this._extends_), (PClassName)this.cloneNode(this._className_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAExtendsClause(this);
   }

   public TExtends getExtends() {
      return this._extends_;
   }

   public void setExtends(TExtends node) {
      if (this._extends_ != null) {
         this._extends_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._extends_ = node;
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
      return "" + this.toString(this._extends_) + this.toString(this._className_);
   }

   void removeChild(Node child) {
      if (this._extends_ == child) {
         this._extends_ = null;
      } else if (this._className_ == child) {
         this._className_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._extends_ == oldChild) {
         this.setExtends((TExtends)newChild);
      } else if (this._className_ == oldChild) {
         this.setClassName((PClassName)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
