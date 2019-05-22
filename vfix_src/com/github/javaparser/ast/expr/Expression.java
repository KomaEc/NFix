package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.ExpressionMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.CodeGenerationUtils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Expression extends Node {
   @AllFieldsConstructor
   public Expression() {
      this((TokenRange)null);
   }

   public Expression(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public Expression clone() {
      return (Expression)this.accept(new CloneVisitor(), (Object)null);
   }

   public ExpressionMetaModel getMetaModel() {
      return JavaParserMetaModel.expressionMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isAnnotationExpr() {
      return false;
   }

   public AnnotationExpr asAnnotationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an AnnotationExpr", this));
   }

   public boolean isArrayAccessExpr() {
      return false;
   }

   public ArrayAccessExpr asArrayAccessExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ArrayAccessExpr", this));
   }

   public boolean isArrayCreationExpr() {
      return false;
   }

   public ArrayCreationExpr asArrayCreationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ArrayCreationExpr", this));
   }

   public boolean isArrayInitializerExpr() {
      return false;
   }

   public ArrayInitializerExpr asArrayInitializerExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ArrayInitializerExpr", this));
   }

   public boolean isAssignExpr() {
      return false;
   }

   public AssignExpr asAssignExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an AssignExpr", this));
   }

   public boolean isBinaryExpr() {
      return false;
   }

   public BinaryExpr asBinaryExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an BinaryExpr", this));
   }

   public boolean isBooleanLiteralExpr() {
      return false;
   }

   public BooleanLiteralExpr asBooleanLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an BooleanLiteralExpr", this));
   }

   public boolean isCastExpr() {
      return false;
   }

   public CastExpr asCastExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an CastExpr", this));
   }

   public boolean isCharLiteralExpr() {
      return false;
   }

   public CharLiteralExpr asCharLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an CharLiteralExpr", this));
   }

   public boolean isClassExpr() {
      return false;
   }

   public ClassExpr asClassExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ClassExpr", this));
   }

   public boolean isConditionalExpr() {
      return false;
   }

   public ConditionalExpr asConditionalExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ConditionalExpr", this));
   }

   public boolean isDoubleLiteralExpr() {
      return false;
   }

   public DoubleLiteralExpr asDoubleLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an DoubleLiteralExpr", this));
   }

   public boolean isEnclosedExpr() {
      return false;
   }

   public EnclosedExpr asEnclosedExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an EnclosedExpr", this));
   }

   public boolean isFieldAccessExpr() {
      return false;
   }

   public FieldAccessExpr asFieldAccessExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an FieldAccessExpr", this));
   }

   public boolean isInstanceOfExpr() {
      return false;
   }

   public InstanceOfExpr asInstanceOfExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an InstanceOfExpr", this));
   }

   public boolean isIntegerLiteralExpr() {
      return false;
   }

   public IntegerLiteralExpr asIntegerLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an IntegerLiteralExpr", this));
   }

   public boolean isLambdaExpr() {
      return false;
   }

   public LambdaExpr asLambdaExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LambdaExpr", this));
   }

   public boolean isLiteralExpr() {
      return false;
   }

   public LiteralExpr asLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LiteralExpr", this));
   }

   public boolean isLiteralStringValueExpr() {
      return false;
   }

   public LiteralStringValueExpr asLiteralStringValueExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LiteralStringValueExpr", this));
   }

   public boolean isLongLiteralExpr() {
      return false;
   }

   public LongLiteralExpr asLongLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an LongLiteralExpr", this));
   }

   public boolean isMarkerAnnotationExpr() {
      return false;
   }

   public MarkerAnnotationExpr asMarkerAnnotationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an MarkerAnnotationExpr", this));
   }

   public boolean isMethodCallExpr() {
      return false;
   }

   public MethodCallExpr asMethodCallExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an MethodCallExpr", this));
   }

   public boolean isMethodReferenceExpr() {
      return false;
   }

   public MethodReferenceExpr asMethodReferenceExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an MethodReferenceExpr", this));
   }

   public boolean isNameExpr() {
      return false;
   }

   public NameExpr asNameExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an NameExpr", this));
   }

   public boolean isNormalAnnotationExpr() {
      return false;
   }

   public NormalAnnotationExpr asNormalAnnotationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an NormalAnnotationExpr", this));
   }

   public boolean isNullLiteralExpr() {
      return false;
   }

   public NullLiteralExpr asNullLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an NullLiteralExpr", this));
   }

   public boolean isObjectCreationExpr() {
      return false;
   }

   public ObjectCreationExpr asObjectCreationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ObjectCreationExpr", this));
   }

   public boolean isSingleMemberAnnotationExpr() {
      return false;
   }

   public SingleMemberAnnotationExpr asSingleMemberAnnotationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an SingleMemberAnnotationExpr", this));
   }

   public boolean isStringLiteralExpr() {
      return false;
   }

   public StringLiteralExpr asStringLiteralExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an StringLiteralExpr", this));
   }

   public boolean isSuperExpr() {
      return false;
   }

   public SuperExpr asSuperExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an SuperExpr", this));
   }

   public boolean isThisExpr() {
      return false;
   }

   public ThisExpr asThisExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ThisExpr", this));
   }

   public boolean isTypeExpr() {
      return false;
   }

   public TypeExpr asTypeExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an TypeExpr", this));
   }

   public boolean isUnaryExpr() {
      return false;
   }

   public UnaryExpr asUnaryExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an UnaryExpr", this));
   }

   public boolean isVariableDeclarationExpr() {
      return false;
   }

   public VariableDeclarationExpr asVariableDeclarationExpr() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an VariableDeclarationExpr", this));
   }

   public void ifAnnotationExpr(Consumer<AnnotationExpr> action) {
   }

   public void ifArrayAccessExpr(Consumer<ArrayAccessExpr> action) {
   }

   public void ifArrayCreationExpr(Consumer<ArrayCreationExpr> action) {
   }

   public void ifArrayInitializerExpr(Consumer<ArrayInitializerExpr> action) {
   }

   public void ifAssignExpr(Consumer<AssignExpr> action) {
   }

   public void ifBinaryExpr(Consumer<BinaryExpr> action) {
   }

   public void ifBooleanLiteralExpr(Consumer<BooleanLiteralExpr> action) {
   }

   public void ifCastExpr(Consumer<CastExpr> action) {
   }

   public void ifCharLiteralExpr(Consumer<CharLiteralExpr> action) {
   }

   public void ifClassExpr(Consumer<ClassExpr> action) {
   }

   public void ifConditionalExpr(Consumer<ConditionalExpr> action) {
   }

   public void ifDoubleLiteralExpr(Consumer<DoubleLiteralExpr> action) {
   }

   public void ifEnclosedExpr(Consumer<EnclosedExpr> action) {
   }

   public void ifFieldAccessExpr(Consumer<FieldAccessExpr> action) {
   }

   public void ifInstanceOfExpr(Consumer<InstanceOfExpr> action) {
   }

   public void ifIntegerLiteralExpr(Consumer<IntegerLiteralExpr> action) {
   }

   public void ifLambdaExpr(Consumer<LambdaExpr> action) {
   }

   public void ifLiteralExpr(Consumer<LiteralExpr> action) {
   }

   public void ifLiteralStringValueExpr(Consumer<LiteralStringValueExpr> action) {
   }

   public void ifLongLiteralExpr(Consumer<LongLiteralExpr> action) {
   }

   public void ifMarkerAnnotationExpr(Consumer<MarkerAnnotationExpr> action) {
   }

   public void ifMethodCallExpr(Consumer<MethodCallExpr> action) {
   }

   public void ifMethodReferenceExpr(Consumer<MethodReferenceExpr> action) {
   }

   public void ifNameExpr(Consumer<NameExpr> action) {
   }

   public void ifNormalAnnotationExpr(Consumer<NormalAnnotationExpr> action) {
   }

   public void ifNullLiteralExpr(Consumer<NullLiteralExpr> action) {
   }

   public void ifObjectCreationExpr(Consumer<ObjectCreationExpr> action) {
   }

   public void ifSingleMemberAnnotationExpr(Consumer<SingleMemberAnnotationExpr> action) {
   }

   public void ifStringLiteralExpr(Consumer<StringLiteralExpr> action) {
   }

   public void ifSuperExpr(Consumer<SuperExpr> action) {
   }

   public void ifThisExpr(Consumer<ThisExpr> action) {
   }

   public void ifTypeExpr(Consumer<TypeExpr> action) {
   }

   public void ifUnaryExpr(Consumer<UnaryExpr> action) {
   }

   public void ifVariableDeclarationExpr(Consumer<VariableDeclarationExpr> action) {
   }

   public ResolvedType calculateResolvedType() {
      return this.getSymbolResolver().calculateType(this);
   }

   public Optional<AnnotationExpr> toAnnotationExpr() {
      return Optional.empty();
   }

   public Optional<ArrayAccessExpr> toArrayAccessExpr() {
      return Optional.empty();
   }

   public Optional<ArrayCreationExpr> toArrayCreationExpr() {
      return Optional.empty();
   }

   public Optional<ArrayInitializerExpr> toArrayInitializerExpr() {
      return Optional.empty();
   }

   public Optional<AssignExpr> toAssignExpr() {
      return Optional.empty();
   }

   public Optional<BinaryExpr> toBinaryExpr() {
      return Optional.empty();
   }

   public Optional<BooleanLiteralExpr> toBooleanLiteralExpr() {
      return Optional.empty();
   }

   public Optional<CastExpr> toCastExpr() {
      return Optional.empty();
   }

   public Optional<CharLiteralExpr> toCharLiteralExpr() {
      return Optional.empty();
   }

   public Optional<ClassExpr> toClassExpr() {
      return Optional.empty();
   }

   public Optional<ConditionalExpr> toConditionalExpr() {
      return Optional.empty();
   }

   public Optional<DoubleLiteralExpr> toDoubleLiteralExpr() {
      return Optional.empty();
   }

   public Optional<EnclosedExpr> toEnclosedExpr() {
      return Optional.empty();
   }

   public Optional<FieldAccessExpr> toFieldAccessExpr() {
      return Optional.empty();
   }

   public Optional<InstanceOfExpr> toInstanceOfExpr() {
      return Optional.empty();
   }

   public Optional<IntegerLiteralExpr> toIntegerLiteralExpr() {
      return Optional.empty();
   }

   public Optional<LambdaExpr> toLambdaExpr() {
      return Optional.empty();
   }

   public Optional<LiteralExpr> toLiteralExpr() {
      return Optional.empty();
   }

   public Optional<LiteralStringValueExpr> toLiteralStringValueExpr() {
      return Optional.empty();
   }

   public Optional<LongLiteralExpr> toLongLiteralExpr() {
      return Optional.empty();
   }

   public Optional<MarkerAnnotationExpr> toMarkerAnnotationExpr() {
      return Optional.empty();
   }

   public Optional<MethodCallExpr> toMethodCallExpr() {
      return Optional.empty();
   }

   public Optional<MethodReferenceExpr> toMethodReferenceExpr() {
      return Optional.empty();
   }

   public Optional<NameExpr> toNameExpr() {
      return Optional.empty();
   }

   public Optional<NormalAnnotationExpr> toNormalAnnotationExpr() {
      return Optional.empty();
   }

   public Optional<NullLiteralExpr> toNullLiteralExpr() {
      return Optional.empty();
   }

   public Optional<ObjectCreationExpr> toObjectCreationExpr() {
      return Optional.empty();
   }

   public Optional<SingleMemberAnnotationExpr> toSingleMemberAnnotationExpr() {
      return Optional.empty();
   }

   public Optional<StringLiteralExpr> toStringLiteralExpr() {
      return Optional.empty();
   }

   public Optional<SuperExpr> toSuperExpr() {
      return Optional.empty();
   }

   public Optional<ThisExpr> toThisExpr() {
      return Optional.empty();
   }

   public Optional<TypeExpr> toTypeExpr() {
      return Optional.empty();
   }

   public Optional<UnaryExpr> toUnaryExpr() {
      return Optional.empty();
   }

   public Optional<VariableDeclarationExpr> toVariableDeclarationExpr() {
      return Optional.empty();
   }
}
