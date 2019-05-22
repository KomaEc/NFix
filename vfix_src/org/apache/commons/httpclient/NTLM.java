package org.apache.commons.httpclient;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.httpclient.util.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @deprecated */
public final class NTLM {
   private byte[] currentResponse;
   private int currentPosition = 0;
   private static final Log LOG;
   public static final String DEFAULT_CHARSET = "ASCII";
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$NTLM;

   public final String getResponseFor(String message, String username, String password, String host, String domain) throws HttpException {
      String response;
      if (message != null && !message.trim().equals("")) {
         response = this.getType3Message(username, password, host, domain, this.parseType2Message(message));
      } else {
         response = this.getType1Message(host, domain);
      }

      return response;
   }

   private Cipher getCipher(byte[] key) throws HttpException {
      try {
         Cipher ecipher = Cipher.getInstance("DES/ECB/NoPadding");
         key = this.setupKey(key);
         ecipher.init(1, new SecretKeySpec(key, "DES"));
         return ecipher;
      } catch (NoSuchAlgorithmException var5) {
         throw new HttpException("DES encryption is not available.");
      } catch (InvalidKeyException var6) {
         throw new HttpException("Invalid key for DES encryption.");
      } catch (NoSuchPaddingException var7) {
         throw new HttpException("NoPadding option for DES is not available.");
      }
   }

   private byte[] setupKey(byte[] key56) {
      byte[] key = new byte[]{(byte)(key56[0] >> 1 & 255), (byte)(((key56[0] & 1) << 6 | (key56[1] & 255) >> 2 & 255) & 255), (byte)(((key56[1] & 3) << 5 | (key56[2] & 255) >> 3 & 255) & 255), (byte)(((key56[2] & 7) << 4 | (key56[3] & 255) >> 4 & 255) & 255), (byte)(((key56[3] & 15) << 3 | (key56[4] & 255) >> 5 & 255) & 255), (byte)(((key56[4] & 31) << 2 | (key56[5] & 255) >> 6 & 255) & 255), (byte)(((key56[5] & 63) << 1 | (key56[6] & 255) >> 7 & 255) & 255), (byte)(key56[6] & 127)};

      for(int i = 0; i < key.length; ++i) {
         key[i] = (byte)(key[i] << 1);
      }

      return key;
   }

   private byte[] encrypt(byte[] key, byte[] bytes) throws HttpException {
      Cipher ecipher = this.getCipher(key);

      try {
         byte[] enc = ecipher.doFinal(bytes);
         return enc;
      } catch (IllegalBlockSizeException var6) {
         throw new HttpException("Invalid block size for DES encryption.");
      } catch (BadPaddingException var7) {
         throw new HttpException("Data not padded correctly for DES encryption.");
      }
   }

   private void prepareResponse(int length) {
      this.currentResponse = new byte[length];
      this.currentPosition = 0;
   }

   private void addByte(byte b) {
      this.currentResponse[this.currentPosition] = b;
      ++this.currentPosition;
   }

   private void addBytes(byte[] bytes) {
      for(int i = 0; i < bytes.length; ++i) {
         this.currentResponse[this.currentPosition] = bytes[i];
         ++this.currentPosition;
      }

   }

   private String getResponse() {
      byte[] resp;
      if (this.currentResponse.length > this.currentPosition) {
         byte[] tmp = new byte[this.currentPosition];

         for(int i = 0; i < this.currentPosition; ++i) {
            tmp[i] = this.currentResponse[i];
         }

         resp = tmp;
      } else {
         resp = this.currentResponse;
      }

      return HttpConstants.getString(Base64.encode(resp));
   }

   private String getType1Message(String host, String domain) {
      host = host.toUpperCase();
      domain = domain.toUpperCase();
      byte[] hostBytes = getBytes(host);
      byte[] domainBytes = getBytes(domain);
      int finalLength = 32 + hostBytes.length + domainBytes.length;
      this.prepareResponse(finalLength);
      byte[] protocol = getBytes("NTLMSSP");
      this.addBytes(protocol);
      this.addByte((byte)0);
      this.addByte((byte)1);
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addByte((byte)6);
      this.addByte((byte)82);
      this.addByte((byte)0);
      this.addByte((byte)0);
      int iDomLen = domainBytes.length;
      byte[] domLen = this.convertShort(iDomLen);
      this.addByte(domLen[0]);
      this.addByte(domLen[1]);
      this.addByte(domLen[0]);
      this.addByte(domLen[1]);
      byte[] domOff = this.convertShort(hostBytes.length + 32);
      this.addByte(domOff[0]);
      this.addByte(domOff[1]);
      this.addByte((byte)0);
      this.addByte((byte)0);
      byte[] hostLen = this.convertShort(hostBytes.length);
      this.addByte(hostLen[0]);
      this.addByte(hostLen[1]);
      this.addByte(hostLen[0]);
      this.addByte(hostLen[1]);
      byte[] hostOff = this.convertShort(32);
      this.addByte(hostOff[0]);
      this.addByte(hostOff[1]);
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(hostBytes);
      this.addBytes(domainBytes);
      return this.getResponse();
   }

   private byte[] parseType2Message(String message) {
      byte[] msg = Base64.decode(getBytes(message));
      byte[] nonce = new byte[8];

      for(int i = 0; i < 8; ++i) {
         nonce[i] = msg[i + 24];
      }

      return nonce;
   }

