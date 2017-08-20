package com.text.index.test;

import com.text.index.Index;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by 李小娟 on 2017/8/20.
 */
public class IndexTest {
    private Directory dir;// 目录
    IndexWriter writer;

    @Before
    public void setUp() throws Exception {
        dir = FSDirectory.open(new File("D://data/index/sougou"));// 目录
        Index index = new Index();
        writer = index.getWriter(dir);
    }

    /**
     * 测试写了几个文档
     *
     * @throws Exception
     */
    @Test
    public void testIndexWriter() throws Exception {
        System.out.println("写入了" + writer.numDocs() + "个文档");
        writer.close();
    }

    /**
     * 测试读取文档
     *
     * @throws Exception
     */
    @Test
    public void testIndexReader() throws Exception {
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "计算机")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("label", "IT")), BooleanClause.Occur.MUST);
        double n = searcher.search(query, Integer.MAX_VALUE).totalHits;
        System.out.println(n);
        reader.close();
    }

    /**
     * 测试删除 在合并前
     *
     * @throws Exception
     */
    @Test
    public void testDeleteBeforeMerge() throws Exception {
        System.out.println("删除前：" + writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));// term：根据id找到为1的
        writer.commit();
        System.out.println("writer.maxDoc()：" + writer.maxDoc());
        System.out.println("writer.numDocs()：" + writer.numDocs());
        writer.close();
    }

    /**
     * 测试删除 在合并后
     *
     * @throws Exception
     */
    @Test
    public void testDeleteAfterMerge() throws Exception {
        System.out.println("删除前：" + writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        System.out.println("writer.maxDoc()：" + writer.maxDoc());
        System.out.println("writer.numDocs()：" + writer.numDocs());
        writer.close();
    }

    /**
     * 测试更新
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        Document doc = new Document();
        doc.add(new StringField("id", "1", Field.Store.YES));
        doc.add(new StringField("city", "qingdao", Field.Store.YES));
        doc.add(new TextField("desc", "dsss is a city.", Field.Store.NO));
        writer.updateDocument(new Term("id", "1"), doc);
        writer.close();
    }
}
