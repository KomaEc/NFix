package com.gzoltar.shaded.org.pitest.reloc.antlr.common;

public class TokenStreamRecognitionException extends TokenStreamException {
   public RecognitionException recog;

   public TokenStreamRecognitionException(RecognitionException var1) {
      super(var1.getMessage());
      this.recog = var1;
   }

   public String toString() {
      return this.recog.toString();
   }
}
