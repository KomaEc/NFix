package com.gzoltar.shaded.org.pitest.coverage;

import java.util.Collection;

public class BlockCoverage {
   private final BlockLocation block;
   private final Collection<String> tests;

   public BlockCoverage(BlockLocation block, Collection<String> tests) {
      this.block = block;
      this.tests = tests;
   }

   public BlockLocation getBlock() {
      return this.block;
   }

   public Collection<String> getTests() {
      return this.tests;
   }
}
