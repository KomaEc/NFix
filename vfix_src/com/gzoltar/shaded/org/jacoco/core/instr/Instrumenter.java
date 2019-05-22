package com.gzoltar.shaded.org.jacoco.core.instr;

import com.gzoltar.shaded.org.jacoco.core.internal.ContentTypeDetector;
import com.gzoltar.shaded.org.jacoco.core.internal.Java9Support;
import com.gzoltar.shaded.org.jacoco.core.internal.Pack200Streams;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.ClassProbesAdapter;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.ClassInstrumenter;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.IProbeArrayStrategy;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.ProbeArrayStrategyFactory;
import com.gzoltar.shaded.org.jacoco.core.internal.instr.SignatureRemover;
import com.gzoltar.shaded.org.jacoco.core.runtime.IExecutionDataAccessorGenerator;
import com.gzoltar.shaded.org.objectweb.asm.ClassReader;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import com.gzoltar.shaded.org.objectweb.asm.ClassWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Instrumenter {
   private final IExecutionDataAccessorGenerator accessorGenerator;
   private final SignatureRemover signatureRemover;

   public Instrumenter(IExecutionDataAccessorGenerator runtime) {
      this.accessorGenerator = runtime;
      this.signatureRemover = new SignatureRemover();
   }

   public void setRemoveSignatures(boolean flag) {
      this.signatureRemover.setActive(flag);
   }

   public byte[] instrument(ClassReader reader) {
      ClassWriter writer = new ClassWriter(reader, 0) {
         protected String getCommonSuperClass(String type1, String type2) {
            throw new IllegalStateException();
         }
      };
      IProbeArrayStrategy strategy = ProbeArrayStrategyFactory.createFor(reader, this.accessorGenerator);
      ClassVisitor visitor = new ClassProbesAdapter(new ClassInstrumenter(strategy, writer), true);
      reader.accept(visitor, 8);
      return writer.toByteArray();
   }

   public byte[] instrument(byte[] buffer, String name) throws IOException {
      try {
         if (Java9Support.isPatchRequired(buffer)) {
            byte[] result = this.instrument(new ClassReader(Java9Support.downgrade(buffer)));
            Java9Support.upgrade(result);
            return result;
         } else {
            return this.instrument(new ClassReader(buffer));
         }
      } catch (RuntimeException var4) {
         throw this.instrumentError(name, var4);
      }
   }

   public byte[] instrument(InputStream input, String name) throws IOException {
      byte[] bytes;
      try {
         bytes = Java9Support.readFully(input);
      } catch (IOException var5) {
         throw this.instrumentError(name, var5);
      }

      return this.instrument(bytes, name);
   }

   public void instrument(InputStream input, OutputStream output, String name) throws IOException {
      output.write(this.instrument(input, name));
   }

   private IOException instrumentError(String name, Exception cause) {
      IOException ex = new IOException(String.format("Error while instrumenting %s.", name));
      ex.initCause(cause);
      return ex;
   }

   public int instrumentAll(InputStream input, OutputStream output, String name) throws IOException {
      ContentTypeDetector detector;
      try {
         detector = new ContentTypeDetector(input);
      } catch (IOException var6) {
         throw this.instrumentError(name, var6);
      }

      switch(detector.getType()) {
      case -889275714:
         this.instrument(detector.getInputStream(), output, name);
         return 1;
      case -889270259:
         return this.instrumentPack200(detector.getInputStream(), output, name);
      case 529203200:
         return this.instrumentGzip(detector.getInputStream(), output, name);
      case 1347093252:
         return this.instrumentZip(detector.getInputStream(), output, name);
      default:
         this.copy(detector.getInputStream(), output, name);
         return 0;
      }
   }

   private int instrumentZip(InputStream input, OutputStream output, String name) throws IOException {
      ZipInputStream zipin = new ZipInputStream(input);
      ZipOutputStream zipout = new ZipOutputStream(output);
      int count = 0;

      ZipEntry entry;
      while((entry = this.nextEntry(zipin, name)) != null) {
         String entryName = entry.getName();
         if (!this.signatureRemover.removeEntry(entryName)) {
            zipout.putNextEntry(new ZipEntry(entryName));
            if (!this.signatureRemover.filterEntry(entryName, zipin, zipout)) {
               count += this.instrumentAll(zipin, zipout, name + "@" + entryName);
            }

            zipout.closeEntry();
         }
      }

      zipout.finish();
      return count;
   }

   private ZipEntry nextEntry(ZipInputStream input, String location) throws IOException {
      try {
         return input.getNextEntry();
      } catch (IOException var4) {
         throw this.instrumentError(location, var4);
      }
   }

   private int instrumentGzip(InputStream input, OutputStream output, String name) throws IOException {
      GZIPInputStream gzipInputStream;
      try {
         gzipInputStream = new GZIPInputStream(input);
      } catch (IOException var7) {
         throw this.instrumentError(name, var7);
      }

      GZIPOutputStream gzout = new GZIPOutputStream(output);
      int count = this.instrumentAll(gzipInputStream, gzout, name);
      gzout.finish();
      return count;
   }

   private int instrumentPack200(InputStream input, OutputStream output, String name) throws IOException {
      InputStream unpackedInput;
      try {
         unpackedInput = Pack200Streams.unpack(input);
      } catch (IOException var7) {
         throw this.instrumentError(name, var7);
      }

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int count = this.instrumentAll(unpackedInput, buffer, name);
      Pack200Streams.pack(buffer.toByteArray(), output);
      return count;
   }

   private void copy(InputStream input, OutputStream output, String name) throws IOException {
      byte[] buffer = new byte[1024];

      int len;
      while((len = this.read(input, buffer, name)) != -1) {
         output.write(buffer, 0, len);
      }

   }

   private int read(InputStream input, byte[] buffer, String name) throws IOException {
      try {
         return input.read(buffer);
      } catch (IOException var5) {
         throw this.instrumentError(name, var5);
      }
   }
}
