package com.ikinsure.conference.lecture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ikinsure.conference.user.User;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Lecture {

    @Transient
    private static final int MAX_SIZE = 5;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Category category;

    @ManyToMany(mappedBy = "lectures", fetch = FetchType.LAZY)
    private Set<User> users;

    public Lecture() {
    }

    public Lecture(String name, LocalTime startTime, LocalTime endTime, Category category) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.users = new HashSet<>();
    }

    @JsonProperty
    public int size() {
        return users.size();
    }

    @JsonProperty
    public int maxSize() {
        return MAX_SIZE;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id) &&
                Objects.equals(name, lecture.name) &&
                Objects.equals(startTime, lecture.startTime) &&
                Objects.equals(endTime, lecture.endTime) &&
                category == lecture.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startTime, endTime, category);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", category=" + category +
                ", users=" + users +
                '}';
    }
}
