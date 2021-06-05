package com.ikinsure.conference.reservation;

import com.ikinsure.conference.ConferenceApplication;
import com.ikinsure.conference.exception.LocalisedException;
import com.ikinsure.conference.lecture.Lecture;
import com.ikinsure.conference.lecture.LectureRepository;
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
public class ReservationService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final Sender sender;

    @Autowired
    public ReservationService(LectureRepository lectureRepository,
                              UserRepository userRepository,
                              Sender sender) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.sender = sender;
    }

    public List<Lecture> getLectures() {
        return lectureRepository.findAll();
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Set<Lecture> getUserLectures(String login) {
        User user = userRepository
                .findUserByLogin(login)
                .orElseThrow(() -> new LocalisedException("user.not-exists"));
        return user.getLectures();
    }

    public void createReservation(UserCommand userCommand, UUID lectureId) {

        // get user by login or create new if not exists
        User user = userRepository
                .findUserByLogin(userCommand.getLogin())
                .orElseGet(() -> userRepository.save(new User(
                        userCommand.getEmail(),
                        userCommand.getLogin())
                ));

        // login is already taken
        if (!user.getEmail().equals(userCommand.getEmail())) {
            throw new LocalisedException("login.taken");
        }

        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(() -> new LocalisedException("lecture.not-exists"));

        Set<User> usersInLecture = lecture.getUsers();
        Set<Lecture> userLectures = user.getLectures();

        // user already reserved
        if (usersInLecture.contains(user) || userLectures.contains(lecture)) {
            throw new LocalisedException("reservation.joined");
        }

        // check capacity of lecture
        if (usersInLecture.size() >= ConferenceApplication.MAX_LECTURE_SIZE) {
            throw new LocalisedException("lecture.full");
        }

        // check if user has reservation at this time
        boolean atThisTime = userLectures.stream()
                .map(Lecture::getStartTime)
                .anyMatch(time -> time.equals(lecture.getStartTime()));
        if (atThisTime) {
            throw new LocalisedException("reservation.already");
        }

        // add reservation and save
        usersInLecture.add(user);
        userLectures.add(lecture);
        lectureRepository.save(lecture);
        userRepository.save(user);

        // send confirmation email
        String text = MessageFormat.format(
                "Data wysłania: {0}\nDo: {1}\nTreść: Zarezerwowano prelekcję \"{2}\", która odbędzie się o {3}.\n\n",
                LocalDateTime.now(),
                user.getEmail(),
                lecture.getName(),
                lecture.getStartTime()
        );
        sender.send("powiadomienia.txt", text);
    }

    public Lecture deleteReservation(UserCommand userCommand, UUID lectureId) {

        User user = userRepository
                .findUserByLogin(userCommand.getLogin())
                .orElseThrow(() -> new LocalisedException("user.not-exists"));

        if (!user.getEmail().equals(userCommand.getEmail())) {
            throw new LocalisedException("email.not-match");
        }

        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(() -> new LocalisedException("lecture.not-exists"));

        Set<User> usersInLecture = lecture.getUsers();
        Set<Lecture> userLectures = user.getLectures();

        if (!usersInLecture.contains(user)) {
            throw new LocalisedException("reservation.not-joined");
        }

        // delete reservation and save
        usersInLecture.remove(user);
        userLectures.remove(lecture);
        userRepository.save(user);
        return lectureRepository.save(lecture);
    }

    public User updateEmail(UserCommand userCommand) {
        User user = userRepository
                .findUserByLogin(userCommand.getLogin())
                .orElseThrow(() -> new LocalisedException("user.not-exists"));
        user.setEmail(userCommand.getEmail());
        return userRepository.save(user);
    }

}
