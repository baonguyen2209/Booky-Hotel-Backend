package com.giabao.BookyHotel_BE.controller;

import com.giabao.BookyHotel_BE.exception.PhotoRetrievalException;
import com.giabao.BookyHotel_BE.exception.ResourceNotFoundException;
import com.giabao.BookyHotel_BE.model.BookedRoom;
import com.giabao.BookyHotel_BE.model.Room;
import com.giabao.BookyHotel_BE.response.BookingResponse;
import com.giabao.BookyHotel_BE.response.Response;
import com.giabao.BookyHotel_BE.response.RoomResponse;
import com.giabao.BookyHotel_BE.service.BookingService;
import com.giabao.BookyHotel_BE.service.IBookingService;
import com.giabao.BookyHotel_BE.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private final IRoomService roomService;

    @Autowired
    private IBookingService iBookingService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false)BigDecimal roomPrice,
            @RequestParam(value = "roomTitle", required = false)String roomTitle,
            @RequestParam(value = "capacity", required = false)int capacity,
            @RequestParam(value = "roomSize", required = false)int roomSize,
            @RequestParam(value = "description", required = false)String description) {
        if (photo == null || photo.isEmpty()
                || roomType == null || roomType.isBlank()
                || roomPrice == null
                || roomTitle == null || roomTitle.isBlank()
                || capacity == 0 || roomSize == 0
                || description == null || description.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.addNewRoom(photo, roomType, roomPrice, roomTitle, capacity, roomSize, description);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms() {
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDateAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType) {
        if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(value = "roomTitle", required = false) String roomTitle,
                                                   @RequestParam(value = "roomType", required = false) String roomType,
                                                   @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                                   @RequestParam(value = "capacity", required = false, defaultValue = "0") int capacity,
                                                   @RequestParam(value = "roomSize", required = false, defaultValue = "0") int roomSize,
                                                   @RequestParam(value = "description", required = false) String description,
                                                   @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException, SQLException {
        byte[] photoBytes = photo != null && !photo.isEmpty()?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Response response = roomService.updateRoom(roomId, roomTitle, roomType, roomPrice, capacity, roomSize, description, photoBytes);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
