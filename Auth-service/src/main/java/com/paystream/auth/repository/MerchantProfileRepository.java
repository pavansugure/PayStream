package com.paystream.auth.repository;

import com.paystream.auth.entity.MerchantProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {

	/*
	 * Finds the merchant profile associated with a particular User.
	 *
	 * The User relationship is one-to-one, so there should be at most one merchant
	 * profile for a user.
	 */
	Optional<MerchantProfile> findByUserId(Long userId);
}