package org.apache.tools.ant.taskdefs;

import java.io.File;

public interface XSLTLiaison {
   String FILE_PROTOCOL_PREFIX = "file://";

   void setStylesheet(File var1) throws Exception;

   void addParam(String var1, String var2) throws Exception;

   void transform(File var1, File var2) throws Exception;
}
