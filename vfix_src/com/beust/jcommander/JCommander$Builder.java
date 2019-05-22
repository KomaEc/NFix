package com.beust.jcommander;

import java.nio.charset.Charset;

class JCommander$Builder {
   private JCommander jCommander = new JCommander();
   private String[] args = null;

   public JCommander$Builder() {
   }

   public JCommander$Builder addObject(Object var1) {
      this.jCommander.addObject(var1);
      return this;
   }

   public JCommander$Builder resourceBundle(java.util.ResourceBundle var1) {
      this.jCommander.setDescriptionsBundle(var1);
      return this;
   }

   public JCommander$Builder args(String[] var1) {
      this.args = var1;
      return this;
   }

   public JCommander$Builder expandAtSign(Boolean var1) {
      this.jCommander.setExpandAtSign(var1);
      return this;
   }

   public JCommander$Builder programName(String var1) {
      this.jCommander.setProgramName(var1);
      return this;
   }

   public JCommander$Builder columnSize(int var1) {
      this.jCommander.setColumnSize(var1);
      return this;
   }

   public JCommander$Builder defaultProvider(IDefaultProvider var1) {
      this.jCommander.setDefaultProvider(var1);
      return this;
   }

   public JCommander$Builder addConverterFactory(IStringConverterFactory var1) {
      this.jCommander.addConverterFactory(var1);
      return this;
   }

   public JCommander$Builder verbose(int var1) {
      this.jCommander.setVerbose(var1);
      return this;
   }

   public JCommander$Builder allowAbbreviatedOptions(boolean var1) {
      this.jCommander.setAllowAbbreviatedOptions(var1);
      return this;
   }

   public JCommander$Builder acceptUnknownOptions(boolean var1) {
      this.jCommander.setAcceptUnknownOptions(var1);
      return this;
   }

   public JCommander$Builder allowParameterOverwriting(boolean var1) {
      this.jCommander.setAllowParameterOverwriting(var1);
      return this;
   }

   public JCommander$Builder atFileCharset(Charset var1) {
      this.jCommander.setAtFileCharset(var1);
      return this;
   }

   public JCommander$Builder addConverterInstanceFactory(IStringConverterInstanceFactory var1) {
      this.jCommander.addConverterInstanceFactory(var1);
      return this;
   }

   public JCommander$Builder addCommand(Object var1) {
      this.jCommander.addCommand(var1);
      return this;
   }

   public JCommander$Builder addCommand(String var1, Object var2, String... var3) {
      this.jCommander.addCommand(var1, var2, var3);
      return this;
   }

   public JCommander build() {
      if (this.args != null) {
         this.jCommander.parse(this.args);
      }

      return this.jCommander;
   }
}
