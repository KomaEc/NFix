package com.github.javaparser.printer;

import com.github.javaparser.utils.Utils;
import java.util.function.Function;

public class PrettyPrinterConfiguration {
   public static final int DEFAULT_MAX_ENUM_CONSTANTS_TO_ALIGN_HORIZONTALLY = 5;
   private boolean orderImports = false;
   private boolean printComments = true;
   private boolean printJavadoc = true;
   private boolean columnAlignParameters = false;
   private boolean columnAlignFirstMethodChain = false;
   private PrettyPrinterConfiguration.IndentType indentType;
   private int tabWidth;
   private int indentSize;
   private String endOfLineCharacter;
   private Function<PrettyPrinterConfiguration, PrettyPrintVisitor> visitorFactory;
   private int maxEnumConstantsToAlignHorizontally;

   public PrettyPrinterConfiguration() {
      this.indentType = PrettyPrinterConfiguration.IndentType.SPACES;
      this.tabWidth = 4;
      this.indentSize = 4;
      this.endOfLineCharacter = Utils.EOL;
      this.visitorFactory = PrettyPrintVisitor::new;
      this.maxEnumConstantsToAlignHorizontally = 5;
   }

   /** @deprecated */
   @Deprecated
   public PrettyPrinterConfiguration setIndent(String indent) {
      System.err.println("PrettyPrinterConfiguration.setIndent is deprecated, please review the changes.");
      if (indent.matches(" *")) {
         this.indentSize = indent.length();
         this.indentType = PrettyPrinterConfiguration.IndentType.SPACES;
      } else {
         if (!indent.matches("\\t+")) {
            throw new UnsupportedOperationException("This type of indentation is not yet supported: '" + indent + "'");
         }

         this.indentSize = indent.length();
         this.indentType = PrettyPrinterConfiguration.IndentType.TABS;
      }

      return this;
   }

   public String getIndent() {
      StringBuilder indentString = new StringBuilder();
      char indentChar;
      switch(this.indentType) {
      case SPACES:
         indentChar = ' ';
         break;
      case TABS:
      case TABS_WITH_SPACE_ALIGN:
         indentChar = '\t';
         break;
      default:
         throw new AssertionError("Unhandled indent type");
      }

      for(int i = 0; i < this.indentSize; ++i) {
         indentString.append(indentChar);
      }

      return indentString.toString();
   }

   public int getIndentSize() {
      return this.indentSize;
   }

   public PrettyPrinterConfiguration setIndentSize(int indentSize) {
      this.indentSize = (Integer)Utils.assertNonNegative(indentSize);
      return this;
   }

   public PrettyPrinterConfiguration.IndentType getIndentType() {
      return this.indentType;
   }

   public PrettyPrinterConfiguration setIndentType(PrettyPrinterConfiguration.IndentType indentType) {
      this.indentType = (PrettyPrinterConfiguration.IndentType)Utils.assertNotNull(indentType);
      return this;
   }

   public int getTabWidth() {
      return this.tabWidth;
   }

   public PrettyPrinterConfiguration setTabWidth(int tabWidth) {
      this.tabWidth = (Integer)Utils.assertPositive(tabWidth);
      return this;
   }

   public boolean isOrderImports() {
      return this.orderImports;
   }

   /** @deprecated */
   @Deprecated
   public boolean isNormalizeEolInComment() {
      return true;
   }

   /** @deprecated */
   @Deprecated
   public PrettyPrinterConfiguration setNormalizeEolInComment(boolean normalizeEolInComment) {
      return this;
   }

   public boolean isPrintComments() {
      return this.printComments;
   }

   public boolean isIgnoreComments() {
      return !this.printComments;
   }

   public boolean isPrintJavadoc() {
      return this.printJavadoc;
   }

   public boolean isColumnAlignParameters() {
      return this.columnAlignParameters;
   }

   public boolean isColumnAlignFirstMethodChain() {
      return this.columnAlignFirstMethodChain;
   }

   public PrettyPrinterConfiguration setPrintComments(boolean printComments) {
      this.printComments = printComments;
      return this;
   }

   public PrettyPrinterConfiguration setPrintJavadoc(boolean printJavadoc) {
      this.printJavadoc = printJavadoc;
      return this;
   }

   public PrettyPrinterConfiguration setColumnAlignParameters(boolean columnAlignParameters) {
      this.columnAlignParameters = columnAlignParameters;
      return this;
   }

   public PrettyPrinterConfiguration setColumnAlignFirstMethodChain(boolean columnAlignFirstMethodChain) {
      this.columnAlignFirstMethodChain = columnAlignFirstMethodChain;
      return this;
   }

   public Function<PrettyPrinterConfiguration, PrettyPrintVisitor> getVisitorFactory() {
      return this.visitorFactory;
   }

   public PrettyPrinterConfiguration setVisitorFactory(Function<PrettyPrinterConfiguration, PrettyPrintVisitor> visitorFactory) {
      this.visitorFactory = (Function)Utils.assertNotNull(visitorFactory);
      return this;
   }

   public String getEndOfLineCharacter() {
      return this.endOfLineCharacter;
   }

   public PrettyPrinterConfiguration setEndOfLineCharacter(String endOfLineCharacter) {
      this.endOfLineCharacter = (String)Utils.assertNotNull(endOfLineCharacter);
      return this;
   }

   public PrettyPrinterConfiguration setOrderImports(boolean orderImports) {
      this.orderImports = orderImports;
      return this;
   }

   public int getMaxEnumConstantsToAlignHorizontally() {
      return this.maxEnumConstantsToAlignHorizontally;
   }

   public PrettyPrinterConfiguration setMaxEnumConstantsToAlignHorizontally(int maxEnumConstantsToAlignHorizontally) {
      this.maxEnumConstantsToAlignHorizontally = maxEnumConstantsToAlignHorizontally;
      return this;
   }

   public static enum IndentType {
      SPACES,
      TABS,
      TABS_WITH_SPACE_ALIGN;
   }
}
