package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test   // №1
    public void shouldSuccessfulFormSubmission() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Волгоград");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=date] input").setValue(verificationDate);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79019012121");
        $("[data-test-id=agreement]").click();
        $("//*[text()=\"Забронировать\"]").click();
        $("[data-test-id=notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate));
    }

    @Test   // №2
    public void shouldSuccessfulFormSubmissionAfterInteractingWithComplexElements() {
        open("http://localhost:9999");
        String city = "Волгоград";
        $("[data-test-id=city] input").setValue(city.substring(0, 2));
        $(Selectors.byText("Волгоград")).click();
        $("[data-test-id=date] input").click();
        int days = 4;
        for (int cycle = 0; cycle < days; cycle++) {
            $(".calendar").sendKeys(Keys.ARROW_RIGHT);
        }
        $(".calendar").sendKeys(Keys.ENTER);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79019012121");
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(Condition.text("Забронировать")).click();
        String verificationDate = LocalDate.now().plusDays(7)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate));
    }
}