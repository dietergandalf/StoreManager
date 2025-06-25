package com.dietergandalf.store_manager.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ADDRESS")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
	@SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1)
	@Column(name = "ADDRESS_ID")
	private Long addressId;

	@Column(name = "STREET", nullable = false)
	private String street;

	@Column(name = "POSTAL_CODE", nullable = false)
	private String postalCode;

	@Column(name = "CITY", nullable = false)
	private String city;

	@Column(name = "PROVINCE", nullable = false)
	private String province;

	@Column(name = "COUNTRY", nullable = false)
	private String country;

	
}
