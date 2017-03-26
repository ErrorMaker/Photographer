package com.hypermine.habbo.photographer.util.crypto;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */
public class RC4
{
    private int i = 0;
    private int j = 0;
    private final int[] Table = new int[256];
    public void init(byte[] a)
    {
        int k = a.length;
        this.i = 0;
        while (this.i < 256)
        {
            this.Table[this.i] = this.i;
            this.i++;
        }
        this.j = 0;
        this.i = 0;
        while (this.i < 256)
        {
            this.j = ((this.j + this.Table[this.i]) + (a[this.i % k] & 0xff)) % 256;
            this.swap(this.i, this.j);
            this.i++;
        }
        this.i = 0;
        this.j = 0;
    }

    public byte[] parse(byte[] b) // encrypt and decrypt
    {
        for(int a = 0;a<b.length;a++)
        {
            this.i = (this.i + 1) % 256;
            this.j = (this.j + this.Table[this.i]) % 256;
            this.swap(this.i, this.j);
            b[a] = (byte) ((b[a] & 0xff) ^ this.Table[(this.Table[this.i] + this.Table[this.j]) % 256]);
        }
        return b;
    }

    private void swap(int a1, int a2)
    {
        int k = this.Table[a1];
        this.Table[a1] = this.Table[a2];
        this.Table[a2] = k;
    }
}