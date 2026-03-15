package com.ra.base_spring_boot.security.Initializer;

import com.ra.base_spring_boot.model.constants.BannerType;
import com.ra.base_spring_boot.model.constants.BookingStatus;
import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import com.ra.base_spring_boot.model.constants.PaymentMethod;
import com.ra.base_spring_boot.model.constants.PaymentStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.model.constants.SeatType;
import com.ra.base_spring_boot.model.constants.UserStatus;
import com.ra.base_spring_boot.model.entity.booking.Booking;
import com.ra.base_spring_boot.model.entity.booking.BookingSeat;
import com.ra.base_spring_boot.model.entity.booking.Payment;
import com.ra.base_spring_boot.model.entity.booking.PaymentProvider;
import com.ra.base_spring_boot.model.entity.booking.TicketPrice;
import com.ra.base_spring_boot.model.entity.content.Banner;
import com.ra.base_spring_boot.model.entity.content.Festival;
import com.ra.base_spring_boot.model.entity.content.News;
import com.ra.base_spring_boot.model.entity.movie.Genre;
import com.ra.base_spring_boot.model.entity.movie.Movie;
import com.ra.base_spring_boot.model.entity.theater.Screen;
import com.ra.base_spring_boot.model.entity.theater.Seat;
import com.ra.base_spring_boot.model.entity.theater.ShowTime;
import com.ra.base_spring_boot.model.entity.theater.Theater;
import com.ra.base_spring_boot.model.entity.user.Role;
import com.ra.base_spring_boot.model.entity.user.User;
import com.ra.base_spring_boot.repository.IBannerRepository;
import com.ra.base_spring_boot.repository.IFestivalRepository;
import com.ra.base_spring_boot.repository.IGenreRepository;
import com.ra.base_spring_boot.repository.IMovieRepository;
import com.ra.base_spring_boot.repository.INewRepository;
import com.ra.base_spring_boot.repository.ScreenRepository;
import com.ra.base_spring_boot.repository.Theater.TheaterRepo;
import com.ra.base_spring_boot.repository.authrp.IRoleRepository;
import com.ra.base_spring_boot.repository.authrp.IUserRepository;
import com.ra.base_spring_boot.repository.booking.IBookingRepository;
import com.ra.base_spring_boot.repository.booking.IBookingSeatRepository;
import com.ra.base_spring_boot.repository.booking.ISeatRepository;
import com.ra.base_spring_boot.repository.booking.IShowTimeRepository;
import com.ra.base_spring_boot.repository.booking.ITicketPriceRepository;
import com.ra.base_spring_boot.repository.payment.IPaymentProviderRepository;
import com.ra.base_spring_boot.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Profile("!test")
@Order(1000)
@RequiredArgsConstructor
public class LocalSeedDataInitializer implements CommandLineRunner {

    private static final String DEFAULT_PASSWORD = "123456";

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IGenreRepository genreRepository;
    private final IMovieRepository movieRepository;
    private final TheaterRepo theaterRepository;
    private final ScreenRepository screenRepository;
    private final ISeatRepository seatRepository;
    private final IShowTimeRepository showTimeRepository;
    private final ITicketPriceRepository ticketPriceRepository;
    private final IPaymentProviderRepository paymentProviderRepository;
    private final IBookingRepository bookingRepository;
    private final IBookingSeatRepository bookingSeatRepository;
    private final PaymentRepository paymentRepository;
    private final IFestivalRepository festivalRepository;
    private final INewRepository newsRepository;
    private final IBannerRepository bannerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = role(RoleName.ROLE_ADMIN);
        Role userRole = role(RoleName.ROLE_USER);

        User admin = user("admin@example.com", "Admin", "User", adminRole);
        User customer = user("user@example.com", "Demo", "Customer", userRole);
        List<User> demoUsers = demoUsers(userRole);

        Genre action = genre("Action");
        Genre adventure = genre("Adventure");
        Genre drama = genre("Drama");
        List<Genre> demoGenres = demoGenres();

        Movie nowShowing = movie(
                "Seed Movie Now Showing",
                "Seed Director",
                MovieStatus.NOW_SHOWING,
                MovieType._2D,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(30),
                Set.of(action, adventure)
        );
        movie(
                "Seed Movie Coming Soon",
                "Seed Director",
                MovieStatus.COMING_SOON,
                MovieType._3D,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(50),
                Set.of(drama)
        );
        List<Movie> demoMovies = demoMovies(demoGenres);

