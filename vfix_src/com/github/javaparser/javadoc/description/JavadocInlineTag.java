package com.github.javaparser.javadoc.description;

import com.github.javaparser.utils.Utils;

public class JavadocInlineTag implements JavadocDescriptionElement {
   private String tagName;
   private JavadocInlineTag.Type type;
   private String content;

   public static JavadocDescriptionElement fromText(String text) {
      if (!text.startsWith("{@")) {
         throw new IllegalArgumentException(String.format("Expected to start with '{@'. Text '%s'", text));
      } else if (!text.endsWith("}")) {
         throw new IllegalArgumentException(String.format("Expected to end with '}'. Text '%s'", text));
      } else {
         text = text.substring(2, text.length() - 1);
         String tagName = Utils.nextWord(text);
         JavadocInlineTag.Type type = JavadocInlineTag.Type.fromName(tagName);
         String content = text.substring(tagName.length());
         return new JavadocInlineTag(tagName, type, content);
      }
   }

   public JavadocInlineTag(String tagName, JavadocInlineTag.Type type, String content) {
      this.tagName = tagName;
      this.type = type;
      this.content = content;
   }

   public JavadocInlineTag.Type getType() {
      return this.type;
   }

   public String getContent() {
      return this.content;
   }

   public String getName() {
      return this.tagName;
   }

   public String toText() {
      return "{@" + this.tagName + this.content + "}";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavadocInlineTag that;
         label33: {
            that = (JavadocInlineTag)o;
            if (this.tagName != null) {
               if (this.tagName.equals(that.tagName)) {
                  break label33;
               }
            } else if (that.tagName == null) {
               break label33;
            }

            return false;
         }

         if (this.type != that.type) {
            return false;
         } else {
            return this.content != null ? this.content.equals(that.content) : that.content == null;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.tagName != null ? this.tagName.hashCode() : 0;
      result = 31 * result + (this.type != null ? this.type.hashCode() : 0);
      result = 31 * result + (this.content != null ? this.content.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "JavadocInlineTag{tagName='" + this.tagName + '\'' + ", type=" + this.type + ", content='" + this.content + '\'' + '}';
   }

   public static enum Type {
      CODE,
      DOC_ROOT,
      INHERIT_DOC,
      LINK,
      LINKPLAIN,
      LITERAL,
      VALUE,
      UNKNOWN;

      private String keyword = Utils.screamingToCamelCase(this.name());

      static JavadocInlineTag.Type fromName(String tagName) {
         JavadocInlineTag.Type[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            JavadocInlineTag.Type t = var1[var3];
            if (t.keyword.equals(tagName)) {
               return t;
            }
         }

         return UNKNOWN;
      }
   }
}
