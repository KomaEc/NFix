package org.codehaus.groovy.antlr.treewalker;

import org.codehaus.groovy.antlr.GroovySourceAST;

public interface Visitor {
   int OPENING_VISIT = 1;
   int SECOND_VISIT = 2;
   int SUBSEQUENT_VISIT = 3;
   int CLOSING_VISIT = 4;

   void setUp();

   void visitAbstract(GroovySourceAST var1, int var2);

   void visitAnnotation(GroovySourceAST var1, int var2);

   void visitAnnotations(GroovySourceAST var1, int var2);

   void visitAnnotationArrayInit(GroovySourceAST var1, int var2);

   void visitAnnotationDef(GroovySourceAST var1, int var2);

   void visitAnnotationFieldDef(GroovySourceAST var1, int var2);

   void visitAnnotationMemberValuePair(GroovySourceAST var1, int var2);

   void visitArrayDeclarator(GroovySourceAST var1, int var2);

   void visitAssign(GroovySourceAST var1, int var2);

   void visitAt(GroovySourceAST var1, int var2);

   void visitBand(GroovySourceAST var1, int var2);

   void visitBandAssign(GroovySourceAST var1, int var2);

   void visitBigSuffix(GroovySourceAST var1, int var2);

   void visitBlock(GroovySourceAST var1, int var2);

   void visitBnot(GroovySourceAST var1, int var2);

   void visitBor(GroovySourceAST var1, int var2);

   void visitBorAssign(GroovySourceAST var1, int var2);

   void visitBsr(GroovySourceAST var1, int var2);

   void visitBsrAssign(GroovySourceAST var1, int var2);

   void visitBxor(GroovySourceAST var1, int var2);

   void visitBxorAssign(GroovySourceAST var1, int var2);

   void visitCaseGroup(GroovySourceAST var1, int var2);

   void visitClassDef(GroovySourceAST var1, int var2);

   void visitClosedBlock(GroovySourceAST var1, int var2);

   void visitClosureList(GroovySourceAST var1, int var2);

   void visitClosureOp(GroovySourceAST var1, int var2);

   void visitColon(GroovySourceAST var1, int var2);

   void visitComma(GroovySourceAST var1, int var2);

   void visitCompareTo(GroovySourceAST var1, int var2);

   void visitCtorCall(GroovySourceAST var1, int var2);

   void visitCtorIdent(GroovySourceAST var1, int var2);

   void visitDec(GroovySourceAST var1, int var2);

   void visitDigit(GroovySourceAST var1, int var2);

   void visitDiv(GroovySourceAST var1, int var2);

   void visitDivAssign(GroovySourceAST var1, int var2);

   void visitDollar(GroovySourceAST var1, int var2);

   void visitDot(GroovySourceAST var1, int var2);

   void visitDynamicMember(GroovySourceAST var1, int var2);

   void visitElist(GroovySourceAST var1, int var2);

   void visitEmptyStat(GroovySourceAST var1, int var2);

   void visitEnumConstantDef(GroovySourceAST var1, int var2);

   void visitEnumDef(GroovySourceAST var1, int var2);

   void visitEof(GroovySourceAST var1, int var2);

   void visitEqual(GroovySourceAST var1, int var2);

   void visitEsc(GroovySourceAST var1, int var2);

   void visitExponent(GroovySourceAST var1, int var2);

   void visitExpr(GroovySourceAST var1, int var2);

   void visitExtendsClause(GroovySourceAST var1, int var2);

   void visitFinal(GroovySourceAST var1, int var2);

   void visitFloatSuffix(GroovySourceAST var1, int var2);

   void visitForCondition(GroovySourceAST var1, int var2);

   void visitForEachClause(GroovySourceAST var1, int var2);

   void visitForInit(GroovySourceAST var1, int var2);

   void visitForInIterable(GroovySourceAST var1, int var2);

   void visitForIterator(GroovySourceAST var1, int var2);

   void visitGe(GroovySourceAST var1, int var2);

   void visitGt(GroovySourceAST var1, int var2);

   void visitHexDigit(GroovySourceAST var1, int var2);

   void visitIdent(GroovySourceAST var1, int var2);

   void visitImplementsClause(GroovySourceAST var1, int var2);

   void visitImplicitParameters(GroovySourceAST var1, int var2);

   void visitImport(GroovySourceAST var1, int var2);

   void visitInc(GroovySourceAST var1, int var2);

   void visitIndexOp(GroovySourceAST var1, int var2);

   void visitInstanceInit(GroovySourceAST var1, int var2);

   void visitInterfaceDef(GroovySourceAST var1, int var2);

   void visitLabeledArg(GroovySourceAST var1, int var2);

   void visitLabeledStat(GroovySourceAST var1, int var2);

   void visitLand(GroovySourceAST var1, int var2);

   void visitLbrack(GroovySourceAST var1, int var2);

   void visitLcurly(GroovySourceAST var1, int var2);

