package org.netbeans.lib.cvsclient.command;

import java.io.UnsupportedEncodingException;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.BinaryMessageEvent;
import org.netbeans.lib.cvsclient.event.EnhancedMessageEvent;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.event.TerminationEvent;

public abstract class BuildableCommand extends Command {
   protected Builder builder;
   private final StringBuffer taggedLineBuffer = new StringBuffer();
   private boolean builderSet;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      super.execute(var1, var2);
      if (this.builder == null && !this.isBuilderSet()) {
         this.builder = this.createBuilder(var2);
      }

   }

   public Builder createBuilder(EventManager var1) {
      return null;
   }

   public void messageSent(BinaryMessageEvent var1) {
      super.messageSent(var1);
      if (this.builder != null) {
         if (this.builder instanceof BinaryBuilder) {
            BinaryBuilder var2 = (BinaryBuilder)this.builder;
            var2.parseBytes(var1.getMessage(), var1.getMessageLength());
         }

      }
   }

   public void messageSent(MessageEvent var1) {
      super.messageSent(var1);
      if (this.builder != null) {
         if (var1 instanceof EnhancedMessageEvent) {
            EnhancedMessageEvent var4 = (EnhancedMessageEvent)var1;
            this.builder.parseEnhancedMessage(var4.getKey(), var4.getValue());
         } else {
            String var2;
            if (var1.isTagged()) {
               var2 = MessageEvent.parseTaggedMessage(this.taggedLineBuffer, var1.getMessage());
               if (var2 != null) {
                  this.builder.parseLine(var2, false);
                  this.taggedLineBuffer.setLength(0);
               }
            } else {
               if (this.taggedLineBuffer.length() > 0) {
                  this.builder.parseLine(this.taggedLineBuffer.toString(), false);
                  this.taggedLineBuffer.setLength(0);
               }

               if (this.builder instanceof PipedFilesBuilder && !var1.isError()) {
                  try {
                     var2 = new String(var1.getRawData(), "ISO-8859-1");
                     this.builder.parseLine(var2, var1.isError());
                  } catch (UnsupportedEncodingException var3) {
                     if (!$assertionsDisabled) {
                        throw new AssertionError();
                     }
                  }
               } else {
                  this.builder.parseLine(var1.getMessage(), var1.isError());
               }
            }

         }
      }
   }

   protected boolean isBuilderSet() {
      return this.builderSet;
   }

   public void setBuilder(Builder var1) {
      this.builder = var1;
      this.builderSet = true;
   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         if (this.taggedLineBuffer.length() > 0) {
            this.builder.parseLine(this.taggedLineBuffer.toString(), false);
            this.taggedLineBuffer.setLength(0);
         }

         this.builder.outputDone();
      }
   }

   static {
      $assertionsDisabled = !BuildableCommand.class.desiredAssertionStatus();
   }
}
