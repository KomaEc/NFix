package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.utils.PositionUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

class CommentsInserter {
   private final ParserConfiguration configuration;

   CommentsInserter(ParserConfiguration configuration) {
      this.configuration = configuration;
   }

   private void insertComments(CompilationUnit cu, TreeSet<Comment> comments) {
      if (!comments.isEmpty()) {
         List<Node> children = cu.getChildNodes();
         Comment firstComment = (Comment)comments.iterator().next();
         if (cu.getPackageDeclaration().isPresent() && (children.isEmpty() || PositionUtils.areInOrder(firstComment, (Node)cu.getPackageDeclaration().get()))) {
            cu.setComment(firstComment);
            comments.remove(firstComment);
         }

      }
   }

   void insertComments(Node node, TreeSet<Comment> commentsToAttribute) {
      if (!commentsToAttribute.isEmpty()) {
         if (node instanceof CompilationUnit) {
            this.insertComments((CompilationUnit)node, commentsToAttribute);
         }

         List<Node> children = node.getChildNodes();
         Iterator var4 = children.iterator();

         while(var4.hasNext()) {
            Node child = (Node)var4.next();
            TreeSet<Comment> commentsInsideChild = new TreeSet(Node.NODE_BY_BEGIN_POSITION);
            commentsInsideChild.addAll((Collection)commentsToAttribute.stream().filter((cx) -> {
               return cx.getRange().isPresent();
            }).filter((cx) -> {
               return PositionUtils.nodeContains(child, cx, this.configuration.isIgnoreAnnotationsWhenAttributingComments());
            }).collect(Collectors.toList()));
            commentsToAttribute.removeAll(commentsInsideChild);
            this.insertComments(child, commentsInsideChild);
         }

         this.attributeLineCommentsOnSameLine(commentsToAttribute, children);
         Comment previousComment = null;
         List<Comment> attributedComments = new LinkedList();
         List<Node> childrenAndComments = new LinkedList();
         childrenAndComments.addAll(children);
         commentsToAttribute.removeAll(attributedComments);
         childrenAndComments.addAll(commentsToAttribute);
         PositionUtils.sortByBeginPosition(childrenAndComments, this.configuration.isIgnoreAnnotationsWhenAttributingComments());
         Iterator var7 = childrenAndComments.iterator();

         while(true) {
            while(var7.hasNext()) {
               Node thing = (Node)var7.next();
               if (thing instanceof Comment) {
                  previousComment = (Comment)thing;
                  if (!previousComment.isOrphan()) {
                     previousComment = null;
                  }
               } else if (previousComment != null && !thing.getComment().isPresent() && (!this.configuration.isDoNotAssignCommentsPrecedingEmptyLines() || !this.thereAreLinesBetween(previousComment, thing))) {
                  thing.setComment(previousComment);
                  attributedComments.add(previousComment);
                  previousComment = null;
               }
            }

            commentsToAttribute.removeAll(attributedComments);
            var7 = commentsToAttribute.iterator();

            while(var7.hasNext()) {
               Comment c = (Comment)var7.next();
               if (c.isOrphan()) {
                  node.addOrphanComment(c);
               }
            }

            return;
         }
      }
   }

   private void attributeLineCommentsOnSameLine(TreeSet<Comment> commentsToAttribute, List<Node> children) {
      List<Comment> attributedComments = new LinkedList();
      commentsToAttribute.stream().filter((comment) -> {
         return comment.getRange().isPresent();
      }).filter(Comment::isLineComment).forEach((comment) -> {
         children.stream().filter((child) -> {
            return child.getRange().isPresent();
         }).forEach((child) -> {
            Range commentRange = (Range)comment.getRange().get();
            Range childRange = (Range)child.getRange().get();
            if (childRange.end.line == commentRange.begin.line && this.attributeLineCommentToNodeOrChild(child, comment.asLineComment())) {
               attributedComments.add(comment);
            }

         });
      });
      commentsToAttribute.removeAll(attributedComments);
   }

   private boolean attributeLineCommentToNodeOrChild(Node node, LineComment lineComment) {
      if (node.getRange().isPresent() && lineComment.getRange().isPresent()) {
         if (((Position)node.getBegin().get()).line == ((Position)lineComment.getBegin().get()).line && !node.getComment().isPresent()) {
            if (!(node instanceof Comment)) {
               node.setComment(lineComment);
            }

            return true;
         } else {
            List<Node> children = new LinkedList();
            children.addAll(node.getChildNodes());
            PositionUtils.sortByBeginPosition((List)children);
            Collections.reverse(children);
            Iterator var4 = children.iterator();

            Node child;
            do {
               if (!var4.hasNext()) {
                  return false;
               }

               child = (Node)var4.next();
            } while(!this.attributeLineCommentToNodeOrChild(child, lineComment));

            return true;
         }
      } else {
         return false;
      }
   }

   private boolean thereAreLinesBetween(Node a, Node b) {
      if (a.getRange().isPresent() && b.getRange().isPresent()) {
         if (!PositionUtils.areInOrder(a, b)) {
            return this.thereAreLinesBetween(b, a);
         } else {
            int endOfA = ((Position)a.getEnd().get()).line;
            return ((Position)b.getBegin().get()).line > endOfA + 1;
         }
      } else {
         return true;
      }
   }
}
