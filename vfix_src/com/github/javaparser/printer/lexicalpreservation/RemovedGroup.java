package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.JavaToken;
import com.github.javaparser.TokenRange;
import com.github.javaparser.TokenTypes;
import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class RemovedGroup implements Iterable<Removed> {
   private final Integer firstElementIndex;
   private final List<Removed> removedList;
   private boolean isProcessed = false;
   private final Function<JavaToken, Boolean> hasOnlyWhitespaceJavaTokenInFrontFunction = (begin) -> {
      return this.hasOnlyWhiteSpaceForTokenFunction(begin, (token) -> {
         return token.getPreviousToken();
      });
   };
   private final Function<JavaToken, Boolean> hasOnlyWhitespaceJavaTokenBehindFunction = (end) -> {
      return this.hasOnlyWhiteSpaceForTokenFunction(end, (token) -> {
         return token.getNextToken();
      });
   };
   private final Function<TokenRange, Boolean> hasOnlyWhitespaceInFrontFunction = (tokenRange) -> {
      return (Boolean)this.hasOnlyWhitespaceJavaTokenInFrontFunction.apply(tokenRange.getBegin());
   };
   private final Function<TokenRange, Boolean> hasOnlyWhitespaceBehindFunction = (tokenRange) -> {
      return (Boolean)this.hasOnlyWhitespaceJavaTokenBehindFunction.apply(tokenRange.getEnd());
   };

   private RemovedGroup(Integer firstElementIndex, List<Removed> removedList) {
      if (firstElementIndex == null) {
         throw new IllegalArgumentException("firstElementIndex should not be null");
      } else if (removedList != null && !removedList.isEmpty()) {
         this.firstElementIndex = firstElementIndex;
         this.removedList = removedList;
      } else {
         throw new IllegalArgumentException("removedList should not be null or empty");
      }
   }

   public static RemovedGroup of(Integer firstElementIndex, List<Removed> removedList) {
      return new RemovedGroup(firstElementIndex, removedList);
   }

   final void processed() {
      this.isProcessed = true;
   }

   final boolean isProcessed() {
      return this.isProcessed;
   }

   private List<Integer> getIndicesBeingRemoved() {
      return (List)IntStream.range(this.firstElementIndex, this.firstElementIndex + this.removedList.size()).boxed().collect(Collectors.toList());
   }

   final Integer getLastElementIndex() {
      List<Integer> indicesBeingRemoved = this.getIndicesBeingRemoved();
      return (Integer)indicesBeingRemoved.get(indicesBeingRemoved.size() - 1);
   }

   final Removed getFirstElement() {
      return (Removed)this.removedList.get(0);
   }

   final Removed getLastElement() {
      return (Removed)this.removedList.get(this.removedList.size() - 1);
   }

   final boolean isACompleteLine() {
      return this.hasOnlyWhitespace(this.getFirstElement(), this.hasOnlyWhitespaceInFrontFunction) && this.hasOnlyWhitespace(this.getLastElement(), this.hasOnlyWhitespaceBehindFunction);
   }

   private boolean hasOnlyWhitespace(Removed startElement, Function<TokenRange, Boolean> hasOnlyWhitespaceFunction) {
      boolean hasOnlyWhitespace = false;
      if (startElement.isChild()) {
         LexicalDifferenceCalculator.CsmChild csmChild = (LexicalDifferenceCalculator.CsmChild)startElement.getElement();
         Node child = csmChild.getChild();
         Optional<TokenRange> tokenRange = child.getTokenRange();
         if (tokenRange.isPresent()) {
            hasOnlyWhitespace = (Boolean)hasOnlyWhitespaceFunction.apply(tokenRange.get());
         }
      } else if (startElement.isToken()) {
         CsmToken token = (CsmToken)startElement.getElement();
         if (TokenTypes.isEndOfLineToken(token.getTokenType())) {
            hasOnlyWhitespace = true;
         }
      }

      return hasOnlyWhitespace;
   }

   private boolean hasOnlyWhiteSpaceForTokenFunction(JavaToken token, Function<JavaToken, Optional<JavaToken>> tokenFunction) {
      Optional<JavaToken> tokenResult = (Optional)tokenFunction.apply(token);
      if (tokenResult.isPresent()) {
         if (TokenTypes.isSpaceOrTab(((JavaToken)tokenResult.get()).getKind())) {
            return this.hasOnlyWhiteSpaceForTokenFunction((JavaToken)tokenResult.get(), tokenFunction);
         } else {
            return TokenTypes.isEndOfLineToken(((JavaToken)tokenResult.get()).getKind());
         }
      } else {
         return true;
      }
   }

   final Optional<Integer> getIndentation() {
      Removed firstElement = this.getFirstElement();
      int indentation = 0;
      if (firstElement.isChild()) {
         LexicalDifferenceCalculator.CsmChild csmChild = (LexicalDifferenceCalculator.CsmChild)firstElement.getElement();
         Node child = csmChild.getChild();
         Optional<TokenRange> tokenRange = child.getTokenRange();
         if (tokenRange.isPresent()) {
            JavaToken begin = ((TokenRange)tokenRange.get()).getBegin();
            if ((Boolean)this.hasOnlyWhitespaceJavaTokenInFrontFunction.apply(begin)) {
               Optional previousToken;
               for(previousToken = begin.getPreviousToken(); previousToken.isPresent() && TokenTypes.isSpaceOrTab(((JavaToken)previousToken.get()).getKind()); previousToken = ((JavaToken)previousToken.get()).getPreviousToken()) {
                  ++indentation;
               }

               if (previousToken.isPresent()) {
                  if (TokenTypes.isEndOfLineToken(((JavaToken)previousToken.get()).getKind())) {
                     return Optional.of(indentation);
                  }

                  return Optional.empty();
               }

               return Optional.of(indentation);
            }
         }
      }

      return Optional.empty();
   }

   public final Iterator<Removed> iterator() {
      return new Iterator<Removed>() {
         private int currentIndex = 0;

         public boolean hasNext() {
            return this.currentIndex < RemovedGroup.this.removedList.size() && RemovedGroup.this.removedList.get(this.currentIndex) != null;
         }

         public Removed next() {
            return (Removed)RemovedGroup.this.removedList.get(this.currentIndex++);
         }
      };
   }
}
