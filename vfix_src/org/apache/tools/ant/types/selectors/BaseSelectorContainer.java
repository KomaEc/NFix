package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

public abstract class BaseSelectorContainer extends BaseSelector implements SelectorContainer {
   private Vector selectorsList = new Vector();

   public boolean hasSelectors() {
      return !this.selectorsList.isEmpty();
   }

   public int selectorCount() {
      return this.selectorsList.size();
   }

   public FileSelector[] getSelectors(Project p) {
      FileSelector[] result = new FileSelector[this.selectorsList.size()];
      this.selectorsList.copyInto(result);
      return result;
   }

   public Enumeration selectorElements() {
      return this.selectorsList.elements();
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      Enumeration e = this.selectorElements();
      if (e.hasMoreElements()) {
         while(e.hasMoreElements()) {
            buf.append(e.nextElement().toString());
            if (e.hasMoreElements()) {
               buf.append(", ");
            }
         }
      }

      return buf.toString();
   }

   public void appendSelector(FileSelector selector) {
      this.selectorsList.addElement(selector);
   }

   public void validate() {
      this.verifySettings();
      String errmsg = this.getError();
      if (errmsg != null) {
         throw new BuildException(errmsg);
      } else {
         Enumeration e = this.selectorElements();

         while(e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof BaseSelector) {
               ((BaseSelector)o).validate();
            }
         }

      }
   }

   public abstract boolean isSelected(File var1, String var2, File var3);

   public void addSelector(SelectSelector selector) {
      this.appendSelector(selector);
   }

   public void addAnd(AndSelector selector) {
      this.appendSelector(selector);
   }

   public void addOr(OrSelector selector) {
      this.appendSelector(selector);
   }

   public void addNot(NotSelector selector) {
      this.appendSelector(selector);
   }

   public void addNone(NoneSelector selector) {
      this.appendSelector(selector);
   }

   public void addMajority(MajoritySelector selector) {
      this.appendSelector(selector);
   }

   public void addDate(DateSelector selector) {
      this.appendSelector(selector);
   }

   public void addSize(SizeSelector selector) {
      this.appendSelector(selector);
   }

   public void addFilename(FilenameSelector selector) {
      this.appendSelector(selector);
   }

   public void addCustom(ExtendSelector selector) {
      this.appendSelector(selector);
   }

   public void addContains(ContainsSelector selector) {
      this.appendSelector(selector);
   }

   public void addPresent(PresentSelector selector) {
      this.appendSelector(selector);
   }

   public void addDepth(DepthSelector selector) {
      this.appendSelector(selector);
   }

   public void addDepend(DependSelector selector) {
      this.appendSelector(selector);
   }

   public void addDifferent(DifferentSelector selector) {
      this.appendSelector(selector);
   }

   public void addType(TypeSelector selector) {
      this.appendSelector(selector);
   }

   public void addContainsRegexp(ContainsRegexpSelector selector) {
      this.appendSelector(selector);
   }

   public void addModified(ModifiedSelector selector) {
      this.appendSelector(selector);
   }

   public void add(FileSelector selector) {
      this.appendSelector(selector);
   }
}
