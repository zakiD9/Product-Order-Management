package com.example.productorderManagement.service;

import java.util.List;
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
    

    public AddressService(AddressRepository addressRepository,OrderRepository orderRepository,UserRepository userRepository){
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;

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
        Address address = new Address();
        address.setCity(addressRequest.getCity());
        address.setStreet(addressRequest.getStreet());
        address.setState(addressRequest.getState());
        address.setZipCode(addressRequest.getZipCode());
        address.setCreatedAt(java.time.LocalDate.now());
        Address savedAddress = addressRepository.save(address);

    return new AddressResponse(savedAddress);
    }

    public List<AddressResponse> getAllAddresses() {
    List<Address> addresses = addressRepository.findAll();
    return addresses.stream()
            .map(AddressResponse::new)
            .toList();
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
    Address existingAddress = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

    boolean exists = addressRepository.existsByStreetAndCityAndStateAndZipCode(
        AddressRequest.getStreet(),
        AddressRequest.getCity(),
        AddressRequest.getState(),
        AddressRequest.getZipCode()
    );

    if (exists) {
        throw new BadRequestException("This address already exists");
    }
    existingAddress.setStreet(AddressRequest.getStreet());
    existingAddress.setCity(AddressRequest.getCity());
    existingAddress.setState(AddressRequest.getState());
    existingAddress.setZipCode(AddressRequest.getZipCode());
    Address savedAddress = addressRepository.save(existingAddress);
    return new AddressResponse(savedAddress);
}


}
