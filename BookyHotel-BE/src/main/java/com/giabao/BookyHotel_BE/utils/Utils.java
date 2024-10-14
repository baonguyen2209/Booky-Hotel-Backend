package com.giabao.BookyHotel_BE.utils;

import com.giabao.BookyHotel_BE.model.BookedRoom;
import com.giabao.BookyHotel_BE.model.Room;
import com.giabao.BookyHotel_BE.model.User;
import com.giabao.BookyHotel_BE.response.BookingResponse;
import com.giabao.BookyHotel_BE.response.RoomResponse;
import com.giabao.BookyHotel_BE.response.UserResponse;

import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserResponse mapUserEntityToUserDTO(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setUserId(user.getUserId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

    public static BookingResponse mapBookingEntityToBookingDTO(BookedRoom booking) {
        BookingResponse bookingResponse = new BookingResponse();

        bookingResponse.setId(booking.getBookingId());
        bookingResponse.setCheckInDate(booking.getCheckInDate());
        bookingResponse.setCheckOutDate(booking.getCheckOutDate());
        bookingResponse.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingResponse.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingResponse;
    }

    public static RoomResponse mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(room.getId());
        roomResponse.setRoomType(room.getRoomType());
        roomResponse.setRoomPrice(room.getRoomPrice());
        roomResponse.setRoomTitle(room.getRoomTitle());
        roomResponse.setCapacity(room.getCapacity());
        roomResponse.setRoomSize(room.getRoomSize());
        roomResponse.setDescription(room.getDescription());

        // Convert Blob to Base64 String for photo
        if (room.getPhoto() != null) {
            roomResponse.setPhoto(convertBlobToBase64(room.getPhoto()));
        }

        if (room.getBookings() != null) {
            roomResponse.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }
        return roomResponse;
    }

    public static RoomResponse mapRoomEntityToRoomDTO(Room room) {
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(room.getId());
        roomResponse.setRoomType(room.getRoomType());
        roomResponse.setRoomPrice(room.getRoomPrice());
        roomResponse.setRoomTitle(room.getRoomTitle());
        roomResponse.setCapacity(room.getCapacity());
        roomResponse.setRoomSize(room.getRoomSize());
        roomResponse.setDescription(room.getDescription());

        // Convert Blob to Base64 String for photo
        if (room.getPhoto() != null) {
            roomResponse.setPhoto(convertBlobToBase64(room.getPhoto()));
        }
        return roomResponse;
    }

    public static UserResponse mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setUserId(user.getUserId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());

        if (!user.getBookedRooms().isEmpty()) {
            userResponse.setBookedRooms(user.getBookedRooms().stream().map(bookedRoom -> mapBookingEntityToBookingDTOPlusBookedRoom(bookedRoom,false)).collect(Collectors.toList()));
        }
        return userResponse;
    }

    public static BookingResponse mapBookingEntityToBookingDTOPlusBookedRoom(BookedRoom booking, boolean mapUser) {
        BookingResponse bookingResponse = new BookingResponse();

        bookingResponse.setId(booking.getBookingId());
        bookingResponse.setCheckInDate(booking.getCheckInDate());
        bookingResponse.setCheckOutDate(booking.getCheckOutDate());
        bookingResponse.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingResponse.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser) {
            bookingResponse.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }

        if (booking.getRoom() != null) {
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setId(booking.getRoom().getId());
            roomResponse.setRoomType(booking.getRoom().getRoomType());
            roomResponse.setRoomPrice(booking.getRoom().getRoomPrice());
            roomResponse.setRoomTitle(booking.getRoom().getRoomTitle());
            roomResponse.setCapacity(booking.getRoom().getCapacity());
            roomResponse.setRoomSize(booking.getRoom().getRoomSize());
            roomResponse.setDescription(booking.getRoom().getDescription());

            // Convert Blob to Base64 String for photo
            if (booking.getRoom().getPhoto() != null) {
                roomResponse.setPhoto(convertBlobToBase64(booking.getRoom().getPhoto()));
            }
            bookingResponse.setRoom(roomResponse);
        }

        return bookingResponse;
    }

    public static List<UserResponse> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }
    public static List<RoomResponse> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }
    public static List<BookingResponse> mapBookingListEntityToBookingListDTO(List<BookedRoom> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }

    private static String convertBlobToBase64(Blob blob) {
        try {
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            return Base64.getEncoder().encodeToString(blobAsBytes);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
