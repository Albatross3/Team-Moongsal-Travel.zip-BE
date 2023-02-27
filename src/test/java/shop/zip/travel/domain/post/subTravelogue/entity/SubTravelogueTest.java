package shop.zip.travel.domain.post.subTravelogue.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;

class SubTravelogueTest {

  private static final String validTitle = "오사카 다녀옴.";
  private static final String validContent = "오사카 존잼임.";

  @Test
  @DisplayName("제목의 길이가 50글자를 넘어갈 수 없다.")
  void test_title_over_50() {
    String title = "제목은 50 글자를 넘을 수 없어요.제목은 50 글자를 넘을 수 없어요.제목은 50 글자를 넘을 수 없어요.제목은 50 글자를 넘을 수 없어요.";

    assertThatThrownBy(() -> new SubTravelogue(
        title,
        validContent,
        List.of(DummyGenerator.createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("제목은 null이거나 빈 값일 수 없다.")
  void test_create_no_title() {
    String title = " ";

    assertThatThrownBy(() -> new SubTravelogue(
        title,
        validContent,
        List.of(DummyGenerator.createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new SubTravelogue(
        null,
        validContent,
        List.of(DummyGenerator.createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("내용은 빈 값일 수 없다.")
  void test_no_content() {
    String content = " ";

    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        content,
        List.of(DummyGenerator.createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        null,
        List.of(DummyGenerator.createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주소는 비어있을 수 없다.")
  void test_no_address() {
    List<Address> emptyList = new ArrayList<>();

    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        validContent,
        emptyList,
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        validContent,
        null,
        Set.of(Transportation.BUS),
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("이동 수단은 비어있을 수 없다.")
  void test_no_transportations() {
    Set<Transportation> emptySet = new HashSet<>();

    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        validContent,
        List.of(DummyGenerator.createAddress()),
        emptySet,
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        validContent,
        List.of(DummyGenerator.createAddress()),
        null,
        new ArrayList<>()
    )).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("이미지는 Null 값일 수 없다.")
  void test_travelPhotos_null() {
    assertThatThrownBy(() -> new SubTravelogue(
        validTitle,
        validContent,
        List.of(DummyGenerator.createAddress()),
        Set.of(Transportation.BUS),
        null
    )).isInstanceOf(IllegalArgumentException.class);
  }

}