package com.gzoltar.shaded.org.jacoco.report;

import java.io.IOException;
import java.io.OutputStream;

public interface IMultiReportOutput {
   OutputStream createFile(String var1) throws IOException;

   void close() throws IOException;
}
