package polyglot.ext.jl.qq;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java_cup.runtime.Symbol;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.ast.TypeNode;
import polyglot.frontend.ExtensionInfo;
import polyglot.lex.Lexer;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;

public class QQ {
   protected ExtensionInfo ext;
   protected Position pos;
   protected static final int EXPR = 0;
   protected static final int STMT = 1;
   protected static final int TYPE = 2;
   protected static final int MEMB = 3;
   protected static final int DECL = 4;
   protected static final int FILE = 5;

   public QQ(ExtensionInfo ext) {
      this(ext, Position.COMPILER_GENERATED);
   }

   public QQ(ExtensionInfo ext, Position pos) {
      this.ext = ext;
      this.pos = pos;
   }

   private List list() {
      return Collections.EMPTY_LIST;
   }

   private List list(Object o1) {
      return this.list(new Object[]{o1});
   }

   private List list(Object o1, Object o2) {
      return this.list(new Object[]{o1, o2});
   }

   private List list(Object o1, Object o2, Object o3) {
      return this.list(new Object[]{o1, o2, o3});
   }

   private List list(Object o1, Object o2, Object o3, Object o4) {
      return this.list(new Object[]{o1, o2, o3, o4});
   }

   private List list(Object o1, Object o2, Object o3, Object o4, Object o5) {
      return this.list(new Object[]{o1, o2, o3, o4, o5});
   }

