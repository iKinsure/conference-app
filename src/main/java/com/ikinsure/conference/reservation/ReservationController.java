package com.ikinsure.conference.reservation;

import com.ikinsure.conference.lecture.Lecture;
import com.ikinsure.conference.user.User;
import com.ikinsure.conference.user.dto.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService service;
    private final MessageSource messageSource;

    @Autowired
    public ReservationController(ReservationService service, MessageSource messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }

    /**
     * 1.
     */
    @GetMapping("/lectures")
    public List<Lecture> getLectures() {
        return service.getLectures();
    }

    /**
     * 2.
     */
    @GetMapping("/users/{login}/lectures")
    public Set<Lecture> getUserLectures(@PathVariable String login) {
        return service.getUserLectures(login);
    }

    /**
     * 3.
     * 4.
     * 5.
     */
    @PostMapping("/lectures/{lectureId}")
    public ResponseEntity<Object> createReservation(
            @PathVariable UUID lectureId,
            @RequestBody @Valid UserCommand userCommand) {
        service.createReservation(userCommand, lectureId);
        Map<String, String> messages = new HashMap<>();
        messages.put("message", getLocaleMessage("reservation.new"));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * 6.
     */
    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity<Object> deleteReservation(
            @PathVariable UUID lectureId,
            @RequestBody @Valid UserCommand userCommand) {
        Lecture lecture = service.deleteReservation(userCommand, lectureId);
        Map<String, Object> messages = new HashMap<>();
        messages.put("message", getLocaleMessage("reservation.cancel"));
        messages.put("lecture", lecture);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * 7.
     */
    @PutMapping("/users")
    public User update(@RequestBody @Valid UserCommand userCommand) {
        return service.updateEmail(userCommand);
    }

    /**
     * 8.
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return service.getUsers();
    }

    private String getLocaleMessage(String msg) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msg, new Object[]{},locale);
    }
}
