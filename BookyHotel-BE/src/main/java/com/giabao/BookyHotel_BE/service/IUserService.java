package com.giabao.BookyHotel_BE.service;

import com.giabao.BookyHotel_BE.model.User;
import com.giabao.BookyHotel_BE.response.LoginRequest;
import com.giabao.BookyHotel_BE.response.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
