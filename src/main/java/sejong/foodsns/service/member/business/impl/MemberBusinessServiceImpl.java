package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.repository.member.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberBusinessServiceImpl {

    private final MemberRepository memberRepository;


}
