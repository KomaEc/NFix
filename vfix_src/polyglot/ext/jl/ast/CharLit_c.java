package polyglot.ext.jl.ast;

import polyglot.ast.CharLit;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class CharLit_c extends NumLit_c implements CharLit {
   public CharLit_c(Position pos, char value) {
      super(pos, (long)value);
   }

   public char value() {
      return (char)((int)this.longValue());
   }

   public CharLit value(char value) {
      CharLit_c n = (CharLit_c)this.copy();
      n.value = (long)value;
      return n;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      return this.type(tc.typeSystem().Char());
   }

   public String toString() {
      return "'" + StringUtil.escape((char)((int)this.value)) + "'";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("'");
      w.write(StringUtil.escape((char)((int)this.value)));
      w.write("'");
   }

   public Object constantValue() {
      return new Character((char)((int)this.value));
   }
}
