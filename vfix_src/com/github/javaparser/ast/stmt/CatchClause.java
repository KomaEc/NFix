package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.CatchClauseMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;

public final class CatchClause extends Node implements NodeWithBlockStmt<CatchClause> {
   private Parameter parameter;
   private BlockStmt body;

   public CatchClause() {
      this((TokenRange)null, new Parameter(), new BlockStmt());
   }

   public CatchClause(final EnumSet<Modifier> exceptModifier, final NodeList<AnnotationExpr> exceptAnnotations, final ClassOrInterfaceType exceptType, final SimpleName exceptName, final BlockStmt body) {
      this((TokenRange)null, new Parameter((TokenRange)null, exceptModifier, exceptAnnotations, exceptType, false, new NodeList(), exceptName), body);
   }

   @AllFieldsConstructor
   public CatchClause(final Parameter parameter, final BlockStmt body) {
      this((TokenRange)null, parameter, body);
   }

   public CatchClause(TokenRange tokenRange, Parameter parameter, BlockStmt body) {
      super(tokenRange);
      this.setParameter(parameter);
      this.setBody(body);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Parameter getParameter() {
      return this.parameter;
   }

   public CatchClause setParameter(final Parameter parameter) {
      Utils.assertNotNull(parameter);
      if (parameter == this.parameter) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.PARAMETER, this.parameter, parameter);
         if (this.parameter != null) {
            this.parameter.setParentNode((Node)null);
         }

         this.parameter = parameter;
         this.setAsParentNodeOf(parameter);
         return this;
      }
   }

   public BlockStmt getBody() {
      return this.body;
   }

   public CatchClause setBody(final BlockStmt body) {
      Utils.assertNotNull(body);
      if (body == this.body) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.BODY, this.body, body);
         if (this.body != null) {
            this.body.setParentNode((Node)null);
         }

         this.body = body;
         this.setAsParentNodeOf(body);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public CatchClause clone() {
      return (CatchClause)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public CatchClauseMetaModel getMetaModel() {
      return JavaParserMetaModel.catchClauseMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((BlockStmt)replacementNode);
         return true;
      } else if (node == this.parameter) {
         this.setParameter((Parameter)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }
}
