package kr.ac.mjc.mbti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // 뷰 초기화
            initViews();

            // 클릭 리스너 설정
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "앱 초기화 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 뷰 초기화
     */
    private void initViews() {
        // btn_start 관련 코드 삭제
    }

    /**
     * 클릭 리스너 설정
     */
    private void setupClickListeners() {
        // 각 카드뷰에 클릭 리스너 설정
        findViewById(R.id.card_baseball).setOnClickListener(v -> startCategory("야구구단"));
        findViewById(R.id.card_game).setOnClickListener(v -> startCategory("게임포지션"));
        findViewById(R.id.card_movie).setOnClickListener(v -> startCategory("영화장르"));
        findViewById(R.id.card_travel).setOnClickListener(v -> startCategory("여행지"));
    }

    private void startCategory(String category) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}