package com.gzoltar.shaded.org.jacoco.core.tools;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataReader;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataStore;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataWriter;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfoStore;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExecFileLoader {
   private final SessionInfoStore sessionInfos = new SessionInfoStore();
   private final ExecutionDataStore executionData = new ExecutionDataStore();

   public void load(InputStream stream) throws IOException {
      ExecutionDataReader reader = new ExecutionDataReader(new BufferedInputStream(stream));
      reader.setExecutionDataVisitor(this.executionData);
      reader.setSessionInfoVisitor(this.sessionInfos);
      reader.read();
   }

   public void load(File file) throws IOException {
      FileInputStream stream = new FileInputStream(file);

      try {
         this.load((InputStream)stream);
      } finally {
         stream.close();
      }

   }

   public void save(OutputStream stream) throws IOException {
      ExecutionDataWriter dataWriter = new ExecutionDataWriter(stream);
      this.sessionInfos.accept(dataWriter);
      this.executionData.accept(dataWriter);
   }

   public void save(File file, boolean append) throws IOException {
      File folder = file.getParentFile();
      if (folder != null) {
         folder.mkdirs();
      }

      FileOutputStream fileStream = new FileOutputStream(file, append);
      fileStream.getChannel().lock();
      BufferedOutputStream bufferedStream = new BufferedOutputStream(fileStream);

      try {
         this.save(bufferedStream);
      } finally {
         bufferedStream.close();
      }

   }

   public SessionInfoStore getSessionInfoStore() {
      return this.sessionInfos;
   }

   public ExecutionDataStore getExecutionDataStore() {
      return this.executionData;
   }
}
