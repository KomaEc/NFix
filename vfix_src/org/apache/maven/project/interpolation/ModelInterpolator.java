package org.apache.maven.project.interpolation;

import java.io.File;
import java.util.Map;
import org.apache.maven.model.Model;
import org.apache.maven.project.ProjectBuilderConfiguration;

public interface ModelInterpolator {
   String DEFAULT_BUILD_TIMESTAMP_FORMAT = "yyyyMMdd-HHmm";
   String BUILD_TIMESTAMP_FORMAT_PROPERTY = "maven.build.timestamp.format";
   String ROLE = ModelInterpolator.class.getName();

   /** @deprecated */
   Model interpolate(Model var1, Map<String, ?> var2) throws ModelInterpolationException;

   /** @deprecated */
   Model interpolate(Model var1, Map<String, ?> var2, boolean var3) throws ModelInterpolationException;

   Model interpolate(Model var1, File var2, ProjectBuilderConfiguration var3, boolean var4) throws ModelInterpolationException;

   String interpolate(String var1, Model var2, File var3, ProjectBuilderConfiguration var4, boolean var5) throws ModelInterpolationException;
}
