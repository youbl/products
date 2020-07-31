package com.beinet.carsmanagement.owner.repository;

import com.beinet.carsmanagement.owner.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    List<Owner> findByOrderByAddress();

    List<Owner> findByAddressLikeOrderByAddress(String address);

    Owner findByPhone(String phone);

    Owner findByAddress(String address);

}
