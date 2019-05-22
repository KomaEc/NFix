package polyglot.ext.jl.parse;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FloatLit;
import polyglot.ast.For;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Import;
import polyglot.ast.IntLit;
import polyglot.ast.Labeled;
import polyglot.ast.Lit;
import polyglot.ast.MethodDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.PackageNode;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.SourceFile;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Synchronized;
import polyglot.ast.Throw;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.lex.BooleanLiteral;
import polyglot.lex.CharacterLiteral;
import polyglot.lex.DoubleLiteral;
import polyglot.lex.FloatLiteral;
import polyglot.lex.Identifier;
import polyglot.lex.IntegerLiteral;
import polyglot.lex.LongLiteral;
import polyglot.lex.NullLiteral;
import polyglot.lex.StringLiteral;
import polyglot.lex.Token;
import polyglot.parse.VarDeclarator;
import polyglot.types.Flags;
import polyglot.util.Position;
import polyglot.util.TypedList;

class CUP$Grm$actions {
   private final Grm parser;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;
   // $FF: synthetic field
   static Class class$polyglot$ast$Catch;
   // $FF: synthetic field
   static Class class$polyglot$ast$Eval;
   // $FF: synthetic field
   static Class class$polyglot$ast$ForUpdate;
   // $FF: synthetic field
   static Class class$polyglot$ast$ForInit;
   // $FF: synthetic field
   static Class class$polyglot$ast$Case;
   // $FF: synthetic field
   static Class class$polyglot$ast$SwitchElement;
   // $FF: synthetic field
   static Class class$polyglot$ast$Stmt;
   // $FF: synthetic field
   static Class class$polyglot$ast$ClassMember;
   // $FF: synthetic field
   static Class class$polyglot$ast$TypeNode;
   // $FF: synthetic field
   static Class class$polyglot$ast$Formal;
   // $FF: synthetic field
   static Class class$polyglot$parse$VarDeclarator;
   // $FF: synthetic field
   static Class class$polyglot$ast$TopLevelDecl;
   // $FF: synthetic field
   static Class class$polyglot$ast$Import;

   CUP$Grm$actions(Grm parser) {
      this.parser = parser;
   }

