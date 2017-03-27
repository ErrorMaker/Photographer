package com.hypermine.habbo.photographer.util.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/26/2017.
 */
public class RSAKey {
    public int e;
    public BigInteger n;
    public BigInteger d;
    public BigInteger p;
    public BigInteger q;
    public BigInteger dmp1;
    public BigInteger dmq1;
    public BigInteger coeff;
    public boolean canEncrypt;
    public boolean canDecrypt;

    public RSAKey(BigInteger N, int E, BigInteger D, BigInteger P, BigInteger Q, BigInteger Dmp1, BigInteger Dmq1, BigInteger Coeff)
    {
        this.n = N;
        this.e = E;
        this.d = D;
        this.p = P;
        this.q = Q;
        this.dmp1 = Dmp1;
        this.dmq1 = Dmq1;
        this.coeff = Coeff;
        this.canEncrypt = ((!(this.n == null)) && (!(this.e == 0)));
        this.canDecrypt = (canEncrypt && (!((this.d == null))));
    }

    public static RSAKey parsePublicKey(String n, int e)
    {
        return (new RSAKey(new BigInteger(n, 16), e, null, null, null, null, null, null));
    }

    public static RSAKey parsePrivateKey(String n, String e, String d, String p, String q, String Dmp1, String Dmq1, String Coeff)
    {
        if(p == null || p.equals(""))
        {
            return (new RSAKey(new BigInteger(n, 16), Integer.parseInt(e), new BigInteger(d, 16), null, null, null, null, null));
        }
        return (new RSAKey(new BigInteger(n, 16), Integer.parseInt(e), new BigInteger(d, 16), new BigInteger(p, 16), new BigInteger(q, 16), new BigInteger(Dmp1), new BigInteger(Dmq1), new BigInteger(Coeff)));
    }


    public int getBlockSize()
    {
        return (int)Math.floor((n.bitLength()+7)/8);
    }

    public int getBlockSizeBase(int b) throws Exception
    {
        int k = 0;
        int adjustedBl = n.bitLength();
        switch (b)
        {
            case 2: k=1; break;
            case 4: k=2; break;
            case 16: k=4; break;
            case 64:
                k=6;
                adjustedBl = (int) (12 * Math.floor((n.bitLength()+11)/12));
                break;
            case 256: k = 8; break;
            default: throw new Exception("Invalid block size" + b);
        }

        return (int) (Math.floor(adjustedBl+k-1)/k);
    }

    public void dispose()
    {
        e = 0;
        n = null;
    }

    public BigInteger doPublic(BigInteger x)
    {
        if (this.canEncrypt)
        {
            return x.modPow(BigInteger.valueOf(65537), this.n);
        }

        return BigInteger.ZERO;
    }

    public String encrypt(BigInteger value)
    {
        BigInteger m = new BigInteger(this.pkcs1pad2(value.toByteArray(), this.getBlockSize()));
        if (m.equals(BigInteger.ZERO))
        {
            return null;
        }

        BigInteger c = this.doPublic(m);
        if (c.equals(BigInteger.ZERO))
        {
            return null;
        }

        String result = c.toString(16);
        if ((result.length() & 1) == 0)
        {
            return result;
        }

        return "0" + result;
    }

    public String encrypt(String text)
    {
        if (text.length() > this.getBlockSize() - 11)
        {
            //Console.WriteLine("RSA Encrypt: Message is to big!");
        }

        BigInteger m = new BigInteger(this.pkcs1pad2(text.getBytes(), this.getBlockSize()));
        if (m.equals(BigInteger.ZERO))
        {
            return null;
        }

        BigInteger c = this.doPublic(m);
        if (c.equals(BigInteger.ZERO))
        {
            return null;
        }

        String result = c.toString(16);
        if ((result.length() & 1) == 0)
        {
            return result;
        }

        return "0" + result;
    }

    private byte[] pkcs1pad2(byte[] data, int n)
    {
        byte[] bytes = new byte[n];
        int i = data.length - 1;
        while (i >= 0 && n > 11)
        {
            bytes[--n] = data[i--];
        }
        bytes[--n] = 0;

        while (n > 2)
        {
            bytes[--n] = 0x01;
        }

        bytes[--n] = 0x2;
        bytes[--n] = 0;

        return bytes;
    }

    public BigInteger doPrivate(BigInteger x)
    {
        if (this.canDecrypt)
        {
            return x.modPow(this.d, this.n);
        }

        return BigInteger.ZERO;
    }

    public byte[] decrypt(String ctext)
    {
        return this.decrypt(ctext, 16);
    }

    public byte[] decrypt(String ctext, int size)
    {
        BigInteger c = new BigInteger(ctext, size);
//        c = new BigInteger(pkcs1pad2(c.toByteArray(), this.getBlockSize()));
        BigInteger m = this.doPublic(c);
        if (m.equals(BigInteger.ZERO))
        {
            return null;
        }

        byte[] bytes = this.pkcs1unpad2(m, this.getBlockSize());

        if (bytes == null)
        {
            return null;
        }

        return bytes;
    }

    private byte[] pkcs1unpad2(BigInteger src, int n) {
        byte[] bytes = src.toByteArray();
        byte[] out;
        int i = 0;
        while (i < bytes.length && bytes[i] == 0) {
            ++i;
        }
        if (bytes.length - i != n - 1 || bytes[i] > 2) {
            return null;
        }
        ++i;
        while (bytes[i] != 0) {
            if (++i >= bytes.length) {
                return null;
            }
        }
        out = new byte[(bytes.length - i) + 1];
        int p = 0;
        while (++i < bytes.length) {
            out[p++] = (bytes[i]);
        }
        return out;
    }

