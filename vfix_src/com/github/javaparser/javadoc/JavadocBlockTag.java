package com.github.javaparser.javadoc;

import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.utils.Utils;
import java.util.Optional;

public class JavadocBlockTag {
   private JavadocBlockTag.Type type;
   private JavadocDescription content;
   private Optional<String> name;
   private String tagName;

   public JavadocBlockTag(JavadocBlockTag.Type type, String content) {
      this.name = Optional.empty();
      this.type = type;
      this.tagName = type.keyword;
      if (type.hasName()) {
         this.name = Optional.of(Utils.nextWord(content));
         content = content.substring(((String)this.name.get()).length()).trim();
      }

      this.content = JavadocDescription.parseText(content);
   }

   public JavadocBlockTag(String tagName, String content) {
      this(JavadocBlockTag.Type.fromName(tagName), content);
      this.tagName = tagName;
   }

   public static JavadocBlockTag createParamBlockTag(String paramName, String content) {
      return new JavadocBlockTag(JavadocBlockTag.Type.PARAM, paramName + " " + content);
   }

   public JavadocBlockTag.Type getType() {
      return this.type;
   }

   public JavadocDescription getContent() {
      return this.content;
   }

   public Optional<String> getName() {
      return this.name;
   }

   public String getTagName() {
      return this.tagName;
   }

   public String toText() {
      StringBuilder sb = new StringBuilder();
      sb.append("@");
      sb.append(this.tagName);
      this.name.ifPresent((s) -> {
         sb.append(" ").append(s);
      });
      if (!this.content.isEmpty()) {
         sb.append(" ");
         sb.append(this.content.toText());
      }

      return sb.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavadocBlockTag that = (JavadocBlockTag)o;
         if (this.type != that.type) {
            return false;
         } else {
            return !this.content.equals(that.content) ? false : this.name.equals(that.name);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.type.hashCode();
      result = 31 * result + this.content.hashCode();
      result = 31 * result + this.name.hashCode();
      return result;
   }

   public String toString() {
      return "JavadocBlockTag{type=" + this.type + ", content='" + this.content + '\'' + ", name=" + this.name + '}';
   }

   public static enum Type {
      AUTHOR,
      DEPRECATED,
      EXCEPTION,
      PARAM,
      RETURN,
      SEE,
      SERIAL,
      SERIAL_DATA,
      SERIAL_FIELD,
      SINCE,
      THROWS,
      VERSION,
      UNKNOWN;

      private String keyword = Utils.screamingToCamelCase(this.name());

      boolean hasName() {
         return this == PARAM;
      }

      static JavadocBlockTag.Type fromName(String tagName) {
         JavadocBlockTag.Type[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            JavadocBlockTag.Type t = var1[var3];
            if (t.keyword.equals(tagName)) {
               return t;
            }
         }

         return UNKNOWN;
      }
   }
}
