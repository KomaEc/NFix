package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AAssignStatement extends PStatement {
   private PVariable _variable_;
   private TEquals _equals_;
   private PExpression _expression_;
   private TSemicolon _semicolon_;

   public AAssignStatement() {
   }

   public AAssignStatement(PVariable _variable_, TEquals _equals_, PExpression _expression_, TSemicolon _semicolon_) {
      this.setVariable(_variable_);
      this.setEquals(_equals_);
      this.setExpression(_expression_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AAssignStatement((PVariable)this.cloneNode(this._variable_), (TEquals)this.cloneNode(this._equals_), (PExpression)this.cloneNode(this._expression_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAAssignStatement(this);
   }

   public PVariable getVariable() {
      return this._variable_;
   }

   public void setVariable(PVariable node) {
      if (this._variable_ != null) {
         this._variable_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._variable_ = node;
   }

   public TEquals getEquals() {
      return this._equals_;
   }

   public void setEquals(TEquals node) {
      if (this._equals_ != null) {
         this._equals_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._equals_ = node;
   }

   public PExpression getExpression() {
      return this._expression_;
   }

   public void setExpression(PExpression node) {
      if (this._expression_ != null) {
         this._expression_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._expression_ = node;
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
      return "" + this.toString(this._variable_) + this.toString(this._equals_) + this.toString(this._expression_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (this._variable_ == child) {
         this._variable_ = null;
      } else if (this._equals_ == child) {
         this._equals_ = null;
      } else if (this._expression_ == child) {
         this._expression_ = null;
      } else if (this._semicolon_ == child) {
         this._semicolon_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._variable_ == oldChild) {
         this.setVariable((PVariable)newChild);
      } else if (this._equals_ == oldChild) {
         this.setEquals((TEquals)newChild);
      } else if (this._expression_ == oldChild) {
         this.setExpression((PExpression)newChild);
      } else if (this._semicolon_ == oldChild) {
         this.setSemicolon((TSemicolon)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
