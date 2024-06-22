package kovalev.grpc.client.GrpcClientApp.controller;

import com.sample.GrpcService;
import com.sample.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@Controller
public class ClientAppController {

    public static final String RANDOM_ID_MESSAGE = "Клиент запрашивает рандомное ID = ";
    public static final String SUCCESS_RESPONSE_TEMPLATE = """
            Response was success. Your Entity:
            %s
            """;
    public static final String ERROR_RESPONSE_TEMPLATE = """
            Error from server. INFO:
            %s
            """;
    public static final String END_MESSAGE_LINE = """
            					
            ----------------------------------------------
            					
            """;
    public static final int REQUESTS_COUNT = 10;
    public static final int MAX_REQUESTED_ID = 15;
    public static final int TIME_BETWEEN_REQUESTS_SEC = 1;
    public static final int SERVER_PORT = 8090;

    public void sendRequests() throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", SERVER_PORT)
                .usePlaintext().build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);

        for (int i = 0; i < REQUESTS_COUNT; i++) {
            long id = (long) (Math.random() * MAX_REQUESTED_ID + 1);

            System.out.println(RANDOM_ID_MESSAGE + id);

            GrpcService.FindByIDRequest request = GrpcService.FindByIDRequest.newBuilder()
                    .setId(id)
                    .build();

            GrpcService.FindByIDResponse response = stub.findById(request);

            switch (response.getResponseCase()) {
                case SUCCESS_RESPONSE -> System.out.printf(SUCCESS_RESPONSE_TEMPLATE, response.getSuccessResponse());
                case ERROR_RESPONSE -> System.out.printf(ERROR_RESPONSE_TEMPLATE, response.getErrorResponse().getErrorDescription());
            }
            System.out.print(END_MESSAGE_LINE);

            TimeUnit.SECONDS.sleep(TIME_BETWEEN_REQUESTS_SEC);
        }
        channel.shutdownNow();
    }
}
