package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AIdentityStatement extends PStatement {
   private PLocalName _localName_;
   private TColonEquals _colonEquals_;
   private TAtIdentifier _atIdentifier_;
   private PType _type_;
   private TSemicolon _semicolon_;

   public AIdentityStatement() {
   }

   public AIdentityStatement(PLocalName _localName_, TColonEquals _colonEquals_, TAtIdentifier _atIdentifier_, PType _type_, TSemicolon _semicolon_) {
      this.setLocalName(_localName_);
      this.setColonEquals(_colonEquals_);
      this.setAtIdentifier(_atIdentifier_);
      this.setType(_type_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AIdentityStatement((PLocalName)this.cloneNode(this._localName_), (TColonEquals)this.cloneNode(this._colonEquals_), (TAtIdentifier)this.cloneNode(this._atIdentifier_), (PType)this.cloneNode(this._type_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAIdentityStatement(this);
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

   public TColonEquals getColonEquals() {
      return this._colonEquals_;
   }

   public void setColonEquals(TColonEquals node) {
      if (this._colonEquals_ != null) {
         this._colonEquals_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._colonEquals_ = node;
   }

   public TAtIdentifier getAtIdentifier() {
      return this._atIdentifier_;
   }

   public void setAtIdentifier(TAtIdentifier node) {
      if (this._atIdentifier_ != null) {
         this._atIdentifier_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._atIdentifier_ = node;
   }

   public PType getType() {
      return this._type_;
   }

   public void setType(PType node) {
      if (this._type_ != null) {
         this._type_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._type_ = node;
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
      return "" + this.toString(this._localName_) + this.toString(this._colonEquals_) + this.toString(this._atIdentifier_) + this.toString(this._type_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._localName_ == child) {
         this._localName_ = null;
      } else if (this._colonEquals_ == child) {
         this._colonEquals_ = null;
      } else if (this._atIdentifier_ == child) {
         this._atIdentifier_ = null;
      } else if (this._type_ == child) {
         this._type_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._localName_ == oldChild) {
         this.setLocalName((PLocalName)newChild);
      } else if (this._colonEquals_ == oldChild) {
         this.setColonEquals((TColonEquals)newChild);
      } else if (this._atIdentifier_ == oldChild) {
         this.setAtIdentifier((TAtIdentifier)newChild);
      } else if (this._type_ == oldChild) {
         this.setType((PType)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
