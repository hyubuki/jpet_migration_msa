package org.mybatis.jpetstore.common.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.mybatis.jpetstore.common.domain.Item;
import org.mybatis.jpetstore.common.domain.Product;
import org.mybatis.jpetstore.grpc.*;

import java.util.List;
import java.util.Map;

public class CatalogGrpcClient {

    @GrpcClient("catalog")
    private CatalogServiceGrpc.CatalogServiceBlockingStub stub;


    /**
     * 지정한 카테고리 ID에 해당하는 상품 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 상품 목록
     */
    public List<Product> getProductListByCategory(String categoryId) {
        ProductRequest request = ProductRequest.newBuilder().setCategoryId(categoryId).build();
        ProductList response = stub.getProductList(request);
        return response.getProductsList().stream().map(p -> {
            Product prod = new Product();
            prod.setProductId(p.getProductId());
            prod.setCategoryId(p.getCategoryId());
            prod.setName(p.getName());
            prod.setDescription(p.getDescription());
            return prod;
        }).toList();
    }

    /**
     * 지정한 상품 ID에 해당하는 아이템을 조회합니다.
     *
     * @param itemId 아이템 ID
     * @return 아이템 정보
     */
    public Item getItem(String itemId) {
        ItemRequest req = ItemRequest.newBuilder().setItemId(itemId).build();
        ItemResponse resp = stub.getItem(req);

        return convertToItem(resp);
    }

    private static Item convertToItem(ItemResponse resp) {
        ItemProto proto = resp.getItem();
        Item item = new Item();
        item.setItemId(proto.getItemId());
        item.setProduct(new Product());
        item.getProduct().setProductId(proto.getProductId());
        item.getProduct().setCategoryId(proto.getProduct().getCategoryId());
        item.getProduct().setName(proto.getProduct().getName());
        item.getProduct().setDescription(proto.getProduct().getDescription());
        item.setListPrice(java.math.BigDecimal.valueOf(proto.getListPrice()));
        item.setUnitCost(java.math.BigDecimal.valueOf(proto.getUnitCost()));
        item.setSupplierId(proto.getSupplierId());
        item.setStatus(proto.getStatus());
        item.setAttribute1(proto.getAttribute1());
        item.setAttribute2(proto.getAttribute2());
        item.setAttribute3(proto.getAttribute3());
        item.setAttribute4(proto.getAttribute4());
        item.setAttribute5(proto.getAttribute5());
        item.setQuantity(proto.getQuantity());
        return item;
    }

    /**
     * 아이템이 재고에 존재하는지 확인합니다.
     *
     * @param itemId 아이템 ID
     * @return 재고 존재 여부
     */
    public boolean isItemInStock(String itemId) {
        ItemRequest req = ItemRequest.newBuilder().setItemId(itemId).build();
        StockResponse resp = stub.isItemInStock(req);
        return resp.getInStock();
    }



    /**
     * 지정한 상품 ID에 해당하는 상품 재고를 변경합니다.
     * 재고 변경은 주문 ID와 함께 수행되어야 하며, 주문 ID는 재고 업데이트가 성공적으로 완료되었는지 확인하는 데 사용됩니다.
     * @param param 아이템 ID와 변경할 수량을 포함하는 맵
     * @param orderId 주문 ID
     * @return 재고 업데이트 성공 여부
     */
    public boolean updateInventoryQuantity(Map<String, Object> param, Integer orderId) {
        UpdateInventoryRequest request = UpdateInventoryRequest.newBuilder()
                .addAllItemId(param.keySet())
                .addAllIncrement(param.values().stream().map(v -> Integer.parseInt(String.valueOf(v))).toList())
                .setOrderId(orderId)
                .build();
        BooleanResponse resp = stub.updateInventoryQuantity(request);
        return resp.getResult();
    }

    /**
     * 지정한 주문 ID에 대한 재고 업데이트가 성공적으로 완료되었는지 확인합니다.
     *
     * @param orderId 주문 ID
     * @return 재고 업데이트 성공 여부
     */
    public boolean isInventoryUpdateCommitSuccess(Integer orderId){
        IsInventoryUpdatedRequest request = IsInventoryUpdatedRequest.newBuilder().setOrderId(orderId).build();
        BooleanResponse resp = stub.isInventoryUpdated(request);
        return resp.getResult();
    }

    /**
     * 지정한 아이템 ID에 대한 재고 수량을 조회합니다.
     *
     * @param itemId 아이템 ID
     * @return 재고 수량
     */
    public int getInventoryQuantity(String itemId) {
        ItemRequest request = ItemRequest.newBuilder().setItemId(itemId).build();
        QuantityResponse resp = stub.getInventoryQuantity(request);
        return resp.getQuantity();
    }
}
