package com.example.bookings.infra.dto;

public class BookingRequest {
    private Long userId;
    private Long gymClassId;

    public BookingRequest(Long userId, Long gymClassId) {
        this.userId = userId;
        this.gymClassId = gymClassId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGymClassId() {
        return gymClassId;
    }

    public void setGymClassId(Long gymClassId) {
        this.gymClassId = gymClassId;
    }
}