    public static byte[] pkcs1unpad(final BigInteger src, int n) {
        final byte[] b = src.toByteArray();

        int i = 0;
        while ((i < b.length) && (b[i] == 0)) {
            i++;
        }

        if (((b.length - i) != (n - 1)) || (b[i] != 0x2))
            return null;

        i++;

        while (b[i] != 0) {
            if (++i >= b.length)
                return null;
        }
        final byte[] out = new byte[b.length - (i + 1)];
        int p = 0;
        while (++i < b.length) {
            out[p] = b[i];
            p++;
        }
        return out;
    }

}
/*


 public function dispose()
 {
  e = 0;
  n.dispose();
  n = null;
 }
 public function encrypt(src:String, base:Number)
 {
  if ( ! base )
   base = 16;

  var srcData = new Object();
  srcData.data = src;
  srcData.position = 0;

  var dst:String = "";
  // adjust pad if needed
  var bl:Number = getBlockSize();
  var end:Number = srcData.position + src.length;

  while (srcData.position<end)
  {
   var block:BigInteger = pkcs1pad(srcData, end, bl);
   var chunk:BigInteger = block.modPowInt(this.e, this.n);
   dst += chunk.toString(base);
  }

  return dst;
 }

 public function decrypt(src:String, base:Number, raw:Boolean)
 {
  var dst:String = "";
  var srcData = new Object();
  srcData.data = src;
  srcData.position = 0;

  // convert src to BigInteger
  var blBase:Number = this.getBlockSizeBase(base);
  var bl:Number = this.getBlockSize();
  var end:Number = srcData.position + src.length;
  while (srcData.position<end)
  {
   var d =  Math.min(src.length, srcData.position + blBase);

   var block:BigInteger = new BigInteger();
   block.fromStringPos(srcData.data, base, srcData.position, d);
   var chunk:BigInteger = doPrivate2(block);

   dst += this.pkcs1unpad(chunk, bl, raw, 2);
   srcData.position = d;
  }

  return dst;
 }

 public function verify(src:String, base:Number, raw:Boolean):String
 {
  var dst:String = "";

  var srcData = new Object();
  srcData.data = src;
  srcData.position = 0;

  // convert src to BigInteger
  var blBase:Number = this.getBlockSizeBase(base);
  var bl:Number = this.getBlockSize();
  var end:Number = srcData.position + src.length;
  while (srcData.position<end)
  {
   var d =  Math.min(src.length, srcData.position + blBase);

   var block:BigInteger = new BigInteger();
   block.fromStringPos(srcData.data, base, srcData.position, d, true);
   var chunk:BigInteger = block.modPowInt(this.e, this.n);

   dst += this.pkcs1unpad(chunk, bl, raw, 1);
   srcData.position = d;
  }

  return dst;
 }

 private static function pkcs1pad(src:Object, end:Number, n:Number):BigInteger
 {
  var ba:Array = new Array();
  var p = src.position;
  end = Math.min(end, src.data.length, p+n-11);
  src.position = end;
  var i:Number = end-1;
  while (i>=p && n>11)
   ba[--n] = src.data.charCodeAt(i--);
  ba[--n] = 0;
  var rng:SecureRandom = new SecureRandom();
  while (n>2)
  {
   var x:Number = 0;
   while (x==0)
    x = rng.nextByte();
   ba[--n] = x;
  }
  ba[--n] = 2;
  ba[--n] = 0;
  return new BigInteger(ba);
 }

 private function pkcs1unpad(src:BigInteger, n:Number, raw:Boolean, blockType:Number)
 {
  var b:Array = src.toByteArray(raw);
  var i:Number = 0;
  while (i<b.length && b[i]==0) ++i;
  if (b.length-i != n-1 || b[i]!=blockType)
  {
   trace("PKCS#1 unpad: i="+i+", expected b[i]==" + blockType + ", got b[i]="+b[i]);
   return null;
  }
  ++i;
  while (b[i]!=0)
  {
   if (++i>=b.length)
   {
    trace("PKCS#1 unpad: i="+i+", b[i-1]!=0 (="+b[i-1]+")");
    return null;
   }
  }

  var out:String = "";
  if ( raw )
  {
   while (++i < b.length)
   {
    var cur:Number = b[i];
    if ( cur < 0x10 )
     out += "0";
    out += cur.toString(16);
   }
  }
  else
  {
   while (++i < b.length)
   {
    out += String.fromCharCode(b[i]);
   }
  }

  return out;
 }

 public function toString():String
 {
  return "rsa";
 }

 public function dump():String
 {
  var s:String= "N="+n.toString(16)+"\n"+
  "E="+e.toString(16)+"\n";
  if (canDecrypt)
  {
   s+="D="+d.toString(16)+"\n";
   if (p!=null && q!=null)
   {
    s+="P="+p.toString(16)+"\n";
    s+="Q="+q.toString(16)+"\n";
    s+="DMP1="+dmp1.toString(16)+"\n";
    s+="DMQ1="+dmq1.toString(16)+"\n";
    s+="IQMP="+coeff.toString(16)+"\n";
   }
  }
  return s;
 }

 private function doPrivate2(x:BigInteger):BigInteger
 {
  if (p==null && q==null)
  {
   return x.modPow(d,n);
  }

  var xp:BigInteger = x.mod(p).modPow(dmp1, p);
  var xq:BigInteger = x.mod(q).modPow(dmq1, q);

  while (xp.compareTo(xq)<0)
  {
   xp = xp.add(p);
  }
  var r:BigInteger = xp.subtract(xq).multiply(coeff).mod(p).multiply(q).add(xq);

  return r;
 }
 */
