package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.service.member.business.MemberBlackListService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberBlackListServiceImpl implements MemberBlackListService {

    @Override
    public ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(MemberBlackListCreateRequestDto memberBlackListCreateRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList() {
        return null;
    }
}
