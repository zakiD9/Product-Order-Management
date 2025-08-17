package com.example.productorderManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.repository.AddressRepository;
import com.example.productorderManagement.repository.UserRepository;

@Service
public class AddressService {
    
    private final AddressRepository addressRepository;
    

    public AddressService(AddressRepository addressRepository,UserRepository userRepository){
        this.addressRepository = addressRepository;

    }

    public Address addNewAddress(Address address){
        return addressRepository.save(address);
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    public void deleteAddress(Long addressId){
        addressRepository.deleteById(addressId);
    }

    public String updateAddress(Long addressId){
        Optional<Address> address = addressRepository.findById(addressId);
        if(address.isPresent()){
        return "address updated";
        }
        return "not found address";
    }

}
