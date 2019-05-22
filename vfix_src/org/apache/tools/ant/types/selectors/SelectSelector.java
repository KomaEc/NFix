package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;
import org.apache.tools.ant.Project;

public class SelectSelector extends BaseSelectorContainer {
   private String ifProperty;
   private String unlessProperty;

   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (this.hasSelectors()) {
         buf.append("{select");
         if (this.ifProperty != null) {
            buf.append(" if: ");
            buf.append(this.ifProperty);
         }

         if (this.unlessProperty != null) {
            buf.append(" unless: ");
            buf.append(this.unlessProperty);
         }

         buf.append(" ");
         buf.append(super.toString());
         buf.append("}");
      }

      return buf.toString();
   }

   private SelectSelector getRef() {
      Object o = this.getCheckedRef(this.getClass(), "SelectSelector");
      return (SelectSelector)o;
   }

   public boolean hasSelectors() {
      return this.isReference() ? this.getRef().hasSelectors() : super.hasSelectors();
   }

   public int selectorCount() {
      return this.isReference() ? this.getRef().selectorCount() : super.selectorCount();
   }

   public FileSelector[] getSelectors(Project p) {
      return this.isReference() ? this.getRef().getSelectors(p) : super.getSelectors(p);
   }

   public Enumeration selectorElements() {
      return this.isReference() ? this.getRef().selectorElements() : super.selectorElements();
   }

   public void appendSelector(FileSelector selector) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         super.appendSelector(selector);
      }
   }

   public void verifySettings() {
      int cnt = this.selectorCount();
      if (cnt < 0 || cnt > 1) {
         this.setError("Only one selector is allowed within the <selector> tag");
      }

   }

   public boolean passesConditions() {
      if (this.ifProperty != null && this.getProject().getProperty(this.ifProperty) == null) {
         return false;
      } else {
         return this.unlessProperty == null || this.getProject().getProperty(this.unlessProperty) == null;
      }
   }

   public void setIf(String ifProperty) {
      this.ifProperty = ifProperty;
   }

   public void setUnless(String unlessProperty) {
      this.unlessProperty = unlessProperty;
   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      if (!this.passesConditions()) {
         return false;
      } else {
         Enumeration e = this.selectorElements();
         if (!e.hasMoreElements()) {
            return true;
         } else {
            FileSelector f = (FileSelector)e.nextElement();
            return f.isSelected(basedir, filename, file);
         }
      }
   }
}