   private String getType3Message(String user, String password, String host, String domain, byte[] nonce) throws HttpException {
      int ntRespLen = 0;
      int lmRespLen = 24;
      domain = domain.toUpperCase();
      host = host.toUpperCase();
      user = user.toUpperCase();
      byte[] domainBytes = getBytes(domain);
      byte[] hostBytes = getBytes(host);
      byte[] userBytes = getBytes(user);
      int domainLen = domainBytes.length;
      int hostLen = hostBytes.length;
      int userLen = userBytes.length;
      int finalLength = 64 + ntRespLen + lmRespLen + domainLen + userLen + hostLen;
      this.prepareResponse(finalLength);
      byte[] ntlmssp = getBytes("NTLMSSP");
      this.addBytes(ntlmssp);
      this.addByte((byte)0);
      this.addByte((byte)3);
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(this.convertShort(24));
      this.addBytes(this.convertShort(24));
      this.addBytes(this.convertShort(finalLength - 24));
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(this.convertShort(0));
      this.addBytes(this.convertShort(0));
      this.addBytes(this.convertShort(finalLength));
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(this.convertShort(domainLen));
      this.addBytes(this.convertShort(domainLen));
      this.addBytes(this.convertShort(64));
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(this.convertShort(userLen));
      this.addBytes(this.convertShort(userLen));
      this.addBytes(this.convertShort(64 + domainLen));
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(this.convertShort(hostLen));
      this.addBytes(this.convertShort(hostLen));
      this.addBytes(this.convertShort(64 + domainLen + userLen));

      for(int i = 0; i < 6; ++i) {
         this.addByte((byte)0);
      }

      this.addBytes(this.convertShort(finalLength));
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addByte((byte)6);
      this.addByte((byte)82);
      this.addByte((byte)0);
      this.addByte((byte)0);
      this.addBytes(domainBytes);
      this.addBytes(userBytes);
      this.addBytes(hostBytes);
      this.addBytes(this.hashPassword(password, nonce));
      return this.getResponse();
   }

   private byte[] hashPassword(String password, byte[] nonce) throws HttpException {
      byte[] passw = getBytes(password.toUpperCase());
      byte[] lmPw1 = new byte[7];
      byte[] lmPw2 = new byte[7];
      int len = passw.length;
      if (len > 7) {
         len = 7;
      }

      int idx;
      for(idx = 0; idx < len; ++idx) {
         lmPw1[idx] = passw[idx];
      }

      while(idx < 7) {
         lmPw1[idx] = 0;
         ++idx;
      }

      len = passw.length;
      if (len > 14) {
         len = 14;
      }

      for(idx = 7; idx < len; ++idx) {
         lmPw2[idx - 7] = passw[idx];
      }

      while(idx < 14) {
         lmPw2[idx - 7] = 0;
         ++idx;
      }

      byte[] magic = new byte[]{75, 71, 83, 33, 64, 35, 36, 37};
      byte[] lmHpw1 = this.encrypt(lmPw1, magic);
      byte[] lmHpw2 = this.encrypt(lmPw2, magic);
      byte[] lmHpw = new byte[21];

      for(int i = 0; i < lmHpw1.length; ++i) {
         lmHpw[i] = lmHpw1[i];
      }

      for(int i = 0; i < lmHpw2.length; ++i) {
         lmHpw[i + 8] = lmHpw2[i];
      }

      for(int i = 0; i < 5; ++i) {
         lmHpw[i + 16] = 0;
      }

      byte[] lmResp = new byte[24];
      this.calcResp(lmHpw, nonce, lmResp);
      return lmResp;
   }

   private void calcResp(byte[] keys, byte[] plaintext, byte[] results) throws HttpException {
      byte[] keys1 = new byte[7];
      byte[] keys2 = new byte[7];
      byte[] keys3 = new byte[7];

      for(int i = 0; i < 7; ++i) {
         keys1[i] = keys[i];
      }

      for(int i = 0; i < 7; ++i) {
         keys2[i] = keys[i + 7];
      }

      for(int i = 0; i < 7; ++i) {
         keys3[i] = keys[i + 14];
      }

      byte[] results1 = this.encrypt(keys1, plaintext);
      byte[] results2 = this.encrypt(keys2, plaintext);
      byte[] results3 = this.encrypt(keys3, plaintext);

      for(int i = 0; i < 8; ++i) {
         results[i] = results1[i];
      }

      for(int i = 0; i < 8; ++i) {
         results[i + 8] = results2[i];
      }

      for(int i = 0; i < 8; ++i) {
         results[i + 16] = results3[i];
      }

   }

   private byte[] convertShort(int num) {
      byte[] val = new byte[2];

      String hex;
      for(hex = Integer.toString(num, 16); hex.length() < 4; hex = "0" + hex) {
      }

      String low = hex.substring(2, 4);
      String high = hex.substring(0, 2);
      val[0] = (byte)Integer.parseInt(low, 16);
      val[1] = (byte)Integer.parseInt(high, 16);
      return val;
   }

   private static byte[] getBytes(String s) {
      if (s == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         try {
            return s.getBytes("ASCII");
         } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException("NTLM requires ASCII support");
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$NTLM == null ? (class$org$apache$commons$httpclient$NTLM = class$("org.apache.commons.httpclient.NTLM")) : class$org$apache$commons$httpclient$NTLM);
   }
}
