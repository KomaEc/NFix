package com.github.javaparser;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.utils.Pair;
import com.github.javaparser.utils.Utils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

abstract class GeneratedJavaParserBase {
   List<Problem> problems = new ArrayList();
   boolean storeTokens;

   abstract GeneratedJavaParserTokenManager getTokenSource();

   abstract void ReInit(Provider provider);

   abstract JavaToken token();

   abstract Token getNextToken();

   void reset(Provider provider) {
      this.ReInit(provider);
      this.problems = new ArrayList();
      this.getTokenSource().reset();
   }

   public List<JavaToken> getTokens() {
      return this.getTokenSource().getTokens();
   }

   CommentsCollection getCommentsCollection() {
      return this.getTokenSource().getCommentsCollection();
   }

   void addProblem(String message) {
      this.problems.add(new Problem(message, this.tokenRange(), (Throwable)null));
   }

   TokenRange tokenRange() {
      return this.storeTokens ? new TokenRange(this.token(), this.token()) : null;
   }

   TokenRange range(JavaToken begin, JavaToken end) {
      return this.storeTokens ? new TokenRange(begin, end) : null;
   }

   TokenRange range(Node begin, JavaToken end) {
      return this.storeTokens ? new TokenRange(((TokenRange)begin.getTokenRange().get()).getBegin(), end) : null;
   }

   TokenRange range(JavaToken begin, Node end) {
      return this.storeTokens ? new TokenRange(begin, ((TokenRange)end.getTokenRange().get()).getEnd()) : null;
   }

   TokenRange range(Node begin, Node end) {
      return this.storeTokens ? new TokenRange(((TokenRange)begin.getTokenRange().get()).getBegin(), ((TokenRange)end.getTokenRange().get()).getEnd()) : null;
   }

   JavaToken orIfInvalid(JavaToken firstChoice, JavaToken secondChoice) {
      if (this.storeTokens) {
         Utils.assertNotNull(firstChoice);
         Utils.assertNotNull(secondChoice);
         return !firstChoice.valid() && !secondChoice.invalid() ? secondChoice : firstChoice;
      } else {
         return null;
      }
   }

   JavaToken orIfInvalid(JavaToken firstChoice, Node secondChoice) {
      return this.storeTokens ? this.orIfInvalid(firstChoice, ((TokenRange)secondChoice.getTokenRange().get()).getBegin()) : null;
   }

   JavaToken nodeListBegin(NodeList<?> l) {
      return this.storeTokens && !l.isEmpty() ? ((TokenRange)l.get(0).getTokenRange().get()).getBegin() : JavaToken.INVALID;
   }

   void setTokenKind(int newKind) {
      this.token().setKind(newKind);
   }

   void setStoreTokens(boolean storeTokens) {
      this.storeTokens = storeTokens;
      this.getTokenSource().setStoreTokens(storeTokens);
   }

   TokenRange recover(int recoveryTokenType, ParseException p) {
      JavaToken begin = null;
      if (p.currentToken != null) {
         begin = this.token();
      }

      Token t;
      do {
         t = this.getNextToken();
      } while(t.kind != recoveryTokenType && t.kind != 0);

      JavaToken end = this.token();
      TokenRange tokenRange = null;
      if (begin != null && end != null) {
         tokenRange = this.range(begin, end);
      }

      this.problems.add(new Problem(this.makeMessageForParseException(p), tokenRange, p));
      return tokenRange;
   }

   <T extends Node> NodeList<T> emptyList() {
      return new NodeList();
   }

   <T extends Node> NodeList<T> add(NodeList<T> list, T obj) {
      if (list == null) {
         list = new NodeList();
      }

      list.add(obj);
      return list;
   }

   <T extends Node> NodeList<T> addWhenNotNull(NodeList<T> list, T obj) {
      return obj == null ? list : this.add(list, obj);
   }

   <T extends Node> NodeList<T> prepend(NodeList<T> list, T obj) {
      if (list == null) {
         list = new NodeList();
      }

      list.addFirst(obj);
      return list;
   }

   <T> List<T> add(List<T> list, T obj) {
      if (list == null) {
         list = new LinkedList();
      }

      ((List)list).add(obj);
      return (List)list;
   }

   void addModifier(EnumSet<Modifier> modifiers, Modifier mod) {
      if (modifiers.contains(mod)) {
         this.addProblem("Duplicated modifier");
      }

      modifiers.add(mod);
   }

   private void propagateRangeGrowthOnRight(Node node, Node endNode) {
      if (this.storeTokens) {
         node.getParentNode().ifPresent((nodeParent) -> {
            boolean isChildOnTheRightBorderOfParent = ((TokenRange)node.getTokenRange().get()).getEnd().equals(((TokenRange)nodeParent.getTokenRange().get()).getEnd());
            if (isChildOnTheRightBorderOfParent) {
               this.propagateRangeGrowthOnRight(nodeParent, endNode);
            }

         });
         node.setTokenRange(this.range(node, endNode));
      }

   }

