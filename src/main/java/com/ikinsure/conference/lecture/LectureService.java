package com.ikinsure.conference.lecture;

import com.ikinsure.conference.ConferenceApplication;
import com.ikinsure.conference.sender.Sender;
import com.ikinsure.conference.user.User;
import com.ikinsure.conference.user.UserRepository;
import com.ikinsure.conference.user.dto.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture id does not exist"));

        Set<User> usersInLecture = lecture.getUsers();
        Set<Lecture> userLectures = user.getLectures();

        if (usersInLecture.contains(user) || userLectures.contains(lecture)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You have already joined this lecture");
        }

        System.out.println(usersInLecture.size());
        if (usersInLecture.size() >= ConferenceApplication.MAX_LECTURE_SIZE) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This lecture is already full");
        }

        long atThisTime = userLectures.stream()
                .map(Lecture::getStartTime)
                .filter(time -> time.equals(lecture.getStartTime()))
                .count();
        if (atThisTime > 0L) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You have already lecture at this time");
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture id does not exist"));

        Set<User> usersInLecture = lecture.getUsers();
        Set<Lecture> userLectures = user.getLectures();

        if (!usersInLecture.contains(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You have not joined this lecture");
        }

        usersInLecture.remove(user);
        userLectures.remove(lecture);

        lectureRepository.save(lecture);
        userRepository.save(user);
    }

    private User findUser(UserCommand userCommand) {
        User user = userRepository
                .findUserByLogin(userCommand.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        if (!user.getEmail().equals(userCommand.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User email is not valid");
        }
        return user;
    }
}

