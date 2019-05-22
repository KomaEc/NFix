package org.testng.mustache;

public abstract class BaseChunk {
   protected Model m_model;

   public BaseChunk(Model model) {
      this.m_model = model;
   }

   protected void p(String string) {
   }

   abstract String compose();
}
