package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.request.MemberRequest;
import com.example.demo.service.LuceneService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class LuceneController {

    private final LuceneService luceneService;

    public LuceneController(LuceneService luceneService) {
        this.luceneService = luceneService;
    }

    @GetMapping("/indexing")
    public void indexing() throws IOException {
        luceneService.indexingClient();
    }

    @PostMapping
    public List<Member> findByName(@RequestBody MemberRequest request) throws IOException {
        return luceneService.findByName(request.getName(), request.getPage(), request.getSize());
    }
}
