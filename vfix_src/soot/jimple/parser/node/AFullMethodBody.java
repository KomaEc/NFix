package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AFullMethodBody extends PMethodBody {
   private TLBrace _lBrace_;
   private final LinkedList<PDeclaration> _declaration_ = new LinkedList();
   private final LinkedList<PStatement> _statement_ = new LinkedList();
   private final LinkedList<PCatchClause> _catchClause_ = new LinkedList();
   private TRBrace _rBrace_;

   public AFullMethodBody() {
   }

   public AFullMethodBody(TLBrace _lBrace_, List<?> _declaration_, List<?> _statement_, List<?> _catchClause_, TRBrace _rBrace_) {
      this.setLBrace(_lBrace_);
      this.setDeclaration(_declaration_);
      this.setStatement(_statement_);
      this.setCatchClause(_catchClause_);
      this.setRBrace(_rBrace_);
   }

   public Object clone() {
      return new AFullMethodBody((TLBrace)this.cloneNode(this._lBrace_), this.cloneList(this._declaration_), this.cloneList(this._statement_), this.cloneList(this._catchClause_), (TRBrace)this.cloneNode(this._rBrace_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFullMethodBody(this);
   }

   public TLBrace getLBrace() {
      return this._lBrace_;
   }

   public void setLBrace(TLBrace node) {
      if (this._lBrace_ != null) {
         this._lBrace_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._lBrace_ = node;
   }

   public LinkedList<PDeclaration> getDeclaration() {
      return this._declaration_;
   }

   public void setDeclaration(List<?> list) {
      Iterator var2 = this._declaration_.iterator();

      while(var2.hasNext()) {
         PDeclaration e = (PDeclaration)var2.next();
         e.parent((Node)null);
      }

      this._declaration_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PDeclaration e = (PDeclaration)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._declaration_.add(e);
      }

   }

   public LinkedList<PStatement> getStatement() {
      return this._statement_;
   }

   public void setStatement(List<?> list) {
      Iterator var2 = this._statement_.iterator();

      while(var2.hasNext()) {
         PStatement e = (PStatement)var2.next();
         e.parent((Node)null);
      }

      this._statement_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PStatement e = (PStatement)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._statement_.add(e);
      }

   }

   public LinkedList<PCatchClause> getCatchClause() {
      return this._catchClause_;
   }

   public void setCatchClause(List<?> list) {
      Iterator var2 = this._catchClause_.iterator();

      while(var2.hasNext()) {
         PCatchClause e = (PCatchClause)var2.next();
         e.parent((Node)null);
      }

      this._catchClause_.clear();
      var2 = list.iterator();

      while(var2.hasNext()) {
         Object obj_e = var2.next();
         PCatchClause e = (PCatchClause)obj_e;
         if (e.parent() != null) {
            e.parent().removeChild(e);
         }

         e.parent(this);
         this._catchClause_.add(e);
      }

   }

   public TRBrace getRBrace() {
      return this._rBrace_;
   }

   public void setRBrace(TRBrace node) {
      if (this._rBrace_ != null) {
         this._rBrace_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._rBrace_ = node;
   }

   public String toString() {
      return "" + this.toString(this._lBrace_) + this.toString(this._declaration_) + this.toString(this._statement_) + this.toString(this._catchClause_) + this.toString(this._rBrace_);
   }

   void removeChild(Node child) {
      if (this._lBrace_ == child) {
         this._lBrace_ = null;
      } else if (!this._declaration_.remove(child)) {
         if (!this._statement_.remove(child)) {
            if (!this._catchClause_.remove(child)) {
               if (this._rBrace_ == child) {
                  this._rBrace_ = null;
               } else {
                  throw new RuntimeException("Not a child.");
               }
            }
         }
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      if (this._lBrace_ == oldChild) {
         this.setLBrace((TLBrace)newChild);
      } else {
         ListIterator i = this._declaration_.listIterator();

         do {
            if (!i.hasNext()) {
               i = this._statement_.listIterator();

               do {
                  if (!i.hasNext()) {
                     i = this._catchClause_.listIterator();

                     do {
                        if (!i.hasNext()) {
                           if (this._rBrace_ == oldChild) {
                              this.setRBrace((TRBrace)newChild);
                              return;
                           }

                           throw new RuntimeException("Not a child.");
                        }
                     } while(i.next() != oldChild);

                     if (newChild != null) {
                        i.set((PCatchClause)newChild);
                        newChild.parent(this);
                        oldChild.parent((Node)null);
                        return;
                     }

                     i.remove();
                     oldChild.parent((Node)null);
                     return;
                  }
               } while(i.next() != oldChild);

               if (newChild != null) {
                  i.set((PStatement)newChild);
                  newChild.parent(this);
                  oldChild.parent((Node)null);
                  return;
               }

               i.remove();
               oldChild.parent((Node)null);
               return;
            }
         } while(i.next() != oldChild);

         if (newChild != null) {
            i.set((PDeclaration)newChild);
            newChild.parent(this);
            oldChild.parent((Node)null);
         } else {
            i.remove();
            oldChild.parent((Node)null);
         }
      }
   }
}
