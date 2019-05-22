package org.codehaus.plexus.util.interpolation;

import java.util.List;

/** @deprecated */
public class RegexBasedInterpolator extends hidden.org.codehaus.plexus.interpolation.RegexBasedInterpolator implements Interpolator {
   public RegexBasedInterpolator() {
   }

   public RegexBasedInterpolator(List valueSources) {
      super(valueSources);
   }

   public RegexBasedInterpolator(String startRegex, String endRegex, List valueSources) {
      super(startRegex, endRegex, valueSources);
   }

   public RegexBasedInterpolator(String startRegex, String endRegex) {
      super(startRegex, endRegex);
   }

   public void addValueSource(ValueSource valueSource) {
      super.addValueSource(valueSource);
   }

   public void removeValuesSource(ValueSource valueSource) {
      super.removeValuesSource(valueSource);
   }
}
