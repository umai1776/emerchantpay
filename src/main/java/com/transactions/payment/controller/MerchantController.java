package com.transactions.payment.controller;

import com.transactions.payment.dto.MerchantDto;
import com.transactions.payment.model.User;
import com.transactions.payment.model.response.MessageResponse;
import com.transactions.payment.repository.MerchantRepository;
import com.transactions.payment.services.MerchantServices;
import com.transactions.payment.services.SubscribeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class MerchantController {

    @Autowired private MerchantServices merchantServices;
    @Autowired private SubscribeServices subscribeServices;
    @Autowired
    private MerchantRepository merchantRepository;

    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @GetMapping("/merchants")
    public ResponseEntity<List<MerchantDto>> getAllMerchants() {
        return new ResponseEntity<>(merchantServices.listMerchants(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @PostMapping("/merchant/add")
    public ResponseEntity addMerchant(@Valid @RequestBody MerchantDto merchantDto) {
        if (merchantRepository.existsByName(merchantDto.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (merchantRepository.existsByEmail(merchantDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        merchantServices.addMerchant(merchantDto);
        subscribeServices.subscribe(merchantDto.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @GetMapping("/merchant/{id}")
    public ResponseEntity<MerchantDto> getMerchantById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(merchantServices.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/merchant/{id}")
    public ResponseEntity<User> updateMerchant(@PathVariable(value = "id") Long id,
                                           @Valid @RequestBody MerchantDto merchantDto) {
        merchantServices.update(id, merchantDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/merchant/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        merchantServices.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("/merchant/unsubscribe/{id}")
    public ResponseEntity<?> unsubscribe(@PathVariable(value = "id") Long id) {
        subscribeServices.unsubscribe(id);
        return ResponseEntity.ok().build();
    }
}
