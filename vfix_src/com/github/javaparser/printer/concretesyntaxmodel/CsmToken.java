package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.GeneratedJavaParserConstants;
import com.github.javaparser.TokenTypes;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.utils.Utils;

public class CsmToken implements CsmElement {
   private final int tokenType;
   private String content;
   private CsmToken.TokenContentCalculator tokenContentCalculator;

   public int getTokenType() {
      return this.tokenType;
   }

   public String getContent(Node node) {
      return this.tokenContentCalculator != null ? this.tokenContentCalculator.calculate(node) : this.content;
   }

   public CsmToken(int tokenType) {
      this.tokenType = tokenType;
      this.content = GeneratedJavaParserConstants.tokenImage[tokenType];
      if (this.content.startsWith("\"")) {
         this.content = this.content.substring(1, this.content.length() - 1);
      }

      if (TokenTypes.isEndOfLineToken(tokenType)) {
         this.content = Utils.EOL;
      } else if (TokenTypes.isSpaceOrTab(tokenType)) {
         this.content = " ";
      }

   }

   public CsmToken(int tokenType, String content) {
      this.tokenType = tokenType;
      this.content = content;
   }

   public CsmToken(int tokenType, CsmToken.TokenContentCalculator tokenContentCalculator) {
      this.tokenType = tokenType;
      this.tokenContentCalculator = tokenContentCalculator;
   }

   public void prettyPrint(Node node, SourcePrinter printer) {
      if (TokenTypes.isEndOfLineToken(this.tokenType)) {
         printer.println();
      } else {
         printer.print(this.getContent(node));
      }

   }

   public String toString() {
      return "token(" + this.content + ")";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CsmToken csmToken = (CsmToken)o;
         if (this.tokenType != csmToken.tokenType) {
            return false;
         } else {
            if (this.content != null) {
               if (this.content.equals(csmToken.content)) {
                  return this.tokenContentCalculator != null ? this.tokenContentCalculator.equals(csmToken.tokenContentCalculator) : csmToken.tokenContentCalculator == null;
               }
            } else if (csmToken.content == null) {
               return this.tokenContentCalculator != null ? this.tokenContentCalculator.equals(csmToken.tokenContentCalculator) : csmToken.tokenContentCalculator == null;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.tokenType;
      result = 31 * result + (this.content != null ? this.content.hashCode() : 0);
      result = 31 * result + (this.tokenContentCalculator != null ? this.tokenContentCalculator.hashCode() : 0);
      return result;
   }

   public boolean isWhiteSpace() {
      return TokenTypes.isWhitespace(this.tokenType);
   }

   public boolean isNewLine() {
      return TokenTypes.isEndOfLineToken(this.tokenType);
   }

   public interface TokenContentCalculator {
      String calculate(Node node);
   }
}
