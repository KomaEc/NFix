package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AMethodMember extends PMember {
   private final LinkedList<PModifier> _modifier_ = new LinkedList();
   private PType _type_;
   private PName _name_;
   private TLParen _lParen_;
   private PParameterList _parameterList_;
   private TRParen _rParen_;
   private PThrowsClause _throwsClause_;
   private PMethodBody _methodBody_;

   public AMethodMember() {
   }

   public AMethodMember(List<?> _modifier_, PType _type_, PName _name_, TLParen _lParen_, PParameterList _parameterList_, TRParen _rParen_, PThrowsClause _throwsClause_, PMethodBody _methodBody_) {
      this.setModifier(_modifier_);
      this.setType(_type_);
      this.setName(_name_);
      this.setLParen(_lParen_);
      this.setParameterList(_parameterList_);
      this.setRParen(_rParen_);
      this.setThrowsClause(_throwsClause_);
      this.setMethodBody(_methodBody_);
   }

   public Object clone() {
      return new AMethodMember(this.cloneList(this._modifier_), (PType)this.cloneNode(this._type_), (PName)this.cloneNode(this._name_), (TLParen)this.cloneNode(this._lParen_), (PParameterList)this.cloneNode(this._parameterList_), (TRParen)this.cloneNode(this._rParen_), (PThrowsClause)this.cloneNode(this._throwsClause_), (PMethodBody)this.cloneNode(this._methodBody_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAMethodMember(this);
   }

   public LinkedList<PModifier> getModifier() {
      return this._modifier_;
   }

   public void setModifier(List<?> list) {
      Iterator var2 = this._modifier_.iterator();

      while(var2.hasNext()) {
         PModifier e = (PModifier)var2.next();
         e.parent((Node)null);
      }

      this._modifier_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PModifier e = (PModifier)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._modifier_.add(e);
      }

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

   public PName getName() {
      return this._name_;
   }

   public void setName(PName node) {
      if (this._name_ != null) {
         this._name_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._name_ = node;
   }

   public TLParen getLParen() {
      return this._lParen_;
   }

   public void setLParen(TLParen node) {
      if (this._lParen_ != null) {
         this._lParen_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lParen_ = node;
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

   public TRParen getRParen() {
      return this._rParen_;
   }

   public void setRParen(TRParen node) {
      if (this._rParen_ != null) {
         this._rParen_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._rParen_ = node;
   }

   public PThrowsClause getThrowsClause() {
      return this._throwsClause_;
   }

   public void setThrowsClause(PThrowsClause node) {
      if (this._throwsClause_ != null) {
         this._throwsClause_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._throwsClause_ = node;
   }

   public PMethodBody getMethodBody() {
      return this._methodBody_;
   }

   public void setMethodBody(PMethodBody node) {
      if (this._methodBody_ != null) {
         this._methodBody_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._methodBody_ = node;
   }

   public String toString() {
      return "" + this.toString(this._modifier_) + this.toString(this._type_) + this.toString(this._name_) + this.toString(this._lParen_) + this.toString(this._parameterList_) + this.toString(this._rParen_) + this.toString(this._throwsClause_) + this.toString(this._methodBody_);
   }

   void removeChild(Node child) {
      if (!this._modifier_.remove(child)) {
         if (this._type_ == child) {
            this._type_ = null;
         } else if (this._name_ == child) {
            this._name_ = null;
         } else if (this._lParen_ == child) {
            this._lParen_ = null;
         } else if (this._parameterList_ == child) {
            this._parameterList_ = null;
         } else if (this._rParen_ == child) {
            this._rParen_ = null;
         } else if (this._throwsClause_ == child) {
            this._throwsClause_ = null;
         } else if (this._methodBody_ == child) {
            this._methodBody_ = null;
         } else {
            throw new RuntimeException("Not a child.");
         }
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      ListIterator i = this._modifier_.listIterator();

      do {
         if (!i.hasNext()) {
            if (this._type_ == oldChild) {
               this.setType((PType)newChild);
               return;
            }

            if (this._name_ == oldChild) {
               this.setName((PName)newChild);
               return;
            }

            if (this._lParen_ == oldChild) {
               this.setLParen((TLParen)newChild);
               return;
            }

            if (this._parameterList_ == oldChild) {
               this.setParameterList((PParameterList)newChild);
               return;
            }

            if (this._rParen_ == oldChild) {
               this.setRParen((TRParen)newChild);
               return;
            }

            if (this._throwsClause_ == oldChild) {
               this.setThrowsClause((PThrowsClause)newChild);
               return;
            }

            if (this._methodBody_ == oldChild) {
               this.setMethodBody((PMethodBody)newChild);
               return;
            }

            throw new RuntimeException("Not a child.");
         }
      } while(i.next() != oldChild);

      if (newChild != null) {
         i.set((PModifier)newChild);
         newChild.parent(this);
         oldChild.parent((Node)null);
      } else {
         i.remove();
         oldChild.parent((Node)null);
      }
   }
}
