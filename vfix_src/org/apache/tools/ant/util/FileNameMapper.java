package org.apache.tools.ant.util;

public interface FileNameMapper {
   void setFrom(String var1);

   void setTo(String var1);

   String[] mapFileName(String var1);
}
