package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.InitializerDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class InitializerDeclaration extends BodyDeclaration<InitializerDeclaration> implements NodeWithJavadoc<InitializerDeclaration>, NodeWithBlockStmt<InitializerDeclaration> {
   private boolean isStatic;
   private BlockStmt body;

   public InitializerDeclaration() {
      this((TokenRange)null, false, new BlockStmt());
   }

   @AllFieldsConstructor
   public InitializerDeclaration(boolean isStatic, BlockStmt body) {
      this((TokenRange)null, isStatic, body);
   }

   public InitializerDeclaration(TokenRange tokenRange, boolean isStatic, BlockStmt body) {
      super(tokenRange);
      this.setStatic(isStatic);
      this.setBody(body);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public BlockStmt getBody() {
      return this.body;
   }

   public boolean isStatic() {
      return this.isStatic;
   }

   public InitializerDeclaration setBody(final BlockStmt body) {
      Utils.assertNotNull(body);
      if (body == this.body) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.BODY, this.body, body);
         if (this.body != null) {
            this.body.setParentNode((Node)null);
         }

         this.body = body;
         this.setAsParentNodeOf(body);
         return this;
      }
   }

   public InitializerDeclaration setStatic(final boolean isStatic) {
      if (isStatic == this.isStatic) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.STATIC, this.isStatic, isStatic);
         this.isStatic = isStatic;
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public InitializerDeclaration clone() {
      return (InitializerDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public InitializerDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.initializerDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((BlockStmt)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isInitializerDeclaration() {
      return true;
   }

   public InitializerDeclaration asInitializerDeclaration() {
      return this;
   }

   public void ifInitializerDeclaration(Consumer<InitializerDeclaration> action) {
      action.accept(this);
   }

   public Optional<InitializerDeclaration> toInitializerDeclaration() {
      return Optional.of(this);
   }
}
