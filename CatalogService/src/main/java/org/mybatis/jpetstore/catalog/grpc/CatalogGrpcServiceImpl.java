package org.mybatis.jpetstore.catalog.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.mybatis.jpetstore.catalog.service.CatalogService;
import org.mybatis.jpetstore.common.domain.Item;
import org.mybatis.jpetstore.common.domain.Product;
import org.mybatis.jpetstore.grpc.*;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class CatalogGrpcServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private final CatalogService catalogService;

    @Override
    public void getProductList(ProductRequest request, StreamObserver<ProductList> responseObserver) {
        List<Product> products = catalogService.getProductListByCategory(request.getCategoryId());
        ProductList.Builder builder = ProductList.newBuilder();
        for (Product p : products) {
            builder.addProducts(ProductProto.newBuilder()
                    .setProductId(p.getProductId())
                    .setCategoryId(p.getCategoryId())
                    .setName(p.getName() == null ? "" : p.getName())
                    .setDescription(p.getDescription() == null ? "" : p.getDescription())
                    .build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getItem(ItemRequest request, StreamObserver<ItemResponse> responseObserver) {
        Item item = catalogService.getItem(request.getItemId());
        ItemProto proto = ItemProto.newBuilder()
                .setItemId(item.getItemId())
                .setProductId(item.getProduct().getProductId())
                .build();
        responseObserver.onNext(ItemResponse.newBuilder().setItem(proto).build());
        responseObserver.onCompleted();
    }

    @Override
    public void isItemInStock(ItemRequest request, StreamObserver<StockResponse> responseObserver) {
        boolean inStock = catalogService.isItemInStock(request.getItemId());
        responseObserver.onNext(StockResponse.newBuilder().setInStock(inStock).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getInventoryQuantity(ItemRequest request, StreamObserver<QuantityResponse> responseObserver) {
        int quantity = catalogService.getItemQuantity(request.getItemId());
        responseObserver.onNext(QuantityResponse.newBuilder().setQuantity(quantity).build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateInventoryQuantity(UpdateInventoryRequest request, StreamObserver<BooleanResponse> responseObserver) {
        try {
            catalogService.updateItemQuantity(request.getItemIdList(), request.getIncrementList(), request.getOrderId());
            responseObserver.onNext(BooleanResponse.newBuilder().setResult(true).build());
        } catch (Exception e) {
            responseObserver.onNext(BooleanResponse.newBuilder().setResult(false).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void isInventoryUpdated(IsInventoryUpdatedRequest request, StreamObserver<BooleanResponse> responseObserver) {
        boolean result = catalogService.isInventoryUpdateSuccess(request.getOrderId());
        responseObserver.onNext(BooleanResponse.newBuilder().setResult(result).build());
        responseObserver.onCompleted();
    }
}
