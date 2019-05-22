package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.StringLit;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class StringLit_c extends Lit_c implements StringLit {
   protected String value;
   protected int MAX_LENGTH = 60;

   public StringLit_c(Position pos, String value) {
      super(pos);
      this.value = value;
   }

   public String value() {
      return this.value;
   }

   public StringLit value(String value) {
      StringLit_c n = (StringLit_c)this.copy();
      n.value = value;
      return n;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      return this.type(tc.typeSystem().String());
   }

   public String toString() {
      return StringUtil.unicodeEscape(this.value).length() > 11 ? "\"" + StringUtil.unicodeEscape(this.value).substring(0, 8) + "...\"" : "\"" + StringUtil.unicodeEscape(this.value) + "\"";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      List l = this.breakupString();
      if (l.size() > 1) {
         w.write("(");
      }

      Iterator i = l.iterator();

      String s;
      while(i.hasNext()) {
         s = (String)i.next();
         w.begin(0);
      }

      i = l.iterator();

      while(i.hasNext()) {
         s = (String)i.next();
         w.write("\"");
         w.write(StringUtil.escape(s));
         w.write("\"");
         w.end();
         if (i.hasNext()) {
            w.write(" +");
            w.allowBreak(0, " ");
         }
      }

      if (l.size() > 1) {
         w.write(")");
      }

   }

   public List breakupString() {
      List result = new LinkedList();
      int n = this.value.length();

      int j;
      for(int i = 0; i < n; i = j) {
         int len = 0;

         for(j = i; j < n; ++j) {
            char c = this.value.charAt(j);
            int k = StringUtil.unicodeEscape(c).length();
            if (len + k > this.MAX_LENGTH) {
               break;
            }

            len += k;
         }

         result.add(this.value.substring(i, j));
      }

      if (result.isEmpty()) {
         if (!this.value.equals("")) {
            throw new InternalCompilerError("breakupString failed");
         }

         result.add(this.value);
      }

      return result;
   }

   public Object constantValue() {
      return this.value;
   }
}
