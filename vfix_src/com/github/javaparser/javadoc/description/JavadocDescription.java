package com.github.javaparser.javadoc.description;

import com.github.javaparser.utils.Pair;
import java.util.LinkedList;
import java.util.List;

public class JavadocDescription {
   private List<JavadocDescriptionElement> elements;

   public static JavadocDescription parseText(String text) {
      JavadocDescription instance = new JavadocDescription();

      int index;
      Pair nextInlineTagPos;
      for(index = 0; (nextInlineTagPos = indexOfNextInlineTag(text, index)) != null; index = (Integer)nextInlineTagPos.b + 1) {
         if ((Integer)nextInlineTagPos.a != index) {
            instance.addElement(new JavadocSnippet(text.substring(index, (Integer)nextInlineTagPos.a)));
         }

         instance.addElement(JavadocInlineTag.fromText(text.substring((Integer)nextInlineTagPos.a, (Integer)nextInlineTagPos.b + 1)));
      }

      if (index < text.length()) {
         instance.addElement(new JavadocSnippet(text.substring(index)));
      }

      return instance;
   }

   private static Pair<Integer, Integer> indexOfNextInlineTag(String text, int start) {
      int index = text.indexOf("{@", start);
      if (index == -1) {
         return null;
      } else {
         int closeIndex = text.indexOf("}", index);
         return closeIndex == -1 ? null : new Pair(index, closeIndex);
      }
   }

   public JavadocDescription() {
      this.elements = new LinkedList();
   }

   public JavadocDescription(List<JavadocDescriptionElement> elements) {
      this();
      this.elements.addAll(elements);
   }

   public boolean addElement(JavadocDescriptionElement element) {
      return this.elements.add(element);
   }

   public List<JavadocDescriptionElement> getElements() {
      return this.elements;
   }

   public String toText() {
      StringBuilder sb = new StringBuilder();
      this.elements.forEach((e) -> {
         sb.append(e.toText());
      });
      return sb.toString();
   }

   public boolean isEmpty() {
      return this.toText().isEmpty();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavadocDescription that = (JavadocDescription)o;
         return this.elements.equals(that.elements);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.elements.hashCode();
   }

   public String toString() {
      return "JavadocDescription{elements=" + this.elements + '}';
   }
}
