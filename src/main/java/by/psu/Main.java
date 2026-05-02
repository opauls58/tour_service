package by.psu;

import by.psu.exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            by.psu.model.Client client = new by.psu.model.Client(
                    "Иван Петров",
                    "ivan.petrov@example.com",
                    "+375291234567",
                    "AB12345678",
                    120
            );

            by.psu.model.HotelStay hotel = new by.psu.model.HotelStay(
                    1,
                    "Отель Европа",
                    new BigDecimal("150.00"),
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    4,
                    3,
                    by.psu.model.RoomType.DOUBLE
            );

            by.psu.model.Excursion excursion = new by.psu.model.Excursion(
                    2,
                    "Обзорная экскурсия",
                    new BigDecimal("50.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Анна Смирнова",   // guideName
                    "Пешеходная",      // excursionType
                    false              // lunchIncluded
            );

            by.psu.model.Flight flight = new by.psu.model.Flight(
                    3,
                    "Рейс Минск–Париж",
                    new BigDecimal("300.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Минск",
                    "Париж",
                    "B2-123",
                    true
            );

            Map<by.psu.model.TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);
            services.put(excursion, 5);
            services.put(flight, 2);

            by.psu.model.Booking booking = new by.psu.model.Booking(client, services);

            System.out.println("Создано бронирование:");
            System.out.println(booking);

            System.out.println("\nИтоговая цена: " + booking.calculateTotalPrice());

            booking.confirm();
            System.out.println("\nСтатус после подтверждения: " + booking.getStatus());

            booking.complete();
            System.out.println("Статус после завершения: " + booking.getStatus());
            System.out.println("Баллы клиента: " + client.getLoyaltyPoints());

        } catch (TourServiceValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }
}