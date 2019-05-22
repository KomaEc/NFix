package com.github.javaparser.javadoc;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.utils.Utils;
import java.util.LinkedList;
import java.util.List;

public class Javadoc {
   private JavadocDescription description;
   private List<JavadocBlockTag> blockTags;

   public Javadoc(JavadocDescription description) {
      this.description = description;
      this.blockTags = new LinkedList();
   }

   public Javadoc addBlockTag(JavadocBlockTag blockTag) {
      this.blockTags.add(blockTag);
      return this;
   }

   public Javadoc addBlockTag(String tagName, String content) {
      return this.addBlockTag(new JavadocBlockTag(tagName, content));
   }

   public Javadoc addBlockTag(String tagName, String parameter, String content) {
      return this.addBlockTag(tagName, parameter + " " + content);
   }

   public Javadoc addBlockTag(String tagName) {
      return this.addBlockTag(tagName, "");
   }

   public String toText() {
      StringBuilder sb = new StringBuilder();
      if (!this.description.isEmpty()) {
         sb.append(this.description.toText());
         sb.append(Utils.EOL);
      }

      if (!this.blockTags.isEmpty()) {
         sb.append(Utils.EOL);
      }

      this.blockTags.forEach((bt) -> {
         sb.append(bt.toText());
         sb.append(Utils.EOL);
      });
      return sb.toString();
   }

   public JavadocComment toComment() {
      return this.toComment("");
   }

   public JavadocComment toComment(String indentation) {
      char[] var2 = indentation.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c = var2[var4];
         if (!Character.isWhitespace(c)) {
            throw new IllegalArgumentException("The indentation string should be composed only by whitespace characters");
         }
      }

      StringBuilder sb = new StringBuilder();
      sb.append(Utils.EOL);
      String text = this.toText();
      if (!text.isEmpty()) {
         String[] var10 = text.split(Utils.EOL);
         int var11 = var10.length;

         for(int var6 = 0; var6 < var11; ++var6) {
            String line = var10[var6];
            sb.append(indentation);
            sb.append(" * ");
            sb.append(line);
            sb.append(Utils.EOL);
         }
      }

      sb.append(indentation);
      sb.append(" ");
      return new JavadocComment(sb.toString());
   }

   public JavadocDescription getDescription() {
      return this.description;
   }

   public List<JavadocBlockTag> getBlockTags() {
      return this.blockTags;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Javadoc document = (Javadoc)o;
         return this.description.equals(document.description) && this.blockTags.equals(document.blockTags);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.description.hashCode();
      result = 31 * result + this.blockTags.hashCode();
      return result;
   }

   public String toString() {
      return "Javadoc{description=" + this.description + ", blockTags=" + this.blockTags + '}';
   }
}
