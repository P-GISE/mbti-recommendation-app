package kr.ac.mjc.mbti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvInfo;
    private Button btnPrevious;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 뷰 초기화
        initViews();

        // Intent에서 데이터 받기
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String title = intent.getStringExtra("title");
        String info = intent.getStringExtra("info");
        String history = intent.getStringExtra("history");
        String feature = intent.getStringExtra("feature");
        String player = intent.getStringExtra("player");

        // null-safe 처리 및 기본값 설정
        category = category != null ? category : "카테고리";
        title = title != null ? title : "제목 없음";
        info = info != null ? info : "정보 없음";
        history = history != null ? history : "성향 정보 없음";
        feature = feature != null ? feature : "특징 정보 없음";
        player = player != null ? player : "추가 정보 없음";

        // 데이터 표시
        tvTitle.setText(category + " 상세 정보");
        tvInfo.setText(String.format("%s\n\n%s\n\n%s\n\n%s", 
            title, info, history, feature, player));

        // 버튼 리스너 설정
        setupButtonListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvInfo = findViewById(R.id.tv_info);
        btnPrevious = findViewById(R.id.btn_previous);
        btnHome = findViewById(R.id.btn_home);
    }

    private void setupButtonListeners() {
        // 이전 버튼
        btnPrevious.setOnClickListener(v -> finish());

        // 홈 버튼
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}