package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    public static DataGenerator Registration;

    private DataGenerator() {}

    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now().plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    private static String generateCity(Faker faker) {
        String[] cities = {"Красноярск", "Москва", "Санкт-Петербург", "Новосибирск",
                "Абакан", "Нижний Новгород", "Астрахань", "Сочи",
                "Челябинск", "Краснодар"};
        Random random = new Random();
        return cities[random.nextInt(cities.length)];
    }

    private static String generateName(Faker faker) {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    private static String generatePhone(Faker faker) {
        return faker.phoneNumber().phoneNumber();
    }

    public static UserInfo generateUser(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return new UserInfo(generateCity(faker), generateName(faker), generatePhone(faker));
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}