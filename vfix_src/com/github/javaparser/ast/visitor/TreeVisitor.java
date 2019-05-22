package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public abstract class TreeVisitor {
   public void visitLeavesFirst(Node node) {
      Iterator var2 = node.getChildNodes().iterator();

      while(var2.hasNext()) {
         Node child = (Node)var2.next();
         this.visitLeavesFirst(child);
      }

      this.process(node);
   }

   public void visitPreOrder(Node node) {
      this.process(node);
      (new ArrayList(node.getChildNodes())).forEach(this::visitPreOrder);
   }

   public void visitPostOrder(Node node) {
      (new ArrayList(node.getChildNodes())).forEach(this::visitPostOrder);
      this.process(node);
   }

   /** @deprecated */
   @Deprecated
   public void visitDepthFirst(Node node) {
      this.visitPreOrder(node);
   }

   public void visitBreadthFirst(Node node) {
      Queue<Node> queue = new LinkedList();
      queue.offer(node);

      while(queue.size() > 0) {
         Node head = (Node)queue.peek();
         Iterator var4 = head.getChildNodes().iterator();

         while(var4.hasNext()) {
            Node child = (Node)var4.next();
            queue.offer(child);
         }

         this.process((Node)queue.poll());
      }

   }

   public abstract void process(Node node);

   public void visitDirectChildren(Node node) {
      (new ArrayList(node.getChildNodes())).forEach(this::process);
   }
}
