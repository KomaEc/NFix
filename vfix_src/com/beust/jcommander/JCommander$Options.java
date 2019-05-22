package com.beust.jcommander;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class JCommander$Options {
   private java.util.ResourceBundle bundle;
   private IDefaultProvider defaultProvider;
   private Comparator<? super ParameterDescription> parameterDescriptionComparator;
   private int columnSize;
   private boolean acceptUnknownOptions;
   private boolean allowParameterOverwriting;
   private boolean expandAtSign;
   private int verbose;
   private boolean caseSensitiveOptions;
   private boolean allowAbbreviatedOptions;
   private final List<IStringConverterInstanceFactory> converterInstanceFactories;
   private Charset atFileCharset;

   private JCommander$Options() {
      this.parameterDescriptionComparator = new JCommander$Options$1(this);
      this.columnSize = 79;
      this.acceptUnknownOptions = false;
      this.allowParameterOverwriting = false;
      this.expandAtSign = true;
      this.verbose = 0;
      this.caseSensitiveOptions = true;
      this.allowAbbreviatedOptions = false;
      this.converterInstanceFactories = new CopyOnWriteArrayList();
      this.atFileCharset = Charset.defaultCharset();
   }

   // $FF: synthetic method
   JCommander$Options(Object var1) {
      this();
   }

   // $FF: synthetic method
   static boolean access$102(JCommander$Options var0, boolean var1) {
      return var0.expandAtSign = var1;
   }

   // $FF: synthetic method
   static java.util.ResourceBundle access$202(JCommander$Options var0, java.util.ResourceBundle var1) {
      return var0.bundle = var1;
   }

   // $FF: synthetic method
   static IDefaultProvider access$300(JCommander$Options var0) {
      return var0.defaultProvider;
   }

   // $FF: synthetic method
   static boolean access$100(JCommander$Options var0) {
      return var0.expandAtSign;
   }

   // $FF: synthetic method
   static boolean access$400(JCommander$Options var0) {
      return var0.caseSensitiveOptions;
   }

   // $FF: synthetic method
   static boolean access$500(JCommander$Options var0) {
      return var0.allowAbbreviatedOptions;
   }

   // $FF: synthetic method
   static boolean access$600(JCommander$Options var0) {
      return var0.acceptUnknownOptions;
   }

   // $FF: synthetic method
   static Charset access$700(JCommander$Options var0) {
      return var0.atFileCharset;
   }

   // $FF: synthetic method
   static java.util.ResourceBundle access$200(JCommander$Options var0) {
      return var0.bundle;
   }

   // $FF: synthetic method
   static Comparator access$1200(JCommander$Options var0) {
      return var0.parameterDescriptionComparator;
   }

   // $FF: synthetic method
   static Comparator access$1202(JCommander$Options var0, Comparator var1) {
      return var0.parameterDescriptionComparator = var1;
   }

   // $FF: synthetic method
   static int access$1302(JCommander$Options var0, int var1) {
      return var0.columnSize = var1;
   }

   // $FF: synthetic method
   static int access$1300(JCommander$Options var0) {
      return var0.columnSize;
   }

   // $FF: synthetic method
   static int access$1400(JCommander$Options var0) {
      return var0.verbose;
   }

   // $FF: synthetic method
   static IDefaultProvider access$302(JCommander$Options var0, IDefaultProvider var1) {
      return var0.defaultProvider = var1;
   }

   // $FF: synthetic method
   static List access$1600(JCommander$Options var0) {
      return var0.converterInstanceFactories;
   }

   // $FF: synthetic method
   static int access$1402(JCommander$Options var0, int var1) {
      return var0.verbose = var1;
   }

   // $FF: synthetic method
   static boolean access$402(JCommander$Options var0, boolean var1) {
      return var0.caseSensitiveOptions = var1;
   }

   // $FF: synthetic method
   static boolean access$502(JCommander$Options var0, boolean var1) {
      return var0.allowAbbreviatedOptions = var1;
   }

   // $FF: synthetic method
   static boolean access$602(JCommander$Options var0, boolean var1) {
      return var0.acceptUnknownOptions = var1;
   }

   // $FF: synthetic method
   static boolean access$1702(JCommander$Options var0, boolean var1) {
      return var0.allowParameterOverwriting = var1;
   }

   // $FF: synthetic method
   static boolean access$1700(JCommander$Options var0) {
      return var0.allowParameterOverwriting;
   }

   // $FF: synthetic method
   static Charset access$702(JCommander$Options var0, Charset var1) {
      return var0.atFileCharset = var1;
   }
}