   public final Symbol CUP$Grm$do_action(int CUP$Grm$act_num, lr_parser CUP$Grm$parser, Stack CUP$Grm$stack, int CUP$Grm$top) throws Exception {
      Symbol CUP$Grm$result;
      Assign.Operator RESULT;
      int aleft;
      int aright;
      Expr a;
      int bleft;
      int bright;
      int cleft;
      int cright;
      Expr c;
      int cleft;
      int cright;
      Expr c;
      int dleft;
      int dright;
      Token d;
      int dleft;
      int dright;
      Stmt d;
      ArrayAccess a;
      Field a;
      Name a;
      Expr b;
      TypeNode b;
      Integer b;
      Token d;
      Expr RESULT;
      Identifier b;
      List c;
      List c;
      Token e;
      Name a;
      ArrayInit c;
      Binary RESULT;
      Integer c;
      TypedList l;
      ClassBody d;
      List c;
      Token b;
      Token n;
      Block b;
      Identifier b;
      Stmt c;
      TypedList l;
      List b;
      ClassBody e;
      Stmt b;
      Unary a;
      TypeNode c;
      Integer a;
      Formal a;
      List a;
      Block a;
      Cast RESULT;
      Stmt a;
      Unary RESULT;
      TypeNode a;
      Call a;
      Block a;
      ArrayAccess RESULT;
      Identifier a;
      ConstructorCall a;
      Call RESULT;
      VarDeclarator b;
      Field RESULT;
      Integer RESULT;
      Integer b;
      NewArray RESULT;
      TypedList RESULT;
      New RESULT;
      Branch a;
      Stmt a;
      For a;
      While a;
      If a;
      Labeled a;
      Special RESULT;
      ClassLit RESULT;
      ClassDecl a;
      Assert RESULT;
      MethodDecl a;
      Try RESULT;
      Flags a;
      Branch RESULT;
      Name RESULT;
      VarDeclarator a;
      For RESULT;
      While RESULT;
      Case RESULT;
      If RESULT;
      Labeled RESULT;
      Empty RESULT;
      Import a;
      PackageNode a;
      List RESULT;
      LongLiteral a;
      IntegerLiteral a;
      Block RESULT;
      ArrayInit RESULT;
      SourceFile a;
      ClassBody RESULT;
      ClassDecl RESULT;
      ConstructorCall RESULT;
      Formal RESULT;
      MethodDecl RESULT;
      Flags RESULT;
      Import RESULT;
      SourceFile RESULT;
      TypeNode RESULT;
      CanonicalTypeNode RESULT;
      IntLit RESULT;
      FloatLit RESULT;
      switch(CUP$Grm$act_num) {
      case 0:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (SourceFile)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         CUP$Grm$result = new Symbol(0, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         CUP$Grm$parser.done_parsing();
         return CUP$Grm$result;
      case 1:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (SourceFile)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         if (this.parser.eq.hasErrors()) {
            RESULT = null;
         } else {
            RESULT = a;
         }

         CUP$Grm$result = new Symbol(1, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 2:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (IntegerLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.IntLit(this.parser.pos(a), IntLit.INT, (long)a.getValue().intValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 3:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (LongLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.IntLit(this.parser.pos(a), IntLit.LONG, a.getValue().longValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 4:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         DoubleLiteral a = (DoubleLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.FloatLit(this.parser.pos(a), FloatLit.DOUBLE, a.getValue().doubleValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 5:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         FloatLiteral a = (FloatLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.FloatLit(this.parser.pos(a), FloatLit.FLOAT, (double)a.getValue().floatValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 6:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         BooleanLiteral a = (BooleanLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Lit RESULT = this.parser.nf.BooleanLit(this.parser.pos(a), a.getValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 7:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         CharacterLiteral a = (CharacterLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Lit RESULT = this.parser.nf.CharLit(this.parser.pos(a), a.getValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 8:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         StringLiteral a = (StringLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Lit RESULT = this.parser.nf.StringLit(this.parser.pos(a), a.getValue());
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 9:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         NullLiteral a = (NullLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Lit RESULT = this.parser.nf.NullLit(this.parser.pos(a));
         CUP$Grm$result = new Symbol(2, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 10:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (IntegerLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.IntLit(this.parser.pos(a), IntLit.INT, (long)a.getValue().intValue());
         CUP$Grm$result = new Symbol(3, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 11:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (LongLiteral)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.IntLit(this.parser.pos(a), IntLit.LONG, a.getValue().longValue());
         CUP$Grm$result = new Symbol(3, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 12:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(4, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 13:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(4, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 14:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(5, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 15:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Boolean());
         CUP$Grm$result = new Symbol(5, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 16:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(6, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 17:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(6, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 18:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Byte());
         CUP$Grm$result = new Symbol(7, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 19:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Char());
         CUP$Grm$result = new Symbol(7, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 20:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Short());
         CUP$Grm$result = new Symbol(7, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 21:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Int());
         CUP$Grm$result = new Symbol(7, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 22:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Long());
         CUP$Grm$result = new Symbol(7, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 23:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Float());
         CUP$Grm$result = new Symbol(8, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 24:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Double());
         CUP$Grm$result = new Symbol(8, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 25:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(9, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 26:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(9, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 27:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = a.toType();
         CUP$Grm$result = new Symbol(10, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 28:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(11, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 29:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(12, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 30:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.array(a, b);
         CUP$Grm$result = new Symbol(13, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 31:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.array(a.toType(), b);
         CUP$Grm$result = new Symbol(13, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 32:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(14, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 33:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(14, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 34:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = new Name(this.parser, this.parser.pos(a), a.getIdentifier());
         CUP$Grm$result = new Symbol(15, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 35:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = new Name(this.parser, this.parser.pos(a, b), a, b.getIdentifier());
         CUP$Grm$result = new Symbol(16, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 36:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (PackageNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.SourceFile(new Position(this.parser.lexer.file()), a, b, c);
         CUP$Grm$result = new Symbol(17, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 37:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.SourceFile(new Position(this.parser.lexer.file()), (PackageNode)null, Collections.EMPTY_LIST, a);
         CUP$Grm$result = new Symbol(17, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 38:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (PackageNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(18, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 39:
         RESULT = null;
         RESULT = null;
         CUP$Grm$result = new Symbol(18, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 40:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(20, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 41:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$Import == null ? (class$polyglot$ast$Import = class$("polyglot.ast.Import")) : class$polyglot$ast$Import, false);
         CUP$Grm$result = new Symbol(20, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 42:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(22, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 43:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$TopLevelDecl == null ? (class$polyglot$ast$TopLevelDecl = class$("polyglot.ast.TopLevelDecl")) : class$polyglot$ast$TopLevelDecl, false);
         CUP$Grm$result = new Symbol(22, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 44:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Import)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Import == null ? (class$polyglot$ast$Import = class$("polyglot.ast.Import")) : class$polyglot$ast$Import, false);
         l.add(a);
         CUP$Grm$result = new Symbol(21, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 45:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Import b = (Import)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(21, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 46:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$TopLevelDecl == null ? (class$polyglot$ast$TopLevelDecl = class$("polyglot.ast.TopLevelDecl")) : class$polyglot$ast$TopLevelDecl, false);
         if (a != null) {
            l.add(a);
         }

         CUP$Grm$result = new Symbol(23, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 47:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         ClassDecl b = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         if (b != null) {
            a.add(b);
         }

         CUP$Grm$result = new Symbol(23, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 48:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         PackageNode RESULT = a.toPackage();
         CUP$Grm$result = new Symbol(19, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 49:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Import)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(24, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 50:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Import)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(24, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 51:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Import(this.parser.pos(n, d), Import.CLASS, a.toString());
         CUP$Grm$result = new Symbol(25, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 52:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Import(this.parser.pos(n, d), Import.PACKAGE, a.toString());
         CUP$Grm$result = new Symbol(26, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 53:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(27, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 54:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(27, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 55:
         RESULT = null;
         RESULT = null;
         CUP$Grm$result = new Symbol(27, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 56:
         RESULT = null;
         RESULT = Flags.NONE;
         CUP$Grm$result = new Symbol(28, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 57:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(28, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 58:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(29, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 59:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Flags b = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         if (a.intersects(b)) {
            this.parser.die(this.parser.position());
         }

         RESULT = a.set(b);
         CUP$Grm$result = new Symbol(29, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 60:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.PUBLIC;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 61:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.PROTECTED;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 62:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.PRIVATE;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 63:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.STATIC;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 64:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.ABSTRACT;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 65:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.FINAL;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 66:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.NATIVE;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 67:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.SYNCHRONIZED;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 68:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.TRANSIENT;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 69:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.VOLATILE;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 70:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = Flags.STRICTFP;
         CUP$Grm$result = new Symbol(30, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 71:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (ClassBody)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassDecl(this.parser.pos(b, e), a, b.getIdentifier(), c, c, e);
         CUP$Grm$result = new Symbol(31, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 72:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(32, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 73:
         RESULT = null;
         CUP$Grm$result = new Symbol(33, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 74:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(33, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 75:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(34, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 76:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, false);
         CUP$Grm$result = new Symbol(35, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 77:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(35, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 78:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, false);
         l.add(a);
         CUP$Grm$result = new Symbol(36, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 79:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(36, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 80:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassBody(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(37, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 81:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         CUP$Grm$result = new Symbol(39, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 82:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(39, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 83:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(38, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 84:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.addAll(b);
         CUP$Grm$result = new Symbol(38, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 85:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 86:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(this.parser.nf.Initializer(this.parser.pos(a), Flags.STATIC, a));
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 87:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         ConstructorDecl a = (ConstructorDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(a);
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 88:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(this.parser.nf.Initializer(this.parser.pos(a), Flags.NONE, a));
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 89:
         RESULT = null;
         List l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 90:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 91:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         CUP$Grm$result = new Symbol(40, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 92:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(41, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 93:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (MethodDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(a);
         CUP$Grm$result = new Symbol(41, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 94:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (ClassBody)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         List l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(this.parser.nf.ClassDecl(this.parser.pos(b, e), a, b.getIdentifier(), c, c, e));
         CUP$Grm$result = new Symbol(41, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 95:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(a);
         CUP$Grm$result = new Symbol(41, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 96:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         Iterator i = c.iterator();

         while(i.hasNext()) {
            VarDeclarator d = (VarDeclarator)i.next();
            l.add(this.parser.nf.FieldDecl(this.parser.pos(b, e), a, this.parser.array(b, d.dims), d.name, d.init));
         }

         CUP$Grm$result = new Symbol(42, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 97:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$parse$VarDeclarator == null ? (class$polyglot$parse$VarDeclarator = class$("polyglot.parse.VarDeclarator")) : class$polyglot$parse$VarDeclarator, false);
         l.add(a);
         CUP$Grm$result = new Symbol(43, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 98:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(43, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 99:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(44, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 100:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.init = b;
         CUP$Grm$result = new Symbol(44, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 101:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         VarDeclarator RESULT = new VarDeclarator(this.parser.pos(a), a.getIdentifier());
         CUP$Grm$result = new Symbol(45, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 102:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         ++a.dims;
         CUP$Grm$result = new Symbol(45, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 103:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(46, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 104:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         ArrayInit a = (ArrayInit)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(46, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 105:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (MethodDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = (MethodDecl)a.body(a);
         CUP$Grm$result = new Symbol(47, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 106:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         Integer e = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         int fleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         int fright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         List f = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.MethodDecl(this.parser.pos(b, d, b), a, this.parser.array(b, e), b.getIdentifier(), c, f, (Block)null);
         CUP$Grm$result = new Symbol(48, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 107:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         List f = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.MethodDecl(this.parser.pos(b, d, b), a, this.parser.nf.CanonicalTypeNode(this.parser.pos(b), this.parser.ts.Void()), b.getIdentifier(), c, f, (Block)null);
         CUP$Grm$result = new Symbol(48, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 108:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, false);
         CUP$Grm$result = new Symbol(49, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 109:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(49, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 110:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Formal a = (Formal)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, false);
         l.add(a);
         CUP$Grm$result = new Symbol(50, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 111:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Formal)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(a);
         CUP$Grm$result = new Symbol(50, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 112:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Formal(this.parser.pos(a, b, b), Flags.NONE, this.parser.array(a, b.dims), b.name);
         CUP$Grm$result = new Symbol(51, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 113:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (VarDeclarator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Formal(this.parser.pos(a, b, b), Flags.FINAL, this.parser.array(a, b.dims), b.name);
         CUP$Grm$result = new Symbol(51, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 114:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, false);
         CUP$Grm$result = new Symbol(52, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 115:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(52, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 116:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(53, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 117:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, false);
         l.add(a);
         CUP$Grm$result = new Symbol(54, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 118:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(54, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 119:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(55, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 120:
         RESULT = null;
         RESULT = null;
         CUP$Grm$result = new Symbol(55, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 121:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(56, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 122:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Block d = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         ConstructorDecl RESULT = this.parser.nf.ConstructorDecl(this.parser.pos(a, d), a, a.toString(), c, c, d);
         CUP$Grm$result = new Symbol(57, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 123:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (ConstructorCall)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, false);
         l.add(a);
         l.addAll(c);
         RESULT = this.parser.nf.Block(this.parser.pos(n, e), (List)l);
         CUP$Grm$result = new Symbol(58, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 124:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (ConstructorCall)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Block(this.parser.pos(n, d), (Stmt)a);
         CUP$Grm$result = new Symbol(58, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 125:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         b.add(0, this.parser.nf.SuperCall(this.parser.pos(n, d), Collections.EMPTY_LIST));
         RESULT = this.parser.nf.Block(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(58, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 126:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Block(this.parser.pos(n, b), (Stmt)this.parser.nf.SuperCall(this.parser.pos(n, b), Collections.EMPTY_LIST));
         CUP$Grm$result = new Symbol(58, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 127:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ThisCall(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(59, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 128:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.SuperCall(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(59, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 129:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ThisCall(this.parser.pos(a, e, b), a, c);
         CUP$Grm$result = new Symbol(59, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 130:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.SuperCall(this.parser.pos(a, e, b), a, c);
         CUP$Grm$result = new Symbol(59, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 131:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         a = (Flags)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         ClassBody d = (ClassBody)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassDecl(this.parser.pos(b, d), a.Interface(), b.getIdentifier(), (TypeNode)null, c, d);
         CUP$Grm$result = new Symbol(60, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 132:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, false);
         CUP$Grm$result = new Symbol(61, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 133:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(61, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 134:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, false);
         l.add(a);
         CUP$Grm$result = new Symbol(62, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 135:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(62, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 136:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassBody(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(63, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 137:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         CUP$Grm$result = new Symbol(64, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 138:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(64, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 139:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(65, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 140:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.addAll(b);
         CUP$Grm$result = new Symbol(65, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 141:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(66, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 142:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (MethodDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(a);
         CUP$Grm$result = new Symbol(66, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 143:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(a);
         CUP$Grm$result = new Symbol(66, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 144:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, false);
         l.add(a);
         CUP$Grm$result = new Symbol(66, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 145:
         RESULT = null;
         RESULT = Collections.EMPTY_LIST;
         CUP$Grm$result = new Symbol(66, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 146:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(67, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 147:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (MethodDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         CUP$Grm$result = new Symbol(68, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 148:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ArrayInit(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(69, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 149:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ArrayInit(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(69, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 150:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ArrayInit(this.parser.pos(n, b));
         CUP$Grm$result = new Symbol(69, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 151:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ArrayInit(this.parser.pos(n, b));
         CUP$Grm$result = new Symbol(69, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 152:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, false);
         l.add(a);
         CUP$Grm$result = new Symbol(70, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 153:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(70, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 154:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Block(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(71, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 155:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Block(this.parser.pos(n), Collections.EMPTY_LIST);
         CUP$Grm$result = new Symbol(71, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 156:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, false);
         CUP$Grm$result = new Symbol(72, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 157:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(72, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 158:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, false);
         l.addAll(a);
         CUP$Grm$result = new Symbol(73, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 159:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.addAll(b);
         CUP$Grm$result = new Symbol(73, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 160:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(74, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 161:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, false);
         l.add(a);
         CUP$Grm$result = new Symbol(74, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 162:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ClassDecl)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, false);
         l.add(this.parser.nf.LocalClassDecl(this.parser.pos(a), a));
         CUP$Grm$result = new Symbol(74, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 163:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         CUP$Grm$result = new Symbol(75, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 164:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.variableDeclarators(a, b, Flags.NONE);
         CUP$Grm$result = new Symbol(76, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 165:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.variableDeclarators(a, b, Flags.FINAL);
         CUP$Grm$result = new Symbol(76, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 166:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 167:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Labeled)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 168:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (If)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 169:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (If)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 170:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (While)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 171:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (For)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 172:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Empty(this.parser.pos(n));
         CUP$Grm$result = new Symbol(77, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 173:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(78, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 174:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Labeled)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(78, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 175:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (If)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(78, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 176:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (While)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(78, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 177:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (For)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(78, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 178:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 179:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Empty a = (Empty)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 180:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 181:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Switch a = (Switch)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 182:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Do a = (Do)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 183:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Branch)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 184:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Branch)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 185:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Return a = (Return)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 186:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Synchronized a = (Synchronized)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 187:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Throw a = (Throw)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 188:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Try a = (Try)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 189:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Assert a = (Assert)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(79, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 190:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Empty(this.parser.pos(n));
         CUP$Grm$result = new Symbol(80, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 191:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Labeled(this.parser.pos(a, a), a.getIdentifier(), a);
         CUP$Grm$result = new Symbol(81, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 192:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Labeled(this.parser.pos(a, a), a.getIdentifier(), a);
         CUP$Grm$result = new Symbol(82, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 193:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Stmt RESULT = this.parser.nf.Eval(this.parser.pos(a, b), a);
         CUP$Grm$result = new Symbol(83, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 194:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 195:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 196:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 197:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 198:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 199:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Call)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 200:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(84, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 201:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.If(this.parser.pos(n, b), b, b);
         CUP$Grm$result = new Symbol(85, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 202:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.If(this.parser.pos(n, c), b, b, c);
         CUP$Grm$result = new Symbol(86, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 203:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.If(this.parser.pos(n, c), b, b, c);
         CUP$Grm$result = new Symbol(87, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 204:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Switch RESULT = this.parser.nf.Switch(this.parser.pos(n, c), b, c);
         CUP$Grm$result = new Symbol(88, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 205:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         a.addAll(b);
         CUP$Grm$result = new Symbol(89, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 206:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         CUP$Grm$result = new Symbol(89, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 207:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         CUP$Grm$result = new Symbol(89, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 208:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$SwitchElement == null ? (class$polyglot$ast$SwitchElement = class$("polyglot.ast.SwitchElement")) : class$polyglot$ast$SwitchElement, false);
         CUP$Grm$result = new Symbol(89, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 209:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(90, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 210:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.addAll(b);
         CUP$Grm$result = new Symbol(90, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 211:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         List l = new TypedList(new LinkedList(), class$polyglot$ast$SwitchElement == null ? (class$polyglot$ast$SwitchElement = class$("polyglot.ast.SwitchElement")) : class$polyglot$ast$SwitchElement, false);
         l.addAll(a);
         l.add(this.parser.nf.SwitchBlock(this.parser.pos(a, b), b));
         CUP$Grm$result = new Symbol(91, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 212:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Case a = (Case)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Case == null ? (class$polyglot$ast$Case = class$("polyglot.ast.Case")) : class$polyglot$ast$Case, false);
         l.add(a);
         CUP$Grm$result = new Symbol(92, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 213:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Case b = (Case)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(92, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 214:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Case(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(93, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 215:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Default(this.parser.pos(n, b));
         CUP$Grm$result = new Symbol(93, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 216:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.While(this.parser.pos(n, b), b, b);
         CUP$Grm$result = new Symbol(94, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 217:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.While(this.parser.pos(n, b), b, b);
         CUP$Grm$result = new Symbol(95, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 218:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         a = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Do RESULT = this.parser.nf.Do(this.parser.pos(n, e), a, c);
         CUP$Grm$result = new Symbol(96, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 219:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.For(this.parser.pos(n, e), b, c, c, d);
         CUP$Grm$result = new Symbol(97, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 220:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Stmt)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.For(this.parser.pos(n, e), b, c, c, d);
         CUP$Grm$result = new Symbol(98, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 8)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 221:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$ForInit == null ? (class$polyglot$ast$ForInit = class$("polyglot.ast.ForInit")) : class$polyglot$ast$ForInit, false);
         CUP$Grm$result = new Symbol(99, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 222:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(99, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 223:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(100, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 224:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$ForInit == null ? (class$polyglot$ast$ForInit = class$("polyglot.ast.ForInit")) : class$polyglot$ast$ForInit, false);
         l.addAll(a);
         CUP$Grm$result = new Symbol(100, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 225:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$ForUpdate == null ? (class$polyglot$ast$ForUpdate = class$("polyglot.ast.ForUpdate")) : class$polyglot$ast$ForUpdate, false);
         CUP$Grm$result = new Symbol(101, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 226:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(101, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 227:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(102, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 228:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Eval == null ? (class$polyglot$ast$Eval = class$("polyglot.ast.Eval")) : class$polyglot$ast$Eval, false);
         l.add(this.parser.nf.Eval(this.parser.pos(a), a));
         CUP$Grm$result = new Symbol(103, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 229:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(this.parser.nf.Eval(this.parser.pos(a, b, b), b));
         CUP$Grm$result = new Symbol(103, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 230:
         RESULT = null;
         RESULT = null;
         CUP$Grm$result = new Symbol(104, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 231:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = new Name(this.parser, this.parser.pos(a), a.getIdentifier());
         CUP$Grm$result = new Symbol(104, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 232:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         if (a == null) {
            RESULT = this.parser.nf.Break(this.parser.pos(n, d));
         } else {
            RESULT = this.parser.nf.Break(this.parser.pos(n, d), a.toString());
         }

         CUP$Grm$result = new Symbol(105, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 233:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         if (a == null) {
            RESULT = this.parser.nf.Continue(this.parser.pos(n, d));
         } else {
            RESULT = this.parser.nf.Continue(this.parser.pos(n, d), a.toString());
         }

         CUP$Grm$result = new Symbol(106, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 234:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Return RESULT = this.parser.nf.Return(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(107, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 235:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Throw RESULT = this.parser.nf.Throw(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(108, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 236:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Synchronized RESULT = this.parser.nf.Synchronized(this.parser.pos(n, b), b, b);
         CUP$Grm$result = new Symbol(109, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 237:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Try(this.parser.pos(n, c), a, c);
         CUP$Grm$result = new Symbol(110, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 238:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Block c = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Try(this.parser.pos(n, c), a, c, c);
         CUP$Grm$result = new Symbol(110, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 239:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$Catch == null ? (class$polyglot$ast$Catch = class$("polyglot.ast.Catch")) : class$polyglot$ast$Catch, false);
         CUP$Grm$result = new Symbol(111, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 240:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(111, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 241:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Catch a = (Catch)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Catch == null ? (class$polyglot$ast$Catch = class$("polyglot.ast.Catch")) : class$polyglot$ast$Catch, false);
         l.add(a);
         CUP$Grm$result = new Symbol(112, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 242:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Catch b = (Catch)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(112, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 243:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Formal)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Catch RESULT = this.parser.nf.Catch(this.parser.pos(n, b), a, b);
         CUP$Grm$result = new Symbol(113, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 244:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Block)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(114, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 245:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Assert(this.parser.pos(n, d), b);
         CUP$Grm$result = new Symbol(115, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 246:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Assert(this.parser.pos(n, e), b, c);
         CUP$Grm$result = new Symbol(115, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 247:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(116, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 248:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         NewArray a = (NewArray)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(116, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 249:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Lit a = (Lit)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 250:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.This(this.parser.pos(n));
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 251:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 252:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 253:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Field)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 254:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Call)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 255:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ArrayAccess)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 256:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassLit(this.parser.pos(a, b, b), a);
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 257:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassLit(this.parser.pos(n, b, b), this.parser.nf.CanonicalTypeNode(this.parser.pos(n), this.parser.ts.Void()));
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 258:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassLit(this.parser.pos(a, b, b), a);
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 259:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ClassLit(this.parser.pos(a, b, b), a.toType());
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 260:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.This(this.parser.pos(a, b, b), a.toType());
         CUP$Grm$result = new Symbol(117, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 261:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.New(this.parser.pos(n, e), b, c);
         CUP$Grm$result = new Symbol(118, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 262:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (ClassBody)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.New(this.parser.pos(n, d), b, c, d);
         CUP$Grm$result = new Symbol(118, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 263:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.New(this.parser.pos(a, e), a, a.toType(), c);
         CUP$Grm$result = new Symbol(118, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 264:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (ClassBody)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.New(this.parser.pos(a, d), a, a.toType(), c, d);
         CUP$Grm$result = new Symbol(118, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 265:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.New(this.parser.pos(a, e), a.toExpr(), a.toType(), c);
         CUP$Grm$result = new Symbol(118, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 6)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 266:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (ClassBody)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.New(this.parser.pos(a, d), a.toExpr(), a.toType(), c, d);
         CUP$Grm$result = new Symbol(118, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 267:
         RESULT = null;
         RESULT = new TypedList(new LinkedList(), class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, false);
         CUP$Grm$result = new Symbol(119, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 268:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(119, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 269:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, false);
         l.add(a);
         CUP$Grm$result = new Symbol(120, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 270:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(120, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 271:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.NewArray(this.parser.pos(n, c), b, c, c);
         CUP$Grm$result = new Symbol(121, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 272:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.NewArray(this.parser.pos(n, c), b, c, c);
         CUP$Grm$result = new Symbol(121, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 273:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (ArrayInit)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.NewArray(this.parser.pos(n, c), b, b, c);
         CUP$Grm$result = new Symbol(121, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 274:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (ArrayInit)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.NewArray(this.parser.pos(n, c), b, b, c);
         CUP$Grm$result = new Symbol(121, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 275:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         l = new TypedList(new LinkedList(), class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, false);
         l.add(a);
         CUP$Grm$result = new Symbol(122, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, l);
         return CUP$Grm$result;
      case 276:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         a.add(b);
         CUP$Grm$result = new Symbol(122, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 277:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = (Expr)b.position(this.parser.pos(n, d, b));
         CUP$Grm$result = new Symbol(123, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 278:
         RESULT = null;
         RESULT = new Integer(0);
         CUP$Grm$result = new Symbol(124, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 279:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(124, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 280:
         RESULT = null;
         RESULT = new Integer(1);
         CUP$Grm$result = new Symbol(125, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 281:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         RESULT = new Integer(a + 1);
         CUP$Grm$result = new Symbol(125, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 282:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Field(this.parser.pos(a, b, b), a, b.getIdentifier());
         CUP$Grm$result = new Symbol(126, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 283:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Field(this.parser.pos(b), this.parser.nf.Super(this.parser.pos(n)), b.getIdentifier());
         CUP$Grm$result = new Symbol(126, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 284:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Field(this.parser.pos(b), this.parser.nf.Super(this.parser.pos(b), a.toType()), b.getIdentifier());
         CUP$Grm$result = new Symbol(126, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 285:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Call(this.parser.pos(a, d), a.prefix == null ? null : a.prefix.toReceiver(), a.name, b);
         CUP$Grm$result = new Symbol(127, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 286:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Call(this.parser.pos(b, e), (Receiver)a, (String)b.getIdentifier(), (List)c);
         CUP$Grm$result = new Symbol(127, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 287:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         e = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Call(this.parser.pos(n, e, b), (Receiver)this.parser.nf.Super(this.parser.pos(n)), (String)b.getIdentifier(), (List)c);
         CUP$Grm$result = new Symbol(127, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 288:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 5)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (Identifier)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         c = (List)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         dleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         dright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Call(this.parser.pos(b, d), (Receiver)this.parser.nf.Super(this.parser.pos(b), a.toType()), (String)b.getIdentifier(), (List)c);
         CUP$Grm$result = new Symbol(127, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 7)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 289:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ArrayAccess(this.parser.pos(a, d), a.toExpr(), b);
         CUP$Grm$result = new Symbol(128, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 290:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         d = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.ArrayAccess(this.parser.pos(a, d), a, b);
         CUP$Grm$result = new Symbol(128, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 291:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(129, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 292:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = a.toExpr();
         CUP$Grm$result = new Symbol(129, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 293:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(129, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 294:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(129, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 295:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(a, b), a, Unary.POST_INC);
         CUP$Grm$result = new Symbol(130, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 296:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(a, b), a, Unary.POST_DEC);
         CUP$Grm$result = new Symbol(131, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 297:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(132, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 298:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Unary)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(132, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 299:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, b, b), Unary.POS, b);
         CUP$Grm$result = new Symbol(132, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 300:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, b, b), Unary.NEG, b);
         CUP$Grm$result = new Symbol(132, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 301:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Lit a = (Lit)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, a, a), (Unary.Operator)Unary.NEG, (Expr)a);
         CUP$Grm$result = new Symbol(132, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 302:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(132, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 303:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, b, b), Unary.PRE_INC, b);
         CUP$Grm$result = new Symbol(134, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 304:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, b, b), Unary.PRE_DEC, b);
         CUP$Grm$result = new Symbol(135, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 305:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(133, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 306:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, b, b), Unary.BIT_NOT, b);
         CUP$Grm$result = new Symbol(133, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 307:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Unary(this.parser.pos(n, b, b), Unary.NOT, b);
         CUP$Grm$result = new Symbol(133, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 308:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         Cast a = (Cast)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(133, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 309:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Cast(this.parser.pos(n, c, b), this.parser.array(b, b), c);
         CUP$Grm$result = new Symbol(136, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 310:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Cast(this.parser.pos(n, c, b), this.parser.exprToType(b), c);
         CUP$Grm$result = new Symbol(136, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 311:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         n = (Token)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 3)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Integer)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Cast(this.parser.pos(n, c, a), this.parser.array(a.toType(), b), c);
         CUP$Grm$result = new Symbol(136, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 312:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(137, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 313:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.MUL, b);
         CUP$Grm$result = new Symbol(137, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 314:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.DIV, b);
         CUP$Grm$result = new Symbol(137, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 315:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.MOD, b);
         CUP$Grm$result = new Symbol(137, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 316:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(138, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 317:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.ADD, b);
         CUP$Grm$result = new Symbol(138, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 318:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.SUB, b);
         CUP$Grm$result = new Symbol(138, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 319:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(139, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 320:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.SHL, b);
         CUP$Grm$result = new Symbol(139, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 321:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.SHR, b);
         CUP$Grm$result = new Symbol(139, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 322:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.USHR, b);
         CUP$Grm$result = new Symbol(139, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 323:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(140, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 324:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.LT, b);
         CUP$Grm$result = new Symbol(140, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 325:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.GT, b);
         CUP$Grm$result = new Symbol(140, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 326:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.LE, b);
         CUP$Grm$result = new Symbol(140, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 327:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.GE, b);
         CUP$Grm$result = new Symbol(140, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 328:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (TypeNode)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Expr RESULT = this.parser.nf.Instanceof(this.parser.pos(a, b), a, b);
         CUP$Grm$result = new Symbol(140, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 329:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(141, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 330:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.EQ, b);
         CUP$Grm$result = new Symbol(141, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 331:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.NE, b);
         CUP$Grm$result = new Symbol(141, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 332:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(142, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 333:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.BIT_AND, b);
         CUP$Grm$result = new Symbol(142, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 334:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(143, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 335:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.BIT_XOR, b);
         CUP$Grm$result = new Symbol(143, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 336:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(144, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 337:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.BIT_OR, b);
         CUP$Grm$result = new Symbol(144, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 338:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(145, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 339:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.COND_AND, b);
         CUP$Grm$result = new Symbol(145, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 340:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(146, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 341:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = this.parser.nf.Binary(this.parser.pos(a, b), a, Binary.COND_OR, b);
         CUP$Grm$result = new Symbol(146, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 342:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(147, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 343:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         b = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Expr RESULT = this.parser.nf.Conditional(this.parser.pos(a, c), a, b, c);
         CUP$Grm$result = new Symbol(147, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 4)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 344:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(148, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 345:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(148, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 346:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).value;
         bleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).left;
         bright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).right;
         Assign.Operator b = (Assign.Operator)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 1)).value;
         cleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         cright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         c = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         Expr RESULT = this.parser.nf.Assign(this.parser.pos(a, c), a, b, c);
         CUP$Grm$result = new Symbol(149, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 2)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 347:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Name)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         RESULT = a.toExpr();
         CUP$Grm$result = new Symbol(150, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 348:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Field)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(150, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 349:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (ArrayAccess)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(150, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 350:
         RESULT = null;
         RESULT = Assign.ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 351:
         RESULT = null;
         RESULT = Assign.MUL_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 352:
         RESULT = null;
         RESULT = Assign.DIV_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 353:
         RESULT = null;
         RESULT = Assign.MOD_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 354:
         RESULT = null;
         RESULT = Assign.ADD_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 355:
         RESULT = null;
         RESULT = Assign.SUB_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 356:
         RESULT = null;
         RESULT = Assign.SHL_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 357:
         RESULT = null;
         RESULT = Assign.SHR_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 358:
         RESULT = null;
         RESULT = Assign.USHR_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 359:
         RESULT = null;
         RESULT = Assign.BIT_AND_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 360:
         RESULT = null;
         RESULT = Assign.BIT_XOR_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 361:
         RESULT = null;
         RESULT = Assign.BIT_OR_ASSIGN;
         CUP$Grm$result = new Symbol(151, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 362:
         RESULT = null;
         RESULT = null;
         CUP$Grm$result = new Symbol(152, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, RESULT);
         return CUP$Grm$result;
      case 363:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(152, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 364:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(153, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      case 365:
         RESULT = null;
         aleft = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left;
         aright = ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right;
         a = (Expr)((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).value;
         CUP$Grm$result = new Symbol(154, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).left, ((Symbol)CUP$Grm$stack.elementAt(CUP$Grm$top - 0)).right, a);
         return CUP$Grm$result;
      default:
         throw new Exception("Invalid action number found in internal parse table");
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
