package com.github.javaparser.ast.comments;

import com.github.javaparser.JavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.JavadocCommentMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class JavadocComment extends Comment {
   public JavadocComment() {
      this((TokenRange)null, "empty");
   }

   @AllFieldsConstructor
   public JavadocComment(String content) {
      this((TokenRange)null, content);
   }

   public JavadocComment(TokenRange tokenRange, String content) {
      super(tokenRange, content);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Javadoc parse() {
      return JavaParser.parseJavadoc(this.getContent());
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public JavadocComment clone() {
      return (JavadocComment)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public JavadocCommentMetaModel getMetaModel() {
      return JavaParserMetaModel.javadocCommentMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isJavadocComment() {
      return true;
   }

   public JavadocComment asJavadocComment() {
      return this;
   }

   public void ifJavadocComment(Consumer<JavadocComment> action) {
      action.accept(this);
   }

   public Optional<JavadocComment> toJavadocComment() {
      return Optional.of(this);
   }
}
