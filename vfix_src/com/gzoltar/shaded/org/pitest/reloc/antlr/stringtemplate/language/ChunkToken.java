package com.gzoltar.shaded.org.pitest.reloc.antlr.stringtemplate.language;

import com.gzoltar.shaded.org.pitest.reloc.antlr.common.CommonToken;

public class ChunkToken extends CommonToken {
   protected String indentation;

   public ChunkToken() {
   }

   public ChunkToken(int type, String text, String indentation) {
      super(type, text);
      this.setIndentation(indentation);
   }

   public String getIndentation() {
      return this.indentation;
   }

   public void setIndentation(String indentation) {
      this.indentation = indentation;
   }

   public String toString() {
      return super.toString() + "<indent='" + this.indentation + "'>";
   }
}
