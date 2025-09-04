package com.example.productorderManagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.productorderManagement.dto.AddressDTO;
import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.service.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }
    
    @PostMapping
    public ResponseEntity<AddressDTO> addNewAddress(@RequestBody Address address){
        AddressDTO addedAddress = addressService.addNewAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAddress);
    }

    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(@PathVariable Long userId) {
        List<AddressDTO> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
        AddressDTO addressDTO = addressService.updateAddress(addressId, updatedAddress);
        return ResponseEntity.ok(addressDTO);
    }


}
