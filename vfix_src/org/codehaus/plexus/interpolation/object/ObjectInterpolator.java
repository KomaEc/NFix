package org.codehaus.plexus.interpolation.object;

import java.util.List;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.RecursionInterceptor;

public interface ObjectInterpolator {
   void interpolate(Object var1, Interpolator var2) throws InterpolationException;

   void interpolate(Object var1, Interpolator var2, RecursionInterceptor var3) throws InterpolationException;

   boolean hasWarnings();

   List getWarnings();
}
