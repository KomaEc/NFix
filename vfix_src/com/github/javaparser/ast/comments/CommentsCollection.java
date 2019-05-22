package com.github.javaparser.ast.comments;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CommentsCollection {
   private final TreeSet<Comment> comments;

   public CommentsCollection() {
      this.comments = new TreeSet(Node.NODE_BY_BEGIN_POSITION);
   }

   public CommentsCollection(Collection<Comment> commentsToCopy) {
      this.comments = new TreeSet(Node.NODE_BY_BEGIN_POSITION);
      this.comments.addAll(commentsToCopy);
   }

   public Set<LineComment> getLineComments() {
      return (Set)this.comments.stream().filter((comment) -> {
         return comment instanceof LineComment;
      }).map((comment) -> {
         return (LineComment)comment;
      }).collect(Collectors.toCollection(() -> {
         return new TreeSet(Node.NODE_BY_BEGIN_POSITION);
      }));
   }

   public Set<BlockComment> getBlockComments() {
      return (Set)this.comments.stream().filter((comment) -> {
         return comment instanceof BlockComment;
      }).map((comment) -> {
         return (BlockComment)comment;
      }).collect(Collectors.toCollection(() -> {
         return new TreeSet(Node.NODE_BY_BEGIN_POSITION);
      }));
   }

   public Set<JavadocComment> getJavadocComments() {
      return (Set)this.comments.stream().filter((comment) -> {
         return comment instanceof JavadocComment;
      }).map((comment) -> {
         return (JavadocComment)comment;
      }).collect(Collectors.toCollection(() -> {
         return new TreeSet(Node.NODE_BY_BEGIN_POSITION);
      }));
   }

   public void addComment(Comment comment) {
      this.comments.add(comment);
   }

   public boolean contains(Comment comment) {
      if (!comment.getRange().isPresent()) {
         return false;
      } else {
         Range commentRange = (Range)comment.getRange().get();
         Iterator var3 = this.getComments().iterator();

         Range cRange;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            Comment c = (Comment)var3.next();
            if (!c.getRange().isPresent()) {
               return false;
            }

            cRange = (Range)c.getRange().get();
         } while(!cRange.begin.equals(commentRange.begin) || cRange.end.line != commentRange.end.line || Math.abs(cRange.end.column - commentRange.end.column) >= 2);

         return true;
      }
   }

   public TreeSet<Comment> getComments() {
      return this.comments;
   }

   public int size() {
      return this.comments.size();
   }

   public CommentsCollection minus(CommentsCollection other) {
      CommentsCollection result = new CommentsCollection();
      result.comments.addAll((Collection)this.comments.stream().filter((comment) -> {
         return !other.contains(comment);
      }).collect(Collectors.toList()));
      return result;
   }

   public CommentsCollection copy() {
      return new CommentsCollection(this.comments);
   }
}
