package com.giabao.BookyHotel_BE.service;

import com.giabao.BookyHotel_BE.exception.InternalServerException;
import com.giabao.BookyHotel_BE.exception.OurException;
import com.giabao.BookyHotel_BE.exception.ResourceNotFoundException;
import com.giabao.BookyHotel_BE.model.Room;
import com.giabao.BookyHotel_BE.repository.BookingRepository;
import com.giabao.BookyHotel_BE.repository.RoomRepository;
import com.giabao.BookyHotel_BE.response.Response;
import com.giabao.BookyHotel_BE.response.RoomResponse;
import com.giabao.BookyHotel_BE.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    @Override
    public Response addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice, String roomTitle, int capacity, int roomSize,
                               String description) {

        Response response = new Response();
        try {
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomTitle(roomTitle);
            room.setCapacity(capacity);
            room.setRoomSize(roomSize);
            room.setDescription(description);
            if (!file.isEmpty()) {
                byte[] photoBytes = file.getBytes();
                Blob photoBlod = new SerialBlob(photoBytes);
                room.setPhoto(photoBlod);
            }
            Room savedRoom = roomRepository.save(room);
            RoomResponse roomResponse = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomResponse);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomResponse> roomResponseList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomResponseList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public byte[] getRoomPhotoByRoomId(long roomId) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String roomTitle, String roomType, BigDecimal roomPrice, int capacity, int roomSize, String description, byte[] photoBytes) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            if (roomTitle != null) room.setRoomTitle(roomTitle);
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (capacity > 0) room.setCapacity(capacity);
            if (roomSize > 0) room.setRoomSize(roomSize);
            if (description != null) room.setDescription(description);
            if (photoBytes != null && photoBytes.length > 0) {
                try {
                    room.setPhoto(new SerialBlob(photoBytes));
                } catch (SQLException ex) {
                    throw new InternalServerException("Error updating room");
                }
            }
            Room updatedRoom = roomRepository.save(room);
            RoomResponse roomResponse = Utils.mapRoomEntityToRoomDTO(updatedRoom);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomResponse);
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            RoomResponse roomResponse = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomResponse);
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomResponse> roomResponseList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomResponseList);
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomResponse> roomResponseList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomResponseList);
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }
}
