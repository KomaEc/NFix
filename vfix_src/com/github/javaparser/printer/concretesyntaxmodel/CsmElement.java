package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.TokenTypes;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.utils.Utils;
import java.util.Arrays;
import java.util.List;

public interface CsmElement {
   void prettyPrint(Node node, SourcePrinter printer);

   static CsmElement child(ObservableProperty property) {
      return new CsmSingleReference(property);
   }

   static CsmElement attribute(ObservableProperty property) {
      return new CsmAttribute(property);
   }

   static CsmElement sequence(CsmElement... elements) {
      return new CsmSequence(Arrays.asList(elements));
   }

   static CsmElement string(int tokenType, String content) {
      return new CsmToken(tokenType, content);
   }

   static CsmElement string(int tokenType) {
      return new CsmToken(tokenType);
   }

   static CsmElement stringToken(ObservableProperty property) {
      return new CsmString(property);
   }

   static CsmElement charToken(ObservableProperty property) {
      return new CsmChar(property);
   }

   static CsmElement token(int tokenType) {
      return new CsmToken(tokenType);
   }

   static CsmElement token(int tokenType, CsmToken.TokenContentCalculator tokenContentCalculator) {
      return new CsmToken(tokenType, tokenContentCalculator);
   }

   static CsmElement conditional(ObservableProperty property, CsmConditional.Condition condition, CsmElement thenElement) {
      return new CsmConditional(property, condition, thenElement);
   }

   static CsmElement conditional(ObservableProperty property, CsmConditional.Condition condition, CsmElement thenElement, CsmElement elseElement) {
      return new CsmConditional(property, condition, thenElement, elseElement);
   }

   static CsmElement conditional(List<ObservableProperty> properties, CsmConditional.Condition condition, CsmElement thenElement, CsmElement elseElement) {
      return new CsmConditional(properties, condition, thenElement, elseElement);
   }

   static CsmElement space() {
      return new CsmToken(TokenTypes.spaceTokenKind(), " ");
   }

   static CsmElement semicolon() {
      return new CsmToken(98);
   }

   static CsmElement comment() {
      return new CsmComment();
   }

   static CsmElement newline() {
      return new CsmToken(TokenTypes.eolTokenKind(), Utils.EOL);
   }

   static CsmElement none() {
      return new CsmNone();
   }

   static CsmElement comma() {
      return new CsmToken(99);
   }

   static CsmElement list(ObservableProperty property) {
      return new CsmList(property);
   }

   static CsmElement list(ObservableProperty property, CsmElement separator) {
      return new CsmList(property, none(), separator, new CsmNone(), new CsmNone());
   }

   static CsmElement list(ObservableProperty property, CsmElement separator, CsmElement preceeding, CsmElement following) {
      return new CsmList(property, none(), separator, preceeding, following);
   }

   static CsmElement list(ObservableProperty property, CsmElement separatorPre, CsmElement separatorPost, CsmElement preceeding, CsmElement following) {
      return new CsmList(property, separatorPre, separatorPost, preceeding, following);
   }

   static CsmElement orphanCommentsEnding() {
      return new CsmOrphanCommentsEnding();
   }

   static CsmElement orphanCommentsBeforeThis() {
      return new CsmNone();
   }

   static CsmElement indent() {
      return new CsmIndent();
   }

   static CsmElement unindent() {
      return new CsmUnindent();
   }

   static CsmElement block(CsmElement content) {
      return sequence(token(94), indent(), content, unindent(), token(95));
   }
}
