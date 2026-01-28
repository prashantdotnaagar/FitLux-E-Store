package com.fitlux.estore.constants.serviceCodes.enums;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;
import lombok.Getter;

@Getter
public enum serviceCodeImpl implements ServiceCode {

    /* ===================== ORDER (FSO) ===================== */
    ORDER_PLACED_SUCCESS("FSO001", "Order placed successfully"),
    ORDER_CANCELLED_SUCCESS("FSO002", "Order cancelled successfully"),
    ORDER_NOT_FOUND("FSO003", "Order not found"),
    ORDER_STATUS_UPDATED("FSO004", "Order status updated successfully"),
    ORDER_CANNOT_BE_CANCELLED("FSO005", "Order cannot be cancelled at this stage"),

    /* ===================== TRANSACTION / PAYMENT (FST) ===================== */
    TRANSACTION_SUCCESS("FST001", "Transaction successful"),
    TRANSACTION_FAILED("FST002", "Transaction failed"),
    TRANSACTION_PENDING("FST003", "Transaction pending"),
    PAYMENT_VERIFICATION_FAILED("FST004", "Payment verification failed"),
    REFUND_INITIATED("FST005", "Refund initiated successfully"),
    REFUND_COMPLETED("FST006", "Refund completed successfully"),

    /* ===================== PRODUCT (FSP) ===================== */
    PRODUCT_CREATED("FSP001", "Product created successfully"),
    PRODUCT_UPDATED("FSP002", "Product updated successfully"),
    PRODUCT_DELETED("FSP003", "Product deleted successfully"),
    PRODUCT_NOT_FOUND("FSP004", "Product not found"),
    PRODUCT_OUT_OF_STOCK("FSP005", "Product is out of stock"),

    /* ===================== CART (FSC) ===================== */
    ITEM_ADDED_TO_CART("FSC001", "Item added to cart"),
    ITEM_REMOVED_FROM_CART("FSC002", "Item removed from cart"),
    CART_UPDATED("FSC003", "Cart updated successfully"),
    CART_EMPTY("FSC004", "Cart is empty"),
    CART_NOT_FOUND("FSC005", "Cart not found"),

    /* ===================== WISHLIST (FSW) ===================== */
    ITEM_ADDED_TO_WISHLIST("FSW001", "Item added to wishlist"),
    ITEM_REMOVED_FROM_WISHLIST("FSW002", "Item removed from wishlist"),

    /* ===================== AUTH (FSA) ===================== */
    LOGIN_SUCCESS("FSA001", "Login successful"),
    LOGIN_FAILED("FSA002", "Invalid credentials"),
    TOKEN_EXPIRED("FSA003", "Authentication token expired"),
    ACCESS_DENIED("FSA004", "Access denied"),
    REFRESH_TOKEN_EXPIRED("FSA005", "Refresh token expired"),
    INVALID_REFRESH_TOKEN("FSA006", "Invalid refresh token"),
    TOKEN_REFRESH_SUCCESS("FSA007", "Token refreshed successfully"),
    ALREADY_LOGGED_IN("FSA008", "User already logged in"),
    LOGOUT_SUCCESS("FSA009", "Logout successful"),
    USER_NOT_LOGGED_IN("FSA010", "Please login first"),

    /* ===================== USER (FSU) ===================== */
    USER_CREATED("FSU001", "User registered successfully"),
    USER_ALREADY_EXISTS("FSU002", "User already exists"),
    USER_NOT_FOUND("FSU003", "User not found"),
    USER_DEACTIVATED("FSU004", "User account deactivated"),

    /* ===================== ADMIN (FSD) ===================== */
    ADMIN_ACTION_SUCCESS("FSD001", "Admin action completed successfully"),
    ADMIN_ACCESS_DENIED("FSD002", "Admin access denied"),

    /* ===================== COUPON (FSK) ===================== */
    COUPON_APPLIED("FSK001", "Coupon applied successfully"),
    COUPON_INVALID("FSK002", "Invalid or expired coupon"),

    /* ===================== INVENTORY (FSI) ===================== */
    STOCK_UPDATED("FSI001", "Stock updated successfully"),
    INSUFFICIENT_STOCK("FSI002", "Insufficient stock available"),

    /* ===================== SYSTEM / GENERIC (FSG) ===================== */
    REQUEST_SUCCESS("FSG001", "Request processed successfully"),
    INTERNAL_SERVER_ERROR("FSG002", "Internal server error"),
    INVALID_REQUEST("FSG003", "Invalid request parameters");

    private final String code;
    private final String message;

    serviceCodeImpl(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