   void visitLe(GroovySourceAST var1, int var2);

   void visitLetter(GroovySourceAST var1, int var2);

   void visitListConstructor(GroovySourceAST var1, int var2);

   void visitLiteralAs(GroovySourceAST var1, int var2);

   void visitLiteralAssert(GroovySourceAST var1, int var2);

   void visitLiteralBoolean(GroovySourceAST var1, int var2);

   void visitLiteralBreak(GroovySourceAST var1, int var2);

   void visitLiteralByte(GroovySourceAST var1, int var2);

   void visitLiteralCase(GroovySourceAST var1, int var2);

   void visitLiteralCatch(GroovySourceAST var1, int var2);

   void visitLiteralChar(GroovySourceAST var1, int var2);

   void visitLiteralClass(GroovySourceAST var1, int var2);

   void visitLiteralContinue(GroovySourceAST var1, int var2);

   void visitLiteralDef(GroovySourceAST var1, int var2);

   void visitLiteralDefault(GroovySourceAST var1, int var2);

   void visitLiteralDouble(GroovySourceAST var1, int var2);

   void visitLiteralElse(GroovySourceAST var1, int var2);

   void visitLiteralEnum(GroovySourceAST var1, int var2);

   void visitLiteralExtends(GroovySourceAST var1, int var2);

   void visitLiteralFalse(GroovySourceAST var1, int var2);

   void visitLiteralFinally(GroovySourceAST var1, int var2);

   void visitLiteralFloat(GroovySourceAST var1, int var2);

   void visitLiteralFor(GroovySourceAST var1, int var2);

   void visitLiteralIf(GroovySourceAST var1, int var2);

   void visitLiteralImplements(GroovySourceAST var1, int var2);

   void visitLiteralImport(GroovySourceAST var1, int var2);

   void visitLiteralIn(GroovySourceAST var1, int var2);

   void visitLiteralInstanceof(GroovySourceAST var1, int var2);

   void visitLiteralInt(GroovySourceAST var1, int var2);

   void visitLiteralInterface(GroovySourceAST var1, int var2);

   void visitLiteralLong(GroovySourceAST var1, int var2);

   void visitLiteralNative(GroovySourceAST var1, int var2);

   void visitLiteralNew(GroovySourceAST var1, int var2);

   void visitLiteralNull(GroovySourceAST var1, int var2);

   void visitLiteralPackage(GroovySourceAST var1, int var2);

   void visitLiteralPrivate(GroovySourceAST var1, int var2);

   void visitLiteralProtected(GroovySourceAST var1, int var2);

   void visitLiteralPublic(GroovySourceAST var1, int var2);

   void visitLiteralReturn(GroovySourceAST var1, int var2);

   void visitLiteralShort(GroovySourceAST var1, int var2);

   void visitLiteralStatic(GroovySourceAST var1, int var2);

   void visitLiteralSuper(GroovySourceAST var1, int var2);

   void visitLiteralSwitch(GroovySourceAST var1, int var2);

   void visitLiteralSynchronized(GroovySourceAST var1, int var2);

   void visitLiteralThis(GroovySourceAST var1, int var2);

   void visitLiteralThreadsafe(GroovySourceAST var1, int var2);

   void visitLiteralThrow(GroovySourceAST var1, int var2);

   void visitLiteralThrows(GroovySourceAST var1, int var2);

   void visitLiteralTransient(GroovySourceAST var1, int var2);

   void visitLiteralTrue(GroovySourceAST var1, int var2);

   void visitLiteralTry(GroovySourceAST var1, int var2);

   void visitLiteralVoid(GroovySourceAST var1, int var2);

   void visitLiteralVolatile(GroovySourceAST var1, int var2);

   void visitLiteralWhile(GroovySourceAST var1, int var2);

   void visitLnot(GroovySourceAST var1, int var2);

   void visitLor(GroovySourceAST var1, int var2);

   void visitLparen(GroovySourceAST var1, int var2);

   void visitLt(GroovySourceAST var1, int var2);

   void visitMapConstructor(GroovySourceAST var1, int var2);

   void visitMemberPointer(GroovySourceAST var1, int var2);

   void visitMethodCall(GroovySourceAST var1, int var2);

   void visitMethodDef(GroovySourceAST var1, int var2);

   void visitMinus(GroovySourceAST var1, int var2);

   void visitMinusAssign(GroovySourceAST var1, int var2);

   void visitMlComment(GroovySourceAST var1, int var2);

   void visitMod(GroovySourceAST var1, int var2);

   void visitModifiers(GroovySourceAST var1, int var2);

   void visitModAssign(GroovySourceAST var1, int var2);

   void visitNls(GroovySourceAST var1, int var2);

   void visitNotEqual(GroovySourceAST var1, int var2);

   void visitNullTreeLookahead(GroovySourceAST var1, int var2);

   void visitNumBigDecimal(GroovySourceAST var1, int var2);

