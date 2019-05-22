package org.testng.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.testng.TestNGException;
import org.testng.remote.strprotocol.GenericMessage;
import org.testng.remote.strprotocol.IRemoteSuiteListener;
import org.testng.remote.strprotocol.IRemoteTestListener;
import org.testng.remote.strprotocol.SuiteMessage;
import org.testng.remote.strprotocol.TestMessage;
import org.testng.remote.strprotocol.TestResultMessage;
import org.xml.sax.SAXException;

public class ResultXMLParser extends XMLParser<Object> {
   private IRemoteTestListener m_testListener;
   private IRemoteSuiteListener m_suiteListener;

   public ResultXMLParser(IRemoteSuiteListener suiteListener, IRemoteTestListener testListener) {
      this.m_suiteListener = suiteListener;
      this.m_testListener = testListener;
   }

   public void parse() {
   }

   public Object parse(String currentFile, InputStream inputStream, boolean loadClasses) {
      ResultContentHandler handler = new ResultContentHandler(this.m_suiteListener, this.m_testListener, loadClasses);

      try {
         this.parse(inputStream, handler);
         return null;
      } catch (IOException | SAXException var6) {
         throw new TestNGException(var6);
      }
   }

   public static void main(String[] args) throws FileNotFoundException {
      IRemoteSuiteListener l1 = new IRemoteSuiteListener() {
         public void onInitialization(GenericMessage genericMessage) {
         }

         public void onStart(SuiteMessage suiteMessage) {
         }

         public void onFinish(SuiteMessage suiteMessage) {
         }
      };
      IRemoteTestListener l2 = new IRemoteTestListener() {
         public void onStart(TestMessage tm) {
         }

         public void onFinish(TestMessage tm) {
         }

         public void onTestStart(TestResultMessage trm) {
         }

         public void onTestSuccess(TestResultMessage trm) {
         }

         public void onTestFailure(TestResultMessage trm) {
         }

         public void onTestSkipped(TestResultMessage trm) {
         }

         public void onTestFailedButWithinSuccessPercentage(TestResultMessage trm) {
         }
      };
      ResultXMLParser parser = new ResultXMLParser(l1, l2);
      String fileName = "/Users/cbeust/java/testng/test-output/testng-results.xml";
      parser.parse(fileName, new FileInputStream(new File(fileName)), false);
   }
}
