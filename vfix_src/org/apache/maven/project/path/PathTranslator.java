package org.apache.maven.project.path;

import java.io.File;
import org.apache.maven.model.Model;

public interface PathTranslator {
   String ROLE = PathTranslator.class.getName();

   void alignToBaseDirectory(Model var1, File var2);

   String alignToBaseDirectory(String var1, File var2);

   void unalignFromBaseDirectory(Model var1, File var2);

   String unalignFromBaseDirectory(String var1, File var2);
}
