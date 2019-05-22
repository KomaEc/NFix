package com.gzoltar.shaded.org.jacoco.report.csv;

import java.io.IOException;
import java.io.Writer;

class DelimitedWriter {
   private static final String QUOTE = "\"";
   private static final String ESCAPED_QUOTE = "\"\"";
   private static final char DEFAULT_DELIMITER = ',';
   private static final String NEW_LINE = System.getProperty("line.separator");
   private final char delimiter;
   private final Writer delegate;
   private int fieldPosition;

   public DelimitedWriter(Writer delegate) {
      this(delegate, ',');
   }

   public DelimitedWriter(Writer delegate, char delimiter) {
      this.fieldPosition = 0;
      this.delegate = delegate;
      this.delimiter = delimiter;
   }

   public void write(String... fields) throws IOException {
      String[] arr$ = fields;
      int len$ = fields.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String field = arr$[i$];
         this.write(field);
      }

   }

   public void write(String field) throws IOException {
      if (this.fieldPosition != 0) {
         this.delegate.write(this.delimiter);
      }

      this.delegate.write(this.escape(field));
      ++this.fieldPosition;
   }

   public void write(int value) throws IOException {
      this.write(Integer.toString(value));
   }

   public void write(int... values) throws IOException {
      int[] arr$ = values;
      int len$ = values.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int value = arr$[i$];
         this.write(Integer.toString(value));
      }

   }

   public void nextLine() throws IOException {
      this.delegate.write(NEW_LINE);
      this.fieldPosition = 0;
   }

   public void close() throws IOException {
      this.delegate.close();
   }

   private String escape(String value) {
      String escapedValue = value;
      if (value.indexOf("\"") != -1 || value.indexOf(this.delimiter) != -1) {
         escapedValue = value.replace("\"", "\"\"");
         escapedValue = "\"" + escapedValue + "\"";
      }

      return escapedValue;
   }
}
