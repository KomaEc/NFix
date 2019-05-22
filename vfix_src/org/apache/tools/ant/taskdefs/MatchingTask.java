package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.DifferentSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SelectorContainer;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.TypeSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

public abstract class MatchingTask extends Task implements SelectorContainer {
   protected FileSet fileset = new FileSet();

   public void setProject(Project project) {
      super.setProject(project);
      this.fileset.setProject(project);
   }

   public PatternSet.NameEntry createInclude() {
      return this.fileset.createInclude();
   }

   public PatternSet.NameEntry createIncludesFile() {
      return this.fileset.createIncludesFile();
   }

   public PatternSet.NameEntry createExclude() {
      return this.fileset.createExclude();
   }

   public PatternSet.NameEntry createExcludesFile() {
      return this.fileset.createExcludesFile();
   }

   public PatternSet createPatternSet() {
      return this.fileset.createPatternSet();
   }

   public void setIncludes(String includes) {
      this.fileset.setIncludes(includes);
   }

   public void XsetItems(String itemString) {
      this.log("The items attribute is deprecated. Please use the includes attribute.", 1);
      if (itemString != null && !itemString.equals("*") && !itemString.equals(".")) {
         StringTokenizer tok = new StringTokenizer(itemString, ", ");

         while(tok.hasMoreTokens()) {
            String pattern = tok.nextToken().trim();
            if (pattern.length() > 0) {
               this.createInclude().setName(pattern + "/**");
            }
         }
      } else {
         this.createInclude().setName("**");
      }

   }

   public void setExcludes(String excludes) {
      this.fileset.setExcludes(excludes);
   }

   public void XsetIgnore(String ignoreString) {
      this.log("The ignore attribute is deprecated.Please use the excludes attribute.", 1);
      if (ignoreString != null && ignoreString.length() > 0) {
         StringTokenizer tok = new StringTokenizer(ignoreString, ", ", false);

         while(tok.hasMoreTokens()) {
            this.createExclude().setName("**/" + tok.nextToken().trim() + "/**");
         }
      }

   }

   public void setDefaultexcludes(boolean useDefaultExcludes) {
      this.fileset.setDefaultexcludes(useDefaultExcludes);
   }

   protected DirectoryScanner getDirectoryScanner(File baseDir) {
      this.fileset.setDir(baseDir);
      return this.fileset.getDirectoryScanner(this.getProject());
   }

   public void setIncludesfile(File includesfile) {
      this.fileset.setIncludesfile(includesfile);
   }

   public void setExcludesfile(File excludesfile) {
      this.fileset.setExcludesfile(excludesfile);
   }

   public void setCaseSensitive(boolean isCaseSensitive) {
      this.fileset.setCaseSensitive(isCaseSensitive);
   }

   public void setFollowSymlinks(boolean followSymlinks) {
      this.fileset.setFollowSymlinks(followSymlinks);
   }

   public boolean hasSelectors() {
      return this.fileset.hasSelectors();
   }

   public int selectorCount() {
      return this.fileset.selectorCount();
   }

   public FileSelector[] getSelectors(Project p) {
      return this.fileset.getSelectors(p);
   }

   public Enumeration selectorElements() {
      return this.fileset.selectorElements();
   }

   public void appendSelector(FileSelector selector) {
      this.fileset.appendSelector(selector);
   }

   public void addSelector(SelectSelector selector) {
      this.fileset.addSelector(selector);
   }

   public void addAnd(AndSelector selector) {
      this.fileset.addAnd(selector);
   }

   public void addOr(OrSelector selector) {
      this.fileset.addOr(selector);
   }

   public void addNot(NotSelector selector) {
      this.fileset.addNot(selector);
   }

   public void addNone(NoneSelector selector) {
      this.fileset.addNone(selector);
   }

   public void addMajority(MajoritySelector selector) {
      this.fileset.addMajority(selector);
   }

   public void addDate(DateSelector selector) {
      this.fileset.addDate(selector);
   }

   public void addSize(SizeSelector selector) {
      this.fileset.addSize(selector);
   }

   public void addFilename(FilenameSelector selector) {
      this.fileset.addFilename(selector);
   }

   public void addCustom(ExtendSelector selector) {
      this.fileset.addCustom(selector);
   }

   public void addContains(ContainsSelector selector) {
      this.fileset.addContains(selector);
   }

   public void addPresent(PresentSelector selector) {
      this.fileset.addPresent(selector);
   }

   public void addDepth(DepthSelector selector) {
      this.fileset.addDepth(selector);
   }

   public void addDepend(DependSelector selector) {
      this.fileset.addDepend(selector);
   }

   public void addContainsRegexp(ContainsRegexpSelector selector) {
      this.fileset.addContainsRegexp(selector);
   }

   public void addDifferent(DifferentSelector selector) {
      this.fileset.addDifferent(selector);
   }

   public void addType(TypeSelector selector) {
      this.fileset.addType(selector);
   }

   public void addModified(ModifiedSelector selector) {
      this.fileset.addModified(selector);
   }

   public void add(FileSelector selector) {
      this.fileset.add(selector);
   }

   protected final FileSet getImplicitFileSet() {
      return this.fileset;
   }
}
