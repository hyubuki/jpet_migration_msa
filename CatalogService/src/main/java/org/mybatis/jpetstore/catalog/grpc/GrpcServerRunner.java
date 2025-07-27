//package org.mybatis.jpetstore.grpc;
//
//import io.grpc.Server;
//import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class GrpcServerRunner {
//
//    private final CatalogGrpcServiceImpl service;
//
//    @Value("${grpc.port:9090}")
//    private int port;
//
//    private Server server;
//
//    public GrpcServerRunner(CatalogGrpcServiceImpl service) {
//        this.service = service;
//    }
//
//    @PostConstruct
//    public void start() throws IOException {
//        server = NettyServerBuilder.forPort(port)
//                .addService(service)
//                .build()
//                .start();
//    }
//}
