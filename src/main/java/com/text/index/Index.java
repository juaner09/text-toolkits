package com.text.index;

import com.utils.FileOperatorUtils;
import org.ansj.lucene4.AnsjIndexAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李小娟 on 2017/8/20.
 */
public class Index {
    private String ids[] = { "1", "2", "3" };
    private String citys[] = { "qingdao", "nanjing", "shanghai" };
    private String descs[] = { "Qingdao is a beautiful city.",
            "Nanjing is a city of culture.", "Shanghai is a bustling city." };
    private Map<String, String> category;

    public void setUp(){
        category = new HashMap<String, String>();
        category.put("C000007","car");
        category.put("C000008","finance");
        category.put("C000010","IT");
        category.put("C000013","health");
        category.put("C000014","sport");
        category.put("C000016","travel");
        category.put("C000020","education");
        category.put("C000022","recruit");
        category.put("C000023","culture");
        category.put("C000024","military");
    }
    private Directory dir;// 目录
    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    public IndexWriter getWriter(Directory directory) throws Exception {
        Analyzer analyzer = new AnsjIndexAnalysis();//new StandardAnalyzer(Version.LUCENE_47); // 标准分词器
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);
        return writer;
    }

    /**
     * 获取输入文档
     * */
    public void getIndexFile(String path) throws Exception {
        dir = FSDirectory.open(new File("D://data/index/sougou"));// 得到luceneIndex目录
        IndexWriter writer = getWriter(dir);// 得到索引
        writer.deleteAll();

        File labelFiles = new File(path);
        File[] listLabelFiles = labelFiles.listFiles();
        for(File LabelFile: listLabelFiles){
            String fileName = LabelFile.getName();
            String label = category.get(fileName);

            String pathLabel = LabelFile.getAbsolutePath();
            File files = new File(pathLabel);
            File[] listFiles = files.listFiles();
            for(File file: listFiles){
                String filePath = file.getAbsolutePath();
                String content = FileOperatorUtils.readFile(filePath,"gbk");

                Document doc = new Document();// 创建文档
                doc.add(new StringField("label", label, Field.Store.YES));// 将id属性存入内存中
                doc.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED));
                writer.addDocument(doc); // 添加文档
            }
        }
        writer.close();
        System.out.println("Index created");
    }

    /**
     * 添加文档
     *
     * @throws Exception
     */
    public void createIndex() throws Exception {
        dir = FSDirectory.open(new File("D://data/index/sougou"));// 得到luceneIndex目录
        IndexWriter writer = getWriter(dir);// 得到索引
        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();// 创建文档
            doc.add(new StringField("id", ids[i], Field.Store.YES));// 将id属性存入内存中
            doc.add(new StringField("city", citys[i], Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.NO));
            writer.addDocument(doc); // 添加文档
        }
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        Index index = new Index();
        index.setUp();
        index.getIndexFile("D://data/sougou");
    }
}
