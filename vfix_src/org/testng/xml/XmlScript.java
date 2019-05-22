package org.testng.xml;

import org.testng.xml.dom.TagContent;

public class XmlScript {
   private String m_language;
   private String m_script;

   public void setLanguage(String language) {
      this.m_language = language;
   }

   @TagContent(
      name = "script"
   )
   public void setScript(String script) {
      this.m_script = script;
   }

   public String getScript() {
      return this.m_script;
   }

   public String getLanguage() {
      return this.m_language;
   }
}
