package com.github.javaparser;

import java.util.Iterator;
import java.util.TreeSet;

public class ParseException extends Exception {
   private static final long serialVersionUID = 1L;
   private static final String INDENT = "    ";
   protected static String EOL = "\n";
   public Token currentToken;
   public int[][] expectedTokenSequences;
   public String[] tokenImage;

   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
      this(currentTokenVal, expectedTokenSequencesVal, tokenImageVal, (String)null);
   }

   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal, String lexicalStateName) {
      super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal, lexicalStateName));
      this.currentToken = currentTokenVal;
      this.expectedTokenSequences = expectedTokenSequencesVal;
      this.tokenImage = tokenImageVal;
   }

   public ParseException() {
   }

   public ParseException(String message) {
      super(message);
   }

   private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage, String lexicalStateName) {
      StringBuilder sb = new StringBuilder();
      StringBuffer expected = new StringBuffer();
      int maxSize = 0;
      TreeSet<String> sortedOptions = new TreeSet();

      int numExpectedTokens;
      for(int i = 0; i < expectedTokenSequences.length; ++i) {
         if (maxSize < expectedTokenSequences[i].length) {
            maxSize = expectedTokenSequences[i].length;
         }

         for(numExpectedTokens = 0; numExpectedTokens < expectedTokenSequences[i].length; ++numExpectedTokens) {
            sortedOptions.add(tokenImage[expectedTokenSequences[i][numExpectedTokens]]);
         }
      }

      Iterator var12 = sortedOptions.iterator();

      while(var12.hasNext()) {
         String option = (String)var12.next();
         expected.append("    ").append(option).append(EOL);
      }

      sb.append("Encountered unexpected token:");
      Token tok = currentToken.next;

      for(numExpectedTokens = 0; numExpectedTokens < maxSize; ++numExpectedTokens) {
         String tokenText = tok.image;
         String escapedTokenText = add_escapes(tokenText);
         if (numExpectedTokens != 0) {
            sb.append(" ");
         }

         if (tok.kind == 0) {
            sb.append(tokenImage[0]);
            break;
         }

         sb.append(" \"");
         sb.append(escapedTokenText);
         sb.append("\"");
         sb.append(" " + tokenImage[tok.kind]);
         tok = tok.next;
      }

      sb.append(EOL).append("    ").append("at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn);
      sb.append(".").append(EOL);
      if (expectedTokenSequences.length != 0) {
         numExpectedTokens = expectedTokenSequences.length;
         sb.append(EOL).append("Was expecting" + (numExpectedTokens == 1 ? ":" : " one of:") + EOL + EOL);
         sb.append(expected.toString());
      }

      return sb.toString();
   }

   static String add_escapes(String str) {
      StringBuffer retval = new StringBuffer();

      for(int i = 0; i < str.length(); ++i) {
         switch(str.charAt(i)) {
         case '\b':
            retval.append("\\b");
            break;
         case '\t':
            retval.append("\\t");
            break;
         case '\n':
            retval.append("\\n");
            break;
         case '\f':
            retval.append("\\f");
            break;
         case '\r':
            retval.append("\\r");
            break;
         case '"':
            retval.append("\\\"");
            break;
         case '\'':
            retval.append("\\'");
            break;
         case '\\':
            retval.append("\\\\");
            break;
         default:
            char ch;
            if ((ch = str.charAt(i)) >= ' ' && ch <= '~') {
               retval.append(ch);
            } else {
               String s = "0000" + Integer.toString(ch, 16);
               retval.append("\\u" + s.substring(s.length() - 4, s.length()));
            }
         }
      }

      return retval.toString();
   }
}
