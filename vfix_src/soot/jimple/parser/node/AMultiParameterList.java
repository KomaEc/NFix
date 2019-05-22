package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class AMultiParameterList extends PParameterList {
   private PParameter _parameter_;
   private TComma _comma_;
   private PParameterList _parameterList_;

   public AMultiParameterList() {
   }

   public AMultiParameterList(PParameter _parameter_, TComma _comma_, PParameterList _parameterList_) {
      this.setParameter(_parameter_);
      this.setComma(_comma_);
      this.setParameterList(_parameterList_);
   }

   public Object clone() {
      return new AMultiParameterList((PParameter)this.cloneNode(this._parameter_), (TComma)this.cloneNode(this._comma_), (PParameterList)this.cloneNode(this._parameterList_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMultiParameterList(this);
   }

   public PParameter getParameter() {
      return this._parameter_;
   }

   public void setParameter(PParameter node) {
      if (this._parameter_ != null) {
         this._parameter_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._parameter_ = node;
   }

   public TComma getComma() {
      return this._comma_;
   }

   public void setComma(TComma node) {
      if (this._comma_ != null) {
         this._comma_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._comma_ = node;
   }

   public PParameterList getParameterList() {
      return this._parameterList_;
   }

   public void setParameterList(PParameterList node) {
      if (this._parameterList_ != null) {
         this._parameterList_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._parameterList_ = node;
   }

   public String toString() {
      return "" + this.toString(this._parameter_) + this.toString(this._comma_) + this.toString(this._parameterList_);
   }

   void removeChild(Node child) {
      if (this._parameter_ == child) {
         this._parameter_ = null;
      } else if (this._comma_ == child) {
         this._comma_ = null;
      } else if (this._parameterList_ == child) {
         this._parameterList_ = null;
      } else {
         throw new RuntimeException("Not a child.");
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._parameter_ == oldChild) {
         this.setParameter((PParameter)newChild);
      } else if (this._comma_ == oldChild) {
         this.setComma((TComma)newChild);
      } else if (this._parameterList_ == oldChild) {
         this.setParameterList((PParameterList)newChild);
      } else {
         throw new RuntimeException("Not a child.");
      }
   }
}
