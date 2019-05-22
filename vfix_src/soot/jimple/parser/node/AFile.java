package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import soot.jimple.parser.analysis.Analysis;

public final class AFile extends PFile {
   private final LinkedList<PModifier> _modifier_ = new LinkedList();
   private PFileType _fileType_;
   private PClassName _className_;
   private PExtendsClause _extendsClause_;
   private PImplementsClause _implementsClause_;
   private PFileBody _fileBody_;

   public AFile() {
   }

   public AFile(List<?> _modifier_, PFileType _fileType_, PClassName _className_, PExtendsClause _extendsClause_, PImplementsClause _implementsClause_, PFileBody _fileBody_) {
      this.setModifier(_modifier_);
      this.setFileType(_fileType_);
      this.setClassName(_className_);
      this.setExtendsClause(_extendsClause_);
      this.setImplementsClause(_implementsClause_);
      this.setFileBody(_fileBody_);
   }

   public Object clone() {
      return new AFile(this.cloneList(this._modifier_), (PFileType)this.cloneNode(this._fileType_), (PClassName)this.cloneNode(this._className_), (PExtendsClause)this.cloneNode(this._extendsClause_), (PImplementsClause)this.cloneNode(this._implementsClause_), (PFileBody)this.cloneNode(this._fileBody_));
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseAFile(this);
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

   public PFileType getFileType() {
      return this._fileType_;
   }

   public void setFileType(PFileType node) {
      if (this._fileType_ != null) {
         this._fileType_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._fileType_ = node;
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

   public PExtendsClause getExtendsClause() {
      return this._extendsClause_;
   }

   public void setExtendsClause(PExtendsClause node) {
      if (this._extendsClause_ != null) {
         this._extendsClause_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._extendsClause_ = node;
   }

   public PImplementsClause getImplementsClause() {
      return this._implementsClause_;
   }

   public void setImplementsClause(PImplementsClause node) {
      if (this._implementsClause_ != null) {
         this._implementsClause_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._implementsClause_ = node;
   }

   public PFileBody getFileBody() {
      return this._fileBody_;
   }

   public void setFileBody(PFileBody node) {
      if (this._fileBody_ != null) {
         this._fileBody_.parent((Node)null);
      }

      if (node != null) {
         if (node.parent() != null) {
            node.parent().removeChild(node);
         }

         node.parent(this);
      }

      this._fileBody_ = node;
   }

   public String toString() {
      return "" + this.toString(this._modifier_) + this.toString(this._fileType_) + this.toString(this._className_) + this.toString(this._extendsClause_) + this.toString(this._implementsClause_) + this.toString(this._fileBody_);
   }

   void removeChild(Node child) {
      if (!this._modifier_.remove(child)) {
         if (this._fileType_ == child) {
            this._fileType_ = null;
         } else if (this._className_ == child) {
            this._className_ = null;
         } else if (this._extendsClause_ == child) {
            this._extendsClause_ = null;
         } else if (this._implementsClause_ == child) {
            this._implementsClause_ = null;
         } else if (this._fileBody_ == child) {
            this._fileBody_ = null;
         } else {
            throw new RuntimeException("Not a child.");
         }
      }
   }

   void replaceChild(Node oldChild, Node newChild) {
      ListIterator i = this._modifier_.listIterator();

      do {
         if (!i.hasNext()) {
            if (this._fileType_ == oldChild) {
               this.setFileType((PFileType)newChild);
               return;
            }

            if (this._className_ == oldChild) {
               this.setClassName((PClassName)newChild);
               return;
            }

            if (this._extendsClause_ == oldChild) {
               this.setExtendsClause((PExtendsClause)newChild);
               return;
            }

            if (this._implementsClause_ == oldChild) {
               this.setImplementsClause((PImplementsClause)newChild);
               return;
            }

            if (this._fileBody_ == oldChild) {
               this.setFileBody((PFileBody)newChild);
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
