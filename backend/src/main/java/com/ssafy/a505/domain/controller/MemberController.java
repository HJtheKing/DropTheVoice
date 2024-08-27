
package com.ssafy.a505.domain.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.request.PasswordRequestDTO;
import com.ssafy.a505.domain.dto.response.MailDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.service.MemberService;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import com.ssafy.a505.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api-member")
@Tag(name = "MemberController")
public class MemberController {
    // 응답을 편하게 하기 위해 상수로 지정
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final MemberService memberService;
    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "멤버 아이디로 멤버 정보 확인")
    @GetMapping("{memberId}")
    public ResponseEntity<MemberResponseDTO> memberDetail(@PathVariable("memberId") long memberId) {
        MemberResponseDTO memberResponseDTO = memberService.getMemberByMemberId(memberId);
        if (memberResponseDTO != null) {
            return new ResponseEntity<>(memberResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "멤버 이메일로 멤버 아이디 확인")
    @PostMapping("/findMemberId")
    public ResponseEntity<MemberResponseDTO> findMemberId(@RequestBody MemberRequestDTO memberRequestDTO) {
        MemberResponseDTO memberResponseDTO = memberService.getMemberByMemberEmail(memberRequestDTO);
        if (memberResponseDTO != null) {
            return new ResponseEntity<>(memberResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "이름 중복 여부 확인")
    @PostMapping("/check-duplicate")
    public ResponseEntity<String> checkDuplicateName(@RequestBody MemberRequestDTO memberRequestDTO) {
        String memberName = memberRequestDTO.getMemberName();
        try {
            memberService.findMemberByName(memberName);
            return new ResponseEntity<>(FAIL, HttpStatus.BAD_REQUEST);
        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.INVALID_MEMBER_NAME) {
                return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
            }
            return new ResponseEntity<>(FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "로그인 요청 검증")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody MemberRequestDTO memberRequestDTO) {
        log.info("login start");

        HttpStatus status = null;
        Map<String, Object> result = new HashMap<>();

        long memberID = memberService.login(memberRequestDTO);
        if(memberID != -1) {
            result.put("message", SUCCESS);
            result.put("access-token", jwtUtil.createToken(memberRequestDTO.getMemberName(), memberID));
            status = HttpStatus.ACCEPTED;
        }else {
            log.info("로그인 실패");
            result.put("message", FAIL);
            status = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(result, status);
    }

    @Operation(summary = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<Boolean> signup(@RequestBody MemberRequestDTO memberRequestDTO) {
        try{
            memberService.registerMember(memberRequestDTO);
            return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
        }catch (CustomException e){
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
    }

    // 회원 삭제
    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable("userId") long userId) {
        if (memberService.removeUser(userId))
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
    }

    // 비밀번호 변경
    @Operation(summary = "회원 삭제")
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequestDTO passwordRequestDTO) {
        if (memberService.changePassword(passwordRequestDTO)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "임시 비밀번호 보내기")
    @PostMapping("/getNewPassword")
    public ResponseEntity<Boolean> sendPassword(@RequestBody MemberRequestDTO memberRequestDTO) {
        MemberRequestDTO target = memberService.findMemberByName(memberRequestDTO.getMemberName());
        if(target.getMemberEmail().equals(memberRequestDTO.getMemberEmail())){
            MailDTO mailDTO = memberService.createMailAndChangePassword(memberRequestDTO);
            memberService.sendMail(mailDTO);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }


    @PostMapping("/image")
    public ResponseEntity<?> uploadFile(@RequestPart("userData") String userData,
                                        @RequestPart(value = "file", required = false) MultipartFile file) throws IllegalStateException, IOException {

        if(file == null) log.info("couldnt recieve the file");
        else log.info("file recieved");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(userData, HashMap.class);

        Long id = null;
        if (jsonMap.get("id") instanceof Integer) {
            id = ((Integer) jsonMap.get("id")).longValue();
        } else if (jsonMap.get("id") instanceof Long) {
            id = (Long) jsonMap.get("id");
        }

        if (id != null && memberService.setUserImg(id, file)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


}



