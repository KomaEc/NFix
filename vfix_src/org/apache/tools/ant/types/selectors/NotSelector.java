package org.apache.tools.ant.types.selectors;

public class NotSelector extends NoneSelector {
   public NotSelector() {
   }

   public NotSelector(FileSelector other) {
      this();
      this.appendSelector(other);
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (this.hasSelectors()) {
         buf.append("{notselect: ");
         buf.append(super.toString());
         buf.append("}");
      }

      return buf.toString();
   }

   public void verifySettings() {
      if (this.selectorCount() != 1) {
         this.setError("One and only one selector is allowed within the <not> tag");
      }

   }
}
