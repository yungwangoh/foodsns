package sejong.foodsns.controller.member.blacklist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.service.member.business.MemberBlackListService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class MemberBlackListController {

    private final MemberBlackListService memberBlackListService;

    @PostMapping("/member/blackList")
    ResponseEntity<MemberBlackListResponseDto> memberBlackListCreate(@RequestBody @Valid MemberBlackListCreateRequestDto memberBlackListCreateRequestDto) {
        ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberCreate =
                memberBlackListService.blackListMemberCreate(memberBlackListCreateRequestDto);

        return new ResponseEntity<>(getBody(blackListMemberCreate).get(), blackListMemberCreate.getStatusCode());
    }

    @GetMapping("/member/blackLists")
    ResponseEntity<List<MemberBlackListResponseDto>> memberBlackLists() {

        ResponseEntity<Optional<List<MemberBlackListResponseDto>>> blackListMemberList =
                memberBlackListService.blackListMemberList();

        return new ResponseEntity<>(getBlackListMemberListBody(blackListMemberList).get(), blackListMemberList.getStatusCode());
    }

    private static Optional<List<MemberBlackListResponseDto>> getBlackListMemberListBody(ResponseEntity<Optional<List<MemberBlackListResponseDto>>> blackListMemberList) {
        return blackListMemberList.getBody();
    }

    private static Optional<MemberBlackListResponseDto> getBody(ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberCreate) {
        return blackListMemberCreate.getBody();
    }
}
