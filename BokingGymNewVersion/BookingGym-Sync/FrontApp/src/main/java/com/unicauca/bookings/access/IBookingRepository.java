package com.unicauca.bookings.access;

import com.unicauca.clientproducthttpclient.domain.entities.Booking;
import java.util.List;

public interface IBookingRepository {

    public List<Booking> findAll();

    Booking findById(int id);

    public void create(Booking product);

    public void edit(int id, Booking productUpdated);

    public void delete(int id);

}
