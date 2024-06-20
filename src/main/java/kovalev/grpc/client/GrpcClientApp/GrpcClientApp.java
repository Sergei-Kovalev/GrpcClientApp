package kovalev.grpc.client.GrpcClientApp;

import kovalev.grpc.client.GrpcClientApp.controller.ClientAppController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GrpcClientApp {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(GrpcClientApp.class, args);

		ClientAppController controller = context.getBean(ClientAppController.class);
		controller.sendRequests();
	}
}
