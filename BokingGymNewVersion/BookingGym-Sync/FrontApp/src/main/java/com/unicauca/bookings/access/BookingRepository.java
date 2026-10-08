package com.unicauca.bookings.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.clientproducthttpclient.domain.entities.Booking;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BookingRepository implements IBookingRepository {

    public BookingRepository() {

    }

    @Override
    public List<Booking> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public Booking findById(int id) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        Booking booking = null;
        try {

            // Definir la URL de la API REST de productos
            String apiUrl = "http://localhost:8080/api/bookings/booking/" + id ;
            // Crear una solicitud GET 
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a objetos Product
                booking = mapper.readValue(jsonResponse, Booking.class);


            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(BookingRepository.class.getName()).log(Level.SEVERE, null, "Error al obtener productos. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(BookingRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return booking;
    }

    @Override
    public void create(Booking product) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(int id, Booking productUpdated) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
