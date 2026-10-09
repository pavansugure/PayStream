package com.paystream.auth.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "customer_profiles", uniqueConstraints = {
		@UniqueConstraint(name = "uk_customer_profile_user", columnNames = "user_id") })
public class CustomerProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Column(nullable = false, length = 100)
	private String firstName;

	@Column(nullable = false, length = 100)
	private String lastName;

	@Column(length = 20, unique = true)
	private String phoneNumber;

	private LocalDate dateOfBirth;

	@Column(length = 200)
	private String street;

	@Column(length = 100)
	private String city;

	@Column(length = 100)
	private String state;

	@Column(length = 20)
	private String postalCode;

	@Column(length = 100)
	private String country;

	protected CustomerProfile() {
	}

	public CustomerProfile(User user, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth,
			String street, String city, String state, String postalCode, String country) {

		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
	}

	public Long getProfileId() {
		return profileId;
	}

	public User getUser() {
		return user;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void updateProfile(String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth,
			String street, String city, String state, String postalCode, String country) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
	}
}	