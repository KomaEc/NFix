package org.testng.internal;

public class TestNGProperty {
   private String m_commandLineName = null;
   private String m_name = null;
   private String m_documentation = null;
   private String m_default = null;

   public TestNGProperty(String clName, String name, String doc, String def) {
      this.init(clName, name, doc, def);
   }

   public TestNGProperty(String name, String doc, String def) {
      this.init(name, name, doc, def);
   }

   private void init(String clName, String name, String doc, String def) {
      this.m_commandLineName = clName;
      this.m_name = name;
      this.m_documentation = doc;
      this.m_default = def;
   }

   public String getDefault() {
      return this.m_default;
   }

   public String getDocumentation() {
      return this.m_documentation;
   }

   public String getName() {
      return this.m_name;
   }

   public String getCommandLineName() {
      return this.m_commandLineName;
   }
}
