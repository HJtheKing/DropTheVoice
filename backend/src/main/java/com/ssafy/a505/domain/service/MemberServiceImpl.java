package com.ssafy.a505.domain.service;

import com.ssafy.a505.domain.dto.request.MemberRequestDTO;
import com.ssafy.a505.domain.dto.request.PasswordRequestDTO;
import com.ssafy.a505.domain.dto.response.MailDTO;
import com.ssafy.a505.domain.dto.response.MemberResponseDTO;
import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.global.execption.CustomException;
import com.ssafy.a505.global.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberResponseDTO getMemberByMemberId(long id){
        Member member = memberRepository.findByMemberId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        return MemberResponseDTO.getMember(member);
    }

    @Override
    public MemberRequestDTO findMemberByName(String name){
        Member member = memberRepository.findByMemberName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_NAME));
        return MemberRequestDTO.getMember(member);
    }

    @Override
    public long login(MemberRequestDTO memberRequestDTO) {
        MemberRequestDTO target = findMemberByName(memberRequestDTO.getMemberName());
        if(target != null) {
            if(passwordEncoder.matches(memberRequestDTO.getMemberPassword(), target.getMemberPassword())){
                return target.getMemberId();
            }
            return -1;
        }
        return -1;
    }

    // membername 중복 불가(front에서 처리)
    @Override
    public MemberResponseDTO registerMember(MemberRequestDTO memberRequestDto) {
        String encodedPassword = passwordEncoder.encode(memberRequestDto.getMemberPassword());

        Member member = Member.builder()
                .memberEmail(memberRequestDto.getMemberEmail())
                .memberName(memberRequestDto.getMemberName())
                .memberPassword(encodedPassword)
                .profileImgUrl(memberRequestDto.getProfileImgUrl())
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.getMember(savedMember);
    }

    //회원 삭제
    @Override
    public boolean removeUser(long memberId) {
        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }

    //비밀번호 변경
    public boolean changePassword(PasswordRequestDTO passwordRequestDTO) {
        Optional<Member> findOptional = memberRepository.findByMemberName(passwordRequestDTO.getMemberName());
        Member target = findOptional.get();
        if(target != null) {
            if(passwordEncoder.matches(passwordRequestDTO.getOldPassword(), target.getMemberPassword())){
                String encodedPassword = passwordEncoder.encode(passwordRequestDTO.getNewPassword());
                target.setMemberPassword(encodedPassword);
                memberRepository.save(target);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean setUserImg(String memberId, String newImageName) {
        return false;
    }

    @Override
    public int findRemainChangeCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_ID));
        return member.getRemainChangeCount();
    }

    @Override
    public boolean isUserNameDuplicate(String userName) {
        return memberRepository.findByMemberName(userName).isPresent();
    }

    @Override
    public MemberResponseDTO getMemberByMemberEmail(MemberRequestDTO memberRequestDTO) {
        Optional<Member> findOptional = memberRepository.findByMemberEmail(memberRequestDTO.getMemberEmail());
        Member target = findOptional.get();
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .memberName(target.getMemberName())
                .build();

        return memberResponseDTO;
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    @Override
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String str = "";
        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    //임시비밀번호 메일 생성 및 비밀번호 변경
    public MailDTO createMailAndChangePassword(MemberRequestDTO memberRequestDTO){
        String str = getTempPassword();
        MailDTO mailDTO = new MailDTO();
        mailDTO.setAddress(memberRequestDTO.getMemberEmail());
        mailDTO.setTitle("DropTheVoice 임시비밀번호 안내 이메일 입니다.");
        mailDTO.setMessage("안녕하세요. DropTheVoice 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        Optional<Member> findOptional = memberRepository.findByMemberName(memberRequestDTO.getMemberName());
        Member target = findOptional.get();
        target.setMemberPassword(passwordEncoder.encode(str));
        memberRepository.save(target);
        return mailDTO;
    }

    // 메일 전송
    public void sendMail(MailDTO mailDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom(from);
        message.setReplyTo(from);
        javaMailSender.send(message);
    }

}


