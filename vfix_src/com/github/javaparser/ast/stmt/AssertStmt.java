package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.AssertStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class AssertStmt extends Statement {
   private Expression check;
   @OptionalProperty
   private Expression message;

   public AssertStmt() {
      this((TokenRange)null, new BooleanLiteralExpr(), (Expression)null);
   }

   public AssertStmt(final Expression check) {
      this((TokenRange)null, check, (Expression)null);
   }

   @AllFieldsConstructor
   public AssertStmt(final Expression check, final Expression message) {
      this((TokenRange)null, check, message);
   }

   public AssertStmt(TokenRange tokenRange, Expression check, Expression message) {
      super(tokenRange);
      this.setCheck(check);
      this.setMessage(message);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getCheck() {
      return this.check;
   }

   public Optional<Expression> getMessage() {
      return Optional.ofNullable(this.message);
   }

   public AssertStmt setCheck(final Expression check) {
      Utils.assertNotNull(check);
      if (check == this.check) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CHECK, this.check, check);
         if (this.check != null) {
            this.check.setParentNode((Node)null);
         }

         this.check = check;
         this.setAsParentNodeOf(check);
         return this;
      }
   }

   public AssertStmt setMessage(final Expression message) {
      if (message == this.message) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MESSAGE, this.message, message);
         if (this.message != null) {
            this.message.setParentNode((Node)null);
         }

         this.message = message;
         this.setAsParentNodeOf(message);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.message != null && node == this.message) {
         this.removeMessage();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public AssertStmt removeMessage() {
      return this.setMessage((Expression)null);
   }

   public AssertStmt clone() {
      return (AssertStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public AssertStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.assertStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.check) {
         this.setCheck((Expression)replacementNode);
         return true;
      } else if (this.message != null && node == this.message) {
         this.setMessage((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isAssertStmt() {
      return true;
   }

   public AssertStmt asAssertStmt() {
      return this;
   }

   public void ifAssertStmt(Consumer<AssertStmt> action) {
      action.accept(this);
   }

   public Optional<AssertStmt> toAssertStmt() {
      return Optional.of(this);
   }
}
