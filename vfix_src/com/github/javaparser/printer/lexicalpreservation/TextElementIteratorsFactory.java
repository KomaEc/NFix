package com.github.javaparser.printer.lexicalpreservation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class TextElementIteratorsFactory {
   private static Iterator<TokenTextElement> reverseIterator(NodeText nodeText, int index) {
      TextElement textElement = nodeText.getTextElement(index);
      if (textElement instanceof TokenTextElement) {
         return new TextElementIteratorsFactory.SingleElementIterator<TokenTextElement>((TokenTextElement)textElement) {
            public void remove() {
               nodeText.removeElement(index);
            }
         };
      } else if (textElement instanceof ChildTextElement) {
         ChildTextElement childTextElement = (ChildTextElement)textElement;
         NodeText textForChild = childTextElement.getNodeTextForWrappedNode();
         return reverseIterator(textForChild);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static Iterator<TokenTextElement> reverseIterator(NodeText nodeText) {
      return partialReverseIterator(nodeText, nodeText.numberOfElements() - 1);
   }

   public static Iterator<TokenTextElement> partialReverseIterator(NodeText nodeText, int fromIndex) {
      List<Iterator<TokenTextElement>> elements = new LinkedList();

      for(int i = fromIndex; i >= 0; --i) {
         elements.add(reverseIterator(nodeText, i));
      }

      return new TextElementIteratorsFactory.ComposedIterator(elements);
   }

   static class ComposedIterator<E> implements Iterator<E> {
      private final List<Iterator<E>> elements;
      private int currIndex;

      ComposedIterator(List<Iterator<E>> elements) {
         this.elements = elements;
         this.currIndex = 0;
      }

      public boolean hasNext() {
         if (this.currIndex >= this.elements.size()) {
            return false;
         } else if (((Iterator)this.elements.get(this.currIndex)).hasNext()) {
            return true;
         } else {
            ++this.currIndex;
            return this.hasNext();
         }
      }

      public E next() {
         if (!this.hasNext()) {
            throw new IllegalArgumentException();
         } else {
            return ((Iterator)this.elements.get(this.currIndex)).next();
         }
      }

      public void remove() {
         ((Iterator)this.elements.get(this.currIndex)).remove();
      }
   }

   private static class SingleElementIterator<E> implements Iterator<E> {
      private final E element;
      private boolean returned;

      SingleElementIterator(E element) {
         this.element = element;
      }

      public boolean hasNext() {
         return !this.returned;
      }

      public E next() {
         this.returned = true;
         return this.element;
      }

      public void remove() {
      }
   }

   static class EmptyIterator<E> implements Iterator<E> {
      public boolean hasNext() {
         return false;
      }

      public E next() {
         throw new IllegalArgumentException();
      }
   }

   static class CascadingIterator<E> implements Iterator<E> {
      private final TextElementIteratorsFactory.CascadingIterator.Provider<E> nextProvider;
      private Iterator<E> current;
      private Iterator<E> next;
      private boolean lastReturnedFromCurrent = false;
      private boolean lastReturnedFromNext = false;

      public CascadingIterator(Iterator<E> current, TextElementIteratorsFactory.CascadingIterator.Provider<E> nextProvider) {
         this.nextProvider = nextProvider;
         this.current = current;
      }

      public boolean hasNext() {
         if (this.current.hasNext()) {
            return true;
         } else {
            if (this.next == null) {
               this.next = this.nextProvider.provide();
            }

            return this.next.hasNext();
         }
      }

      public E next() {
         if (this.current.hasNext()) {
            this.lastReturnedFromCurrent = true;
            this.lastReturnedFromNext = false;
            return this.current.next();
         } else {
            if (this.next == null) {
               this.next = this.nextProvider.provide();
            }

            this.lastReturnedFromCurrent = false;
            this.lastReturnedFromNext = true;
            return this.next.next();
         }
      }

      public void remove() {
         if (this.lastReturnedFromCurrent) {
            this.current.remove();
         } else if (this.lastReturnedFromNext) {
            this.next.remove();
         } else {
            throw new IllegalArgumentException();
         }
      }

      interface Provider<E> {
         Iterator<E> provide();
      }
   }
}
