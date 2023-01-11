package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Friend;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.FriendRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.business.MemberFriendService;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.FRIEND_DELETE_FAILED;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.FRIEND_SEARCH_FAILED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFriendServiceImpl implements MemberFriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseEntity<List<MemberResponseDto>> friendMemberAdd(MemberRequestDto memberRequestDto, String username) {
        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.getEmail());
        Optional<Member> friendSearch = memberRepository.findMemberByUsername(username);
        Friend friend = new Friend(getMember(friendSearch));

        Friend friendSave = friendRepository.save(friend);

        getMember(member).setFriends(friendSave);
        List<Friend> friends = getMember(member).getFriends();

        List<MemberResponseDto> collect =
                friends.stream().map(friend1 -> new MemberResponseDto()).collect(toList());

        return new ResponseEntity<>(collect, CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<List<MemberResponseDto>> friendMemberDelete(MemberRequestDto memberRequestDto, int index) {
        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.getEmail());

        // 삭제 완료
        try {
            getMember(member).getFriends().remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FRIEND_DELETE_FAILED);
        }

        List<Friend> friends = getMember(member).getFriends();
        List<MemberResponseDto> collect =
                friends.stream().map(friend1 -> new MemberResponseDto()).collect(toList());

        return new ResponseEntity<>(collect, OK);
    }

    @Override
    public ResponseEntity<List<MemberResponseDto>> friendMemberList(MemberRequestDto memberRequestDto) {

        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.getEmail());

        List<Friend> friends = getMember(member).getFriends();

        List<MemberResponseDto> collect =
                friends.stream().map(friend1 -> new MemberResponseDto()).collect(toList());

        return new ResponseEntity<>(collect, OK);
    }

    @Override
    public ResponseEntity<MemberResponseDto> friendMemberDetailSearch(MemberRequestDto memberRequestDto, int index) {

        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.getEmail());
        try {
            Friend friend = getMember(member).getFriends().get(index);

            MemberResponseDto memberResponseDto = getMemberResponseDto(friend);

            return new ResponseEntity<>(memberResponseDto, OK);

        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(FRIEND_SEARCH_FAILED);
        }
    }

    private MemberResponseDto getMemberResponseDto(Friend friend) {
        return MemberResponseDto.builder()
                .member(friend.getMember())
                .build();
    }

    private Member getMember(Optional<Member> member) {
        return member.get();
    }
}
