package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.printer.SourcePrinter;

public class CsmComment implements CsmElement {
   static void process(Comment comment, SourcePrinter printer) {
      String content = printer.normalizeEolInTextBlock(comment.getContent());
      if (comment instanceof BlockComment) {
         printer.print("/*");
         printer.print(content);
         printer.println("*/");
      } else if (comment instanceof JavadocComment) {
         printer.print("/**");
         printer.print(content);
         printer.println("*/");
      } else {
         if (!(comment instanceof LineComment)) {
            throw new UnsupportedOperationException(comment.getClass().getSimpleName());
         }

         printer.print("//");
         printer.print(content);
         printer.println();
      }

   }

   public void prettyPrint(Node node, SourcePrinter printer) {
      node.getComment().ifPresent((c) -> {
         process(c, printer);
      });
   }
}
