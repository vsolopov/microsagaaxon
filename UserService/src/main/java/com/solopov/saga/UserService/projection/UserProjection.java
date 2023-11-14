package com.solopov.saga.UserService.projection;

import com.solopov.saga.CommonService.model.CardDetails;
import com.solopov.saga.CommonService.model.User;
import com.solopov.saga.CommonService.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {
    @QueryHandler
    public User getUserDetails(GetUserPaymentDetailsQuery query){

        //ideally to get the details from the db.
        CardDetails cardDetails = CardDetails.builder()
                .name("Vladyslav Solopov")
                .validUtillYear(2024)
                .validUntilMounth(12)
                .cardNumber("1234123413241234")
                .cvv(11)
                .build();

        return User.builder()
                .userId(query.getUserId())
                .firstName("Vladyslav")
                .lastName("Solopov")
                .cardDetails(cardDetails)
                .build();
    }
}
