package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.repository.LuceneRepostory;
import com.example.demo.util.koreanTokenizer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;

import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;

import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import org.apache.lucene.store.NIOFSDirectory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LuceneService {

    private final LuceneRepostory luceneRepostory;

    public LuceneService(LuceneRepostory luceneRepostory) {
        this.luceneRepostory = luceneRepostory;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void indexingClient() throws IOException {
        try(Directory dir = NIOFSDirectory.open(Path.of("C:\\Users\\thdgu"));
             IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(new KoreanAnalyzer()))) {

            final Integer chunkSize = 100;

            Pageable pageable = PageRequest.of(0, chunkSize);
            Page<Member> page = luceneRepostory.findAll(pageable);

            indexing(writer, page);

            while (page.hasNext()) {
                page = luceneRepostory.findAll(page.nextPageable());
                indexing(writer, page);
            }

            writer.commit();
            writer.flush();
        } catch (LockObtainFailedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void indexing(IndexWriter writer, Page<Member> page, IndexSearcher indexSearcher) {
        page.getContent().forEach(member -> {
            Term term = new Term("id", String.valueOf(member.getId()));
            Document document = new Document();
            document.add(new LongPoint("id", member.getId()));
            document.add(new StringField("id", member.getId().toString(), Field.Store.YES));
            document.add(new StringField("nameToken", koreanTokenizer.tokenize(member.getName()), Field.Store.YES));
            document.add(new StringField("nameTokenFirst", koreanTokenizer.tokenizeFirst(member.getName()), Field.Store.YES));
            document.add(new StringField("name", member.getName(), Field.Store.YES));
            document.add(new StringField("birth", member.getBirth(), Field.Store.YES));
            document.add(new StringField("gender", member.getGender(), Field.Store.YES));
            document.add(new StringField("city", member.getCity(), Field.Store.YES));
            document.add(new StringField("joinDt", member.getJoinDt().format(DateTimeFormatter.ISO_DATE), Field.Store.YES));
            document.add(new StringField("mail", member.getMail(), Field.Store.YES));
            document.add(new StringField("loginIp", member.getLoginIp(), Field.Store.YES));
            try {
                writer.updateDocument(term, document);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Member> findByName(String name, Integer page, Integer size) throws IOException {
        Directory dir = NIOFSDirectory.open(Path.of("C:\\Users\\thdgu"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(dir));

        Term term = new Term("nameToken", koreanTokenizer.tokenize(name));
        Query query = new PrefixQuery(term);

        TopDocs topDocs = indexSearcher.search(query, ((page-1) * size) + size);

        List<Member> list =  Arrays.asList(topDocs.scoreDocs).stream().skip((page-1) * size).map(scoreDoc -> {
            try {
                Document document = indexSearcher.doc(scoreDoc.doc);
                return new Member(document);
            } catch (IOException e) {
                return new Member();
            }
        }).collect(Collectors.toList());

        if(list.size() != size) {
            term = new Term("nameTokenFirst", koreanTokenizer.tokenizeFirst(name));
            query = new PrefixQuery(term);

            topDocs = indexSearcher.search(query, (((page-1) * size) + size) - list.size());
            list.addAll(Arrays.asList(topDocs.scoreDocs).stream().skip((page-1) * size).map(scoreDoc -> {
                try {
                    Document document = indexSearcher.doc(scoreDoc.doc);
                    return new Member(document);
                } catch (IOException e) {
                    return new Member();
                }
            }).collect(Collectors.toList()));
        }

        dir.close();

        return list;
    }
}
