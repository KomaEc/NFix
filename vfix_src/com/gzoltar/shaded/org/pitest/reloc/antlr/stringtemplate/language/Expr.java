package com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language;

import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplate;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplateWriter;
import java.io.IOException;

public abstract class Expr {
   protected StringTemplate enclosingTemplate;
   protected String indentation = null;

   public Expr(StringTemplate enclosingTemplate) {
      this.enclosingTemplate = enclosingTemplate;
   }

   public abstract int write(StringTemplate var1, StringTemplateWriter var2) throws IOException;

   public StringTemplate getEnclosingTemplate() {
      return this.enclosingTemplate;
   }

   public String getIndentation() {
      return this.indentation;
   }

   public void setIndentation(String indentation) {
      this.indentation = indentation;
   }
}
