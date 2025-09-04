package com.example.productorderManagement.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.AddressDTO;
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

    public AddressDTO addNewAddress(Address address){
        boolean exists = addressRepository.existsByStreetAndCityAndStateAndZipCode(
        address.getStreet(),
        address.getCity(),
        address.getState(),
        address.getZipCode());
        if(exists){
            throw new BadRequestException("this address already exists");
        }
        address.setCreatedAt(java.time.LocalDate.now());
        Address savedAddress = addressRepository.save(address);

    return new AddressDTO(savedAddress);
    }

    public List<AddressDTO> getAllAddresses() {
    List<Address> addresses = addressRepository.findAll();
    return addresses.stream()
            .map(AddressDTO::new)
            .toList();
    }

    public List<AddressDTO> getAddressesByUserId(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    List<Address> addresses = addressRepository.findByUsers(user);

    return addresses.stream()
            .map(AddressDTO::new)
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

    public AddressDTO updateAddress(Long addressId, Address updatedAddress) {
    Address existingAddress = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

    boolean exists = addressRepository.existsByStreetAndCityAndStateAndZipCode(
        updatedAddress.getStreet(),
        updatedAddress.getCity(),
        updatedAddress.getState(),
        updatedAddress.getZipCode()
    );

    if (exists) {
        throw new BadRequestException("This address already exists");
    }
    existingAddress.setStreet(updatedAddress.getStreet());
    existingAddress.setCity(updatedAddress.getCity());
    existingAddress.setState(updatedAddress.getState());
    existingAddress.setZipCode(updatedAddress.getZipCode());
    Address savedAddress = addressRepository.save(existingAddress);
    return new AddressDTO(savedAddress);
}


}