   private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return this.list(new Object[]{o1, o2, o3, o4, o5, o6});
   }

   private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return this.list(new Object[]{o1, o2, o3, o4, o5, o6, o7});
   }

   private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return this.list(new Object[]{o1, o2, o3, o4, o5, o6, o7, o8});
   }

   private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return this.list(new Object[]{o1, o2, o3, o4, o5, o6, o7, o8, o9});
   }

   private List list(Object[] os) {
      return Arrays.asList(os);
   }

   public SourceFile parseFile(String fmt) {
      return (SourceFile)this.parse(fmt, this.list(), 5);
   }

   public SourceFile parseFile(String fmt, Object o1) {
      return (SourceFile)this.parse(fmt, this.list(o1), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3, o4), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3, o4, o5), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8), 5);
   }

   public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return (SourceFile)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 5);
   }

   public SourceFile parseFile(String fmt, Object[] os) {
      return (SourceFile)this.parse(fmt, this.list(os), 5);
   }

   public SourceFile parseFile(String fmt, List subst) {
      return (SourceFile)this.parse(fmt, subst, 5);
   }

   public ClassDecl parseDecl(String fmt) {
      return (ClassDecl)this.parse(fmt, this.list(), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1) {
      return (ClassDecl)this.parse(fmt, this.list(o1), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3, o4), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3, o4, o5), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8), 4);
   }

   public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return (ClassDecl)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 4);
   }

   public ClassDecl parseDecl(String fmt, Object[] os) {
      return (ClassDecl)this.parse(fmt, this.list(os), 4);
   }

   public ClassDecl parseDecl(String fmt, List subst) {
      return (ClassDecl)this.parse(fmt, subst, 4);
   }

   public ClassMember parseMember(String fmt) {
      return (ClassMember)this.parse(fmt, this.list(), 3);
   }

   public ClassMember parseMember(String fmt, Object o1) {
      return (ClassMember)this.parse(fmt, this.list(o1), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3, o4), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3, o4, o5), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8), 3);
   }

   public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return (ClassMember)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 3);
   }

   public ClassMember parseMember(String fmt, Object[] os) {
      return (ClassMember)this.parse(fmt, this.list(os), 3);
   }

   public ClassMember parseMember(String fmt, List subst) {
      return (ClassMember)this.parse(fmt, subst, 3);
   }

   public Expr parseExpr(String fmt) {
      return (Expr)this.parse(fmt, this.list(), 0);
   }

   public Expr parseExpr(String fmt, Object o1) {
      return (Expr)this.parse(fmt, this.list(o1), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2) {
      return (Expr)this.parse(fmt, this.list(o1, o2), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3, o4), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3, o4, o5), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8), 0);
   }

   public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return (Expr)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 0);
   }

   public Expr parseExpr(String fmt, Object[] os) {
      return (Expr)this.parse(fmt, this.list(os), 0);
   }

   public Expr parseExpr(String fmt, List subst) {
      return (Expr)this.parse(fmt, subst, 0);
   }

   public Stmt parseStmt(String fmt) {
      return (Stmt)this.parse(fmt, this.list(), 1);
   }

   public Stmt parseStmt(String fmt, Object o1) {
      return (Stmt)this.parse(fmt, this.list(o1), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2) {
      return (Stmt)this.parse(fmt, this.list(o1, o2), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3, o4), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3, o4, o5), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8), 1);
   }

   public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return (Stmt)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 1);
   }

   public Stmt parseStmt(String fmt, Object[] os) {
      return (Stmt)this.parse(fmt, this.list(os), 1);
   }

   public Stmt parseStmt(String fmt, List subst) {
      return (Stmt)this.parse(fmt, subst, 1);
   }

   public TypeNode parseType(String fmt) {
      return (TypeNode)this.parse(fmt, this.list(), 2);
   }

   public TypeNode parseType(String fmt, Object o1) {
      return (TypeNode)this.parse(fmt, this.list(o1), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3, o4), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3, o4, o5), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8), 2);
   }

   public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
      return (TypeNode)this.parse(fmt, this.list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 2);
   }

   public TypeNode parseType(String fmt, Object[] os) {
      return (TypeNode)this.parse(fmt, this.list(os), 2);
   }

   public TypeNode parseType(String fmt, List subst) {
      return (TypeNode)this.parse(fmt, subst, 2);
   }

   protected Lexer lexer(String fmt, Position pos, List subst) {
      return new Lexer_c(fmt, pos, subst);
   }

   protected QQParser parser(Lexer lexer, TypeSystem ts, NodeFactory nf, ErrorQueue eq) {
      return new Grm(lexer, ts, nf, eq);
   }

   protected Node parse(String fmt, List subst, int kind) {
      TypeSystem ts = this.ext.typeSystem();
      NodeFactory nf = this.ext.nodeFactory();
      ErrorQueue eq = this.ext.compiler().errorQueue();
      ListIterator i = subst.listIterator();

      while(true) {
         while(i.hasNext()) {
            Object o = i.next();
            if (o instanceof Type) {
               Type t = (Type)o;
               i.set(nf.CanonicalTypeNode(t.position(), t));
            } else if (o instanceof List) {
               List l = (List)o;
               ListIterator j = l.listIterator();

               while(j.hasNext()) {
                  Object p = j.next();
                  if (p instanceof Type) {
                     Type t = (Type)p;
                     j.set(nf.CanonicalTypeNode(t.position(), t));
                  }
               }
            }
         }

         Position pos = this.pos;
         if (pos == Position.COMPILER_GENERATED) {
            pos = Position.compilerGenerated(3);
         }

         Lexer lexer = this.lexer(fmt, pos, subst);
         QQParser grm = this.parser(lexer, ts, nf, eq);
         if (Report.should_report((String)"qq", 1)) {
            Report.report(1, "qq: " + fmt);
            Report.report(1, "subst: " + subst);
         }

         try {
            Symbol var20;
            switch(kind) {
            case 0:
               var20 = grm.qq_expr();
               break;
            case 1:
               var20 = grm.qq_stmt();
               break;
            case 2:
               var20 = grm.qq_type();
               break;
            case 3:
               var20 = grm.qq_member();
               break;
            case 4:
               var20 = grm.qq_decl();
               break;
            case 5:
               var20 = grm.qq_file();
               break;
            default:
               throw new QQError("bad quasi-quoting kind: " + kind, pos);
            }

            if (var20 != null && var20.value instanceof Node) {
               Node n = (Node)var20.value;
               if (Report.should_report((String)"qq", 1)) {
                  Report.report(1, "result: " + n);
               }

               return n;
            }

            throw new QQError("Unable to parse: \"" + fmt + "\".", pos);
         } catch (IOException var13) {
            throw new QQError("Unable to parse: \"" + fmt + "\".", pos);
         } catch (RuntimeException var14) {
            throw var14;
         } catch (Exception var15) {
            throw new QQError("Unable to parse: \"" + fmt + "\"; " + var15.getMessage(), pos);
         }
      }
   }
}
