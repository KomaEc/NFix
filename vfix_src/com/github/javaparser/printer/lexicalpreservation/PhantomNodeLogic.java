package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.AstObserver;
import com.github.javaparser.ast.observer.AstObserverAdapter;
import com.github.javaparser.ast.type.UnknownType;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

class PhantomNodeLogic {
   private static final int LEVELS_TO_EXPLORE = 3;
   private static final Map<Node, Boolean> isPhantomNodeCache = Collections.synchronizedMap(new IdentityHashMap());
   private static final AstObserver cacheCleaner = new AstObserverAdapter() {
      public void parentChange(Node observedNode, Node previousParent, Node newParent) {
         PhantomNodeLogic.isPhantomNodeCache.remove(observedNode);
      }
   };

   static boolean isPhantomNode(Node node) {
      if (isPhantomNodeCache.containsKey(node)) {
         return (Boolean)isPhantomNodeCache.get(node);
      } else if (node instanceof UnknownType) {
         return true;
      } else {
         boolean res = node.getParentNode().isPresent() && !((Range)((Node)node.getParentNode().get()).getRange().get()).contains((Range)node.getRange().get()) || inPhantomNode(node, 3);
         isPhantomNodeCache.put(node, res);
         node.register(cacheCleaner);
         return res;
      }
   }

   private static boolean inPhantomNode(Node node, int levels) {
      return node.getParentNode().isPresent() && (isPhantomNode((Node)node.getParentNode().get()) || inPhantomNode((Node)node.getParentNode().get(), levels - 1));
   }
}
