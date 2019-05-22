package org.jboss.logging;

import java.util.Stack;

public class NullNDCProvider implements NDCProvider {
   public void clear() {
   }

   public Stack<String> cloneStack() {
      return null;
   }

   public String get() {
      return null;
   }

   public int getDepth() {
      return 0;
   }

   public void inherit(Stack<String> stack) {
   }

   public String peek() {
      return null;
   }

   public String pop() {
      return null;
   }

   public void push(String message) {
   }

   public void remove() {
   }

   public void setMaxDepth(int maxDepth) {
   }
}
