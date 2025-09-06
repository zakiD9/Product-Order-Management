package com.example.productorderManagement.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productorderManagement.dto.request.AddressRequest;
import com.example.productorderManagement.dto.response.AddressResponse;
import com.example.productorderManagement.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressResponse> addNewAddress(@Valid @RequestBody AddressRequest address){
        AddressResponse addedAddress = addressService.addNewAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAddress);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressResponse>> getAllAddresses(
        @RequestParam(required = false) String street,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String zipCode,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        Page<AddressResponse> addresses = addressService.getAllAddresses(street, city, state, zipCode, page, size);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<AddressResponse>> getAddressesByUserId(@PathVariable Long userId) {
        List<AddressResponse> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN') or @addressService.isOwner(#addressId, authentication.principal.id)")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN') or @addressService.isOwner(#addressId, authentication.principal.id)")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,@Valid @RequestBody AddressRequest updatedAddress) {
        AddressResponse addressDTO = addressService.updateAddress(addressId, updatedAddress);
        return ResponseEntity.ok(addressDTO);
    }
}
