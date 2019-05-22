package org.codehaus.groovy.tools.groovydoc;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;
import org.codehaus.groovy.groovydoc.GroovyDoc;
import org.codehaus.groovy.groovydoc.GroovyTag;

public class SimpleGroovyDoc implements GroovyDoc, GroovyTokenTypes {
   private static final Pattern TAG2_PATTERN = Pattern.compile("(?s)([a-z]+)\\s+(.*)");
   private static final Pattern TAG3_PATTERN = Pattern.compile("(?s)([a-z]+)\\s+(\\S*)\\s+(.*)");
   private String name;
   private String commentText = null;
   private String rawCommentText = "";
   private String firstSentenceCommentText = null;
   private int definitionType;
   private boolean deprecated;
   private boolean isScript;
   private GroovyTag[] tags;

   public SimpleGroovyDoc(String name) {
      this.name = name;
      this.definitionType = 13;
   }

   public String name() {
      return this.name;
   }

   public String toString() {
      return "" + this.getClass() + "(" + this.name + ")";
   }

   protected void setCommentText(String commentText) {
      this.commentText = commentText;
   }

   protected void setFirstSentenceCommentText(String firstSentenceCommentText) {
      this.firstSentenceCommentText = firstSentenceCommentText;
   }

   public String commentText() {
      return this.commentText;
   }

   public String firstSentenceCommentText() {
      return this.firstSentenceCommentText;
   }

   public String getRawCommentText() {
      return this.rawCommentText;
   }

   public void setRawCommentText(String rawCommentText) {
      this.rawCommentText = rawCommentText;
      this.calculateTags(rawCommentText);
   }

   public void setScript(boolean script) {
      this.isScript = script;
   }

   private void calculateTags(String rawCommentText) {
      String trimmed = rawCommentText.replaceFirst("(?s).*?\\*\\s*@", "@");
      if (!trimmed.equals(rawCommentText)) {
         String cleaned = trimmed.replaceAll("(?m)^\\s*\\*\\s*([^*]*)$", "$1").trim();
         String[] split = cleaned.split("(?m)^@");
         List<GroovyTag> result = new ArrayList();
         String[] arr$ = split;
         int len$ = split.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            String tagname = null;
            Matcher m;
            if (!s.startsWith("param") && !s.startsWith("throws")) {
               m = TAG2_PATTERN.matcher(s);
               if (m.find()) {
                  tagname = m.group(1);
                  result.add(new SimpleGroovyTag(tagname, (String)null, m.group(2)));
               }
            } else {
               m = TAG3_PATTERN.matcher(s);
               if (m.find()) {
                  tagname = m.group(1);
                  result.add(new SimpleGroovyTag(tagname, m.group(2), m.group(3)));
               }
            }

            if ("deprecated".equals(tagname)) {
               this.setDeprecated(true);
            }
         }

         this.tags = (GroovyTag[])result.toArray(new GroovyTag[result.size()]);
      }
   }

   public static String calculateFirstSentence(String raw) {
      String text = raw.replaceAll("(?m)^\\s*\\*", "").trim();
      text = text.replaceFirst("(?ms)\\n\\s*\\n.*", "").trim();
      text = text.replaceFirst("(?ms)\\n\\s*@(see|param|throws|return|author|since|exception|version|deprecated|todo)\\s.*", "").trim();
      BreakIterator boundary = BreakIterator.getSentenceInstance(Locale.getDefault());
      boundary.setText(text);
      int start = boundary.first();
      int end = boundary.next();
      if (start > -1 && end > -1) {
         text = text.substring(start, end);
      }

      return text;
   }

   public boolean isClass() {
      return this.definitionType == 13 && !this.isScript;
   }

   public boolean isScript() {
      return this.definitionType == 13 && this.isScript;
   }

   public boolean isInterface() {
      return this.definitionType == 14;
   }

   public boolean isAnnotationType() {
      return this.definitionType == 63;
   }

   public boolean isEnum() {
      return this.definitionType == 60;
   }

   public String getTypeDescription() {
      if (this.isInterface()) {
         return "Interface";
      } else if (this.isAnnotationType()) {
         return "Annotation Type";
      } else {
         return this.isEnum() ? "Enum" : "Class";
      }
   }

   public String getTypeSourceDescription() {
      if (this.isInterface()) {
         return "interface";
      } else if (this.isAnnotationType()) {
         return "@interface";
      } else {
         return this.isEnum() ? "enum" : "class";
      }
   }

   public void setTokenType(int t) {
      this.definitionType = t;
   }

   public int tokenType() {
      return this.definitionType;
   }

   public int compareTo(Object that) {
      if (that instanceof GroovyDoc) {
         return this.name.compareTo(((GroovyDoc)that).name());
      } else {
         throw new ClassCastException(String.format("Cannot compare object of type %s.", that.getClass()));
      }
   }

   public boolean isAnnotationTypeElement() {
      return false;
   }

   public boolean isConstructor() {
      return false;
   }

   public boolean isEnumConstant() {
      return false;
   }

   public boolean isDeprecated() {
      return this.deprecated;
   }

   public boolean isError() {
      return false;
   }

   public boolean isException() {
      return false;
   }

   public boolean isField() {
      return false;
   }

   public boolean isIncluded() {
      return false;
   }

   public boolean isMethod() {
      return false;
   }

   public boolean isOrdinaryClass() {
      return false;
   }

   public GroovyTag[] tags() {
      return this.tags;
   }

   public void setDeprecated(boolean deprecated) {
      this.deprecated = deprecated;
   }
}
