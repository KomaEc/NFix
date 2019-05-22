package org.codehaus.groovy.antlr.treewalker;

import java.io.PrintStream;
import java.util.Stack;
import org.codehaus.groovy.antlr.GroovySourceAST;

public class SourcePrinter extends VisitorAdapter {
   private final String[] tokenNames;
   private int tabLevel;
   private int lastLinePrinted;
   private final boolean newLines;
   protected final PrintStream out;
   private String className;
   private final Stack stack;
   private int stringConstructorCounter;

   public SourcePrinter(PrintStream out, String[] tokenNames) {
      this(out, tokenNames, true);
   }

   public SourcePrinter(PrintStream out, String[] tokenNames, boolean newLines) {
      this.tokenNames = tokenNames;
      this.tabLevel = 0;
      this.lastLinePrinted = 0;
      this.out = out;
      this.newLines = newLines;
      this.stack = new Stack();
   }

   public void visitAbstract(GroovySourceAST t, int visit) {
      this.print(t, visit, "abstract ", (String)null, (String)null);
   }

   public void visitAnnotation(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.print(t, visit, "@");
      }

      if (visit == 2) {
         this.print(t, visit, "(");
      }

      if (visit == 3) {
         this.print(t, visit, ", ");
      }

      if (visit == 4) {
         if (t.getNumberOfChildren() > 1) {
            this.print(t, visit, ") ");
         } else {
            this.print(t, visit, " ");
         }
      }

   }

   public void visitAnnotations(GroovySourceAST t, int visit) {
   }

   public void visitAnnotationDef(GroovySourceAST t, int visit) {
      this.print(t, visit, "@interface ", (String)null, (String)null);
   }

   public void visitAnnotationFieldDef(GroovySourceAST t, int visit) {
      this.print(t, visit, "() ", "default ", (String)null);
   }

   public void visitAnnotationMemberValuePair(GroovySourceAST t, int visit) {
      this.print(t, visit, " = ", (String)null, (String)null);
   }

   public void visitArrayDeclarator(GroovySourceAST t, int visit) {
      if (this.getParentNode().getType() != 12 && this.getParentNode().getType() != 22) {
         this.print(t, visit, "[", (String)null, "]");
      } else {
         this.print(t, visit, (String)null, (String)null, "[]");
      }

   }

   public void visitAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " = ", (String)null, (String)null);
   }

   public void visitBand(GroovySourceAST t, int visit) {
      this.print(t, visit, " & ", (String)null, (String)null);
   }

   public void visitBandAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " &= ", (String)null, (String)null);
   }

   public void visitBnot(GroovySourceAST t, int visit) {
      this.print(t, visit, "~", (String)null, (String)null);
   }

   public void visitBor(GroovySourceAST t, int visit) {
      this.print(t, visit, " | ", (String)null, (String)null);
   }

   public void visitBorAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " |= ", (String)null, (String)null);
   }

   public void visitBsr(GroovySourceAST t, int visit) {
      this.print(t, visit, " >>> ", (String)null, (String)null);
   }

   public void visitBsrAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " >>>= ", (String)null, (String)null);
   }

   public void visitBxor(GroovySourceAST t, int visit) {
      this.print(t, visit, " ^ ", (String)null, (String)null);
   }

   public void visitBxorAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " ^= ", (String)null, (String)null);
   }

   public void visitCaseGroup(GroovySourceAST t, int visit) {
      if (visit == 1) {
         ++this.tabLevel;
      }

      if (visit == 4) {
         --this.tabLevel;
      }

   }

   public void visitClassDef(GroovySourceAST t, int visit) {
      this.print(t, visit, "class ", (String)null, (String)null);
      if (visit == 1) {
         this.className = t.childOfType(84).getText();
      }

   }

   public void visitClosedBlock(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "{", "-> ", "}");
   }

   public void visitClosureList(GroovySourceAST t, int visit) {
      this.print(t, visit, "(", "; ", ")");
   }

   public void visitCompareTo(GroovySourceAST t, int visit) {
      this.print(t, visit, " <=> ", (String)null, (String)null);
   }

   public void visitCtorCall(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "this(", " ", ")");
   }

   public void visitCtorIdent(GroovySourceAST t, int visit) {
      this.print(t, visit, this.className, (String)null, (String)null);
   }

   public void visitDec(GroovySourceAST t, int visit) {
      this.print(t, visit, "--", (String)null, (String)null);
   }

   public void visitDiv(GroovySourceAST t, int visit) {
      this.print(t, visit, " / ", (String)null, (String)null);
   }

   public void visitDivAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " /= ", (String)null, (String)null);
   }

   public void visitDot(GroovySourceAST t, int visit) {
      this.print(t, visit, ".", (String)null, (String)null);
   }

   public void visitDynamicMember(GroovySourceAST t, int visit) {
      if (t.childOfType(47) == null) {
         this.printUpdatingTabLevel(t, visit, "(", (String)null, ")");
      }

   }

   public void visitElist(GroovySourceAST t, int visit) {
      if (this.getParentNode().getType() == 61) {
         this.print(t, visit, "(", ", ", ")");
      } else {
         this.print(t, visit, (String)null, ", ", (String)null);
      }

   }

   public void visitEnumConstantDef(GroovySourceAST t, int visit) {
      GroovySourceAST sibling = (GroovySourceAST)t.getNextSibling();
      if (sibling != null && sibling.getType() == 61) {
         this.print(t, visit, (String)null, (String)null, ", ");
      }

   }

   public void visitEnumDef(GroovySourceAST t, int visit) {
      this.print(t, visit, "enum ", (String)null, (String)null);
   }

   public void visitEqual(GroovySourceAST t, int visit) {
      this.print(t, visit, " == ", (String)null, (String)null);
   }

   public void visitExpr(GroovySourceAST t, int visit) {
   }

   public void visitExtendsClause(GroovySourceAST t, int visit) {
      if (visit == 1 && t.getNumberOfChildren() != 0) {
         this.print(t, visit, " extends ");
      }

   }

   public void visitFinal(GroovySourceAST t, int visit) {
      this.print(t, visit, "final ", (String)null, (String)null);
   }

   public void visitForCondition(GroovySourceAST t, int visit) {
      this.print(t, visit, " ; ", (String)null, (String)null);
   }

   public void visitForInit(GroovySourceAST t, int visit) {
      this.print(t, visit, "(", (String)null, (String)null);
   }

   public void visitForInIterable(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "(", " in ", ") ");
   }

   public void visitForIterator(GroovySourceAST t, int visit) {
      this.print(t, visit, " ; ", (String)null, ")");
   }

   public void visitGe(GroovySourceAST t, int visit) {
      this.print(t, visit, " >= ", (String)null, (String)null);
   }

   public void visitGt(GroovySourceAST t, int visit) {
      this.print(t, visit, " > ", (String)null, (String)null);
   }

   public void visitIdent(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitImplementsClause(GroovySourceAST t, int visit) {
      if (visit == 1 && t.getNumberOfChildren() != 0) {
         this.print(t, visit, " implements ");
      }

      if (visit == 4) {
         this.print(t, visit, " ");
      }

   }

   public void visitImplicitParameters(GroovySourceAST t, int visit) {
   }

   public void visitImport(GroovySourceAST t, int visit) {
      this.print(t, visit, "import ", (String)null, (String)null);
   }

   public void visitInc(GroovySourceAST t, int visit) {
      this.print(t, visit, "++", (String)null, (String)null);
   }

   public void visitIndexOp(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "[", (String)null, "]");
   }

   public void visitInterfaceDef(GroovySourceAST t, int visit) {
      this.print(t, visit, "interface ", (String)null, (String)null);
   }

   public void visitInstanceInit(GroovySourceAST t, int visit) {
   }

   public void visitLabeledArg(GroovySourceAST t, int visit) {
      this.print(t, visit, ":", (String)null, (String)null);
   }

   public void visitLabeledStat(GroovySourceAST t, int visit) {
      this.print(t, visit, ":", (String)null, (String)null);
   }

   public void visitLand(GroovySourceAST t, int visit) {
      this.print(t, visit, " && ", (String)null, (String)null);
   }

   public void visitLe(GroovySourceAST t, int visit) {
      this.print(t, visit, " <= ", (String)null, (String)null);
   }

   public void visitListConstructor(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "[", (String)null, "]");
   }

   public void visitLiteralAs(GroovySourceAST t, int visit) {
      this.print(t, visit, " as ", (String)null, (String)null);
   }

   public void visitLiteralAssert(GroovySourceAST t, int visit) {
      if (t.getNumberOfChildren() > 1) {
         this.print(t, visit, "assert ", (String)null, " : ");
      } else {
         this.print(t, visit, "assert ", (String)null, (String)null);
      }

   }

   public void visitLiteralBoolean(GroovySourceAST t, int visit) {
      this.print(t, visit, "boolean", (String)null, (String)null);
   }

   public void visitLiteralBreak(GroovySourceAST t, int visit) {
      this.print(t, visit, "break ", (String)null, (String)null);
   }

   public void visitLiteralByte(GroovySourceAST t, int visit) {
      this.print(t, visit, "byte", (String)null, (String)null);
   }

   public void visitLiteralCase(GroovySourceAST t, int visit) {
      this.print(t, visit, "case ", (String)null, ":");
   }

   public void visitLiteralCatch(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, " catch (", (String)null, ") ");
   }

   public void visitLiteralChar(GroovySourceAST t, int visit) {
      this.print(t, visit, "char", (String)null, (String)null);
   }

   public void visitLiteralContinue(GroovySourceAST t, int visit) {
      this.print(t, visit, "continue ", (String)null, (String)null);
   }

   public void visitLiteralDefault(GroovySourceAST t, int visit) {
      this.print(t, visit, "default", (String)null, ":");
   }

   public void visitLiteralDouble(GroovySourceAST t, int visit) {
      this.print(t, visit, "double", (String)null, (String)null);
   }

   public void visitLiteralFalse(GroovySourceAST t, int visit) {
      this.print(t, visit, "false", (String)null, (String)null);
   }

   public void visitLiteralFinally(GroovySourceAST t, int visit) {
      this.print(t, visit, "finally ", (String)null, (String)null);
   }

   public void visitLiteralFloat(GroovySourceAST t, int visit) {
      this.print(t, visit, "float", (String)null, (String)null);
   }

   public void visitLiteralFor(GroovySourceAST t, int visit) {
      this.print(t, visit, "for ", (String)null, (String)null);
   }

   public void visitLiteralIf(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "if (", " else ", ") ");
   }

   public void visitLiteralIn(GroovySourceAST t, int visit) {
      this.print(t, visit, " in ", (String)null, (String)null);
   }

   public void visitLiteralInstanceof(GroovySourceAST t, int visit) {
      this.print(t, visit, " instanceof ", (String)null, (String)null);
   }

   public void visitLiteralInt(GroovySourceAST t, int visit) {
      this.print(t, visit, "int", (String)null, (String)null);
   }

   public void visitLiteralLong(GroovySourceAST t, int visit) {
      this.print(t, visit, "long", (String)null, (String)null);
   }

   public void visitLiteralNative(GroovySourceAST t, int visit) {
      this.print(t, visit, "native ", (String)null, (String)null);
   }

   public void visitLiteralNew(GroovySourceAST t, int visit) {
      if (t.childOfType(16) == null) {
         this.print(t, visit, "new ", "(", ")");
      } else {
         this.print(t, visit, "new ", (String)null, (String)null);
      }

   }

   public void visitLiteralNull(GroovySourceAST t, int visit) {
      this.print(t, visit, "null", (String)null, (String)null);
   }

   public void visitLiteralPrivate(GroovySourceAST t, int visit) {
      this.print(t, visit, "private ", (String)null, (String)null);
   }

   public void visitLiteralProtected(GroovySourceAST t, int visit) {
      this.print(t, visit, "protected ", (String)null, (String)null);
   }

   public void visitLiteralPublic(GroovySourceAST t, int visit) {
      this.print(t, visit, "public ", (String)null, (String)null);
   }

   public void visitLiteralReturn(GroovySourceAST t, int visit) {
      this.print(t, visit, "return ", (String)null, (String)null);
   }

   public void visitLiteralShort(GroovySourceAST t, int visit) {
      this.print(t, visit, "short", (String)null, (String)null);
   }

   public void visitLiteralStatic(GroovySourceAST t, int visit) {
      this.print(t, visit, "static ", (String)null, (String)null);
   }

   public void visitLiteralSuper(GroovySourceAST t, int visit) {
      this.print(t, visit, "super", (String)null, (String)null);
   }

   public void visitLiteralSwitch(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.print(t, visit, "switch (");
         ++this.tabLevel;
      }

      if (visit == 3) {
         this.print(t, visit, ") {");
      }

      if (visit == 4) {
         --this.tabLevel;
         this.print(t, visit, "}");
      }

   }

   public void visitLiteralSynchronized(GroovySourceAST t, int visit) {
      if (t.getNumberOfChildren() > 0) {
         this.print(t, visit, "synchronized (", (String)null, ") ");
      } else {
         this.print(t, visit, "synchronized ", (String)null, (String)null);
      }

   }

   public void visitLiteralThis(GroovySourceAST t, int visit) {
      this.print(t, visit, "this", (String)null, (String)null);
   }

   public void visitLiteralThreadsafe(GroovySourceAST t, int visit) {
      this.print(t, visit, "threadsafe ", (String)null, (String)null);
   }

   public void visitLiteralThrow(GroovySourceAST t, int visit) {
      this.print(t, visit, "throw ", (String)null, (String)null);
   }

   public void visitLiteralThrows(GroovySourceAST t, int visit) {
      this.print(t, visit, "throws ", (String)null, (String)null);
   }

   public void visitLiteralTransient(GroovySourceAST t, int visit) {
      this.print(t, visit, "transient ", (String)null, (String)null);
   }

   public void visitLiteralTrue(GroovySourceAST t, int visit) {
      this.print(t, visit, "true", (String)null, (String)null);
   }

   public void visitLiteralTry(GroovySourceAST t, int visit) {
      this.print(t, visit, "try ", (String)null, (String)null);
   }

   public void visitLiteralVoid(GroovySourceAST t, int visit) {
      this.print(t, visit, "void", (String)null, (String)null);
   }

   public void visitLiteralVolatile(GroovySourceAST t, int visit) {
      this.print(t, visit, "volatile ", (String)null, (String)null);
   }

   public void visitLiteralWhile(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "while (", (String)null, ") ");
   }

   public void visitLnot(GroovySourceAST t, int visit) {
      this.print(t, visit, "!", (String)null, (String)null);
   }

   public void visitLor(GroovySourceAST t, int visit) {
      this.print(t, visit, " || ", (String)null, (String)null);
   }

   public void visitLt(GroovySourceAST t, int visit) {
      this.print(t, visit, " < ", (String)null, (String)null);
   }

   public void visitMapConstructor(GroovySourceAST t, int visit) {
      if (t.getNumberOfChildren() == 0) {
         this.print(t, visit, "[:]", (String)null, (String)null);
      } else {
         this.printUpdatingTabLevel(t, visit, "[", (String)null, "]");
      }

   }

   public void visitMemberPointer(GroovySourceAST t, int visit) {
      this.print(t, visit, ".&", (String)null, (String)null);
   }

   public void visitMethodCall(GroovySourceAST t, int visit) {
      if ("<command>".equals(t.getText())) {
         this.printUpdatingTabLevel(t, visit, " ", " ", (String)null);
      } else {
         this.printUpdatingTabLevel(t, visit, "(", " ", ")");
      }

   }

   public void visitMethodDef(GroovySourceAST t, int visit) {
   }

   public void visitMinus(GroovySourceAST t, int visit) {
      this.print(t, visit, " - ", (String)null, (String)null);
   }

   public void visitMinusAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " -= ", (String)null, (String)null);
   }

   public void visitMod(GroovySourceAST t, int visit) {
      this.print(t, visit, " % ", (String)null, (String)null);
   }

   public void visitModifiers(GroovySourceAST t, int visit) {
   }

   public void visitModAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " %= ", (String)null, (String)null);
   }

   public void visitNotEqual(GroovySourceAST t, int visit) {
      this.print(t, visit, " != ", (String)null, (String)null);
   }

   public void visitNumBigDecimal(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitNumBigInt(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitNumDouble(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitNumInt(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitNumFloat(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitNumLong(GroovySourceAST t, int visit) {
      this.print(t, visit, t.getText(), (String)null, (String)null);
   }

   public void visitObjblock(GroovySourceAST t, int visit) {
      if (visit == 1) {
         ++this.tabLevel;
         this.print(t, visit, "{");
      } else {
         --this.tabLevel;
         this.print(t, visit, "}");
      }

   }

   public void visitOptionalDot(GroovySourceAST t, int visit) {
      this.print(t, visit, "?.", (String)null, (String)null);
   }

   public void visitPackageDef(GroovySourceAST t, int visit) {
      this.print(t, visit, "package ", (String)null, (String)null);
   }

   public void visitParameterDef(GroovySourceAST t, int visit) {
   }

   public void visitParameters(GroovySourceAST t, int visit) {
      if (this.getParentNode().getType() == 49) {
         this.printUpdatingTabLevel(t, visit, (String)null, ",", " ");
      } else {
         this.printUpdatingTabLevel(t, visit, "(", ", ", ") ");
      }

   }

   public void visitPlus(GroovySourceAST t, int visit) {
      this.print(t, visit, " + ", (String)null, (String)null);
   }

   public void visitPlusAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " += ", (String)null, (String)null);
   }

   public void visitPostDec(GroovySourceAST t, int visit) {
      this.print(t, visit, (String)null, (String)null, "--");
   }

   public void visitPostInc(GroovySourceAST t, int visit) {
      this.print(t, visit, (String)null, (String)null, "++");
   }

   public void visitQuestion(GroovySourceAST t, int visit) {
      this.print(t, visit, "?", ":", (String)null);
   }

   public void visitRangeExclusive(GroovySourceAST t, int visit) {
      this.print(t, visit, "..<", (String)null, (String)null);
   }

   public void visitRangeInclusive(GroovySourceAST t, int visit) {
      this.print(t, visit, "..", (String)null, (String)null);
   }

   public void visitRegexFind(GroovySourceAST t, int visit) {
      this.print(t, visit, " =~ ", (String)null, (String)null);
   }

   public void visitRegexMatch(GroovySourceAST t, int visit) {
      this.print(t, visit, " ==~ ", (String)null, (String)null);
   }

   public void visitSelectSlot(GroovySourceAST t, int visit) {
      this.print(t, visit, "@", (String)null, (String)null);
   }

   public void visitSl(GroovySourceAST t, int visit) {
      this.print(t, visit, " << ", (String)null, (String)null);
   }

   public void visitSlAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " <<= ", (String)null, (String)null);
   }

   public void visitSlist(GroovySourceAST t, int visit) {
      if (visit == 1) {
         ++this.tabLevel;
         this.print(t, visit, "{");
      } else {
         --this.tabLevel;
         this.print(t, visit, "}");
      }

   }

   public void visitSpreadArg(GroovySourceAST t, int visit) {
      this.print(t, visit, "*", (String)null, (String)null);
   }

   public void visitSpreadDot(GroovySourceAST t, int visit) {
      this.print(t, visit, "*.", (String)null, (String)null);
   }

   public void visitSpreadMapArg(GroovySourceAST t, int visit) {
      this.print(t, visit, "*:", (String)null, (String)null);
   }

   public void visitSr(GroovySourceAST t, int visit) {
      this.print(t, visit, " >> ", (String)null, (String)null);
   }

   public void visitSrAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " >>= ", (String)null, (String)null);
   }

   public void visitStar(GroovySourceAST t, int visit) {
      this.print(t, visit, "*", (String)null, (String)null);
   }

   public void visitStarAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " *= ", (String)null, (String)null);
   }

   public void visitStarStar(GroovySourceAST t, int visit) {
      this.print(t, visit, "**", (String)null, (String)null);
   }

   public void visitStarStarAssign(GroovySourceAST t, int visit) {
      this.print(t, visit, " **= ", (String)null, (String)null);
   }

   public void visitStaticInit(GroovySourceAST t, int visit) {
      this.print(t, visit, "static ", (String)null, (String)null);
   }

   public void visitStaticImport(GroovySourceAST t, int visit) {
      this.print(t, visit, "import static ", (String)null, (String)null);
   }

   public void visitStrictfp(GroovySourceAST t, int visit) {
      this.print(t, visit, "strictfp ", (String)null, (String)null);
   }

   public void visitStringConstructor(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.stringConstructorCounter = 0;
         this.print(t, visit, "\"");
      }

      if (visit == 3) {
         if (this.stringConstructorCounter % 2 == 0) {
            this.print(t, visit, "$");
         }

         ++this.stringConstructorCounter;
      }

      if (visit == 4) {
         this.print(t, visit, "\"");
      }

   }

   public void visitStringLiteral(GroovySourceAST t, int visit) {
      if (visit == 1) {
         String theString = this.escape(t.getText());
         if (this.getParentNode().getType() != 53 && this.getParentNode().getType() != 47) {
            theString = "\"" + theString + "\"";
         }

         this.print(t, visit, theString);
      }

   }

   private String escape(String literal) {
      literal = literal.replaceAll("\n", "\\\\<<REMOVE>>n");
      literal = literal.replaceAll("<<REMOVE>>", "");
      return literal;
   }

   public void visitSuperCtorCall(GroovySourceAST t, int visit) {
      this.printUpdatingTabLevel(t, visit, "super(", " ", ")");
   }

   public void visitType(GroovySourceAST t, int visit) {
      GroovySourceAST parent = this.getParentNode();
      GroovySourceAST modifiers = parent.childOfType(5);
      if (modifiers != null && modifiers.getNumberOfChildren() != 0) {
         if (visit == 4 && t.getNumberOfChildren() != 0) {
            this.print(t, visit, " ");
         }
      } else {
         if (visit == 1 && t.getNumberOfChildren() == 0 && parent.getType() != 20) {
            this.print(t, visit, "def");
         }

         if (visit == 4 && (parent.getType() == 9 || parent.getType() == 8 || parent.getType() == 67 || parent.getType() == 20 && t.getNumberOfChildren() != 0)) {
            this.print(t, visit, " ");
         }
      }

   }

   public void visitTypeArgument(GroovySourceAST t, int visit) {
   }

   public void visitTypeArguments(GroovySourceAST t, int visit) {
      this.print(t, visit, "<", ", ", ">");
   }

   public void visitTypecast(GroovySourceAST t, int visit) {
      this.print(t, visit, "(", (String)null, ")");
   }

   public void visitTypeLowerBounds(GroovySourceAST t, int visit) {
      this.print(t, visit, " super ", " & ", (String)null);
   }

   public void visitTypeParameter(GroovySourceAST t, int visit) {
   }

   public void visitTypeParameters(GroovySourceAST t, int visit) {
      this.print(t, visit, "<", ", ", ">");
   }

   public void visitTypeUpperBounds(GroovySourceAST t, int visit) {
      this.print(t, visit, " extends ", " & ", (String)null);
   }

   public void visitUnaryMinus(GroovySourceAST t, int visit) {
      this.print(t, visit, "-", (String)null, (String)null);
   }

   public void visitUnaryPlus(GroovySourceAST t, int visit) {
      this.print(t, visit, "+", (String)null, (String)null);
   }

   public void visitVariableDef(GroovySourceAST t, int visit) {
   }

   public void visitVariableParameterDef(GroovySourceAST t, int visit) {
      this.print(t, visit, (String)null, "... ", (String)null);
   }

   public void visitWildcardType(GroovySourceAST t, int visit) {
      this.print(t, visit, "?", (String)null, (String)null);
   }

   public void visitDefault(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.print(t, visit, "<" + this.tokenNames[t.getType()] + ">");
      } else {
         this.print(t, visit, "</" + this.tokenNames[t.getType()] + ">");
      }

   }

   protected void printUpdatingTabLevel(GroovySourceAST t, int visit, String opening, String subsequent, String closing) {
      if (visit == 1 && opening != null) {
         this.print(t, visit, opening);
         ++this.tabLevel;
      }

      if (visit == 3 && subsequent != null) {
         this.print(t, visit, subsequent);
      }

      if (visit == 4 && closing != null) {
         --this.tabLevel;
         this.print(t, visit, closing);
      }

   }

   protected void print(GroovySourceAST t, int visit, String opening, String subsequent, String closing) {
      if (visit == 1 && opening != null) {
         this.print(t, visit, opening);
      }

      if (visit == 3 && subsequent != null) {
         this.print(t, visit, subsequent);
      }

      if (visit == 4 && closing != null) {
         this.print(t, visit, closing);
      }

   }

   protected void print(GroovySourceAST t, int visit, String value) {
      if (visit == 1) {
         this.printNewlineAndIndent(t, visit);
      }

      if (visit == 4) {
         this.printNewlineAndIndent(t, visit);
      }

      this.out.print(value);
   }

   protected void printNewlineAndIndent(GroovySourceAST t, int visit) {
      int currentLine = t.getLine();
      if (this.lastLinePrinted == 0) {
         this.lastLinePrinted = currentLine;
      }

      if (this.lastLinePrinted != currentLine) {
         if (this.newLines && (visit != 1 || t.getType() != 7)) {
            int i;
            for(i = this.lastLinePrinted; i < currentLine; ++i) {
               this.out.println();
            }

            if (this.lastLinePrinted > currentLine) {
               this.out.println();
               this.lastLinePrinted = currentLine;
            }

            if (visit == 1 || visit == 4 && this.lastLinePrinted > currentLine) {
               for(i = 0; i < this.tabLevel; ++i) {
                  this.out.print("    ");
               }
            }
         }

         this.lastLinePrinted = Math.max(currentLine, this.lastLinePrinted);
      }

   }

   public void push(GroovySourceAST t) {
      this.stack.push(t);
   }

   public GroovySourceAST pop() {
      return !this.stack.empty() ? (GroovySourceAST)this.stack.pop() : null;
   }

   private GroovySourceAST getParentNode() {
      Object currentNode = this.stack.pop();
      Object parentNode = this.stack.peek();
      this.stack.push(currentNode);
      return (GroovySourceAST)parentNode;
   }
}
