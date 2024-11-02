package com.be.service;

import com.be.entity.Subscription;
import com.be.entity.User;
import com.be.enums.PlanType;

public interface SubscriptionService {

    Subscription createSubscription(User user);
    Subscription getUserSubscription(Long userId) throws Exception;
    Subscription upgradeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
