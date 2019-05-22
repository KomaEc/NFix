package com.gzoltar.client.utils;

import com.gzoltar.shaded.jline.TerminalFactory;
import java.time.Duration;
import java.time.LocalDateTime;

public class ProgressBar {
   private String task = "";
   private static final int CONSOLE_RIGHT_MARGIN = 4;
   private int length = 0;
   private int current = 0;
   private int max;
   private LocalDateTime startTime = null;
   private String extraMessage = "";

   public ProgressBar(int var1) {
      this.max = var1;
   }

   public ProgressBar(String var1, int var2) {
      this.task = var1;
      this.max = var2;
   }

   private String repeat(char var1, int var2) {
      if (var2 <= 0) {
         return "";
      } else {
         char[] var3 = new char[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var1;
         }

         return new String(var3);
      }
   }

   private int progress() {
      return this.max == 0 ? 0 : (int)Math.round((double)this.current / (double)this.max * (double)this.length);
   }

   private String formatDuration(Duration var1) {
      long var2 = var1.getSeconds();
      return String.format("%d:%02d:%02d", var2 / 3600L, var2 % 3600L / 60L, var2 % 60L);
   }

   private String percentage() {
      String var1;
      if (this.max == 0) {
         var1 = "? %";
      } else {
         var1 = Math.round((double)this.current / (double)this.max * 100.0D) + "%";
      }

      return this.repeat(' ', 4 - var1.length()) + var1;
   }

   private String ratio() {
      int var1 = String.valueOf(this.max).length();
      String var2 = String.valueOf(this.current);
      return this.repeat(' ', var1 - var2.length()) + var2 + "/" + this.max;
   }

   private int consoleWidth() {
      return TerminalFactory.get().getWidth();
   }

   private void show() {
      System.out.print('\r');
      Duration var1 = Duration.between(this.startTime, LocalDateTime.now());
      String var2 = this.task + " " + this.percentage() + " [";
      String var4 = "] " + this.ratio() + " (" + this.formatDuration(var1) + ") " + this.extraMessage;
      this.length = this.consoleWidth() - 4 - var2.length() - var4.length();
      int var3 = this.progress();
      int var5 = (var4 = var2 + this.repeat('=', var3) + this.repeat(' ', this.length - var3) + var4).length();
      System.out.print(var4 + this.repeat(' ', var5 - var4.length()));
   }

   public void start() {
      this.startTime = LocalDateTime.now();
   }

   public void stepBy(int var1) {
      this.current += var1;
      if (this.current > this.max) {
         this.max = this.current;
      }

      this.show();
   }

   public void step() {
      this.stepBy(1);
   }

   public void stop() {
      System.out.println();
   }

   public void setExtraMessage(String var1) {
      this.length = this.length + this.extraMessage.length() - var1.length();
      this.extraMessage = var1;
   }
}
