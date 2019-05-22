package soot.jimple.parser;

import java.util.HashSet;
import java.util.Set;
import soot.Scene;
import soot.jimple.parser.analysis.DepthFirstAdapter;
import soot.jimple.parser.node.AFullIdentClassName;
import soot.jimple.parser.node.AFullIdentNonvoidType;
import soot.jimple.parser.node.AIdentClassName;
import soot.jimple.parser.node.AIdentNonvoidType;
import soot.jimple.parser.node.AQuotedClassName;
import soot.jimple.parser.node.AQuotedNonvoidType;
import soot.jimple.parser.node.Start;
import soot.util.StringTools;

class CstPoolExtractor {
   private Set<String> mRefTypes = null;
   private Start mParseTree;

   public CstPoolExtractor(Start parseTree) {
      this.mParseTree = parseTree;
   }

   public Set<String> getCstPool() {
      if (this.mRefTypes == null) {
         this.mRefTypes = new HashSet();
         CstPoolExtractor.CstPoolExtractorWalker walker = new CstPoolExtractor.CstPoolExtractorWalker();
         this.mParseTree.apply(walker);
         this.mParseTree = null;
      }

      return this.mRefTypes;
   }

   private class CstPoolExtractorWalker extends DepthFirstAdapter {
      CstPoolExtractorWalker() {
      }

      public void inStart(Start node) {
         this.defaultIn(node);
      }

      public void outAQuotedClassName(AQuotedClassName node) {
         String tokenString = node.getQuotedName().getText();
         tokenString = tokenString.substring(1, tokenString.length() - 1);
         tokenString = StringTools.getUnEscapedStringOf(tokenString);
         CstPoolExtractor.this.mRefTypes.add(tokenString);
      }

      public void outAIdentClassName(AIdentClassName node) {
         String tokenString = node.getIdentifier().getText();
         tokenString = StringTools.getUnEscapedStringOf(tokenString);
         CstPoolExtractor.this.mRefTypes.add(tokenString);
      }

      public void outAFullIdentClassName(AFullIdentClassName node) {
         String tokenString = node.getFullIdentifier().getText();
         tokenString = Scene.v().unescapeName(tokenString);
         tokenString = StringTools.getUnEscapedStringOf(tokenString);
         CstPoolExtractor.this.mRefTypes.add(tokenString);
      }

      public void outAQuotedNonvoidType(AQuotedNonvoidType node) {
         String tokenString = node.getQuotedName().getText();
         tokenString = tokenString.substring(1, tokenString.length() - 1);
         tokenString = StringTools.getUnEscapedStringOf(tokenString);
         CstPoolExtractor.this.mRefTypes.add(tokenString);
      }

      public void outAFullIdentNonvoidType(AFullIdentNonvoidType node) {
         String tokenString = node.getFullIdentifier().getText();
         tokenString = Scene.v().unescapeName(tokenString);
         tokenString = StringTools.getUnEscapedStringOf(tokenString);
         CstPoolExtractor.this.mRefTypes.add(tokenString);
      }

      public void outAIdentNonvoidType(AIdentNonvoidType node) {
         String tokenString = node.getIdentifier().getText();
         tokenString = StringTools.getUnEscapedStringOf(tokenString);
         CstPoolExtractor.this.mRefTypes.add(tokenString);
      }
   }
}
