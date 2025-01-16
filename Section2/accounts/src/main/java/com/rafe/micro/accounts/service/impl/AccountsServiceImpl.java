package com.rafe.micro.accounts.service.impl;

import com.rafe.micro.accounts.constants.AccountsConstants;
import com.rafe.micro.accounts.dto.CustomerDto;
import com.rafe.micro.accounts.entity.Accounts;
import com.rafe.micro.accounts.entity.Customer;
import com.rafe.micro.accounts.exception.CustomerAlreadyExistsException;
import com.rafe.micro.accounts.mapper.AccountsMapper;
import com.rafe.micro.accounts.mapper.CustomerMapper;
import com.rafe.micro.accounts.repository.AccountsRepository;
import com.rafe.micro.accounts.repository.CustomerRepository;
import com.rafe.micro.accounts.service.IAccountsService;
import com.rafe.micro.accounts.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedBy("Anonymous");
        customer.setCreatedAt(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedBy("Anonymous");
        newAccount.setCreatedAt(LocalDateTime.now());
        return newAccount;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
       // customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        return true;
//        boolean isUpdated = false;
//       // AccountsDto accountsDto = customerDto.getAccountsDto();
//        if(accountsDto !=null ){
//            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
//                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
//            );
//            AccountsMapper.mapToAccounts(accountsDto, accounts);
//            accounts = accountsRepository.save(accounts);
//
//            Long customerId = accounts.getCustomerId();
//            Customer customer = customerRepository.findById(customerId).orElseThrow(
//                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
//            );
//            CustomerMapper.mapToCustomer(customerDto,customer);
//            customerRepository.save(customer);
//            isUpdated = true;
//        }
//        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}
