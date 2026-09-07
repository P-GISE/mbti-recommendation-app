# MBTI 추천 앱

사용자의 선택 성향을 바탕으로 야구 구단, 게임 포지션, 영화 장르, 여행지를 추천하는 Android 앱입니다.

단순한 MBTI 유형 출력이 아니라 카테고리별 질문과 결과 상세 화면, 스크린샷 저장/공유 흐름을 포함한 모바일 추천 앱으로 구성했습니다.

## 주요 기능

- 야구 구단, 게임 포지션, 영화 장르, 여행지 추천 카테고리
- 카테고리별 질문 30개 중 10개를 무작위로 선택
- 두 가지 선택지 기반 성향 점수 계산
- 결과 제목, 설명, 궁합/특징, 상세 정보 표시
- 결과 화면 스크린샷 저장
- Android 공유 Intent를 통한 결과 공유
- 뒤로가기, 홈 이동, 재시작 등 기본 모바일 흐름

## 기술 스택

- Java
- Android Gradle Plugin
- AndroidX AppCompat
- Material Components
- ConstraintLayout
- GridLayout
- FileProvider

## 프로젝트 구조

```text
MBTI/
  app/src/main/java/kr/ac/mjc/mbti/
    MainActivity.java       카테고리 진입 화면
    CategoryActivity.java   카테고리 선택 화면
    QuestionActivity.java   질문 진행과 랜덤 질문 선택
    ResultActivity.java     성향 분석, 결과 표시, 공유
    DetailActivity.java     결과 상세 설명
  app/src/main/res/
    layout/                 화면 XML
    drawable/               버튼, 카드, 이미지 자원
    values/                 색상, 문자열, 테마
```

## 실행 방법

1. Android Studio에서 `MBTI/` 폴더를 엽니다.
2. Gradle Sync를 실행합니다.
3. 에뮬레이터 또는 Android 기기를 선택합니다.
4. `app` 구성을 실행합니다.

명령어로 빌드할 때:

```bash
./gradlew assembleDebug
```

Windows PowerShell에서는:

```powershell
.\gradlew.bat assembleDebug
```

## 구현 포인트

- 추천 카테고리마다 질문과 결과 매핑을 분리해 확장할 수 있게 구성했습니다.
- 질문은 전체 후보 중 일부를 무작위로 선택해 반복 사용 시 결과 경험이 달라지도록 했습니다.
- 결과 화면은 캡처 영역을 Bitmap으로 변환한 뒤 FileProvider를 통해 공유합니다.
- 화면 전환은 Activity 단위로 나누어 Android 기본 내비게이션 구조를 따릅니다.

## 개선 방향

- 질문/결과 데이터를 코드에서 JSON 또는 로컬 DB로 분리
- 결과 알고리즘 테스트 추가
- Material3 기반 UI 정리
- 다크 모드와 태블릿 레이아웃 대응

## GitHub 업로드 전 주의

- `local.properties`, `.idea`, `build`, `.gradle`은 커밋하지 않습니다.
- 이미지 자산의 출처와 사용 가능 여부를 확인한 뒤 공개 저장소에 포함합니다.
