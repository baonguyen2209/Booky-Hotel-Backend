package com.giabao.BookyHotel_BE.service;

import com.giabao.BookyHotel_BE.exception.InvalidBookingRequestException;
import com.giabao.BookyHotel_BE.exception.OurException;
import com.giabao.BookyHotel_BE.model.BookedRoom;
import com.giabao.BookyHotel_BE.model.Room;
import com.giabao.BookyHotel_BE.model.User;
import com.giabao.BookyHotel_BE.repository.BookingRepository;
import com.giabao.BookyHotel_BE.repository.RoomRepository;
import com.giabao.BookyHotel_BE.repository.UserRepository;
import com.giabao.BookyHotel_BE.response.BookingResponse;
import com.giabao.BookyHotel_BE.response.Response;
import com.giabao.BookyHotel_BE.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final IRoomService roomService;

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final UserRepository userRepository;


    @Override
    public Response saveBooking(Long roomId, Long userId, BookedRoom bookingRequest) {
        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("Room Not Found"));

            List<BookedRoom> existingBookings = room.getBookings();

            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Room not Available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findByBookingConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            BookedRoom booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking Not Found"));
            BookingResponse bookingResponse = Utils.mapBookingEntityToBookingDTOPlusBookedRoom(booking, true);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBooking(bookingResponse);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<BookedRoom> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "bookingId"));
            List<BookingResponse> bookingResponseList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingList(bookingResponseList);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting all booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Canceling a booking: " + e.getMessage());
        }
        return response;
    }


    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
        );
    }
}
