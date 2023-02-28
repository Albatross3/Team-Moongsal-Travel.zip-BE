package shop.zip.travel.presentation.travelogue;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.member.exception.NotLoggedInException;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCreateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/travelogues")
public class TravelogueController {

  private static final String DEFAULT_SIZE = "5";
  private static final String DEFAULT_PAGE = "0";
  private static final String DEFAULT_SORT_FIELD = "createDate";

  private final TravelogueService travelogueService;

  public TravelogueController(TravelogueService travelogueService) {
    this.travelogueService = travelogueService;
  }

  @PostMapping
  public ResponseEntity<TravelogueCreateRes> create(
    @RequestBody @Valid TravelogueCreateReq createReq,
    @AuthenticationPrincipal UserPrincipal userPrincipal) {
    TravelogueCreateRes travelogueCreateRes =
      travelogueService.save(createReq, userPrincipal.getUserId());

    return ResponseEntity.ok(travelogueCreateRes);
  }

  @GetMapping("/{travelogueId}")
  public ResponseEntity<TravelogueDetailRes> get(
    @PathVariable Long travelogueId,
    @AuthenticationPrincipal UserPrincipal userPrincipal) {
    checkLogin(userPrincipal);
    TravelogueDetailRes travelogueDetail = travelogueService.getTravelogueDetail(travelogueId);
    return ResponseEntity.ok(travelogueDetail);
  }

  private void checkLogin(UserPrincipal userPrincipal) {
    if (userPrincipal.getUserId() == null) {
      throw new NotLoggedInException(ErrorCode.NOT_LOGGED_IN);
    }
  }

  @GetMapping
  public ResponseEntity<TravelogueCustomSlice<TravelogueSimpleRes>> getAll(
    @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size,
    @RequestParam(required = false, defaultValue = DEFAULT_PAGE) int page,
    @RequestParam(required = false, defaultValue = DEFAULT_SORT_FIELD) String sortField) {
    TravelogueCustomSlice<TravelogueSimpleRes> travelogueSimpleRes =
      travelogueService.getTravelogues(page, size, sortField);

    return ResponseEntity.ok(travelogueSimpleRes);
  }

  @GetMapping("/search")
  public ResponseEntity<List<TravelogueSimpleRes>> search(
    @RequestParam(name = "keyword", required = false) String keyword,
    @RequestParam(name = "lastTravelogue", required = false) Long lastTravelogue,
    @RequestParam(name = "orderType") String orderType, @RequestParam(name = "size") int size) {
    List<TravelogueSimpleRes> travelogueSimpleResList = travelogueService.search(lastTravelogue,
      keyword, orderType, size);

    return ResponseEntity.ok(travelogueSimpleResList);
  }

}