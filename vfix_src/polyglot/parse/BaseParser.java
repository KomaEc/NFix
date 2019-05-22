package polyglot.parse;

import java.io.IOException;
import java.util.List;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import polyglot.ast.AmbExpr;
import polyglot.ast.AmbPrefix;
import polyglot.ast.AmbReceiver;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.TypeNode;
import polyglot.lex.Lexer;
import polyglot.lex.Token;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;

public abstract class BaseParser extends lr_parser {
   public final Lexer lexer;
   public final ErrorQueue eq;
   public final TypeSystem ts;
   public final NodeFactory nf;
   protected Position prev_pos;
   protected Position position;

   public BaseParser(Lexer l, TypeSystem t, NodeFactory n, ErrorQueue q) {
      this.lexer = l;
      this.eq = q;
      this.ts = t;
      this.nf = n;
      this.prev_pos = Position.COMPILER_GENERATED;
      this.position = Position.COMPILER_GENERATED;
   }

   public Symbol nextSymbol() throws IOException {
      Token t = this.lexer.nextToken();
      this.position = this.prev_pos;
      this.prev_pos = t.getPosition();
      return t.symbol();
   }

   public Position position() {
      return this.position;
   }

   public void report_fatal_error(String message, Object info) throws Exception {
      this.report_error(message, info);
      this.die();
   }

   public void die(String msg, Position pos) throws Exception {
      this.report_fatal_error(msg, pos);
   }

   public void die(Position pos) throws Exception {
      this.report_fatal_error("Syntax error.", pos);
   }

   public void die() throws Exception {
      this.done_parsing();
      throw new Exception();
   }

   protected Position posForObject(Object o) {
      if (o instanceof Node) {
         return this.pos((Node)o);
      } else if (o instanceof Token) {
         return this.pos((Token)o);
      } else if (o instanceof Type) {
         return this.pos((Type)o);
      } else if (o instanceof List) {
         return this.pos((List)o);
      } else {
         return o instanceof VarDeclarator ? this.pos((VarDeclarator)o) : null;
      }
   }

   public Position pos(Object first, Object last) {
      return this.pos(first, last, first);
   }

   public Position pos(Object first, Object last, Object noEndDefault) {
      Position fpos = this.posForObject(first);
      Position epos = this.posForObject(last);
      if (fpos != null && epos != null) {
         return epos.endColumn() != -2 ? new Position(fpos, epos) : this.posForObject(noEndDefault);
      } else {
         return null;
      }
   }

   public Position pos(Token t) {
      return t == null ? null : t.getPosition();
   }

   public Position pos(Type n) {
      return n == null ? null : n.position();
   }

   public Position pos(List l) {
      return l != null && !l.isEmpty() ? this.pos(l.get(0), l.get(l.size() - 1)) : null;
   }

   public Position pos(VarDeclarator n) {
      return n == null ? null : n.pos;
   }

   public Position pos(Node n) {
      return n == null ? null : n.position();
   }

   public TypeNode array(TypeNode n, int dims) throws Exception {
      if (dims > 0) {
         if (n instanceof CanonicalTypeNode) {
            Type t = ((CanonicalTypeNode)n).type();
            return this.nf.CanonicalTypeNode(this.pos((Node)n), this.ts.arrayOf(t, dims));
         } else {
            return this.nf.ArrayTypeNode(this.pos((Node)n), this.array(n, dims - 1));
         }
      } else {
         return n;
      }
   }

   protected QualifierNode prefixToQualifier(Prefix p) throws Exception {
      if (p instanceof TypeNode) {
         return this.typeToQualifier((TypeNode)p);
      } else if (p instanceof Expr) {
         return this.exprToQualifier((Expr)p);
      } else if (p instanceof AmbReceiver) {
         AmbReceiver a = (AmbReceiver)p;
         return a.prefix() != null ? this.nf.AmbQualifierNode(this.pos((Node)p), this.prefixToQualifier(a.prefix()), a.name()) : this.nf.AmbQualifierNode(this.pos((Node)p), a.name());
      } else if (p instanceof AmbPrefix) {
         AmbPrefix a = (AmbPrefix)p;
         return a.prefix() != null ? this.nf.AmbQualifierNode(this.pos((Node)p), this.prefixToQualifier(a.prefix()), a.name()) : this.nf.AmbQualifierNode(this.pos((Node)p), a.name());
      } else {
         this.die(this.pos((Node)p));
         return null;
      }
   }

   protected QualifierNode typeToQualifier(TypeNode t) throws Exception {
      if (t instanceof AmbTypeNode) {
         AmbTypeNode a = (AmbTypeNode)t;
         return a.qualifier() != null ? this.nf.AmbQualifierNode(this.pos((Node)t), a.qual(), a.name()) : this.nf.AmbQualifierNode(this.pos((Node)t), a.name());
      } else {
         this.die(this.pos((Node)t));
         return null;
      }
   }

   protected QualifierNode exprToQualifier(Expr e) throws Exception {
      if (e instanceof AmbExpr) {
         AmbExpr a = (AmbExpr)e;
         return this.nf.AmbQualifierNode(this.pos((Node)e), a.name());
      } else if (e instanceof Field) {
         Field f = (Field)e;
         Receiver r = f.target();
         return this.nf.AmbQualifierNode(this.pos((Node)e), this.prefixToQualifier(r), f.name());
      } else {
         this.die(this.pos((Node)e));
         return null;
      }
   }

   public TypeNode exprToType(Expr e) throws Exception {
      if (e instanceof AmbExpr) {
         AmbExpr a = (AmbExpr)e;
         return this.nf.AmbTypeNode(this.pos((Node)e), a.name());
      } else if (e instanceof Field) {
         Field f = (Field)e;
         Receiver r = f.target();
         return this.nf.AmbTypeNode(this.pos((Node)e), this.prefixToQualifier(r), f.name());
      } else {
         this.die(this.pos((Node)e));
         return null;
      }
   }
}
