package com.ifreeshare.dht.crawler.coding;

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BencodingOutputStream extends FilterOutputStream
  implements DataOutput
{
  private final String encoding;

  public BencodingOutputStream(OutputStream paramOutputStream)
  {
    this(paramOutputStream, "UTF-8");
  }

  public BencodingOutputStream(OutputStream paramOutputStream, String paramString)
  {
    super(paramOutputStream);
    if (paramString == null)
      throw new NullPointerException("encoding");
    this.encoding = paramString;
  }

  public String getEncoding()
  {
    return this.encoding;
  }

  public void writeObject(Object paramObject)
    throws IOException
  {
    if (paramObject == null)
      writeNull();
    else if ((paramObject instanceof byte[]))
      writeBytes((byte[])(byte[])paramObject);
    else if ((paramObject instanceof Boolean))
      writeBoolean(((Boolean)paramObject).booleanValue());
    else if ((paramObject instanceof Character))
      writeChar(((Character)paramObject).charValue());
    else if ((paramObject instanceof Number))
      writeNumber((Number)paramObject);
    else if ((paramObject instanceof String))
      writeString((String)paramObject);
    else if ((paramObject instanceof Collection))
      writeCollection((Collection)paramObject);
    else if ((paramObject instanceof Map))
      writeMap((Map)paramObject);
    else if ((paramObject instanceof Enum))
      writeEnum((Enum)paramObject);
    else if (paramObject.getClass().isArray())
      writeArray(paramObject);
    else
      writeCustom(paramObject);
  }

  public void writeNull()
    throws IOException
  {
    throw new IOException("Null is not supported");
  }

  protected void writeCustom(Object paramObject)
    throws IOException
  {
    throw new IOException("Cannot bencode " + paramObject);
  }

  public void writeBytes(byte[] paramArrayOfByte)
    throws IOException
  {
    writeBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    write(Integer.toString(paramInt2).getBytes(this.encoding));
    write(58);
    write(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void writeBoolean(boolean paramBoolean)
    throws IOException
  {
    writeNumber(paramBoolean ? BencodingUtils.TRUE : BencodingUtils.FALSE);
  }

  public void writeChar(int paramInt)
    throws IOException
  {
    writeString(Character.toString((char)paramInt));
  }

  public void writeByte(int paramInt)
    throws IOException
  {
    writeNumber(Byte.valueOf((byte)paramInt));
  }

  public void writeShort(int paramInt)
    throws IOException
  {
    writeNumber(Short.valueOf((short)paramInt));
  }

  public void writeInt(int paramInt)
    throws IOException
  {
    writeNumber(Integer.valueOf(paramInt));
  }

  public void writeLong(long paramLong)
    throws IOException
  {
    writeNumber(Long.valueOf(paramLong));
  }

  public void writeFloat(float paramFloat)
    throws IOException
  {
    writeNumber(Float.valueOf(paramFloat));
  }

  public void writeDouble(double paramDouble)
    throws IOException
  {
    writeNumber(Double.valueOf(paramDouble));
  }

  public void writeNumber(Number paramNumber)
    throws IOException
  {
    String str = paramNumber.toString();
    write(105);
    write(str.getBytes(this.encoding));
    write(101);
  }

  public void writeString(String paramString)
    throws IOException
  {
    writeBytes(paramString.getBytes(this.encoding));
  }

  public void writeCollection(Collection<?> paramCollection)
    throws IOException
  {
    write(108);
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      writeObject(localObject);
    }
    write(101);
  }

  public void writeMap(Map<?, ?> paramMap)
    throws IOException
  {
    if (!(paramMap instanceof SortedMap))
      paramMap = new TreeMap(paramMap);
    write(100);
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getKey();
      Object localObject2 = localEntry.getValue();
      if ((localObject1 instanceof String))
        writeString((String)localObject1);
      else
        writeBytes((byte[])(byte[])localObject1);
      writeObject(localObject2);
    }
    write(101);
  }

  public void writeEnum(Enum<?> paramEnum)
    throws IOException
  {
    writeString(paramEnum.name());
  }

  public void writeArray(Object paramObject)
    throws IOException
  {
    write(108);
    int i = Array.getLength(paramObject);
    for (int j = 0; j < i; j++)
      writeObject(Array.get(paramObject, j));
    write(101);
  }

  public void writeBytes(String paramString)
    throws IOException
  {
    writeString(paramString);
  }

  public void writeChars(String paramString)
    throws IOException
  {
    writeString(paramString);
  }

  public void writeUTF(String paramString)
    throws IOException
  {
    writeBytes(paramString.getBytes("UTF-8"));
  }
}