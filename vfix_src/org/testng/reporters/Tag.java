package org.testng.reporters;

import java.util.Properties;

class Tag {
   public final String tagName;
   public final String indent;
   public final Properties properties;

   public Tag(String ind, String n, Properties p) {
      this.tagName = n;
      this.indent = ind;
      this.properties = p;
   }

   public String toString() {
      return this.tagName;
   }
}
