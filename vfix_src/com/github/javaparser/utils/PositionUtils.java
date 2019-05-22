package com.github.javaparser.utils;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.Type;
import java.util.List;

public final class PositionUtils {
   private PositionUtils() {
   }

   public static <T extends Node> void sortByBeginPosition(List<T> nodes) {
      sortByBeginPosition(nodes, false);
   }

   public static <T extends Node> void sortByBeginPosition(NodeList<T> nodes) {
      sortByBeginPosition(nodes, false);
   }

   public static <T extends Node> void sortByBeginPosition(List<T> nodes, final boolean ignoringAnnotations) {
      nodes.sort((o1, o2) -> {
         return compare(o1, o2, ignoringAnnotations);
      });
   }

   public static boolean areInOrder(Node a, Node b) {
      return areInOrder(a, b, false);
   }

   public static boolean areInOrder(Node a, Node b, boolean ignoringAnnotations) {
      return compare(a, b, ignoringAnnotations) <= 0;
   }

   private static int compare(Node a, Node b, boolean ignoringAnnotations) {
      if (a.getRange().isPresent() && !b.getRange().isPresent()) {
         return -1;
      } else if (!a.getRange().isPresent() && b.getRange().isPresent()) {
         return 1;
      } else if (!a.getRange().isPresent() && !b.getRange().isPresent()) {
         return 0;
      } else if (ignoringAnnotations) {
         int signLine = Integer.signum(beginLineWithoutConsideringAnnotation(a) - beginLineWithoutConsideringAnnotation(b));
         return signLine == 0 ? Integer.signum(beginColumnWithoutConsideringAnnotation(a) - beginColumnWithoutConsideringAnnotation(b)) : signLine;
      } else {
         Position aBegin = (Position)a.getBegin().get();
         Position bBegin = (Position)b.getBegin().get();
         int signLine = Integer.signum(aBegin.line - bBegin.line);
         return signLine == 0 ? Integer.signum(aBegin.column - bBegin.column) : signLine;
      }
   }

   public static AnnotationExpr getLastAnnotation(Node node) {
      if (node instanceof NodeWithAnnotations) {
         NodeList<AnnotationExpr> annotations = NodeList.nodeList(((NodeWithAnnotations)node).getAnnotations());
         if (annotations.isEmpty()) {
            return null;
         } else {
            sortByBeginPosition(annotations);
            return (AnnotationExpr)annotations.get(annotations.size() - 1);
         }
      } else {
         return null;
      }
   }

   private static int beginLineWithoutConsideringAnnotation(Node node) {
      return ((Range)beginNodeWithoutConsideringAnnotations(node).getRange().get()).begin.line;
   }

   private static int beginColumnWithoutConsideringAnnotation(Node node) {
      return ((Range)beginNodeWithoutConsideringAnnotations(node).getRange().get()).begin.column;
   }

   private static Node beginNodeWithoutConsideringAnnotations(Node node) {
      if (!(node instanceof MethodDeclaration) && !(node instanceof FieldDeclaration)) {
         if (node instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration casted = (ClassOrInterfaceDeclaration)node;
            return casted.getName();
         } else {
            return node;
         }
      } else {
         NodeWithType<?, Type> casted = (NodeWithType)node;
         return casted.getType();
      }
   }

   public static boolean nodeContains(Node container, Node contained, boolean ignoringAnnotations) {
      Range containedRange = (Range)contained.getRange().get();
      Range containerRange = (Range)container.getRange().get();
      if (ignoringAnnotations && getLastAnnotation(container) != null) {
         if (!container.containsWithin(contained)) {
            return false;
         } else if (container instanceof NodeWithAnnotations) {
            int bl = beginLineWithoutConsideringAnnotation(container);
            int bc = beginColumnWithoutConsideringAnnotation(container);
            if (bl > containedRange.begin.line) {
               return false;
            } else if (bl == containedRange.begin.line && bc > containedRange.begin.column) {
               return false;
            } else if (containerRange.end.line < containedRange.end.line) {
               return false;
            } else {
               return containerRange.end.line != containedRange.end.line || containerRange.end.column >= containedRange.end.column;
            }
         } else {
            return true;
         }
      } else {
         return container.containsWithin(contained);
      }
   }
}
