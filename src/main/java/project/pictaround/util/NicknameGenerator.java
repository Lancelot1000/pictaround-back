package project.pictaround.util;

import java.util.Random;

enum Adjective {
    까칠한, 열정적인, 유쾌한, 꼼꼼한, 도전적인, 감성적인, 자유로운, 개성있는, 탐구적인, 다정한, 독특한, 섬세한, 모험적인, 세련된, 신비로운, 낭만적인, 활기찬, 솔직한, 차분한, 창의적인
}

enum Noun {
    미식가, 탐험가, 여행자, 예술가, 산책러, 모험가, 감상가, 맛집러, 전시러, 나들이꾼, 사진가, 기록가, 탐구자, 일상러, 경험자, 도전자, 발견가, 감각러, 힐링러, 순간러
}

public abstract class NicknameGenerator {

    // 닉네임 생성 함수
    public static String generateNickname() {
        Random random = new Random();

        // 랜덤 형용사 선택
        Adjective randomAdjective = Adjective.values()[random.nextInt(Adjective.values().length)];
        // 랜덤 명사 선택
        Noun randomNoun = Noun.values()[random.nextInt(Noun.values().length)];

        // 형용사 + 명사 조합
        return randomAdjective + " " + randomNoun;
    }


}
