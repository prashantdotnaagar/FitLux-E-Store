package com.fitlux.estore.constants.serviceCodes.enums;

public enum PermissionCode {

    //    For user specific

    USER_VIEW,
    USER_UPDATE,
    USER_DEACTIVATE,
    USER_REACTIVATE,
    ROLE_ASSIGN,
    ROLE_REVOKE,

    //    For products specific

    PRODUCT_VIEW,
    PRODUCT_MANAGE,


    //    For Categories specific
    CATEGORY_VIEW,
    CATEGORY_MANAGE,

    //    For Brands specific

    BRAND_VIEW,
    BRAND_MANAGE,


    //    For Inventory specific

    INVENTORY_VIEW,
    INVENTORY_UPDATE,
    INVENTORY_ADJUST,

    //    For Cart specific

    CART_VIEW,
    CART_UPDATE,


    //    For  WISHLIST specific

    WISHLIST_VIEW,
    WISHLIST_UPDATE,


    //    For ORDER specific

    ORDER_VIEW,
    ORDER_STATUS_UPDATE,
    ORDER_CANCEL,
    ORDER_REFUND,

    //    For PAYMENT specific

    PAYMENT_VIEW,
    REFUND_INITIATE,
    REFUND_APPROVE,


    //    For COUPON specific

    COUPON_VIEW,
    COUPON_CREATE,
    COUPON_DISABLE,


    //    For System specific
    REPORT_VIEW,
    SYSTEM_CONFIG
}
