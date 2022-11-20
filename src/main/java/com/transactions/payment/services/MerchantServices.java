package com.transactions.payment.services;

import com.transactions.payment.dto.MerchantDto;
import com.transactions.payment.exception.ResourceNotFoundException;
import com.transactions.payment.model.Merchant;
import com.transactions.payment.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MerchantServices {

    @Autowired
    private MerchantRepository merchantRepository;


    @Transactional
    public List<MerchantDto> listMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        return  merchants.stream().map(this::mapFromMerchantToDto).collect(toList());
    }

    @Transactional
    public void add(MerchantDto merchantDto) {
        Merchant merchant = mapFromDtoToMerchant(merchantDto);
        merchantRepository.save(merchant);
    }

    @Transactional
    public void addMerchant(MerchantDto merchantDto) {
        Merchant merchant = mapFromDtoToMerchant(merchantDto);
        merchantRepository.save(merchant);
    }

    private Merchant mapFromDtoToMerchant(MerchantDto merchantDto) {
        Merchant merchant = new Merchant();
        merchant.setName(merchantDto.getName());
        merchant.setEmail(merchantDto.getEmail());
        merchant.setDescription(merchantDto.getDescription());
        merchant.setTransactions(merchantDto.getTransactions());
        merchant.setTotalTransactionSum(merchantDto.getTotalTransactionSum());
        return merchant;
    }

    @Transactional
    public MerchantDto findById(Long id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("For id " + id));
        return mapFromMerchantToDto(merchant);
    }

    @Transactional
    public void update(Long id, MerchantDto merchantDto) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("For id " + id));
        merchant.setName(merchantDto.getName());
        merchant.setEmail(merchantDto.getEmail());
        merchant.setDescription(merchantDto.getDescription());
        merchant.setTransactions(merchant.getTransactions());
        merchant.setTotalTransactionSum(merchant.getTotalTransactionSum());
        merchantRepository.save(merchant);
    }

    @Transactional
    public void delete(Long id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("For id " + id));
        merchantRepository.delete(merchant);
    }

    @Transactional
    MerchantDto mapFromMerchantToDto(Merchant merchant) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setId(merchant.getId());
        merchantDto.setName(merchant.getName());
        merchantDto.setDescription(merchant.getDescription());
        merchantDto.setEmail(merchant.getEmail());
        merchantDto.setTransactions(merchant.getTransactions());
        merchantDto.setTotalTransactionSum(merchantDto.getTotalTransactionSum());
        return  merchantDto;
    }
}
