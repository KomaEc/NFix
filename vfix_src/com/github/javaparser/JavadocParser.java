package com.github.javaparser;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.utils.Utils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JavadocParser {
   private static String BLOCK_TAG_PREFIX = "@";
   private static Pattern BLOCK_PATTERN;

   public static Javadoc parse(JavadocComment comment) {
      return parse(comment.getContent());
   }

   public static Javadoc parse(String commentContent) {
      List<String> cleanLines = cleanLines(Utils.normalizeEolInTextBlock(commentContent, Utils.EOL));
      Stream var10000 = cleanLines.stream().filter(JavadocParser::isABlockLine);
      cleanLines.getClass();
      int indexOfFirstBlockTag = (Integer)var10000.map(cleanLines::indexOf).findFirst().orElse(-1);
      List blockLines;
      String descriptionText;
      if (indexOfFirstBlockTag == -1) {
         descriptionText = trimRight(String.join(Utils.EOL, cleanLines));
         blockLines = Collections.emptyList();
      } else {
         descriptionText = trimRight(String.join(Utils.EOL, cleanLines.subList(0, indexOfFirstBlockTag)));
         String tagBlock = (String)cleanLines.subList(indexOfFirstBlockTag, cleanLines.size()).stream().collect(Collectors.joining(Utils.EOL));
         blockLines = (List)BLOCK_PATTERN.splitAsStream(tagBlock).filter(Utils.STRING_NOT_EMPTY).map((s) -> {
            return BLOCK_TAG_PREFIX + s;
         }).collect(Collectors.toList());
      }

      Javadoc document = new Javadoc(JavadocDescription.parseText(descriptionText));
      blockLines.forEach((l) -> {
         document.addBlockTag(parseBlockTag(l));
      });
      return document;
   }

   private static JavadocBlockTag parseBlockTag(String line) {
      line = line.trim().substring(1);
      String tagName = Utils.nextWord(line);
      String rest = line.substring(tagName.length()).trim();
      return new JavadocBlockTag(tagName, rest);
   }

   private static boolean isABlockLine(String line) {
      return line.trim().startsWith(BLOCK_TAG_PREFIX);
   }

   private static String trimRight(String string) {
      while(!string.isEmpty() && Character.isWhitespace(string.charAt(string.length() - 1))) {
         string = string.substring(0, string.length() - 1);
      }

      return string;
   }

   private static List<String> cleanLines(String content) {
      String[] lines = content.split(Utils.EOL);
      List<String> cleanedLines = (List)Arrays.stream(lines).map((l) -> {
         int asteriskIndex = startsWithAsterisk(l);
         if (asteriskIndex == -1) {
            return l;
         } else {
            if (l.length() > asteriskIndex + 1) {
               char c = l.charAt(asteriskIndex + 1);
               if (c == ' ' || c == '\t') {
                  return l.substring(asteriskIndex + 2);
               }
            }

            return l.substring(asteriskIndex + 1);
         }
      }).collect(Collectors.toList());
      cleanedLines = (List)cleanedLines.stream().map((l) -> {
         return l.trim().isEmpty() ? "" : l;
      }).collect(Collectors.toList());
      if (!((String)cleanedLines.get(0)).isEmpty() && (((String)cleanedLines.get(0)).charAt(0) == ' ' || ((String)cleanedLines.get(0)).charAt(0) == '\t')) {
         cleanedLines.set(0, ((String)cleanedLines.get(0)).substring(1));
      }

      while(cleanedLines.size() > 0 && ((String)cleanedLines.get(0)).trim().isEmpty()) {
         cleanedLines = cleanedLines.subList(1, cleanedLines.size());
      }

      while(cleanedLines.size() > 0 && ((String)cleanedLines.get(cleanedLines.size() - 1)).trim().isEmpty()) {
         cleanedLines = cleanedLines.subList(0, cleanedLines.size() - 1);
      }

      return cleanedLines;
   }

   static int startsWithAsterisk(String line) {
      if (line.startsWith("*")) {
         return 0;
      } else if ((line.startsWith(" ") || line.startsWith("\t")) && line.length() > 1) {
         int res = startsWithAsterisk(line.substring(1));
         return res == -1 ? -1 : 1 + res;
      } else {
         return -1;
      }
   }

   static {
      BLOCK_PATTERN = Pattern.compile("^\\s*" + BLOCK_TAG_PREFIX, 8);
   }
}
