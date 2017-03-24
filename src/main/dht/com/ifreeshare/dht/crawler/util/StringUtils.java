package com.ifreeshare.dht.crawler.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtils
{
	
	 private static final Map<String, Charset> encodingToCharsetCache = new HashMap();
	 
	 public static final Charset ISO_8859_1;
	  public static final Charset UTF_8;
	 
  private static byte[] getBytes(String string, Charset charset)
  {
    if (string == null) {
      return null;
    }
    return string.getBytes(charset);
  }

  public static byte[] getBytesUtf8(String string)
  {
    return getBytes(string, UTF_8);
  }

  private static String newString(byte[] bytes, Charset charset)
  {
    return bytes == null ? null : new String(bytes, charset);
  }

  public static String newStringUtf8(byte[] bytes)
  {
    return newString(bytes, UTF_8);
  }
  
  public static Charset getCharset(String enc)
		    throws UnsupportedEncodingException
		  {
		    String lowerCaseEnc = enc.toLowerCase(Locale.ENGLISH);

		    return getCharsetLower(lowerCaseEnc);
		  }
  
  public static Charset getCharsetLower(String lowerCaseEnc)
		    throws UnsupportedEncodingException
		  {
		    Charset charset = (Charset)encodingToCharsetCache.get(lowerCaseEnc);

		    if (charset == null)
		    {
		      throw new UnsupportedEncodingException("b2cConverter.unknownEncoding");
		    }

		    return charset;
		  }
  
  
  
  static
  {
    for (Iterator i$ = Charset.availableCharsets().values().iterator(); i$.hasNext(); ) {
    	Charset charset = (Charset)i$.next();
      encodingToCharsetCache.put(charset.name().toLowerCase(Locale.ENGLISH), charset);

      for (String alias : charset.aliases())
        encodingToCharsetCache.put(alias.toLowerCase(Locale.ENGLISH), charset);
    }
    Charset charset;
    Charset iso88591 = null;
    Charset utf8 = null;
    try {
      iso88591 = getCharset("ISO-8859-1");
      utf8 = getCharset("UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    ISO_8859_1 = iso88591;
    UTF_8 = utf8;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static final String EMPTY = "";
  public static final int INDEX_NOT_FOUND = -1;
  private static final int PAD_LIMIT = 8192;

  public static boolean isEmpty(String str)
  {
    return (str == null) || (str.length() == 0);
  }

  public static boolean isNotEmpty(String str)
  {
    return !isEmpty(str);
  }

  public static boolean isBlank(String str)
  {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0))
      return true;
    
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(String str)
  {
    return !isBlank(str);
  }

  /** @deprecated */
  public static String clean(String str)
  {
    return str == null ? "" : str.trim();
  }

  public static String trim(String str)
  {
    return str == null ? null : str.trim();
  }

  public static String trimToNull(String str)
  {
    String ts = trim(str);
    return isEmpty(ts) ? null : ts;
  }

  public static String trimToEmpty(String str)
  {
    return str == null ? "" : str.trim();
  }

  public static String strip(String str)
  {
    return strip(str, null);
  }

  public static String stripToNull(String str)
  {
    if (str == null) {
      return null;
    }
    str = strip(str, null);
    return str.length() == 0 ? null : str;
  }

  public static String stripToEmpty(String str)
  {
    return str == null ? "" : strip(str, null);
  }

  public static String strip(String str, String stripChars)
  {
    if (isEmpty(str)) {
      return str;
    }
    str = stripStart(str, stripChars);
    return stripEnd(str, stripChars);
  }

  public static String stripStart(String str, String stripChars)
  {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0))
      return str;
    int start = 0;
    if (stripChars == null) {
      while ((start != strLen) && (Character.isWhitespace(str.charAt(start))))
        start++;
    }
    if (stripChars.length() == 0) {
      return str;
    }
    while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
      start++;
    }

    return str.substring(start);
  }

  public static String stripEnd(String str, String stripChars)
  {
    int end;
    if ((str == null) || ((end = str.length()) == 0))
      return str;
    if (stripChars == null) {
      while ((end != 0) && (Character.isWhitespace(str.charAt(end - 1))))
        end--;
    }
    if (stripChars.length() == 0) {
      return str;
    }
    while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
      end--;
    }

    return str.substring(0, end);
  }

  public static String[] stripAll(String[] strs)
  {
    return stripAll(strs, null);
  }

  public static String[] stripAll(String[] strs, String stripChars)
  {
    int strsLen;
    if ((strs == null) || ((strsLen = strs.length) == 0))
      return strs;
    String[] newArr = new String[strsLen];
    for (int i = 0; i < strsLen; i++) {
      newArr[i] = strip(strs[i], stripChars);
    }
    return newArr;
  }

  public static boolean equals(String str1, String str2)
  {
    return str1 == null ? false : str2 == null ? true : str1.equals(str2);
  }

  public static boolean equalsIgnoreCase(String str1, String str2)
  {
    return str1 == null ? false : str2 == null ? true : str1.equalsIgnoreCase(str2);
  }

  public static int indexOf(String str, char searchChar)
  {
    if (isEmpty(str)) {
      return -1;
    }
    return str.indexOf(searchChar);
  }

  public static int indexOf(String str, char searchChar, int startPos)
  {
    if (isEmpty(str)) {
      return -1;
    }
    return str.indexOf(searchChar, startPos);
  }

  public static int indexOf(String str, String searchStr)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }
    return str.indexOf(searchStr);
  }

  public static int ordinalIndexOf(String str, String searchStr, int ordinal)
  {
    return ordinalIndexOf(str, searchStr, ordinal, false);
  }

  private static int ordinalIndexOf(String str, String searchStr, int ordinal, boolean lastIndex)
  {
    if ((str == null) || (searchStr == null) || (ordinal <= 0)) {
      return -1;
    }
    if (searchStr.length() == 0) {
      return lastIndex ? str.length() : 0;
    }
    int found = 0;
    int index = lastIndex ? str.length() : -1;
    do {
      if (lastIndex)
        index = str.lastIndexOf(searchStr, index - 1);
      else {
        index = str.indexOf(searchStr, index + 1);
      }
      if (index < 0) {
        return index;
      }
      found++;
    }while (found < ordinal);
    return index;
  }

  public static int indexOf(String str, String searchStr, int startPos)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }

    if ((searchStr.length() == 0) && (startPos >= str.length())) {
      return str.length();
    }
    return str.indexOf(searchStr, startPos);
  }

  public static int indexOfIgnoreCase(String str, String searchStr)
  {
    return indexOfIgnoreCase(str, searchStr, 0);
  }

  public static int indexOfIgnoreCase(String str, String searchStr, int startPos)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }
    if (startPos < 0) {
      startPos = 0;
    }
    int endLimit = str.length() - searchStr.length() + 1;
    if (startPos > endLimit) {
      return -1;
    }
    if (searchStr.length() == 0) {
      return startPos;
    }
    for (int i = startPos; i < endLimit; i++) {
      if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOf(String str, char searchChar)
  {
    if (isEmpty(str)) {
      return -1;
    }
    return str.lastIndexOf(searchChar);
  }

  public static int lastIndexOf(String str, char searchChar, int startPos)
  {
    if (isEmpty(str)) {
      return -1;
    }
    return str.lastIndexOf(searchChar, startPos);
  }

  public static int lastIndexOf(String str, String searchStr)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }
    return str.lastIndexOf(searchStr);
  }

  public static int lastOrdinalIndexOf(String str, String searchStr, int ordinal)
  {
    return ordinalIndexOf(str, searchStr, ordinal, true);
  }

  public static int lastIndexOf(String str, String searchStr, int startPos)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }
    return str.lastIndexOf(searchStr, startPos);
  }

  public static int lastIndexOfIgnoreCase(String str, String searchStr)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }
    return lastIndexOfIgnoreCase(str, searchStr, str.length());
  }

  public static int lastIndexOfIgnoreCase(String str, String searchStr, int startPos)
  {
    if ((str == null) || (searchStr == null)) {
      return -1;
    }
    if (startPos > str.length() - searchStr.length()) {
      startPos = str.length() - searchStr.length();
    }
    if (startPos < 0) {
      return -1;
    }
    if (searchStr.length() == 0) {
      return startPos;
    }

    for (int i = startPos; i >= 0; i--) {
      if (str.regionMatches(true, i, searchStr, 0, searchStr.length())) {
        return i;
      }
    }
    return -1;
  }

  public static boolean contains(String str, char searchChar)
  {
    if (isEmpty(str)) {
      return false;
    }
    return str.indexOf(searchChar) >= 0;
  }

  public static boolean contains(String str, String searchStr)
  {
    if ((str == null) || (searchStr == null)) {
      return false;
    }
    return str.indexOf(searchStr) >= 0;
  }

  public static boolean containsIgnoreCase(String str, String searchStr)
  {
    if ((str == null) || (searchStr == null)) {
      return false;
    }
    int len = searchStr.length();
    int max = str.length() - len;
    for (int i = 0; i <= max; i++) {
      if (str.regionMatches(true, i, searchStr, 0, len)) {
        return true;
      }
    }
    return false;
  }


  public static int indexOfAny(String str, String[] searchStrs)
  {
    if ((str == null) || (searchStrs == null)) {
      return -1;
    }
    int sz = searchStrs.length;

    int ret = 2147483647;

    int tmp = 0;
    for (int i = 0; i < sz; i++) {
      String search = searchStrs[i];
      if (search == null) {
        continue;
      }
      tmp = str.indexOf(search);
      if (tmp == -1)
      {
        continue;
      }
      if (tmp < ret) {
        ret = tmp;
      }
    }

    return ret == 2147483647 ? -1 : ret;
  }

  public static int lastIndexOfAny(String str, String[] searchStrs)
  {
    if ((str == null) || (searchStrs == null)) {
      return -1;
    }
    int sz = searchStrs.length;
    int ret = -1;
    int tmp = 0;
    for (int i = 0; i < sz; i++) {
      String search = searchStrs[i];
      if (search == null) {
        continue;
      }
      tmp = str.lastIndexOf(search);
      if (tmp > ret) {
        ret = tmp;
      }
    }
    return ret;
  }

  public static String substring(String str, int start)
  {
    if (str == null) {
      return null;
    }

    if (start < 0) {
      start = str.length() + start;
    }

    if (start < 0) {
      start = 0;
    }
    if (start > str.length()) {
      return "";
    }

    return str.substring(start);
  }

  public static String substring(String str, int start, int end)
  {
    if (str == null) {
      return null;
    }

    if (end < 0) {
      end = str.length() + end;
    }
    if (start < 0) {
      start = str.length() + start;
    }

    if (end > str.length()) {
      end = str.length();
    }

    if (start > end) {
      return "";
    }

    if (start < 0) {
      start = 0;
    }
    if (end < 0) {
      end = 0;
    }

    return str.substring(start, end);
  }

  public static String left(String str, int len)
  {
    if (str == null) {
      return null;
    }
    if (len < 0) {
      return "";
    }
    if (str.length() <= len) {
      return str;
    }
    return str.substring(0, len);
  }

  public static String right(String str, int len)
  {
    if (str == null) {
      return null;
    }
    if (len < 0) {
      return "";
    }
    if (str.length() <= len) {
      return str;
    }
    return str.substring(str.length() - len);
  }

  public static String mid(String str, int pos, int len)
  {
    if (str == null) {
      return null;
    }
    if ((len < 0) || (pos > str.length())) {
      return "";
    }
    if (pos < 0) {
      pos = 0;
    }
    if (str.length() <= pos + len) {
      return str.substring(pos);
    }
    return str.substring(pos, pos + len);
  }

  public static String substringBefore(String str, String separator)
  {
    if ((isEmpty(str)) || (separator == null)) {
      return str;
    }
    if (separator.length() == 0) {
      return "";
    }
    int pos = str.indexOf(separator);
    if (pos == -1) {
      return str;
    }
    return str.substring(0, pos);
  }

  public static String substringAfter(String str, String separator)
  {
    if (isEmpty(str)) {
      return str;
    }
    if (separator == null) {
      return "";
    }
    int pos = str.indexOf(separator);
    if (pos == -1) {
      return "";
    }
    return str.substring(pos + separator.length());
  }

  public static String substringBeforeLast(String str, String separator)
  {
    if ((isEmpty(str)) || (isEmpty(separator))) {
      return str;
    }
    int pos = str.lastIndexOf(separator);
    if (pos == -1) {
      return str;
    }
    return str.substring(0, pos);
  }

  public static String substringAfterLast(String str, String separator)
  {
    if (isEmpty(str)) {
      return str;
    }
    if (isEmpty(separator)) {
      return "";
    }
    int pos = str.lastIndexOf(separator);
    if ((pos == -1) || (pos == str.length() - separator.length())) {
      return "";
    }
    return str.substring(pos + separator.length());
  }

  public static String substringBetween(String str, String tag)
  {
    return substringBetween(str, tag, tag);
  }

  public static String substringBetween(String str, String open, String close)
  {
    if ((str == null) || (open == null) || (close == null)) {
      return null;
    }
    int start = str.indexOf(open);
    if (start != -1) {
      int end = str.indexOf(close, start + open.length());
      if (end != -1) {
        return str.substring(start + open.length(), end);
      }
    }
    return null;
  }

  /** @deprecated */
  public static String getNestedString(String str, String tag)
  {
    return substringBetween(str, tag, tag);
  }

  /** @deprecated */
  public static String getNestedString(String str, String open, String close)
  {
    return substringBetween(str, open, close);
  }






  /** @deprecated */
  public static String concatenate(Object[] array)
  {
    return join(array, null);
  }

  public static String join(Object[] array)
  {
    return join(array, null);
  }

  public static String join(Object[] array, char separator)
  {
    if (array == null) {
      return null;
    }

    return join(array, separator, 0, array.length);
  }

  public static String join(Object[] array, char separator, int startIndex, int endIndex)
  {
    if (array == null) {
      return null;
    }
    int bufSize = endIndex - startIndex;
    if (bufSize <= 0) {
      return "";
    }

    bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
    StringBuilder buf = new StringBuilder(bufSize);

    for (int i = startIndex; i < endIndex; i++) {
      if (i > startIndex) {
        buf.append(separator);
      }
      if (array[i] != null) {
        buf.append(array[i]);
      }
    }
    return buf.toString();
  }

  public static String join(Object[] array, String separator)
  {
    if (array == null) {
      return null;
    }
    return join(array, separator, 0, array.length);
  }

  public static String join(Object[] array, String separator, int startIndex, int endIndex)
  {
    if (array == null) {
      return null;
    }
    if (separator == null) {
      separator = "";
    }

    int bufSize = endIndex - startIndex;
    if (bufSize <= 0) {
      return "";
    }

    bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

    StringBuilder buf = new StringBuilder(bufSize);

    for (int i = startIndex; i < endIndex; i++) {
      if (i > startIndex) {
        buf.append(separator);
      }
      if (array[i] != null) {
        buf.append(array[i]);
      }
    }
    return buf.toString();
  }



  public static String deleteWhitespace(String str)
  {
    if (isEmpty(str)) {
      return str;
    }
    int sz = str.length();
    char[] chs = new char[sz];
    int count = 0;
    for (int i = 0; i < sz; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        chs[(count++)] = str.charAt(i);
      }
    }
    if (count == sz) {
      return str;
    }
    return new String(chs, 0, count);
  }

  public static String removeStart(String str, String remove)
  {
    if ((isEmpty(str)) || (isEmpty(remove))) {
      return str;
    }
    if (str.startsWith(remove)) {
      return str.substring(remove.length());
    }
    return str;
  }


  public static String removeEnd(String str, String remove)
  {
    if ((isEmpty(str)) || (isEmpty(remove))) {
      return str;
    }
    if (str.endsWith(remove)) {
      return str.substring(0, str.length() - remove.length());
    }
    return str;
  }

  public static String removeEndIgnoreCase(String str, String remove)
  {
    if ((isEmpty(str)) || (isEmpty(remove))) {
      return str;
    }
    if (endsWithIgnoreCase(str, remove)) {
      return str.substring(0, str.length() - remove.length());
    }
    return str;
  }

  public static String remove(String str, String remove)
  {
    if ((isEmpty(str)) || (isEmpty(remove))) {
      return str;
    }
    return replace(str, remove, "", -1);
  }

  public static String remove(String str, char remove)
  {
    if ((isEmpty(str)) || (str.indexOf(remove) == -1)) {
      return str;
    }
    char[] chars = str.toCharArray();
    int pos = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] != remove) {
        chars[(pos++)] = chars[i];
      }
    }
    return new String(chars, 0, pos);
  }

  public static String replaceOnce(String text, String searchString, String replacement)
  {
    return replace(text, searchString, replacement, 1);
  }

  public static String replace(String text, String searchString, String replacement)
  {
    return replace(text, searchString, replacement, -1);
  }

  public static String replace(String text, String searchString, String replacement, int max)
  {
    if ((isEmpty(text)) || (isEmpty(searchString)) || (replacement == null) || (max == 0)) {
      return text;
    }
    int start = 0;
    int end = text.indexOf(searchString, start);
    if (end == -1) {
      return text;
    }
    int replLength = searchString.length();
    int increase = replacement.length() - replLength;
    increase = increase < 0 ? 0 : increase;
    increase *= (max > 64 ? 64 : max < 0 ? 16 : max);
    StringBuilder buf = new StringBuilder(text.length() + increase);
    while (end != -1) {
      buf.append(text.substring(start, end)).append(replacement);
      start = end + replLength;
      max--; if (max == 0) {
        break;
      }
      end = text.indexOf(searchString, start);
    }
    buf.append(text.substring(start));
    return buf.toString();
  }


  public static String replaceChars(String str, char searchChar, char replaceChar)
  {
    if (str == null) {
      return null;
    }
    return str.replace(searchChar, replaceChar);
  }

  public static String replaceChars(String str, String searchChars, String replaceChars)
  {
    if ((isEmpty(str)) || (isEmpty(searchChars))) {
      return str;
    }
    if (replaceChars == null) {
      replaceChars = "";
    }
    boolean modified = false;
    int replaceCharsLength = replaceChars.length();
    int strLength = str.length();
    StringBuilder buf = new StringBuilder(strLength);
    for (int i = 0; i < strLength; i++) {
      char ch = str.charAt(i);
      int index = searchChars.indexOf(ch);
      if (index >= 0) {
        modified = true;
        if (index < replaceCharsLength)
          buf.append(replaceChars.charAt(index));
      }
      else {
        buf.append(ch);
      }
    }
    if (modified) {
      return buf.toString();
    }
    return str;
  }

  /** @deprecated */
  public static String overlayString(String text, String overlay, int start, int end)
  {
    return new StringBuilder(start + overlay.length() + text.length() - end + 1).append(text.substring(0, start)).append(overlay).append(text.substring(end)).toString();
  }

  public static String overlay(String str, String overlay, int start, int end)
  {
    if (str == null) {
      return null;
    }
    if (overlay == null) {
      overlay = "";
    }
    int len = str.length();
    if (start < 0) {
      start = 0;
    }
    if (start > len) {
      start = len;
    }
    if (end < 0) {
      end = 0;
    }
    if (end > len) {
      end = len;
    }
    if (start > end) {
      int temp = start;
      start = end;
      end = temp;
    }
    return new StringBuilder(len + start - end + overlay.length() + 1).append(str.substring(0, start)).append(overlay).append(str.substring(end)).toString();
  }

  public static String chomp(String str)
  {
    if (isEmpty(str)) {
      return str;
    }

    if (str.length() == 1) {
      char ch = str.charAt(0);
      if ((ch == '\r') || (ch == '\n')) {
        return "";
      }
      return str;
    }

    int lastIdx = str.length() - 1;
    char last = str.charAt(lastIdx);

    if (last == '\n') {
      if (str.charAt(lastIdx - 1) == '\r')
        lastIdx--;
    }
    else if (last != '\r') {
      lastIdx++;
    }
    return str.substring(0, lastIdx);
  }

  public static String chomp(String str, String separator)
  {
    if ((isEmpty(str)) || (separator == null)) {
      return str;
    }
    if (str.endsWith(separator)) {
      return str.substring(0, str.length() - separator.length());
    }
    return str;
  }

  /** @deprecated */
  public static String chompLast(String str)
  {
    return chompLast(str, "\n");
  }

  /** @deprecated */
  public static String chompLast(String str, String sep)
  {
    if (str.length() == 0) {
      return str;
    }
    String sub = str.substring(str.length() - sep.length());
    if (sep.equals(sub)) {
      return str.substring(0, str.length() - sep.length());
    }
    return str;
  }

  /** @deprecated */
  public static String getChomp(String str, String sep)
  {
    int idx = str.lastIndexOf(sep);
    if (idx == str.length() - sep.length())
      return sep;
    if (idx != -1) {
      return str.substring(idx);
    }
    return "";
  }

  /** @deprecated */
  public static String prechomp(String str, String sep)
  {
    int idx = str.indexOf(sep);
    if (idx == -1) {
      return str;
    }
    return str.substring(idx + sep.length());
  }

  /** @deprecated */
  public static String getPrechomp(String str, String sep)
  {
    int idx = str.indexOf(sep);
    if (idx == -1) {
      return "";
    }
    return str.substring(0, idx + sep.length());
  }

  public static String chop(String str)
  {
    if (str == null) {
      return null;
    }
    int strLen = str.length();
    if (strLen < 2) {
      return "";
    }
    int lastIdx = strLen - 1;
    String ret = str.substring(0, lastIdx);
    char last = str.charAt(lastIdx);
    if ((last == '\n') && 
      (ret.charAt(lastIdx - 1) == '\r')) {
      return ret.substring(0, lastIdx - 1);
    }

    return ret;
  }

  /** @deprecated */
  public static String chopNewline(String str)
  {
    int lastIdx = str.length() - 1;
    if (lastIdx <= 0) {
      return "";
    }
    char last = str.charAt(lastIdx);
    if (last == '\n') {
      if (str.charAt(lastIdx - 1) == '\r')
        lastIdx--;
    }
    else {
      lastIdx++;
    }
    return str.substring(0, lastIdx);
  }


  public static String repeat(String str, int repeat)
  {
    if (str == null) {
      return null;
    }
    if (repeat <= 0) {
      return "";
    }
    int inputLength = str.length();
    if ((repeat == 1) || (inputLength == 0)) {
      return str;
    }
    if ((inputLength == 1) && (repeat <= 8192)) {
      return padding(repeat, str.charAt(0));
    }

    int outputLength = inputLength * repeat;
    switch (inputLength) {
    case 1:
      char ch = str.charAt(0);
      char[] output1 = new char[outputLength];
      for (int i = repeat - 1; i >= 0; i--) {
        output1[i] = ch;
      }
      return new String(output1);
    case 2:
      char ch0 = str.charAt(0);
      char ch1 = str.charAt(1);
      char[] output2 = new char[outputLength];
      for (int i = repeat * 2 - 2; i >= 0; i--) {
        output2[i] = ch0;
        output2[(i + 1)] = ch1;

        i--;
      }

      return new String(output2);
    }
    StringBuilder buf = new StringBuilder(outputLength);
    for (int i = 0; i < repeat; i++) {
      buf.append(str);
    }
    return buf.toString();
  }

  public static String repeat(String str, String separator, int repeat)
  {
    if ((str == null) || (separator == null)) {
      return repeat(str, repeat);
    }

    String result = repeat(str + separator, repeat);
    return removeEnd(result, separator);
  }

  private static String padding(int repeat, char padChar)
    throws IndexOutOfBoundsException
  {
    if (repeat < 0) {
      throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
    }
    char[] buf = new char[repeat];
    for (int i = 0; i < buf.length; i++) {
      buf[i] = padChar;
    }
    return new String(buf);
  }

  public static String rightPad(String str, int size)
  {
    return rightPad(str, size, ' ');
  }

  public static String rightPad(String str, int size, char padChar)
  {
    if (str == null) {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0) {
      return str;
    }
    if (pads > 8192) {
      return rightPad(str, size, String.valueOf(padChar));
    }
    return str.concat(padding(pads, padChar));
  }

  public static String rightPad(String str, int size, String padStr)
  {
    if (str == null) {
      return null;
    }
    if (isEmpty(padStr)) {
      padStr = " ";
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str;
    }
    if ((padLen == 1) && (pads <= 8192)) {
      return rightPad(str, size, padStr.charAt(0));
    }

    if (pads == padLen)
      return str.concat(padStr);
    if (pads < padLen) {
      return str.concat(padStr.substring(0, pads));
    }
    char[] padding = new char[pads];
    char[] padChars = padStr.toCharArray();
    for (int i = 0; i < pads; i++) {
      padding[i] = padChars[(i % padLen)];
    }
    return str.concat(new String(padding));
  }

  public static String leftPad(String str, int size)
  {
    return leftPad(str, size, ' ');
  }

  public static String leftPad(String str, int size, char padChar)
  {
    if (str == null) {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0) {
      return str;
    }
    if (pads > 8192) {
      return leftPad(str, size, String.valueOf(padChar));
    }
    return padding(pads, padChar).concat(str);
  }

  public static String leftPad(String str, int size, String padStr)
  {
    if (str == null) {
      return null;
    }
    if (isEmpty(padStr)) {
      padStr = " ";
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str;
    }
    if ((padLen == 1) && (pads <= 8192)) {
      return leftPad(str, size, padStr.charAt(0));
    }

    if (pads == padLen)
      return padStr.concat(str);
    if (pads < padLen) {
      return padStr.substring(0, pads).concat(str);
    }
    char[] padding = new char[pads];
    char[] padChars = padStr.toCharArray();
    for (int i = 0; i < pads; i++) {
      padding[i] = padChars[(i % padLen)];
    }
    return new String(padding).concat(str);
  }

  public static int length(String str)
  {
    return str == null ? 0 : str.length();
  }

  public static String center(String str, int size)
  {
    return center(str, size, ' ');
  }

  public static String center(String str, int size, char padChar)
  {
    if ((str == null) || (size <= 0)) {
      return str;
    }
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str;
    }
    str = leftPad(str, strLen + pads / 2, padChar);
    str = rightPad(str, size, padChar);
    return str;
  }

  public static String center(String str, int size, String padStr)
  {
    if ((str == null) || (size <= 0)) {
      return str;
    }
    if (isEmpty(padStr)) {
      padStr = " ";
    }
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str;
    }
    str = leftPad(str, strLen + pads / 2, padStr);
    str = rightPad(str, size, padStr);
    return str;
  }

  public static String upperCase(String str)
  {
    if (str == null) {
      return null;
    }
    return str.toUpperCase();
  }

  public static String upperCase(String str, Locale locale)
  {
    if (str == null) {
      return null;
    }
    return str.toUpperCase(locale);
  }

  public static String lowerCase(String str)
  {
    if (str == null) {
      return null;
    }
    return str.toLowerCase();
  }

  public static String lowerCase(String str, Locale locale)
  {
    if (str == null) {
      return null;
    }
    return str.toLowerCase(locale);
  }

  public static String capitalize(String str)
  {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0))
      return str;
    return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
  }

  /** @deprecated */
  public static String capitalise(String str)
  {
    return capitalize(str);
  }

  public static String uncapitalize(String str)
  {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0))
      return str;
    return new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
  }

  /** @deprecated */
  public static String uncapitalise(String str)
  {
    return uncapitalize(str);
  }

  public static String swapCase(String str)
  {
    int strLen;
    if ((str == null) || ((strLen = str.length()) == 0))
      return str;
    StringBuilder buffer = new StringBuilder(strLen);

    char ch = '\000';
    for (int i = 0; i < strLen; i++) {
      ch = str.charAt(i);
      if (Character.isUpperCase(ch))
        ch = Character.toLowerCase(ch);
      else if (Character.isTitleCase(ch))
        ch = Character.toLowerCase(ch);
      else if (Character.isLowerCase(ch)) {
        ch = Character.toUpperCase(ch);
      }
      buffer.append(ch);
    }
    return buffer.toString();
  }

  public static int countMatches(String str, String sub)
  {
    if ((isEmpty(str)) || (isEmpty(sub))) {
      return 0;
    }
    int count = 0;
    int idx = 0;
    while ((idx = str.indexOf(sub, idx)) != -1) {
      count++;
      idx += sub.length();
    }
    return count;
  }

  public static boolean isAlpha(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetter(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isAlphaSpace(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if ((!Character.isLetter(str.charAt(i))) && (str.charAt(i) != ' ')) {
        return false;
      }
    }
    return true;
  }

  public static boolean isAlphanumeric(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetterOrDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isAlphanumericSpace(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if ((!Character.isLetterOrDigit(str.charAt(i))) && (str.charAt(i) != ' ')) {
        return false;
      }
    }
    return true;
  }


  public static boolean isNumeric(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNumericSpace(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if ((!Character.isDigit(str.charAt(i))) && (str.charAt(i) != ' ')) {
        return false;
      }
    }
    return true;
  }

  public static boolean isWhitespace(String str)
  {
    if (str == null) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isAllLowerCase(String str)
  {
    if ((str == null) || (isEmpty(str))) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLowerCase(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isAllUpperCase(String str)
  {
    if ((str == null) || (isEmpty(str))) {
      return false;
    }
    int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isUpperCase(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static String defaultString(String str)
  {
    return str == null ? "" : str;
  }

  public static String defaultString(String str, String defaultStr)
  {
    return str == null ? defaultStr : str;
  }

  public static String defaultIfBlank(String str, String defaultStr)
  {
    return isBlank(str) ? defaultStr : str;
  }

  public static String defaultIfEmpty(String str, String defaultStr)
  {
    return isEmpty(str) ? defaultStr : str;
  }

  public static String reverse(String str)
  {
    if (str == null) {
      return null;
    }
    return new StringBuilder(str).reverse().toString();
  }



  public static String abbreviate(String str, int maxWidth)
  {
    return abbreviate(str, 0, maxWidth);
  }

  public static String abbreviate(String str, int offset, int maxWidth)
  {
    if (str == null) {
      return null;
    }
    if (maxWidth < 4) {
      throw new IllegalArgumentException("Minimum abbreviation width is 4");
    }
    if (str.length() <= maxWidth) {
      return str;
    }
    if (offset > str.length()) {
      offset = str.length();
    }
    if (str.length() - offset < maxWidth - 3) {
      offset = str.length() - (maxWidth - 3);
    }
    if (offset <= 4) {
      return str.substring(0, maxWidth - 3) + "...";
    }
    if (maxWidth < 7) {
      throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
    }
    if (offset + (maxWidth - 3) < str.length()) {
      return "..." + abbreviate(str.substring(offset), maxWidth - 3);
    }
    return "..." + str.substring(str.length() - (maxWidth - 3));
  }

  public static String abbreviateMiddle(String str, String middle, int length)
  {
    if ((isEmpty(str)) || (isEmpty(middle))) {
      return str;
    }

    if ((length >= str.length()) || (length < middle.length() + 2)) {
      return str;
    }

    int targetSting = length - middle.length();
    int startOffset = targetSting / 2 + targetSting % 2;
    int endOffset = str.length() - targetSting / 2;

    StringBuilder builder = new StringBuilder(length);
    builder.append(str.substring(0, startOffset));
    builder.append(middle);
    builder.append(str.substring(endOffset));

    return builder.toString();
  }


  public static boolean endsWith(String str, String suffix)
  {
    return endsWith(str, suffix, false);
  }

  public static boolean endsWithIgnoreCase(String str, String suffix)
  {
    return endsWith(str, suffix, true);
  }

  private static boolean endsWith(String str, String suffix, boolean ignoreCase)
  {
    if ((str == null) || (suffix == null)) {
      return (str == null) && (suffix == null);
    }
    if (suffix.length() > str.length()) {
      return false;
    }
    int strOffset = str.length() - suffix.length();
    return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
  }

  public static String normalizeSpace(String str)
  {
    str = strip(str);
    if ((str == null) || (str.length() <= 2)) {
      return str;
    }
    StringBuilder b = new StringBuilder(str.length());
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (Character.isWhitespace(c)) {
        if ((i > 0) && (!Character.isWhitespace(str.charAt(i - 1))))
          b.append(' ');
      }
      else {
        b.append(c);
      }
    }
    return b.toString();
  }

  
}