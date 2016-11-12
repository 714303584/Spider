package com.ifreeshare.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer4Lucene5;

import com.ifreeshare.spider.core.CoreBase;

/**
 * @author zhuss
 * @date 2016-11-3-PM7:07:31
 * @description Full text retrieval tools 
 */
public class LuceneFactory {
	
	private static Directory indexDir ;
	
	private static IndexWriter indexWriter = null;
	
	private static IndexReader indexReader = null;
	
	static{
		try {
			//
			indexDir = FSDirectory.open(Paths.get("D:\\lucene"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * According to the path for Directory
	 * @param path
	 * @return
	 */
	public static Directory getDirectory(String path){
		try {
			return FSDirectory.open(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * According to the path for IndexWriter
	 * @param path  Full-text index path
	 * @return  IndexWriter
	 */
	public static IndexWriter getIndexWriter(String path){
		Analyzer analyzer = new IKAnalyzer4Lucene5();
    	IndexWriterConfig conf = new IndexWriterConfig(analyzer);
    	conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
    	try {
    		return new IndexWriter(getDirectory(path), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	public static  IndexWriter getIndexWriter(OpenMode createOrAppend){
		
		if(indexWriter != null){
			return indexWriter;
		}
		Analyzer analyzer = new IKAnalyzer4Lucene5();
    	IndexWriterConfig conf = new IndexWriterConfig(analyzer);
    	conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
    	try {
			indexWriter = new IndexWriter(indexDir, conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return indexWriter;
	}
	
	
	public static IndexReader getIndexReader(){
//		if(indexReader != null){
//			return indexReader;
//		}else{
			try {
				indexReader = DirectoryReader.open(indexDir);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return indexReader;
//		}
	}
	
	public static void writerDocument(Document doc){
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static  IndexSearcher getIndexSearcher()  
	{  
		return new IndexSearcher(getIndexReader());  
	} 
	
	/**
	 * 
	 * @param value The query keywords
	 * @param numb   Take out how many rows
	 * @param field  Query of the fields
	 * @param occur  
	 * @param after
	 * @return
	 */
	public static Document[]  search(String[] value,int numb,String[] field,Occur[] occur ,ScoreDoc after){
		Query query = null;
		try {
			IndexReader indexReader = DirectoryReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(indexReader);
			query = MultiFieldQueryParser.parse(value, field, occur, new IKAnalyzer4Lucene5());
			TopDocs docs = searcher.searchAfter(after,query, numb);
		
			ScoreDoc[] scoreDocs =  docs.scoreDocs;
			Document[] documents = new Document[scoreDocs.length];
			for (int i = 0; i < scoreDocs.length; i++) {
	           Document document = searcher.doc(scoreDocs[i].doc); 
	           documents[i] = document;
			}
			if(scoreDocs.length > 0){
				after = scoreDocs[scoreDocs.length-1];
			}
			indexReader.close();
			 return documents;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  Full text search page
	 * @param path The index is stored
	 * @param value The query keywords
	 * @param pageSize   Take out how many rows
	 * @param field  Query of the fields
	 * @param occur
	 * @param pageindex The page number
	 * @return  data 
	 */
	public static Document[] search(String path, String[] value, int pageSize, String[] field, Occur[] occur, int pageindex){
		Query query = null;
		try {
			IndexReader indexReader = DirectoryReader.open(getDirectory(path));
			IndexSearcher searcher = new IndexSearcher(indexReader);
			query = MultiFieldQueryParser.parse(value, field, occur, new IKAnalyzer4Lucene5());
			
			ScoreDoc last = null;
			if(pageindex > 1){
				int numb = pageSize * (pageindex - 1);
				TopDocs tds = searcher.search(query, numb);
				int lastNum = numb - 1;
				if(tds.totalHits <  lastNum ){
					return null;
				}else{
					last = tds.scoreDocs[lastNum];
				}
			}
			
			TopDocs docs = searcher.searchAfter(last,query, pageSize);
			
		
			ScoreDoc[] scoreDocs =  docs.scoreDocs;
			Document[] documents = new Document[scoreDocs.length];
			for (int i = 0; i < scoreDocs.length; i++) {
	           Document document = searcher.doc(scoreDocs[i].doc); 
	           documents[i] = document;
			}
			
			indexReader.close();
			 return documents;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ScoreDoc after = new ScoreDoc(0, 0);
		LuceneFactory.search(after);
		
		if(after != null){
			ScoreDoc a = new ScoreDoc(after.doc, after.score, after.shardIndex);
			System.out.println("--------------------------------------------");
			LuceneFactory.search(after);
		}
		
	
		
//		 IKSegmenter ik = new IKSegmenter(new StringReader("从上面的定义我们知道，在Java中，同样的方法名称和参数，但是返回值不同，这种情况不可以使用重载。"+
//
//"这两个方法的区别在于返回值，每一个分词器都可能有多种分词模式，每种模式的分词结果都可能不相同，第一个方法忽略分词器模式，返回所有模式的所有不重复分词结果，第二个方法返回每一种分词器模式及其对应的分词结果"), false);		
//		  try {
//		    Lexeme word = null;
//		    while((word=ik.next())!=null) {		  
//		      System.out.println(word.getLexemeText());
//		    }
//		  } catch (IOException ex) {
//		    throw new RuntimeException(ex);
//		  }

		
		
		
		
		
		
	
		
//		 try {
//			 
//				DirectoryReader ireader = DirectoryReader.open(indexDir);
//				QueryParser queryParser = new QueryParser(CoreBase.HTML_DESCRIPTION, new IKAnalyzer4Lucene5());
//			    IndexSearcher isearcher = new IndexSearcher(ireader);
//			    Query query = queryParser.parse("art,artistic,community,images");
//			    ScoreDoc[] hits = isearcher.search(query,100).scoreDocs;
//			    // Iterate through the results:
//			    for (int i = 0; i < hits.length; i++) {
//			      Document hitDoc = isearcher.doc(hits[i].doc);
//			    }
//			    ireader.close();
//		} catch (ParseException | IOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
	}
	
	
	public static void search(ScoreDoc after){
		
		String[] value = 
			{"电子书","电子书","电子书"};
		String[] field = {CoreBase.HTML_KEYWORDS,CoreBase.HTML_TITLE,CoreBase.HTML_DESCRIPTION};
		Occur[] occur =  {Occur.SHOULD,Occur.SHOULD,Occur.SHOULD};
		
		Document[] documents = LuceneFactory.search(value, 10, field,occur, after);
		
		for (int i = 0; i < documents.length; i++) {
			Document document = documents[i];
			System.out.println(document.toString());
		}
		
		if(documents.length >= 1000){
			search(after);
		}else{
			return;
		}
	}
	
	
	
	public static FieldType getFieldType(DocValuesType docValuesType,IndexOptions indexOptions){
		FieldType result = new FieldType();
		result.setDocValuesType(docValuesType);
		result.setIndexOptions(indexOptions);
		return result;
	}
	
	
	
	

}
