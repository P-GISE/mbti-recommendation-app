# MBTI 추천 앱

사용자의 선택 성향을 바탕으로 야구 구단, 게임 포지션, 영화 장르, 여행지를 추천하는 Android 앱입니다.

단순한 MBTI 유형 출력이 아니라 카테고리별 질문과 결과 상세 화면, 스크린샷 저장/공유 흐름을 포함한 모바일 추천 앱으로 구성했습니다.

## 한눈에 보기

| 항목 | 내용 |
| --- | --- |
| 한국어 프로젝트명 | MBTI 추천 앱 |
| 저장소 성격 | Java 기반 Android 네이티브 앱 |
| 주요 사용자 | 간단한 성향 테스트와 추천 결과를 보고 싶은 모바일 사용자 |
| 실행 형태 | Android Studio 또는 Gradle debug APK 빌드 |
| 데이터 저장 | 별도 서버 없이 앱 내부 질문/결과 데이터 사용 |
| 공유 기능 | 결과 화면 캡처 후 Android 공유 Intent 사용 |

## 앱 사용 흐름

1. 메인 화면에서 테스트 시작 버튼을 누릅니다.
2. 야구 구단, 게임 포지션, 영화 장르, 여행지 중 하나의 카테고리를 선택합니다.
3. 앱이 해당 카테고리의 질문 후보 중 10개를 무작위로 보여줍니다.
4. 사용자는 두 선택지 중 하나를 고르며 성향 점수를 쌓습니다.
5. 결과 화면에서 추천 항목, 설명, 특징, 궁합 정보를 확인합니다.
6. 상세 보기로 더 긴 설명을 보거나, 결과 화면을 이미지로 저장/공유합니다.

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

## 화면 구성

| Activity | 역할 |
| --- | --- |
| `MainActivity` | 앱 첫 화면과 시작 진입점 |
| `CategoryActivity` | 추천 카테고리 선택 |
| `QuestionActivity` | 질문 표시, 답변 수집, 진행률 표시 |
| `ResultActivity` | 추천 결과 계산, 요약 표시, 캡처/공유 |
| `DetailActivity` | 결과별 상세 설명 표시 |

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

빌드 결과는 일반적으로 `app/build/outputs/apk/debug/` 아래에 생성됩니다.

## 권한과 공유 방식

- `INTERNET`: 공유/외부 기능 확장을 고려한 기본 권한입니다.
- `WRITE_EXTERNAL_STORAGE`: Android 12 이하에서 결과 스크린샷 저장에 사용합니다.
- `READ_MEDIA_IMAGES`: Android 13 이상 이미지 접근 정책에 대응합니다.
- `FileProvider`: 캡처한 결과 이미지를 다른 앱에 안전하게 전달하기 위해 사용합니다.

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

## 검증 방법

```bash
./gradlew test
./gradlew assembleDebug
```

Windows PowerShell:

```powershell
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

## GitHub 업로드 전 주의

- `local.properties`, `.idea`, `build`, `.gradle`은 커밋하지 않습니다.
- 이미지 자산의 출처와 사용 가능 여부를 확인한 뒤 공개 저장소에 포함합니다.
