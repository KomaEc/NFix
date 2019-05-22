package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.utils.PositionUtils;
import java.util.LinkedList;
import java.util.List;

public class CsmOrphanCommentsEnding implements CsmElement {
   public void prettyPrint(Node node, SourcePrinter printer) {
      List<Node> everything = new LinkedList();
      everything.addAll(node.getChildNodes());
      PositionUtils.sortByBeginPosition((List)everything);
      if (!everything.isEmpty()) {
         int commentsAtEnd = 0;
         boolean findingComments = true;

         while(findingComments && commentsAtEnd < everything.size()) {
            Node last = (Node)everything.get(everything.size() - 1 - commentsAtEnd);
            findingComments = last instanceof Comment;
            if (findingComments) {
               ++commentsAtEnd;
            }
         }

         for(int i = 0; i < commentsAtEnd; ++i) {
            Comment c = (Comment)everything.get(everything.size() - commentsAtEnd + i);
            CsmComment.process(c, printer);
         }

      }
   }
}
