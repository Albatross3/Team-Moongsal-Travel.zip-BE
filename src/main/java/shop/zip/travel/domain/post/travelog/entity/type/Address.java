package shop.zip.travel.domain.post.travelog.entity.type;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

	@Column(nullable = false)
	private Country country;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String spot;

	protected Address() {
	}

	public Address(String city, String spot) {
		this.city = city;
		this.spot = spot;
	}

	public Country getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getSpot() {
		return spot;
	}
}

