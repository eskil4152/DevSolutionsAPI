package com.devsolutions.DevSolutionsAPI.Entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class BillingInfo {
    String paymentMethod;
    String paymentStatus;
    String billingAddress;
}
