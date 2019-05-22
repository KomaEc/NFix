package com.github.javaparser.ast.comments;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.BlockCommentMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class BlockComment extends Comment {
   public BlockComment() {
      this((TokenRange)null, "empty");
   }

   @AllFieldsConstructor
   public BlockComment(String content) {
      this((TokenRange)null, content);
   }

   public BlockComment(TokenRange tokenRange, String content) {
      super(tokenRange, content);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public BlockComment clone() {
      return (BlockComment)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public BlockCommentMetaModel getMetaModel() {
      return JavaParserMetaModel.blockCommentMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isBlockComment() {
      return true;
   }

   public BlockComment asBlockComment() {
      return this;
   }

   public void ifBlockComment(Consumer<BlockComment> action) {
      action.accept(this);
   }

   public Optional<BlockComment> toBlockComment() {
      return Optional.of(this);
   }
}
