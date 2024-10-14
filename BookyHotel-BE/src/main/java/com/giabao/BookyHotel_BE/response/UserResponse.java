package com.giabao.BookyHotel_BE.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.giabao.BookyHotel_BE.model.BookedRoom;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private List<BookingResponse> bookedRooms = new ArrayList<>();

    public UserResponse(long userId, String email, String firstName, String lastName, String phoneNumber, String role) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
