package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Positive
    private  Integer id;
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String login;
    private String name;

    @DateTimeFormat(pattern = "dd.MM.yyyy") //todo надо ли ?
    @Past
    private LocalDate birthday;
}
