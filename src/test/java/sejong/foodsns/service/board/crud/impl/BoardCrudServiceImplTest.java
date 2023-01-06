package sejong.foodsns.service.board.crud.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.board.BoardRequestDto;
import sejong.foodsns.dto.board.BoardResponseDto;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest
public class BoardCrudServiceImplTest {

    @Test
    @DisplayName("게시물 등록")
    void boardCreate() {
        //given
//        BoardRequestDto boardRequestDto = ();
//
//        //when
//        ResponseEntity<Optional<MemberResponseDto>> memberCreate = memberCrudService.memberCreate(memberRequestDto);
//
//        //then
//        assertThat(memberCreate.getStatusCode()).isEqualTo(CREATED);
//        assertThat(getBody(memberCreate).getUsername()).isEqualTo(memberResponseDto.getUsername());
//        assertThat(getBody(memberCreate).getEmail()).isEqualTo(memberResponseDto.getEmail());
//        assertThat(getBody(memberCreate).getMemberType()).isEqualTo(memberResponseDto.getMemberType());
//        assertThat(getBody(memberCreate).getMemberRank()).isEqualTo(memberResponseDto.getMemberRank());
    }
}
