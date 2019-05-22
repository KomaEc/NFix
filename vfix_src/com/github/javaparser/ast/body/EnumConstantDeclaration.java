package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.EnumConstantDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class EnumConstantDeclaration extends BodyDeclaration<EnumConstantDeclaration> implements NodeWithJavadoc<EnumConstantDeclaration>, NodeWithSimpleName<EnumConstantDeclaration>, NodeWithArguments<EnumConstantDeclaration>, Resolvable<ResolvedEnumConstantDeclaration> {
   private SimpleName name;
   private NodeList<Expression> arguments;
   private NodeList<BodyDeclaration<?>> classBody;

   public EnumConstantDeclaration() {
      this((TokenRange)null, new NodeList(), new SimpleName(), new NodeList(), new NodeList());
   }

   public EnumConstantDeclaration(String name) {
      this((TokenRange)null, new NodeList(), new SimpleName(name), new NodeList(), new NodeList());
   }

   @AllFieldsConstructor
   public EnumConstantDeclaration(NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<Expression> arguments, NodeList<BodyDeclaration<?>> classBody) {
      this((TokenRange)null, annotations, name, arguments, classBody);
   }

   public EnumConstantDeclaration(TokenRange tokenRange, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<Expression> arguments, NodeList<BodyDeclaration<?>> classBody) {
      super(tokenRange, annotations);
      this.setName(name);
      this.setArguments(arguments);
      this.setClassBody(classBody);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<Expression> getArguments() {
      return this.arguments;
   }

   public NodeList<BodyDeclaration<?>> getClassBody() {
      return this.classBody;
   }

   public SimpleName getName() {
      return this.name;
   }

   public EnumConstantDeclaration setArguments(final NodeList<Expression> arguments) {
      Utils.assertNotNull(arguments);
      if (arguments == this.arguments) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ARGUMENTS, this.arguments, arguments);
         if (this.arguments != null) {
            this.arguments.setParentNode((Node)null);
         }

         this.arguments = arguments;
         this.setAsParentNodeOf(arguments);
         return this;
      }
   }

   public EnumConstantDeclaration setClassBody(final NodeList<BodyDeclaration<?>> classBody) {
      Utils.assertNotNull(classBody);
      if (classBody == this.classBody) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CLASS_BODY, this.classBody, classBody);
         if (this.classBody != null) {
            this.classBody.setParentNode((Node)null);
         }

         this.classBody = classBody;
         this.setAsParentNodeOf(classBody);
         return this;
      }
   }

   public EnumConstantDeclaration setName(final SimpleName name) {
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

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == node) {
               this.arguments.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.classBody.size(); ++i) {
            if (this.classBody.get(i) == node) {
               this.classBody.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public EnumConstantDeclaration clone() {
      return (EnumConstantDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public EnumConstantDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.enumConstantDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.arguments.size(); ++i) {
            if (this.arguments.get(i) == node) {
               this.arguments.set(i, (Node)((Expression)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.classBody.size(); ++i) {
            if (this.classBody.get(i) == node) {
               this.classBody.set(i, (Node)((BodyDeclaration)replacementNode));
               return true;
            }
         }

         if (node == this.name) {
            this.setName((SimpleName)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isEnumConstantDeclaration() {
      return true;
   }

   public EnumConstantDeclaration asEnumConstantDeclaration() {
      return this;
   }

   public void ifEnumConstantDeclaration(Consumer<EnumConstantDeclaration> action) {
      action.accept(this);
   }

   public ResolvedEnumConstantDeclaration resolve() {
      return (ResolvedEnumConstantDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedEnumConstantDeclaration.class);
   }

   public Optional<EnumConstantDeclaration> toEnumConstantDeclaration() {
      return Optional.of(this);
   }
}
