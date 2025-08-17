package com.example.productorderManagement.dto;
import java.util.List;
import java.util.stream.Collectors;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.User;

import lombok.Data;

@Data
public class AddressDTO {

    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private List<UserDTO> users;

    public AddressDTO(Address address) {
        this.addressId = address.getAddressId();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
        this.users = address.getUsers()
                        .stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList());
    }
}
