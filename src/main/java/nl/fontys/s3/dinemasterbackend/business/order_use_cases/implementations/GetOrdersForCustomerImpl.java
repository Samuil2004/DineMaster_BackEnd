package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.OrderConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrdersForCustomer;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.OrderEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetOrdersForCustomerImpl implements GetOrdersForCustomer {
    private final OrderEntityRepository orderEntityRepository;
    private final OrderConverter orderConverter;

    @Override
    public GetOrdersResponse getOrdersForCustomer(GetOrdersForCustomerRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNumber() - 1, 10);

        List<OrderEntity> foundOrdersByCustomerId = orderEntityRepository.findByCustomerEntityUserId(request.getCustomerId(), pageable);
        long totalCountOfOrdersForTheCustomer = orderEntityRepository.countByCustomerEntityUserId(request.getCustomerId());
        return GetOrdersResponse.builder().allOrders(foundOrdersByCustomerId.stream().map(orderConverter::convertEntityToNormal).toList()).totalCount(totalCountOfOrdersForTheCustomer).build();
    }
}
