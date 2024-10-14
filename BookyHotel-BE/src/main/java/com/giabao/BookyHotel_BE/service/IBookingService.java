package com.giabao.BookyHotel_BE.service;

import com.giabao.BookyHotel_BE.model.BookedRoom;
import com.giabao.BookyHotel_BE.response.Response;

import java.util.List;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, BookedRoom bookingRequest);

    Response findByBookingConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
