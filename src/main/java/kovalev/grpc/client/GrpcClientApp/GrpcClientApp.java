package kovalev.grpc.client.GrpcClientApp;

import com.sample.GrpcService;
import com.sample.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GrpcClientApp {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(GrpcClientApp.class, args);
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
				.usePlaintext().build();

		UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);

		for (int i = 0; i < 10; i++) {
			long id = (long) (Math.random() * 15);

			System.out.println("Клиент запрашивает рандомное ID = " + id);

			GrpcService.FindByIDRequest request = GrpcService.FindByIDRequest.newBuilder()
					.setId(id)
					.build();

			GrpcService.FindByIDResponse response = stub.findById(request);

			switch (response.getResponseCase()) {
				case SUCCESS_RESPONSE -> System.out.printf("""
					Response was success. Your Entity:
					%s
					""", response.getSuccessResponse());
				case ERROR_RESPONSE -> System.out.printf("""
					Error from server. INFO:
					%s
					""", response.getErrorResponse().getErrorDescription());
			}
			System.out.println("""
					
					----------------------------------------------
					
					""");

			TimeUnit.SECONDS.sleep(1);
		}
		channel.shutdownNow();
	}
}