        Theater theater = theater("Seed Cinema", "Tokyo demo address", "0900000000");
        Screen screen = screen("Seed Screen 1", theater, 12);
        List<Seat> seats = seats(screen);
        ShowTime showTime = showTime(screen, nowShowing);
        List<Theater> demoTheaters = demoTheaters();
        List<Screen> demoScreens = demoScreens(demoTheaters);
        for (Screen demoScreen : demoScreens) {
            seats(demoScreen);
        }
        List<ShowTime> demoShowTimes = demoShowTimes(demoScreens, demoMovies);

        deduplicateTicketPrices();
        ticketPrices();
        PaymentProvider paypal = paymentProvider("PayPal", "PAYPAL", "PayPal sandbox provider");
        paymentProvider("Stripe", "STRIPE", "Stripe sandbox provider");
        paymentProvider("VNPAY", "VNPAY", "VNPAY provider");
        List<PaymentProvider> demoProviders = demoPaymentProviders();

        completedBooking(customer, showTime, seats.get(0), paypal);
        demoBookings(demoUsers, demoShowTimes, demoProviders);
        content();
        demoContent();

        if (admin.getId() == null) {
            throw new IllegalStateException("Admin seed was not persisted");
        }
    }

    private Role role(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(roleName).build()));
    }

    private User user(String email, String firstName, String lastName, Role role) {
        return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                .phone("0900000000")
                .address("Seed address")
                .status(UserStatus.ACTIVE)
                .roles(new HashSet<>(Set.of(role)))
                .build()));
    }

    private List<User> demoUsers(Role role) {
        return List.of(
                user("demo1@example.com", "Demo", "One", role),
                user("demo2@example.com", "Demo", "Two", role),
                user("demo3@example.com", "Demo", "Three", role),
                user("demo4@example.com", "Demo", "Four", role),
                user("demo5@example.com", "Demo", "Five", role)
        );
    }

    private Genre genre(String name) {
        return genreRepository.findAll()
                .stream()
                .filter(genre -> genre.getGenreName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> genreRepository.save(Genre.builder().genreName(name).build()));
    }

    private List<Genre> demoGenres() {
        return List.of(
                genre("Comedy"),
                genre("Horror"),
                genre("Romance"),
                genre("Animation"),
                genre("Science Fiction")
        );
    }

    private Movie movie(
            String title,
            String author,
            MovieStatus status,
            MovieType type,
            LocalDateTime start,
            LocalDateTime end,
            Set<Genre> genres
    ) {
        return movieRepository.findAll()
                .stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseGet(() -> movieRepository.save(Movie.builder()
                        .title(title)
                        .descriptions("Seed data movie for local API testing")
                        .author(author)
                        .image("https://example.com/seed-movie.jpg")
                        .trailer("https://example.com/seed-trailer")
                        .type(type)
                        .duration(120)
                        .releaseDate(start)
                        .releaseStartDate(start)
                        .releaseEndDate(end)
                        .status(status)
                        .genres(genres)
                        .build()));
    }

    private List<Movie> demoMovies(List<Genre> genres) {
        return List.of(
                movie("Demo Movie Solar Chase", "Demo Director A", MovieStatus.NOW_SHOWING, MovieType._2D,
                        LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(30), Set.of(genres.get(0), genres.get(4))),
                movie("Demo Movie Midnight Signal", "Demo Director B", MovieStatus.NOW_SHOWING, MovieType._3D,
                        LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(35), Set.of(genres.get(1), genres.get(4))),
                movie("Demo Movie Autumn Letters", "Demo Director C", MovieStatus.NOW_SHOWING, MovieType._2D,
                        LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(40), Set.of(genres.get(2))),
                movie("Demo Movie Pixel Journey", "Demo Director D", MovieStatus.COMING_SOON, MovieType._3D,
                        LocalDateTime.now().plusDays(8), LocalDateTime.now().plusDays(45), Set.of(genres.get(3))),
                movie("Demo Movie City Lights", "Demo Director E", MovieStatus.COMING_SOON, MovieType._2D,
                        LocalDateTime.now().plusDays(12), LocalDateTime.now().plusDays(50), Set.of(genres.get(0), genres.get(2)))
        );
    }

    private Theater theater(String name, String location, String phone) {
        return theaterRepository.findAll()
                .stream()
                .filter(theater -> theater.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> theaterRepository.save(Theater.builder()
                        .name(name)
                        .location(location)
                        .phone(phone)
                        .build()));
    }

    private List<Theater> demoTheaters() {
        return List.of(
                theater("Demo Cinema District 1", "District 1 demo address", "0900000001"),
                theater("Demo Cinema District 2", "District 2 demo address", "0900000002"),
                theater("Demo Cinema District 3", "District 3 demo address", "0900000003"),
                theater("Demo Cinema District 4", "District 4 demo address", "0900000004"),
                theater("Demo Cinema District 5", "District 5 demo address", "0900000005")
        );
    }

    private Screen screen(String name, Theater theater, int capacity) {
        return screenRepository.findAll()
                .stream()
                .filter(screen -> screen.getName().equalsIgnoreCase(name)
                        && screen.getTheater().getId().equals(theater.getId()))
                .findFirst()
                .orElseGet(() -> screenRepository.save(Screen.builder()
                        .name(name)
                        .theater(theater)
                        .seatCapacity(capacity)
                        .build()));
    }

    private List<Screen> demoScreens(List<Theater> theaters) {
        return List.of(
                screen("Demo Screen 1", theaters.get(0), 12),
                screen("Demo Screen 2", theaters.get(1), 12),
                screen("Demo Screen 3", theaters.get(2), 12),
                screen("Demo Screen 4", theaters.get(3), 12),
                screen("Demo Screen 5", theaters.get(4), 12)
        );
    }

    private List<Seat> seats(Screen screen) {
        List<String> seatNumbers = List.of("A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4");
        for (String seatNumber : seatNumbers) {
            seatRepository.findAll()
                    .stream()
                    .filter(seat -> seat.getScreen().getId().equals(screen.getId())
                            && seat.getSeatNumber().equalsIgnoreCase(seatNumber))
                    .findFirst()
                    .orElseGet(() -> seatRepository.save(Seat.builder()
                            .screen(screen)
                            .seatNumber(seatNumber)
                            .type(seatNumber.startsWith("A") ? SeatType.STANDARD : SeatType.VIP)
                            .isVariable(false)
                            .build()));
        }
        return seatRepository.findAll()
                .stream()
                .filter(seat -> seat.getScreen().getId().equals(screen.getId()))
                .sorted((left, right) -> left.getSeatNumber().compareToIgnoreCase(right.getSeatNumber()))
                .toList();
    }

    private ShowTime showTime(Screen screen, Movie movie) {
        LocalDateTime startTime = nextWeekdayAt(10, 0);
        return showTime(screen, movie, startTime);
    }

    private ShowTime showTime(Screen screen, Movie movie, LocalDateTime startTime) {
        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());
        return showTimeRepository.findAll()
                .stream()
                .filter(showTime -> showTime.getScreen().getId().equals(screen.getId())
                        && showTime.getMovie().getId().equals(movie.getId())
                        && showTime.getStartTime().equals(startTime))
                .findFirst()
                .orElseGet(() -> showTimeRepository.save(ShowTime.builder()
                        .screen(screen)
                        .movie(movie)
                        .startTime(startTime)
                        .endTime(endTime)
                        .build()));
    }

    private List<ShowTime> demoShowTimes(List<Screen> screens, List<Movie> movies) {
        return List.of(
                showTime(screens.get(0), movies.get(0), nextWeekdayAt(11, 0)),
                showTime(screens.get(1), movies.get(1), nextWeekdayAt(13, 30)),
                showTime(screens.get(2), movies.get(2), nextWeekdayAt(16, 0)),
                showTime(screens.get(3), movies.get(3), nextWeekdayAt(18, 30)),
                showTime(screens.get(4), movies.get(4), nextWeekdayAt(20, 0))
        );
    }

    private LocalDateTime nextWeekdayAt(int hour, int minute) {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(2).withHour(hour).withMinute(minute).withSecond(0).withNano(0);
        while (dateTime.getDayOfWeek() == DayOfWeek.SATURDAY || dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dateTime = dateTime.plusDays(1);
        }
        return dateTime;
    }

    private void ticketPrices() {
        for (MovieType movieType : MovieType.values()) {
            ticketPrice(SeatType.STANDARD, movieType, false, 90000d);
            ticketPrice(SeatType.VIP, movieType, false, 120000d);
            ticketPrice(SeatType.SWEETBOX, movieType, false, 180000d);
            ticketPrice(SeatType.STANDARD, movieType, true, 110000d);
            ticketPrice(SeatType.VIP, movieType, true, 150000d);
            ticketPrice(SeatType.SWEETBOX, movieType, true, 220000d);
        }
    }

    private void ticketPrice(SeatType seatType, MovieType movieType, boolean weekend, double price) {
        boolean exists = ticketPriceRepository.findAll()
                .stream()
                .anyMatch(ticketPrice -> ticketPrice.getTypeSeat() == seatType
                        && ticketPrice.getTypeMovie() == movieType
                        && Boolean.valueOf(weekend).equals(ticketPrice.getDayType())
                        && LocalTime.MIN.equals(ticketPrice.getStartTime())
                        && LocalTime.of(23, 59).equals(ticketPrice.getEndTime()));
        if (!exists) {
            ticketPriceRepository.save(TicketPrice.builder()
                    .typeSeat(seatType)
                    .typeMovie(movieType)
                    .dayType(weekend)
                    .price(price)
                    .startTime(LocalTime.MIN)
                    .endTime(LocalTime.of(23, 59))
                    .build());
        }
    }

    private PaymentProvider paymentProvider(String name, String code, String description) {
        return paymentProviderRepository.findByProviderCodeIgnoreCase(code)
                .orElseGet(() -> paymentProviderRepository.save(PaymentProvider.builder()
                        .providerName(name)
                        .providerCode(code)
                        .description(description)
                        .status(true)
                        .build()));
    }

    private List<PaymentProvider> demoPaymentProviders() {
        return List.of(
                paymentProvider("PayPal", "PAYPAL", "PayPal sandbox provider"),
                paymentProvider("Stripe", "STRIPE", "Stripe sandbox provider"),
                paymentProvider("VNPAY", "VNPAY", "VNPAY provider"),
                paymentProvider("VietQR", "VIETQR", "VietQR local testing provider"),
                paymentProvider("Viettel Pay", "VIETTEL_PAY", "Viettel Pay local testing provider")
        );
    }

    private void completedBooking(User user, ShowTime showTime, Seat seat, PaymentProvider provider) {
        completedBooking(user, showTime, seat, provider, "SEED-BOOKING-001", PaymentMethod.PAYPAL, 90000d, 1);
    }

    private void completedBooking(
            User user,
            ShowTime showTime,
            Seat seat,
            PaymentProvider provider,
            String bookingCode,
            PaymentMethod paymentMethod,
            double amount,
            int index
    ) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseGet(() -> bookingRepository.save(Booking.builder()
                        .user(user)
                        .showTime(showTime)
                        .bookingCode(bookingCode)
                        .totalSeat(1)
                        .totalAmount(amount)
                        .status(BookingStatus.COMPLETED)
                        .paymentStatus(PaymentStatus.COMPLETED)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .build()));

        if (bookingSeatRepository.findByBooking_Id(booking.getId()).isEmpty()) {
            bookingSeatRepository.save(BookingSeat.builder()
                    .booking(booking)
                    .seat(seat)
                    .quantity(1)
                    .price(amount)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build());
        }

        if (!paymentRepository.existsByBooking_IdAndPaymentStatus(booking.getId(), PaymentStatus.COMPLETED)) {
            paymentRepository.save(Payment.builder()
                    .booking(booking)
                    .provider(provider)
                    .paymentMethod(paymentMethod)
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .paymentTime(LocalDateTime.now().minusDays(1))
                    .amount(BigDecimal.valueOf(amount))
                    .transactionId("SEED-TXN-" + String.format("%03d", index))
                    .paypalOrderId("SEED-PAYPAL-ORDER-" + String.format("%03d", index))
                    .paypalCaptureId("SEED-PAYPAL-CAPTURE-" + String.format("%03d", index))
                    .build());
        }
    }

    private void demoBookings(List<User> users, List<ShowTime> showTimes, List<PaymentProvider> providers) {
        PaymentMethod[] methods = {
                PaymentMethod.PAYPAL,
                PaymentMethod.STRIPE,
                PaymentMethod.VNPAY,
                PaymentMethod.VIETQR,
                PaymentMethod.VIETTEL_PAY
        };

        for (int i = 0; i < 5; i++) {
            List<Seat> showTimeSeats = seats(showTimes.get(i).getScreen());
            completedBooking(
                    users.get(i),
                    showTimes.get(i),
                    showTimeSeats.get(i),
                    providers.get(i),
                    "SEED-BOOKING-" + String.format("%03d", i + 2),
                    methods[i],
                    90000d + (i * 10000d),
                    i + 2
            );
        }
    }

    private void deduplicateTicketPrices() {
        Map<String, TicketPrice> firstByBusinessKey = new LinkedHashMap<>();
        ticketPriceRepository.findAll()
                .stream()
                .sorted((left, right) -> left.getId().compareTo(right.getId()))
                .forEach(ticketPrice -> {
                    String key = ticketPriceKey(ticketPrice);
                    TicketPrice existing = firstByBusinessKey.putIfAbsent(key, ticketPrice);
                    if (existing != null) {
                        ticketPriceRepository.delete(ticketPrice);
                    }
                });
        removeContainedTicketPriceRanges();
    }

    private void removeContainedTicketPriceRanges() {
        List<TicketPrice> prices = ticketPriceRepository.findAll()
                .stream()
                .sorted((left, right) -> left.getId().compareTo(right.getId()))
                .toList();

        for (TicketPrice candidate : prices) {
            if (!ticketPriceRepository.existsById(candidate.getId())) {
                continue;
            }

            for (TicketPrice other : prices) {
                if (candidate.getId().equals(other.getId())
                        || !ticketPriceRepository.existsById(other.getId())
                        || !sameTicketPriceType(candidate, other)) {
                    continue;
                }

                if (containsRange(other, candidate)) {
                    ticketPriceRepository.delete(candidate);
                    break;
                }
            }
        }
    }

    private String ticketPriceKey(TicketPrice ticketPrice) {
        return ticketPrice.getTypeSeat() + "|"
                + ticketPrice.getTypeMovie() + "|"
                + ticketPrice.getDayType() + "|"
                + ticketPrice.getStartTime() + "|"
                + ticketPrice.getEndTime();
    }

    private boolean sameTicketPriceType(TicketPrice left, TicketPrice right) {
        return left.getTypeSeat() == right.getTypeSeat()
                && left.getTypeMovie() == right.getTypeMovie()
                && Boolean.valueOf(left.getDayType()).equals(right.getDayType());
    }

    private boolean containsRange(TicketPrice outer, TicketPrice inner) {
        return !outer.getStartTime().isAfter(inner.getStartTime())
                && !outer.getEndTime().isBefore(inner.getEndTime());
    }

    private void content() {
        if (bannerRepository.count() == 0) {
            bannerRepository.save(Banner.builder()
                    .url("https://example.com/banner.jpg")
                    .type(BannerType.IMAGE)
                    .position("HOME_TOP")
                    .build());
        }

        Festival festival = festivalRepository.findAll()
                .stream()
                .filter(item -> item.getTitle().equalsIgnoreCase("Seed Film Festival"))
                .findFirst()
                .orElseGet(() -> festivalRepository.save(Festival.builder()
                        .title("Seed Film Festival")
                        .image("https://example.com/festival.jpg")
                        .startTime(LocalDateTime.now().plusDays(5))
                        .endTime(LocalDateTime.now().plusDays(12))
                        .build()));

        boolean newsExists = newsRepository.findAll()
                .stream()
                .anyMatch(news -> news.getTitle().equalsIgnoreCase("Seed Cinema News"));
        if (!newsExists) {
            newsRepository.save(News.builder()
                    .title("Seed Cinema News")
                    .content("Seed news content for local API testing")
                    .festival(festival)
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }

    private void demoContent() {
        List<Festival> festivals = List.of(
                festival("Demo Summer Film Week", "https://example.com/festival-summer.jpg", 6, 11),
                festival("Demo Animation Days", "https://example.com/festival-animation.jpg", 12, 18),
                festival("Demo Horror Night", "https://example.com/festival-horror.jpg", 20, 24),
                festival("Demo Family Cinema", "https://example.com/festival-family.jpg", 28, 35),
                festival("Demo Classic Weekend", "https://example.com/festival-classic.jpg", 40, 43)
        );

        for (int i = 0; i < 5; i++) {
            banner("https://example.com/demo-banner-" + (i + 1) + ".jpg", "HOME_SLOT_" + (i + 1));
            news(
                    "Demo Cinema News " + (i + 1),
                    "Demo news content " + (i + 1) + " for local API testing",
                    festivals.get(i)
            );
        }
    }

    private Banner banner(String url, String position) {
        return bannerRepository.findAll()
                .stream()
                .filter(banner -> banner.getPosition().equalsIgnoreCase(position))
                .findFirst()
                .orElseGet(() -> bannerRepository.save(Banner.builder()
                        .url(url)
                        .type(BannerType.IMAGE)
                        .position(position)
                        .build()));
    }

    private Festival festival(String title, String image, int startAfterDays, int endAfterDays) {
        return festivalRepository.findAll()
                .stream()
                .filter(item -> item.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseGet(() -> festivalRepository.save(Festival.builder()
                        .title(title)
                        .image(image)
                        .startTime(LocalDateTime.now().plusDays(startAfterDays))
                        .endTime(LocalDateTime.now().plusDays(endAfterDays))
                        .build()));
    }

    private void news(String title, String content, Festival festival) {
        boolean exists = newsRepository.findAll()
                .stream()
                .anyMatch(news -> news.getTitle().equalsIgnoreCase(title));
        if (!exists) {
            newsRepository.save(News.builder()
                    .title(title)
                    .content(content)
                    .festival(festival)
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }
}
