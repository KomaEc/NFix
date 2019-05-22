package com.github.javaparser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

class RangedList<T extends Node> {
   TokenRange range;
   NodeList<T> list;

   RangedList(NodeList<T> list) {
      this.range = new TokenRange(JavaToken.INVALID, JavaToken.INVALID);
      this.list = list;
   }

   void beginAt(JavaToken begin) {
      this.range = this.range.withBegin(begin);
   }

   void endAt(JavaToken end) {
      this.range = this.range.withEnd(end);
   }

   void add(T t) {
      if (this.list == null) {
         this.list = new NodeList();
      }

      this.list.add(t);
   }
}
