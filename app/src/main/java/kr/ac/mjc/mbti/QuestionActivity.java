package kr.ac.mjc.mbti;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    // UI 컴포넌트들
    private TextView tvCategory, tvQuestionNumber, tvQuestion;
    private ProgressBar progressBar;
    private Button btnAnswer1, btnAnswer2;
    private TextView btnBack;
    private LinearLayout questionContainer;

    // 데이터 변수들
    private String selectedCategory;
    private int currentQuestionIndex = 0;
    private final int TOTAL_QUESTIONS = 30;  // 전체 질문 수
    private final int QUESTIONS_TO_SHOW = 10; // 실제 보여줄 질문 수
    private ArrayList<String> userAnswers = new ArrayList<>();
    private List<String> allQuestions;  // 전체 질문 리스트
    private List<String[]> allAnswerOptions;  // 전체 답변 옵션 리스트
    private ArrayList<Integer> selectedQuestionIndices; // 랜덤으로 선택된 질문 인덱스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                            systemBars.bottom);
                    return insets;
                });

        // 전달받은 카테고리 정보
        selectedCategory = getIntent().getStringExtra("category");
        if (selectedCategory == null) {
            selectedCategory = "기본";
        }

        // 뷰 초기화
        initViews();

        // 질문 데이터 초기화
        initQuestionData();

        // 랜덤하게 10개 질문 선택
        selectRandomQuestions();

        // 클릭 리스너 설정
        setupClickListeners();

        // 첫 번째 질문 표시
        displayQuestion();

        // 애니메이션 효과
        setupAnimations();
    }

    /**
     * 뷰 초기화
     */
    private void initViews() {
        tvCategory = findViewById(R.id.tv_category);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestion = findViewById(R.id.tv_question);
        progressBar = findViewById(R.id.progress_bar);
        btnAnswer1 = findViewById(R.id.btn_answer1);
        btnAnswer2 = findViewById(R.id.btn_answer2);
        btnBack = findViewById(R.id.btn_back);
        questionContainer = findViewById(R.id.question_container);

        // 카테고리 표시
        tvCategory.setText(selectedCategory + " 추천");

        // 프로그레스바 최대값 설정
        progressBar.setMax(QUESTIONS_TO_SHOW);
    }

    /**
     * 랜덤하게 10개 질문 선택
     */
    private void selectRandomQuestions() {
        selectedQuestionIndices = new ArrayList<>();
        List<Integer> allIndices = new ArrayList<>();

        // 0부터 29까지의 인덱스 생성
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            allIndices.add(i);
        }

        // 섞기
        Collections.shuffle(allIndices);

        // 앞에서 10개만 선택
        for (int i = 0; i < QUESTIONS_TO_SHOW; i++) {
            selectedQuestionIndices.add(allIndices.get(i));
        }
    }

    /**
     * 질문 데이터 초기화 (카테고리별)
     */
    private void initQuestionData() {
        switch (selectedCategory) {
            case "야구구단":
                initBaseballQuestions();
                break;
            case "게임포지션":
                initGameQuestions();
                break;
            case "영화장르":
                initMovieQuestions();
                break;
            case "여행지":
                initTravelQuestions();
                break;
            default:
                initDefaultQuestions();
                break;
        }
    }

    /**
     * 야구구단 질문 초기화 (30개 질문)
     */
    private void initBaseballQuestions() {
        allQuestions = Arrays.asList(
                // 1-10
                "야구장에서의 나의 모습",
                "내가 응원하는 팀, 어떤 스타일이 좋을까",
                "팀의 매력 포인트",
                "어떤 선수가 끌리나요",
                "팀 리더는 어떤 이미지가 좋나요",
                "팀이 지고 있을때 어떻게 반응하나요",
                "어떤 경기 스타일을 선호하나요",
                "응원할 팀의 강점이라면",
                "경기 관람 중 가장 집중하는 부분은?",
                "야구에 대한 나의 관심사는?",
                // 11-20
                "응원할 때 소리 지르고 응원가를 부르는 걸 좋아하시나요?",
                "조용히 앉아서 경기를 관람하는 게 더 좋나요?",
                "팀의 역사와 전통을 중요하게 생각하나요?",
                "실력 중심으로 냉정하게 판단하는 편인가요?",
                "응원도구나 복장을 챙겨 입는 편인가요?",
                "경기를 보기 위해 미리 자리와 경로를 확인하나요?",
                "당일 기분에 따라 팀 응원 여부가 바뀌기도 하나요?",
                "팀의 최근 성적과 통계를 참고해서 경기를 보는 편인가요?",
                "불리한 상황에서도 팀을 끝까지 믿고 응원하나요?",
                "응원할 팀을 고를 때 특별한 이유가 있어야 하나요?",
                // 21-30
                "경기장에서 먹는 치맥이 야구의 필수라고 생각하나요?",
                "선수 개인의 기록보다 팀 성적이 더 중요하다고 생각하나요?",
                "야구 중계를 볼 때 해설위원의 분석을 집중해서 듣나요?",
                "응원가를 외우고 다니는 편인가요?",
                "라이벌 팀과의 경기가 더 재미있다고 생각하나요?",
                "신인 드래프트나 트레이드 소식에 관심이 많나요?",
                "경기 결과보다 선수들의 플레이 과정이 더 중요하다고 생각하나요?",
                "야구장 가기 전에 선발 라인업을 확인하나요?",
                "홈런보다 정교한 수비 플레이가 더 멋있다고 생각하나요?",
                "시즌권을 구매해서 정기적으로 경기를 보고 싶나요?"
        );

        allAnswerOptions = Arrays.asList(
                // 1-10
                new String[]{"조용히 관전하며 분위기 즐김", "응원단과 함께 함성"},
                new String[]{"데이터 기반의 전략 팀", "분위기 타며 감동을 주는 팀"},
                new String[]{"전통 및 꾸준함", "계속 새로운 변화 및 도전"},
                new String[]{"수비 잘하고 팀워크 좋은 선수", "슈퍼스타의 등장"},
                new String[]{"책임감 있는 우직한 주장", "유쾌한 리더"},
                new String[]{"침착하게 상황 분석", "감정이입하고 탈주"},
                new String[]{"투수전", "타자전"},
                new String[]{"유망주 성장", "스타플레이어"},
                new String[]{"전략과 데이터 분석", "선수들의 감정과 드라마"},
                new String[]{"장기적인 팀 발전", "지금 당장의 성과"},
                // 11-20
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                // 21-30
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"}
        );
    }

    /**
     * 게임포지션 질문 초기화 (30개 질문)
     */
    private void initGameQuestions() {
        allQuestions = Arrays.asList(
                // 1-10
                "혼자서 게임하는 걸 더 좋아하시나요?",
                "여러 사람과 팀플레이하는 걸 좋아하시나요?",
                "게임할 때 팀원을 도와주는 역할이 더 편한가요?",
                "빠르게 진행되는 전투나 반응 싸움을 좋아하시나요?",
                "게임 시작 전에 전략이나 아이템 경로를 미리 계획하나요?",
                "상황에 따라 즉흥적으로 전략을 바꾸는 걸 잘하시나요?",
                "정확한 계산과 수치를 바탕으로 플레이하는 걸 선호하시나요?",
                "새로운 챔피언이나 전술을 실험해보는 걸 즐기시나요?",
                "같은 챔피언을 반복해서 연습하는 걸 좋아하시나요?",
                "혼자 돌아다니며 전체 게임 흐름을 조율하는 게 재미있다고 느끼시나요?",
                // 11-20
                "게임에서 눈에 띄는 활약보다 팀을 위한 조율을 더 중요하게 생각하시나요?",
                "게임 중 예상하지 못한 상황이 생기면 유연하게 대처할 수 있나요?",
                "팀에서 가장 중요한 역할을 맡는 것이 부담스럽지 않나요?",
                "게임에서 지더라도 재미있으면 괜찮다고 생각하나요?",
                "랭크 게임보다 일반 게임을 더 선호하나요?",
                "프로 경기를 보면서 전략을 분석하나요?",
                "음성 채팅으로 팀원과 소통하는 걸 좋아하나요?",
                "메타에 맞춰 플레이하는 걸 중요하게 생각하나요?",
                "게임 내 스토리나 캐릭터 배경에 관심이 있나요?",
                "CS(미니언 처치)를 정확하게 먹는 것에 집중하나요?",
                // 21-30
                "팀 전투보다 1대1 상황을 더 선호하나요?",
                "게임 패치노트를 꼼꼼히 읽어보나요?",
                "팀원이 실수해도 격려하는 편인가요?",
                "오브젝트(용, 바론 등) 타이밍을 중요하게 생각하나요?",
                "챔피언 스킨이나 이펙트에 관심이 많나요?",
                "게임 시작 전 워밍업을 하나요?",
                "팀원의 플레이를 분석하고 피드백을 주나요?",
                "게임 내 이벤트나 미션을 꼭 완료하나요?",
                "실력보다 팀워크가 더 중요하다고 생각하나요?",
                "새로운 게임 모드가 나오면 바로 시도해보나요?"
        );

        allAnswerOptions = Arrays.asList(
                // 1-10
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                // 11-20
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                // 21-30
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"}
        );
    }

    /**
     * 영화장르 질문 초기화 (30개 질문)
     */
    private void initMovieQuestions() {
        allQuestions = Arrays.asList(
                // 1-10
                "영화를 선택할 때 가장 중요한 기준은?",
                "영화 관람 후 어떤 감정을 선호하나요?",
                "영화 속 주인공은 어떤 타입이 좋나요?",
                "영화의 스토리텔링 방식은?",
                "영화를 보는 목적은 무엇인가요?",
                "영화 속 갈등 상황은 어떤 것을 선호하나요?",
                "영화의 결말은 어떤 스타일을 좋아하나요?",
                "영화 관람 환경은?",
                "영화 추천을 받을 때 기준은?",
                "영화에 대한 평가 기준은?",
                // 11-20
                "혼자 영화 보는 것을 선호하나요?",
                "영화관보다 집에서 보는 게 더 편하나요?",
                "해피엔딩을 선호하나요?",
                "열린 결말이나 해석이 필요한 영화를 좋아하나요?",
                "영화 리뷰나 평점을 미리 확인하나요?",
                "개봉일에 맞춰 영화를 보러 가나요?",
                "실화 바탕 영화를 선호하나요?",
                "CG나 특수효과가 화려한 영화를 좋아하나요?",
                "영화를 볼 때 팝콘은 필수라고 생각하나요?",
                "독립영화나 예술영화도 즐겨 보나요?",
                // 21-30
                "시리즈물을 정주행하는 걸 좋아하나요?",
                "영화 OST를 따로 찾아 듣나요?",
                "스포일러를 미리 찾아보는 편인가요?",
                "자막보다 더빙을 선호하나요?",
                "영화제나 시사회에 관심이 있나요?",
                "감독이나 배우를 보고 영화를 선택하나요?",
                "영화를 본 후 감상을 SNS에 공유하나요?",
                "고전 명작을 찾아보는 편인가요?",
                "4DX나 IMAX 같은 특별관을 선호하나요?",
                "영화의 메시지나 주제를 중요하게 생각하나요?"
        );

        allAnswerOptions = Arrays.asList(
                // 1-10
                new String[]{"흥미진진한 스토리", "깊이 있는 메시지"},
                new String[]{"짜릿함과 흥분", "감동과 여운"},
                new String[]{"활동적이고 역동적인 캐릭터", "내면적이고 복합적인 캐릭터"},
                new String[]{"빠른 전개와 액션", "차근차근 풀어가는 서사"},
                new String[]{"스트레스 해소와 재미", "생각할 거리와 깨달음"},
                new String[]{"외적 갈등과 액션", "내적 갈등과 심리묘사"},
                new String[]{"시원한 해결과 해피엔딩", "여운 있는 열린 결말"},
                new String[]{"영화관에서 큰 화면으로", "집에서 편안하게"},
                new String[]{"장르와 액션", "감독과 배우"},
                new String[]{"재미있었는지 지루했는지", "의미있었는지 깊이가 있었는지"},
                // 11-20
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                // 21-30
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"}
        );
    }

    /**
     * 여행지 질문 초기화 (30개 질문)
     */
    private void initTravelQuestions() {
        allQuestions = Arrays.asList(
                // 1-10
                "혼자 조용한 장소에서 시간을 보내는 걸 좋아하시나요?",
                "사람 많은 곳에서 에너지를 얻는 편인가요?",
                "즉흥적으로 여행을 떠난 적이 있나요?",
                "여행 계획을 세울 때 루트와 시간을 자세히 짜는 편인가요?",
                "풍경이나 전통 문화가 느껴지는 장소를 좋아하시나요?",
                "쇼핑이나 액티비티를 중심으로 여행 일정을 구성하시나요?",
                "사진 찍는 걸 중요하게 생각하시나요?",
                "여행 중 새로운 길을 찾아 탐험하는 걸 좋아하시나요?",
                "숙소, 음식, 교통 정보를 미리 조사해두는 편인가요?",
                "유명한 관광지보다 덜 알려진 곳에 끌리시나요?",
                // 11-20
                "현지 문화를 직접 체험하는 걸 중요하게 생각하시나요?",
                "일정에 여유를 두기보단 빡빡하게 계획을 세우는 편인가요?",
                "자연보다는 도시적인 분위기를 더 좋아하시나요?",
                "여행 예산을 미리 정하고 지키는 편인가요?",
                "현지인들과 대화하는 것을 즐기나요?",
                "일출이나 일몰을 보기 위해 일찍 일어날 수 있나요?",
                "패키지 여행보다 자유 여행을 선호하나요?",
                "여행지의 역사나 배경을 미리 공부하나요?",
                "호텔보다 에어비앤비나 게스트하우스를 선호하나요?",
                "여행 중 운동이나 액티비티를 꼭 포함시키나요?",
                // 21-30
                "맛집보다는 편의점이나 길거리 음식도 괜찮다고 생각하나요?",
                "여행 일기나 기록을 남기는 편인가요?",
                "대중교통보다 렌터카를 선호하나요?",
                "여행지에서도 일찍 자고 일찍 일어나나요?",
                "기념품을 사는 것을 중요하게 생각하나요?",
                "날씨가 안 좋아도 계획대로 일정을 진행하나요?",
                "혼자 여행하는 것도 좋다고 생각하나요?",
                "여행 전 리뷰를 꼼꼼히 확인하나요?",
                "휴양지보다 도시 관광을 선호하나요?",
                "같은 곳을 다시 여행하는 것도 좋다고 생각하나요?"
        );

        allAnswerOptions = Arrays.asList(
                // 1-30 모두 예/아니오
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"},
                new String[]{"예", "아니오"}
        );
    }

    /**
     * 기본 질문 초기화 (카테고리가 매칭되지 않을 때)
     */
    private void initDefaultQuestions() {
        allQuestions = Arrays.asList(
                "평소 결정을 내릴 때 어떤 방식을 선호하나요?",
                "새로운 환경에 적응하는 스타일은?",
                "스트레스를 받을 때 해소 방법은?",
                "목표를 설정할 때 어떤 방식인가요?",
                "사람들과의 관계에서 중요하게 생각하는 것은?",
                "문제 해결 접근 방식은?",
                "변화에 대한 태도는?",
                "성취감을 느끼는 순간은?",
                "의견 충돌이 있을 때 대처법은?",
                "미래에 대한 계획은?"
        );

        allAnswerOptions = Arrays.asList(
                new String[]{"직감적으로 빠르게 결정", "충분히 고민한 후 결정"},
                new String[]{"적극적으로 먼저 다가간다", "상황을 관찰한 후 적응한다"},
                new String[]{"활동적인 방법으로 해소", "조용한 방법으로 해소"},
                new String[]{"도전적이고 높은 목표", "현실적이고 달성 가능한 목표"},
                new String[]{"많은 사람과 넓은 관계", "소수와 깊은 관계"},
                new String[]{"즉시 행동으로 해결", "신중하게 분석 후 해결"},
                new String[]{"변화를 즐기고 받아들인다", "변화보다 안정을 선호한다"},
                new String[]{"새로운 도전을 성공했을 때", "꾸준한 노력이 결실을 맺을 때"},
                new String[]{"적극적으로 의견을 개진", "조화를 위해 양보하는 편"},
                new String[]{"큰 그림을 그리며 계획", "단계별로 차근차근 계획"}
        );

        // 기본 카테고리는 30개가 없으므로 10개만 사용
        selectedQuestionIndices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            selectedQuestionIndices.add(i);
        }
    }

    /**
     * 클릭 리스너 설정
     */
    private void setupClickListeners() {
        // 답변 버튼 1
        btnAnswer1.setOnClickListener(v -> {
            selectAnswer(0);
        });

        // 답변 버튼 2
        btnAnswer2.setOnClickListener(v -> {
            selectAnswer(1);
        });

        // 뒤로가기 버튼
        btnBack.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                // 이전 질문으로
                currentQuestionIndex--;
                userAnswers.remove(userAnswers.size() - 1); // 마지막 답변 제거
                displayQuestion();
            } else {
                // 첫 번째 질문이면 이전 화면으로
                finish();
            }
        });
    }

    /**
     * 답변 선택 처리
     */
    private void selectAnswer(int answerIndex) {
        // 현재 표시 중인 질문의 실제 인덱스 가져오기
        int actualQuestionIndex = selectedQuestionIndices.get(currentQuestionIndex);

        // 답변 저장 (질문인덱스:선택한답변인덱스 형식)
        userAnswers.add(actualQuestionIndex + ":" + answerIndex);

        // 버튼 애니메이션
        Button selectedButton = answerIndex == 0 ? btnAnswer1 : btnAnswer2;
        animateButton(selectedButton);

        // 다음 질문으로 이동 (딜레이 후)
        selectedButton.postDelayed(() -> {
            if (currentQuestionIndex < QUESTIONS_TO_SHOW - 1) {
                currentQuestionIndex++;
                displayQuestion();
            } else {
                // 모든 질문 완료 - 결과 화면으로
                showResults();
            }
        }, 200);
    }

    /**
     * 질문 표시
     */
    private void displayQuestion() {
        if (currentQuestionIndex < selectedQuestionIndices.size()) {
            // 실제 질문 인덱스 가져오기
            int actualQuestionIndex = selectedQuestionIndices.get(currentQuestionIndex);

            // 질문 번호 및 진행률 업데이트
            tvQuestionNumber.setText((currentQuestionIndex + 1) + "/" + QUESTIONS_TO_SHOW);
            progressBar.setProgress(currentQuestionIndex + 1);

            // 질문 및 답변 옵션 표시
            tvQuestion.setText(allQuestions.get(actualQuestionIndex));
            btnAnswer1.setText(allAnswerOptions.get(actualQuestionIndex)[0]);
            btnAnswer2.setText(allAnswerOptions.get(actualQuestionIndex)[1]);

            // 질문 애니메이션
            animateQuestionEntry();
        }
    }

    /**
     * 질문 등장 애니메이션
     */
    private void animateQuestionEntry() {
        questionContainer.setTranslationX(100f);
        questionContainer.setAlpha(0f);
        questionContainer.animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(300)
                .start();
    }

    /**
     * 버튼 클릭 애니메이션
     */
    private void animateButton(Button button) {
        button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    button.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    /**
     * 초기 애니메이션 설정
     */
    private void setupAnimations() {
        // 프로그레스바 애니메이션
        progressBar.setAlpha(0f);
        progressBar.animate()
                .alpha(1f)
                .setDuration(500)
                .start();
    }

    /**
     * 결과 화면으로 이동
     */
    private void showResults() {
        showToast("테스트 완료! 결과 분석 중...");

        // ResultActivity로 이동
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("category", selectedCategory);
        intent.putStringArrayListExtra("answers", userAnswers);
        intent.putIntegerArrayListExtra("questionIndices", selectedQuestionIndices);
        startActivity(intent);
        finish(); // 질문 화면 종료
    }

    /**
     * 토스트 메시지 표시
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (currentQuestionIndex > 0) {
            // 이전 질문으로
            currentQuestionIndex--;
            userAnswers.remove(userAnswers.size() - 1);
            displayQuestion();
        } else {
            super.onBackPressed();
        }
    }
}