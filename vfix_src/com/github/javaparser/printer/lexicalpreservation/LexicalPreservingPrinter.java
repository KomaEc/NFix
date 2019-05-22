package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.JavaParser;
import com.github.javaparser.JavaToken;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Provider;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.TokenTypes;
import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.observer.AstObserver;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.observer.PropagatingAstObserver;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.TreeVisitor;
import com.github.javaparser.printer.ConcreteSyntaxModel;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmIndent;
import com.github.javaparser.printer.concretesyntaxmodel.CsmMix;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import com.github.javaparser.printer.concretesyntaxmodel.CsmUnindent;
import com.github.javaparser.utils.Pair;
import com.github.javaparser.utils.Utils;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LexicalPreservingPrinter {
   public static final DataKey<NodeText> NODE_TEXT_DATA = new DataKey<NodeText>() {
   };
   private static final LexicalDifferenceCalculator LEXICAL_DIFFERENCE_CALCULATOR = new LexicalDifferenceCalculator();

   /** @deprecated */
   public static <N extends Node> Pair<ParseResult<N>, LexicalPreservingPrinter> setup(ParseStart<N> parseStart, Provider provider) {
      ParseResult<N> parseResult = (new JavaParser()).parse(parseStart, provider);
      if (!parseResult.isSuccessful()) {
         throw new RuntimeException("Parsing failed, unable to setup the lexical preservation printer: " + parseResult.getProblems());
      } else {
         LexicalPreservingPrinter lexicalPreservingPrinter = new LexicalPreservingPrinter((Node)parseResult.getResult().get());
         return new Pair(parseResult, lexicalPreservingPrinter);
      }
   }

   public static <N extends Node> N setup(N node) {
      Utils.assertNotNull(node);
      node.getTokenRange().ifPresent((r) -> {
         storeInitialText(node);
         AstObserver observer = createObserver();
         node.registerForSubtree(observer);
      });
      return node;
   }

   /** @deprecated */
   @Deprecated
   public LexicalPreservingPrinter(Node node) {
      setup(node);
   }

   private static AstObserver createObserver() {
      return new LexicalPreservingPrinter.Observer();
   }

   private static void storeInitialText(Node root) {
      final Map<Node, List<JavaToken>> tokensByNode = new IdentityHashMap();

      JavaToken token;
      Node owner;
      for(Iterator var2 = ((TokenRange)root.getTokenRange().get()).iterator(); var2.hasNext(); ((List)tokensByNode.get(owner)).add(token)) {
         token = (JavaToken)var2.next();
         Range tokenRange = (Range)token.getRange().orElseThrow(() -> {
            return new RuntimeException("Token without range: " + token);
         });
         owner = findNodeForToken(root, tokenRange);
         if (owner == null) {
            throw new RuntimeException("Token without node owning it: " + token);
         }

         if (!tokensByNode.containsKey(owner)) {
            tokensByNode.put(owner, new LinkedList());
         }
      }

      (new TreeVisitor() {
         public void process(Node node) {
            if (!PhantomNodeLogic.isPhantomNode(node)) {
               LexicalPreservingPrinter.storeInitialTextForOneNode(node, (List)tokensByNode.get(node));
            }

         }
      }).visitBreadthFirst(root);
   }

   private static Node findNodeForToken(Node node, Range tokenRange) {
      if (PhantomNodeLogic.isPhantomNode(node)) {
         return null;
      } else if (((Range)node.getRange().get()).contains(tokenRange)) {
         Iterator var2 = node.getChildNodes().iterator();

         Node found;
         do {
            if (!var2.hasNext()) {
               return node;
            }

            Node child = (Node)var2.next();
            found = findNodeForToken(child, tokenRange);
         } while(found == null);

         return found;
      } else {
         return null;
      }
   }

   private static void storeInitialTextForOneNode(Node node, List<JavaToken> nodeTokens) {
      if (nodeTokens == null) {
         nodeTokens = Collections.emptyList();
      }

      List<Pair<Range, TextElement>> elements = new LinkedList();
      Iterator var3 = node.getChildNodes().iterator();

      while(var3.hasNext()) {
         Node child = (Node)var3.next();
         if (!PhantomNodeLogic.isPhantomNode(child)) {
            if (!child.getRange().isPresent()) {
               throw new RuntimeException("Range not present on node " + child);
            }

            elements.add(new Pair(child.getRange().get(), new ChildTextElement(child)));
         }
      }

      var3 = nodeTokens.iterator();

      while(var3.hasNext()) {
         JavaToken token = (JavaToken)var3.next();
         elements.add(new Pair(token.getRange().get(), new TokenTextElement(token)));
      }

      elements.sort(Comparator.comparing((e) -> {
         return ((Range)e.a).begin;
      }));
      node.setData(NODE_TEXT_DATA, new NodeText((List)elements.stream().map((p) -> {
         return (TextElement)p.b;
      }).collect(Collectors.toList())));
   }

   private static Iterator<TokenTextElement> tokensPreceeding(final Node node) {
      if (!node.getParentNode().isPresent()) {
         return new TextElementIteratorsFactory.EmptyIterator();
      } else {
         NodeText parentNodeText = getOrCreateNodeText((Node)node.getParentNode().get());
         int index = parentNodeText.tryToFindChild(node);
         if (index == -1) {
            if (node.getParentNode().get() instanceof VariableDeclarator) {
               return tokensPreceeding((Node)node.getParentNode().get());
            } else {
               throw new IllegalArgumentException(String.format("I could not find child '%s' in parent '%s'. parentNodeText: %s", node, node.getParentNode().get(), parentNodeText));
            }
         } else {
            return new TextElementIteratorsFactory.CascadingIterator(TextElementIteratorsFactory.partialReverseIterator(parentNodeText, index - 1), () -> {
               return tokensPreceeding((Node)node.getParentNode().get());
            });
         }
      }
   }

   public static String print(Node node) {
      StringWriter writer = new StringWriter();

      try {
         print(node, writer);
      } catch (IOException var3) {
         throw new RuntimeException("Unexpected IOException on a StringWriter", var3);
      }

      return writer.toString();
   }

   public static void print(Node node, Writer writer) throws IOException {
      if (!node.containsData(NODE_TEXT_DATA)) {
         getOrCreateNodeText(node);
      }

      NodeText text = (NodeText)node.getData(NODE_TEXT_DATA);
      writer.append(text.expand());
   }

   private static void prettyPrintingTextNode(Node node, NodeText nodeText) {
      if (node instanceof PrimitiveType) {
         PrimitiveType primitiveType = (PrimitiveType)node;
         switch(primitiveType.getType()) {
         case BOOLEAN:
            nodeText.addToken(13, node.toString());
            break;
         case CHAR:
            nodeText.addToken(18, node.toString());
            break;
         case BYTE:
            nodeText.addToken(15, node.toString());
            break;
         case SHORT:
            nodeText.addToken(49, node.toString());
            break;
         case INT:
            nodeText.addToken(38, node.toString());
            break;
         case LONG:
            nodeText.addToken(40, node.toString());
            break;
         case FLOAT:
            nodeText.addToken(31, node.toString());
            break;
         case DOUBLE:
            nodeText.addToken(24, node.toString());
            break;
         default:
            throw new IllegalArgumentException();
         }

      } else if (node instanceof JavadocComment) {
         nodeText.addToken(8, "/**" + ((JavadocComment)node).getContent() + "*/");
      } else if (node instanceof BlockComment) {
         nodeText.addToken(9, "/*" + ((BlockComment)node).getContent() + "*/");
      } else if (node instanceof LineComment) {
         nodeText.addToken(5, "//" + ((LineComment)node).getContent());
      } else {
         interpret(node, ConcreteSyntaxModel.forClass(node.getClass()), nodeText);
      }
   }

   private static NodeText interpret(Node node, CsmElement csm, NodeText nodeText) {
      LexicalDifferenceCalculator.CalculatedSyntaxModel calculatedSyntaxModel = (new LexicalDifferenceCalculator()).calculatedSyntaxModelForNode(csm, node);
      List<TokenTextElement> indentation = findIndentation(node);
      boolean pendingIndentation = false;
      Iterator var6 = calculatedSyntaxModel.elements.iterator();

      while(true) {
         while(var6.hasNext()) {
            CsmElement element = (CsmElement)var6.next();
            if (pendingIndentation && (!(element instanceof CsmToken) || !((CsmToken)element).isNewLine())) {
               indentation.forEach(nodeText::addElement);
            }

            pendingIndentation = false;
            if (element instanceof LexicalDifferenceCalculator.CsmChild) {
               nodeText.addChild(((LexicalDifferenceCalculator.CsmChild)element).getChild());
            } else if (element instanceof CsmToken) {
               CsmToken csmToken = (CsmToken)element;
               nodeText.addToken(csmToken.getTokenType(), csmToken.getContent(node));
               if (csmToken.isNewLine()) {
                  pendingIndentation = true;
               }
            } else if (element instanceof CsmMix) {
               CsmMix csmMix = (CsmMix)element;
               csmMix.getElements().forEach((e) -> {
                  interpret(node, e, nodeText);
               });
            } else {
               int i;
               if (element instanceof CsmIndent) {
                  for(i = 0; i < 4; ++i) {
                     nodeText.addToken(1, " ");
                  }
               } else {
                  if (!(element instanceof CsmUnindent)) {
                     throw new UnsupportedOperationException(element.getClass().getSimpleName());
                  }

                  for(i = 0; i < 4; ++i) {
                     if (nodeText.endWithSpace()) {
                        nodeText.removeLastElement();
                     }
                  }
               }
            }
         }

         if (node instanceof VariableDeclarator) {
            VariableDeclarator variableDeclarator = (VariableDeclarator)node;
            variableDeclarator.getParentNode().ifPresent((parent) -> {
               ((NodeWithVariables)parent).getMaximumCommonType().ifPresent((mct) -> {
                  int extraArrayLevels = variableDeclarator.getType().getArrayLevel() - mct.getArrayLevel();

                  for(int i = 0; i < extraArrayLevels; ++i) {
                     nodeText.addElement(new TokenTextElement(96));
                     nodeText.addElement(new TokenTextElement(97));
                  }

               });
            });
         }

         return nodeText;
      }
   }

   static NodeText getOrCreateNodeText(Node node) {
      if (!node.containsData(NODE_TEXT_DATA)) {
         NodeText nodeText = new NodeText();
         node.setData(NODE_TEXT_DATA, nodeText);
         prettyPrintingTextNode(node, nodeText);
      }

      return (NodeText)node.getData(NODE_TEXT_DATA);
   }

   static List<TokenTextElement> findIndentation(Node node) {
      List<TokenTextElement> followingNewlines = new LinkedList();
      Iterator it = tokensPreceeding(node);

      while(it.hasNext()) {
         TokenTextElement tte = (TokenTextElement)it.next();
         if (tte.getTokenKind() == 5 || tte.isNewline()) {
            break;
         }

         followingNewlines.add(tte);
      }

      Collections.reverse(followingNewlines);

      for(int i = 0; i < followingNewlines.size(); ++i) {
         if (!((TokenTextElement)followingNewlines.get(i)).isSpaceOrTab()) {
            return followingNewlines.subList(0, i);
         }
      }

      return followingNewlines;
   }

   private static boolean isReturningOptionalNodeList(Method m) {
      if (!m.getReturnType().getCanonicalName().equals(Optional.class.getCanonicalName())) {
         return false;
      } else if (!(m.getGenericReturnType() instanceof ParameterizedType)) {
         return false;
      } else {
         ParameterizedType parameterizedType = (ParameterizedType)m.getGenericReturnType();
         Type optionalArgument = parameterizedType.getActualTypeArguments()[0];
         return optionalArgument.getTypeName().startsWith(NodeList.class.getCanonicalName());
      }
   }

   private static ObservableProperty findNodeListName(NodeList nodeList) {
      Node parent = nodeList.getParentNodeForChildren();
      Method[] var2 = parent.getClass().getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method m = var2[var4];
         if (m.getParameterCount() == 0 && m.getReturnType().getCanonicalName().equals(NodeList.class.getCanonicalName())) {
            try {
               Object raw = m.invoke(parent);
               if (!(raw instanceof NodeList)) {
                  throw new IllegalStateException("Expected NodeList, found " + raw.getClass().getCanonicalName());
               }

               NodeList result = (NodeList)raw;
               if (result == nodeList) {
                  String name = m.getName();
                  if (name.startsWith("get")) {
                     name = name.substring("get".length());
                  }

                  return ObservableProperty.fromCamelCaseName(Utils.decapitalize(name));
               }
            } catch (InvocationTargetException | IllegalAccessException var9) {
               throw new RuntimeException(var9);
            }
         } else if (m.getParameterCount() == 0 && isReturningOptionalNodeList(m)) {
            try {
               Optional<NodeList<?>> raw = (Optional)m.invoke(parent);
               if (raw.isPresent() && raw.get() == nodeList) {
                  String name = m.getName();
                  if (name.startsWith("get")) {
                     name = name.substring("get".length());
                  }

                  return ObservableProperty.fromCamelCaseName(Utils.decapitalize(name));
               }
            } catch (InvocationTargetException | IllegalAccessException var10) {
               throw new RuntimeException(var10);
            }
         }
      }

      throw new IllegalArgumentException("Cannot find list name of NodeList of size " + nodeList.size());
   }

   private static class Observer extends PropagatingAstObserver {
      private Observer() {
      }

      public void concretePropertyChange(Node observedNode, ObservableProperty property, Object oldValue, Object newValue) {
         if ((oldValue == null || !oldValue.equals(newValue)) && (oldValue != null || newValue != null)) {
            if (property != ObservableProperty.RANGE && property != ObservableProperty.COMMENTED_NODE) {
               NodeText nodeText;
               if (property == ObservableProperty.COMMENT) {
                  if (!observedNode.getParentNode().isPresent()) {
                     throw new IllegalStateException();
                  }

                  nodeText = LexicalPreservingPrinter.getOrCreateNodeText((Node)observedNode.getParentNode().get());
                  if (oldValue == null) {
                     int index = nodeText.findChild(observedNode);
                     nodeText.addChild(index, (Comment)newValue);
                     nodeText.addToken(index + 1, TokenTypes.eolTokenKind(), Utils.EOL);
                  } else {
                     List matchingTokens;
                     if (newValue == null) {
                        if (!(oldValue instanceof JavadocComment)) {
                           throw new UnsupportedOperationException();
                        }

                        matchingTokens = this.getMatchingTokenTextElements((JavadocComment)oldValue, nodeText);
                        TokenTextElement matchingElement = (TokenTextElement)matchingTokens.get(0);
                        int index = nodeText.findElement(matchingElement.and(matchingElement.matchByRange()));
                        nodeText.removeElement(index);
                        if (((TextElement)nodeText.getElements().get(index)).isNewline()) {
                           nodeText.removeElement(index);
                        }
                     } else {
                        if (!(oldValue instanceof JavadocComment)) {
                           throw new UnsupportedOperationException();
                        }

                        matchingTokens = this.getMatchingTokenTextElements((JavadocComment)oldValue, nodeText);
                        JavadocComment newJavadocComment = (JavadocComment)newValue;
                        TokenTextElement matchingElement = (TokenTextElement)matchingTokens.get(0);
                        nodeText.replace(matchingElement.and(matchingElement.matchByRange()), new TokenTextElement(8, "/**" + newJavadocComment.getContent() + "*/"));
                     }
                  }
               }

               nodeText = LexicalPreservingPrinter.getOrCreateNodeText(observedNode);
               if (nodeText == null) {
                  throw new NullPointerException(observedNode.getClass().getSimpleName());
               } else {
                  LexicalPreservingPrinter.LEXICAL_DIFFERENCE_CALCULATOR.calculatePropertyChange(nodeText, observedNode, property, oldValue, newValue);
               }
            }
         }
      }

      private List<TokenTextElement> getMatchingTokenTextElements(JavadocComment oldValue, NodeText nodeText) {
         List<TokenTextElement> matchingTokens = (List)nodeText.getElements().stream().filter((e) -> {
            return e.isToken(8);
         }).map((e) -> {
            return (TokenTextElement)e;
         }).filter((t) -> {
            return t.getText().equals("/**" + oldValue.getContent() + "*/");
         }).collect(Collectors.toList());
         if (matchingTokens.size() > 1) {
            matchingTokens = (List)matchingTokens.stream().filter((t) -> {
               return this.isEqualRange(t.getToken().getRange(), oldValue.getRange());
            }).collect(Collectors.toList());
         }

         if (matchingTokens.size() != 1) {
            throw new IllegalStateException("The matching JavadocComment to be removed / replaced could not be found");
         } else {
            return matchingTokens;
         }
      }

      private boolean isEqualRange(Optional<Range> range1, Optional<Range> range2) {
         return range1.isPresent() && range2.isPresent() ? ((Range)range1.get()).equals(range2.get()) : false;
      }

      public void concreteListChange(NodeList changedList, AstObserver.ListChangeType type, int index, Node nodeAddedOrRemoved) {
         NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(changedList.getParentNodeForChildren());
         List differenceElements;
         if (type == AstObserver.ListChangeType.REMOVAL) {
            differenceElements = LexicalPreservingPrinter.LEXICAL_DIFFERENCE_CALCULATOR.calculateListRemovalDifference(LexicalPreservingPrinter.findNodeListName(changedList), changedList, index);
         } else {
            if (type != AstObserver.ListChangeType.ADDITION) {
               throw new UnsupportedOperationException();
            }

            differenceElements = LexicalPreservingPrinter.LEXICAL_DIFFERENCE_CALCULATOR.calculateListAdditionDifference(LexicalPreservingPrinter.findNodeListName(changedList), changedList, index, nodeAddedOrRemoved);
         }

         Difference difference = new Difference(differenceElements, nodeText, changedList.getParentNodeForChildren());
         difference.apply();
      }

      public void concreteListReplacement(NodeList changedList, int index, Node oldValue, Node newValue) {
         NodeText nodeText = LexicalPreservingPrinter.getOrCreateNodeText(changedList.getParentNodeForChildren());
         List<DifferenceElement> differenceElements = LexicalPreservingPrinter.LEXICAL_DIFFERENCE_CALCULATOR.calculateListReplacementDifference(LexicalPreservingPrinter.findNodeListName(changedList), changedList, index, newValue);
         Difference difference = new Difference(differenceElements, nodeText, changedList.getParentNodeForChildren());
         difference.apply();
      }

      // $FF: synthetic method
      Observer(Object x0) {
         this();
      }
   }
}
