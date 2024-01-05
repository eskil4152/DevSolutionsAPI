package com.devsolutions.DevSolutionsAPI.Entities;

import jakarta.persistence.*;

@Entity
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faq_id_generator")
    @SequenceGenerator(
            name = "faq_id_generator",
            allocationSize = 1
    )
    Long id;

    String question;
    String answer;

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
