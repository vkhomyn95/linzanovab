package com.backend.linzanova.service.orders;

import com.backend.linzanova.dao.orders.DeliveryDao;
import com.backend.linzanova.entity.order.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService implements IDeliveryService {
    @Autowired
    private DeliveryDao deliveryDao;

    @Override
    public Delivery insertDelivery(Delivery delivery) {
        return deliveryDao.save(delivery);
    }
}
