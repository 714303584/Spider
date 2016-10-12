package com.ifreeshare.spider.core;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;

import com.ifreeshare.spider.redis.RedisPool;

import io.vertx.core.json.JsonObject;

/**
 * @author zhuss
 * @date 2016年9月19日下午5:26:20
 */
public class CoreBase {
	
	/**
	 * redis  key for image
	 */
	public static final String MD5_UUID_IMAGE = "md5_uuid_image_ifreeshare_com";

	public static final String SHA1_UUID_IMAGE = "sha1_uuid_image_ifreeshare_com";

	public static final String SHA512_UUID_IMAGE = "sha512_uuid_image_ifreeshare_com";
	
	//Image Storage Address  File Type :jpg,jpeg, png, icon........ Picture  Photo
	public static final String UUID_MD5_SHA1_SHA512_IMAGES_KEY = "uuid_md5_sha1_sha512_images_info_ifreeshare_com";
	
	public static final String MD5_SHA1_SHA512_EXIST_IMAGES_KEY = "md5_sha1_sha512_exist_image_key_ifreeshare_com";

	
	/**
	 * redis key for file
	 */
	public static final String MD5_UUID_FILE = "md5_uuid_file_ifreeshare_com";

	public static final String SHA1_UUID_FILE = "sha1_uuid_file_ifreeshare_com";

	public static final String SHA512_UUID_FILE = "sha512_uuid_file_ifreeshare_com";
	
	//File Storage Address  File Type :pdf ,doc , exel........ Document
	public static final String UUID_MD5_SHA1_SHA512_FILE_KEY = "uuid_md5_sha1_sha512_file_info_ifreeshare_com";
	
	public static final String MD5_SHA1_SHA512_EXIST_FILE_KEY = "md5_sha1_sha512_exist_file_key_ifreeshare_com";
	
	/**
	 * redis key for compressed file
	 */
	public static final String MD5_UUID_COMPRESSED_FILE = "md5_uuid_compressed_file_ifreeshare_com";

	public static final String SHA1_UUID_COMPRESSED_FILE = "sha1_uuid_compressed_file_ifreeshare_com";

	public static final String SHA512_UUID_COMPRESSED_FILE = "sha512_uuid_compressed_file_ifreeshare_com";
	
	//Compressed File Storage Address  File Type :rar ,zip, tar,tar.gz........ Compressed
	public static final String UUID_MD5_SHA1_SHA512_COMPRESSED_FILE_KEY = "uuid_md5_sha1_sha512_compressed_file_info_ifreeshare_com";
	public static final String MD5_SHA1_SHA512_EXIST_COMPRESSED_FILE_KEY = "md5_sha1_sha512_exist_compressed_file_key_ifreeshare_com";
	
	
	//Some Abnormal file information into The Address
	public static final String EXCEPTION_FILE_KEY = "md5_sha1_sha512_exception_file_key_ifreeshare_com";
	
	
	public static final String DOT = ".";
	
	public static final String UUID = "uuid";
	
	public static final String MD5 = "md5";

	public static final String SHA512 = "sha512";
	public static final String SHA1 = "sha1";
	
	public static final String RESOLUTION = "resolution";

	public static final String FILE_SIZE = "fileSize";
	
	public static final String SOURCE_ADDRESS = "sourceAddress";
	
	public static final String HTML_TITLE = "title";
	
	public static final String HTML_KEYWORDS = "keywords";
	
	public static final String HTML_DESCRIPTION = "description";
	
	public static final String Content_Type = "Content-Type";
	
	public static final String  TEXT_HTML = "text/html";
	
	public static final String  OBJECTS = "objects";
	
	public  static final  String FILE_PATH = "file_path";
	public static final String FILE_STATUS = "status";
	
	
	
    public static final String TOKEN = "token";
    public static final String ACCOUNTS = "accounts";
    public static final String MODE = "mode";
    public static final String PROTOCOL = "protocol";
    public static final String PASSWORD = "password";
    public static final String TIMESTAMP = "timestamp";
    public static final String EXPIRE = "user_expire";
    public static final String LAST_CHANGE = "last_change_time";
    public static final String TTL = "ttl";
    public static final String STATUS = "status";
    public static final String VERSION = "version";
    public static final String PHOTO = "photo";
    public static final String AVATOR ="avator";
    public static final String DEPT_NUMBER = "departmentNumber";
    public static final String ENTERPRISE_CODE = "comcode";
    public static final String ADDRESS = "address";
    public static final String POSTCODE = "postcode";
    public static final String URL = "url";
    public static final String PHONE = "phone";
    public static final String ONUSE = "onuse";
    public static final String LIMIT = "upper_limit";
    public static final String RECORDS = "records";
    public static final String DATA = "data";
	
	
	/**
	 * File Type 
	 */
	public static final String FILE_TYPE_RAR = "rar";
	public static final String FILE_TYPE_ZIP = "zip";
	public static final String FILE_TYPE_PDF = "pdf";
	public static final String FILE_TYPE_DOC = "doc";
	
	/**
	 * Add and Update the Info 
	 * @param info  Calculated file information 
	 * @param uuid Unique identifier for system generation 
	 * @param md5  The md5 of file. ---------- Obtain calculation 
	 * @param sha1 The sha1 of file. ---------- Obtain calculation 
	 * @param sha512 The sha512 of file. ---------- Obtain calculation 
	 */
	public static void setUUid(JsonObject info, String uuid, String md5, String sha1, String sha512){
		info.put(CoreBase.UUID, uuid);
		info.put(CoreBase.MD5, md5);
		info.put(CoreBase.SHA1, sha1);
		info.put(CoreBase.SHA512, sha512);
	}
	
	
	/**
	 * Add Data Information Full Text Search
	 * @param uuid 
	 * @param fileInfo
	 * @throws IOException
	 */
	
	public static void saveFileToLucene(JsonObject fileInfo, IndexWriter writer) throws IOException{
		Document document = new  Document();
		document.add( new StringField(CoreBase.UUID, fileInfo.getString(CoreBase.UUID), Store.YES));
		document.add( new StringField(CoreBase.FILE_PATH, fileInfo.getString(CoreBase.FILE_PATH), Store.YES));
		document.add(new Field(CoreBase.HTML_KEYWORDS, fileInfo.getString(CoreBase.HTML_KEYWORDS), TextField.TYPE_STORED));
		document.add(new Field(CoreBase.HTML_TITLE, fileInfo.getString(CoreBase.HTML_TITLE), TextField.TYPE_STORED));
		document.add(new Field(CoreBase.HTML_DESCRIPTION, fileInfo.getString(CoreBase.HTML_DESCRIPTION), TextField.TYPE_STORED));
		writer.addDocument(document);
		writer.flush();
		writer.commit();
	}
	
	
	
	public static void saveExceptionFileInfo(JsonObject value){
		RedisPool.addfield(CoreBase.EXCEPTION_FILE_KEY, value.getString(CoreBase.UUID), value.toString());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
