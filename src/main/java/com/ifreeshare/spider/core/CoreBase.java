package com.ifreeshare.spider.core;

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
	
	
	
	/**
	 * File Type 
	 */
	public static final String FILE_TYPE_RAR = "rar";
	public static final String FILE_TYPE_ZIP = "zip";
	public static final String FILE_TYPE_PDF = "pdf";
	public static final String FILE_TYPE_DOC = "doc";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
