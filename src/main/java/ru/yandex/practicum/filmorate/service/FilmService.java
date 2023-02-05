package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class FilmService {
    public void addLike(){
        isLikeDeliveredByUser(User user);
    }
    public void deleteLike(){}
    public void getTop10(){}
    private void isLikeDeliveredByUser(User user){}
}
