package com.example.productorderManagement.dto.response;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.productorderManagement.model.Address;

import lombok.Data;

@Data
public class AddressResponse {

    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public AddressResponse(Address address) {
        this.addressId = address.getAddressId();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
    }
}
