package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.DerivedProperty;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LambdaExprMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class LambdaExpr extends Expression implements NodeWithParameters<LambdaExpr> {
   private NodeList<Parameter> parameters;
   private boolean isEnclosingParameters;
   private Statement body;

   public LambdaExpr() {
      this((TokenRange)null, new NodeList(), new ReturnStmt(), false);
   }

   @AllFieldsConstructor
   public LambdaExpr(NodeList<Parameter> parameters, Statement body, boolean isEnclosingParameters) {
      this((TokenRange)null, parameters, body, isEnclosingParameters);
   }

   public LambdaExpr(TokenRange tokenRange, NodeList<Parameter> parameters, Statement body, boolean isEnclosingParameters) {
      super(tokenRange);
      this.setParameters(parameters);
      this.setBody(body);
      this.setEnclosingParameters(isEnclosingParameters);
      this.customInitialization();
   }

   public NodeList<Parameter> getParameters() {
      return this.parameters;
   }

   public LambdaExpr setParameters(final NodeList<Parameter> parameters) {
      Utils.assertNotNull(parameters);
      if (parameters == this.parameters) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.PARAMETERS, this.parameters, parameters);
         if (this.parameters != null) {
            this.parameters.setParentNode((Node)null);
         }

         this.parameters = parameters;
         this.setAsParentNodeOf(parameters);
         return this;
      }
   }

   public Statement getBody() {
      return this.body;
   }

   public LambdaExpr setBody(final Statement body) {
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

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean isEnclosingParameters() {
      return this.isEnclosingParameters;
   }

   public LambdaExpr setEnclosingParameters(final boolean isEnclosingParameters) {
      if (isEnclosingParameters == this.isEnclosingParameters) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ENCLOSING_PARAMETERS, this.isEnclosingParameters, isEnclosingParameters);
         this.isEnclosingParameters = isEnclosingParameters;
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.parameters.size(); ++i) {
            if (this.parameters.get(i) == node) {
               this.parameters.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   @DerivedProperty
   public Optional<Expression> getExpressionBody() {
      return this.body.isExpressionStmt() ? Optional.of(this.body.asExpressionStmt().getExpression()) : Optional.empty();
   }

   public LambdaExpr clone() {
      return (LambdaExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public LambdaExprMetaModel getMetaModel() {
      return JavaParserMetaModel.lambdaExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((Statement)replacementNode);
         return true;
      } else {
         for(int i = 0; i < this.parameters.size(); ++i) {
            if (this.parameters.get(i) == node) {
               this.parameters.set(i, (Node)((Parameter)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isLambdaExpr() {
      return true;
   }

   public LambdaExpr asLambdaExpr() {
      return this;
   }

   public void ifLambdaExpr(Consumer<LambdaExpr> action) {
      action.accept(this);
   }

   public Optional<LambdaExpr> toLambdaExpr() {
      return Optional.of(this);
   }
}
