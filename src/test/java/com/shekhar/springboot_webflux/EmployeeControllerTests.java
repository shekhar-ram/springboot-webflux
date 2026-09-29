package com.shekhar.springboot_webflux;

import com.shekhar.springboot_webflux.controller.EmployeeController;
import com.shekhar.springboot_webflux.dto.EmployeeDto;
import com.shekhar.springboot_webflux.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        //given
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@gmail.com");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        //when
        WebTestClient.ResponseSpec responseSpec = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        //then
        responseSpec.expectStatus().isCreated()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {
        //given
        String employeeId = "123";
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@gmail.com");

        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

        //when
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/api/employees/{id}",
                    Collections.singletonMap("id", employeeId))
                .exchange();

        //then
        responseSpec.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
        //given
        List<EmployeeDto> list = new ArrayList<>();
        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("John");
        employeeDto1.setLastName("Doe");
        employeeDto1.setEmail("john.doe@gmail.com");
        list.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Jane");
        employeeDto2.setLastName("Doe");
        employeeDto2.setEmail("jane.doe@gmail.com");
        list.add(employeeDto2);

        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(list);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeDtoFlux);

        //when
        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        responseSpec.expectStatus()
                .isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);

    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        //given
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Jane");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("jane.doe@gmail.com");

        BDDMockito.given(employeeService.updateEmployee(
                ArgumentMatchers.any(EmployeeDto.class),
                ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(employeeDto));

        //when
        WebTestClient.ResponseSpec responseSpec = webTestClient.put().uri("/api/employees/{id}",
                Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        //then
        responseSpec.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        String employeeId = "123";
        Mono<Void> voidMono = Mono.empty();
        //given
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(voidMono);

        //when
        WebTestClient.ResponseSpec responseSpec = webTestClient.delete().uri("/api/employees/{id}",
                Collections.singletonMap("id", employeeId)).exchange();

        //then
        responseSpec.expectStatus()
                .isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
