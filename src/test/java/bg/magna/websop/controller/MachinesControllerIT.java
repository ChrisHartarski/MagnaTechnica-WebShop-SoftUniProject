package bg.magna.websop.controller;

import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "machines-api-service"))
public class MachinesControllerIT {

}
