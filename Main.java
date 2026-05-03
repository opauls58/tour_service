package by.psu;

import by.psu.db.ConnectionManager;
import by.psu.db.JdbcHelper;
import by.psu.exception.TourServiceValidationException;
import by.psu.model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // ---------- Часть 1: работа с моделями ----------
        System.out.println("=== Тестирование моделей ===");
        try {
            Client client = new Client(
                    "Иван Петров",
                    "ivan.petrov@example.com",
                    "+375291234567",
                    "AB12345678",
                    120
            );

            HotelStay hotel = new HotelStay(
                    1,
                    "Отель Европа",
                    new BigDecimal("150.00"),
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    4,
                    3,
                    RoomType.DOUBLE
            );

            Excursion excursion = new Excursion(
                    2,
                    "Обзорная экскурсия",
                    new BigDecimal("50.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Анна Смирнова",
                    "Пешеходная",
                    false
            );

            Flight flight = new Flight(
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

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);
            services.put(excursion, 5);
            services.put(flight, 2);

            Booking booking = new Booking(client, services);

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

        // ---------- Часть 2: работа с базой данных ----------
        System.out.println("\n=== Тестирование работы с БД (Excursion) ===");
        try (ConnectionManager cm = new ConnectionManager()) {
            JdbcHelper helper = new JdbcHelper(cm.getConnection());

            // 1. Вставка новой экскурсии
            Excursion newExcursion = new Excursion(
                    null,                     // id сгенерируется БД
                    "Тестовая экскурсия",
                    new BigDecimal("99.99"),
                    LocalDate.now(),
                    LocalDate.now().plusDays(1),
                    "Гид Иванов",
                    "Автобусная",
                    true
            );
            helper.saveExcursion(newExcursion);
            System.out.println("Создана экскурсия с id = " + newExcursion.getId());

            // 2. Поиск по id
            Excursion found = helper.findExcursionById(newExcursion.getId());
            System.out.println("Найдена по id: " + found);

            // 3. Обновление
            found.setGuideName("Новый гид");
            helper.saveExcursion(found);
            System.out.println("После обновления гида: " + helper.findExcursionById(found.getId()));

            // 4. Получение всех записей
            List<Excursion> all = helper.findAllExcursions();
            System.out.println("Всего экскурсий в БД: " + all.size());
            all.forEach(ex -> System.out.println(" - " + ex));

            // 5. Удаление тестовой записи
            helper.deleteExcursionById(newExcursion.getId());
            System.out.println("Экскурсия с id=" + newExcursion.getId() + " удалена");

        } catch (SQLException e) {
            System.err.println("Ошибка при работе с БД: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}