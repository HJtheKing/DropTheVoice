package com.ssafy.a505.global;

import com.ssafy.a505.domain.entity.Member;
import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.entity.VoiceType;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;
    private final VoiceRepository voiceRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // 멤버 데이터 생성 및 저장
            Member member1 = Member.builder()
                    .memberId(1L)
                    .userEmail("user1@example.com")
                    .userName("김병관")
                    .userPassword("password1")
                    .profileImgUrl("")
                    .remainChangeCount(5)
                    .totalSpreadCount(10)
                    .totalUploadCount(15)
                    .build();
            Member member2 = Member.builder()
                    .memberId(2L)
                    .userEmail("user2@example.com")
                    .userName("User2")
                    .userPassword("password2")
                    .profileImgUrl("")
                    .remainChangeCount(3)
                    .totalSpreadCount(8)
                    .totalUploadCount(12)
                    .build();
            memberRepository.save(member1);
            memberRepository.save(member2);

            // 보이스 데이터 생성 및 저장
            Voice voice1 = Voice.builder()
                    .voiceId(1L)
                    .member(member1)
                    .listenCount(200L)
                    .title("프로님들 10층 욕심 이유 찾았다")
                    .imageUrl("https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png")
                    .originalName("voice1.mp3")
                    .savePath("/voices/voice1.mp3")
                    .saveFolder("/voices/")
                    .latitude(37.5665)
                    .longitude(126.9780)
                    .dateTime(LocalDateTime.now())
                    .voiceType(VoiceType.Processed)
                    .build();
            Voice voice2 = Voice.builder()
                    .voiceId(2L)
                    .member(member2)
                    .listenCount(300L)
                    .title("재용이햄 애플 런칭행사 줄서있는거 봄")
                    .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXobLcA4-7fiJkSmqBaQnd2QL8gY8EFIx4pQ&s")
                    .originalName("voice2.mp3")
                    .savePath("/voices/voice2.mp3")
                    .saveFolder("/voices/")
                    .latitude(37.7749)
                    .longitude(-122.4194)
                    .dateTime(LocalDateTime.now())
                    .voiceType(VoiceType.NormalVoice)
                    .build();


            voiceRepository.save(voice1);
            voiceRepository.save(voice2);
        };
    }
}
