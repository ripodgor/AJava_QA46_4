package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test   // №1
    public void shouldBeSuccessfullyCompleted1() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Волгоград");
        String generateDate = generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(generateDate);
        $("[data-test-id='name'] input").setValue("Иванов-Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79019012121");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id=notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + generateDate));
    }

    @Test   // №2
    public void shouldBeSuccessfullyCompleted2() {
        open("http://localhost:9999");
        String city = "Волгоград";
        int dayToAdd = 7;
        int defaultAddedDays = 3;
        $("[data-test-id=city] input").setValue(city.substring(0, 2));
        $$(".menu-item__control").findBy(text(city)).click();
        $("[data-test-id=date] input").click();
        if (!generateDate(defaultAddedDays, "MM").equals(generateDate(dayToAdd, "MM"))) {
            $("[data-step=1']").click();
        }
        $$(".calendar__day").findBy(text(generateDate(dayToAdd, "d"))).click();
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79019012121");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + generateDate(dayToAdd, "dd.MM.yyyy")));

    }
}

