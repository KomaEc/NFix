package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import java.util.Optional;

public interface NodeWithJavadoc<N extends Node> {
   Optional<Comment> getComment();

   Node setComment(Comment comment);

   default Optional<JavadocComment> getJavadocComment() {
      return this.getComment().filter((comment) -> {
         return comment instanceof JavadocComment;
      }).map((comment) -> {
         return (JavadocComment)comment;
      });
   }

   default Optional<Javadoc> getJavadoc() {
      return this.getJavadocComment().map(JavadocComment::parse);
   }

   default N setJavadocComment(String comment) {
      return this.setJavadocComment(new JavadocComment(comment));
   }

   default N setJavadocComment(JavadocComment comment) {
      this.setComment(comment);
      return (Node)this;
   }

   default N setJavadocComment(String indentation, Javadoc javadoc) {
      return this.setJavadocComment(javadoc.toComment(indentation));
   }

   default N setJavadocComment(Javadoc javadoc) {
      return this.setJavadocComment(javadoc.toComment());
   }

   default boolean removeJavaDocComment() {
      return this.hasJavaDocComment() && ((Comment)this.getComment().get()).remove();
   }

   default boolean hasJavaDocComment() {
      return this.getComment().isPresent() && this.getComment().get() instanceof JavadocComment;
   }
}
