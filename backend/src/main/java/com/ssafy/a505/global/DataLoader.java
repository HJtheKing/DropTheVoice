package com.ssafy.a505.global;

import com.ssafy.a505.domain.entity.*;
import com.ssafy.a505.domain.repository.HeartRepository;
import com.ssafy.a505.domain.repository.MemberRepository;
import com.ssafy.a505.domain.repository.SpreadRepository;
import com.ssafy.a505.domain.repository.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;
    private final VoiceRepository voiceRepository;

    @Bean
    public CommandLineRunner loadData(HeartRepository heartRepository, SpreadRepository spreadRepository) {
        return args -> {
            // 멤버 데이터 생성 및 저장
            Member member1 = Member.builder()
                    .memberId(1L)
                    .memberEmail("admin@example.com")
                    .memberName("admin")
                    .memberPassword("$2a$10$w.68Nb5lODdeU9D3H7bwpe5Q25uS7sJTFrdXK9KplB6oNtpBq56zq")
                    .profileImgUrl("")
                    .remainChangeCount(5)
                    .totalSpreadCount(10)
                    .totalUploadCount(15)
                    .build();
            Member member2 = Member.builder()
                    .memberId(2L)
                    .memberEmail("user2@example.com")
                    .memberName("User2")
                    .memberPassword("password2")
                    .profileImgUrl("")
                    .remainChangeCount(3)
                    .totalSpreadCount(8)
                    .totalUploadCount(12)
                    .build();
            memberRepository.save(member1);
            memberRepository.save(member2);

            List<String> titles = List.of(
                    "오늘 힘든일이 있었어요", "오늘 꿀잼썰", "여러분 이거 아세요?", "오늘 기분 최고!",
                    "속상한 일이 있었어요", "재밌는 이야기 해드릴게요", "오늘 먹은 맛있는 음식",
                    "산책 중 만난 강아지", "이상한 경험담", "행복했던 순간", "감동적인 이야기",
                    "오늘의 운세", "오늘 만난 사람", "기쁜 소식", "슬픈 소식",
                    "우울할 때 들으면 좋은 노래", "스트레스 해소법", "오늘의 명언", "새로운 취미",
                    "여행 가고 싶어요", "맛집 추천", "영화 감상 후기", "책 추천", "오늘의 운동",
                    "재미있는 퀴즈", "유익한 정보", "고민 상담", "오늘의 목표", "작은 성취", "뜻밖의 발견"
            );

            Long k = 1L;
            Random random = new Random();
            for (int i = 0; i < 30; i++) {
                long heartCount = random.nextInt(300) + 1;
                long listenCount = random.nextInt(300) + 1;
                Voice voice = Voice.builder()
                        .voiceId(k)
                        .member(member1)
                        .title(titles.get(i % titles.size()))
                        .imageUrl("https://picsum.photos/id/" + random.nextInt(300) + "/200/300")
                        .originalName("original" + i + ".mp3")
                        .savePath("http://example.com/audio" + i + ".mp3")
                        .saveFolder("folder" + i)
                        .latitude(37.5 + random.nextDouble() * 0.1)
                        .longitude(127.0 + random.nextDouble() * 0.1)
                        .heartCount(heartCount)
                        .listenCount(listenCount)
                        .voiceType(VoiceType.virus)
                        .build();
                ProcessedVoice processedVoice = ProcessedVoice.builder()
                        .processedPath("http://example.com/processed" + i + ".mp3")
                        .voice(voice)
                        .build();
                k++;
                voiceRepository.save(voice);
            }
            // 보이스 데이터 생성 및 저장
            Voice voice1 = Voice.builder()
                    .voiceId(1L)
                    .member(member1)
                    .listenCount(200L)
                    .title("1111.프로님들 10층 욕심 이유 찾았다")
                    .imageUrl("https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png")
                    .originalName("voice1.mp3")
                    .savePath("/voices/voice1.mp3")
                    .saveFolder("/voices/")
                    .latitude(37.5665)
                    .longitude(126.9780)
                    .dateTime(LocalDateTime.now())
                    .voiceType(VoiceType.virus)
                    .build();
            Voice voice2 = Voice.builder()
                    .voiceId(2L)
                    .member(member2)
                    .listenCount(300L)
                    .title("2222.재용이햄 애플 런칭행사 줄서있는거 봄")
                    .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXobLcA4-7fiJkSmqBaQnd2QL8gY8EFIx4pQ&s")
                    .originalName("voice2.mp3")
                    .savePath("/voices/voice2.mp3")
                    .saveFolder("/voices/")
                    .latitude(37.7749)
                    .longitude(122.4194)
                    .dateTime(LocalDateTime.now())
                    .voiceType(VoiceType.pokemon)
                    .build();
            Voice voice3 = Voice.builder()
                    .voiceId(3L)
                    .member(member1)
                    .listenCount(200L)
                    .title("3333.프로님들 10층 욕심 이유 찾았다")
                    .imageUrl("https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png")
                    .originalName("voice1.mp3")
                    .savePath("/voices/voice1.mp3")
                    .saveFolder("/voices/")
                    .latitude(37.5665)
                    .longitude(126.9780)
                    .dateTime(LocalDateTime.now())
                    .voiceType(VoiceType.virus)
                    .build();
            Voice voice4 = Voice.builder()
                    .voiceId(4L)
                    .member(member2)
                    .listenCount(300L)
                    .title("4444.재용이햄 애플 런칭행사 줄서있는거 봄")
                    .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXobLcA4-7fiJkSmqBaQnd2QL8gY8EFIx4pQ&s")
                    .originalName("voice2.mp3")
                    .savePath("/voices/voice2.mp3")
                    .saveFolder("/voices/")
                    .latitude(37.7749)
                    .longitude(122.4194)
                    .dateTime(LocalDateTime.now())
                    .voiceType(VoiceType.pokemon)
                    .build();


            voiceRepository.save(voice1);
            voiceRepository.save(voice2);

            Long count = 0L;
            for (int i = 0; i < 40; i++) {
                Voice voice = new Voice();
                voice.setMember(member1);
                voice.setHeartCount(count++);
                voice.setListenCount(Math.round(Math.random() * 100));
                voiceRepository.save(voice3);
                voiceRepository.save(voice4);
            }

                for (int i = 0; i < 30; i++) {
                    Voice voice = new Voice();
                    voice.setMember(member1);
                    voice.setHeartCount(3L);
                    voice.setListenCount(Math.round(Math.random() * 100000));
                    voice.setLatitude(127.0 + random.nextDouble() * 0.1);
                    voice.setLongitude(37.5 + random.nextDouble() * 0.1);
                    voice.setTitle("두부를 왜 좋아함" + i);
                    voice.setVoiceType(VoiceType.virus);
                    voice.setDateTime(LocalDateTime.now());
                    voice.setSavePath("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");
                    voice.setImageUrl("https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true");
                    voiceRepository.save(voice);
                }

                for (int i = 1; i <= 43; i++) {
                    Voice voice = new Voice();
                    voice.setMember(member1);
                    voice.setHeartCount(Math.round(Math.random() * 1));
                    voice.setTitle("spread" + i);
                    voiceRepository.save(voice);
                }
                for (int i = 1; i <= 43; i++) {
                    Voice voice = new Voice();
                    voice.setMember(member1);
                    voice.setHeartCount(Math.round(Math.random() * 10));
                    voice.setListenCount(Math.round(Math.random() * 1000));
                    voice.setTitle("like" + i);
                    voiceRepository.save(voice);
                }

                Heart heart1 = new Heart();
                heart1.setMember(member1);
                heart1.setVoice(voice1);
                heartRepository.save(heart1);

                Heart heart2 = new Heart();
                heart2.setMember(member1);
                heart2.setVoice(voice2);
                heartRepository.save(heart2);

                Spread spread1 = new Spread();
                spread1.setMember(member1);
                spread1.setVoice(voice3);
                spreadRepository.save(spread1);

                Spread spread2 = new Spread();
                spread2.setMember(member1);
                spread2.setVoice(voice4);
                spreadRepository.save(spread2);



        };
    }
}

