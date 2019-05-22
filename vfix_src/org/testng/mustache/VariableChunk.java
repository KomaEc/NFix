package org.testng.mustache;

public class VariableChunk extends BaseChunk {
   private String m_variable;

   public VariableChunk(Model model, String variable) {
      super(model);
      this.m_variable = variable;
   }

   public String compose() {
      String result = this.m_model.resolveValueToString(this.m_variable);
      this.p("VariableChunk returning: " + result);
      return result;
   }

   public String toString() {
      return "[VariableChunk " + this.m_variable + " model:" + this.m_model + "]";
   }
}
