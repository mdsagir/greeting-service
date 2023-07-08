package com.greeting.jpa;

import com.greeting.config.TestContainerConfig;
import com.greeting.entity.Customer;
import com.greeting.repo.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Import(TestContainerConfig.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepo customerRepo;


    @BeforeEach
    void cleanUp() {
        customerRepo.deleteAll();
    }

    @Test
    void findAllCustomer() {
        var sagir = new Customer("sagir", "tech.sagir@gmail.com", "9052708146");
        var asim = new Customer("asim", "tech.asim@gmail.com", "9052708140");
        customerRepo.saveAll(List.of(sagir, asim));
        var customers = customerRepo.findAll();
        assertThat(customers).isNotEmpty().hasSize(2);
    }

    @Test
    void findByEmailAllCustomer() {
        var sagir = new Customer("sagir", "tech.sagir@gmail.com", "9052708146");
        customerRepo.save(sagir);
        var exists = customerRepo.existsByEmail(sagir.getEmail());
        var notExists = customerRepo.existsByEmail("john@gmail.com");
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void findAllByPageNation() {
        var sagir = new Customer("sagir", "tech.sagir@gmail.com", "9052708146");
        var asim = new Customer("asim", "tech.asim@gmail.com", "9052708140");
        var sara = new Customer("sara", "tech.sara@gmail.com", "9052708141");
        customerRepo.saveAll(List.of(sagir, asim, sara));
        var pageRequest = PageRequest.of(0, 2);
        var customers = customerRepo.fetchAllCustomer(pageRequest);
        assertThat(customers).isNotEmpty().hasSize(2);
    }
}
