package com.solopov.saga.UserService.controller;

import com.solopov.saga.CommonService.model.User;
import com.solopov.saga.CommonService.queries.GetUserPaymentDetailsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
@RequestMapping
public class UserController {

    private transient QueryGateway queryGateway;

    public UserController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }


    @GetMapping("/{userId}")
    public User getUserPaymentDetails(@PathVariable String userId) {
        GetUserPaymentDetailsQuery userPaymentDetailsQuery = new GetUserPaymentDetailsQuery(userId);

        User user = queryGateway.query(userPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        return user;
    }
}
