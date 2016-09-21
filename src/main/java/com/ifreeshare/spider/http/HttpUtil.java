package com.ifreeshare.spider.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
	
	
	public static final String URL = "url";
	
	public static final String LINK_A = "a";
	
	public static final String LINK_A_HREF = "href";
	
	
	public static final String HTML_TITLE = "title";
	
	public static final String HTML_KEYWORDS = "keywords";
	
	public static final String HTML_DESCRIPTION = "description";
	
	public static final String Content_Type = "Content-Type";
	
	public static final String  TEXT_HTML = "text/html";
	
	public static final String IMAGE_JPEG = "image/jpeg";
	
	public static final String IMAGE_PNG = "image/png";
	
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream"; 
	public static final String APPLICATION_PDF = "application/pdf"; 
	//
	

	public static final String  CHARSET = "charset";
	
	public static final String FILENAME = "filename";
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36";

	
	
	
	
	
	

	public static Map<String, String> ContentType_Map_FileType = new HashMap<String, String>();
	
	
	

	
	
	
	
	static{
		ContentType_Map_FileType.put("application/octet-stream", null);
		
		
		ContentType_Map_FileType.put("image/jpeg", ".jpg");
		
		ContentType_Map_FileType.put("image/fax", ".fax");
		ContentType_Map_FileType.put("image/gif", ".gif");
		ContentType_Map_FileType.put("image/x-icon", ".ico");
		ContentType_Map_FileType.put("image/png", ".png");
		ContentType_Map_FileType.put("application/x-png", ".png");
		ContentType_Map_FileType.put("image/vnd.rn-realpix", ".rp");
		
		
		ContentType_Map_FileType.put("application/x-001", ".001");
		ContentType_Map_FileType.put("application/x-301", ".301");
		ContentType_Map_FileType.put("text/h323", ".323");
		ContentType_Map_FileType.put("application/x-906", ".906");
		ContentType_Map_FileType.put("application/x-a11", ".a11");
		ContentType_Map_FileType.put("drawing/907", ".907");

		ContentType_Map_FileType.put("audio/x-mei-aac", ".acp");
		ContentType_Map_FileType.put("application/postscript", ".ai");
		ContentType_Map_FileType.put("audio/aiff", ".aiff");
		ContentType_Map_FileType.put("application/x-anv", ".anv");
		ContentType_Map_FileType.put("text/asa", ".asa");
		ContentType_Map_FileType.put("video/x-ms-asf", ".asf");
		ContentType_Map_FileType.put("video/x-ms-asf", ".asx");
		ContentType_Map_FileType.put("text/asp", ".asp");
		ContentType_Map_FileType.put("audio/basic", ".au");
		ContentType_Map_FileType.put("video/avi", ".avi");
		ContentType_Map_FileType.put("text/xml", ".biz");
		ContentType_Map_FileType.put("application/vnd.adobe.workflow", ".awf");
		ContentType_Map_FileType.put("application/x-bmp", ".bmp");
		ContentType_Map_FileType.put("application/x-bot", ".bot");
		ContentType_Map_FileType.put("application/x-c4t", ".c4t");
		ContentType_Map_FileType.put("application/x-c90", ".c90");
		ContentType_Map_FileType.put("application/x-cals", ".cal");
		ContentType_Map_FileType.put("application/vnd.ms-pki.seccat", ".cat");
		ContentType_Map_FileType.put("application/x-netcdf", ".cdf");
		ContentType_Map_FileType.put("application/x-cdr", ".cdr");
		ContentType_Map_FileType.put("application/x-cel", ".cel");
		ContentType_Map_FileType.put("application/x-x509-ca-cert", ".cer");
		ContentType_Map_FileType.put("application/x-g4", ".cg4");
		ContentType_Map_FileType.put("application/x-cgm", ".cgm");
		
		
		ContentType_Map_FileType.put("application/x-cit", ".cit");
		ContentType_Map_FileType.put("java/*", ".class");		
//		ContentType_Map_FileType.put("text/xml", ".cml");
		ContentType_Map_FileType.put("application/x-cmp", ".cmp");
		ContentType_Map_FileType.put("application/x-cmx", ".cmx");
		ContentType_Map_FileType.put("application/x-cot", ".cot");
		ContentType_Map_FileType.put("application/pkix-crl", ".crl");
		ContentType_Map_FileType.put("application/x-x509-ca-cert", ".crt");
		ContentType_Map_FileType.put("application/x-csi", ".csi");
		ContentType_Map_FileType.put("text/css", ".css");
		ContentType_Map_FileType.put("application/x-cut", ".cut");
		ContentType_Map_FileType.put("application/x-dbf", ".dbf");
		ContentType_Map_FileType.put("application/x-dbm", ".dbm");
		ContentType_Map_FileType.put("application/x-dbx", ".dbx");
		ContentType_Map_FileType.put("text/xml", ".dcd");
		ContentType_Map_FileType.put("application/x-dcx", ".dcx");
		ContentType_Map_FileType.put("application/x-x509-ca-cert", ".der");
		ContentType_Map_FileType.put("application/x-dgn", ".dgn");
		ContentType_Map_FileType.put("application/x-dib", ".dib");
		ContentType_Map_FileType.put("application/x-msdownload", ".dll");
		ContentType_Map_FileType.put("application/msword", ".doc");
//		ContentType_Map_FileType.put("application/msword", ".dot");
		ContentType_Map_FileType.put("application/x-drw", ".drw");
//		ContentType_Map_FileType.put("text/xml", ".dtd");
		ContentType_Map_FileType.put("Model/vnd.dwf", ".dwf");
		ContentType_Map_FileType.put("application/x-dwf", ".dwf");
		ContentType_Map_FileType.put("application/x-dwg", ".dwg");
		ContentType_Map_FileType.put("application/x-dxb", ".dxb");
		ContentType_Map_FileType.put("application/x-dxf", ".dxf");
		ContentType_Map_FileType.put("application/vnd.adobe.edn", ".edn");
		ContentType_Map_FileType.put("application/x-emf", ".emf");
		ContentType_Map_FileType.put("message/rfc822", ".eml");
		ContentType_Map_FileType.put("text/xml", ".ent");
		ContentType_Map_FileType.put("application/x-epi", ".epi");
		ContentType_Map_FileType.put("application/x-ps", ".eps");
		ContentType_Map_FileType.put("application/postscript", ".eps");
		ContentType_Map_FileType.put("application/x-ebx", ".etd");
		ContentType_Map_FileType.put("application/x-msdownload", ".exe");
		ContentType_Map_FileType.put("image/fax", ".fax");
		ContentType_Map_FileType.put("application/vnd.fdf", ".fdf");
		ContentType_Map_FileType.put("application/fractals", ".fif");
//		ContentType_Map_FileType.put("text/xml", ".fo");
		ContentType_Map_FileType.put("application/x-g4", ".g4");
		ContentType_Map_FileType.put("application/x-frm", ".frm");
		ContentType_Map_FileType.put("application/x-gbr", ".gbr");
		ContentType_Map_FileType.put("application/x-", ".");
		ContentType_Map_FileType.put("application/x-gl2", ".gl2");
		ContentType_Map_FileType.put("application/x-gp4", ".gp4");
		ContentType_Map_FileType.put("application/x-hgl", ".hgl");
		ContentType_Map_FileType.put("application/x-hmr", ".hmr");
		ContentType_Map_FileType.put("application/x-hpgl", ".hpg");
		ContentType_Map_FileType.put("application/x-hpl", ".hpl");
		ContentType_Map_FileType.put("application/mac-binhex40", ".hqx");
		ContentType_Map_FileType.put("application/x-hrf", ".hrf");
		ContentType_Map_FileType.put("application/hta", ".hta");
		ContentType_Map_FileType.put("text/x-component", ".htc");
		ContentType_Map_FileType.put("application/x-ico", ".ico");
		ContentType_Map_FileType.put("application/x-iff", ".iff");
		
		ContentType_Map_FileType.put("application/x-g4", ".ig4");
		ContentType_Map_FileType.put("application/x-igs", ".igs");
		ContentType_Map_FileType.put("application/x-iphone", ".iii");
		ContentType_Map_FileType.put("application/x-img", ".img");
		ContentType_Map_FileType.put("application/x-internet-signup", ".ins");
		ContentType_Map_FileType.put("application/x-internet-signup", ".isp");
		ContentType_Map_FileType.put("video/x-ivf", ".IVF");
		ContentType_Map_FileType.put("java/*", ".java");
		ContentType_Map_FileType.put("application/x-jpe", ".jpe");
		ContentType_Map_FileType.put("application/x-jpg", ".jpg");
		ContentType_Map_FileType.put("application/x-javascript", ".js");
		ContentType_Map_FileType.put("audio/x-liquid-file", ".la1");
		ContentType_Map_FileType.put("application/x-laplayer-reg", ".lar");
		ContentType_Map_FileType.put("application/x-latex", ".latex");
		ContentType_Map_FileType.put("audio/x-liquid-secure", ".lavs");
		ContentType_Map_FileType.put("application/x-lbm", ".lbm");
		ContentType_Map_FileType.put("audio/x-la-lms", ".lmsff");
		ContentType_Map_FileType.put("application/x-javascript", ".ls");
		
		ContentType_Map_FileType.put("application/x-ltr", ".ltr");
		ContentType_Map_FileType.put("video/x-mpeg", ".m1v");
		ContentType_Map_FileType.put("video/x-mpeg", ".m2v");
		ContentType_Map_FileType.put("audio/mpegurl", ".m3u");
		ContentType_Map_FileType.put("video/mpeg4", ".m4e");
		ContentType_Map_FileType.put("application/x-mac", ".mac");
		ContentType_Map_FileType.put("application/x-troff-man", ".man");
		ContentType_Map_FileType.put("application/msaccess", ".mdb");
		ContentType_Map_FileType.put("application/x-mdb", ".mdb");
		ContentType_Map_FileType.put("application/x-shockwave-flash", ".mfp");
		ContentType_Map_FileType.put("message/rfc822", ".mht");
		
		
		ContentType_Map_FileType.put("message/rfc822", "..mhtml");
		ContentType_Map_FileType.put("application/x-mi", ".mi");
		ContentType_Map_FileType.put("audio/mid", ".midi");
		ContentType_Map_FileType.put("audio/mid", ".mid");
		ContentType_Map_FileType.put("application/x-mil", ".mil");
		ContentType_Map_FileType.put("audio/x-musicnet-download", ".mnd");
		ContentType_Map_FileType.put("audio/x-musicnet-stream", ".mns");
		ContentType_Map_FileType.put("application/x-javascript", ".mocha");
		ContentType_Map_FileType.put("video/x-sgi-movie", ".movie");
		ContentType_Map_FileType.put("audio/mp1", ".mp1");
		ContentType_Map_FileType.put("audio/mp2", ".mp2");
		ContentType_Map_FileType.put("video/mpeg", ".mp2v");
		ContentType_Map_FileType.put("audio/mp3", ".mp3");
		ContentType_Map_FileType.put("video/mpeg4", ".mp4");
		ContentType_Map_FileType.put("video/x-mpg", ".mpa");
		ContentType_Map_FileType.put("application/vnd.ms-project", ".mpd");
		ContentType_Map_FileType.put("video/x-mpeg", ".mpe");
		ContentType_Map_FileType.put("video/mpg", ".mpeg");
		ContentType_Map_FileType.put("video/mpg", ".mpg");
		
		ContentType_Map_FileType.put("audio/rn-mpeg", ".mpga");
		ContentType_Map_FileType.put("application/vnd.ms-project", ".mpp");
		ContentType_Map_FileType.put("video/x-mpeg", ".mps");
		ContentType_Map_FileType.put("application/vnd.ms-project", ".mpt");
		ContentType_Map_FileType.put("video/mpg", ".mpv");
		ContentType_Map_FileType.put("video/mpeg", ".mpv2");
		ContentType_Map_FileType.put("application/vnd.ms-project", ".mpw");
//		ContentType_Map_FileType.put("application/vnd.ms-project", ".mpx");
		ContentType_Map_FileType.put("application/x-mmxp", ".mxp");
		ContentType_Map_FileType.put("image/pnetvue", ".net");
		ContentType_Map_FileType.put("application/x-nrf", ".nrf");
		ContentType_Map_FileType.put("message/rfc822", ".nws");
		ContentType_Map_FileType.put("text/x-ms-odc", ".odc");
		ContentType_Map_FileType.put("application/x-out", ".out");
		ContentType_Map_FileType.put("application/pkcs10", ".p10");
		ContentType_Map_FileType.put("application/x-pkcs12", ".p12");
		ContentType_Map_FileType.put("application/x-pkcs7-certificates", ".p7b");
		ContentType_Map_FileType.put("application/pkcs7-mime", ".p7c");
		ContentType_Map_FileType.put("application/pkcs7-mime", ".p7m");
		ContentType_Map_FileType.put("application/x-pkcs7-certreqresp", ".p7r");
		
		
		ContentType_Map_FileType.put("application/pkcs7-signature", ".p7s");
		ContentType_Map_FileType.put("application/x-pc5", ".pc5");
		ContentType_Map_FileType.put("application/x-pci", ".pci");
		ContentType_Map_FileType.put("application/x-pcl", ".pcl");
		ContentType_Map_FileType.put("application/x-pcx", ".pcx");
		ContentType_Map_FileType.put("application/pdf", ".pdf");
		ContentType_Map_FileType.put("application/pdf", ".pdf");
		ContentType_Map_FileType.put("application/vnd.adobe.pdx", ".pdx");
		ContentType_Map_FileType.put("application/x-pkcs12", ".pfx");
		ContentType_Map_FileType.put("application/x-pgl", ".pgl");
		ContentType_Map_FileType.put("application/x-pic", ".pic");
		ContentType_Map_FileType.put("application/vnd.ms-pki.pko", ".pko");
		ContentType_Map_FileType.put("application/x-perl", ".pl");
		ContentType_Map_FileType.put("audio/scpls", ".pls");
		ContentType_Map_FileType.put("application/x-plt", ".plt");
		ContentType_Map_FileType.put("application/x-png", ".png");
		ContentType_Map_FileType.put("application/vnd.ms-powerpoint", ".pot");
		
		
		ContentType_Map_FileType.put("application/vnd.ms-powerpoint", ".ppa");
		ContentType_Map_FileType.put("application/x-ppm", ".ppm");
		ContentType_Map_FileType.put("application/vnd.ms-powerpoint", ".pps");
		ContentType_Map_FileType.put("application/vnd.ms-powerpoint", ".ppt");
		ContentType_Map_FileType.put("application/x-ppt", ".ppt");
		ContentType_Map_FileType.put("application/x-pr", ".pr");
		ContentType_Map_FileType.put("application/pics-rules", ".prf");
		ContentType_Map_FileType.put("application/x-prn", ".prn");
		ContentType_Map_FileType.put("application/x-prt", ".prt");
		
		
		ContentType_Map_FileType.put("application/x-ps", ".ps");
		ContentType_Map_FileType.put("application/postscript", ".ps");
		ContentType_Map_FileType.put("application/x-ptn", ".ptn");
//		ContentType_Map_FileType.put("application/vnd.ms-powerpoint", "pwz");
		ContentType_Map_FileType.put("text/vnd.rn-realtext3d", ".r3t");
		ContentType_Map_FileType.put("audio/vnd.rn-realaudio", ".ra");
		ContentType_Map_FileType.put("audio/x-pn-realaudio", ".ram");
		ContentType_Map_FileType.put("application/x-ras", ".ras");
		ContentType_Map_FileType.put("application/rat-file", ".rat");
		ContentType_Map_FileType.put("application/vnd.rn-recording", ".rec");
		ContentType_Map_FileType.put("application/x-red", ".red");
		ContentType_Map_FileType.put("application/x-rgb", ".rgb");
		ContentType_Map_FileType.put("application/vnd.rn-realsystem-rjs", ".rjs");
		ContentType_Map_FileType.put("application/vnd.rn-realsystem-rjt", ".rjt");
		ContentType_Map_FileType.put("application/x-rlc", ".rlc");
		ContentType_Map_FileType.put("application/x-rle", ".rle");
		
		
		ContentType_Map_FileType.put("application/vnd.rn-realmedia", ".rm");
		ContentType_Map_FileType.put("application/vnd.adobe.rmf", ".rmf");
		ContentType_Map_FileType.put("audio/mid", ".rmi");
		ContentType_Map_FileType.put("application/vnd.rn-realsystem-rmj", ".rmj");
		ContentType_Map_FileType.put("audio/x-pn-realaudio", ".rmm");
		ContentType_Map_FileType.put("application/vnd.rn-rn_music_package", ".rmp");
		ContentType_Map_FileType.put("application/vnd.rn-realmedia-secure", ".rms");
		ContentType_Map_FileType.put("application/vnd.rn-realmedia-vbr", ".rmvb");
		ContentType_Map_FileType.put("application/vnd.rn-realsystem-rmx", ".rmx");
		ContentType_Map_FileType.put("application/vnd.rn-realplayer", ".rnx");
		
		
		ContentType_Map_FileType.put("image/vnd.rn-realpix", ".rp");
		ContentType_Map_FileType.put("audio/x-pn-realaudio-plugin", ".rpm");
		ContentType_Map_FileType.put("application/vnd.rn-rsml", ".rsml");
		ContentType_Map_FileType.put("text/vnd.rn-realtext", ".rt");
		ContentType_Map_FileType.put("application/msword", ".rtf");
		ContentType_Map_FileType.put("application/x-rtf", ".rtf");
		ContentType_Map_FileType.put("video/vnd.rn-realvideo", ".rv");
		ContentType_Map_FileType.put("application/x-sam", ".sam");
		ContentType_Map_FileType.put("application/x-sat", ".sat");
		ContentType_Map_FileType.put("application/sdp", ".sdp");
		ContentType_Map_FileType.put("application/x-sdw", ".sdw");
		ContentType_Map_FileType.put("application/x-stuffit", ".sit");
		ContentType_Map_FileType.put("application/x-slb", ".slb");
		ContentType_Map_FileType.put("application/x-sld", ".sld");
		
		ContentType_Map_FileType.put("drawing/x-slk", ".slk");
		ContentType_Map_FileType.put("application/smil", ".smi");
		ContentType_Map_FileType.put("application/smil", ".smil");
		ContentType_Map_FileType.put("application/x-smk", ".smk");
		ContentType_Map_FileType.put("audio/basic", ".snd");
		ContentType_Map_FileType.put("application/x-pkcs7-certificates", ".spc");
		ContentType_Map_FileType.put("application/futuresplash", ".spl");
		ContentType_Map_FileType.put("application/streamingmedia", ".ssm");
		ContentType_Map_FileType.put("application/vnd.ms-pki.certstore", ".sst");
		ContentType_Map_FileType.put("application/vnd.ms-pki.stl", ".stl");
		ContentType_Map_FileType.put("application/x-shockwave-flash", ".swf");
		ContentType_Map_FileType.put("application/x-tdf", ".tdf");
		ContentType_Map_FileType.put("application/x-tg4", ".tg4");
		ContentType_Map_FileType.put("application/x-tga", ".tga");
		ContentType_Map_FileType.put("application/x-tif", ".tif");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		
		
		ContentType_Map_FileType.put("application/x-bittorrent", ".torrent");
		ContentType_Map_FileType.put("text/plain", ".txt");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		ContentType_Map_FileType.put("drawing/x-top", ".top");
		ContentType_Map_FileType.put("drawing/x-top", ".top");



		
		
		
	}
	
	
	
	
	
	
	
	
	
	public static String getFileType(String contentType){
		return ContentType_Map_FileType.get(contentType);
	}
	
	
	
	
	public static String getFileNameByContentDisposition(String contentDisposition){
		String fileName = null;
		if(contentDisposition != null){
			String[] contentSpilt = contentDisposition.split("=");
			if(contentSpilt.length > 1){
				fileName = contentSpilt[1];
				fileName = fileName.replaceAll("\"", "");
			}
		}
		return fileName;
	}
	
	
	
	public static String getDomain(String url) throws MalformedURLException{
		URL getDomain = new URL(url);
		String domain = getDomain.getHost();
		return domain;
	}
	
	
	public static String  getMainDomain(String url) throws MalformedURLException{
		String host = new URL(url).getHost().toLowerCase();
		Pattern p = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|"
				+ "\\.ac|\\.ad|\\.ae|\\.af|\\.ag|\\.ai|\\.al|\\.am|\\.an|\\.ao|\\.aq|\\.ar|\\.as|\\.at|\\.au|\\.aw|\\.az|\\.ba|"
				+ "\\.bb|\\.bd|\\.be|\\.bf|\\.bg|\\.bh|\\.bi|\\.bj|\\.bm|\\.bn|\\.bo|\\.br|\\.bs|\\.bt|\\.bv|\\.bw|\\.by|\\.bz|\\.ca|\\.cat|"
				+ "\\.cd|\\.cf|\\.cg|\\.ch|\\.ci|\\.ck|\\.cl|\\.cm|\\.co|\\.co\\.uk|\\.co\\.uz|\\.co\\.kr|\\.co\\.uz|\\.co\\.kr|\\.co\\.jp|\\.co\\.nr|\\.co\\.nr|\\.co\\.il|\\.co\\.id|"
				+ "\\.cq|\\.cr|\\.cu|\\.cv|\\.cx|\\.cy|\\.cz|\\.de|\\.dj|\\.dk|\\.dm|\\.do|\\.dz|\\.ec|\\.ee|\\.eg|\\.eh|\\.es|\\.et|\\.eu|\\.ev|\\.fi|\\.fj|\\.fk|\\.fm|\\.fo|\\.fr|\\.ga|"
				+ "\\.gb|\\.gd|\\.ge|\\.gf|\\.gh|\\.gi|\\.gl|\\.gm|\\.gn|\\.gp|\\.gr|\\.gt|\\.gu|\\.gw|\\.gy|\\.hk|\\.hm|\\.hn|\\.hr|\\.ht|\\.hu|\\.id|\\.ie|\\.il|\\.in|\\.io|\\.iq|\\.ir|"
				+ "\\.is|\\.it|\\.jm|\\.jo|\\.jp|\\.ke|\\.kg|\\.kh|\\.ki|\\.km|\\.kn|\\.kp|\\.kr|\\.kw|\\.ky|\\.kz|\\.la|\\.lb|\\.lc|\\.li|\\.lk|\\.lr|\\.ls|\\.lt|\\.lu|\\.lv|\\.ly|\\.ma|"
				+ "\\.mc|\\.md|\\.me|\\.mg|\\.mh|\\.ml|\\.mm|\\.mn|\\.mo|\\.mp|\\.mq|\\.mr|\\.ms|\\.mt|\\.mv|\\.mw|\\.mx|\\.my|\\.mz|\\.na|\\.nc|\\.ne|\\.nf|\\.ng|\\.ni|\\.nl|\\.no|\\.np|"
				+ "\\.nr|\\.nt|\\.nu|\\.om|\\.pa|\\.pe|\\.pf|\\.pg|\\.ph|\\.pk|\\.pl|\\.pn|\\.pr|\\.pt|\\.pw|\\.py|\\.qa|\\.re|\\.ro|\\.rs|\\.ru|\\.rw|\\.sa|\\.sb|\\.sc|\\.sd|\\.se|"
				+ "\\.sg|\\.sh|\\.si|\\.sj|\\.sk|\\.sl|\\.sm|\\.sn|\\.so|\\.sr|\\.st|\\.su|\\.sy|\\.sz|\\.tc|\\.td|\\.tf|\\.tg|\\.th|\\.tj|\\.tk|\\.tl|\\.tm|\\.tn|\\.to|\\.tp|\\.tr|"
				+ "\\.tt|\\.tw|\\.tz|\\.ua|\\.ug|\\.uk|\\.us|\\.uy|\\.va|\\.vc|\\.ve|\\.vg|\\.vn|\\.vu|\\.wf|\\.ws|\\.wf|\\.ws|\\.ye|\\.za|\\.zm|\\.zw|"
				+ "\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.xin|\\.win|\\.xzy|\\.top|\\.ltd|\\.vip|\\.pub|\\.wang|\\.club|\\.site|\\.store|\\.bid|\\.ren|"
				+ "\\.online|\\.tech|\\.mom|\\.lol|\\.work|\\.red|\\.website|\\.space|\\.link|\\.news|\\.date|\\.loan|\\.live|\\.studio|\\.help|\\.click|\\.pics|"
				+ "\\.photo|\\.trade|\\.science|\\.party|\\.rocks|\\.band|\\.gift|\\.wiki|\\.design|\\.software|\\.social|\\.lawyer|\\.engineer|\\.me|\\.co|\\.press|\\.video|"
				+ "\\.market|\\.game|"
				+ "\\.我爱你|\\.集团|\\.公司|\\.中国|\\.网络)");
		Matcher matcher = p.matcher(host);  
		if(matcher.find()) return matcher.group();
		else return null;

		
	}
	
	
	
	
	public static void main(String[] args) {
//		System.out.println(HttpUtil.getDomain("https://www.baidu.com/s?wd=java%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F%20%E6%8C%87%E5%AE%9A%E5%AD%97%E7%AC%A6%E4%B8%B2&rsv_spt=1&rsv_iqid=0x9b5994530000f315&issp=1&f=3&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=33&rsv_sug1=41&rsv_sug7=100&rsv_sug2=0&prefixsug=java%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F%20zhiding&rsp=0&inputT=28427&rsv_sug4=28428"));;
	
		String urlS = "http://ifreeshare.tf:81/201607/books/JMeterzwsc_jb51.rar";
		
		try {
			System.out.println(HttpUtil.getDomain(urlS));
		} catch (MalformedURLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
//		
//		System.out.println(urlss);
		
		
		try {
			System.out.println(getMainDomain(urlS));;
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	.rdf text/xml .  
//	.  .  
//	.  .  
//	.  .  
//	.  .sol text/plain 
//	.sor text/plain .  
//	.  .spp text/xml 
//	.  .  
//	.  .stm text/html 
//	.  .svg text/xml 
//	.  .  
//	.  .  
//	.tif image/tiff .  
//	.tiff image/tiff .tld text/xml 
//	.  .  
//	.tsd text/xml .  
//	.uin application/x-icq .uls text/iuls 
//	.vcf text/x-vcard .vda application/x-vda 
//	.vdx application/vnd.visio .vml text/xml 
//	.vpg application/x-vpeg005 .vsd application/vnd.visio 
//	.vsd application/x-vsd .vss application/vnd.visio 
//	.vst application/vnd.visio .vst application/x-vst 
//	.vsw application/vnd.visio .vsx application/vnd.visio 
//	.vtx application/vnd.visio .vxml text/xml 
//	.wav audio/wav .wax audio/x-ms-wax 
//	.wb1 application/x-wb1 .wb2 application/x-wb2 
//	.wb3 application/x-wb3 .wbmp image/vnd.wap.wbmp 
//	.wiz application/msword .wk3 application/x-wk3 
//	.wk4 application/x-wk4 .wkq application/x-wkq 
//	.wks application/x-wks .wm video/x-ms-wm 
//	.wma audio/x-ms-wma .wmd application/x-ms-wmd 
//	.wmf application/x-wmf .wml text/vnd.wap.wml 
//	.wmv video/x-ms-wmv .wmx video/x-ms-wmx 
//	.wmz application/x-ms-wmz .wp6 application/x-wp6 
//	.wpd application/x-wpd .wpg application/x-wpg 
//	.wpl application/vnd.ms-wpl .wq1 application/x-wq1 
//	.wr1 application/x-wr1 .wri application/x-wri 
//	.wrk application/x-wrk .ws application/x-ws 
//	.ws2 application/x-ws .wsc text/scriptlet 
//	.wsdl text/xml .wvx video/x-ms-wvx 
//	.xdp application/vnd.adobe.xdp .xdr text/xml 
//	.xfd application/vnd.adobe.xfd .xfdf application/vnd.adobe.xfdf 
//	.xhtml text/html .xls application/vnd.ms-excel 
//	.xls application/x-xls .xlw application/x-xlw 
//	.xml text/xml .xpl audio/scpls 
//	.xq text/xml .xql text/xml 
//	.xquery text/xml .xsd text/xml 
//	.xsl text/xml .xslt text/xml 
//	.xwd application/x-xwd .x_b application/x-x_b 
//	.sis application/vnd.symbian.install .sisx application/vnd.symbian.install 
//	.x_t application/x-x_t .ipa application/vnd.iphone 
//	.apk application/vnd.android.package-archive .xap application/x-silverlight-app 

	
	
	
	
//	.jsp text/html
	
	
//	.  .math text/xml 
//	.  .  
//	.  .  
	
//	.  .  
//	.  .mml text/xml 
//	.  .  
//	.  .  
//	.  .  
//	.  .  
//	.mtx text/xml .  
//	.  .  
//	.plg text/html .  
//	.  .png image/png 
	
//	.  .htm text/html 
//	.html text/html .htt text/webviewhtml 
//	.htx text/html 
	//.icb application/x-icb 
//	.  .  
//	.  .  
//	.  .  
//	.  .  
//	  .jfif image/jpeg 
//	.jpe image/jpeg .  
//	.jpeg image/jpeg .jpg image/jpeg 
	

}
