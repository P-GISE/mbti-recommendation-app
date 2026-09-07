package kr.ac.mjc.mbti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    // UI 컴포넌트
    private TextView tvCategory, tvResultTitle, tvResultSubtitle, tvCompatibility, tvResultDescription;
    private LinearLayout characteristicsList, captureArea;
    private Button btnScreenshot, btnShare, btnRestart, btnHome, btnDetail;
    private ImageView ivTeamLogo;

    // 데이터
    private String selectedCategory;
    private ArrayList<String> userAnswers;
    private ArrayList<Integer> questionIndices;

    // 성향 점수 저장용
    private Map<String, Integer> traitScores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Intent에서 데이터 받기
        Intent intent = getIntent();
        selectedCategory = intent.getStringExtra("category");
        userAnswers = intent.getStringArrayListExtra("answers");
        questionIndices = intent.getIntegerArrayListExtra("questionIndices");

        // 뷰 초기화
        initViews();

        // 성향 분석
        analyzeTraits();

        // 결과 표시
        displayResult();

        // 버튼 리스너 설정
        setupButtonListeners();
    }

    private void initViews() {
        tvCategory = findViewById(R.id.tv_category);
        tvResultTitle = findViewById(R.id.tv_result_title);
        tvResultSubtitle = findViewById(R.id.tv_result_subtitle);
        tvCompatibility = findViewById(R.id.tv_compatibility);
        tvResultDescription = findViewById(R.id.tv_result_description);
        characteristicsList = findViewById(R.id.characteristics_list);
        captureArea = findViewById(R.id.capture_area);
        btnScreenshot = findViewById(R.id.btn_screenshot);
        btnShare = findViewById(R.id.btn_share);
        btnRestart = findViewById(R.id.btn_restart);
        btnHome = findViewById(R.id.btn_home);
        btnDetail = findViewById(R.id.btn_detail);
        if (btnDetail == null) {
            Log.e(TAG, "btn_detail 버튼이 레이아웃에 없습니다! activity_result.xml을 확인하세요.");
            Toast.makeText(this, "상세보기 버튼이 레이아웃에 없습니다.", Toast.LENGTH_LONG).show();
        } else {
            btnDetail.setEnabled(false);
        }
        tvCategory.setText(selectedCategory + " 추천 결과");
        ivTeamLogo = findViewById(R.id.iv_team_logo);
    }

    /**
     * 성향 매핑 데이터
     */
    private Map<String, String[][][]> getCategoryTraitMappings() {
        Map<String, String[][][]> mappings = new HashMap<>();
        // 야구구단 매핑 (질문 10개 예시)
        mappings.put("야구구단", new String[][][]{
            {{"외향"}, {"내향"}}, // 1번 질문
            {{"감성"}, {"이성"}}, // 2번 질문
            {{"계획"}, {"무계획"}}, // 3번 질문
            {{"상상력"}, {"현실적"}}, // 4번 질문
            {{"외향"}, {"내향"}}, // 5번 질문
            {{"감성"}, {"이성"}}, // 6번 질문
            {{"계획"}, {"무계획"}}, // 7번 질문
            {{"상상력"}, {"현실적"}}, // 8번 질문
            {{"외향"}, {"내향"}}, // 9번 질문
            {{"감성"}, {"이성"}}, // 10번 질문
        });
        // 게임포지션 매핑 (질문 10개 예시)
        mappings.put("게임포지션", new String[][][]{
            {{"외향"}, {"내향"}},
            {{"감성"}, {"이성"}},
            {{"계획"}, {"무계획"}},
            {{"상상력"}, {"현실적"}},
            {{"이성"}, {"감성"}},
            {{"외향"}, {"내향"}},
            {{"계획"}, {"무계획"}},
            {{"상상력"}, {"현실적"}},
            {{"감성"}, {"이성"}},
            {{"외향"}, {"내향"}},
        });
        // 영화장르 매핑 (질문 10개 예시)
        mappings.put("영화장르", new String[][][]{
            {{"외향"}, {"내향"}},
            {{"감성"}, {"이성"}},
            {{"계획"}, {"무계획"}},
            {{"상상력"}, {"현실적"}},
            {{"이성"}, {"감성"}},
            {{"외향"}, {"내향"}},
            {{"계획"}, {"무계획"}},
            {{"상상력"}, {"현실적"}},
            {{"감성"}, {"이성"}},
            {{"외향"}, {"내향"}},
        });
        // 여행지 매핑 (질문 10개 예시)
        mappings.put("여행지", new String[][][]{
            {{"외향"}, {"내향"}},
            {{"감성"}, {"이성"}},
            {{"계획"}, {"무계획"}},
            {{"상상력"}, {"현실적"}},
            {{"이성"}, {"감성"}},
            {{"외향"}, {"내향"}},
            {{"계획"}, {"무계획"}},
            {{"상상력"}, {"현실적"}},
            {{"감성"}, {"이성"}},
            {{"외향"}, {"내향"}},
        });
        return mappings;
    }

    /**
     * 성향 분석
     */
    private void analyzeTraits() {
        traitScores.clear();
        String[] keys = {"외향","내향","감성","이성","계획","무계획","상상력","현실적"};
        for (String k : keys) traitScores.put(k, 0);

        String[][][] traitMappings = getCategoryTraitMappings().get(selectedCategory);
        if (traitMappings == null) {
            Log.e(TAG, "No mappings for category: " + selectedCategory);
            return;
        }

        for (String answer : userAnswers) {
            String[] parts = answer.split(":");
            int qIdx = Integer.parseInt(parts[0]);
            int opt = Integer.parseInt(parts[1]);
            if (qIdx < traitMappings.length) {
                for (String t : traitMappings[qIdx][opt]) {
                    t = t.trim();
                    if (traitScores.containsKey(t)) {
                        traitScores.put(t, traitScores.get(t) + 1);
                    }
                }
            }
        }
        Log.d(TAG, "Trait scores: " + traitScores);
    }

    /**
     * 결과 표시
     */
    private void displayResult() {
        String mainType = determineMainType();
        if (mainType == null || mainType.isEmpty()) {
            mainType = "내향-이성"; // 기본값 설정
        }

        // 카테고리별 결과 설정
        switch (selectedCategory) {
            case "야구구단": 
                displayBaseballResult(mainType); 
                break;
            case "게임포지션": 
                displayGameResult(mainType); 
                break;
            case "영화장르": 
                displayMovieResult(mainType); 
                break;
            case "여행지": 
                displayTravelResult(mainType); 
                break;
            default:
                tvResultTitle.setText("분석 결과 없음");
                tvResultSubtitle.setText("");
                tvResultDescription.setText("");
                tvCompatibility.setText("");
        }
        displayCharacteristics();
        
        // 결과가 표시된 후에만 상세보기 버튼 활성화
        if (btnDetail != null) btnDetail.setEnabled(true);
    }

    /**
     * 주요 성향 결정 (외향/내향 - 감성/이성)
     */
    private String determineMainType() {
        StringBuilder sb = new StringBuilder();
        
        // 외향/내향 결정
        sb.append(traitScores.get("외향") > traitScores.get("내향") ? "외향-" : "내향-");
        
        // 두 번째 성향 결정 (감성/이성, 계획/무계획, 상상력/현실적 중 가장 높은 점수 선택)
        int 감성점수 = traitScores.get("감성");
        int 이성점수 = traitScores.get("이성");
        int 계획점수 = traitScores.get("계획");
        int 무계획점수 = traitScores.get("무계획");
        int 상상력점수 = traitScores.get("상상력");
        int 현실적점수 = traitScores.get("현실적");
        
        int maxScore = Math.max(Math.max(감성점수, 이성점수), 
                      Math.max(Math.max(계획점수, 무계획점수),
                      Math.max(상상력점수, 현실적점수)));
        
        if (maxScore == 감성점수) sb.append("감성");
        else if (maxScore == 이성점수) sb.append("이성");
        else if (maxScore == 계획점수) sb.append("계획");
        else if (maxScore == 무계획점수) sb.append("무계획");
        else if (maxScore == 상상력점수) sb.append("상상력");
        else sb.append("현실적");
        
        return sb.toString();
    }

    private void displayBaseballResult(String type) {
        tvResultTitle.setText("당신에게 어울리는 야구구단");
        Map<String, String[]> map = new HashMap<>();
        map.put("외향-감성", new String[]{"LG 트윈스", "서울을 대표하는 인기 구단으로 열정적인 응원 문화와 단결된 팬심이 강점입니다.", "95"});
        map.put("외향-이성", new String[]{"키움 히어로즈", "분석 중심의 운영과 젊은 전력, MLB에 진출하는 선수가 많은 서울 고척돔 연고 구단입니다.", "92"});
        map.put("내향-감성", new String[]{"롯데 자이언츠", "부산의 상징이자 전국 최상위 팬덤을 가진 구단으로, 뜨거운 열기가 특징입니다.", "88"});
        map.put("내향-이성", new String[]{"SSG 랜더스", "신세계 그룹이 인수한 세련된 브랜드 이미지와 창의적 마케팅이 돋보이는 구단입니다.", "90"});
        map.put("외향-계획", new String[]{"KIA 타이거즈", "전통과 우승 DNA를 자랑하는 구단으로, 감성과 스토리에 몰입하는 팬층이 많습니다.", "89"});
        map.put("내향-계획", new String[]{"NC 다이노스", "창단 역사는 짧지만 데이터 기반의 운영과 실리 추구 전략이 강한 팀입니다.", "87"});
        map.put("외향-무계획", new String[]{"한화 이글스", "오랜 암흑기를 거쳤지만 팬의 충성도와 희망의 상징으로 유명한 구단입니다.", "86"});
        map.put("내향-무계획", new String[]{"KT 위즈", "젊고 유연한 운영, 부담 없는 응원 문화가 특징인 수원 연고 신생 구단입니다.", "85"});
        map.put("외향-상상력", new String[]{"삼성 라이온즈", "KBO 전통의 명문 팀으로 안정적인 운영과 리더십 기반 응원이 강점입니다.", "93"});
        map.put("내향-상상력", new String[]{"두산 베어스", "서울 연고의 명문 구단으로 전략적 운영과 실속 중심 팬층이 특징입니다.", "91"});
        
        String[] r = map.get(type);
        if (r != null) {
            tvResultSubtitle.setText(r[0]);
            tvResultDescription.setText(r[1]);
            tvCompatibility.setText("✨ 궁합도: " + r[2] + "%");
        } else {
            // 매핑되지 않은 type의 경우 기본값 설정
            tvResultSubtitle.setText("SSG 랜더스");
            tvResultDescription.setText("신세계 그룹이 인수한 세련된 브랜드 이미지와 창의적 마케팅이 돋보이는 구단입니다.");
            tvCompatibility.setText("✨ 궁합도: 90%");
        }

        // 구단명에 따라 이미지 변경
        String teamName = tvResultSubtitle.getText().toString();
        switch (teamName) {
            case "LG 트윈스":
                ivTeamLogo.setImageResource(R.drawable.lg_twins);
                break;
            case "키움 히어로즈":
                ivTeamLogo.setImageResource(R.drawable.kiwoom_heros);
                break;
            case "롯데 자이언츠":
                ivTeamLogo.setImageResource(R.drawable.lotte_giants);
                break;
            case "SSG 랜더스":
                ivTeamLogo.setImageResource(R.drawable.ssg_landers);
                break;
            case "KIA 타이거즈":
                ivTeamLogo.setImageResource(R.drawable.kia_tigers);
                break;
            case "NC 다이노스":
                ivTeamLogo.setImageResource(R.drawable.nc_dinos);
                break;
            case "한화 이글스":
                ivTeamLogo.setImageResource(R.drawable.hanwha_eagles);
                break;
            case "KT 위즈":
                ivTeamLogo.setImageResource(R.drawable.kt_wiz);
                break;
            case "삼성 라이온즈":
                ivTeamLogo.setImageResource(R.drawable.samsung_lions);
                break;
            case "두산 베어스":
                ivTeamLogo.setImageResource(R.drawable.dosan_bears);
                break;
            default:
                ivTeamLogo.setImageResource(R.drawable.ssg_landers); // 기본값
        }
    }

    private void displayGameResult(String type) {
        tvResultTitle.setText("당신에게 어울리는 게임 포지션");
        Map<String, String[]> map = new HashMap<>();
        map.put("외향-감성", new String[]{"원딜 (ADC)", "팀의 후방에서 강력한 딜링을 담당하는 포지션. 주로 원거리 공격수로, 팀의 핵심 화력원입니다.", "93"});
        map.put("외향-이성", new String[]{"정글", "맵 전체를 누비며 오브젝트 관리, 갱킹, 팀원 지원을 담당하는 포지션. 전략적 사고와 판단력이 중요합니다.", "91"});
        map.put("내향-감성", new String[]{"서포터", "팀원 보호와 시야 장악, 전략적 지원을 담당하는 포지션. 팀의 안정감을 책임집니다.", "94"});
        map.put("내향-이성", new String[]{"미드", "게임의 중심에서 다양한 역할을 수행하는 포지션. 창의적이고 변칙적인 플레이가 강점입니다.", "89"});
        map.put("외향-계획", new String[]{"탑", "한 라인에서 독립적으로 성장하며, 팀의 전투를 뒷받침하는 포지션. 꾸준함과 현실적인 운영이 중요합니다.", "88"});
        map.put("내향-계획", new String[]{"정글", "맵 전체를 누비며 오브젝트 관리, 갱킹, 팀원 지원을 담당하는 포지션. 전략적 사고와 판단력이 중요합니다.", "90"});
        map.put("외향-무계획", new String[]{"원딜 (ADC)", "팀의 후방에서 강력한 딜링을 담당하는 포지션. 주로 원거리 공격수로, 팀의 핵심 화력원입니다.", "92"});
        map.put("내향-무계획", new String[]{"서포터", "팀원 보호와 시야 장악, 전략적 지원을 담당하는 포지션. 팀의 안정감을 책임집니다.", "87"});
        map.put("외향-상상력", new String[]{"미드", "게임의 중심에서 다양한 역할을 수행하는 포지션. 창의적이고 변칙적인 플레이가 강점입니다.", "91"});
        map.put("내향-상상력", new String[]{"탑", "한 라인에서 독립적으로 성장하며, 팀의 전투를 뒷받침하는 포지션. 꾸준함과 현실적인 운영이 중요합니다.", "89"});
        String[] r = map.get(type);
        if (r != null) {
            tvResultSubtitle.setText(r[0]);
            tvResultDescription.setText(r[1]);
            tvCompatibility.setText("✨ 궁합도: " + r[2] + "%");
        }

        // 포지션명에 따라 이미지 변경
        String position = tvResultSubtitle.getText().toString();
        if (ivTeamLogo != null) {
            switch (position) {
                case "원딜 (ADC)":
                    ivTeamLogo.setImageResource(R.drawable.bottom);
                    break;
                case "정글":
                    ivTeamLogo.setImageResource(R.drawable.jungle);
                    break;
                case "서포터":
                    ivTeamLogo.setImageResource(R.drawable.support);
                    break;
                case "탑":
                    ivTeamLogo.setImageResource(R.drawable.top);
                    break;
                case "미드":
                    ivTeamLogo.setImageResource(R.drawable.mid);
                    break;
                default:
                    ivTeamLogo.setImageResource(R.drawable.bottom); // 기본값
            }
        }
    }

    private void displayMovieResult(String type) {
        tvResultTitle.setText("당신에게 어울리는 영화 장르");
        Map<String, String[]> map = new HashMap<>();
        map.put("외향-감성", new String[]{"로맨틱 코미디", "사랑과 웃음이 어우러진 가볍고 즐거운 이야기. 발랄한 분위기와 공감되는 관계 중심 서사가 특징입니다.", "96"});
        map.put("외향-이성", new String[]{"액션", "속도감 있고 물리적인 긴장감이 핵심. 복잡한 생각 없이 몰입하기 좋은 장르입니다.", "92"});
        map.put("내향-감성", new String[]{"드라마", "인물의 감정과 현실을 섬세하게 담아낸 서사가 중심. 몰입감 있고 깊이 있는 전개가 특징입니다.", "95"});
        map.put("내향-이성", new String[]{"SF", "과학, 기술, 미래 등을 배경으로 한 가상의 설정. 논리적 사고와 창의적 상상력이 조화를 이룹니다.", "90"});
        map.put("외향-계획", new String[]{"범죄 스릴러", "치밀한 사건 전개와 심리 묘사가 특징. 현실적이고 논리적인 사건 중심입니다.", "93"});
        map.put("내향-계획", new String[]{"심리 스릴러", "인물의 내면과 감정의 파고를 따라가는 긴장감 높은 이야기입니다.", "91"});
        map.put("외향-무계획", new String[]{"애니메이션", "감성과 창의력이 조화를 이루는 영상 예술. 나이와 상관없이 감동을 줍니다.", "94"});
        map.put("내향-무계획", new String[]{"힐링 영화", "특별한 사건보다 정서적 위안과 평온한 분위기를 중심으로 하는 영화입니다.", "89"});
        map.put("외향-상상력", new String[]{"판타지", "현실 너머의 세계관을 배경으로 한 상상 가득한 이야기입니다.", "92"});
        map.put("내향-상상력", new String[]{"추리물", "범인을 찾거나 사건의 진실을 파헤치는 구조. 트릭과 반전이 핵심입니다.", "90"});
        String[] r = map.get(type);
        if (r != null) {
            tvResultSubtitle.setText(r[0]);
            tvResultDescription.setText(r[1]);
            tvCompatibility.setText("✨ 궁합도: " + r[2] + "%");
        }

        // 장르명에 따라 이미지 변경
        String genre = tvResultSubtitle.getText().toString();
        if (ivTeamLogo != null) {
            switch (genre) {
                case "로맨틱 코미디":
                    ivTeamLogo.setImageResource(R.drawable.romantic);
                    break;
                case "액션":
                    ivTeamLogo.setImageResource(R.drawable.action);
                    break;
                case "드라마":
                    ivTeamLogo.setImageResource(R.drawable.drama);
                    break;
                case "SF":
                    ivTeamLogo.setImageResource(R.drawable.sf);
                    break;
                case "범죄 스릴러":
                    ivTeamLogo.setImageResource(R.drawable.thriller);
                    break;
                case "심리 스릴러":
                    ivTeamLogo.setImageResource(R.drawable.psychothriller);
                    break;
                case "애니메이션":
                    ivTeamLogo.setImageResource(R.drawable.animation);
                    break;
                case "힐링 영화":
                    ivTeamLogo.setImageResource(R.drawable.healing);
                    break;
                case "판타지":
                    ivTeamLogo.setImageResource(R.drawable.fantasy);
                    break;
                case "추리물":
                    ivTeamLogo.setImageResource(R.drawable.mystery);
                    break;
                default:
                    ivTeamLogo.setImageResource(R.drawable.romantic); // 기본값
            }
        }
    }

    private void displayTravelResult(String type) {
        tvResultTitle.setText("당신에게 어울리는 여행지");
        Map<String, String[]> map = new HashMap<>();
        map.put("외향-감성", new String[]{"오사카", "일본의 유쾌한 도시. 도톤보리와 다양한 거리 음식, 독특한 문화로 여행객을 매료시킵니다.", "94"});
        map.put("외향-이성", new String[]{"싱가포르", "다양한 문화가 융합된 도시국가로, 위생과 치안이 우수하고 동남아 여행 초심자에게 적합합니다.", "91"});
        map.put("내향-감성", new String[]{"일본 교토", "전통 사찰과 고즈넉한 골목길이 매력적인 도시. 벚꽃과 단풍철에는 감성적인 풍경이 절정을 이룹니다.", "97"});
        map.put("내향-이성", new String[]{"베를린", "예술과 역사, 현대문화가 공존하는 도시. 각 명소의 상징성과 맥락을 함께 체험할 수 있습니다.", "93"});
        map.put("외향-계획", new String[]{"전주", "전통과 한옥이 살아있는 도시. 전통음식, 체험, 사진 찍기 좋은 포인트가 많습니다.", "90"});
        map.put("내향-계획", new String[]{"경주", "한국 고대 유적이 밀집된 도시. 걸으며 배우는 역사 여행에 적합합니다.", "92"});
        map.put("외향-무계획", new String[]{"제주도", "자연과 카페, 바다가 어우러진 섬. 어디를 가도 힐링 포인트가 넘치는 국내 대표 감성 여행지입니다.", "95"});
        map.put("내향-무계획", new String[]{"방콕", "활기찬 분위기와 예측 불가능한 매력이 공존하는 동남아의 중심 도시입니다.", "88"});
        map.put("외향-상상력", new String[]{"바르셀로나", "열정적인 문화와 예술의 도시. 건축과 예술이 어우러진 독특한 매력이 있습니다.", "93"});
        map.put("내향-상상력", new String[]{"인터라켄", "자연과 질서가 조화를 이루는 스위스의 아름다운 도시입니다.", "91"});
        String[] r = map.get(type);
        if (r != null) {
            tvResultSubtitle.setText(r[0]);
            tvResultDescription.setText(r[1]);
            tvCompatibility.setText("✨ 궁합도: " + r[2] + "%");
        }

        // 여행지명에 따라 이미지 변경
        String place = tvResultSubtitle.getText().toString();
        if (ivTeamLogo != null) {
            switch (place) {
                case "오사카":
                    ivTeamLogo.setImageResource(R.drawable.osaka);
                    break;
                case "싱가포르":
                    ivTeamLogo.setImageResource(R.drawable.singapore);
                    break;
                case "일본 교토":
                    ivTeamLogo.setImageResource(R.drawable.kyoto);
                    break;
                case "베를린":
                    ivTeamLogo.setImageResource(R.drawable.berlin);
                    break;
                case "전주":
                    ivTeamLogo.setImageResource(R.drawable.jeounju);
                    break;
                case "경주":
                    ivTeamLogo.setImageResource(R.drawable.gyeoungju);
                    break;
                case "제주도":
                    ivTeamLogo.setImageResource(R.drawable.jeju);
                    break;
                case "방콕":
                    ivTeamLogo.setImageResource(R.drawable.bangkok);
                    break;
                case "바르셀로나":
                    ivTeamLogo.setImageResource(R.drawable.barcelona);
                    break;
                case "인터라켄":
                    ivTeamLogo.setImageResource(R.drawable.interlaken);
                    break;
                default:
                    ivTeamLogo.setImageResource(R.drawable.osaka); // 기본값
            }
            // 여행지 카테고리일 때만 크기 조정 (200dp x 150dp)
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivTeamLogo.getLayoutParams();
            params.width = dpToPx(200);
            params.height = dpToPx(150);
            ivTeamLogo.setLayoutParams(params);
        }
    }

    /** 주요 특성 표시 */
    private void displayCharacteristics() {
        characteristicsList.removeAllViews();
        addCharacteristic(traitScores.get("외향") > traitScores.get("내향") ? "• 활발하고 사교적인 성향" : "• 차분하고 내향적인 성향");
        addCharacteristic(traitScores.get("감성") > traitScores.get("이성") ? "• 따뜻하고 감정적인 성격" : "• 논리적이고 분석적인 성격");
        addCharacteristic(traitScores.get("계획") > traitScores.get("무계획") ? "• 체계적이고 계획적인 생활" : "• 즉흥적이고 자유로운 생활");
        addCharacteristic(traitScores.get("상상력") > traitScores.get("현실적") ? "• 창의적이고 상상력이 풍부함" : "• 실용적이고 현실적인 성향");
    }

    private void addCharacteristic(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(14);
        characteristicsList.addView(tv);
    }

    /** 버튼 리스너 설정 */
    private void setupButtonListeners() {
        // 스크린샷 버튼 클릭 리스너
        btnScreenshot.setOnClickListener(v -> {
            takeScreenshot();
            // 스크린샷 후 공유 다이얼로그 표시
            showShareDialog();
        });

        // 결과 공유 버튼 클릭 리스너
        btnShare.setOnClickListener(v -> {
            showShareOptionsDialog();
        });

        // 나머지 버튼 리스너들...
        btnRestart.setOnClickListener(v -> {
            Intent i = new Intent(this, QuestionActivity.class);
            i.putExtra("category", selectedCategory);
            startActivity(i); finish();
        });
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoryActivity.class)); finish();
        });
        if (btnDetail != null) {
            btnDetail.setOnClickListener(v -> {
                Log.d(TAG, "상세보기 버튼 클릭됨");
                if (!btnDetail.isEnabled()) {
                    Toast.makeText(this, "결과가 준비 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent detailIntent = new Intent(ResultActivity.this, DetailActivity.class);
                    
                    // null-safe 처리 추가
                    String category = selectedCategory != null ? selectedCategory : "";
                    String title = tvResultSubtitle != null ? tvResultSubtitle.getText().toString() : "";
                    String info = tvResultDescription != null ? tvResultDescription.getText().toString() : "";
                    
                    detailIntent.putExtra("category", category);
                    detailIntent.putExtra("title", title);
                    detailIntent.putExtra("info", info);

                    // 성향 정보 구성
                    StringBuilder historyBuilder = new StringBuilder();
                    if (traitScores != null && !traitScores.isEmpty()) {
                        historyBuilder.append("• ").append(traitScores.getOrDefault("외향", 0) > traitScores.getOrDefault("내향", 0) ? "외향적" : "내향적");
                        historyBuilder.append("\n• ").append(traitScores.getOrDefault("감성", 0) > traitScores.getOrDefault("이성", 0) ? "감성적" : "이성적");
                        historyBuilder.append("\n• ").append(traitScores.getOrDefault("계획", 0) > traitScores.getOrDefault("무계획", 0) ? "계획적" : "즉흥적");
                        historyBuilder.append("\n• ").append(traitScores.getOrDefault("상상력", 0) > traitScores.getOrDefault("현실적", 0) ? "상상력 풍부" : "현실적");
                    }
                    detailIntent.putExtra("history", historyBuilder.toString());

                    // 특징 정보 구성
                    StringBuilder featureBuilder = new StringBuilder();
                    if (tvCompatibility != null) {
                        featureBuilder.append("• ").append(tvCompatibility.getText().toString());
                    }
                    if (tvResultDescription != null) {
                        featureBuilder.append("\n• ").append(tvResultDescription.getText().toString());
                    }
                    detailIntent.putExtra("feature", featureBuilder.toString());

                    // 구단별 추가 정보 구성
                    String playerInfo = "";
                    if (category.equals("야구구단") && !title.isEmpty()) {
                        playerInfo = "• 홈구장: " + getHomeStadium(title);
                    }
                    detailIntent.putExtra("player", playerInfo);

                    startActivity(detailIntent);
                } catch (Exception e) {
                    Log.e(TAG, "상세보기 이동 실패", e);
                    Toast.makeText(this, "상세보기 화면 이동에 실패했습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 스크린샷 공유 다이얼로그 표시
    private void showShareDialog() {
        new AlertDialog.Builder(this)
            .setTitle("스크린샷 공유")
            .setMessage("스크린샷을 공유하시겠습니까?")
            .setPositiveButton("공유하기", (dialog, which) -> {
                shareScreenshot();
            })
            .setNegativeButton("취소", null)
            .show();
    }

    // 공유 옵션 다이얼로그 표시
    private void showShareOptionsDialog() {
        String[] options = {"앱 공유하기", "결과 링크 공유하기", "텍스트로 결과 공유하기"};
        
        new AlertDialog.Builder(this)
            .setTitle("공유 방법 선택")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        shareApp();
                        break;
                    case 1:
                        shareResultLink();
                        break;
                    case 2:
                        shareResultText();
                        break;
                }
            })
            .show();
    }

    // 스크린샷 공유
    private void shareScreenshot() {
        try {
            View v = captureArea;
            v.setDrawingCacheEnabled(true);
            Bitmap bmp = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            v.draw(c);
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "mbti_share_" + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush(); fos.close();
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(share, "스크린샷 공유하기"));
        } catch (IOException e) {
            Log.e(TAG, "스크린샷 공유 실패", e);
            Toast.makeText(this, "스크린샷 공유 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 앱 공유
    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, "MBTI 테스트 앱을 다운로드하세요!\n" + appUrl);
            startActivity(Intent.createChooser(shareIntent, "앱 공유하기"));
        } catch (Exception e) {
            Log.e(TAG, "앱 공유 실패", e);
            Toast.makeText(this, "앱 공유 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 결과 링크 공유
    private void shareResultLink() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String resultText = String.format(
                "나의 MBTI 테스트 결과!\n\n" +
                "카테고리: %s\n" +
                "결과: %s\n" +
                "궁합도: %s\n\n" +
                "나도 테스트 해보세요!\n" +
                "https://play.google.com/store/apps/details?id=%s",
                selectedCategory,
                tvResultSubtitle.getText().toString(),
                tvCompatibility.getText().toString(),
                getPackageName()
            );
            shareIntent.putExtra(Intent.EXTRA_TEXT, resultText);
            startActivity(Intent.createChooser(shareIntent, "결과 링크 공유하기"));
        } catch (Exception e) {
            Log.e(TAG, "결과 링크 공유 실패", e);
            Toast.makeText(this, "결과 링크 공유 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 텍스트로 결과 공유
    private void shareResultText() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            StringBuilder resultText = new StringBuilder();
            resultText.append("나의 MBTI 테스트 결과!\n\n");
            resultText.append("카테고리: ").append(selectedCategory).append("\n");
            resultText.append("결과: ").append(tvResultSubtitle.getText().toString()).append("\n");
            resultText.append(tvCompatibility.getText().toString()).append("\n\n");
            resultText.append("특징:\n");
            resultText.append(tvResultDescription.getText().toString()).append("\n\n");
            resultText.append("성향 키워드:\n");
            for (int i = 0; i < characteristicsList.getChildCount(); i++) {
                View child = characteristicsList.getChildAt(i);
                if (child instanceof TextView) {
                    resultText.append(((TextView) child).getText().toString()).append("\n");
                }
            }
            shareIntent.putExtra(Intent.EXTRA_TEXT, resultText.toString());
            startActivity(Intent.createChooser(shareIntent, "결과 텍스트 공유하기"));
        } catch (Exception e) {
            Log.e(TAG, "결과 텍스트 공유 실패", e);
            Toast.makeText(this, "결과 텍스트 공유 실패", Toast.LENGTH_SHORT).show();
        }
    }

    /** 스크린샷 */
    private void takeScreenshot() {
        try {
            View v = captureArea;
            v.setDrawingCacheEnabled(true);
            Bitmap bmp = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            v.draw(c);
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "mbti_result_" + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush(); fos.close();
            Toast.makeText(this, "스크린샷이 저장되었습니다!", Toast.LENGTH_SHORT).show();
            Intent scan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scan.setData(Uri.fromFile(file)); sendBroadcast(scan);
        } catch (IOException e) {
            Log.e(TAG, "스크린샷 실패", e);
            Toast.makeText(this, "스크린샷 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 홈구장 정보 반환
    private String getHomeStadium(String teamName) {
        Map<String, String> stadiums = new HashMap<>();
        stadiums.put("LG 트윈스", "잠실야구장");
        stadiums.put("키움 히어로즈", "고척스카이돔");
        stadiums.put("롯데 자이언츠", "사직야구장");
        stadiums.put("SSG 랜더스", "인천SSG랜더스필드");
        stadiums.put("KIA 타이거즈", "광주기아챔피언스필드");
        stadiums.put("NC 다이노스", "창원NC파크");
        stadiums.put("한화 이글스", "대전한화생명이글스파크");
        stadiums.put("KT 위즈", "수원KT위즈파크");
        stadiums.put("삼성 라이온즈", "대구삼성라이온즈파크");
        stadiums.put("두산 베어스", "잠실야구장");
        return stadiums.getOrDefault(teamName, "정보 없음");
    }

    // 게임 포지션 역할 설명
    private String getRoleDescription(String position) {
        Map<String, String> roles = new HashMap<>();
        roles.put("원딜 (ADC)", "팀의 후방에서 안정적인 딜링을 담당");
        roles.put("정글", "맵 전체를 누비며 오브젝트 관리와 갱킹 담당");
        roles.put("서포터", "팀원 보호와 시야 장악을 담당");
        roles.put("미드", "게임의 중심에서 다양한 역할 수행");
        roles.put("탑", "한 라인에서 독립적으로 성장하며 팀 전투 지원");
        return roles.getOrDefault(position, "정보 없음");
    }

    // 영화 장르 추천 작품
    private String getMovieRecommendations(String genre) {
        Map<String, String> movies = new HashMap<>();
        movies.put("로맨틱 코미디", "러브 액츄얼리, 10 Things I Hate About You");
        movies.put("액션", "존 윅, 미션 임파서블");
        movies.put("드라마", "벌새, 리틀 포레스트");
        movies.put("SF", "인터스텔라, 인셉션");
        movies.put("범죄 스릴러", "살인의 추억, 세븐");
        movies.put("심리 스릴러", "블랙 스완, 프라이멀 피어");
        movies.put("애니메이션", "센과 치히로의 행방불명, 코코");
        movies.put("힐링 영화", "월터의 상상은 현실이 된다, 카모메 식당");
        movies.put("판타지", "해리포터, 반지의 제왕");
        movies.put("추리물", "셜록 홈즈, 나이브스 아웃");
        return movies.getOrDefault(genre, "정보 없음");
    }

    // 여행지 추천 코스
    private String getTravelCourse(String destination) {
        Map<String, String> courses = new HashMap<>();
        courses.put("오사카", "도톤보리 → 쿠로몬시장 → 유니버셜스튜디오");
        courses.put("싱가포르", "마리나베이샌즈 → 가든스바이더베이 → 클락키 야경");
        courses.put("일본 교토", "아라시야마 대나무숲 → 기요미즈데라 → 기온 거리");
        courses.put("베를린", "브란덴부르크문 → 베를린장벽 → 페르가몬 박물관");
        courses.put("전주", "경기전 → 전주한옥마을 → 풍남문 야경");
        courses.put("경주", "대릉원 → 불국사 → 황리단길");
        courses.put("제주도", "협재해수욕장 → 동백포레스트 → 용눈이오름");
        courses.put("방콕", "왕궁 → 카오산로드 → 아속 야시장");
        courses.put("바르셀로나", "사그라다 파밀리아 → 구엘 공원 → 라 람블라");
        courses.put("인터라켄", "유능호수 → 하르더 클룸 → 슈피츠");
        return courses.getOrDefault(destination, "정보 없음");
    }

    // 추천 챔피언 정보 반환
    private String getRecommendedChampion(String position) {
        Map<String, String> champions = new HashMap<>();
        champions.put("원딜 (ADC)", "이즈리얼, 진, 칼리스타");
        champions.put("정글", "리 신, 비에고, 엘리스");
        champions.put("서포터", "레오나, 쓰레쉬, 유미");
        champions.put("미드", "아리, 오리아나, 신드라");
        champions.put("탑", "카밀, 나서스, 레넥톤");
        return champions.getOrDefault(position, "정보 없음");
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
