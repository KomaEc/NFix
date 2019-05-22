package com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language;

import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplate;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplateWriter;
import java.io.IOException;

public class StringRef extends Expr {
   String str;

   public StringRef(StringTemplate enclosingTemplate, String str) {
      super(enclosingTemplate);
      this.str = str;
   }

   public int write(StringTemplate self, StringTemplateWriter out) throws IOException {
      if (this.str != null) {
         int n = out.write(this.str);
         return n;
      } else {
         return 0;
      }
   }

   public String toString() {
      return this.str != null ? this.str : "";
   }
}
