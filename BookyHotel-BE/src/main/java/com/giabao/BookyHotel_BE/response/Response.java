package com.giabao.BookyHotel_BE.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;
    private UserResponse user;
    private RoomResponse room;
    private BookingResponse booking;
    private List<UserResponse> userList;
    private List<RoomResponse> roomList;
    private List<BookingResponse> bookingList;
}
