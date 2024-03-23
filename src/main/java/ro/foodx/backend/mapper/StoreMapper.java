package ro.foodx.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.foodx.backend.dto.product.ProductCreateRequest;
import ro.foodx.backend.dto.store.StoreCreateRequest;
import ro.foodx.backend.dto.store.StoreEditRequest;
import ro.foodx.backend.model.store.Product;
import ro.foodx.backend.model.store.Store;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store convertToStore(StoreCreateRequest storeCreateRequest);
    Store convertToStore(StoreEditRequest storeEditRequest);

    Product converToProduct(ProductCreateRequest productCreateRequest);

}