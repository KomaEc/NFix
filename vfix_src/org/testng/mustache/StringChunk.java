package org.testng.mustache;

public class StringChunk extends BaseChunk {
   private String m_string;

   public StringChunk(Model model, String string) {
      super(model);
      this.m_string = string;
   }

   public String compose() {
      return this.m_string;
   }

   public String toString() {
      return "[StringChunk " + this.m_string + "]";
   }
}
