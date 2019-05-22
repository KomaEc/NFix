package com.gzoltar.shaded.org.pitest.junit;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

public class DescriptionFilter extends Filter {
   private final String desc;

   public DescriptionFilter(String description) {
      this.desc = description;
   }

   public boolean shouldRun(Description description) {
      return description.toString().equals(this.desc);
   }

   public String describe() {
      return this.desc;
   }

   public String toString() {
      return this.describe();
   }
}
