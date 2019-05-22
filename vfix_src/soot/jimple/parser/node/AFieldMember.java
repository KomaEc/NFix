package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AFieldMember extends PMember {
   private final LinkedList<PModifier> _modifier_ = new LinkedList();
   private PType _type_;
   private PName _name_;
   private TSemicolon _semicolon_;

   public AFieldMember() {
   }

   public AFieldMember(List<?> _modifier_, PType _type_, PName _name_, TSemicolon _semicolon_) {
      this.setModifier(_modifier_);
      this.setType(_type_);
      this.setName(_name_);
      this.setSemicolon(_semicolon_);
   }

   public Object clone() {
      return new AFieldMember(this.cloneList(this._modifier_), (PType)this.cloneNode(this._type_), (PName)this.cloneNode(this._name_), (TSemicolon)this.cloneNode(this._semicolon_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFieldMember(this);
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
      return "" + this.toString(this._modifier_) + this.toString(this._type_) + this.toString(this._name_) + this.toString(this._semicolon_);
   }

   void removeChild(Node child) {
      if (!this._modifier_.remove(child)) {
         if (this._type_ == child) {
            this._type_ = null;
         } else if (this._name_ == child) {
            this._name_ = null;
         } else if (this._semicolon_ == child) {
            this._semicolon_ = null;
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

            if (this._semicolon_ == oldChild) {
               this.setSemicolon((TSemicolon)newChild);
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
