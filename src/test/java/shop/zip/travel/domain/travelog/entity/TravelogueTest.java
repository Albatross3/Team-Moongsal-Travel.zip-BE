package shop.zip.travel.domain.travelog.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

class TravelogueTest {

	private final String thumbnail = "www.naver.com";
	private final String title = "메인 게시물";

	@Test
	@DisplayName("서브 게시물 없이 메인 게시물을 생성 할 수 없다.")
	void create_fail_by_no_subTravelogues(){
		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			title,
			DummyGenerator.createCountry(),
			thumbnail,
			DummyGenerator.createCost(),
			null,
			DummyGenerator.createMember()
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("기간 없이 메인 게시물을 생성할 수 없다.")
	void create_fail_by_no_period(){
		assertThatThrownBy(() -> new Travelogue(
			null,
			title,
			DummyGenerator.createCountry(),
			thumbnail,
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		));
	}

	@Test
	@DisplayName("제목 없이 메인 게시물을 생성 할 수 없다.")
	void create_fail_by_no_title(){
		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			null,
			DummyGenerator.createCountry(),
			thumbnail,
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		));

		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			"",
			DummyGenerator.createCountry(),
			thumbnail,
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		));
	}

	@Test
	@DisplayName("나라 없이 메인 게시물을 생성 할 수 없다.")
	void create_fail_by_no_country(){
		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			title,
			null,
			thumbnail,
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("썸네일 없이 메인 게시물을 생성할 수 없다.")
	void create_fail_by_no_thumbnail(){
		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			title,
			DummyGenerator.createCountry(),
			null,
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		)).isInstanceOf(IllegalArgumentException.class);

		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			title,
			DummyGenerator.createCountry(),
			"",
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("경비 없이 메인 게시물을 생성할 수 없다.")
	void create_fail_by_no_cost(){
		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			title,
			DummyGenerator.createCountry(),
			thumbnail,
			null,
			List.of(DummyGenerator.createSubTravelogue()),
			DummyGenerator.createMember()
		)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("작성자 없이 메인 게시물을 생성할 수 없다.")
	void create_fail_by_no_member(){
		assertThatThrownBy(() -> new Travelogue(
			DummyGenerator.createPeriod(),
			title,
			DummyGenerator.createCountry(),
			thumbnail,
			DummyGenerator.createCost(),
			List.of(DummyGenerator.createSubTravelogue()),
			null
		)).isInstanceOf(IllegalArgumentException.class);
	}
}