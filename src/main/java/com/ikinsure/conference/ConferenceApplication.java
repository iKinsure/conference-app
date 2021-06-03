package com.ikinsure.conference;

import com.ikinsure.conference.lecture.Category;
import com.ikinsure.conference.lecture.Lecture;
import com.ikinsure.conference.lecture.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class ConferenceApplication {

    public static final Duration LECTURE_LENGTH = Duration.ofMinutes(90);

    public static void main(String[] args) {
        SpringApplication.run(ConferenceApplication.class, args);
    }

    @Bean
    @Autowired
    CommandLineRunner init(LectureRepository repository) {

        LocalTime t1 = LocalTime.of(10, 0);
        LocalTime t2 = LocalTime.of(12, 0);
        LocalTime t3 = LocalTime.of(14, 0);

        List<Lecture> lectures = List.of(

                new Lecture("orange", t1, t1.plus(LECTURE_LENGTH), Category.A),
                new Lecture("apple", t2, t2.plus(LECTURE_LENGTH), Category.A),
                new Lecture("pineapple", t3, t3.plus(LECTURE_LENGTH), Category.A),

                new Lecture("blue", t1, t1.plus(LECTURE_LENGTH), Category.B),
                new Lecture("red", t2, t2.plus(LECTURE_LENGTH), Category.B),
                new Lecture("green", t3, t3.plus(LECTURE_LENGTH), Category.B),

                new Lecture("car", t1, t1.plus(LECTURE_LENGTH), Category.C),
                new Lecture("train", t2, t2.plus(LECTURE_LENGTH), Category.C),
                new Lecture("ship", t3, t3.plus(LECTURE_LENGTH), Category.C)

        );
        return args -> repository.saveAll(lectures);
    }

}
