package com.github.javaparser.javadoc.description;

public class JavadocSnippet implements JavadocDescriptionElement {
   private String text;

   public JavadocSnippet(String text) {
      if (text == null) {
         throw new NullPointerException();
      } else {
         this.text = text;
      }
   }

   public String toText() {
      return this.text;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavadocSnippet that = (JavadocSnippet)o;
         return this.text.equals(that.text);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.text.hashCode();
   }

   public String toString() {
      return "JavadocSnippet{text='" + this.text + '\'' + '}';
   }
}
