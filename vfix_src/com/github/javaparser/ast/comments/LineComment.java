package com.github.javaparser.ast.comments;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LineCommentMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class LineComment extends Comment {
   public LineComment() {
      this((TokenRange)null, "empty");
   }

   @AllFieldsConstructor
   public LineComment(String content) {
      this((TokenRange)null, content);
   }

   public LineComment(TokenRange tokenRange, String content) {
      super(tokenRange, content);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean isLineComment() {
      return true;
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public LineComment clone() {
      return (LineComment)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public LineCommentMetaModel getMetaModel() {
      return JavaParserMetaModel.lineCommentMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public LineComment asLineComment() {
      return this;
   }

   public void ifLineComment(Consumer<LineComment> action) {
      action.accept(this);
   }

   public Optional<LineComment> toLineComment() {
      return Optional.of(this);
   }
}
