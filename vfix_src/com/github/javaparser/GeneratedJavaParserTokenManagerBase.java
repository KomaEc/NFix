package com.github.javaparser;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;

abstract class GeneratedJavaParserTokenManagerBase {
   private static TokenRange tokenRange(Token token) {
      JavaToken javaToken = token.javaToken;
      return new TokenRange(javaToken, javaToken);
   }

   static Comment createCommentFromToken(Token token) {
      String commentText = token.image;
      if (token.kind == 8) {
         return new JavadocComment(tokenRange(token), commentText.substring(3, commentText.length() - 2));
      } else if (token.kind == 9) {
         return new BlockComment(tokenRange(token), commentText.substring(2, commentText.length() - 2));
      } else if (token.kind != 5) {
         throw new AssertionError("Unexpectedly got passed a non-comment token.");
      } else {
         Range range;
         for(range = new Range(Position.pos(token.beginLine, token.beginColumn), Position.pos(token.endLine, token.endColumn)); commentText.endsWith("\r") || commentText.endsWith("\n"); commentText = commentText.substring(0, commentText.length() - 1)) {
         }

         range = range.withEnd(Position.pos(range.begin.line, range.begin.column + commentText.length()));
         LineComment comment = new LineComment(tokenRange(token), commentText.substring(2));
         comment.setRange(range);
         return comment;
      }
   }
}
