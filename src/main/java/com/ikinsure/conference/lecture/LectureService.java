package com.ikinsure.conference.lecture;

import com.ikinsure.conference.ConferenceApplication;
import com.ikinsure.conference.exception.LocalisedException;
import com.ikinsure.conference.sender.Sender;
import com.ikinsure.conference.user.User;
import com.ikinsure.conference.user.UserRepository;
import com.ikinsure.conference.user.dto.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final Sender sender;

    @Autowired
    public LectureService(LectureRepository lectureRepository, UserRepository userRepository, Sender sender) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.sender = sender;
    }

    public List<Lecture> getAll() {
        return lectureRepository.findAll();
    }

    public void reserveLecture(UserCommand userCommand, UUID lectureId) {

        User user = findUser(userCommand);
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(() -> new LocalisedException("lecture.not-exists"));

        Set<User> usersInLecture = lecture.getUsers();
        Set<Lecture> userLectures = user.getLectures();

        if (usersInLecture.contains(user) || userLectures.contains(lecture)) {
            throw new LocalisedException("reservation.joined");
        }

        System.out.println(usersInLecture.size());
        if (usersInLecture.size() >= ConferenceApplication.MAX_LECTURE_SIZE) {
            throw new LocalisedException("lecture.full");
        }

        long atThisTime = userLectures.stream()
                .map(Lecture::getStartTime)
                .filter(time -> time.equals(lecture.getStartTime()))
                .count();
        if (atThisTime > 0L) {
            throw new LocalisedException("reservation.already");
        }

        usersInLecture.add(user);
        userLectures.add(lecture);

        lectureRepository.save(lecture);
        userRepository.save(user);

        String filename = "powiadomienia.txt";
        String pattern = "Data wysłania: {0}\nDo: {1}\nTreść: Zarezerwowano prelekcję \"{2}\", która odbędzie się o {3}.\n\n";
        String text = MessageFormat.format(
                pattern,
                LocalDateTime.now(),
                user.getEmail(),
                lecture.getName(),
                lecture.getStartTime()
        );
        sender.send(filename, text);
    }

    public void cancelReservation(UserCommand userCommand, UUID lectureId) {

        User user = findUser(userCommand);
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(() -> new LocalisedException("lecture.not-exists"));

        Set<User> usersInLecture = lecture.getUsers();
        Set<Lecture> userLectures = user.getLectures();

        if (!usersInLecture.contains(user)) {
            throw new LocalisedException("reservation.not-joined");
        }

        usersInLecture.remove(user);
        userLectures.remove(lecture);

        lectureRepository.save(lecture);
        userRepository.save(user);
    }

    private User findUser(UserCommand userCommand) {
        User user = userRepository
                .findUserByLogin(userCommand.getLogin())
                .orElseThrow(() -> new LocalisedException("user.not-exists"));

        if (!user.getEmail().equals(userCommand.getEmail())) {
            throw new LocalisedException("email.not-match");
        }

        return user;
    }
}