   Expression generateLambda(Expression ret, Statement lambdaBody) {
      if (ret instanceof EnclosedExpr) {
         Expression inner = ((EnclosedExpr)ret).getInner();
         SimpleName id = ((NameExpr)inner).getName();
         NodeList<Parameter> params = this.add((NodeList)(new NodeList()), (Node)(new Parameter((TokenRange)((Expression)ret).getTokenRange().orElse((Object)null), EnumSet.noneOf(Modifier.class), new NodeList(), new UnknownType(), false, new NodeList(), id)));
         ret = new LambdaExpr(this.range((Node)ret, (Node)lambdaBody), params, lambdaBody, true);
      } else if (ret instanceof NameExpr) {
         SimpleName id = ((NameExpr)ret).getName();
         NodeList<Parameter> params = this.add((NodeList)(new NodeList()), (Node)(new Parameter((TokenRange)((Expression)ret).getTokenRange().orElse((Object)null), EnumSet.noneOf(Modifier.class), new NodeList(), new UnknownType(), false, new NodeList(), id)));
         ret = new LambdaExpr(this.range((Node)ret, (Node)lambdaBody), params, lambdaBody, false);
      } else if (ret instanceof LambdaExpr) {
         ((LambdaExpr)ret).setBody(lambdaBody);
         this.propagateRangeGrowthOnRight((Node)ret, lambdaBody);
      } else if (ret instanceof CastExpr) {
         CastExpr castExpr = (CastExpr)ret;
         Expression inner = this.generateLambda(castExpr.getExpression(), lambdaBody);
         castExpr.setExpression(inner);
      } else {
         this.addProblem("Failed to parse lambda expression! Please create an issue at https://github.com/javaparser/javaparser/issues");
      }

      return (Expression)ret;
   }

   ArrayCreationExpr juggleArrayCreation(TokenRange range, List<TokenRange> levelRanges, Type type, NodeList<Expression> dimensions, List<NodeList<AnnotationExpr>> arrayAnnotations, ArrayInitializerExpr arrayInitializerExpr) {
      NodeList<ArrayCreationLevel> levels = new NodeList();

      for(int i = 0; i < arrayAnnotations.size(); ++i) {
         levels.add((Node)(new ArrayCreationLevel((TokenRange)levelRanges.get(i), (Expression)dimensions.get(i), (NodeList)arrayAnnotations.get(i))));
      }

      return new ArrayCreationExpr(range, type, levels, arrayInitializerExpr);
   }

   Type juggleArrayType(Type partialType, List<ArrayType.ArrayBracketPair> additionalBrackets) {
      Pair<Type, List<ArrayType.ArrayBracketPair>> partialParts = ArrayType.unwrapArrayTypes(partialType);
      Type elementType = (Type)partialParts.a;
      List<ArrayType.ArrayBracketPair> leftMostBrackets = (List)partialParts.b;
      return ArrayType.wrapInArrayTypes(elementType, leftMostBrackets, additionalBrackets).clone();
   }

   private String makeMessageForParseException(ParseException exception) {
      StringBuilder sb = new StringBuilder("Parse error. Found ");
      StringBuilder expected = new StringBuilder();
      int maxExpectedTokenSequenceLength = 0;
      TreeSet<String> sortedOptions = new TreeSet();

      int numExpectedTokens;
      for(int i = 0; i < exception.expectedTokenSequences.length; ++i) {
         if (maxExpectedTokenSequenceLength < exception.expectedTokenSequences[i].length) {
            maxExpectedTokenSequenceLength = exception.expectedTokenSequences[i].length;
         }

         for(numExpectedTokens = 0; numExpectedTokens < exception.expectedTokenSequences[i].length; ++numExpectedTokens) {
            sortedOptions.add(exception.tokenImage[exception.expectedTokenSequences[i][numExpectedTokens]]);
         }
      }

      Iterator var11 = sortedOptions.iterator();

      while(var11.hasNext()) {
         String option = (String)var11.next();
         expected.append(" ").append(option);
      }

      sb.append("");
      Token token = exception.currentToken.next;

      for(numExpectedTokens = 0; numExpectedTokens < maxExpectedTokenSequenceLength; ++numExpectedTokens) {
         String tokenText = token.image;
         String escapedTokenText = ParseException.add_escapes(tokenText);
         if (numExpectedTokens != 0) {
            sb.append(" ");
         }

         if (token.kind == 0) {
            sb.append(exception.tokenImage[0]);
            break;
         }

         escapedTokenText = "\"" + escapedTokenText + "\"";
         String image = exception.tokenImage[token.kind];
         if (image.equals(escapedTokenText)) {
            sb.append(image);
         } else {
            sb.append(" ").append(escapedTokenText).append(" ").append(image);
         }

         token = token.next;
      }

      if (exception.expectedTokenSequences.length != 0) {
         numExpectedTokens = exception.expectedTokenSequences.length;
         sb.append(", expected").append(numExpectedTokens == 1 ? "" : " one of ").append(expected.toString());
      }

      return sb.toString();
   }
}
