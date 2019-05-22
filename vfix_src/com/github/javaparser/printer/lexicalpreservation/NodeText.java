package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.ast.Node;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class NodeText {
   private final List<TextElement> elements;
   public static final int NOT_FOUND = -1;

   NodeText(List<TextElement> elements) {
      this.elements = elements;
   }

   NodeText() {
      this(new LinkedList());
   }

   void addElement(TextElement nodeTextElement) {
      this.elements.add(nodeTextElement);
   }

   void addElement(int index, TextElement nodeTextElement) {
      this.elements.add(index, nodeTextElement);
   }

   void addChild(Node child) {
      this.addElement(new ChildTextElement(child));
   }

   void addChild(int index, Node child) {
      this.addElement(index, new ChildTextElement(child));
   }

   void addToken(int tokenKind, String text) {
      this.elements.add(new TokenTextElement(tokenKind, text));
   }

   void addToken(int index, int tokenKind, String text) {
      this.elements.add(index, new TokenTextElement(tokenKind, text));
   }

   int findElement(TextElementMatcher matcher) {
      return this.findElement(matcher, 0);
   }

   int findElement(TextElementMatcher matcher, int from) {
      int res = this.tryToFindElement(matcher, from);
      if (res == -1) {
         throw new IllegalArgumentException(String.format("I could not find child '%s' from position %d. Elements: %s", matcher, from, this.elements));
      } else {
         return res;
      }
   }

   int tryToFindElement(TextElementMatcher matcher, int from) {
      for(int i = from; i < this.elements.size(); ++i) {
         TextElement element = (TextElement)this.elements.get(i);
         if (matcher.match(element)) {
            return i;
         }
      }

      return -1;
   }

   int findChild(Node child) {
      return this.findChild(child, 0);
   }

   int findChild(Node child, int from) {
      return this.findElement(TextElementMatchers.byNode(child), from);
   }

   int tryToFindChild(Node child) {
      return this.tryToFindChild(child, 0);
   }

   int tryToFindChild(Node child, int from) {
      return this.tryToFindElement(TextElementMatchers.byNode(child), from);
   }

   void remove(TextElementMatcher matcher) {
      this.elements.removeIf(matcher::match);
   }

   public void remove(TextElementMatcher matcher, boolean potentiallyFollowingWhitespace) {
      int i = 0;
      Iterator var4 = this.elements.iterator();

      TextElement e;
      do {
         if (!var4.hasNext()) {
            throw new IllegalArgumentException();
         }

         e = (TextElement)var4.next();
      } while(!matcher.match(e));

      this.elements.remove(e);
      if (potentiallyFollowingWhitespace) {
         if (i >= this.elements.size()) {
            throw new UnsupportedOperationException();
         }

         if (((TextElement)this.elements.get(i)).isWhiteSpace()) {
            this.elements.remove(i);
         }
      }

   }

   void removeElement(int index) {
      this.elements.remove(index);
   }

   void replace(TextElementMatcher position, TextElement newElement) {
      int index = this.findElement(position, 0);
      this.elements.remove(index);
      this.elements.add(index, newElement);
   }

   String expand() {
      StringBuffer sb = new StringBuffer();
      this.elements.forEach((e) -> {
         sb.append(e.expand());
      });
      return sb.toString();
   }

   int numberOfElements() {
      return this.elements.size();
   }

   TextElement getTextElement(int index) {
      return (TextElement)this.elements.get(index);
   }

   List<TextElement> getElements() {
      return this.elements;
   }

   public String toString() {
      return "NodeText{" + this.elements + '}';
   }

   public boolean endWithSpace() {
      if (this.elements.isEmpty()) {
         return false;
      } else {
         TextElement lastElement = (TextElement)this.elements.get(this.elements.size() - 1);
         if (lastElement instanceof TokenTextElement) {
            return ((TokenTextElement)lastElement).getTokenKind() == 1;
         } else {
            return false;
         }
      }
   }

   public void removeLastElement() {
      if (this.elements.isEmpty()) {
         throw new IllegalStateException();
      } else {
         this.elements.remove(this.elements.size() - 1);
      }
   }

   static enum Option {
      REMOVE_SPACE_IMMEDIATELY_AFTER,
      EXCLUDE_START,
      EXCLUDE_END;
   }
}
