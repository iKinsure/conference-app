package com.ikinsure.conference.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    private final LectureRepository repository;

    @Autowired
    public LectureService(LectureRepository repository) {
        this.repository = repository;
    }

    public List<Lecture> getAll() {
        return repository.findAll();
    }
}
