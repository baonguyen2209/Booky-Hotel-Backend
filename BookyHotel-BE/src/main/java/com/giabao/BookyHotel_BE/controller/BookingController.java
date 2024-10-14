package com.giabao.BookyHotel_BE.controller;

import com.giabao.BookyHotel_BE.exception.InvalidBookingRequestException;
import com.giabao.BookyHotel_BE.exception.ResourceNotFoundException;
import com.giabao.BookyHotel_BE.model.BookedRoom;
import com.giabao.BookyHotel_BE.model.Room;
import com.giabao.BookyHotel_BE.model.User;
import com.giabao.BookyHotel_BE.response.BookingResponse;
import com.giabao.BookyHotel_BE.response.Response;
import com.giabao.BookyHotel_BE.response.RoomResponse;
import com.giabao.BookyHotel_BE.response.UserResponse;
import com.giabao.BookyHotel_BE.service.IBookingService;
import com.giabao.BookyHotel_BE.service.IRoomService;
import com.giabao.BookyHotel_BE.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private final IBookingService bookingService;

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBooking(@PathVariable Long roomId,
                                                @PathVariable Long userId,
                                                @RequestBody BookedRoom bookingRequest) {
        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findByBookingConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {
        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
