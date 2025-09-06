package com.example.productorderManagement.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.request.AddressRequest;
import com.example.productorderManagement.dto.response.AddressResponse;
import com.example.productorderManagement.exception.BadRequestException;
import com.example.productorderManagement.exception.ResourceNotFoundException;
import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.User;
import com.example.productorderManagement.repository.AddressRepository;
import com.example.productorderManagement.repository.OrderRepository;
import com.example.productorderManagement.repository.UserRepository;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    

    public AddressService(AddressRepository addressRepository,OrderRepository orderRepository,UserRepository userRepository,ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public AddressResponse addNewAddress(AddressRequest addressRequest){
        boolean exists = addressRepository.existsByStreetAndCityAndStateAndZipCode(
        addressRequest.getStreet(),
        addressRequest.getCity(),
        addressRequest.getState(),
        addressRequest.getZipCode());
        if(exists){
            throw new BadRequestException("this address already exists");
        }
        Address address = modelMapper.map(addressRequest, Address.class);
        address.setCreatedAt(java.time.LocalDate.now());
        Address savedAddress = addressRepository.save(address);

    return new AddressResponse(savedAddress);
    }

    public Page<AddressResponse> getAllAddresses(
        String street, String city, String state, String zipCode,
        int page, int size
) {
    Pageable pageable = PageRequest.of(page, size);

    if (street != null && !street.isBlank()) {
        return addressRepository.findByStreetContainingIgnoreCase(street, pageable)
                .map(AddressResponse::new);
    }
    if (city != null && !city.isBlank()) {
        return addressRepository.findByCityContainingIgnoreCase(city, pageable)
                .map(AddressResponse::new);
    }
    if (state != null && !state.isBlank()) {
        return addressRepository.findByStateContainingIgnoreCase(state, pageable)
                .map(AddressResponse::new);
    }
    if (zipCode != null && !zipCode.isBlank()) {
        return addressRepository.findByZipCode(zipCode, pageable)
                .map(AddressResponse::new);
    }

    return addressRepository.findAll(pageable).map(AddressResponse::new);
    }


    public List<AddressResponse> getAddressesByUserId(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    List<Address> addresses = addressRepository.findByUsers(user);

    return addresses.stream()
            .map(AddressResponse::new)
            .toList();
    }


    public void deleteAddress(Long addressId){
        Address address = addressRepository.findById(addressId)
        .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (orderRepository.existsByAddress(address)) {
        throw new BadRequestException("Cannot delete address linked to an order");
    }
        addressRepository.deleteById(addressId);
    }

    public AddressResponse updateAddress(Long addressId, AddressRequest AddressRequest) {
    Boolean addressIdExists = addressRepository.existsById(addressId);
    if (!addressIdExists) {
        throw new ResourceNotFoundException("Address not found with id: " + addressId);
    }
    boolean exists = addressRepository.existsByStreetAndCityAndStateAndZipCode(
        AddressRequest.getStreet(),
        AddressRequest.getCity(),
        AddressRequest.getState(),
        AddressRequest.getZipCode()
    );

    if (exists) {
        throw new BadRequestException("This address already exists");
    }
    Address address = modelMapper.map(AddressRequest, Address.class);
    address.setAddressId(addressId);
    Address savedAddress = addressRepository.save(address);
    return new AddressResponse(savedAddress);
}


}
