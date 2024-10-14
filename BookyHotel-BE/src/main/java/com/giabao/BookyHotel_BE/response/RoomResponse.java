package com.giabao.BookyHotel_BE.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomTitle;
    private int capacity;
    private int roomSize;
    private boolean isBooked;
    private String photo;
    private String description;
    private List<BookingResponse> bookings;

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice,
                        String roomTitle, int capacity, int roomSize,
                        String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomTitle = roomTitle;
        this.capacity = capacity;
        this.roomSize = roomSize;
        this.description = description;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice,
                        String roomTitle, int capacity, int roomSize,
                        boolean isBooked, byte[] photoBytes, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomTitle = roomTitle;
        this.capacity = capacity;
        this.roomSize = roomSize;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.description = description;
//        this.bookings = bookings;
    }
}