   void visitNumBigInt(GroovySourceAST var1, int var2);

   void visitNumDouble(GroovySourceAST var1, int var2);

   void visitNumFloat(GroovySourceAST var1, int var2);

   void visitNumInt(GroovySourceAST var1, int var2);

   void visitNumLong(GroovySourceAST var1, int var2);

   void visitObjblock(GroovySourceAST var1, int var2);

   void visitOneNl(GroovySourceAST var1, int var2);

   void visitOptionalDot(GroovySourceAST var1, int var2);

   void visitPackageDef(GroovySourceAST var1, int var2);

   void visitParameters(GroovySourceAST var1, int var2);

   void visitParameterDef(GroovySourceAST var1, int var2);

   void visitPlus(GroovySourceAST var1, int var2);

   void visitPlusAssign(GroovySourceAST var1, int var2);

   void visitPostDec(GroovySourceAST var1, int var2);

   void visitPostInc(GroovySourceAST var1, int var2);

   void visitQuestion(GroovySourceAST var1, int var2);

   void visitRangeExclusive(GroovySourceAST var1, int var2);

   void visitRangeInclusive(GroovySourceAST var1, int var2);

   void visitRbrack(GroovySourceAST var1, int var2);

   void visitRcurly(GroovySourceAST var1, int var2);

   void visitRegexpCtorEnd(GroovySourceAST var1, int var2);

   void visitRegexpLiteral(GroovySourceAST var1, int var2);

   void visitRegexpSymbol(GroovySourceAST var1, int var2);

   void visitRegexFind(GroovySourceAST var1, int var2);

   void visitRegexMatch(GroovySourceAST var1, int var2);

   void visitRparen(GroovySourceAST var1, int var2);

   void visitSelectSlot(GroovySourceAST var1, int var2);

   void visitSemi(GroovySourceAST var1, int var2);

   void visitShComment(GroovySourceAST var1, int var2);

   void visitSl(GroovySourceAST var1, int var2);

   void visitSlist(GroovySourceAST var1, int var2);

   void visitSlAssign(GroovySourceAST var1, int var2);

   void visitSlComment(GroovySourceAST var1, int var2);

   void visitSpreadArg(GroovySourceAST var1, int var2);

   void visitSpreadDot(GroovySourceAST var1, int var2);

   void visitSpreadMapArg(GroovySourceAST var1, int var2);

   void visitSr(GroovySourceAST var1, int var2);

   void visitSrAssign(GroovySourceAST var1, int var2);

   void visitStar(GroovySourceAST var1, int var2);

   void visitStarAssign(GroovySourceAST var1, int var2);

   void visitStarStar(GroovySourceAST var1, int var2);

   void visitStarStarAssign(GroovySourceAST var1, int var2);

   void visitStaticImport(GroovySourceAST var1, int var2);

   void visitStaticInit(GroovySourceAST var1, int var2);

   void visitStrictfp(GroovySourceAST var1, int var2);

   void visitStringCh(GroovySourceAST var1, int var2);

   void visitStringConstructor(GroovySourceAST var1, int var2);

   void visitStringCtorEnd(GroovySourceAST var1, int var2);

   void visitStringCtorMiddle(GroovySourceAST var1, int var2);

   void visitStringCtorStart(GroovySourceAST var1, int var2);

   void visitStringLiteral(GroovySourceAST var1, int var2);

   void visitStringNl(GroovySourceAST var1, int var2);

   void visitSuperCtorCall(GroovySourceAST var1, int var2);

   void visitTripleDot(GroovySourceAST var1, int var2);

   void visitType(GroovySourceAST var1, int var2);

   void visitTypecast(GroovySourceAST var1, int var2);

   void visitTypeArgument(GroovySourceAST var1, int var2);

   void visitTypeArguments(GroovySourceAST var1, int var2);

   void visitTypeLowerBounds(GroovySourceAST var1, int var2);

   void visitTypeParameter(GroovySourceAST var1, int var2);

   void visitTypeParameters(GroovySourceAST var1, int var2);

   void visitTypeUpperBounds(GroovySourceAST var1, int var2);

   void visitUnaryMinus(GroovySourceAST var1, int var2);

   void visitUnaryPlus(GroovySourceAST var1, int var2);

   void visitUnusedConst(GroovySourceAST var1, int var2);

   void visitUnusedDo(GroovySourceAST var1, int var2);

   void visitUnusedGoto(GroovySourceAST var1, int var2);

   void visitVariableDef(GroovySourceAST var1, int var2);

   void visitVariableParameterDef(GroovySourceAST var1, int var2);

   void visitVocab(GroovySourceAST var1, int var2);

   void visitWildcardType(GroovySourceAST var1, int var2);

   void visitWs(GroovySourceAST var1, int var2);

   void visitDefault(GroovySourceAST var1, int var2);

   void tearDown();

   void push(GroovySourceAST var1);

   GroovySourceAST pop();
}
