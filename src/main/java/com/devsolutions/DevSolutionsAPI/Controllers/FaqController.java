package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Faq;
import com.devsolutions.DevSolutionsAPI.Services.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FaqController {

    private final FaqService faqService;

    @Autowired
    public FaqController(FaqService faqService){
        this.faqService = faqService;
    }

    @GetMapping("/faq")
    public ResponseEntity<List<Faq>> getFaq(){
        return ResponseEntity.ok(faqService.getFaq());
    }
}
