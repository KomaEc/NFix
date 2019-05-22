package org.codehaus.groovy.transform.powerassert;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.SourceUnit;

public class SourceText {
   private final int firstLine;
   private String normalizedText;
   private final List<Integer> lineOffsets = new ArrayList();
   private final List<Integer> textOffsets = new ArrayList();

   public SourceText(AssertStatement stat, SourceUnit sourceUnit, Janitor janitor) {
      if (!this.hasPlausibleSourcePosition(stat)) {
         throw new SourceTextNotAvailableException(stat, sourceUnit, "Invalid source position");
      } else {
         this.firstLine = stat.getLineNumber();
         this.textOffsets.add(0);
         this.normalizedText = "";

         for(int line = stat.getLineNumber(); line <= stat.getLastLineNumber(); ++line) {
            String lineText = sourceUnit.getSample(line, 0, janitor);
            if (lineText == null) {
               throw new SourceTextNotAvailableException(stat, sourceUnit, "SourceUnit.getSample() returned null");
            }

            if (line == stat.getLastLineNumber()) {
               lineText = lineText.substring(0, stat.getLastColumnNumber() - 1);
            }

            if (line == stat.getLineNumber()) {
               lineText = lineText.substring(stat.getColumnNumber() - 1);
               this.lineOffsets.add(stat.getColumnNumber() - 1);
            } else {
               this.lineOffsets.add(this.countLeadingWhitespace(lineText));
            }

            lineText = lineText.trim();
            if (line != stat.getLastLineNumber() && lineText.length() > 0) {
               lineText = lineText + ' ';
            }

            this.normalizedText = this.normalizedText + lineText;
            this.textOffsets.add(this.normalizedText.length());
         }

      }
   }

   public String getNormalizedText() {
      return this.normalizedText;
   }

   public int getNormalizedColumn(int line, int column) {
      int deltaLine = line - this.firstLine;
      if (deltaLine < 0) {
         return -1;
      } else {
         int deltaColumn = column - (Integer)this.lineOffsets.get(deltaLine);
         return deltaColumn < 0 ? -1 : (Integer)this.textOffsets.get(deltaLine) + deltaColumn;
      }
   }

   public int getNormalizedColumn(String str, int line, int column) {
      return this.getNormalizedText().lastIndexOf(str, this.getNormalizedColumn(line, column) - 1 - str.length()) + 1;
   }

   private boolean hasPlausibleSourcePosition(ASTNode node) {
      return node.getLineNumber() > 0 && node.getColumnNumber() > 0 && node.getLastLineNumber() >= node.getLineNumber() && node.getLastColumnNumber() > (node.getLineNumber() == node.getLastLineNumber() ? node.getColumnNumber() : 0);
   }

   private int countLeadingWhitespace(String lineText) {
      int result;
      for(result = 0; result < lineText.length() && Character.isWhitespace(lineText.charAt(result)); ++result) {
      }

      return result;
   }
}
