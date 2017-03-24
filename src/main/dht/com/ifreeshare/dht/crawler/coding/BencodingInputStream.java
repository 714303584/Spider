package com.ifreeshare.dht.crawler.coding;
import java.io.DataInput;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BencodingInputStream extends FilterInputStream
  implements DataInput
{
  private final String encoding;
  private final boolean decodeAsString;

  public BencodingInputStream(InputStream paramInputStream)
  {
    this(paramInputStream, "UTF-8", false);
  }

  public BencodingInputStream(InputStream paramInputStream, String paramString)
  {
    this(paramInputStream, paramString, false);
  }

  public BencodingInputStream(InputStream paramInputStream, boolean paramBoolean)
  {
    this(paramInputStream, "UTF-8", paramBoolean);
  }

  public BencodingInputStream(InputStream paramInputStream, String paramString, boolean paramBoolean)
  {
    super(paramInputStream);
    if (paramString == null)
      throw new NullPointerException("encoding");
    this.encoding = paramString;
    this.decodeAsString = paramBoolean;
  }

  public String getEncoding()
  {
    return this.encoding;
  }

  public boolean isDecodeAsString()
  {
    return this.decodeAsString;
  }

  public Object readObject()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    return readObject(i);
  }

  protected Object readObject(int paramInt)
    throws IOException
  {
    if (paramInt == 100)
      return readMap0();
    if (paramInt == 108)
      return readList0();
    if (paramInt == 105)
      return readNumber0();
    if (isDigit(paramInt))
    {
      byte[] arrayOfByte = readBytes(paramInt);
      return this.decodeAsString ? new String(arrayOfByte, this.encoding) : arrayOfByte;
    }
    return readCustom(paramInt);
  }

  protected Object readCustom(int paramInt)
    throws IOException
  {
    throw new IOException("Not implemented: " + paramInt);
  }

  public byte[] readBytes()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    return readBytes(i);
  }

  private byte[] readBytes(int paramInt)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((char)paramInt);
    while ((paramInt = read()) != 58)
    {
      if (paramInt == -1)
        throw new EOFException();
      localStringBuilder.append((char)paramInt);
    }
    int i = Integer.parseInt(localStringBuilder.toString());
    byte[] arrayOfByte = new byte[i];
    readFully(arrayOfByte);
    return arrayOfByte;
  }

  public String readString()
    throws IOException
  {
    return readString(this.encoding);
  }

  private String readString(String paramString)
    throws IOException
  {
    return new String(readBytes(), paramString);
  }

  public <T extends Enum<T>> T readEnum(Class<T> paramClass)
    throws IOException
  {
    return Enum.valueOf(paramClass, readString());
  }

  public char readChar()
    throws IOException
  {
    return readString().charAt(0);
  }

  public boolean readBoolean()
    throws IOException
  {
    return readInt() != 0;
  }

  public byte readByte()
    throws IOException
  {
    return readNumber().byteValue();
  }

  public short readShort()
    throws IOException
  {
    return readNumber().shortValue();
  }

  public int readInt()
    throws IOException
  {
    return readNumber().intValue();
  }

  public float readFloat()
    throws IOException
  {
    return readNumber().floatValue();
  }

  public long readLong()
    throws IOException
  {
    return readNumber().longValue();
  }

  public double readDouble()
    throws IOException
  {
    return readNumber().doubleValue();
  }

  public Number readNumber()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    if (i != 105)
      throw new IOException();
    return readNumber0();
  }

  private Number readNumber0()
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j = -1;
    while ((j = read()) != 101)
    {
      if (j == -1)
        throw new EOFException();
      if (j == 46)
        i = 1;
      localStringBuilder.append((char)j);
    }
    try
    {
      if (i != 0)
        return new BigDecimal(localStringBuilder.toString());
      return new BigInteger(localStringBuilder.toString());
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw new IOException("NumberFormatException");
  }

  public List<?> readList()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    if (i != 108)
      throw new IOException();
    return readList0();
  }

  private List<?> readList0()
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    int i = -1;
    while ((i = read()) != 101)
    {
      if (i == -1)
        throw new EOFException();
      localArrayList.add(readObject(i));
    }
    return localArrayList;
  }

  public Map<String, ?> readMap()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    if (i != 100)
      throw new IOException();
    return readMap0();
  }

  private Map<String, ?> readMap0()
    throws IOException
  {
    TreeMap localTreeMap = new TreeMap();
    int i = -1;
    while ((i = read()) != 101)
    {
      if (i == -1)
        throw new EOFException();
      String str = new String(readBytes(i), this.encoding);
      Object localObject = readObject();
      localTreeMap.put(str, localObject);
    }
    return localTreeMap;
  }

  public void readFully(byte[] paramArrayOfByte)
    throws IOException
  {
    readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 0;
    while (i < paramInt2)
    {
      int j = read(paramArrayOfByte, i, paramInt2 - i);
      if (j == -1)
        throw new EOFException();
      i += j;
    }
  }

  public String readLine()
    throws IOException
  {
    return readString();
  }

  public int readUnsignedByte()
    throws IOException
  {
    return readByte() & 0xFF;
  }

  public int readUnsignedShort()
    throws IOException
  {
    return readShort() & 0xFFFF;
  }

  public String readUTF()
    throws IOException
  {
    return readString("UTF-8");
  }

  public int skipBytes(int paramInt)
    throws IOException
  {
    return (int)skip(paramInt);
  }

  private static boolean isDigit(int paramInt)
  {
    return (48 <= paramInt) && (paramInt <= 57);
  }
}