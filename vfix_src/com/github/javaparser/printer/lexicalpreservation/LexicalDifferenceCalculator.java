package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.printer.ConcreteSyntaxModel;
import com.github.javaparser.printer.Printable;
import com.github.javaparser.printer.SourcePrinter;
import com.github.javaparser.printer.concretesyntaxmodel.CsmAttribute;
import com.github.javaparser.printer.concretesyntaxmodel.CsmComment;
import com.github.javaparser.printer.concretesyntaxmodel.CsmConditional;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmIndent;
import com.github.javaparser.printer.concretesyntaxmodel.CsmList;
import com.github.javaparser.printer.concretesyntaxmodel.CsmMix;
import com.github.javaparser.printer.concretesyntaxmodel.CsmNone;
import com.github.javaparser.printer.concretesyntaxmodel.CsmOrphanCommentsEnding;
import com.github.javaparser.printer.concretesyntaxmodel.CsmSequence;
import com.github.javaparser.printer.concretesyntaxmodel.CsmSingleReference;
import com.github.javaparser.printer.concretesyntaxmodel.CsmString;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import com.github.javaparser.printer.concretesyntaxmodel.CsmUnindent;
import com.github.javaparser.printer.lexicalpreservation.changes.Change;
import com.github.javaparser.printer.lexicalpreservation.changes.ListAdditionChange;
import com.github.javaparser.printer.lexicalpreservation.changes.ListRemovalChange;
import com.github.javaparser.printer.lexicalpreservation.changes.ListReplacementChange;
import com.github.javaparser.printer.lexicalpreservation.changes.NoChange;
import com.github.javaparser.printer.lexicalpreservation.changes.PropertyChange;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class LexicalDifferenceCalculator {
   List<DifferenceElement> calculateListRemovalDifference(ObservableProperty observableProperty, NodeList nodeList, int index) {
      Node container = nodeList.getParentNodeForChildren();
      CsmElement element = ConcreteSyntaxModel.forClass(container.getClass());
      LexicalDifferenceCalculator.CalculatedSyntaxModel original = this.calculatedSyntaxModelForNode(element, container);
      LexicalDifferenceCalculator.CalculatedSyntaxModel after = this.calculatedSyntaxModelAfterListRemoval(element, observableProperty, nodeList, index);
      return DifferenceElementCalculator.calculate(original, after);
   }

   List<DifferenceElement> calculateListAdditionDifference(ObservableProperty observableProperty, NodeList nodeList, int index, Node nodeAdded) {
      Node container = nodeList.getParentNodeForChildren();
      CsmElement element = ConcreteSyntaxModel.forClass(container.getClass());
      LexicalDifferenceCalculator.CalculatedSyntaxModel original = this.calculatedSyntaxModelForNode(element, container);
      LexicalDifferenceCalculator.CalculatedSyntaxModel after = this.calculatedSyntaxModelAfterListAddition(element, observableProperty, nodeList, index, nodeAdded);
      return DifferenceElementCalculator.calculate(original, after);
   }

   List<DifferenceElement> calculateListReplacementDifference(ObservableProperty observableProperty, NodeList nodeList, int index, Node newValue) {
      Node container = nodeList.getParentNodeForChildren();
      CsmElement element = ConcreteSyntaxModel.forClass(container.getClass());
      LexicalDifferenceCalculator.CalculatedSyntaxModel original = this.calculatedSyntaxModelForNode(element, container);
      LexicalDifferenceCalculator.CalculatedSyntaxModel after = this.calculatedSyntaxModelAfterListReplacement(element, observableProperty, nodeList, index, newValue);
      return DifferenceElementCalculator.calculate(original, after);
   }

   public void calculatePropertyChange(NodeText nodeText, Node observedNode, ObservableProperty property, Object oldValue, Object newValue) {
      if (nodeText == null) {
         throw new NullPointerException();
      } else {
         CsmElement element = ConcreteSyntaxModel.forClass(observedNode.getClass());
         LexicalDifferenceCalculator.CalculatedSyntaxModel original = this.calculatedSyntaxModelForNode(element, observedNode);
         LexicalDifferenceCalculator.CalculatedSyntaxModel after = this.calculatedSyntaxModelAfterPropertyChange(element, observedNode, property, oldValue, newValue);
         List<DifferenceElement> differenceElements = DifferenceElementCalculator.calculate(original, after);
         Difference difference = new Difference(differenceElements, nodeText, observedNode);
         difference.apply();
      }
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelForNode(CsmElement csm, Node node) {
      List<CsmElement> elements = new LinkedList();
      this.calculatedSyntaxModelForNode(csm, node, elements, new NoChange());
      return new LexicalDifferenceCalculator.CalculatedSyntaxModel(elements);
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelForNode(Node node) {
      return this.calculatedSyntaxModelForNode(ConcreteSyntaxModel.forClass(node.getClass()), node);
   }

   private void calculatedSyntaxModelForNode(CsmElement csm, Node node, List<CsmElement> elements, Change change) {
      if (csm instanceof CsmSequence) {
         CsmSequence csmSequence = (CsmSequence)csm;
         csmSequence.getElements().forEach((e) -> {
            this.calculatedSyntaxModelForNode(e, node, elements, change);
         });
      } else if (!(csm instanceof CsmComment)) {
         if (csm instanceof CsmSingleReference) {
            CsmSingleReference csmSingleReference = (CsmSingleReference)csm;
            Node child;
            if (change instanceof PropertyChange && ((PropertyChange)change).getProperty() == csmSingleReference.getProperty()) {
               child = (Node)((PropertyChange)change).getNewValue();
            } else {
               child = csmSingleReference.getProperty().getValueAsSingleReference(node);
            }

            if (child != null) {
               elements.add(new LexicalDifferenceCalculator.CsmChild(child));
            }
         } else if (!(csm instanceof CsmNone)) {
            if (csm instanceof CsmToken) {
               elements.add(csm);
            } else if (!(csm instanceof CsmOrphanCommentsEnding)) {
               Object value;
               if (csm instanceof CsmList) {
                  CsmList csmList = (CsmList)csm;
                  if (csmList.getProperty().isAboutNodes()) {
                     value = change.getValue(csmList.getProperty(), node);
                     NodeList nodeList;
                     if (value instanceof Optional) {
                        Optional optional = (Optional)value;
                        if (optional.isPresent()) {
                           if (!(optional.get() instanceof NodeList)) {
                              throw new IllegalStateException("Expected NodeList, found " + optional.get().getClass().getCanonicalName());
                           }

                           nodeList = (NodeList)optional.get();
                        } else {
                           nodeList = new NodeList();
                        }
                     } else {
                        if (!(value instanceof NodeList)) {
                           throw new IllegalStateException("Expected NodeList, found " + value.getClass().getCanonicalName());
                        }

                        nodeList = (NodeList)value;
                     }

                     if (!nodeList.isEmpty()) {
                        this.calculatedSyntaxModelForNode(csmList.getPreceeding(), node, elements, change);

                        for(int i = 0; i < nodeList.size(); ++i) {
                           if (i != 0) {
                              this.calculatedSyntaxModelForNode(csmList.getSeparatorPre(), node, elements, change);
                           }

                           elements.add(new LexicalDifferenceCalculator.CsmChild(nodeList.get(i)));
                           if (i != nodeList.size() - 1) {
                              this.calculatedSyntaxModelForNode(csmList.getSeparatorPost(), node, elements, change);
                           }
                        }

                        this.calculatedSyntaxModelForNode(csmList.getFollowing(), node, elements, change);
                     }
                  } else {
                     Collection collection = (Collection)change.getValue(csmList.getProperty(), node);
                     if (!collection.isEmpty()) {
                        this.calculatedSyntaxModelForNode(csmList.getPreceeding(), node, elements, change);
                        boolean first = true;

                        for(Iterator it = collection.iterator(); it.hasNext(); first = false) {
                           if (!first) {
                              this.calculatedSyntaxModelForNode(csmList.getSeparatorPre(), node, elements, change);
                           }

                           Object value = it.next();
                           if (!(value instanceof Modifier)) {
                              throw new UnsupportedOperationException(it.next().getClass().getSimpleName());
                           }

                           Modifier modifier = (Modifier)value;
                           elements.add(new CsmToken(this.toToken(modifier)));
                           if (it.hasNext()) {
                              this.calculatedSyntaxModelForNode(csmList.getSeparatorPost(), node, elements, change);
                           }
                        }

                        this.calculatedSyntaxModelForNode(csmList.getFollowing(), node, elements, change);
                     }
                  }
               } else if (csm instanceof CsmConditional) {
                  CsmConditional csmConditional = (CsmConditional)csm;
                  boolean satisfied = change.evaluate(csmConditional, node);
                  if (satisfied) {
                     this.calculatedSyntaxModelForNode(csmConditional.getThenElement(), node, elements, change);
                  } else {
                     this.calculatedSyntaxModelForNode(csmConditional.getElseElement(), node, elements, change);
                  }
               } else if (csm instanceof CsmIndent) {
                  elements.add(csm);
               } else if (csm instanceof CsmUnindent) {
                  elements.add(csm);
               } else if (csm instanceof CsmAttribute) {
                  CsmAttribute csmAttribute = (CsmAttribute)csm;
                  value = change.getValue(csmAttribute.getProperty(), node);
                  String text = value.toString();
                  if (value instanceof Printable) {
                     text = ((Printable)value).asString();
                  }

                  elements.add(new CsmToken(csmAttribute.getTokenType(node, value.toString(), text), text));
               } else if (csm instanceof CsmString && node instanceof StringLiteralExpr) {
                  elements.add(new CsmToken(88, "\"" + ((StringLiteralExpr)node).getValue() + "\""));
               } else {
                  if (!(csm instanceof CsmMix)) {
                     throw new UnsupportedOperationException(csm.getClass().getSimpleName() + " " + csm);
                  }

                  CsmMix csmMix = (CsmMix)csm;
                  List<CsmElement> mixElements = new LinkedList();
                  csmMix.getElements().forEach((e) -> {
                     this.calculatedSyntaxModelForNode(e, node, mixElements, change);
                  });
                  elements.add(new CsmMix(mixElements));
               }
            }
         }
      }

   }

   private int toToken(Modifier modifier) {
      switch(modifier) {
      case PUBLIC:
         return 47;
      case PRIVATE:
         return 45;
      case PROTECTED:
         return 46;
      case STATIC:
         return 50;
      case FINAL:
         return 29;
      case ABSTRACT:
         return 11;
      default:
         throw new UnsupportedOperationException(modifier.name());
      }
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterPropertyChange(Node node, ObservableProperty property, Object oldValue, Object newValue) {
      return this.calculatedSyntaxModelAfterPropertyChange(ConcreteSyntaxModel.forClass(node.getClass()), node, property, oldValue, newValue);
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterPropertyChange(CsmElement csm, Node node, ObservableProperty property, Object oldValue, Object newValue) {
      List<CsmElement> elements = new LinkedList();
      this.calculatedSyntaxModelForNode(csm, node, elements, new PropertyChange(property, oldValue, newValue));
      return new LexicalDifferenceCalculator.CalculatedSyntaxModel(elements);
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterListRemoval(CsmElement csm, ObservableProperty observableProperty, NodeList nodeList, int index) {
      List<CsmElement> elements = new LinkedList();
      Node container = nodeList.getParentNodeForChildren();
      this.calculatedSyntaxModelForNode(csm, container, elements, new ListRemovalChange(observableProperty, index));
      return new LexicalDifferenceCalculator.CalculatedSyntaxModel(elements);
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterListAddition(CsmElement csm, ObservableProperty observableProperty, NodeList nodeList, int index, Node nodeAdded) {
      List<CsmElement> elements = new LinkedList();
      Node container = nodeList.getParentNodeForChildren();
      this.calculatedSyntaxModelForNode(csm, container, elements, new ListAdditionChange(observableProperty, index, nodeAdded));
      return new LexicalDifferenceCalculator.CalculatedSyntaxModel(elements);
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterListAddition(Node container, ObservableProperty observableProperty, int index, Node nodeAdded) {
      CsmElement csm = ConcreteSyntaxModel.forClass(container.getClass());
      Object rawValue = observableProperty.getRawValue(container);
      if (!(rawValue instanceof NodeList)) {
         throw new IllegalStateException("Expected NodeList, found " + rawValue.getClass().getCanonicalName());
      } else {
         NodeList nodeList = (NodeList)rawValue;
         return this.calculatedSyntaxModelAfterListAddition(csm, observableProperty, nodeList, index, nodeAdded);
      }
   }

   LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterListRemoval(Node container, ObservableProperty observableProperty, int index) {
      CsmElement csm = ConcreteSyntaxModel.forClass(container.getClass());
      Object rawValue = observableProperty.getRawValue(container);
      if (!(rawValue instanceof NodeList)) {
         throw new IllegalStateException("Expected NodeList, found " + rawValue.getClass().getCanonicalName());
      } else {
         NodeList nodeList = (NodeList)rawValue;
         return this.calculatedSyntaxModelAfterListRemoval(csm, observableProperty, nodeList, index);
      }
   }

   private LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModelAfterListReplacement(CsmElement csm, ObservableProperty observableProperty, NodeList nodeList, int index, Node newValue) {
      List<CsmElement> elements = new LinkedList();
      Node container = nodeList.getParentNodeForChildren();
      this.calculatedSyntaxModelForNode(csm, container, elements, new ListReplacementChange(observableProperty, index, newValue));
      return new LexicalDifferenceCalculator.CalculatedSyntaxModel(elements);
   }

   static class CsmChild implements CsmElement {
      private final Node child;

      public Node getChild() {
         return this.child;
      }

      CsmChild(Node child) {
         this.child = child;
      }

      public void prettyPrint(Node node, SourcePrinter printer) {
         throw new UnsupportedOperationException();
      }

      public String toString() {
         return "child(" + this.child.getClass().getSimpleName() + ")";
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            LexicalDifferenceCalculator.CsmChild csmChild = (LexicalDifferenceCalculator.CsmChild)o;
            return this.child.equals(csmChild.child);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.child.hashCode();
      }
   }

   static class CalculatedSyntaxModel {
      final List<CsmElement> elements;

      CalculatedSyntaxModel(List<CsmElement> elements) {
         this.elements = elements;
      }

      public LexicalDifferenceCalculator.CalculatedSyntaxModel from(int index) {
         List<CsmElement> newList = new LinkedList();
         newList.addAll(this.elements.subList(index, this.elements.size()));
         return new LexicalDifferenceCalculator.CalculatedSyntaxModel(newList);
      }

      public String toString() {
         return "CalculatedSyntaxModel{elements=" + this.elements + '}';
      }

      LexicalDifferenceCalculator.CalculatedSyntaxModel sub(int start, int end) {
         return new LexicalDifferenceCalculator.CalculatedSyntaxModel(this.elements.subList(start, end));
      }

      void removeIndentationElements() {
         this.elements.removeIf((el) -> {
            return el instanceof CsmIndent || el instanceof CsmUnindent;
         });
      }
   }
}
