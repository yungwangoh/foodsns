package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListDetailDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberBlackListService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberBlackListServiceImpl implements MemberBlackListService {

    private final BlackListRepository blackListRepository;
    private final MemberRepository memberRepository;

    /**
     * 블랙리스트 회원 등록
     * @param reason - 블랙리스트인 사유
     * @param email - 블랙리스트 회원 이메일
     * @return 블랙리스트 사유
     */
    @Override
    @Transactional
    public ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(String reason, String email) {
        Optional<Member> findMember = memberRepository.findMemberByEmail(email);

        if(findMember.isPresent()) {
            Member member = getMember(findMember);
            BlackList blackList = blackListMemberCheck(reason, member);

            return new ResponseEntity<>(new MemberBlackListResponseDto(blackList), CREATED);
        } else {
            throw new IllegalStateException("회원이 존재 하지 않습니다.");
        }
    }

    /**
     * 블랙리스트 회원 조회
     * @param id 블랙리스트 회원 id
     * @return 블랙리스트 사유
     */
    @Override
    public ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(Long id) {

        Optional<BlackList> blackList = of(blackListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("블랙리스트 회원을 찾을 수 없습니다.")));

        return new ResponseEntity<>(new MemberBlackListResponseDto(blackList.get()), OK);
    }

    /**
     * 블랙리스트 회원 상세 정보 조회
     * @param id 회원 id
     * @return 블랙리스트 회원 사유와 상세정보
     */
    @Override
    public ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch(Long id) {
        Optional<BlackList> blackList = of(blackListRepository.findBlackListById(id)
                .orElseThrow(() -> new IllegalArgumentException("블랙리스트 회원을 찾을 수 없습니다.")));

        return new ResponseEntity<>(new MemberBlackListDetailDto(blackList.get()), OK);
    }

    /**
     * 블랙리스트 회원 리스트
     * @return 블랙리스트 회원 사유 리스트
     */
    @Override
    public ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList() {
        List<BlackList> blackLists = blackListRepository.findAll();

        List<MemberBlackListResponseDto> collect = blackLists.stream().map(MemberBlackListResponseDto::new).toList();

        return new ResponseEntity<>(collect, OK);
    }

    private BlackList blackListMemberCheck(String reason, Member member) {
        if (member.getMemberType().equals(BLACKLIST)) {

            return blackListRepository.save(new BlackList(reason, member));
        } else {
            throw new IllegalStateException("블랙리스트 회원이 아닙니다.");
        }
    }

    private static Member getMember(Optional<Member> member) {
        return member.get();
    }
}
