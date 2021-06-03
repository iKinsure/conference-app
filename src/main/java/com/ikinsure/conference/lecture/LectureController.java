package com.ikinsure.conference.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService service;

    @Autowired
    public LectureController(LectureService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<Lecture> getAll() {
        return service.getAll();
    }
}
