
package com.ssafy.a505.domain.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.service.MemberService;
import com.ssafy.a505.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-member")
@Tag(name = "MemberController")
//@CrossOrigin("*")
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

    // 로그인 요청 검증
    @Operation(summary = "로그인 요청 검증")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody MemberRequestDTO memberRequestDTO) {

        HttpStatus status = null;
        Map<String, Object> result = new HashMap<>();

        System.out.println("로그인 시도");
        long memberID = memberService.login(memberRequestDTO);
        if(memberID != -1) {
            // 토큰 만들어서 넘김
            result.put("message", SUCCESS);
            result.put("access-token", jwtUtil.createToken(memberRequestDTO.getMemberName(), memberID));
            // id 같이 보내면 덜 번거로움
            status = HttpStatus.ACCEPTED;
        }else {
            System.out.println("로그인 실패");
            result.put("message", FAIL);
            status = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(result, status);
    }

    //회원 가입
    @Operation(summary = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody Member member) {
        if(memberService.signup(member)) return new ResponseEntity<Member>(member, HttpStatus.CREATED);
        else return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
    }

    // 회원 삭제
    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable("userId") String userId) {
        if (memberService.removeUser(userId))
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/image/{userImgUrl}")
    public ResponseEntity<?> getImage(@PathVariable String userImgUrl) {
        File file = new File("image", userImgUrl);
        Resource imgResource = new FileSystemResource(file);
        Map<String, String> response = new HashMap<>();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userImgUrl + "\"")
                .body(imgResource);
    }

    // 프로필 업로드
    @PostMapping("/image")
    public ResponseEntity<?> uploadFile(@RequestPart("userData") String userData,
                                        @RequestPart(required = false) MultipartFile file) throws IllegalStateException, IOException {

        // JSON 문자열을 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.readValue(userData, HashMap.class);

        File imageFolder = new File("image/");
        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        }

        if (file != null && !file.isEmpty() && file.getSize() != 0) {
            String today = Long.toString(System.currentTimeMillis());
            String newImageName = today + "_" + file.getOriginalFilename();

            File saveFile = new File(imageFolder.getAbsolutePath(), newImageName);

            memberService.setUserImg(jsonMap.get("id"), newImageName);

            file.transferTo(saveFile);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}



