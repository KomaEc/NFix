package com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CommonAST;
import com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.StringTemplate;

public class StringTemplateAST extends CommonAST {
   protected StringTemplate st = null;

   public StringTemplateAST() {
   }

   public StringTemplateAST(int type, String text) {
      this.setType(type);
      this.setText(text);
   }

   public StringTemplate getStringTemplate() {
      return this.st;
   }

   public void setStringTemplate(StringTemplate st) {
      this.st = st;
   }
}
