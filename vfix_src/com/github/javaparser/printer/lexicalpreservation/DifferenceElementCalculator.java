package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmIndent;
import com.github.javaparser.printer.concretesyntaxmodel.CsmMix;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import com.github.javaparser.printer.concretesyntaxmodel.CsmUnindent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class DifferenceElementCalculator {
   static boolean matching(CsmElement a, CsmElement b) {
      if (a instanceof LexicalDifferenceCalculator.CsmChild) {
         if (b instanceof LexicalDifferenceCalculator.CsmChild) {
            LexicalDifferenceCalculator.CsmChild childA = (LexicalDifferenceCalculator.CsmChild)a;
            LexicalDifferenceCalculator.CsmChild childB = (LexicalDifferenceCalculator.CsmChild)b;
            return childA.getChild().equals(childB.getChild());
         } else if (b instanceof CsmToken) {
            return false;
         } else if (b instanceof CsmIndent) {
            return false;
         } else if (b instanceof CsmUnindent) {
            return false;
         } else {
            throw new UnsupportedOperationException(a.getClass().getSimpleName() + " " + b.getClass().getSimpleName());
         }
      } else if (a instanceof CsmToken) {
         if (b instanceof CsmToken) {
            CsmToken childA = (CsmToken)a;
            CsmToken childB = (CsmToken)b;
            return childA.getTokenType() == childB.getTokenType();
         } else if (b instanceof LexicalDifferenceCalculator.CsmChild) {
            return false;
         } else if (b instanceof CsmIndent) {
            return false;
         } else if (b instanceof CsmUnindent) {
            return false;
         } else {
            throw new UnsupportedOperationException(a.getClass().getSimpleName() + " " + b.getClass().getSimpleName());
         }
      } else if (a instanceof CsmIndent) {
         return b instanceof CsmIndent;
      } else if (a instanceof CsmUnindent) {
         return b instanceof CsmUnindent;
      } else {
         throw new UnsupportedOperationException(a.getClass().getSimpleName() + " " + b.getClass().getSimpleName());
      }
   }

   private static boolean replacement(CsmElement a, CsmElement b) {
      if (!(a instanceof CsmIndent) && !(b instanceof CsmIndent) && !(a instanceof CsmUnindent) && !(b instanceof CsmUnindent)) {
         if (a instanceof LexicalDifferenceCalculator.CsmChild) {
            if (b instanceof LexicalDifferenceCalculator.CsmChild) {
               LexicalDifferenceCalculator.CsmChild childA = (LexicalDifferenceCalculator.CsmChild)a;
               LexicalDifferenceCalculator.CsmChild childB = (LexicalDifferenceCalculator.CsmChild)b;
               return childA.getChild().getClass().equals(childB.getChild().getClass());
            } else if (b instanceof CsmToken) {
               return false;
            } else {
               throw new UnsupportedOperationException(a.getClass().getSimpleName() + " " + b.getClass().getSimpleName());
            }
         } else {
            if (a instanceof CsmToken) {
               if (b instanceof CsmToken) {
                  CsmToken childA = (CsmToken)a;
                  CsmToken childB = (CsmToken)b;
                  return childA.getTokenType() == childB.getTokenType();
               }

               if (b instanceof LexicalDifferenceCalculator.CsmChild) {
                  return false;
               }
            }

            throw new UnsupportedOperationException(a.getClass().getSimpleName() + " " + b.getClass().getSimpleName());
         }
      } else {
         return false;
      }
   }

   private static Map<Node, Integer> findChildrenPositions(LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModel) {
      Map<Node, Integer> positions = new HashMap();

      for(int i = 0; i < calculatedSyntaxModel.elements.size(); ++i) {
         CsmElement element = (CsmElement)calculatedSyntaxModel.elements.get(i);
         if (element instanceof LexicalDifferenceCalculator.CsmChild) {
            positions.put(((LexicalDifferenceCalculator.CsmChild)element).getChild(), i);
         }
      }

      return positions;
   }

   static List<DifferenceElement> calculate(LexicalDifferenceCalculator.CalculatedSyntaxModel original, LexicalDifferenceCalculator.CalculatedSyntaxModel after) {
      Map<Node, Integer> childrenInOriginal = findChildrenPositions(original);
      Map<Node, Integer> childrenInAfter = findChildrenPositions(after);
      List<Node> commonChildren = new LinkedList(childrenInOriginal.keySet());
      commonChildren.retainAll(childrenInAfter.keySet());
      childrenInOriginal.getClass();
      commonChildren.sort(Comparator.comparingInt(childrenInOriginal::get));
      List<DifferenceElement> elements = new LinkedList();
      int originalIndex = 0;
      int afterIndex = 0;

      int posOfNextChildInAfter;
      for(int commonChildrenIndex = 0; commonChildrenIndex < commonChildren.size(); afterIndex = posOfNextChildInAfter + 1) {
         Node child = (Node)commonChildren.get(commonChildrenIndex++);
         int posOfNextChildInOriginal = (Integer)childrenInOriginal.get(child);
         posOfNextChildInAfter = (Integer)childrenInAfter.get(child);
         if (originalIndex < posOfNextChildInOriginal || afterIndex < posOfNextChildInAfter) {
            elements.addAll(calculateImpl(original.sub(originalIndex, posOfNextChildInOriginal), after.sub(afterIndex, posOfNextChildInAfter)));
         }

         elements.add(new Kept(new LexicalDifferenceCalculator.CsmChild(child)));
         originalIndex = posOfNextChildInOriginal + 1;
      }

      if (originalIndex < original.elements.size() || afterIndex < after.elements.size()) {
         elements.addAll(calculateImpl(original.sub(originalIndex, original.elements.size()), after.sub(afterIndex, after.elements.size())));
      }

      return elements;
   }

   private static List<DifferenceElement> calculateImpl(LexicalDifferenceCalculator.CalculatedSyntaxModel original, LexicalDifferenceCalculator.CalculatedSyntaxModel after) {
      List<DifferenceElement> elements = new LinkedList();
      int originalIndex = 0;
      int afterIndex = 0;

      do {
         if (originalIndex < original.elements.size() && afterIndex >= after.elements.size()) {
            elements.add(new Removed((CsmElement)original.elements.get(originalIndex)));
            ++originalIndex;
         } else if (originalIndex >= original.elements.size() && afterIndex < after.elements.size()) {
            elements.add(new Added((CsmElement)after.elements.get(afterIndex)));
            ++afterIndex;
         } else {
            CsmElement nextOriginal = (CsmElement)original.elements.get(originalIndex);
            CsmElement nextAfter = (CsmElement)after.elements.get(afterIndex);
            if (nextOriginal instanceof CsmMix && nextAfter instanceof CsmMix) {
               if (((CsmMix)nextAfter).getElements().equals(((CsmMix)nextOriginal).getElements())) {
                  ((CsmMix)nextAfter).getElements().forEach((el) -> {
                     elements.add(new Kept(el));
                  });
               } else {
                  elements.add(new Reshuffled((CsmMix)nextOriginal, (CsmMix)nextAfter));
               }

               ++originalIndex;
               ++afterIndex;
            } else if (matching(nextOriginal, nextAfter)) {
               elements.add(new Kept(nextOriginal));
               ++originalIndex;
               ++afterIndex;
            } else if (replacement(nextOriginal, nextAfter)) {
               elements.add(new Removed(nextOriginal));
               elements.add(new Added(nextAfter));
               ++originalIndex;
               ++afterIndex;
            } else {
               List<DifferenceElement> addingElements = calculate(original.from(originalIndex), after.from(afterIndex + 1));
               List<DifferenceElement> removingElements = null;
               if (cost(addingElements) > 0L) {
                  removingElements = calculate(original.from(originalIndex + 1), after.from(afterIndex));
               }

               if (removingElements != null && cost(removingElements) <= cost(addingElements)) {
                  elements.add(new Removed(nextOriginal));
                  ++originalIndex;
               } else {
                  elements.add(new Added(nextAfter));
                  ++afterIndex;
               }
            }
         }
      } while(originalIndex < original.elements.size() || afterIndex < after.elements.size());

      return elements;
   }

   private static long cost(List<DifferenceElement> elements) {
      return elements.stream().filter((e) -> {
         return !(e instanceof Kept);
      }).count();
   }

   static void removeIndentationElements(List<DifferenceElement> elements) {
      elements.removeIf((el) -> {
         return el.getElement() instanceof CsmIndent || el.getElement() instanceof CsmUnindent;
      });
   }
}
