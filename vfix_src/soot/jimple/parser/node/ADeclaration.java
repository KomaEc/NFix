package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class ADeclaration extends PDeclaration {
   private PJimpleType _jimpleType_;
   private PLocalNameList _localNameList_;
   private TSemicolon _semicolon_;

   public ADeclaration() {
   }

   public ADeclaration(PJimpleType _jimpleType_, PLocalNameList _localNameList_, TSemicolon _semicolon_) {
      this.setJimpleType(_jimpleType_);
      this.setLocalNameList(_localNameList_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new ADeclaration((PJimpleType)this.cloneNode(this._jimpleType_), (PLocalNameList)this.cloneNode(this._localNameList_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseADeclaration(this);
   }

   public PJimpleType getJimpleType() {
      return this._jimpleType_;
   }

   public void setJimpleType(PJimpleType node) {
      if (this._jimpleType_ != null) {
         this._jimpleType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._jimpleType_ = node;
   }

   public PLocalNameList getLocalNameList() {
      return this._localNameList_;
   }

   public void setLocalNameList(PLocalNameList node) {
      if (this._localNameList_ != null) {
         this._localNameList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._localNameList_ = node;
   }

   public TSemicolon getSemicolon() {
      return this._semicolon_;
   }

   public void setSemicolon(TSemicolon node) {
      if (this._semicolon_ != null) {
         this._semicolon_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._semicolon_ = node;
   }

   public String toString() {
      return "" + this.toString(this._jimpleType_) + this.toString(this._localNameList_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._jimpleType_ == child) {
         this._jimpleType_ = null;
      } else if (this._localNameList_ == child) {
         this._localNameList_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._jimpleType_ == oldChild) {
         this.setJimpleType((PJimpleType)newChild);
      } else if (this._localNameList_ == oldChild) {
         this.setLocalNameList((PLocalNameList)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
