package polyglot.frontend;

import java.io.IOException;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import polyglot.ast.Node;
import polyglot.ast.SourceFile;
import polyglot.util.ErrorQueue;

public class CupParser implements Parser {
   lr_parser grm;
   Source source;
   ErrorQueue eq;

   public CupParser(lr_parser grm, Source source, ErrorQueue eq) {
      this.grm = grm;
      this.source = source;
      this.eq = eq;
   }

   public Node parse() {
      try {
         Symbol sym = this.grm.parse();
         if (sym != null && sym.value instanceof Node) {
            SourceFile sf = (SourceFile)sym.value;
            return sf.source(this.source);
         }

         this.eq.enqueue(4, "Unable to parse " + this.source.name() + ".");
      } catch (IOException var3) {
         this.eq.enqueue(2, var3.getMessage());
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         if (var5.getMessage() != null) {
            this.eq.enqueue(4, var5.getMessage());
         }
      }

      return null;
   }

   public String toString() {
      return "CupParser(" + this.grm.getClass().getName() + ")";
   }
}
