package org.apache.tools.ant.types.selectors;

import java.util.Enumeration;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

public interface SelectorContainer {
   boolean hasSelectors();

   int selectorCount();

   FileSelector[] getSelectors(Project var1);

   Enumeration selectorElements();

   void appendSelector(FileSelector var1);

   void addSelector(SelectSelector var1);

   void addAnd(AndSelector var1);

   void addOr(OrSelector var1);

   void addNot(NotSelector var1);

   void addNone(NoneSelector var1);

   void addMajority(MajoritySelector var1);

   void addDate(DateSelector var1);

   void addSize(SizeSelector var1);

   void addFilename(FilenameSelector var1);

   void addCustom(ExtendSelector var1);

   void addContains(ContainsSelector var1);

   void addPresent(PresentSelector var1);

   void addDepth(DepthSelector var1);

   void addDepend(DependSelector var1);

   void addContainsRegexp(ContainsRegexpSelector var1);

   void addType(TypeSelector var1);

   void addDifferent(DifferentSelector var1);

   void addModified(ModifiedSelector var1);

   void add(FileSelector var1);
}
