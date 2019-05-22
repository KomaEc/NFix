package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.StatementMetaModel;
import com.github.javaparser.utils.CodeGenerationUtils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Statement extends Node {
   @AllFieldsConstructor
   public Statement() {
      this((TokenRange)null);
   }

   public Statement(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public Statement clone() {
      return (Statement)this.accept(new CloneVisitor(), (Object)null);
   }

   public StatementMetaModel getMetaModel() {
      return JavaParserMetaModel.statementMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isAssertStmt() {
      return false;
   }

   public AssertStmt asAssertStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an AssertStmt", this));
   }

   public boolean isBlockStmt() {
      return false;
   }

   public BlockStmt asBlockStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an BlockStmt", this));
   }

   public boolean isBreakStmt() {
      return false;
   }

   public BreakStmt asBreakStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an BreakStmt", this));
   }

   public boolean isContinueStmt() {
      return false;
   }

   public ContinueStmt asContinueStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ContinueStmt", this));
   }

   public boolean isDoStmt() {
      return false;
   }

   public DoStmt asDoStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an DoStmt", this));
   }

   public boolean isEmptyStmt() {
      return false;
   }

   public EmptyStmt asEmptyStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an EmptyStmt", this));
   }

   public boolean isExplicitConstructorInvocationStmt() {
      return false;
   }

   public ExplicitConstructorInvocationStmt asExplicitConstructorInvocationStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ExplicitConstructorInvocationStmt", this));
   }

   public boolean isExpressionStmt() {
      return false;
   }

   public ExpressionStmt asExpressionStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ExpressionStmt", this));
   }

   public boolean isForStmt() {
      return false;
   }

   public ForStmt asForStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ForStmt", this));
   }

   public boolean isForeachStmt() {
      return false;
   }

   public ForeachStmt asForeachStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ForeachStmt", this));
   }

   public boolean isIfStmt() {
      return false;
   }

   public IfStmt asIfStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an IfStmt", this));
   }

   public boolean isLabeledStmt() {
      return false;
   }

   public LabeledStmt asLabeledStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LabeledStmt", this));
   }

   public boolean isLocalClassDeclarationStmt() {
      return false;
   }

   public LocalClassDeclarationStmt asLocalClassDeclarationStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LocalClassDeclarationStmt", this));
   }

   public boolean isReturnStmt() {
      return false;
   }

   public ReturnStmt asReturnStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ReturnStmt", this));
   }

   public boolean isSwitchEntryStmt() {
      return false;
   }

   public SwitchEntryStmt asSwitchEntryStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an SwitchEntryStmt", this));
   }

   public boolean isSwitchStmt() {
      return false;
   }

   public SwitchStmt asSwitchStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an SwitchStmt", this));
   }

   public boolean isSynchronizedStmt() {
      return false;
   }

   public SynchronizedStmt asSynchronizedStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an SynchronizedStmt", this));
   }

   public boolean isThrowStmt() {
      return false;
   }

   public ThrowStmt asThrowStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ThrowStmt", this));
   }

   public boolean isTryStmt() {
      return false;
   }

   public TryStmt asTryStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an TryStmt", this));
   }

   public boolean isUnparsableStmt() {
      return false;
   }

   public UnparsableStmt asUnparsableStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an UnparsableStmt", this));
   }

   public boolean isWhileStmt() {
      return false;
   }

   public WhileStmt asWhileStmt() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an WhileStmt", this));
   }

   public void ifAssertStmt(Consumer<AssertStmt> action) {
   }

   public void ifBlockStmt(Consumer<BlockStmt> action) {
   }

   public void ifBreakStmt(Consumer<BreakStmt> action) {
   }

   public void ifContinueStmt(Consumer<ContinueStmt> action) {
   }

   public void ifDoStmt(Consumer<DoStmt> action) {
   }

   public void ifEmptyStmt(Consumer<EmptyStmt> action) {
   }

   public void ifExplicitConstructorInvocationStmt(Consumer<ExplicitConstructorInvocationStmt> action) {
   }

   public void ifExpressionStmt(Consumer<ExpressionStmt> action) {
   }

   public void ifForStmt(Consumer<ForStmt> action) {
   }

   public void ifForeachStmt(Consumer<ForeachStmt> action) {
   }

   public void ifIfStmt(Consumer<IfStmt> action) {
   }

   public void ifLabeledStmt(Consumer<LabeledStmt> action) {
   }

   public void ifLocalClassDeclarationStmt(Consumer<LocalClassDeclarationStmt> action) {
   }

   public void ifReturnStmt(Consumer<ReturnStmt> action) {
   }

   public void ifSwitchEntryStmt(Consumer<SwitchEntryStmt> action) {
   }

   public void ifSwitchStmt(Consumer<SwitchStmt> action) {
   }

   public void ifSynchronizedStmt(Consumer<SynchronizedStmt> action) {
   }

   public void ifThrowStmt(Consumer<ThrowStmt> action) {
   }

   public void ifTryStmt(Consumer<TryStmt> action) {
   }

   public void ifUnparsableStmt(Consumer<UnparsableStmt> action) {
   }

   public void ifWhileStmt(Consumer<WhileStmt> action) {
   }

   public Optional<AssertStmt> toAssertStmt() {
      return Optional.empty();
   }

   public Optional<BlockStmt> toBlockStmt() {
      return Optional.empty();
   }

   public Optional<BreakStmt> toBreakStmt() {
      return Optional.empty();
   }

   public Optional<ContinueStmt> toContinueStmt() {
      return Optional.empty();
   }

   public Optional<DoStmt> toDoStmt() {
      return Optional.empty();
   }

   public Optional<EmptyStmt> toEmptyStmt() {
      return Optional.empty();
   }

   public Optional<ExplicitConstructorInvocationStmt> toExplicitConstructorInvocationStmt() {
      return Optional.empty();
   }

   public Optional<ExpressionStmt> toExpressionStmt() {
      return Optional.empty();
   }

   public Optional<ForStmt> toForStmt() {
      return Optional.empty();
   }

   public Optional<ForeachStmt> toForeachStmt() {
      return Optional.empty();
   }

   public Optional<IfStmt> toIfStmt() {
      return Optional.empty();
   }

   public Optional<LabeledStmt> toLabeledStmt() {
      return Optional.empty();
   }

   public Optional<LocalClassDeclarationStmt> toLocalClassDeclarationStmt() {
      return Optional.empty();
   }

   public Optional<ReturnStmt> toReturnStmt() {
      return Optional.empty();
   }

   public Optional<SwitchEntryStmt> toSwitchEntryStmt() {
      return Optional.empty();
   }

   public Optional<SwitchStmt> toSwitchStmt() {
      return Optional.empty();
   }

   public Optional<SynchronizedStmt> toSynchronizedStmt() {
      return Optional.empty();
   }

   public Optional<ThrowStmt> toThrowStmt() {
      return Optional.empty();
   }

   public Optional<TryStmt> toTryStmt() {
      return Optional.empty();
   }

   public Optional<UnparsableStmt> toUnparsableStmt() {
      return Optional.empty();
   }

   public Optional<WhileStmt> toWhileStmt() {
      return Optional.empty();
   }
}
