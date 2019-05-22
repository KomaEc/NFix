package com.github.javaparser.ast.modules;

import com.github.javaparser.JavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ModuleDeclarationMetaModel;
import com.github.javaparser.utils.Utils;

public final class ModuleDeclaration extends Node implements NodeWithName<ModuleDeclaration>, NodeWithAnnotations<ModuleDeclaration> {
   private Name name;
   private NodeList<AnnotationExpr> annotations;
   private boolean isOpen;
   private NodeList<ModuleStmt> moduleStmts;

   public ModuleDeclaration() {
      this((TokenRange)null, new NodeList(), new Name(), false, new NodeList());
   }

   public ModuleDeclaration(Name name, boolean isOpen) {
      this((TokenRange)null, new NodeList(), name, isOpen, new NodeList());
   }

   @AllFieldsConstructor
   public ModuleDeclaration(NodeList<AnnotationExpr> annotations, Name name, boolean isOpen, NodeList<ModuleStmt> moduleStmts) {
      this((TokenRange)null, annotations, name, isOpen, moduleStmts);
   }

   public ModuleDeclaration(TokenRange tokenRange, NodeList<AnnotationExpr> annotations, Name name, boolean isOpen, NodeList<ModuleStmt> moduleStmts) {
      super(tokenRange);
      this.setAnnotations(annotations);
      this.setName(name);
      this.setOpen(isOpen);
      this.setModuleStmts(moduleStmts);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Name getName() {
      return this.name;
   }

   public ModuleDeclaration setName(final Name name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.NAME, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public ModuleDeclaration setAnnotations(final NodeList<AnnotationExpr> annotations) {
      Utils.assertNotNull(annotations);
      if (annotations == this.annotations) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ANNOTATIONS, this.annotations, annotations);
         if (this.annotations != null) {
            this.annotations.setParentNode((Node)null);
         }

         this.annotations = annotations;
         this.setAsParentNodeOf(annotations);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.moduleStmts.size(); ++i) {
            if (this.moduleStmts.get(i) == node) {
               this.moduleStmts.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public boolean isOpen() {
      return this.isOpen;
   }

   public ModuleDeclaration setOpen(final boolean isOpen) {
      if (isOpen == this.isOpen) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.OPEN, this.isOpen, isOpen);
         this.isOpen = isOpen;
         return this;
      }
   }

   public NodeList<ModuleStmt> getModuleStmts() {
      return this.moduleStmts;
   }

   public ModuleDeclaration setModuleStmts(final NodeList<ModuleStmt> moduleStmts) {
      Utils.assertNotNull(moduleStmts);
      if (moduleStmts == this.moduleStmts) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODULE_STMTS, this.moduleStmts, moduleStmts);
         if (this.moduleStmts != null) {
            this.moduleStmts.setParentNode((Node)null);
         }

         this.moduleStmts = moduleStmts;
         this.setAsParentNodeOf(moduleStmts);
         return this;
      }
   }

   public ModuleDeclaration clone() {
      return (ModuleDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ModuleDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.moduleDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.set(i, (Node)((AnnotationExpr)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.moduleStmts.size(); ++i) {
            if (this.moduleStmts.get(i) == node) {
               this.moduleStmts.set(i, (Node)((ModuleStmt)replacementNode));
               return true;
            }
         }

         if (node == this.name) {
            this.setName((Name)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }

   public ModuleDeclaration addDirective(String directive) {
      return this.addDirective(JavaParser.parseModuleDirective(directive));
   }

   public ModuleDeclaration addDirective(ModuleStmt directive) {
      this.getModuleStmts().add((Node)directive);
      return this;
   }
}
