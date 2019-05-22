package com.github.javaparser.ast.comments;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.CommentMetaModel;
import com.github.javaparser.metamodel.InternalProperty;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Comment extends Node {
   private String content;
   @InternalProperty
   private Node commentedNode;

   @AllFieldsConstructor
   public Comment(String content) {
      this((TokenRange)null, content);
   }

   public Comment(TokenRange tokenRange, String content) {
      super(tokenRange);
      this.setContent(content);
      this.customInitialization();
   }

   public String getContent() {
      return this.content;
   }

   public Comment setContent(final String content) {
      Utils.assertNotNull(content);
      if (content == this.content) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CONTENT, this.content, content);
         this.content = content;
         return this;
      }
   }

   public boolean isLineComment() {
      return false;
   }

   public LineComment asLineComment() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LineComment", this));
   }

   public Optional<Node> getCommentedNode() {
      return Optional.ofNullable(this.commentedNode);
   }

   public Comment setCommentedNode(Node commentedNode) {
      this.notifyPropertyChange(ObservableProperty.COMMENTED_NODE, this.commentedNode, commentedNode);
      if (commentedNode == null) {
         this.commentedNode = null;
         return this;
      } else if (commentedNode == this) {
         throw new IllegalArgumentException();
      } else if (commentedNode instanceof Comment) {
         throw new IllegalArgumentException();
      } else {
         this.commentedNode = commentedNode;
         return this;
      }
   }

   public boolean isOrphan() {
      return this.commentedNode == null;
   }

   public boolean remove() {
      if (this.commentedNode != null) {
         this.commentedNode.setComment((Comment)null);
         return true;
      } else {
         return this.getParentNode().isPresent() ? ((Node)this.getParentNode().get()).removeOrphanComment(this) : false;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public Comment clone() {
      return (Comment)this.accept(new CloneVisitor(), (Object)null);
   }

   public CommentMetaModel getMetaModel() {
      return JavaParserMetaModel.commentMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isBlockComment() {
      return false;
   }

   public BlockComment asBlockComment() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an BlockComment", this));
   }

   public boolean isJavadocComment() {
      return false;
   }

   public JavadocComment asJavadocComment() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an JavadocComment", this));
   }

   public void ifBlockComment(Consumer<BlockComment> action) {
   }

   public void ifJavadocComment(Consumer<JavadocComment> action) {
   }

   public void ifLineComment(Consumer<LineComment> action) {
   }

   public Optional<BlockComment> toBlockComment() {
      return Optional.empty();
   }

   public Optional<JavadocComment> toJavadocComment() {
      return Optional.empty();
   }

   public Optional<LineComment> toLineComment() {
      return Optional.empty();
   }
}
