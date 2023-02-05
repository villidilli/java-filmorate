package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final Set<Integer> friends = new HashSet<>();
    public void addFriend(){}
    public void deleteFriend(){}
    public void getAllFriends(){}
    public void getById(Integer id){}

}
