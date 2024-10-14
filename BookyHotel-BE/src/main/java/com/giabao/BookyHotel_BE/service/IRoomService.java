package com.giabao.BookyHotel_BE.service;

import com.giabao.BookyHotel_BE.model.Room;
import com.giabao.BookyHotel_BE.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String roomTitle, int capacity, int roomSize,
                        String description);

    List<String> getAllRoomTypes();

    Response getAllRooms();

    byte[] getRoomPhotoByRoomId(long roomId) throws SQLException;

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, String roomTitle, String roomType, BigDecimal roomPrice, int capacity, int roomSize, String description, byte[] photoBytes);

    Response getRoomById(Long roomId);

    Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    Response getAllAvailableRooms();
}
