package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;
import sejong.foodsns.repository.member.FriendRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberFriendService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.domain.member.MemberType.BLACKLIST;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.FRIEND_DELETE_FAILED;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.FRIEND_SEARCH_FAILED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFriendServiceImpl implements MemberFriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    /**
     * 친구 추가
     *
     * @param email       찾으려는 친구 닉네임
     * @param friendEmail
     * @return 회원 응답 Dto (본인)
     */
    @Override
    @Transactional
    public ResponseEntity<MemberFriendResponseDto> friendMemberAdd(String email, String friendUsername)
            throws IllegalArgumentException{

        Optional<Member> member = getMember(email);

        Optional<Member> friendSearch = getFriendSearch(friendUsername);

        // 추가하려는 회원이 블랙리스트가 아니라면
        if(!getMember(friendSearch).getMemberType().equals(BLACKLIST)) {

            List<Friend> friends = friendRepository.findByMemberId(getMember(member).getId());

            // 중복 검증
            friendDuplication(friends, getMember(friendSearch));

            // 친구 리스트의 크기 검증
            friendSizeCheck(friends, 5);

            // 친구 추가
            Friend friendSave = friendAdd(member, friendSearch);

            return new ResponseEntity<>(new MemberFriendResponseDto(friendSave), CREATED);
        } else
            // 추가하는 회원이 블랙리스트이면 예외 발생
            throw new IllegalStateException("블랙리스트인 회원을 친구 추가 할 수 없습니다.");
    }

    /**
     * 친구 삭제
     * @param email 회원(본인) email
     * @param index 회원의 친구 리스트 index
     * @return 회원 응답 Dto (본인)
     */
    @Override
    @Transactional
    public ResponseEntity<MemberFriendResponseDto> friendMemberDelete(String email, int index) {

        // 삭제 완료
        try {
            List<Friend> friends = friendRepository.findByMember_Email(email);
            Friend friend = friends.remove(index);
            friendRepository.delete(friend);

            return new ResponseEntity<>(new MemberFriendResponseDto(friend), OK);

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FRIEND_DELETE_FAILED);
        }
    }

    /**
     * 친구 목록
     *
     * @param email 회원(본인) email
     * @return 회원 응답 Dto List (친구 리스트)
     */
    @Override
    public ResponseEntity<List<MemberFriendResponseDto>> friendMemberList(String email) {

        List<Friend> friends = friendRepository.findByMember_Email(email);

        List<MemberFriendResponseDto> collect =
                friends.stream().map(MemberFriendResponseDto::new).collect(toList());

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * 친구 상세 조회
     * @param email 회원(본인) email
     * @param index 회원의 친구 리스트 index
     * @return 회원 응답 Dto
     */
    @Override
    public ResponseEntity<MemberResponseDto> friendMemberDetailSearch(String email, int index) {

        try {
            // 친구 리스트 조회
            List<Friend> friends = friendRepository.findByMember_Email(email);

            // 친구 리스트에서 몇 번째 index 인지 확인.
            Friend friend = friends.get(index);

            // 친구의 이름을 조회하여 상세 회원 정보 찾기.
            Optional<Member> friendInfo = memberRepository.findMemberByUsername(friend.getFriendName());

            return new ResponseEntity<>(new MemberResponseDto(getMember(friendInfo)), OK);

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FRIEND_SEARCH_FAILED);
        }
    }

    /**
     * 회원 email 로 찾기
     * @param email 찾고자하는 회원 이메일
     * @return 찾은 회원 -> 없으면 예외 (NOT_FOUND)
     */
    private Optional<Member> getMember(String email) {
        return of(memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));
    }

    /**
     * 회원의 친구 리스트에 추가할 회원 닉네임으로 찾기
     * @param friendUsername 친구 추가할 회원 닉네임
     * @return 친구 회원 -> 없으면 예외 (NOT_FOUND)
     */
    private Optional<Member> getFriendSearch(String friendUsername) {
        return of(memberRepository.findMemberByUsername(friendUsername)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));
    }

    /**
     * Optional Member peel off the wrapping
     * @param member 회원
     * @return 회원
     */
    private static Member getMember(Optional<Member> member) {
        return member.get();
    }

    /**
     * 친구 추가
     * @param member 본인
     * @param friendSearch 친구
     * @return 추가한 친구
     */
    private Friend friendAdd(Optional<Member> member, Optional<Member> friendSearch) {
        
        Friend friend = new Friend(getMember(friendSearch));
        friend.setMember(getMember(member));

        return friendRepository.save(friend);
    }

    /**
     * 친구 중복 검증
     * @param friends 친구 리스트
     * @param friend 추가할 친구
     */
    private static void friendDuplication(List<Friend> friends, Member friend) {

        long count = friends.stream().filter(f -> f.getFriendName().equals(friend.getUsername())).count();
        if(count > 0) {
            throw new IllegalArgumentException("중복된 친구 입니다.");
        }
    }

    /**
     * 친구 리스트 사이즈 검증
     * @param friends 친구 리스트
     * @param size 친구 리스트의 사이즈
     */
    private static void friendSizeCheck(List<Friend> friends, int size) {

        if(friends.size() > size) {
            throw new IllegalArgumentException("최소 5명의 친구를 추가할 수 있습니다.");
        }
    }
}
