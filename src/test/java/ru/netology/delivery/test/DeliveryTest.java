package ru.netology.delivery.test;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        Allure.step("Заполнение формы для первой встречи", () -> {
            $("[data-test-id='city'] input").setValue(validUser.getCity());
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(firstMeetingDate);
            $("[data-test-id='name'] input").setValue(validUser.getName());
            $("[data-test-id='phone'] input").setValue(validUser.getPhone());
            $("[data-test-id='agreement']").click();
            $(".button__text").click();
        });

        Allure.step("Проверка успешного планирования встречи", () -> {
            $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
            $("[data-test-id='success-notification'] .notification__content")
                    .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        });

        Allure.step("Изменение даты и перепланирование", () -> {
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
            $("[data-test-id='date'] input").setValue(secondMeetingDate);
            $(".button__text").click();
        });

        Allure.step("Проверка модального окна с предложением перепланировать", () -> {
            $("[data-test-id='replan-notification']").shouldBe(visible, Duration.ofSeconds(15));
            $("[data-test-id='replan-notification'] .notification__content")
                    .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
            // Нажатие кнопки "Перепланировать"
            $("[data-test-id='replan-notification'] .button__text").click();
        });

        Allure.step("Проверка успешного перепланирования встречи", () -> {
            $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
            $("[data-test-id='success-notification'] .notification__content")
                    .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
        });
    }
}