package org.virtualpowerplant.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.virtualpowerplant.TestContainersConfiguration;
import org.virtualpowerplant.model.BatteryRequestDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestContainersConfiguration.class})
@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BatteryControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }


    @Test
    @DisplayName("POST /api/v1/batteries should successfully register multiple batteries")
    void shouldRegisterMultipleBatteries() {
        var request = given()
                .log()
                .everything()
                .contentType(ContentType.JSON)
                .body(List.of(BatteryRequestDto.builder()
                                               .name("Cannington")
                                               .postcode(6107)
                                               .wattCapacity(13500.0)
                                               .build(),
                                BatteryRequestDto.builder()
                                                 .name("Midland")
                                                 .postcode(6057)
                                                 .wattCapacity(50500.0)
                                                 .build()
                        )
                );

        var response = request.when()
                              .post("/api/v1/batteries");

        response.then()
                .log()
                .everything()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(".", notNullValue())
                .body(".", hasSize(greaterThanOrEqualTo(1)))
                .body("name", hasItems("Cannington", "Midland"))
                .body("postcode", hasItems(6107, 6057));
    }

    @Test
    @DisplayName("POST /api/v1/batteries should return 400 when battery data is invalid")
    void shouldReturnBadRequestWhenBatteryDataIsInvalid() {
        var request = given()
                .log()
                .everything()
                .contentType(ContentType.JSON)
                .body(List.of(BatteryRequestDto.builder()
                                               .name("Cannington")
                                               .postcode(6107)
                                               .wattCapacity(13500.0)
                                               .build(),
                                BatteryRequestDto.builder()
                                                 .postcode(6057)
                                                 .wattCapacity(50500.0)
                                                 .build()
                        )
                );

        var response = request.when()
                              .post("/api/v1/batteries");

        response.then()
                .log()
                .everything()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(".", notNullValue())
                .body("errors", hasSize(greaterThanOrEqualTo(1)));
    }


    @Test
    @DisplayName("GET /api/v1/batteries/stats should return statistics for batteries within postcode range")
    void shouldReturnBatteryStatisticsForPostcodeRange() {

        var request = given()
                .log()
                .everything()
                .contentType(ContentType.JSON)
                .queryParam("startPostcode", 6000)
                .queryParam("endPostcode", 6999)
                .queryParam("minCapacity", 14500.0)
                .queryParam("maxCapacity", 50500.0);


        var response = request.when()
                              .get("/api/v1/batteries/stats");

        response.then()
                .log()
                .everything()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(".", notNullValue())
                .body("batteryNames", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("POST /api/v1/batteries should handle concurrent requests successfully")
    void shouldHandleConcurrentBatteryRegistration() {
        int threads = 100;
        AtomicInteger successCount = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Boolean>> futures = IntStream.range(0, threads)
                                                                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                                                                    List<BatteryRequestDto> requestDtos = List.of(
                                                                            BatteryRequestDto.builder()
                                                                                             .name("Cannington")
                                                                                             .postcode(6107)
                                                                                             .wattCapacity(13500.0)
                                                                                             .build(),
                                                                            BatteryRequestDto.builder()
                                                                                             .name("Midland")
                                                                                             .postcode(6057)
                                                                                             .wattCapacity(50500.0)
                                                                                             .build()
                                                                    );
                                                                    var request = given()
                                                                            .contentType(ContentType.JSON)
                                                                            .body(requestDtos);

                                                                    var response = request.when()
                                                                                          .post("/api/v1/batteries");

                                                                    response.then()
                                                                            .assertThat()
                                                                            .statusCode(HttpStatus.SC_OK);
                                                                    successCount.incrementAndGet();

                                                                    return true;
                                                                }, executor))
                                                                .toList();

            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
            assert threads == successCount.get();
        }
    }

}