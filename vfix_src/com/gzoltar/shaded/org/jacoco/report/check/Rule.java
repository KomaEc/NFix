package com.gzoltar.shaded.org.jacoco.report.check;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.runtime.WildcardMatcher;
import java.util.ArrayList;
import java.util.List;

public final class Rule {
   private ICoverageNode.ElementType element;
   private String includes;
   private String excludes;
   private List<Limit> limits;
   private WildcardMatcher includesMatcher;
   private WildcardMatcher excludesMatcher;

   public Rule() {
      this.element = ICoverageNode.ElementType.BUNDLE;
      this.limits = new ArrayList();
      this.setIncludes("*");
      this.setExcludes("");
   }

   public ICoverageNode.ElementType getElement() {
      return this.element;
   }

   public void setElement(ICoverageNode.ElementType elementType) {
      this.element = elementType;
   }

   public String getIncludes() {
      return this.includes;
   }

   public void setIncludes(String includes) {
      this.includes = includes;
      this.includesMatcher = new WildcardMatcher(includes);
   }

   public String getExcludes() {
      return this.excludes;
   }

   public void setExcludes(String excludes) {
      this.excludes = excludes;
      this.excludesMatcher = new WildcardMatcher(excludes);
   }

   public List<Limit> getLimits() {
      return this.limits;
   }

   public void setLimits(List<Limit> limits) {
      this.limits = limits;
   }

   public Limit createLimit() {
      Limit limit = new Limit();
      this.limits.add(limit);
      return limit;
   }

   boolean matches(String name) {
      return this.includesMatcher.matches(name) && !this.excludesMatcher.matches(name);
   }
}
