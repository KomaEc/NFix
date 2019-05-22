package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.TokenTypes;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmMix;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import com.github.javaparser.printer.concretesyntaxmodel.CsmUnindent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public class Difference {
   public static final int STANDARD_INDENTATION_SIZE = 4;
   private final NodeText nodeText;
   private final Node node;
   private final List<DifferenceElement> diffElements;
   private final List<TextElement> originalElements;
   private int originalIndex = 0;
   private int diffIndex = 0;
   private final List<TokenTextElement> indentation;
   private boolean addedIndentation = false;

   Difference(List<DifferenceElement> diffElements, NodeText nodeText, Node node) {
      if (nodeText == null) {
         throw new NullPointerException("nodeText can not be null");
      } else {
         this.nodeText = nodeText;
         this.node = node;
         this.diffElements = diffElements;
         this.originalElements = nodeText.getElements();
         this.indentation = LexicalPreservingPrinter.findIndentation(node);
      }
   }

   private List<TextElement> processIndentation(List<TokenTextElement> indentation, List<TextElement> prevElements) {
      List<TextElement> res = new LinkedList();
      res.addAll(indentation);
      boolean afterNl = false;
      Iterator var5 = prevElements.iterator();

      while(true) {
         while(true) {
            while(var5.hasNext()) {
               TextElement e = (TextElement)var5.next();
               if (!e.isNewline() && !e.isToken(5)) {
                  if (afterNl && e instanceof TokenTextElement && TokenTypes.isWhitespace(((TokenTextElement)e).getTokenKind())) {
                     res.add(e);
                  } else {
                     afterNl = false;
                  }
               } else {
                  res.clear();
                  afterNl = true;
               }
            }

            return res;
         }
      }
   }

   private List<TextElement> indentationBlock() {
      List<TextElement> res = new LinkedList();
      res.add(new TokenTextElement(1));
      res.add(new TokenTextElement(1));
      res.add(new TokenTextElement(1));
      res.add(new TokenTextElement(1));
      return res;
   }

   private boolean isAfterLBrace(NodeText nodeText, int nodeTextIndex) {
      if (nodeTextIndex > 0 && ((TextElement)nodeText.getElements().get(nodeTextIndex - 1)).isToken(94)) {
         return true;
      } else {
         return nodeTextIndex > 0 && ((TextElement)nodeText.getElements().get(nodeTextIndex - 1)).isSpaceOrTab() ? this.isAfterLBrace(nodeText, nodeTextIndex - 1) : false;
      }
   }

   private int considerEnforcingIndentation(NodeText nodeText, int nodeTextIndex) {
      boolean hasOnlyWsBefore = true;

      int i;
      for(i = nodeTextIndex; i >= 0 && hasOnlyWsBefore && i < nodeText.getElements().size() && !((TextElement)nodeText.getElements().get(i)).isNewline(); --i) {
         if (!((TextElement)nodeText.getElements().get(i)).isSpaceOrTab()) {
            hasOnlyWsBefore = false;
         }
      }

      if (hasOnlyWsBefore) {
         for(i = nodeTextIndex; i >= 0 && i < nodeText.getElements().size() && !((TextElement)nodeText.getElements().get(i)).isNewline(); --i) {
            nodeText.removeElement(i);
         }
      }

      return nodeTextIndex;
   }

   void apply() {
      this.extractReshuffledDiffElements(this.diffElements);
      Map removedGroups = this.combineRemovedElementsToRemovedGroups();

      do {
         boolean isLeftOverDiffElement = this.applyLeftOverDiffElements();
         boolean isLeftOverOriginalElement = this.applyLeftOverOriginalElements();
         if (!isLeftOverDiffElement && !isLeftOverOriginalElement) {
            DifferenceElement diffElement = (DifferenceElement)this.diffElements.get(this.diffIndex);
            if (diffElement instanceof Added) {
               this.applyAddedDiffElement((Added)diffElement);
            } else {
               TextElement originalElement = (TextElement)this.originalElements.get(this.originalIndex);
               boolean originalElementIsChild = originalElement instanceof ChildTextElement;
               boolean originalElementIsToken = originalElement instanceof TokenTextElement;
               if (diffElement instanceof Kept) {
                  this.applyKeptDiffElement((Kept)diffElement, originalElement, originalElementIsChild, originalElementIsToken);
               } else {
                  if (!(diffElement instanceof Removed)) {
                     throw new UnsupportedOperationException("" + diffElement + " vs " + originalElement);
                  }

                  Removed removed = (Removed)diffElement;
                  this.applyRemovedDiffElement((RemovedGroup)removedGroups.get(removed), removed, originalElement, originalElementIsChild, originalElementIsToken);
               }
            }
         }
      } while(this.diffIndex < this.diffElements.size() || this.originalIndex < this.originalElements.size());

   }

   private boolean applyLeftOverOriginalElements() {
      boolean isLeftOverElement = false;
      if (this.diffIndex >= this.diffElements.size() && this.originalIndex < this.originalElements.size()) {
         TextElement originalElement = (TextElement)this.originalElements.get(this.originalIndex);
         if (!originalElement.isWhiteSpaceOrComment()) {
            throw new UnsupportedOperationException("NodeText: " + this.nodeText + ". Difference: " + this + " " + originalElement);
         }

         ++this.originalIndex;
         isLeftOverElement = true;
      }

      return isLeftOverElement;
   }

   private boolean applyLeftOverDiffElements() {
      boolean isLeftOverElement = false;
      if (this.diffIndex < this.diffElements.size() && this.originalIndex >= this.originalElements.size()) {
         DifferenceElement diffElement = (DifferenceElement)this.diffElements.get(this.diffIndex);
         if (diffElement instanceof Kept) {
            Kept kept = (Kept)diffElement;
            if (!kept.isWhiteSpaceOrComment() && !kept.isIndent() && !kept.isUnindent()) {
               throw new IllegalStateException("Cannot keep element because we reached the end of nodetext: " + this.nodeText + ". Difference: " + this);
            }

            ++this.diffIndex;
         } else {
            if (!(diffElement instanceof Added)) {
               throw new UnsupportedOperationException(diffElement.getClass().getSimpleName());
            }

            Added addedElement = (Added)diffElement;
            this.nodeText.addElement(this.originalIndex, addedElement.toTextElement());
            ++this.originalIndex;
            ++this.diffIndex;
         }

         isLeftOverElement = true;
      }

      return isLeftOverElement;
   }

   private void extractReshuffledDiffElements(List<DifferenceElement> diffElements) {
      for(int index = 0; index < diffElements.size(); ++index) {
         DifferenceElement diffElement = (DifferenceElement)diffElements.get(index);
         if (diffElement instanceof Reshuffled) {
            Reshuffled reshuffled = (Reshuffled)diffElement;
            CsmMix elementsFromPreviousOrder = reshuffled.getPreviousOrder();
            CsmMix elementsFromNextOrder = reshuffled.getNextOrder();
            Map<Integer, Integer> correspondanceBetweenNextOrderAndPreviousOrder = this.getCorrespondanceBetweenNextOrderAndPreviousOrder(elementsFromPreviousOrder, elementsFromNextOrder);
            List<Integer> nodeTextIndexOfPreviousElements = this.findIndexOfCorrespondingNodeTextElement(elementsFromPreviousOrder.getElements(), this.nodeText, this.originalIndex, this.node);
            Map<Integer, Integer> nodeTextIndexToPreviousCSMIndex = new HashMap();

            int lastNodeTextIndex;
            for(lastNodeTextIndex = 0; lastNodeTextIndex < nodeTextIndexOfPreviousElements.size(); ++lastNodeTextIndex) {
               int value = (Integer)nodeTextIndexOfPreviousElements.get(lastNodeTextIndex);
               if (value != -1) {
                  nodeTextIndexToPreviousCSMIndex.put(value, lastNodeTextIndex);
               }
            }

            lastNodeTextIndex = (Integer)nodeTextIndexOfPreviousElements.stream().max(Integer::compareTo).orElse(-1);
            List<CsmElement> elementsToBeAddedAtTheEnd = new LinkedList();
            List<CsmElement> nextOrderElements = elementsFromNextOrder.getElements();
            Map<Integer, List<CsmElement>> elementsToAddBeforeGivenOriginalCSMElement = new HashMap();

            int ni;
            int originalCsmIndex;
            int indexOfOriginalCSMElement;
            for(ni = 0; ni < nextOrderElements.size(); ++ni) {
               if (!correspondanceBetweenNextOrderAndPreviousOrder.containsKey(ni)) {
                  originalCsmIndex = -1;

                  for(indexOfOriginalCSMElement = ni + 1; indexOfOriginalCSMElement < nextOrderElements.size() && originalCsmIndex == -1; ++indexOfOriginalCSMElement) {
                     if (correspondanceBetweenNextOrderAndPreviousOrder.containsKey(indexOfOriginalCSMElement)) {
                        originalCsmIndex = (Integer)correspondanceBetweenNextOrderAndPreviousOrder.get(indexOfOriginalCSMElement);
                        if (!elementsToAddBeforeGivenOriginalCSMElement.containsKey(originalCsmIndex)) {
                           elementsToAddBeforeGivenOriginalCSMElement.put(originalCsmIndex, new LinkedList());
                        }

                        ((List)elementsToAddBeforeGivenOriginalCSMElement.get(originalCsmIndex)).add(nextOrderElements.get(ni));
                     }
                  }

                  if (originalCsmIndex == -1) {
                     elementsToBeAddedAtTheEnd.add(nextOrderElements.get(ni));
                  }
               }
            }

            diffElements.remove(index);
            ni = index;
            if (lastNodeTextIndex != -1) {
               for(originalCsmIndex = this.originalIndex; originalCsmIndex <= lastNodeTextIndex; ++originalCsmIndex) {
                  if (nodeTextIndexToPreviousCSMIndex.containsKey(originalCsmIndex)) {
                     indexOfOriginalCSMElement = (Integer)nodeTextIndexToPreviousCSMIndex.get(originalCsmIndex);
                     if (elementsToAddBeforeGivenOriginalCSMElement.containsKey(indexOfOriginalCSMElement)) {
                        Iterator var17 = ((List)elementsToAddBeforeGivenOriginalCSMElement.get(indexOfOriginalCSMElement)).iterator();

                        while(var17.hasNext()) {
                           CsmElement elementToAdd = (CsmElement)var17.next();
                           diffElements.add(ni++, new Added(elementToAdd));
                        }
                     }

                     CsmElement originalCSMElement = (CsmElement)elementsFromPreviousOrder.getElements().get(indexOfOriginalCSMElement);
                     boolean toBeKept = correspondanceBetweenNextOrderAndPreviousOrder.containsValue(indexOfOriginalCSMElement);
                     if (toBeKept) {
                        diffElements.add(ni++, new Kept(originalCSMElement));
                     } else {
                        diffElements.add(ni++, new Removed(originalCSMElement));
                     }
                  }
               }
            }

            Iterator var20 = elementsToBeAddedAtTheEnd.iterator();

            while(var20.hasNext()) {
               CsmElement elementToAdd = (CsmElement)var20.next();
               diffElements.add(ni++, new Added(elementToAdd));
            }
         }
      }

   }

   private Map<Removed, RemovedGroup> combineRemovedElementsToRemovedGroups() {
      Map<Integer, List<Removed>> removedElementsMap = this.groupConsecutiveRemovedElements();
      List<RemovedGroup> removedGroups = new ArrayList();
      Iterator var3 = removedElementsMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Integer, List<Removed>> entry = (Entry)var3.next();
         removedGroups.add(RemovedGroup.of((Integer)entry.getKey(), (List)entry.getValue()));
      }

      Map<Removed, RemovedGroup> map = new HashMap();
      Iterator var9 = removedGroups.iterator();

      while(var9.hasNext()) {
         RemovedGroup removedGroup = (RemovedGroup)var9.next();
         Iterator var6 = removedGroup.iterator();

         while(var6.hasNext()) {
            Removed index = (Removed)var6.next();
            map.put(index, removedGroup);
         }
      }

      return map;
   }

   private Map<Integer, List<Removed>> groupConsecutiveRemovedElements() {
      Map<Integer, List<Removed>> removedElementsMap = new HashMap();
      Integer firstElement = null;

      for(int i = 0; i < this.diffElements.size(); ++i) {
         DifferenceElement diffElement = (DifferenceElement)this.diffElements.get(i);
         if (diffElement instanceof Removed) {
            if (firstElement == null) {
               firstElement = i;
            }

            ((List)removedElementsMap.computeIfAbsent(firstElement, (key) -> {
               return new ArrayList();
            })).add((Removed)diffElement);
         } else {
            firstElement = null;
         }
      }

      return removedElementsMap;
   }

   private void applyRemovedDiffElement(RemovedGroup removedGroup, Removed removed, TextElement originalElement, boolean originalElementIsChild, boolean originalElementIsToken) {
      if (removed.isChild() && originalElementIsChild) {
         ChildTextElement originalElementChild = (ChildTextElement)originalElement;
         if (originalElementChild.isComment()) {
            Comment comment = (Comment)originalElementChild.getChild();
            if (!comment.isOrphan() && comment.getCommentedNode().isPresent() && ((Node)comment.getCommentedNode().get()).equals(removed.getChild())) {
               this.nodeText.removeElement(this.originalIndex);
            } else {
               ++this.originalIndex;
            }
         } else {
            this.nodeText.removeElement(this.originalIndex);
            if ((this.diffIndex + 1 >= this.diffElements.size() || !(this.diffElements.get(this.diffIndex + 1) instanceof Added)) && !removedGroup.isACompleteLine()) {
               this.originalIndex = this.considerEnforcingIndentation(this.nodeText, this.originalIndex);
            }

            if (this.originalElements.size() > this.originalIndex && this.originalIndex > 0 && ((TextElement)this.originalElements.get(this.originalIndex)).isWhiteSpace() && ((TextElement)this.originalElements.get(this.originalIndex - 1)).isWhiteSpace() && (this.diffIndex + 1 == this.diffElements.size() || this.diffElements.get(this.diffIndex + 1) instanceof Kept)) {
               this.originalElements.remove(this.originalIndex--);
            }

            ++this.diffIndex;
         }
      } else if (removed.isToken() && originalElementIsToken && removed.getTokenType() == ((TokenTextElement)originalElement).getTokenKind()) {
         this.nodeText.removeElement(this.originalIndex);
         ++this.diffIndex;
      } else if (originalElementIsToken && originalElement.isWhiteSpaceOrComment()) {
         ++this.originalIndex;
      } else if (removed.isPrimitiveType()) {
         if (!this.isPrimitiveType(originalElement)) {
            throw new UnsupportedOperationException("removed " + removed.getElement() + " vs " + originalElement);
         }

         this.nodeText.removeElement(this.originalIndex);
         ++this.diffIndex;
      } else if (removed.isWhiteSpace()) {
         ++this.diffIndex;
      } else {
         if (!originalElement.isWhiteSpace()) {
            throw new UnsupportedOperationException("removed " + removed.getElement() + " vs " + originalElement);
         }

         ++this.originalIndex;
      }

      this.cleanTheLineOfLeftOverSpace(removedGroup, removed);
   }

   private void cleanTheLineOfLeftOverSpace(RemovedGroup removedGroup, Removed removed) {
      if (this.originalIndex < this.originalElements.size()) {
         if (!removedGroup.isProcessed() && removedGroup.getLastElement() == removed && removedGroup.isACompleteLine()) {
            Integer lastElementIndex = removedGroup.getLastElementIndex();
            Optional<Integer> indentation = removedGroup.getIndentation();
            if (indentation.isPresent() && !this.isReplaced(lastElementIndex)) {
               for(int i = 0; i < (Integer)indentation.get(); ++i) {
                  if (((TextElement)this.originalElements.get(this.originalIndex)).isSpaceOrTab()) {
                     this.nodeText.removeElement(this.originalIndex);
                  } else if (this.originalIndex >= 1 && ((TextElement)this.originalElements.get(this.originalIndex - 1)).isSpaceOrTab()) {
                     this.nodeText.removeElement(this.originalIndex - 1);
                     --this.originalIndex;
                  }
               }
            }

            removedGroup.processed();
         }

      }
   }

   private void applyKeptDiffElement(Kept kept, TextElement originalElement, boolean originalElementIsChild, boolean originalElementIsToken) {
      if (originalElement.isComment()) {
         ++this.originalIndex;
      } else if (kept.isChild() && originalElementIsChild) {
         ++this.diffIndex;
         ++this.originalIndex;
      } else if (kept.isChild() && originalElementIsToken) {
         if (originalElement.isWhiteSpaceOrComment()) {
            ++this.originalIndex;
         } else {
            if (!kept.isPrimitiveType()) {
               throw new UnsupportedOperationException("kept " + kept.getElement() + " vs " + originalElement);
            }

            ++this.originalIndex;
            ++this.diffIndex;
         }
      } else if (kept.isToken() && originalElementIsToken) {
         TokenTextElement originalTextToken = (TokenTextElement)originalElement;
         if (kept.getTokenType() == originalTextToken.getTokenKind()) {
            ++this.originalIndex;
            ++this.diffIndex;
         } else if (kept.isWhiteSpaceOrComment()) {
            ++this.diffIndex;
         } else {
            if (!originalTextToken.isWhiteSpaceOrComment()) {
               throw new UnsupportedOperationException("Csm token " + kept.getElement() + " NodeText TOKEN " + originalTextToken);
            }

            ++this.originalIndex;
         }
      } else if (kept.isWhiteSpace()) {
         ++this.diffIndex;
      } else if (kept.isIndent()) {
         ++this.diffIndex;
      } else {
         if (!kept.isUnindent()) {
            throw new UnsupportedOperationException("kept " + kept.getElement() + " vs " + originalElement);
         }

         ++this.diffIndex;

         for(int i = 0; i < 4 && this.originalIndex >= 1 && this.nodeText.getTextElement(this.originalIndex - 1).isSpaceOrTab(); ++i) {
            this.nodeText.removeElement(--this.originalIndex);
         }
      }

   }

   private void applyAddedDiffElement(Added added) {
      int i;
      if (added.isIndent()) {
         for(i = 0; i < 4; ++i) {
            this.indentation.add(new TokenTextElement(1));
         }

         this.addedIndentation = true;
         ++this.diffIndex;
      } else if (added.isUnindent()) {
         for(i = 0; i < 4 && !this.indentation.isEmpty(); ++i) {
            this.indentation.remove(this.indentation.size() - 1);
         }

         this.addedIndentation = false;
         ++this.diffIndex;
      } else {
         TextElement addedTextElement = added.toTextElement();
         boolean used = false;
         Iterator var4;
         TextElement e;
         if (this.originalIndex > 0 && ((TextElement)this.originalElements.get(this.originalIndex - 1)).isNewline()) {
            var4 = this.processIndentation(this.indentation, this.originalElements.subList(0, this.originalIndex - 1)).iterator();

            while(var4.hasNext()) {
               e = (TextElement)var4.next();
               this.nodeText.addElement(this.originalIndex++, e);
            }
         } else if (this.isAfterLBrace(this.nodeText, this.originalIndex) && !this.isAReplacement(this.diffIndex)) {
            if (addedTextElement.isNewline()) {
               used = true;
            }

            this.nodeText.addElement(this.originalIndex++, new TokenTextElement(TokenTypes.eolTokenKind()));

            while(((TextElement)this.originalElements.get(this.originalIndex)).isSpaceOrTab()) {
               this.originalElements.remove(this.originalIndex);
            }

            var4 = this.processIndentation(this.indentation, this.originalElements.subList(0, this.originalIndex - 1)).iterator();

            while(var4.hasNext()) {
               e = (TextElement)var4.next();
               this.nodeText.addElement(this.originalIndex++, e);
            }

            if (!this.addedIndentation) {
               var4 = this.indentationBlock().iterator();

               while(var4.hasNext()) {
                  e = (TextElement)var4.next();
                  this.nodeText.addElement(this.originalIndex++, e);
               }
            }
         }

         if (!used) {
            this.nodeText.addElement(this.originalIndex, addedTextElement);
            ++this.originalIndex;
         }

         if (addedTextElement.isNewline()) {
            boolean followedByUnindent = this.isFollowedByUnindent(this.diffElements, this.diffIndex);
            this.originalIndex = this.adjustIndentation(this.indentation, this.nodeText, this.originalIndex, followedByUnindent);
         }

         ++this.diffIndex;
      }
   }

   private Map<Integer, Integer> getCorrespondanceBetweenNextOrderAndPreviousOrder(CsmMix elementsFromPreviousOrder, CsmMix elementsFromNextOrder) {
      Map<Integer, Integer> correspondanceBetweenNextOrderAndPreviousOrder = new HashMap();
      List<CsmElement> nextOrderElements = elementsFromNextOrder.getElements();

      for(int ni = 0; ni < nextOrderElements.size(); ++ni) {
         boolean found = false;
         CsmElement ne = (CsmElement)nextOrderElements.get(ni);
         List<CsmElement> previousOrderElements = elementsFromPreviousOrder.getElements();

         for(int pi = 0; pi < previousOrderElements.size() && !found; ++pi) {
            CsmElement pe = (CsmElement)previousOrderElements.get(pi);
            if (!correspondanceBetweenNextOrderAndPreviousOrder.values().contains(pi) && DifferenceElementCalculator.matching(ne, pe)) {
               found = true;
               correspondanceBetweenNextOrderAndPreviousOrder.put(ni, pi);
            }
         }
      }

      return correspondanceBetweenNextOrderAndPreviousOrder;
   }

   private boolean isFollowedByUnindent(List<DifferenceElement> diffElements, int diffIndex) {
      return diffIndex + 1 < diffElements.size() && ((DifferenceElement)diffElements.get(diffIndex + 1)).isAdded() && ((DifferenceElement)diffElements.get(diffIndex + 1)).getElement() instanceof CsmUnindent;
   }

   private List<Integer> findIndexOfCorrespondingNodeTextElement(List<CsmElement> elements, NodeText nodeText, int startIndex, Node node) {
      List<Integer> correspondingIndices = new ArrayList();
      ListIterator csmElementListIterator = elements.listIterator();

      while(csmElementListIterator.hasNext()) {
         int previousCsmElementIndex = csmElementListIterator.previousIndex();
         CsmElement csmElement = (CsmElement)csmElementListIterator.next();
         int nextCsmElementIndex = csmElementListIterator.nextIndex();
         Map<Difference.MatchClassification, Integer> potentialMatches = new EnumMap(Difference.MatchClassification.class);

         for(int i = startIndex; i < nodeText.getElements().size(); ++i) {
            if (!correspondingIndices.contains(i)) {
               TextElement textElement = nodeText.getTextElement(i);
               boolean isCorresponding = this.isCorrespondingElement(textElement, csmElement, node);
               if (isCorresponding) {
                  boolean hasSamePreviousElement = false;
                  if (i > 0 && previousCsmElementIndex > -1) {
                     TextElement previousTextElement = nodeText.getTextElement(i - 1);
                     hasSamePreviousElement = this.isCorrespondingElement(previousTextElement, (CsmElement)elements.get(previousCsmElementIndex), node);
                  }

                  boolean hasSameNextElement = false;
                  if (i < nodeText.getElements().size() - 1 && nextCsmElementIndex < elements.size()) {
                     TextElement nextTextElement = nodeText.getTextElement(i + 1);
                     hasSameNextElement = this.isCorrespondingElement(nextTextElement, (CsmElement)elements.get(nextCsmElementIndex), node);
                  }

                  if (hasSamePreviousElement && hasSameNextElement) {
                     potentialMatches.putIfAbsent(Difference.MatchClassification.ALL, i);
                  } else if (hasSamePreviousElement) {
                     potentialMatches.putIfAbsent(Difference.MatchClassification.PREVIOUS_AND_SAME, i);
                  } else if (hasSameNextElement) {
                     potentialMatches.putIfAbsent(Difference.MatchClassification.NEXT_AND_SAME, i);
                  } else {
                     potentialMatches.putIfAbsent(Difference.MatchClassification.SAME_ONLY, i);
                  }
               }
            }
         }

         Optional<Difference.MatchClassification> bestMatchKey = potentialMatches.keySet().stream().min(Comparator.comparing(Difference.MatchClassification::getPriority));
         if (bestMatchKey.isPresent()) {
            correspondingIndices.add(potentialMatches.get(bestMatchKey.get()));
         } else {
            correspondingIndices.add(-1);
         }
      }

      return correspondingIndices;
   }

   private boolean isCorrespondingElement(TextElement textElement, CsmElement csmElement, Node node) {
      if (csmElement instanceof CsmToken) {
         CsmToken csmToken = (CsmToken)csmElement;
         if (textElement instanceof TokenTextElement) {
            TokenTextElement tokenTextElement = (TokenTextElement)textElement;
            return tokenTextElement.getTokenKind() == csmToken.getTokenType() && tokenTextElement.getText().equals(csmToken.getContent(node));
         }
      } else {
         if (!(csmElement instanceof LexicalDifferenceCalculator.CsmChild)) {
            throw new UnsupportedOperationException();
         }

         LexicalDifferenceCalculator.CsmChild csmChild = (LexicalDifferenceCalculator.CsmChild)csmElement;
         if (textElement instanceof ChildTextElement) {
            ChildTextElement childTextElement = (ChildTextElement)textElement;
            return childTextElement.getChild() == csmChild.getChild();
         }
      }

      return false;
   }

   private int adjustIndentation(List<TokenTextElement> indentation, NodeText nodeText, int nodeTextIndex, boolean followedByUnindent) {
      List<TextElement> indentationAdj = this.processIndentation(indentation, nodeText.getElements().subList(0, nodeTextIndex - 1));
      if (nodeTextIndex < nodeText.getElements().size() && ((TextElement)nodeText.getElements().get(nodeTextIndex)).isToken(95)) {
         indentationAdj = indentationAdj.subList(0, indentationAdj.size() - Math.min(4, indentationAdj.size()));
      } else if (followedByUnindent) {
         indentationAdj = indentationAdj.subList(0, Math.max(0, indentationAdj.size() - 4));
      }

      Iterator var6 = indentationAdj.iterator();

      while(true) {
         while(var6.hasNext()) {
            TextElement e = (TextElement)var6.next();
            if (nodeTextIndex < nodeText.getElements().size() && ((TextElement)nodeText.getElements().get(nodeTextIndex)).isSpaceOrTab()) {
               ++nodeTextIndex;
            } else {
               nodeText.getElements().add(nodeTextIndex++, e);
            }
         }

         return nodeTextIndex;
      }
   }

   private boolean isAReplacement(int diffIndex) {
      return diffIndex > 0 && this.diffElements.get(diffIndex) instanceof Added && this.diffElements.get(diffIndex - 1) instanceof Removed;
   }

   private boolean isReplaced(int diffIndex) {
      return diffIndex < this.diffElements.size() - 1 && this.diffElements.get(diffIndex + 1) instanceof Added && this.diffElements.get(diffIndex) instanceof Removed;
   }

   private boolean isPrimitiveType(TextElement textElement) {
      if (!(textElement instanceof TokenTextElement)) {
         return false;
      } else {
         TokenTextElement tokenTextElement = (TokenTextElement)textElement;
         int tokenKind = tokenTextElement.getTokenKind();
         return tokenKind == 15 || tokenKind == 18 || tokenKind == 49 || tokenKind == 38 || tokenKind == 40 || tokenKind == 31 || tokenKind == 24;
      }
   }

   public String toString() {
      return "Difference{" + this.diffElements + '}';
   }

   private static enum MatchClassification {
      ALL(1),
      PREVIOUS_AND_SAME(2),
      NEXT_AND_SAME(3),
      SAME_ONLY(4);

      private final int priority;

      private MatchClassification(int priority) {
         this.priority = priority;
      }

      int getPriority() {
         return this.priority;
      }
   }
}
