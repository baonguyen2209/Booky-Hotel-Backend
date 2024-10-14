package com.giabao.BookyHotel_BE.response;

import com.giabao.BookyHotel_BE.model.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;
    private RoomResponse room;
    private UserResponse user;

    public BookingResponse(long id, LocalDate checkInDate,
                           LocalDate checkOutDate,
                           String bookingConfirmationCode) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
